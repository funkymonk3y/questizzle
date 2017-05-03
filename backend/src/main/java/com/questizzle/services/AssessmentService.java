package com.questizzle.services;

import com.questizzle.exceptions.AccessDeniedException;
import com.questizzle.exceptions.DataNotFoundException;
import com.questizzle.models.*;
import com.questizzle.repositories.AssessmentRepository;
import com.questizzle.repositories.QuestionRepository;
import com.questizzle.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Danny on 3/30/2017.
 */
@Service
public class AssessmentService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private PortalService portalService;

    public AssessmentService() {}

    public Assessment getAssessment(String id, String username) throws RuntimeException {
        Assessment assessment = assessmentRepository.findOne(id);

        if(assessment == null) {
            throw new DataNotFoundException(id);
        }

        if(portalService.isNotAllowed(assessment.getPortalId(), username)) {
            throw new AccessDeniedException();
        }

        QualityMetric quality = assessment.getQuality();
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

        assessment.setQuality(quality);

        DifficultyMetric difficulty = assessment.getDifficulty();
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

        assessment.setDifficulty(difficulty);

        assessment = filterVoters(assessment, username);

        return assessment;
    }

    public Assessment updateAssessment(String id, Assessment assessment) throws DataNotFoundException {
        Assessment existingAssessment = assessmentRepository.findOne(id);

        if(existingAssessment == null) {
            throw new DataNotFoundException(id);
        }

        MiniUser userInfo = assessment.getModifiedBy();

        if(userInfo.getUsername() == null) {
            throw new UsernameNotFoundException(userInfo.getUsername());
        }

        if(userInfo.getIdentity() == null) {
            User user = userRepository.findByUsername(userInfo.getUsername());
            userInfo.setIdentity(user.getIdentity());
            existingAssessment.setModifiedBy(userInfo);
        }

        assessment = trimListValues(assessment);

        existingAssessment.setName(assessment.getName());
        existingAssessment.setInstructions(assessment.getInstructions());
        existingAssessment.setTopics(assessment.getTopics());
        existingAssessment.setQuestions(assessment.getQuestions());
        existingAssessment.setDateModified(new Date());

        for(Question question: existingAssessment.getQuestions()) {
            boolean match = false;

            for(LikeMetric likeMetric: existingAssessment.getQuestionLikes()) {
                if(likeMetric.getIdentity().equals(question.getIdentity())) {
                    match = true;
                    break;
                }
            }

            if(!match) {
                LikeMetric likeMetric = new LikeMetric();
                likeMetric.setIdentity(question.getIdentity());

                existingAssessment.getQuestionLikes().add(likeMetric);
            }
        }

        Assessment savedAssessment = assessmentRepository.save(existingAssessment);

        savedAssessment = filterVoters(savedAssessment, userInfo.getUsername());

        return savedAssessment;
    }

    public Assessment createAssessment(Assessment assessment) throws RuntimeException {
        List<Question> questions = new ArrayList<>();

        for(Question question : assessment.getQuestions()) {
            String question_id = question.getIdentity();

            Question completeQuestion = questionRepository.findOne(question_id);

            if(completeQuestion != null) {
                questions.add(completeQuestion);
            }
        }

        MiniUser userInfo = assessment.getCreatedBy();

        if(userInfo.getUsername() == null) {
            throw new UsernameNotFoundException(userInfo.getUsername());
        }

        if(userInfo.getIdentity() == null) {
            User user = userRepository.findByUsername(userInfo.getUsername());
            userInfo.setIdentity(user.getIdentity());
            assessment.setCreatedBy(userInfo);
        }

        assessment = trimListValues(assessment);

        assessment.setQuestions(questions);
        assessment.setDateCreated(new Date());

        for(Question question : assessment.getQuestions()) {
            LikeMetric likeMetric = new LikeMetric();
            likeMetric.setIdentity(question.getIdentity());
            assessment.getQuestionLikes().add(likeMetric);
        }

        Assessment savedAssessment = assessmentRepository.save(assessment);
        return savedAssessment;
    }

    private Assessment trimListValues(Assessment assessment) {
        for(int i = 0; i < assessment.getTopics().size(); i++) {
            String topic = assessment.getTopics().get(i).trim();
            assessment.getTopics().set(i, topic);
        }

        return assessment;
    }

    public List<Assessment> searchAssessments(String query) {
        List<Assessment> assessments = assessmentRepository.findByNameContainingIgnoreCase(query);

        for(int i = 0; i < assessments.size(); i++) {
            assessments.get(i).setQuestions(new ArrayList<>());
        }

        return assessments;
    }

    public List<Assessment> getAssessments(String portal_id) {
        List<Assessment> assessments = assessmentRepository.findByPortalId(portal_id);

        for(int i = 0; i < assessments.size(); i++) {
            assessments.get(i).setQuestions(new ArrayList<>());
        }

        return assessments;
    }

    public void deleteAssessment(String id) throws DataNotFoundException {
        if(assessmentRepository.exists(id)) {
            assessmentRepository.delete(id);
        } else {
            throw new DataNotFoundException(id);
        }
    }

    public Assessment filterVoters(Assessment assessment, String username) {
        for(int i = 0; i < assessment.getQuestionLikes().size(); i++) {
            if(assessment.getQuestionLikes().get(i).getVoted().contains(username)) {
                assessment.getQuestionLikes().get(i).setVoted(Arrays.asList(username));
            } else {
                assessment.getQuestionLikes().get(i).setVoted(Arrays.asList());
            }
        }

        return assessment;
    }

    public LikeMetric voteOnQuestion(String username, String assessmentId, String questionId) {
        Assessment assessment = assessmentRepository.findOne(assessmentId);

        LikeMetric selectedLikeMetric = null;

        for(LikeMetric likeMetric : assessment.getQuestionLikes()) {
            if(likeMetric.getIdentity().equals(questionId)) {
                if(!likeMetric.getVoted().contains(username)) {
                    Query query = new Query();

                    query.addCriteria(new Criteria().andOperator(
                            Criteria.where("identity").is(assessmentId),
                            Criteria.where("questionLikes.identity").is(questionId)
                    ));

                    Update update = new Update();
                    update.inc("questionLikes.$.count", 1);
                    update.addToSet("questionLikes.$.voted", username);

                    mongoTemplate.findAndModify(query, update, Assessment.class);

                    likeMetric.setCount(likeMetric.getCount() + 1);
                    likeMetric.setVoted(Arrays.asList(username));

                    selectedLikeMetric = likeMetric;
                }

                break;
            }
        }

        return selectedLikeMetric;
    }

    public QualityMetric voteOnQuality(String voteProperty, String username, String id) {
        Assessment assessment = assessmentRepository.findOne(id);
        QualityMetric qualityMetric = assessment.getQuality();
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
                    mongoTemplate.findAndModify(query, update, Assessment.class);
                }

                break;
            case "poor":
                if(!qv.getPoor().contains(username)) {
                    update.inc("quality.votes.poor", 1);
                    update.addToSet("quality.voters.poor", username);
                    mongoTemplate.findAndModify(query, update, Assessment.class);
                }

                break;
            case "average":
                if(!qv.getAverage().contains(username)) {
                    update.inc("quality.votes.average", 1);
                    update.addToSet("quality.voters.average", username);
                    mongoTemplate.findAndModify(query, update, Assessment.class);
                }

                break;
            case "good":
                if(!qv.getGood().contains(username)) {
                    update.inc("quality.votes.good", 1);
                    update.addToSet("quality.voters.good", username);
                    mongoTemplate.findAndModify(query, update, Assessment.class);
                }

                break;
            case "excellent":
                if(!qv.getExcellent().contains(username)) {
                    update.inc("quality.votes.excellent", 1);
                    update.addToSet("quality.voters.excellent", username);
                    mongoTemplate.findAndModify(query, update, Assessment.class);
                }

                break;
        }

        Assessment updatedAssessment = assessmentRepository.findOne(id);
        QualityMetric updatedMetric = updatedAssessment.getQuality();
        updatedMetric.setVoted(voteProperty);

        return updatedMetric;
    }

    public DifficultyMetric voteOnDifficulty(String voteProperty, String username, String id) {
        Assessment assessment = assessmentRepository.findOne(id);
        DifficultyMetric difficultyMetric = assessment.getDifficulty();
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
                    mongoTemplate.findAndModify(query, update, Assessment.class);
                }

                break;
            case "average":
                if (!dv.getAverage().contains(username)) {
                    update.inc("difficulty.votes.average", 1);
                    update.addToSet("difficulty.voters.average", username);
                    mongoTemplate.findAndModify(query, update, Assessment.class);
                }

                break;
            case "hard":
                if (!dv.getHard().contains(username)) {
                    update.inc("difficulty.votes.hard", 1);
                    update.addToSet("difficulty.voters.hard", username);
                    mongoTemplate.findAndModify(query, update, Assessment.class);
                }

                break;
        }

        Assessment updatedAssessment = assessmentRepository.findOne(id);
        DifficultyMetric updatedMetric = updatedAssessment.getDifficulty();
        updatedMetric.setVoted(voteProperty);

        return updatedMetric;
    }
}
