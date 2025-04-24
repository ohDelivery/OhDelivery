package com.ohdelivery.service.user.application;

import com.ohdelivery.common.passport.RoleType;
import com.ohdelivery.service.user.application.command.CreateUserCommand;
import com.ohdelivery.service.user.application.command.UpdateSlackIdCommand;
import com.ohdelivery.service.user.application.command.UserLoginCommand;
import com.ohdelivery.service.user.application.exception.UserException.IncorrectPasswordException;
import com.ohdelivery.service.user.domain.model.User;
import com.ohdelivery.service.user.domain.repository.UserRepository;
import com.ohdelivery.service.user.presentation.response.GetUserResponse;
import com.ohdelivery.service.user.presentation.response.UserAuthResponse;
import com.ohdelivery.service.user.presentation.response.UserLoginResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEventPublisher userEventPublisher;

    @Transactional(readOnly = true)
    public UserLoginResponse login(UserLoginCommand userLoginCommand) {
        User user = userRepository.findByLogin(
                userLoginCommand.getUsername(),
                userLoginCommand.getPassword());

        if (!passwordEncoder.matches(userLoginCommand.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        }

        return UserLoginResponse.create(user.getId(), user.getRole());
    }

    @Transactional(readOnly = true)
    public UserAuthResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId);

        return UserAuthResponse.create(user.getId(), user.getRole());
    }

    @Transactional(readOnly = true)
    public GetUserResponse getUser(Long userId){
        User user = userRepository.findById(userId);
        return GetUserResponse.create(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getSlackId()
        );
    }

    public void createUser(CreateUserCommand command) {
        User user = userRepository.save(User.create(
                command.getUsername(),
                command.getName(),
                passwordEncoder.encode(command.getPassword()),
                command.getRole(),
                command.getSlackId()
        ));

        if (user.getRole() == RoleType.RIDER){
            userEventPublisher.produceCreateUser(user.getId(), user.getSlackId());
        }
    }

    public void updateSlackId(UpdateSlackIdCommand command, Long userId) {
        User user = userRepository.findById(userId);
        user.updateSlackId(command.getSlackId());

        if (user.getRole() == RoleType.RIDER) {
            userEventPublisher.produceUpdateSlackId(user.getId(), user.getSlackId());
        }
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId);
        user.delete(LocalDateTime.now(), String.valueOf(userId));

        if (user.getRole() == RoleType.RIDER) {
            userEventPublisher.produceDeleteUser(userId);
        }
    }
}
