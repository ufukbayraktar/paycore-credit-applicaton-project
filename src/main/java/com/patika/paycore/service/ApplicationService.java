package com.patika.paycore.service;

import com.patika.paycore.model.request.ApplicationRequest;
import com.patika.paycore.model.response.ApplicationResponse;

import java.util.List;

public interface ApplicationService {
    ApplicationResponse createApplication(ApplicationRequest request);

    List<ApplicationResponse> getStatus(String identityNumber);

}
