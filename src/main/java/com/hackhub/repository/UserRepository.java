package com.hackhub.repository;

import com.hackhub.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import java.util.Optional;

public class UserRepository implements PanacheRepository<User> {

    public Optional<User> findByUsername(String username) {
        return find("username", username).firstResultOptional();
    }
    
}
