package com.example.gamestore.services.user;

import com.example.gamestore.domain.dtos.UserLogin;
import com.example.gamestore.domain.dtos.UserRegister;
import com.example.gamestore.domain.entities.User;
import com.example.gamestore.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.gamestore.constants.EmailValidation.USERNAME_OR_PASSWORD_NOT_VALID_MESSAGE;

@Service
public class UserServiceImpl implements UserService {
    private User user;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String registerUser(String[] args) throws IllegalAccessException {
        String email = args[1];
        String password = args[2];
        String confirmPassword = args[3];
        String fullName = args[4];

        UserRegister userRegister = new UserRegister(email, password, confirmPassword, fullName);
        User user = this.modelMapper.map(userRegister, User.class);

        if (this.userRepository.count() == 0) {
            user.setIsAdmin(true);
        }

        boolean isUserPresent = this.userRepository.findByEmail(userRegister.getEmail()).isPresent();

        if (isUserPresent) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        this.userRepository.save(user);

        return userRegister.successfulRegisterFormat();
    }

    @Override
    public String loginUser(String[] args) {
        String email = args[1];
        String password = args[2];

        UserLogin userLogin = new UserLogin(email, password);

        Optional<User> user = this.userRepository.findByEmail(userLogin.getEmail());

        if (user.isPresent() && this.user == null && user.get().getPassword().equals(userLogin.getPassword())) {
            this.user = this.userRepository.findByEmail(email).get();
            return "Successfully logged in " + this.user.getFullName();
        }

        return USERNAME_OR_PASSWORD_NOT_VALID_MESSAGE;
    }

    @Override
    public String logoutUser() {
        if (this.user == null) {
            return "Cannot log out. No user was logged in.";
        }

        String output = "User " + this.user.getFullName() + " successfully logged out.";

        this.user = null;

        return output;
    }
}
