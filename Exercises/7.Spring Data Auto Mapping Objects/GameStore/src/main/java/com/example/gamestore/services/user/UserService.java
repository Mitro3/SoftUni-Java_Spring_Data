package com.example.gamestore.services.user;

public interface UserService {
    String registerUser(String[] args) throws IllegalAccessException;

    String loginUser(String[] args);

    String logoutUser ();
}
