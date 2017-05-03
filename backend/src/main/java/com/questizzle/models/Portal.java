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
 * Created by Danny on 3/12/2017.
 */
@Document(collection = "portals")
public class Portal extends ResourceSupport {

    @Id
    @JsonProperty("id")
    private String identity;
    private String name;
    private String description;

    private Date dateCreated;
    private Date dateModified;

    private List<String> users  = new ArrayList<>();
    private List<String> admins = new ArrayList<>();
    private List<String> topics = new ArrayList<>();

    private MiniUser createdBy;
    private MiniUser modifiedBy;

    public Portal() {}

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<String> getAdmins() {
        return admins;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
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
}
