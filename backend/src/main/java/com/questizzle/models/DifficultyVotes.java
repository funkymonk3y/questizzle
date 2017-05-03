package com.questizzle.models;

/**
 * Created by danny.grullon on 4/15/17.
 */
public class DifficultyVotes {

    private Integer easy = 0;
    private Integer average = 0;
    private Integer hard = 0;

    public DifficultyVotes() {}

    public Integer getEasy() {
        return easy;
    }

    public void setEasy(Integer easy) {
        this.easy = easy;
    }

    public Integer getAverage() {
        return average;
    }

    public void setAverage(Integer average) {
        this.average = average;
    }

    public Integer getHard() {
        return hard;
    }

    public void setHard(Integer hard) {
        this.hard = hard;
    }
}
