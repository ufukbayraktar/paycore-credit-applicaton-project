package com.patika.paycore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patika.paycore.TestInitializer;
import com.patika.paycore.entity.Application;
import com.patika.paycore.model.ApiResponse;
import com.patika.paycore.model.request.UserCreateRequest;
import com.patika.paycore.model.request.UserUpdateRequest;
import com.patika.paycore.model.response.UserResponse;
import com.patika.paycore.repository.ApplicationRepository;
import com.patika.paycore.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.assertEquals;



import java.math.BigDecimal;



@ContextConfiguration(initializers = TestInitializer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    TestRestTemplate restTemplate;

    @SpyBean
    UserRepository userRepository;

    @SpyBean
    ApplicationRepository applicationRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    ObjectMapper objectMapper;

    @Value("${app.security.user.name}")
    private String userName;

    @Value("${app.security.user.password}")
    private String password;


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
    public void createUsersShouldReturnUserResponseWhenRequestIsValid() {
        // Arrange
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .identityNumber("12345678910")
                .name("sample-name")
                .surname("sample-surname")
                .phoneNumber("5685681144")
                .salary(new BigDecimal(5000))
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.ACCEPT,MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Object> request = new HttpEntity<>(userCreateRequest,httpHeaders);

        // Act
        ResponseEntity<ApiResponse> responseEntity = restTemplate.withBasicAuth(userName,password).postForEntity("/api/user/create",request,ApiResponse.class);
        UserResponse userResponse = objectMapper.convertValue(responseEntity.getBody().getData(),UserResponse.class);

        // Assert
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals("sample-name",userResponse.getName());
        assertEquals(new BigDecimal(5000),userResponse.getSalary());
    }

    @Test
    public void updateUsersShouldReturnUserResponseWhenRequestIsValid() {
        // Arrange
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .identityNumber("11111111111")
                .name("sample-update-name")
                .surname("sample-update-surname")
                .phoneNumber("3333333333")
                .salary(new BigDecimal(23000))
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.ACCEPT,MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Object> request = new HttpEntity<>(userUpdateRequest,httpHeaders);

        // Act
        ResponseEntity<ApiResponse> responseEntity = restTemplate.withBasicAuth(userName,password).exchange("/api/user/update",HttpMethod.PUT,request,ApiResponse.class);
        UserResponse userResponse = objectMapper.convertValue(responseEntity.getBody().getData(),UserResponse.class);

        // Assert
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals("sample-update-name",userResponse.getName());
        assertEquals(new BigDecimal(23000),userResponse.getSalary());
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
                "VALUES(4, now(), NULL, 0, 0, 0.00, 4);";

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
