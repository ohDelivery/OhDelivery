package com.ohdelivery.service.user.domain.repository;

import com.ohdelivery.service.user.domain.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    User save(User user);

    User findById(Long id);

    User findByLogin(String username, String password);
}
