package com.questizzle.repositories;

import com.questizzle.models.Assessment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Danny on 3/30/2017.
 */
public interface AssessmentRepository extends MongoRepository<Assessment, String> {

    List<Assessment> findByNameContainingIgnoreCase(String query);

    List<Assessment> findByPortalId(String portal_id);

}
