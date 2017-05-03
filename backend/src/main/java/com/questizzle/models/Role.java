package com.questizzle.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;
import java.util.List;

/**
 * Created by Danny on 3/21/2017.
 */
@Document(collection = "roles")
public class Role extends ResourceSupport {

    @Id
    @JsonProperty("id")
    private String identity;
    private String name;
    private String description;

    private Date dateCreated;
    private Date dateModified;

    public Role() {}

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

    @Override
    @Transient
    public List<Link> getLinks() {
        return super.getLinks();
    }
}
