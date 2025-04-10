package com.ohdelivery.service.user.infrastructure.persistence;

import com.ohdelivery.service.user.domain.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdAndDeletedAtIsNull(Long id);

    Optional<User> findByUsernameAndDeletedAtIsNull(String username);
}
