package com.questizzle.models;

/**
 * Created by danny.grullon on 4/15/17.
 */
public class QualityVotes {

    private Integer bad = 0;
    private Integer poor = 0;
    private Integer average = 0;
    private Integer good = 0;
    private Integer excellent = 0;

    public QualityVotes() {}

    public Integer getBad() {
        return bad;
    }

    public void setBad(Integer bad) {
        this.bad = bad;
    }

    public Integer getPoor() {
        return poor;
    }

    public void setPoor(Integer poor) {
        this.poor = poor;
    }

    public Integer getAverage() {
        return average;
    }

    public void setAverage(Integer average) {
        this.average = average;
    }

    public Integer getGood() {
        return good;
    }

    public void setGood(Integer good) {
        this.good = good;
    }

    public Integer getExcellent() {
        return excellent;
    }

    public void setExcellent(Integer excellent) {
        this.excellent = excellent;
    }
}
