package com.questizzle.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * Created by Danny on 3/20/2017.
 */
@Document(collection = "users")
public class User extends ResourceSupport {

    @Id
    @JsonProperty("id")
    private String identity;
    private String username;
    private String email;

    @JsonIgnore
    private String confirmationToken;

    private String password;

    private Date dateCreated;
    private Date dateModified;
    private Date lastPasswordResetDate;

    private Boolean isAccountEnabled;

    private Boolean isEmailConfirmed;

    private List<String> roles = new ArrayList<>();

    private Integer questions = 0;
    private Integer votes = 0;
    private Integer comments = 0;

    public User() {}

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

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
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

    public Boolean getAccountEnabled() {
        return isAccountEnabled;
    }

    public void setAccountEnabled(Boolean accountEnabled) {
        isAccountEnabled = accountEnabled;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    @Transient
    public List<Link> getLinks() {
        return super.getLinks();
    }

    public void addRole(String role) {
        roles.add(role);
    }

    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public Boolean getEmailConfirmed() {
        return isEmailConfirmed;
    }

    public void setEmailConfirmed(Boolean emailConfirmed) {
        isEmailConfirmed = emailConfirmed;
    }

    public Integer getQuestions() {
        return questions;
    }

    public void setQuestions(Integer questions) {
        this.questions = questions;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }
}
