package com.questizzle.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;
import java.util.List;

/**
 * Created by Danny on 3/29/2017.
 */
@Document(collection = "comments")
public class Comment extends ResourceSupport {

    @Id
    @JsonProperty("id")
    private String identity;
    private String text;

    @Field("question_id")
    @JsonProperty("question_id")
    private String questionId;

    @Field("assessment_id")
    @JsonProperty("assessment_id")
    private String assessmentId;

    private Date posted;

    private MiniUser author;

    public Comment() {}

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    public Date getPosted() {
        return posted;
    }

    public void setPosted(Date posted) {
        this.posted = posted;
    }

    public MiniUser getAuthor() {
        return author;
    }

    public void setAuthor(MiniUser author) {
        this.author = author;
    }

    @Override
    @Transient
    public List<Link> getLinks() {
        return super.getLinks();
    }
}
