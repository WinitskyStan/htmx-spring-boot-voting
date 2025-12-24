package com.example.votingapp.management;

import com.example.votingapp.shared.Poll;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/management")
public class ManagementController {
    private final ManagementService managementService;

    public ManagementController(ManagementService managementService) {
        this.managementService = managementService;
    }

    @GetMapping
    public String management(Model model) {
        List<Poll> polls = managementService.getAllPolls();
        model.addAttribute("polls", polls);
        return "management/management";
    }

    @PostMapping("/polls")
    public String createPoll(
            @RequestParam String question,
            @RequestParam(name = "options[]") List<String> options,
            Model model) {
        managementService.createPoll(question, options);
        List<Poll> polls = managementService.getAllPolls();
        model.addAttribute("polls", polls);
        return "management/management :: poll-list";
    }

    @DeleteMapping("/polls/{id}")
    public String deletePoll(@PathVariable Long id, Model model) {
        managementService.deletePoll(id);
        List<Poll> polls = managementService.getAllPolls();
        model.addAttribute("polls", polls);
        return "management/management :: poll-list";
    }

    @GetMapping("/option-input")
    public String getOptionInput() {
        return "management/management :: option-input";
    }
}
