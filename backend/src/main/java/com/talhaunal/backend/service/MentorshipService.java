package com.talhaunal.backend.service;

import com.talhaunal.backend.controller.response.MentorResponse;
import com.talhaunal.backend.controller.response.MentorshipResponse;
import com.talhaunal.backend.domain.Mentor;
import com.talhaunal.backend.domain.Mentorship;
import com.talhaunal.backend.domain.enums.MentorStatus;
import com.talhaunal.backend.domain.enums.MentorshipStatus;
import com.talhaunal.backend.exception.BusinessException;
import com.talhaunal.backend.exception.DomainNotFoundException;
import com.talhaunal.backend.repository.MentorshipRepository;
import com.talhaunal.backend.util.AuthUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MentorshipService {

    private final MentorshipRepository mentorshipRepository;
    private final MentorService mentorService;
    private final PhaseService phaseService;

    public MentorshipService(MentorshipRepository mentorshipRepository,
                             MentorService mentorService,
                             PhaseService phaseService) {
        this.mentorshipRepository = mentorshipRepository;
        this.mentorService = mentorService;
        this.phaseService = phaseService;
    }
    public MentorshipResponse getResponse(Long id) {
        Mentorship mentorship = findById(id);
        return createResponse(mentorship);
    }




    private Mentorship findById(Long mentorshipId) {
        return mentorshipRepository
                .findById(mentorshipId)
                .orElseThrow(() -> new DomainNotFoundException("mentorship not found"));
    }


    public boolean isMentor(Long mentorshipId) {
        String authenticatedUserId = AuthUtils.getAuthenticatedUserId();
        Mentorship mentorship = findById(mentorshipId);
        if (authenticatedUserId.equals(mentorship.getMentor().getUserId())) {
            return true;
        }
        if (authenticatedUserId.equals(mentorship.getMenteeId())) {
            return false;
        }
        throw new BusinessException("You are not part of this mentorship");
    }


    public void pickMentor(Long mentorId) {
        String menteeId = AuthUtils.getAuthenticatedUserId();
        Mentor mentor = mentorService.findById(mentorId);

        validatePickMentor(menteeId, mentor);

        Mentorship mentorship = new Mentorship();
        mentorship.setMentor(mentor);
        mentorship.setMenteeId(menteeId);
        mentorship.setStatus(MentorshipStatus.NOT_STARTED);
        mentorship.setStartDate(Instant.now());
        mentorshipRepository.save(mentorship);
    }

    private void validatePickMentor(String menteeId, Mentor mentor) {
        if (Objects.equals(menteeId, mentor.getUserId())) {
            throw new BusinessException("you can't be your own mentor");
        }
        if (mentor.getStatus().equals(MentorStatus.HOLD) || mentor.getStatus().equals(MentorStatus.REJECTED)) {
            throw new BusinessException("the mentor is not approved");
        }
        Optional<Mentorship> existingMentorship = mentorshipRepository.findByMenteeIdAndMentor_Topic_IdAndStatusIsNot(menteeId, mentor.getTopic().getId(), MentorshipStatus.COMPLETED);
        if (existingMentorship.isPresent()) {
            throw new BusinessException("You can only have one mentor from the same topic");
        }
        Integer mentorshipCount = mentorshipRepository.countByMentor_UserIdAndStatusIsNot(mentor.getUserId(), MentorshipStatus.COMPLETED);
        if (mentorshipCount >= 2) {
            throw new BusinessException("The mentor can have max 2 mentees");
        }
    }

    public List<MentorshipResponse> getMentorProcesses() {
        return getProcessesByUser(AuthUtils.getAuthenticatedUserId(), true);
    }

    public List<MentorshipResponse> getMenteeProcesses() {
        return getProcessesByUser(AuthUtils.getAuthenticatedUserId(), false);
    }

    private List<MentorshipResponse> getProcessesByUser(String userId, boolean isMentor) {
        List<Mentorship> mentorships = isMentor
                ? mentorshipRepository.findAllByMentor_UserId(userId)
                : mentorshipRepository.findAllByMenteeId(userId);

        return mentorships.stream()
                .map(this::createResponse)
                .collect(Collectors.toList());
    }

    private MentorshipResponse createResponse(Mentorship mentorship) {
        boolean isMentor = isMentor(mentorship.getId());
        MentorResponse mentorResponse = mentorService.getResponse(mentorship.getMentor());

        return MentorshipResponse.Builder.aMentorshipResponse()
                .mentorFlag(isMentor)
                .id(mentorship.getId())
                .startDate(mentorship.getStartDate())
                .menteeId(mentorship.getMenteeId())
                .mentorId(mentorship.getMentor().getId())
                .topic(mentorship.getMentor().getTopic().getName())
                .status(mentorship.getStatus().name())
                .mentorName(mentorResponse.getName() + " " + mentorResponse.getSurname())
                .menteeName(mentorService.getMenteeResponse(mentorship.getMenteeId()).getFullName()).build();
    }

}
