package com.example.votingapp.shared;

public class PollOption {
    private Long id;
    private String text;
    private int voteCount;
    private int percentage;

    public PollOption() {
    }

    public PollOption(Long id, String text) {
        this.id = id;
        this.text = text;
        this.voteCount = 0;
        this.percentage = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public void incrementVoteCount() {
        this.voteCount++;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
