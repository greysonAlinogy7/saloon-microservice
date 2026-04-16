package com.salon.service.impl;



import com.salon.entity.User;
import com.salon.exception.UserException;

import java.util.List;

public interface IUserService {
    User createUser(User user);
    User getUserById(Long userId) throws UserException;
    List<User> getAllUser();
    void deleteUser(Long userId) throws UserException;
    User updateUser(Long userId, User user) throws UserException;


}
