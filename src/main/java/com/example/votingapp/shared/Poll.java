package com.example.votingapp.shared;

import java.time.LocalDateTime;
import java.util.List;

public class Poll {
    private Long id;
    private String question;
    private List<PollOption> options;
    private LocalDateTime createdAt;

    public Poll() {
    }

    public Poll(Long id, String question, List<PollOption> options) {
        this.id = id;
        this.question = question;
        this.options = options;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<PollOption> getOptions() {
        return options;
    }

    public void setOptions(List<PollOption> options) {
        this.options = options;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
