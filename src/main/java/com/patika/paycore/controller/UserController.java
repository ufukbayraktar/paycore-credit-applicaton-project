package com.patika.paycore.controller;

import com.patika.paycore.model.ApiResponse;
import com.patika.paycore.model.request.UserCreateRequest;
import com.patika.paycore.model.request.UserUpdateRequest;
import com.patika.paycore.model.response.UserResponse;
import com.patika.paycore.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        log.info("createUser called with request: {}", request);
        UserResponse response = userService.createUser(request);
        return ApiResponse.<UserResponse>builder()
                .data(response)
                .status("0")
                .message("success")
                .build();
    }

    @PutMapping("/update")
    public ApiResponse<UserResponse> updateUser(@Valid @RequestBody UserUpdateRequest request) {
        log.info("updateUser called with request: {}", request);
        UserResponse response = userService.updateUser(request);
        return ApiResponse.<UserResponse>builder()
                .data(response)
                .status("0")
                .message("success")
                .build();
    }

    @DeleteMapping("/delete/{identityNumber}")
    public ApiResponse<Void> deleteUser(@PathVariable String identityNumber) {
        log.info("deleteUser called with identityNumber: {}", identityNumber);
        userService.deleteUser(identityNumber);
        return ApiResponse.<Void>builder()
                .status("0")
                .message("success")
                .build();
    }

    @GetMapping("/getUser/{identityNumber}")
    public ApiResponse<UserResponse> getUser(@PathVariable String identityNumber) {
        log.info("getUser called with identityNumber: {}", identityNumber);
        UserResponse response = userService.getUser(identityNumber);
        return ApiResponse.<UserResponse>builder()
                .data(response)
                .status("0")
                .message("success")
                .build();
    }
}
