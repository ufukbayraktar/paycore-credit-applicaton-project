package com.patika.paycore.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patika.paycore.TestInitializer;
import com.patika.paycore.enums.ApplicationStatus;
import com.patika.paycore.model.ApiErrorResponse;
import com.patika.paycore.model.ApiResponse;
import com.patika.paycore.model.request.ApplicationRequest;
import com.patika.paycore.model.response.ApplicationResponse;
import com.patika.paycore.repository.ApplicationRepository;
import com.patika.paycore.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.List;

import static com.patika.paycore.exception.ApiErrorType.FIELD_VALIDATION_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(initializers = TestInitializer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @SpyBean
    UserRepository userRepository;

    @SpyBean
    ApplicationRepository applicationRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        generateUsersApplicationsAndScores();

    }

    @AfterEach
    void tearDown() {
        clearDatabases();
    }

    @Test
    public void createApplicationShouldReturnRejectedApplicationResponseWhenRequestIsValid() {

        //Arrange
        ApplicationRequest applicationRequest = ApplicationRequest.builder()
                .identityNumber("11111111113")
                .name("name3")
                .surname("surname3")
                .phoneNumber("7777777777")
                .salary(new BigDecimal("77000")).build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Object> request = new HttpEntity<>(applicationRequest, httpHeaders);

        //Act
        ResponseEntity<ApiResponse> responseEntity = restTemplate.postForEntity("/api/application/create", request, ApiResponse.class);
        ApplicationResponse applicationResponse = objectMapper.convertValue(responseEntity.getBody().getData(), ApplicationResponse.class);

        //Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(ApplicationStatus.REJECTED, applicationResponse.getStatus());
        assertEquals(new BigDecimal("0"), applicationResponse.getCreditLimit());

    }

    @Test
    public void createApplicationShouldReturnNotFoundResponseWhenRequestIsInValid() {

        //Arrange
        ApplicationRequest applicationRequest = ApplicationRequest.builder()
                .identityNumber("11111111113")
                .name("name3")
                .phoneNumber("7777777777")
                .salary(new BigDecimal("77000")).build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Object> request = new HttpEntity<>(applicationRequest, httpHeaders);

        //Act
        ResponseEntity<ApiErrorResponse> responseEntity = restTemplate.postForEntity("/api/application/create", request, ApiErrorResponse.class);
        ApiErrorResponse apiErrorResponse = objectMapper.convertValue(responseEntity.getBody(), ApiErrorResponse.class);

        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(FIELD_VALIDATION_ERROR.getErrorCode(), Integer.valueOf(apiErrorResponse.getCode()));
        assertEquals(FIELD_VALIDATION_ERROR.getErrorMessage(), apiErrorResponse.getMessage());

    }

    @Test
    public void createApplicationShouldReturnConfirmedApplicationResponseWhenRequestIsValid() {
        //Arrange
        ApplicationRequest applicationRequest = ApplicationRequest.builder()
                .identityNumber("11111111111")
                .name("name1")
                .surname("surname1")
                .phoneNumber("5555555555")
                .salary(new BigDecimal("55000.00")).build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Object> request = new HttpEntity<>(applicationRequest, httpHeaders);

        //Act
        ResponseEntity<ApiResponse> responseEntity = restTemplate.postForEntity("/api/application/create", request, ApiResponse.class);
        ApplicationResponse applicationResponse = objectMapper.convertValue(responseEntity.getBody().getData(), ApplicationResponse.class);

        //Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(ApplicationStatus.CONFIRMED, applicationResponse.getStatus());
        assertEquals(new BigDecimal("20000"), applicationResponse.getCreditLimit());

    }

    @Test
    public void getStatusShouldReturnConfirmedApplicationResponseWhenRequestIsValid() {
        //Arrange

        //Act
        ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity("/api/application/get-status/11111111111", ApiResponse.class);
        List<ApplicationResponse> applicationResponses = objectMapper.convertValue(responseEntity.getBody().getData(), new TypeReference<List<ApplicationResponse>>() {
        });

        //Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, applicationResponses.size());
        assertEquals(ApplicationStatus.CONFIRMED, applicationResponses.get(0).getStatus());
        assertEquals(ApplicationStatus.CONFIRMED, applicationResponses.get(1).getStatus());
        assertEquals(new BigDecimal("20000.0"), applicationResponses.get(0).getCreditLimit());
        assertEquals(new BigDecimal("50000.0"), applicationResponses.get(1).getCreditLimit());
    }


    private void generateUsersApplicationsAndScores() {
        String userSql = "INSERT INTO users" +
                "(id, created_date, updated_date, version, identity_number, name, phone_number, salary, surname)" +
                "VALUES(1, now(), NULL, 0, '11111111111', 'name1', '5555555555', 55000.00, 'surname1');" +
                "INSERT INTO users" +
                "(id, created_date, updated_date, version, identity_number, name, phone_number, salary, surname)" +
                "VALUES(2, now(), NULL, 0, '11111111112', 'name2', '6666666666', 4000.00, 'surname2');" +
                "INSERT INTO users" +
                "(id, created_date, updated_date, version, identity_number, name, phone_number, salary, surname)" +
                "VALUES(3, now(), NULL, 0, '11111111113', 'name3', '7777777777', 77000.00, 'surname3');" +
                "INSERT INTO users" +
                "(id, created_date, updated_date, version, identity_number, name, phone_number, salary, surname)" +
                "VALUES(4, now(), NULL, 0, '11111111114', 'name4', '8888888888', 88000.00, 'surname4');";

        String applicationSql = "INSERT INTO application" +
                "(id, created_date, updated_date, version, application_status, credit_limit, user_id)" +
                "VALUES(1, now(), NULL, 0, 1, 20000.00, 1);" +
                "INSERT INTO application" +
                "(id, created_date, updated_date, version, application_status, credit_limit, user_id)" +
                "VALUES(2, now(), NULL, 0, 1, 16000.00, 2);" +
                "INSERT INTO application" +
                "(id, created_date, updated_date, version, application_status, credit_limit, user_id)" +
                "VALUES(3, now(), NULL, 0, 0, 0.00, 3);" +
                "INSERT INTO application" +
                "(id, created_date, updated_date, version, application_status, credit_limit, user_id)" +
                "VALUES(4, now(), NULL, 0, 0, 0.00, 4);" +
                "INSERT INTO application" +
                "(id, created_date, updated_date, version, application_status, credit_limit, user_id)" +
                "VALUES(5, now(), NULL, 0, 1, 50000.00, 1);";

        String scoreSql = "INSERT INTO score" +
                "(id, created_date, updated_date, version, identity_number, score)" +
                "VALUES(1, now(),NULL, NULL, '11111111111', 750);" +
                "INSERT INTO score" +
                "(id, created_date, updated_date, version, identity_number, score)" +
                "VALUES(2, now(),NULL, NULL, '11111111112', 1200);";

        jdbcTemplate.execute(userSql);
        jdbcTemplate.execute(applicationSql);
        jdbcTemplate.execute(scoreSql);
    }

    private void clearDatabases() {
        String deleteApplicationSql = "DELETE FROM application";
        String deleteUserSql = "DELETE FROM users";
        String deleteScoreSql = "DELETE FROM score";

        jdbcTemplate.execute(deleteApplicationSql);
        jdbcTemplate.execute(deleteUserSql);
        jdbcTemplate.execute(deleteScoreSql);
    }
}
