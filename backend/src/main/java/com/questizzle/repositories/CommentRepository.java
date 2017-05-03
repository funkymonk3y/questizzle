package com.questizzle.repositories;

import com.questizzle.models.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Danny on 3/29/2017.
 */
public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findByQuestionIdOrderByPostedDesc(String questionId);

    List<Comment> findByAssessmentIdOrderByPostedDesc(String assessmentId);

}
