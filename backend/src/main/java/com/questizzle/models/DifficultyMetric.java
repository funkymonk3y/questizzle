package com.questizzle.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Transient;

/**
 * Created by danny.grullon on 4/15/17.
 */
public class DifficultyMetric {

    private DifficultyVotes votes = new DifficultyVotes();

    @JsonIgnore
    private DifficultyVoters voters = new DifficultyVoters();

    @Transient
    private String voted;

    public DifficultyMetric() {}

    public DifficultyVotes getVotes() {
        return votes;
    }

    public void setVotes(DifficultyVotes votes) {
        this.votes = votes;
    }

    public DifficultyVoters getVoters() {
        return voters;
    }

    public void setVoters(DifficultyVoters voters) {
        this.voters = voters;
    }

    public String getVoted() {
        return voted;
    }

    public void setVoted(String voted) {
        this.voted = voted;
    }
}
