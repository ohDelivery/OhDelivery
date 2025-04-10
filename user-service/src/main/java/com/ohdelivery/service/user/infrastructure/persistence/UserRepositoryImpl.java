package com.ohdelivery.service.user.infrastructure.persistence;

import com.ohdelivery.service.user.application.exception.UserException.UserAlreadyExistsException;
import com.ohdelivery.service.user.application.exception.UserException.UserNotFoundException;
import com.ohdelivery.service.user.domain.model.User;
import com.ohdelivery.service.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        try {
            return userJpaRepository.save(user);
        } catch (ConstraintViolationException e) {
            throw new UserAlreadyExistsException();
        }
    }

    @Override
    public User findById(Long id) {
        return userJpaRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User findByLogin(String username, String password) {
        return userJpaRepository.findByUsernameAndDeletedAtIsNull(username)
                .orElseThrow(UserNotFoundException::new);
    }
}
