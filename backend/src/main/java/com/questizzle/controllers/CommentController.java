package com.questizzle.controllers;

import com.questizzle.models.Comment;
import com.questizzle.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Danny on 4/5/2017.
 */
@RestController
@RequestMapping("/api/portal/{portal_id}")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/question/{question_id}/comment", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Comment> showAllQuestionComments(@PathVariable("portal_id") String portal_id,
                                                 @PathVariable("question_id") String question_id) {
        List<Comment> comments = commentService.getQuestionComments(question_id);

        for(int i = 0; i < comments.size(); i++) {
            String id = comments.get(i).getIdentity();

            comments.get(i).add(linkTo(methodOn(CommentController.class).getQuestionComment(id, portal_id, question_id)).withSelfRel());
            comments.get(i).add(linkTo(methodOn(PortalController.class).getPortal(portal_id)).withRel("portal"));
            comments.get(i).add(linkTo(methodOn(QuestionController.class).getQuestion(question_id, portal_id)).withRel("question"));
            comments.get(i).getAuthor().add(linkTo(methodOn(UserController.class).getUser(comments.get(i).getAuthor().getIdentity())).withSelfRel());
        }

        return comments;
    }

    @RequestMapping(value = "/question/{question_id}/comment/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Comment getQuestionComment(@PathVariable("portal_id") String portal_id,
                                      @PathVariable("question_id") String question_id,
                                      @PathVariable("id") String id) {

        Comment comment = commentService.getComment(id);
        comment.add(linkTo(methodOn(CommentController.class).getQuestionComment(id, portal_id, question_id)).withSelfRel());
        comment.add(linkTo(methodOn(PortalController.class).getPortal(portal_id)).withRel("portal"));
        comment.add(linkTo(methodOn(QuestionController.class).getQuestion(question_id, portal_id)).withRel("question"));
        comment.getAuthor().add(linkTo(methodOn(UserController.class).getUser(comment.getAuthor().getIdentity())).withSelfRel());

        return comment;
    }

    @RequestMapping(value = "/question/{question_id}/comment", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Comment createQuestionComment(@RequestBody Comment comment,
                                         @PathVariable("portal_id") String portal_id,
                                         @PathVariable("question_id") String question_id) {

        Comment savedComment = commentService.createQuestionComment(question_id, comment.getText(), comment.getAuthor().getUsername());

        String id  = savedComment.getIdentity();
        savedComment.add(linkTo(methodOn(CommentController.class).getQuestionComment(id, portal_id, question_id)).withSelfRel());
        savedComment.add(linkTo(methodOn(PortalController.class).getPortal(portal_id)).withRel("portal"));
        savedComment.add(linkTo(methodOn(QuestionController.class).getQuestion(question_id, portal_id)).withRel("question"));
        savedComment.getAuthor().add(linkTo(methodOn(UserController.class).getUser(savedComment.getAuthor().getIdentity())).withSelfRel());

        return savedComment;
    }

    @RequestMapping(value = "/assessment/{assessment_id}/comment", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Comment> showAllAssessmentComments(@PathVariable("portal_id") String portal_id,
                                                   @PathVariable("assessment_id") String assessment_id) {
        List<Comment> comments = commentService.getAssessmentComments(assessment_id);

        for(int i = 0; i < comments.size(); i++) {
            String id = comments.get(i).getIdentity();

            comments.get(i).add(linkTo(methodOn(CommentController.class).getAssessmentComment(id, portal_id, assessment_id)).withSelfRel());
            comments.get(i).add(linkTo(methodOn(PortalController.class).getPortal(portal_id)).withRel("portal"));
            comments.get(i).add(linkTo(methodOn(AssessmentController.class).getAssessment(portal_id, assessment_id)).withRel("assessment"));
            comments.get(i).getAuthor().add(linkTo(methodOn(UserController.class).getUser(comments.get(i).getAuthor().getIdentity())).withSelfRel());
        }

        return comments;
    }

    @RequestMapping(value = "/assessment/{assessment_id}/comment/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Comment getAssessmentComment(@PathVariable("portal_id") String portal_id,
                                        @PathVariable("assessment_id") String assessment_id,
                                        @PathVariable("id") String id) {

        Comment comment = commentService.getComment(id);
        comment.add(linkTo(methodOn(CommentController.class).getAssessmentComment(id, portal_id, assessment_id)).withSelfRel());
        comment.add(linkTo(methodOn(PortalController.class).getPortal(portal_id)).withRel("portal"));
        comment.add(linkTo(methodOn(AssessmentController.class).getAssessment(portal_id, assessment_id)).withRel("assessment"));
        comment.getAuthor().add(linkTo(methodOn(UserController.class).getUser(comment.getAuthor().getIdentity())).withSelfRel());

        return comment;
    }

    @RequestMapping(value = "/assessment/{assessment_id}/comment", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Comment createAssessmentComment(@RequestBody Comment comment,
                                           @PathVariable("portal_id") String portal_id,
                                           @PathVariable("assessment_id") String assessment_id) {

        Comment savedComment = commentService.createAssessmentComment(assessment_id, comment.getText(), comment.getAuthor().getUsername());

        String id  = savedComment.getIdentity();
        savedComment.add(linkTo(methodOn(CommentController.class).getAssessmentComment(id, portal_id, assessment_id)).withSelfRel());
        savedComment.add(linkTo(methodOn(PortalController.class).getPortal(portal_id)).withRel("portal"));
        savedComment.add(linkTo(methodOn(AssessmentController.class).getAssessment(portal_id, assessment_id)).withRel("question"));
        savedComment.getAuthor().add(linkTo(methodOn(UserController.class).getUser(savedComment.getAuthor().getIdentity())).withSelfRel());

        return savedComment;
    }
}
