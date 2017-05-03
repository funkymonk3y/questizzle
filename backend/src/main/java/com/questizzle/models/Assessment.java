package com.questizzle.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Danny on 3/30/2017.
 */
@Document(collection = "assessments")
public class Assessment extends ResourceSupport {

    @Id
    @JsonProperty("id")
    private String identity;

    @JsonProperty("portal_id")
    private String portalId;

    private String name;
    private String instructions;

    private List<String> topics = new ArrayList<>();

    private Date dateCreated;
    private Date dateModified;

    private MiniUser createdBy;
    private MiniUser modifiedBy;

    @DBRef
    private List<Question> questions = new ArrayList<>();

    private QualityMetric quality = new QualityMetric();

    private DifficultyMetric difficulty = new DifficultyMetric();

    private List<LikeMetric> questionLikes = new ArrayList<>();

    public Assessment() {}

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getPortalId() {
        return portalId;
    }

    public void setPortalId(String portalId) {
        this.portalId = portalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
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

    public List<LikeMetric> getQuestionLikes() {
        return questionLikes;
    }

    public void setQuestionLikes(List<LikeMetric> questionLikes) {
        this.questionLikes = questionLikes;
    }
}
