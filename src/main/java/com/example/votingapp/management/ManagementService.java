package com.example.votingapp.management;

import com.example.votingapp.shared.Poll;
import com.example.votingapp.shared.PollStore;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ManagementService {
    private final PollStore pollStore;

    public ManagementService(PollStore pollStore) {
        this.pollStore = pollStore;
    }

    public Poll createPoll(String question, List<String> options) {
        return pollStore.addPoll(question, options);
    }

    public List<Poll> getAllPolls() {
        return pollStore.getAllPolls();
    }

    public void deletePoll(Long pollId) {
        pollStore.deletePoll(pollId);
    }
}
