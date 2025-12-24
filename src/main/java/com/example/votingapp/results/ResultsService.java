package com.example.votingapp.results;

import com.example.votingapp.shared.Poll;
import com.example.votingapp.shared.PollOption;
import com.example.votingapp.shared.PollStore;
import org.springframework.stereotype.Service;

@Service
public class ResultsService {
    private final PollStore pollStore;

    public ResultsService(PollStore pollStore) {
        this.pollStore = pollStore;
    }

    public Poll getPoll(Long pollId) {
        return pollStore.getPollById(pollId).orElse(null);
    }

    public int getTotalVotes(Long pollId) {
        Poll poll = getPoll(pollId);
        if (poll == null) return 0;
        return poll.getOptions().stream()
                .mapToInt(PollOption::getVoteCount)
                .sum();
    }

    public void calculatePercentages(Poll poll) {
        if (poll == null) return;
        int totalVotes = getTotalVotes(poll.getId());

        if (totalVotes == 0) {
            poll.getOptions().forEach(opt -> {
                opt.setPercentage(0);
            });
        } else {
            poll.getOptions().forEach(opt -> {
                double percentage = (double) opt.getVoteCount() / totalVotes * 100;
                opt.setPercentage((int) Math.round(percentage));
            });
        }
    }
}
