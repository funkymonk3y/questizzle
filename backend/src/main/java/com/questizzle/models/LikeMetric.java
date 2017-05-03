package com.questizzle.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danny on 4/23/2017.
 */
public class LikeMetric {

    private String identity;

    private Integer count = 0;

    private List<String> voted = new ArrayList<>();

    public LikeMetric() {}

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<String> getVoted() {
        return voted;
    }

    public void setVoted(List<String> voted) {
        this.voted = voted;
    }
}
