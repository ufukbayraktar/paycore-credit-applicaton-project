package com.patika.paycore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patika.paycore.TestInitializer;
import com.patika.paycore.entity.User;
import com.patika.paycore.model.ApiErrorResponse;
import com.patika.paycore.model.ApiResponse;
import com.patika.paycore.model.request.UserCreateRequest;
import com.patika.paycore.model.request.UserUpdateRequest;
import com.patika.paycore.model.response.UserResponse;
import com.patika.paycore.repository.ApplicationRepository;
import com.patika.paycore.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Optional;

import static com.patika.paycore.exception.ApiErrorType.FIELD_VALIDATION_ERROR;
import static com.patika.paycore.exception.ApiErrorType.USER_NOT_FOUND_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ContextConfiguration(initializers = TestInitializer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {


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
                .identityNumber("72068634466")
                .name("sample-name")
                .surname("sample-surname")
                .phoneNumber("5685681144")
                .salary(new BigDecimal(5000))
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Object> request = new HttpEntity<>(userCreateRequest, httpHeaders);

        // Act
        ResponseEntity<ApiResponse> responseEntity = restTemplate.withBasicAuth(userName, password).postForEntity("/api/user/create", request, ApiResponse.class);
        UserResponse userResponse = objectMapper.convertValue(responseEntity.getBody().getData(), UserResponse.class);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("sample-name", userResponse.getName());
        assertEquals(new BigDecimal(5000), userResponse.getSalary());
    }

    @Test
    public void createUsersShouldReturnUserAlreadyExistsExceptionWhenUserExists() {
        // Arrange
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .identityNumber("15906301892")
                .name("sample-name")
                .surname("sample-surname")
                .phoneNumber("5685681144")
                .salary(new BigDecimal(5000))
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Object> request = new HttpEntity<>(userCreateRequest, httpHeaders);

        // Act
        ResponseEntity<ApiErrorResponse> responseEntity = restTemplate.withBasicAuth(userName, password).postForEntity("/api/user/create", request, ApiErrorResponse.class);
        ApiErrorResponse apiErrorResponse = objectMapper.convertValue(responseEntity.getBody(), ApiErrorResponse.class);

        // Assert
        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
        assertEquals(String.valueOf(2004), apiErrorResponse.getCode());
        assertEquals("User Already Exists Exception", apiErrorResponse.getMessage());
    }

    @Test
    public void createUsersShouldReturnUnauthorizedWhenWithoutBasicAuth() {
        // Arrange
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .identityNumber("15906301892")
                .name("sample-name")
                .surname("sample-surname")
                .phoneNumber("5685681144")
                .salary(new BigDecimal(5000))
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Object> request = new HttpEntity<>(userCreateRequest, httpHeaders);

        // Act
        ResponseEntity<Object> responseEntity = restTemplate.postForEntity("/api/user/create", request, Object.class);
        LinkedHashMap<String, String> responseBodyLinkedHashMap = (LinkedHashMap<String, String>) responseEntity.getBody();

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Authentication Exception", responseBodyLinkedHashMap.get("errorMessage"));
        assertEquals(String.valueOf(2003), responseBodyLinkedHashMap.get("errorCode"));
        assertEquals(HttpStatus.UNAUTHORIZED.toString(), responseBodyLinkedHashMap.get("errorHttpStatus"));
    }

    @Test
    public void createUsersShouldReturnBadRequestWhenRequestIsInvalid() {
        // Arrange
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .identityNumber("15906301892")
                .surname("sample-surname")
                .phoneNumber("5685681144")
                .salary(new BigDecimal(5000))
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Object> request = new HttpEntity<>(userCreateRequest, httpHeaders);

        // Act
        ResponseEntity<ApiErrorResponse> responseEntity = restTemplate.withBasicAuth(userName, password).postForEntity("/api/user/create", request, ApiErrorResponse.class);
        ApiErrorResponse apiErrorResponse = objectMapper.convertValue(responseEntity.getBody(), ApiErrorResponse.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(FIELD_VALIDATION_ERROR.getErrorCode(), Integer.valueOf(apiErrorResponse.getCode()));
        assertEquals(FIELD_VALIDATION_ERROR.getErrorMessage(), apiErrorResponse.getMessage());
    }

    @Test
    public void updateUsersShouldReturnUserResponseWhenRequestIsValid() {
        // Arrange
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .identityNumber("15906301892")
                .name("sample-update-name")
                .surname("sample-update-surname")
                .phoneNumber("3333333333")
                .salary(new BigDecimal(23000))
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Object> request = new HttpEntity<>(userUpdateRequest, httpHeaders);

        // Act
        ResponseEntity<ApiResponse> responseEntity = restTemplate.withBasicAuth(userName, password).exchange("/api/user/update", HttpMethod.PUT, request, ApiResponse.class);
        UserResponse userResponse = objectMapper.convertValue(responseEntity.getBody().getData(), UserResponse.class);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("sample-update-name", userResponse.getName());
        assertEquals(new BigDecimal(23000), userResponse.getSalary());
    }

    @Test
    public void updateUsersShouldReturnUnauthorizedWhenWithoutBasicAuth() {
        // Arrange
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .identityNumber("15906301892")
                .name("sample-update-name")
                .surname("sample-update-surname")
                .phoneNumber("3333333333")
                .salary(new BigDecimal(23000))
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Object> request = new HttpEntity<>(userUpdateRequest, httpHeaders);

        // Act
        ResponseEntity<Object> responseEntity = restTemplate.exchange("/api/user/update", HttpMethod.PUT, request, Object.class);
        LinkedHashMap<String, String> responseBodyLinkedHashMap = (LinkedHashMap<String, String>) responseEntity.getBody();

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Authentication Exception", responseBodyLinkedHashMap.get("errorMessage"));
        assertEquals(String.valueOf(2003), responseBodyLinkedHashMap.get("errorCode"));
        assertEquals(HttpStatus.UNAUTHORIZED.toString(), responseBodyLinkedHashMap.get("errorHttpStatus"));
    }

    @Test
    public void updateUsersShouldReturnBadRequestWhenRequestIsInvalid() {
        // Arrange
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .name("sample-update-name")
                .surname("sample-update-surname")
                .phoneNumber("3333333333")
                .salary(new BigDecimal(23000))
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Object> request = new HttpEntity<>(userUpdateRequest, httpHeaders);

        // Act
        ResponseEntity<ApiErrorResponse> responseEntity = restTemplate.withBasicAuth(userName, password).exchange("/api/user/update", HttpMethod.PUT, request, ApiErrorResponse.class);
        ApiErrorResponse apiErrorResponse = objectMapper.convertValue(responseEntity.getBody(), ApiErrorResponse.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(FIELD_VALIDATION_ERROR.getErrorCode(), Integer.valueOf(apiErrorResponse.getCode()));
        assertEquals(FIELD_VALIDATION_ERROR.getErrorMessage(), apiErrorResponse.getMessage());
    }

    @Test
    public void deleteUserShouldDeleteUserByIdentityNumberWhenRequestIsValid() {
        //Arrange

        //Act
        ResponseEntity<ApiResponse> responseEntity = restTemplate.withBasicAuth(userName, password).exchange("/api/user/delete/15906301892", HttpMethod.DELETE, null, ApiResponse.class);
        Optional<User> user = userRepository.findByIdentityNumber("15906301892");

        //Assert
        assertEquals(false, user.isPresent());

    }

    @Test
    public void deleteUserShouldReturnUnauthorizedWhenWithoutBasicAuth() {
        //Arrange

        //Act
        ResponseEntity<Object> responseEntity = restTemplate.exchange("/api/user/delete/11111111111", HttpMethod.DELETE, null, Object.class);
        LinkedHashMap<String, String> responseBodyLinkedHashMap = (LinkedHashMap<String, String>) responseEntity.getBody();

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Authentication Exception", responseBodyLinkedHashMap.get("errorMessage"));
        assertEquals(String.valueOf(2003), responseBodyLinkedHashMap.get("errorCode"));
        assertEquals(HttpStatus.UNAUTHORIZED.toString(), responseBodyLinkedHashMap.get("errorHttpStatus"));

    }

    @Test
    public void deleteUserShouldReturnNotFoundWhenRequestIsInvalid() {
        //Arrange

        //Act
        ResponseEntity<ApiErrorResponse> responseEntity = restTemplate.withBasicAuth(userName, password).exchange("/api/user/delete/23232323232", HttpMethod.DELETE, null, ApiErrorResponse.class);
        ApiErrorResponse apiErrorResponse = objectMapper.convertValue(responseEntity.getBody(), ApiErrorResponse.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(USER_NOT_FOUND_ERROR.getErrorCode(), Integer.valueOf(apiErrorResponse.getCode()));
        assertEquals(USER_NOT_FOUND_ERROR.getErrorMessage(), apiErrorResponse.getMessage());

    }

    @Test
    public void getUsersShouldReturnUserResponseWhenRequestIsValid() {
        // Arrange

        //Act
        ResponseEntity<ApiResponse> responseEntity = restTemplate.withBasicAuth(userName, password).getForEntity("/api/user/getUser/15906301892", ApiResponse.class);
        UserResponse userResponse = objectMapper.convertValue(responseEntity.getBody().getData(), UserResponse.class);


        //Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("name1", userResponse.getName());
        assertEquals(new BigDecimal("55000.0"), userResponse.getSalary());
    }

    @Test
    public void getUsersShouldReturnUnauthorizedWhenWithoutBasicAuth() {
        // Arrange

        //Act
        ResponseEntity<Object> responseEntity = restTemplate.getForEntity("/api/user/getUser/11111111111", Object.class);
        LinkedHashMap<String, String> responseBodyLinkedHashMap = (LinkedHashMap<String, String>) responseEntity.getBody();

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Authentication Exception", responseBodyLinkedHashMap.get("errorMessage"));
        assertEquals(String.valueOf(2003), responseBodyLinkedHashMap.get("errorCode"));
        assertEquals(HttpStatus.UNAUTHORIZED.toString(), responseBodyLinkedHashMap.get("errorHttpStatus"));
    }

    @Test
    public void getUsersShouldReturnNotFoundWhenRequestIsInvalid() {
        // Arrange

        //Act
        ResponseEntity<ApiErrorResponse> responseEntity = restTemplate.withBasicAuth(userName, password).getForEntity("/api/user/getUser/45645645645", ApiErrorResponse.class);
        ApiErrorResponse apiErrorResponse = objectMapper.convertValue(responseEntity.getBody(), ApiErrorResponse.class);


        //Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(USER_NOT_FOUND_ERROR.getErrorCode(), Integer.valueOf(apiErrorResponse.getCode()));
        assertEquals(USER_NOT_FOUND_ERROR.getErrorMessage(), apiErrorResponse.getMessage());
    }

    private void generateUsersApplicationsAndScores() {
        String userSql = "INSERT INTO users" +
                "(id, created_date, updated_date, version, identity_number, name, phone_number, salary, surname)" +
                "VALUES(1, now(), NULL, 0, '15906301892', 'name1', '5555555555', 55000.00, 'surname1');" +
                "INSERT INTO users" +
                "(id, created_date, updated_date, version, identity_number, name, phone_number, salary, surname)" +
                "VALUES(2, now(), NULL, 0, '39940637090', 'name2', '6666666666', 4000.00, 'surname2');" +
                "INSERT INTO users" +
                "(id, created_date, updated_date, version, identity_number, name, phone_number, salary, surname)" +
                "VALUES(3, now(), NULL, 0, '40940737598', 'name3', '7777777777', 77000.00, 'surname3');" +
                "INSERT INTO users" +
                "(id, created_date, updated_date, version, identity_number, name, phone_number, salary, surname)" +
                "VALUES(4, now(), NULL, 0, '20680889696', 'name4', '8888888888', 88000.00, 'surname4');";

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
                "VALUES(1, now(),NULL, NULL, '15906301892', 750);" +
                "INSERT INTO score" +
                "(id, created_date, updated_date, version, identity_number, score)" +
                "VALUES(2, now(),NULL, NULL, '39940637090', 1200);";

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
