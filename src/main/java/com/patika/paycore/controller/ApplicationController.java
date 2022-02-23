package com.patika.paycore.controller;

import com.patika.paycore.model.ApiResponse;
import com.patika.paycore.model.request.ApplicationRequest;
import com.patika.paycore.model.response.ApplicationResponse;
import com.patika.paycore.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/application")
@RequiredArgsConstructor
@Slf4j
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/create")
    public ApiResponse<ApplicationResponse> createApplication(@Valid @RequestBody ApplicationRequest request) {
        log.info("createApplication called with request: {}", request);
        ApplicationResponse response = applicationService.createApplication(request);
        return ApiResponse.<ApplicationResponse>builder()
                .data(response)
                .status("0")
                .message("success")
                .build();

    }

    @GetMapping("/get-status/{identityNumber}")
    public ApiResponse<List<ApplicationResponse>> getStatus(@PathVariable String identityNumber) {
        log.info("getStatus called with this identityNumber: {}", identityNumber);
        List<ApplicationResponse> response = applicationService.getStatus(identityNumber);
        return ApiResponse.<List<ApplicationResponse>>builder()
                .data(response)
                .status("0")
                .message("success")
                .build();

    }
}
