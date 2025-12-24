package com.example.votingapp.voting;

import com.example.votingapp.shared.Poll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/voting")
public class VotingController {
    private final VotingService votingService;
    private final VotingSessionService votingSessionService;

    public VotingController(VotingService votingService, VotingSessionService votingSessionService) {
        this.votingService = votingService;
        this.votingSessionService = votingSessionService;
    }

    @GetMapping("/{pollId}")
    public String voting(
            @PathVariable Long pollId,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) {
        String sessionId = votingSessionService.getOrCreateSessionId(request, response);
        Poll poll = votingService.getPoll(pollId);

        if (poll == null) {
            return "redirect:/management";
        }

        boolean hasVoted = votingService.hasUserVoted(sessionId, pollId);
        model.addAttribute("poll", poll);
        model.addAttribute("hasVoted", hasVoted);

        if (hasVoted) {
            Long votedOptionId = votingService.getUserVotedOptionId(sessionId, pollId);
            String votedOption = poll.getOptions().stream()
                    .filter(opt -> opt.getId().equals(votedOptionId))
                    .map(opt -> opt.getText())
                    .findFirst()
                    .orElse("");
            model.addAttribute("votedOption", votedOption);
        }

        return "voting/voting";
    }

    @PostMapping("/{pollId}/vote")
    public String submitVote(
            @PathVariable Long pollId,
            @RequestParam Long optionId,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) {
        String sessionId = votingSessionService.getOrCreateSessionId(request, response);

        Poll poll = votingService.getPoll(pollId);
        if (poll != null && !votingService.hasUserVoted(sessionId, pollId)) {
            votingService.submitVote(sessionId, pollId, optionId);
        }

        boolean hasVoted = votingService.hasUserVoted(sessionId, pollId);
        model.addAttribute("poll", poll);
        model.addAttribute("hasVoted", hasVoted);

        if (hasVoted) {
            Long votedOptionId = votingService.getUserVotedOptionId(sessionId, pollId);
            String votedOption = poll.getOptions().stream()
                    .filter(opt -> opt.getId().equals(votedOptionId))
                    .map(opt -> opt.getText())
                    .findFirst()
                    .orElse("");
            model.addAttribute("votedOption", votedOption);
        }

        return "voting/voting :: poll-card";
    }
}
