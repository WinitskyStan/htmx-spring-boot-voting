package com.example.votingapp.voting;

import com.example.votingapp.shared.Poll;
import com.example.votingapp.shared.PollStore;
import org.springframework.stereotype.Service;

@Service
public class VotingService {
    private final PollStore pollStore;

    public VotingService(PollStore pollStore) {
        this.pollStore = pollStore;
    }

    public Poll getPoll(Long pollId) {
        return pollStore.getPollById(pollId).orElse(null);
    }

    public void submitVote(String sessionId, Long pollId, Long optionId) {
        pollStore.addVote(sessionId, pollId, optionId);
    }

    public boolean hasUserVoted(String sessionId, Long pollId) {
        return pollStore.hasVoted(sessionId, pollId);
    }

    public Long getUserVotedOptionId(String sessionId, Long pollId) {
        return pollStore.getUserVoteOptionId(sessionId, pollId).orElse(null);
    }
}
