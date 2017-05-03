package com.questizzle.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danny.grullon on 4/15/17.
 */
public class DifficultyVoters {

    private List<String> easy = new ArrayList<>();
    private List<String> average = new ArrayList<>();
    private List<String> hard = new ArrayList<>();

    public DifficultyVoters() {}

    public List<String> getEasy() {
        return easy;
    }

    public void setEasy(List<String> easy) {
        this.easy = easy;
    }

    public List<String> getAverage() {
        return average;
    }

    public void setAverage(List<String> average) {
        this.average = average;
    }

    public List<String> getHard() {
        return hard;
    }

    public void setHard(List<String> hard) {
        this.hard = hard;
    }
}
