package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {
    private static final Logger log = LoggerFactory.getLogger(UserControllerTest.class);

    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class); // creation of mock object
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    // every before single test
    @BeforeEach
    public void setUp(){
        log.info("-- Init --");
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    // sanity test for user creation
    @Test
    @Order(1)
    @DisplayName("Check user successfully")
    public void createUserHappyPath() throws Exception{
        log.info("-- Start of test  --");
        final ResponseEntity<User> responseEntity = createUser();

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        User user = responseEntity.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
        log.info("-- End of test  --");
    }

    @Test
    @Order(2)
    @DisplayName("Check findById method")
    public void checkGetUserByID(){
        log.info("-- Start of test  --");
        final ResponseEntity<User> responseEntity = createUser();
        User user = responseEntity.getBody();
        when(userRepository.findById(user.getId())).thenReturn(java.util.Optional.of(user));
        ResponseEntity<User> responseFind = userController.findById(user.getId());
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(responseFind.getBody().getId(), user.getId());
        log.info("-- End of test  --");
    }

    @Test
    @Order(3)
    @DisplayName("Check findByUserName method")
    public void checkGetUserByUsername(){
        log.info("-- Start of test  --");
        final ResponseEntity<User> responseEntity = createUser();
        User user = responseEntity.getBody();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        ResponseEntity<User> responseFind = userController.findByUserName(user.getUsername());
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(responseFind.getBody().getUsername(), user.getUsername());
        log.info("-- End of test  --");
    }

    public ResponseEntity<User> createUser(){
        log.info("User creation");
        User user = TestUtils.newUser();
        CreateUserRequest userRequest = new CreateUserRequest();
        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn("thisIsHashed"); // example of stubbing mockito
        userRequest.setUsername(user.getUsername());
        userRequest.setPassword(user.getPassword());
        userRequest.setConfirmPassword(user.getPassword());
        return userController.createUser(userRequest);
    }


}
