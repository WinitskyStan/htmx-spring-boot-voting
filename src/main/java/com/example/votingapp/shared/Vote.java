package com.example.votingapp.shared;

import java.time.LocalDateTime;

public class Vote {
    private String sessionId;
    private Long pollId;
    private Long optionId;
    private LocalDateTime votedAt;

    public Vote() {
    }

    public Vote(String sessionId, Long pollId, Long optionId) {
        this.sessionId = sessionId;
        this.pollId = pollId;
        this.optionId = optionId;
        this.votedAt = LocalDateTime.now();
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getPollId() {
        return pollId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public LocalDateTime getVotedAt() {
        return votedAt;
    }

    public void setVotedAt(LocalDateTime votedAt) {
        this.votedAt = votedAt;
    }
}
