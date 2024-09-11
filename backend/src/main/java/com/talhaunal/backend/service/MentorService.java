package com.talhaunal.backend.service;

import com.talhaunal.backend.controller.request.MentorCreateRequest;
import com.talhaunal.backend.controller.request.MentorStatusChangeRequest;
import com.talhaunal.backend.controller.response.MenteeResponse;
import com.talhaunal.backend.controller.response.MenteeResponseBuilder;
import com.talhaunal.backend.controller.response.MentorResponse;
import com.talhaunal.backend.domain.*;
import com.talhaunal.backend.domain.enums.MentorStatus;
import com.talhaunal.backend.domain.model.MentorDocument;
import com.talhaunal.backend.exception.BusinessException;
import com.talhaunal.backend.exception.DomainNotFoundException;
import com.talhaunal.backend.repository.MentorRepository;
import com.talhaunal.backend.repository.elasticsearch.MentorDocumentRepository;
import com.talhaunal.backend.util.AuthUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MentorService {

    private final MentorRepository mentorRepository;
    private final MentorDocumentRepository mentorDocumentRepository;
    private final TopicService topicService;
    private final SubtopicService subtopicService;
    private final LdapUserService ldapUserService;
    private final GoogleUserService googleUserService;

    public MentorService(MentorRepository mentorRepository,
                         MentorDocumentRepository mentorDocumentRepository,
                         TopicService topicService,
                         SubtopicService subtopicService,
                         LdapUserService ldapUserService,
                         GoogleUserService googleUserService) {
        this.mentorRepository = mentorRepository;
        this.mentorDocumentRepository = mentorDocumentRepository;
        this.topicService = topicService;
        this.subtopicService = subtopicService;
        this.ldapUserService = ldapUserService;
        this.googleUserService = googleUserService;
    }

    public void create(MentorCreateRequest request) {
        String userId = AuthUtils.getAuthenticatedUserId();

        Topic topic = topicService.findById(request.getTopicId());
        List<Subtopic> subtopics = subtopicService.findAllById(request.getSubtopicIds());
        Mentor mentor = Mentor.Builder.aMentor()
                .description(request.getDescription())
                .topic(topic)
                .subtopics(subtopics)
                .status(MentorStatus.HOLD)
                .userId(userId)
                .build();

        mentorRepository.save(mentor);
    }

    public List<MentorResponse> getMentorsByTopicAndSubtopics(Long topicId, List<Long> subtopicIds) {
        var mentors = mentorRepository.findByTopicAndSubtopicsAndStatus(topicId, subtopicIds, MentorStatus.APPROVED);
        return mentors.stream().map(this::getResponse).toList();
    }

    public List<MentorResponse> getAll() {
        List<MentorResponse> list = new ArrayList<>();
        for (Mentor mentor : mentorRepository.findAll()) {
            MentorResponse response = getResponse(mentor);
            list.add(response);
        }
        return list;
    }

    public MentorResponse getResponse(Long id) {
        Mentor mentor = findById(id);
        return getResponse(mentor);
    }

    public MentorResponse getResponse(Mentor mentor) {
        Optional<LdapUser> ldapUser = ldapUserService.findByUid(mentor.getUserId());
        if (ldapUser.isPresent()) {
            return createMentorResponse(mentor, ldapUser.get());
        }
        GoogleUser googleUser = googleUserService.getById(mentor.getUserId());
        return createMentorResponse(mentor, googleUser);
    }

    public MenteeResponse getMenteeResponse(String menteeId) {
        Optional<LdapUser> optionalLdapUser = ldapUserService.findByUid(menteeId);
        if (optionalLdapUser.isPresent()) {
            LdapUser ldapUser = optionalLdapUser.get();
            var fullName = ldapUser.getCommonName() + " " + ldapUser.getSurname();
            return MenteeResponseBuilder.aMenteeResponse()
                    .withFullName(fullName)
                    .withMail(ldapUser.getMail())
                    .build();
        }

        GoogleUser googleUser = googleUserService.getById(menteeId);
        var fullName = googleUser.getName() + " " + googleUser.getSurname();
        return MenteeResponseBuilder.aMenteeResponse()
                .withFullName(fullName)
                .withMail(googleUser.getEmail())
                .build();
    }

    public MentorResponse getResponseByUid(String uid) {
        Mentor mentor = mentorRepository.findByUserId(uid)
                .orElseThrow(() -> new DomainNotFoundException("Mentor not found"));
        LdapUser ldapUser = ldapUserService.getByUid(mentor.getUserId());

        return createMentorResponse(mentor, ldapUser);
    }

    public void changeStatus(Long id, MentorStatusChangeRequest request) {
        Mentor mentor = findById(id);
        MentorStatus status = request.getStatus();
        if(mentor.getStatus()!=MentorStatus.HOLD) {
            throw new BusinessException("Mentor status is not HOLD");
        }

        mentor.setStatus(status);
        mentorRepository.save(mentor);
        if (status == MentorStatus.APPROVED) {
            mentorDocumentRepository.save(mentor.toDocument());
        } else {
            mentorDocumentRepository.delete(mentor.toDocument());
        }
    }

    public void clearElastic() {
        mentorDocumentRepository.deleteAll();
    }

    public Mentor findById(Long id) {
        return mentorRepository.findById(id)
                .orElseThrow(() -> new DomainNotFoundException("Mentor not found"));
    }

    private MentorResponse createMentorResponse(Mentor mentor, LdapUser ldapUser) {
        return MentorResponse.MentorResponseBuilder.aMentorResponse()
                .withName(ldapUser.getCommonName())
                .withSurname(ldapUser.getSurname())
                .withStatus(mentor.getStatus())
                .withId(mentor.getId())
                .withUserId(mentor.getUserId())
                .withTopic(mentor.getTopic().getName())
                .withSubtopics(mentor.getSubtopics().stream().map(Subtopic::getName).toList())
                .withDescription(mentor.getDescription())
                .withMail(ldapUser.getMail())
                .build();
    }

    private MentorResponse createMentorResponse(Mentor mentor, GoogleUser googleUser) {
        return MentorResponse.MentorResponseBuilder.aMentorResponse()
                .withName(googleUser.getName())
                .withSurname(googleUser.getSurname())
                .withStatus(mentor.getStatus())
                .withId(mentor.getId())
                .withUserId(mentor.getUserId())
                .withTopic(mentor.getTopic().getName())
                .withSubtopics(mentor.getSubtopics().stream().map(Subtopic::getName).toList())
                .withDescription(mentor.getDescription())
                .withMail(googleUser.getEmail())
                .build();
    }

    public List<MentorResponse> searchMentorsByDescription(String query) {
        List<MentorDocument> mentorList = mentorDocumentRepository.findByDescriptionContaining(query);
        return mentorList.stream().map(mentor -> getResponse(mentor.getId())).collect(Collectors.toList());
    }
}
