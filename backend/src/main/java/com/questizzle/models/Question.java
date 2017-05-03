package com.questizzle.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by danny.grullon on 3/18/17.
 */
@Document(collection = "questions")
public class Question extends ResourceSupport {

    @Id
    @JsonProperty("id")
    private String identity;
    private String text;

    @JsonProperty("portal_id")
    private String portalId;

    private MiniUser createdBy;
    private MiniUser modifiedBy;

    private Date dateCreated;
    private Date dateModified;

    private List<String> topics = new ArrayList<>();
    private List<String> hints = new ArrayList<>();
    private List<Answer> answers = new ArrayList<>();

    private QualityMetric quality = new QualityMetric();

    private DifficultyMetric difficulty = new DifficultyMetric();

    public Question() {}

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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public List<String> getHints() {
        return hints;
    }

    public void setHints(List<String> hints) {
        this.hints = hints;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public void addTopics(String topic) {
        this.topics.add(topic);
    }

    public void addHints(String hints) {
        this.topics.add(hints);
    }

    public String getPortalId() {
        return portalId;
    }

    public void setPortalId(String portalId) {
        this.portalId = portalId;
    }

    public MiniUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(MiniUser createdBy) {
        this.createdBy = createdBy;
    }

    public MiniUser getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(MiniUser modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    @Transient
    public List<Link> getLinks() {
        return super.getLinks();
    }

    public QualityMetric getQuality() {
        return quality;
    }

    public void setQuality(QualityMetric quality) {
        this.quality = quality;
    }

    public DifficultyMetric getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(DifficultyMetric difficulty) {
        this.difficulty = difficulty;
    }
}
