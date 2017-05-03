package com.questizzle.controllers;

import com.questizzle.models.*;
import com.questizzle.security.utils.JwtTokenUtil;
import com.questizzle.services.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Danny on 3/30/2017.
 */
@RestController
@RequestMapping("/api/portal/{portal_id}/assessment")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public Assessment getAssessment(@PathVariable("portal_id") String portal_id,
                                    @PathVariable("id") String id) {
        return getAssessment(portal_id, id, null);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Assessment getAssessment(@PathVariable("portal_id") String portal_id,
                                    @PathVariable("id") String id,
                                    HttpServletRequest request) {

        String token = request.getHeader(UserController.tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);

        Assessment assessment = assessmentService.getAssessment(id, username);
        assessment.add(linkTo(methodOn(AssessmentController.class).getAssessment(portal_id, id)).withSelfRel());
        assessment.add(linkTo(methodOn(PortalController.class).getPortal(portal_id)).withRel("portal"));
        assessment.getCreatedBy().add(linkTo(methodOn(UserController.class).getUser(assessment.getCreatedBy().getIdentity())).withSelfRel());

        if(assessment.getQuestions() != null) {
            for(int i = 0; i < assessment.getQuestions().size(); i++) {
                Question someQuestion = assessment.getQuestions().get(i);

                if(someQuestion != null) {
                    String question_id = assessment.getQuestions().get(i).getIdentity();
                    assessment.getQuestions().get(i).add(linkTo(methodOn(QuestionController.class).getQuestion(question_id, portal_id)).withSelfRel());
                }
            }
        }

        return assessment;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Assessment> showAllAssessments(@PathVariable("portal_id") String portal_id) {
        List<Assessment> assessments = assessmentService.getAssessments(portal_id);

        assessments = embedLinks(assessments, portal_id);

        return assessments;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Assessment> searchAssessments(@PathVariable("portal_id") String portal_id, @RequestParam(value="query") String query) {
        List<Assessment> assessments = assessmentService.searchAssessments(query);

        assessments = embedLinks(assessments, portal_id);

        return assessments;
    }

    private List<Assessment> embedLinks(List<Assessment> assessments, String portal_id) {
        for(int i = 0; i < assessments.size(); i++) {
            String id = assessments.get(i).getIdentity();
            assessments.get(i).add(linkTo(methodOn(AssessmentController.class).getAssessment(portal_id, id)).withSelfRel());
            assessments.get(i).add(linkTo(methodOn(PortalController.class).getPortal(portal_id)).withRel("portal"));
            assessments.get(i).getCreatedBy().add(linkTo(methodOn(UserController.class).getUser(assessments.get(i).getCreatedBy().getIdentity())).withSelfRel());
        }

        return assessments;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Assessment createQuestion(@RequestBody Assessment assessment, @PathVariable("portal_id") String portal_id) {
        Assessment savedAssessment = assessmentService.createAssessment(assessment);

        String id = savedAssessment.getIdentity();
        savedAssessment.add(linkTo(methodOn(AssessmentController.class).getAssessment(portal_id, id)).withSelfRel());
        savedAssessment.add(linkTo(methodOn(PortalController.class).getPortal(portal_id)).withRel("portal"));
        savedAssessment.getCreatedBy().add(linkTo(methodOn(UserController.class).getUser(savedAssessment.getCreatedBy().getIdentity())).withSelfRel());

        for(int i = 0; i < savedAssessment.getQuestions().size(); i++) {
            String question_id = savedAssessment.getQuestions().get(i).getIdentity();
            savedAssessment.getQuestions().get(i).add(linkTo(methodOn(QuestionController.class).getQuestion(question_id, portal_id)).withSelfRel());
        }

        return savedAssessment;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Assessment updateAssessment(@PathVariable("id") String id, @RequestBody Assessment assessment, @PathVariable("portal_id") String portal_id) {
        assessment.setIdentity(id);
        Assessment savedAssessment = assessmentService.updateAssessment(id, assessment);

        savedAssessment.add(linkTo(methodOn(AssessmentController.class).getAssessment(portal_id, id)).withSelfRel());
        savedAssessment.add(linkTo(methodOn(PortalController.class).getPortal(portal_id)).withRel("portal"));
        savedAssessment.getCreatedBy().add(linkTo(methodOn(UserController.class).getUser(savedAssessment.getCreatedBy().getIdentity())).withSelfRel());
        savedAssessment.getModifiedBy().add(linkTo(methodOn(UserController.class).getUser(savedAssessment.getModifiedBy().getIdentity())).withSelfRel());

        for(int i = 0; i < savedAssessment.getQuestions().size(); i++) {
            String question_id = savedAssessment.getQuestions().get(i).getIdentity();
            savedAssessment.getQuestions().get(i).add(linkTo(methodOn(QuestionController.class).getQuestion(question_id, portal_id)).withSelfRel());
        }

        return savedAssessment;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteAssessment(@PathVariable("id") String id) {
        assessmentService.deleteAssessment(id);
    }

    @RequestMapping(value = "/{id}/vote/quality", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public QualityMetric voteOnQuality(@PathVariable("id") String id, @RequestBody Vote vote) {
        QualityMetric quality = assessmentService.voteOnQuality(vote.getProperty(), vote.getUsername(), id);

        return quality;
    }

    @RequestMapping(value = "/{id}/vote/difficulty", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public DifficultyMetric voteOnDifficulty(@PathVariable("id") String id, @RequestBody Vote vote) {
        DifficultyMetric difficulty = assessmentService.voteOnDifficulty(vote.getProperty(), vote.getUsername(), id);

        return difficulty;
    }

    @RequestMapping(value = "/{id}/vote/question", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public LikeMetric voteOnQuestion(@PathVariable("id") String id, @RequestBody QuestionLike questionLike) {
        LikeMetric likeMetric = assessmentService.voteOnQuestion(questionLike.getUsername(), id, questionLike.getId());
        return likeMetric;
    }
}
