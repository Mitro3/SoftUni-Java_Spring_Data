package com.example.gamestore;

import com.example.gamestore.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Scanner;

import static com.example.gamestore.constants.Commands.*;

@Service
public class ConsoleRunner implements CommandLineRunner {
    private static final Scanner scanner = new Scanner(System.in);
    private final UserService userService;

    @Autowired
    public ConsoleRunner(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        String input = scanner.nextLine();

        while (!input.equals("close")) {

            final String[] arguments = input.split("\\|");
            final String command = arguments[0];

            final String output = switch (command) {
                case REGISTER_USER -> userService.registerUser(arguments);
                case LOGIN_USER -> userService.loginUser(arguments);
                case LOGOUT_USER -> userService.logoutUser();
                default -> "No command found";
            };

            System.out.println(output);
            input = scanner.nextLine();
        }

    }
}
