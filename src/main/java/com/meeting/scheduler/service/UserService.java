package com.meeting.scheduler.service;

import com.meeting.scheduler.entity.User;
import com.meeting.scheduler.exception.UserNotFoundException;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id) throws UserNotFoundException;
    User createUser(User user);
    User updateUser(Long id, User userDetails) throws UserNotFoundException;
    void deleteUser(Long id) throws UserNotFoundException;
}
