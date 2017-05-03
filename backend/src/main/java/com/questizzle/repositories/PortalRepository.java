package com.questizzle.repositories;

import com.questizzle.models.Portal;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

/**
 * Created by danny.grullon on 3/18/17.
 */
public interface PortalRepository extends MongoRepository<Portal, String> {

    /**
     * The below find method is the equivalent to the following:
     * db.portal.find({ '$or': [ { 'name': /query/ }, { 'description': /query/ } ] }).pretty()
     */
    List<Portal> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String nameQuery, String descriptionQuery);

}
