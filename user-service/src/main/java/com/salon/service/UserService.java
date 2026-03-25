package com.salon.service;

import com.salon.entity.User;
import com.salon.exception.UserException;
import com.salon.repository.UserRepository;
import com.salon.service.impl.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private  final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long userId) throws UserException {
        Optional<User> opt = userRepository.findById(userId);
        if (opt.isPresent()){
            return opt.get();
        }
        throw  new UserException("user not found");
    }

    @Override
    public List<User> getAllUser() {
        List<User> userList = userRepository.findAll();

        return userList;
    }

    @Override
    public void deleteUser(Long userId) throws UserException {
        Optional<User> otp = userRepository.findById(userId);
        if (otp.isEmpty()){
            throw  new UserException("user not found with id" + userId);
        }
        userRepository.deleteById(otp.get().getId());


    }

    @Override
    public User updateUser(Long userId, User user) throws UserException {
        Optional<User> otp = userRepository.findById(userId);
        if (otp.isEmpty()){
            throw new UserException("user not found with id" + userId);
        }

        User  existingUser = otp.get();
        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setRole(user.getRole());
        existingUser.setUsername(user.getUsername());
        return userRepository.save(existingUser);
    }
}
