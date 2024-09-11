package com.talhaunal.backend.service;

import com.talhaunal.backend.controller.request.PhaseCompletionRequest;
import com.talhaunal.backend.controller.request.PhaseCreateRequest;
import com.talhaunal.backend.controller.request.PhaseDetailRequest;
import com.talhaunal.backend.controller.response.PhaseResponse;
import com.talhaunal.backend.domain.Mentorship;
import com.talhaunal.backend.domain.Phase;
import com.talhaunal.backend.domain.enums.MentorshipStatus;
import com.talhaunal.backend.domain.enums.PhaseStatus;
import com.talhaunal.backend.exception.BusinessException;
import com.talhaunal.backend.exception.DomainNotFoundException;
import com.talhaunal.backend.repository.MentorshipRepository;
import com.talhaunal.backend.repository.PhaseRepository;
import com.talhaunal.backend.util.AuthUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PhaseService {

    private final PhaseRepository phaseRepository;
    private final MentorshipRepository mentorshipRepository;

    public PhaseService(PhaseRepository phaseRepository, MentorshipRepository mentorshipRepository) {
        this.phaseRepository = phaseRepository;
        this.mentorshipRepository = mentorshipRepository;
    }

    @Transactional
    public void create(PhaseCreateRequest request) {
        Mentorship mentorship = mentorshipRepository.findById(request.getMentorshipId())
                .orElseThrow(() -> new DomainNotFoundException("Mentorship not found"));
        String userId = AuthUtils.getAuthenticatedUserId();
        if (!userId.equals(mentorship.getMentor().getUserId()) && !userId.equals(mentorship.getMenteeId())) {
            throw new BusinessException("You are not part of this mentorship");
        }
        if(findFirstByMentorshipId(request.getMentorshipId()).isPresent()) {
            throw new BusinessException("Phases already planned for this mentorship");
        }

        List<PhaseDetailRequest> phaseDetails = request.getPhaseDetails();
        validatePhaseEndDates(phaseDetails);

        for (int i = 0; i < phaseDetails.size(); i++) {
            Phase phase = getPhase(phaseDetails, i, mentorship);
            phaseRepository.save(phase);
        }
        mentorship.setStatus(MentorshipStatus.IN_PROGRESS);
        mentorshipRepository.save(mentorship);
    }

    private void validatePhaseEndDates(List<PhaseDetailRequest> phaseDetails) {
        Instant now = Instant.now();

        for (int i = 0; i < phaseDetails.size(); i++) {
            Instant endDate = phaseDetails.get(i).getEndDate();

            if (endDate != null && endDate.isBefore(now)) {
                throw new BusinessException("Phase end date cannot be in the past.");
            }

            if (i > 0) {
                Instant previousEndDate = phaseDetails.get(i - 1).getEndDate();
                if (previousEndDate != null && endDate != null && previousEndDate.isAfter(endDate)) {
                    throw new BusinessException("Phases must have end dates in chronological order.");
                }
            }
        }
    }


    private static Phase getPhase(List<PhaseDetailRequest> phaseDetails, int i, Mentorship mentorship) {
        PhaseDetailRequest phaseDetailRequest = phaseDetails.get(i);
        if(phaseDetailRequest.getPriorityId()!=i+1){
            throw new DomainNotFoundException("Priority id mismatch");
        }

        Phase phase = new Phase();
        phase.setMentorship(mentorship);

        if (phaseDetailRequest.getPriorityId() == 1) {
            phase.setStatus(PhaseStatus.ACTIVE);
        } else {
            phase.setStatus(PhaseStatus.NOT_STARTED);
        }
        phase.setName(phaseDetailRequest.getName());
        phase.setPriorityId(phaseDetailRequest.getPriorityId());
        phase.setEndDate(phaseDetailRequest.getEndDate());
        return phase;
    }

    public void completion(Long id, PhaseCompletionRequest request) {
        Phase phase = findById(id);
        if(phase.getStatus() == PhaseStatus.NOT_STARTED) {
            throw new BusinessException("Phase has not started yet.");
        }
        Mentorship mentorship = phase.getMentorship();
        String userId = AuthUtils.getAuthenticatedUserId();
        if (!userId.equals(mentorship.getMentor().getUserId()) && !userId.equals(mentorship.getMenteeId())) {
            throw new BusinessException("You are not part of this mentorship");
        }

        if (phase.getStatus() == PhaseStatus.COMPLETED) {
            if (mentorship.getMenteeId().equals(userId)) {
                phase.setMenteeReview(request.getReview());
                phase.setMenteeRating(request.getRating());
            } else {
                phase.setMentorReview(request.getReview());
                phase.setMentorRating(request.getRating());
            }
            phaseRepository.save(phase);
            return;
        }

        if (mentorship.getMenteeId().equals(userId)) {
            phase.setMenteeReview(request.getReview());
            phase.setMenteeRating(request.getRating());
        } else {
            phase.setMentorReview(request.getReview());
            phase.setMentorRating(request.getRating());
        }
        phase.setStatus(PhaseStatus.COMPLETED);
        phaseRepository.save(phase);

        Optional<Phase> optionalPhase = phaseRepository.findAllByMentorshipIdOrderByPriorityId(mentorship.getId())
                .stream()
                .filter(it -> it.getStatus() == PhaseStatus.NOT_STARTED)
                .findFirst();
        if (optionalPhase.isPresent()) {
            Phase newPhase = optionalPhase.get();
            newPhase.setStatus(PhaseStatus.ACTIVE);
            phaseRepository.save(newPhase);
        } else {
            mentorship.setStatus(MentorshipStatus.COMPLETED);
            mentorshipRepository.save(mentorship);
        }
    }

    public List<PhaseResponse> get(Long mentorshipId){
        List<Phase> phases = phaseRepository.findAllByMentorshipIdOrderByPriorityId(mentorshipId);
        List<PhaseResponse> phaseResponses = new ArrayList<>();
        for (Phase phase : phases) {
            PhaseResponse phaseResponse = new PhaseResponse();
            phaseResponse.setId(phase.getId());
            phaseResponse.setName(phase.getName());
            phaseResponse.setPriorityId(phase.getPriorityId());
            phaseResponse.setEndDate(phase.getEndDate());
            phaseResponse.setStatus(phase.getStatus());
            phaseResponse.setMenteeRating(phase.getMenteeRating());
            phaseResponse.setMentorReview(phase.getMentorReview());
            phaseResponse.setMentorRating(phase.getMentorRating());
            phaseResponse.setMenteeReview(phase.getMenteeReview());


            phaseResponses.add(phaseResponse);

        }
        return phaseResponses;
    }

    public void save(Phase phase) {
        phaseRepository.save(phase);
    }


    public Optional<Phase> findFirstByMentorshipId(Long mentorshipId) {
        return phaseRepository.findAllByMentorshipIdOrderByPriorityId(mentorshipId).stream().findFirst();
    }

    private Phase findById(Long id) {
        return phaseRepository.findById(id).orElseThrow(() -> new DomainNotFoundException("Phase not found"));
    }
}
