package com.patika.paycore.service;

import com.patika.paycore.model.ApiResponse;
import com.patika.paycore.model.request.UserCreateRequest;
import com.patika.paycore.model.request.UserUpdateRequest;
import com.patika.paycore.model.response.UserResponse;

public interface UserService {

    UserResponse createUser(UserCreateRequest request);

    UserResponse updateUser(UserUpdateRequest request);

    void deleteUser(String identityNumber);

    UserResponse getUser(String identityNumber);

}
