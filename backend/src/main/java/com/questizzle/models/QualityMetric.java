package com.questizzle.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Transient;

/**
 * Created by Danny on 4/14/2017.
 */
public class QualityMetric {

    private QualityVotes votes = new QualityVotes();

    @JsonIgnore
    private QualityVoters voters = new QualityVoters();

    @Transient
    private String voted;

    public QualityMetric() {}

    public QualityVotes getVotes() {
        return votes;
    }

    public void setVotes(QualityVotes votes) {
        this.votes = votes;
    }

    public QualityVoters getVoters() {
        return voters;
    }

    public void setVoters(QualityVoters voters) {
        this.voters = voters;
    }

    public String getVoted() {
        return voted;
    }

    public void setVoted(String voted) {
        this.voted = voted;
    }
}
