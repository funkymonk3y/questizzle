package com.questizzle.services;

import com.questizzle.exceptions.AccessDeniedException;
import com.questizzle.models.*;
import com.questizzle.exceptions.DataNotFoundException;
import com.questizzle.repositories.PortalRepository;
import com.questizzle.repositories.QuestionRepository;
import com.questizzle.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Danny on 3/20/2017.
 */
@Service
public class QuestionService {

    @Autowired
    private PortalRepository portalRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PortalService portalService;

    @Autowired
    private UserService userService;

    public QuestionService() {}

    public Question getQuestion(String id, String username) throws RuntimeException {
        Question question =  questionRepository.findOne(id);

        if(question == null) {
            throw new DataNotFoundException(id);
        }

        if(portalService.isNotAllowed(question.getPortalId(), username)) {
            throw new AccessDeniedException();
        }

        QualityMetric quality = question.getQuality();
        QualityVoters qv = quality.getVoters();

        if(qv.getBad().contains(username)) {
            quality.setVoted("bad");
        }
        else if(qv.getPoor().contains(username)) {
            quality.setVoted("poor");
        }
        else if(qv.getAverage().contains(username)) {
            quality.setVoted("average");
        }
        else if(qv.getGood().contains(username)) {
            quality.setVoted("good");
        }
        else if(qv.getExcellent().contains(username)) {
            quality.setVoted("excellent");
        }

        question.setQuality(quality);

        DifficultyMetric difficulty = question.getDifficulty();
        DifficultyVoters dv = difficulty.getVoters();

        if(dv.getEasy().contains(username)) {
            difficulty.setVoted("easy");
        }
        else if(dv.getAverage().contains(username)) {
            difficulty.setVoted("average");
        }
        else if(dv.getHard().contains(username)) {
            difficulty.setVoted("hard");
        }

        question.setDifficulty(difficulty);

        return question;
    }

    public Question createQuestion(Question question, String portalId) throws DataNotFoundException {
        Portal portal = portalRepository.findOne(portalId);

        if(portal == null) {
            throw new DataNotFoundException("Portal" + portalId + " ");
        }

        MiniUser userInfo = question.getCreatedBy();

        if(userInfo.getUsername() == null) {
            throw new UsernameNotFoundException(userInfo.getUsername());
        }

        if(userInfo.getIdentity() == null) {
            User user = userRepository.findByUsername(userInfo.getUsername());
            userInfo.setIdentity(user.getIdentity());
            question.setCreatedBy(userInfo);
        }

        question = trimListValues(question);

        question.setPortalId(portalId);
        question.setDateCreated(new Date());

        Question savedQuestion = questionRepository.save(question);

        userService.incrementCount(userInfo.getUsername(), "questions");

        return savedQuestion;
    }

    private Question trimListValues(Question question) {
        for(int i = 0; i < question.getTopics().size(); i++) {
            String topic = question.getTopics().get(i).trim();
            question.getTopics().set(i, topic);
        }

        return question;
    }

    public Question updateQuestion(String id, Question question) throws DataNotFoundException {
        Question existingQuestion = questionRepository.findOne(id);

        if(existingQuestion == null) {
            throw new DataNotFoundException(id);
        }

        MiniUser userInfo = question.getModifiedBy();

        if(userInfo.getUsername() == null) {
            throw new UsernameNotFoundException(userInfo.getUsername());
        }

        if(userInfo.getIdentity() == null) {
            User user = userRepository.findByUsername(userInfo.getUsername());
            userInfo.setIdentity(user.getIdentity());
            existingQuestion.setModifiedBy(userInfo);
        }

        question = trimListValues(question);

        existingQuestion.setText(question.getText());
        existingQuestion.setTopics(question.getTopics());
        existingQuestion.setHints(question.getHints());
        existingQuestion.setAnswers(question.getAnswers());
        existingQuestion.setDateModified(new Date());

        Question savedQuestion = questionRepository.save(existingQuestion);

        return savedQuestion;
    }

    public void deleteQuestion(String id) throws DataNotFoundException {
        if(questionRepository.exists(id)) {
            questionRepository.delete(id);
        } else {
            throw new DataNotFoundException(id);
        }
    }

    public List<Question> searchQuestions(String portalId, String query) {
        List<Question> questions = questionRepository.findByPortalIdAndTextContainingIgnoreCase(portalId, query);
        return questions;
    }

    public List<Question> getAllQuestions(String portalId) {
        return questionRepository.findByPortalId(portalId);
    }

    public QualityMetric voteOnQuality(String voteProperty, String username, String id) {
        Question question = questionRepository.findOne(id);
        QualityMetric qualityMetric = question.getQuality();
        QualityVoters qv = qualityMetric.getVoters();

        Query query = new Query();
        query.addCriteria(Criteria.where("identity").is(id));

        Update update = new Update();

        if(qv.getBad().contains(username)) {
            update.inc("quality.votes.bad", -1);
            update.pull("quality.voters.bad", username);
        }
        else if(qv.getPoor().contains(username)) {
            update.inc("quality.votes.poor", -1);
            update.pull("quality.voters.poor", username);
        }
        else if(qv.getAverage().contains(username)) {
            update.inc("quality.votes.average", -1);
            update.pull("quality.voters.average", username);
        }
        else if(qv.getGood().contains(username)) {
            update.inc("quality.votes.good", -1);
            update.pull("quality.voters.good", username);
        }
        else if(qv.getExcellent().contains(username)) {
            update.inc("quality.votes.excellent", -1);
            update.pull("quality.voters.excellent", username);
        } else {
            userService.incrementCount(username, "votes");
        }

        switch (voteProperty.toLowerCase()) {
            case "bad":
                if(!qv.getBad().contains(username)) {
                    update.inc("quality.votes.bad", 1);
                    update.addToSet("quality.voters.bad", username);
                    mongoTemplate.findAndModify(query, update, Question.class);
                }

                break;
            case "poor":
                if(!qv.getPoor().contains(username)) {
                    update.inc("quality.votes.poor", 1);
                    update.addToSet("quality.voters.poor", username);
                    mongoTemplate.findAndModify(query, update, Question.class);
                }

                break;
            case "average":
                if(!qv.getAverage().contains(username)) {
                    update.inc("quality.votes.average", 1);
                    update.addToSet("quality.voters.average", username);
                    mongoTemplate.findAndModify(query, update, Question.class);
                }

                break;
            case "good":
                if(!qv.getGood().contains(username)) {
                    update.inc("quality.votes.good", 1);
                    update.addToSet("quality.voters.good", username);
                    mongoTemplate.findAndModify(query, update, Question.class);
                }

                break;
            case "excellent":
                if(!qv.getExcellent().contains(username)) {
                    update.inc("quality.votes.excellent", 1);
                    update.addToSet("quality.voters.excellent", username);
                    mongoTemplate.findAndModify(query, update, Question.class);
                }

                break;
        }

        Question updatedQuestion = questionRepository.findOne(id);
        QualityMetric updatedMetric = updatedQuestion.getQuality();
        updatedMetric.setVoted(voteProperty);

        return updatedMetric;
    }

    public DifficultyMetric voteOnDifficulty(String voteProperty, String username, String id) {
        Question question = questionRepository.findOne(id);
        DifficultyMetric difficultyMetric = question.getDifficulty();
        DifficultyVoters dv = difficultyMetric.getVoters();

        Query query = new Query();
        query.addCriteria(Criteria.where("identity").is(id));

        Update update = new Update();

        if(dv.getEasy().contains(username)) {
            update.inc("difficulty.votes.easy", -1);
            update.pull("difficulty.voters.easy", username);
        }
        else if(dv.getAverage().contains(username)) {
            update.inc("difficulty.votes.average", -1);
            update.pull("difficulty.voters.average", username);
        }
        else if(dv.getHard().contains(username)) {
            update.inc("difficulty.votes.hard", -1);
            update.pull("difficulty.voters.hard", username);
        } else {
            userService.incrementCount(username, "votes");
        }

        switch (voteProperty.toLowerCase()) {
            case "easy":
                if (!dv.getEasy().contains(username)) {
                    update.inc("difficulty.votes.easy", 1);
                    update.addToSet("difficulty.voters.easy", username);
                    mongoTemplate.findAndModify(query, update, Question.class);
                }

                break;
            case "average":
                if (!dv.getAverage().contains(username)) {
                    update.inc("difficulty.votes.average", 1);
                    update.addToSet("difficulty.voters.average", username);
                    mongoTemplate.findAndModify(query, update, Question.class);
                }

                break;
            case "hard":
                if (!dv.getHard().contains(username)) {
                    update.inc("difficulty.votes.hard", 1);
                    update.addToSet("difficulty.voters.hard", username);
                    mongoTemplate.findAndModify(query, update, Question.class);
                }

                break;
        }

        Question updatedQuestion = questionRepository.findOne(id);
        DifficultyMetric updatedMetric = updatedQuestion.getDifficulty();
        updatedMetric.setVoted(voteProperty);

        return updatedMetric;
    }
}
