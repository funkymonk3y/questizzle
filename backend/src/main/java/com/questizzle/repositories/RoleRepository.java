package com.questizzle.repositories;

import com.questizzle.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Danny on 3/21/2017.
 */
public interface RoleRepository extends MongoRepository<Role, String> {
}
