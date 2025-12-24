package com.example.votingapp.results;

import com.example.votingapp.shared.Poll;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/results")
public class ResultsController {
    private final ResultsService resultsService;

    public ResultsController(ResultsService resultsService) {
        this.resultsService = resultsService;
    }

    @GetMapping("/{pollId}")
    public String results(@PathVariable Long pollId, Model model) {
        Poll poll = resultsService.getPoll(pollId);

        if (poll == null) {
            return "redirect:/management";
        }

        resultsService.calculatePercentages(poll);
        int totalVotes = resultsService.getTotalVotes(pollId);

        model.addAttribute("poll", poll);
        model.addAttribute("totalVotes", totalVotes);

        return "results/results";
    }

    @GetMapping("/{pollId}/live")
    public String livePollResults(@PathVariable Long pollId, Model model) {
        Poll poll = resultsService.getPoll(pollId);

        if (poll == null) {
            return "";
        }

        resultsService.calculatePercentages(poll);
        int totalVotes = resultsService.getTotalVotes(pollId);

        model.addAttribute("poll", poll);
        model.addAttribute("totalVotes", totalVotes);

        return "results/results :: poll-results";
    }
}
