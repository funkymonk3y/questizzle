package com.questizzle.controllers;

import com.questizzle.models.DifficultyMetric;
import com.questizzle.models.QualityMetric;
import com.questizzle.models.Question;
import com.questizzle.models.Vote;
import com.questizzle.security.utils.JwtTokenUtil;
import com.questizzle.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Danny on 3/20/2017.
 */
@RestController
@RequestMapping("/api/portal/{portal_id}/question")
public class QuestionController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private QuestionService questionService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Question> showAllQuestions(@PathVariable("portal_id") String portalId) {
        List<Question> questions = questionService.getAllQuestions(portalId);

        questions = embedLinks(questions);

        return questions;
    }

    private List<Question> embedLinks(List<Question> questions) {
        for(int i = 0; i < questions.size(); i++) {
            String id       = questions.get(i).getIdentity();
            String userId   = questions.get(i).getCreatedBy().getIdentity();
            String portalId = questions.get(i).getPortalId();

            questions.get(i).add(linkTo(methodOn(QuestionController.class).getQuestion(id, portalId)).withSelfRel());
            questions.get(i).add(linkTo(methodOn(PortalController.class).getPortal(portalId)).withRel("portal"));
            questions.get(i).getCreatedBy().add(linkTo(methodOn(UserController.class).getUser(userId)).withSelfRel());
        }

        return questions;
    }

    public Question getQuestion(@PathVariable("id") String id, @PathVariable("portal_id") String portalId) {
        return getQuestion(id, portalId, null);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Question getQuestion(@PathVariable("id") String id, @PathVariable("portal_id") String portalId, HttpServletRequest request) {
        String token = request.getHeader(UserController.tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);

        Question question = questionService.getQuestion(id, username);

        String userId = question.getCreatedBy().getIdentity();

        question.add(linkTo(methodOn(QuestionController.class).getQuestion(id, portalId)).withSelfRel());
        question.add(linkTo(methodOn(PortalController.class).getPortal(portalId)).withRel("portal"));
        question.getCreatedBy().add(linkTo(methodOn(UserController.class).getUser(userId)).withSelfRel());

        return question;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Question createQuestion(@RequestBody Question question, @PathVariable("portal_id") String portalId) {
        Question savedQuestion = questionService.createQuestion(question, portalId);

        String id     = savedQuestion.getIdentity();
        String userId = savedQuestion.getCreatedBy().getIdentity();

        savedQuestion.add(linkTo(methodOn(QuestionController.class).getQuestion(id, portalId)).withSelfRel());
        savedQuestion.add(linkTo(methodOn(PortalController.class).getPortal(portalId)).withRel("portal"));
        savedQuestion.getCreatedBy().add(linkTo(methodOn(UserController.class).getUser(userId)).withSelfRel());

        return savedQuestion;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Question updateQuestion(@PathVariable("id") String id, @RequestBody Question question, @PathVariable("portal_id") String portalId) {
        Question savedQuestion = questionService.updateQuestion(id, question);

        String userId = savedQuestion.getCreatedBy().getIdentity();

        savedQuestion.add(linkTo(methodOn(QuestionController.class).getQuestion(id, portalId)).withSelfRel());
        savedQuestion.add(linkTo(methodOn(PortalController.class).getPortal(portalId)).withRel("portal"));
        savedQuestion.getCreatedBy().add(linkTo(methodOn(UserController.class).getUser(userId)).withSelfRel());
        savedQuestion.getModifiedBy().add(linkTo(methodOn(UserController.class).getUser(userId)).withSelfRel());

        return savedQuestion;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deletePortal(@PathVariable("id") String id) {
        questionService.deleteQuestion(id);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Question> searchQuestions(@RequestParam(value="query") String query, @PathVariable("portal_id") String portalId) {
        List<Question> questions = questionService.searchQuestions(portalId, query);

        questions = embedLinks(questions);

        return questions;
    }

    @RequestMapping(value = "/{id}/vote/quality", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public QualityMetric voteOnQuality(@PathVariable("id") String id, @RequestBody Vote vote) {
        QualityMetric quality = questionService.voteOnQuality(vote.getProperty(), vote.getUsername(), id);

        return quality;
    }

    @RequestMapping(value = "/{id}/vote/difficulty", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public DifficultyMetric voteOnDifficulty(@PathVariable("id") String id, @RequestBody Vote vote) {
        DifficultyMetric difficulty = questionService.voteOnDifficulty(vote.getProperty(), vote.getUsername(), id);

        return difficulty;
    }
}