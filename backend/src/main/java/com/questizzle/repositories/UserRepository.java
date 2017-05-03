package com.questizzle.repositories;

import com.questizzle.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Danny on 3/20/2017.
 */
public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String username);

    User findByEmail(String email);

    User findByIdentityAndConfirmationTokenAndIsEmailConfirmed(String id, String confirmationToken, Boolean confirmed);

}
