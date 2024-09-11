package com.talhaunal.backend.service;

import com.talhaunal.backend.controller.response.MenteeResponse;
import com.talhaunal.backend.controller.response.MentorResponse;
import com.talhaunal.backend.repository.PhaseRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class PhaseReminderService {

    private final JavaMailSender mailSender;
    private final PhaseRepository phaseRepository;
    private final MentorService mentorService;

    public PhaseReminderService(JavaMailSender mailSender,
                                PhaseRepository phaseRepository,
                                MentorService mentorService) {
        this.mailSender = mailSender;
        this.phaseRepository = phaseRepository;
        this.mentorService = mentorService;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    void reminderPhase() {
        Instant oneHourBefore = Instant.now().minus(1, ChronoUnit.HOURS);
        var phases = phaseRepository.findAllByEndDateBetween(
                oneHourBefore.minus(1, ChronoUnit.MINUTES),
                oneHourBefore);
        phases.forEach(phase -> {
            MentorResponse mentorResponse = mentorService.getResponse(phase.getMentorship().getMentor());
            MenteeResponse menteeResponse = mentorService.getMenteeResponse(phase.getMentorship().getMenteeId());
            sendMail(mentorResponse.getMail(), phase.getName());
            sendMail(menteeResponse.getMail(), phase.getName());
        });
    }

    public void sendMail(String to, String phaseName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Phase Reminder");
        message.setText("1 hour to the end of the phase named " + phaseName + " in your ongoing mentorship");
        mailSender.send(message);
    }
}