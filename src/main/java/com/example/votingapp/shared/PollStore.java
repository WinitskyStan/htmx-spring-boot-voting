package com.example.votingapp.shared;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class PollStore {
    private final List<Poll> polls = Collections.synchronizedList(new ArrayList<>());
    private final List<Vote> votes = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong pollIdCounter = new AtomicLong(1);
    private final AtomicLong optionIdCounter = new AtomicLong(1);

    // Poll operations
    public Poll addPoll(String question, List<String> optionTexts) {
        List<PollOption> options = new ArrayList<>();
        for (String text : optionTexts) {
            options.add(new PollOption(optionIdCounter.getAndIncrement(), text));
        }

        Poll poll = new Poll(pollIdCounter.getAndIncrement(), question, options);
        polls.add(poll);
        return poll;
    }

    public List<Poll> getAllPolls() {
        return new ArrayList<>(polls);
    }

    public Optional<Poll> getPollById(Long id) {
        return polls.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public void deletePoll(Long id) {
        polls.removeIf(p -> p.getId().equals(id));
        votes.removeIf(v -> v.getPollId().equals(id));
    }

    // Vote operations
    public void addVote(String sessionId, Long pollId, Long optionId) {
        votes.add(new Vote(sessionId, pollId, optionId));

        getPollById(pollId).ifPresent(poll -> {
            poll.getOptions().stream()
                    .filter(opt -> opt.getId().equals(optionId))
                    .findFirst()
                    .ifPresent(PollOption::incrementVoteCount);
        });
    }

    public boolean hasVoted(String sessionId, Long pollId) {
        return votes.stream()
                .anyMatch(v -> v.getSessionId().equals(sessionId) && v.getPollId().equals(pollId));
    }

    public Optional<Long> getUserVoteOptionId(String sessionId, Long pollId) {
        return votes.stream()
                .filter(v -> v.getSessionId().equals(sessionId) && v.getPollId().equals(pollId))
                .map(Vote::getOptionId)
                .findFirst();
    }

    public List<Vote> getVotesByPoll(Long pollId) {
        return new ArrayList<>(votes.stream()
                .filter(v -> v.getPollId().equals(pollId))
                .toList());
    }
}
