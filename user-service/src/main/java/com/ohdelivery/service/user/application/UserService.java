package com.ohdelivery.service.user.application;

import com.ohdelivery.service.user.application.command.CreateUserCommand;
import com.ohdelivery.service.user.application.command.UserLoginCommand;
import com.ohdelivery.service.user.application.exception.UserException.IncorrectPasswordException;
import com.ohdelivery.service.user.domain.model.User;
import com.ohdelivery.service.user.domain.repository.UserRepository;
import com.ohdelivery.service.user.presentation.response.UserAuthResponse;
import com.ohdelivery.service.user.presentation.response.UserLoginResponse;
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

    @Transactional(readOnly = true)
    public UserLoginResponse login(UserLoginCommand userLoginCommand) {
        User user = userRepository.findByLogin(
                userLoginCommand.getUsername(),
                userLoginCommand.getPassword());

        if (!passwordEncoder.matches(userLoginCommand.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        }

        return new UserLoginResponse(user.getId(), user.getRole());
    }

    @Transactional(readOnly = true)
    public UserAuthResponse getUser(Long id) {
        User user = userRepository.findById(id);

        return new UserAuthResponse(user.getId(), user.getRole());
    }

    public void createUser(CreateUserCommand command) {
        userRepository.save(User.create(
                command.getUsername(),
                command.getName(),
                passwordEncoder.encode(command.getPassword()),
                command.getRole(),
                command.getSlackId()
        ));
    }

    public void deleteUser(Long id) {
        userRepository.findById(id).delete();
    }
}
