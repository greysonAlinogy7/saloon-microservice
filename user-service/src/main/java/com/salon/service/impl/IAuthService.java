package com.salon.service.impl;

import com.salon.entity.User;

import java.util.List;

public interface IAuthService {

    User getUserByProfile(String jwt);

}
