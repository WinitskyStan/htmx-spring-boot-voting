package com.example.votingapp;

import com.example.votingapp.shared.PollStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class VotingAppApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(VotingAppApplication.class, args);
        initializeSamplePolls(context.getBean(PollStore.class));
    }

    private static void initializeSamplePolls(PollStore pollStore) {
        pollStore.addPoll(
                "What's your favorite programming language?",
                java.util.Arrays.asList("Java", "Python", "JavaScript", "Rust", "Go")
        );

        pollStore.addPoll(
                "Preferred development environment?",
                java.util.Arrays.asList("VS Code", "IntelliJ IDEA", "Vim", "Emacs")
        );

        pollStore.addPoll(
                "Best HTMX feature?",
                java.util.Arrays.asList("hx-get", "hx-post", "hx-swap", "hx-trigger", "hx-target")
        );
    }
}
