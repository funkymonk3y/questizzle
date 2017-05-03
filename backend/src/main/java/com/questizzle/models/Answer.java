package com.questizzle.models;

/**
 * Created by Danny on 3/26/2017.
 */
public class Answer {

    private String text;

    private Boolean correct = false;

    public Answer() {}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }
}
