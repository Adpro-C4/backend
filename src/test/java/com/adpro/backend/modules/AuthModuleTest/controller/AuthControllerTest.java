package com.adpro.backend.modules.AuthModuleTest.controller;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import java.io.IOException;

import com.adpro.backend.modules.authmodule.controller.AuthController;
import com.adpro.backend.modules.authmodule.enums.UserType;
import com.adpro.backend.modules.authmodule.model.Admin;
import  com.adpro.backend.modules.authmodule.model.Customer;
import com.adpro.backend.modules.authmodule.model.RegistrationRequest;
import com.adpro.backend.modules.authmodule.service.UserService;



@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@AutoConfigureMockMvc
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;
    @Mock
    private UserService<Admin> adminService;

    @Mock
    private UserService<Customer> customerService;


    Admin admin;
    Customer customer;

    @BeforeEach
    void setUp(){
        admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("adminpass");
        admin.setRole("ADMIN");
        admin.setEmail("adminmail@gmail.com");

        
        customer = new Customer();
        customer.setUsername("customer");
        customer.setPassword("customerpass");
        customer.setRole("CUSTOMER");
        customer.setEmail("customer@email.com");
        customer.setName("ucok");
        customer.setPhoneNumber("089234512321");

    }

    @AfterEach
    void tearDown(){
        if(adminService.findByUsername(admin.getUsername()) != null) adminService.removeUser(admin);
        if(customerService.findByUsername(customer.getUsername()) != null) customerService.removeUser(customer);
    }



    @Test
    public void testLoginAdminSuccess() throws Exception {
        when(adminService.authenticateUser(anyString(), anyString())).thenReturn(true);
        when(adminService.findByUsername(anyString())).thenReturn(admin);
        JsonNode requestBody = createJsonNode("{\"username\":\"admin\",\"password\":\"adminpass\",\"role\":\"ADMIN\"}");
        ResponseEntity<Object> responseEntity = authController.login(requestBody, UserType.ADMIN);

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    private JsonNode createJsonNode(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(json);
    }

    @Test
    public void testLoginCustomerSuccess() throws Exception {
        when(customerService.authenticateUser(anyString(), anyString())).thenReturn(true);
        when(customerService.findByUsername(anyString())).thenReturn(customer);

        JsonNode requestBody = createJsonNode("{\"username\":\"customer\",\"password\":\"customerpass\",\"role\":\"CUSTOMER\"}");
        ResponseEntity<Object> responseEntity = authController.login(requestBody, UserType.CUSTOMER);
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testLoginAdminFailured() throws Exception {
        when(adminService.authenticateUser(anyString(), anyString())).thenReturn(false);
        when(adminService.findByUsername(anyString())).thenReturn(admin);
        JsonNode requestBody = createJsonNode("{\"username\":\"admin\",\"password\":\"adminpass\",\"role\":\"ADMIN\"}");
        ResponseEntity<Object> responseEntity = authController.login(requestBody, UserType.ADMIN);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testLoginCustomerFailured() throws Exception {
        when(customerService.authenticateUser(anyString(), anyString())).thenReturn(false);
        when(customerService.findByUsername(anyString())).thenReturn(customer);
        JsonNode requestBody = createJsonNode("{\"username\":\"customer\",\"password\":\"customerpass\",\"role\":\"CUSTOMER\"}");
        ResponseEntity<Object> responseEntity = authController.login(requestBody, UserType.CUSTOMER);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testRegisterAdminSuccess() throws Exception {
        String passwordConfirmation = admin.getPassword();
        when(adminService.addUser(any(Admin.class))).thenReturn(admin);
        RegistrationRequest<Admin> registrationRequest = new RegistrationRequest<>();
        registrationRequest.setUser(admin);
        registrationRequest.setPasswordConfirmation(passwordConfirmation);
        ResponseEntity<?> responseEntity = authController.registerAdmin(registrationRequest);
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    @Test
    public void testRegisterCustomerSuccess() throws Exception {
        String passwordConfirmation = customer.getPassword();
        when(customerService.addUser(any(Customer.class))).thenReturn(customer);
        RegistrationRequest<Customer> registrationRequest = new RegistrationRequest<>();
        registrationRequest.setUser(customer);
        registrationRequest.setPasswordConfirmation(passwordConfirmation);
        ResponseEntity<?> responseEntity = authController.registerCustomer(registrationRequest);
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    @Test
    public void testRegisterAdminFailured() throws Exception {
        String passwordConfirmation = "";
        when(adminService.addUser(any(Admin.class))).thenReturn(admin);
        RegistrationRequest<Admin> registrationRequest = new RegistrationRequest<>();
        registrationRequest.setUser(admin);
        registrationRequest.setPasswordConfirmation(passwordConfirmation);
        ResponseEntity<?> responseEntity = authController.registerAdmin(registrationRequest);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void testRegisterCustomerFailured() throws Exception {
        String passwordConfirmation = "";
        when(customerService.addUser(any(Customer.class))).thenReturn(customer);
        RegistrationRequest<Customer> registrationRequest = new RegistrationRequest<>();
        registrationRequest.setUser(customer);
        registrationRequest.setPasswordConfirmation(passwordConfirmation);
        ResponseEntity<?> responseEntity = authController.registerCustomer(registrationRequest);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    void testLogoutSuccess() {

        String validToken = "valid_token";
        ResponseEntity<?> responseEntity = authController.logout(validToken);
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    @Test
    void testLogoutTokenFailed() {
        String invalidToken = null;
        ResponseEntity<?> responseEntity = authController.logout(invalidToken);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}

