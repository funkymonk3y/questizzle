package com.questizzle.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danny.grullon on 4/15/17.
 */
public class QualityVoters {

    private List<String> bad = new ArrayList<>();
    private List<String> poor = new ArrayList<>();
    private List<String> average = new ArrayList<>();
    private List<String> good = new ArrayList<>();
    private List<String> excellent = new ArrayList<>();

    public QualityVoters() {}

    public List<String> getBad() {
        return bad;
    }

    public void setBad(List<String> bad) {
        this.bad = bad;
    }

    public List<String> getPoor() {
        return poor;
    }

    public void setPoor(List<String> poor) {
        this.poor = poor;
    }

    public List<String> getAverage() {
        return average;
    }

    public void setAverage(List<String> average) {
        this.average = average;
    }

    public List<String> getGood() {
        return good;
    }

    public void setGood(List<String> good) {
        this.good = good;
    }

    public List<String> getExcellent() {
        return excellent;
    }

    public void setExcellent(List<String> excellent) {
        this.excellent = excellent;
    }
}
