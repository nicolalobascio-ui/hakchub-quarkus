package com.hackhub.services;

import com.hackhub.entity.Hackathon;
import com.hackhub.entity.User;
import com.hackhub.repository.HackathonRepository;
import com.hackhub.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ApplicationScoped
public class HackathonSubscriptionService {

    public enum ToggleStatus {
        SUBSCRIBED,
        UNSUBSCRIBED,
        CONFLICT_ALREADY_SUBSCRIBED_TO_ANOTHER
    }

    public record ToggleResult(ToggleStatus status, Hackathon hackathon) {}

    private final UserRepository userRepository;
    private final HackathonRepository hackathonRepository;

    public HackathonSubscriptionService(UserRepository userRepository,
                                       HackathonRepository hackathonRepository) {
        this.userRepository = userRepository;
        this.hackathonRepository = hackathonRepository;
    }

    @Transactional
    public Optional<Hackathon> getMySubscribedHackathon(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User non trovato: " + username));
                
        return Optional.ofNullable(user.getSubscribedHackathon());
    }

    @Transactional
    public ToggleResult toggleSubscription(String username, Long hackathonId) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User non trovato: " + username);
        }

        Hackathon target = hackathonRepository.findById(hackathonId);
        if (target == null) {
            throw new IllegalArgumentException("Hackathon non trovato: " + hackathonId);
        }
        Hackathon current = user.getSubscribedHackathon();

        if (current == null) {
            user.setSubscribedHackathon(target);
            return new ToggleResult(ToggleStatus.SUBSCRIBED, target);
        }

        if (current.getId().equals(target.getId())) {
            user.setSubscribedHackathon(null);
            return new ToggleResult(ToggleStatus.UNSUBSCRIBED, target);
        }

        return new ToggleResult(ToggleStatus.CONFLICT_ALREADY_SUBSCRIBED_TO_ANOTHER, current);
    }
}