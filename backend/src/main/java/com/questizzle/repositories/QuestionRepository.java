package com.questizzle.repositories;

import com.questizzle.models.Question;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by danny.grullon on 3/18/17.
 */
public interface QuestionRepository extends MongoRepository<Question, String> {

    List<Question> findByPortalIdAndTextContainingIgnoreCase(String id, String contentQuery);

    List<Question> findByPortalId(String id);

}
