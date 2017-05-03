package com.questizzle.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

/**
 * Created by Danny on 3/29/2017.
 */
@Document(collection = "users")
public class MiniUser extends ResourceSupport {

    @Id
    @JsonProperty("id")
    private String identity;
    private String username;

    public MiniUser() {}

    public MiniUser(String id, String username) {
        this.identity = id;
        this.username = username;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    @Transient
    public List<Link> getLinks() {
        return super.getLinks();
    }
}
