package com.ohdelivery.service.user.infrastructure.persistence;

import com.ohdelivery.service.user.domain.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdAndIsDeletedIsFalse(Long id);

    Optional<User> findByUsernameAndIsDeletedIsFalse(String username);
}
