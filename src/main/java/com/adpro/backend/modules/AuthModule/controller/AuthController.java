package com.adpro.backend.modules.authmodule.controller;

import com.adpro.backend.modules.authmodule.enums.UserType;
import com.adpro.backend.modules.authmodule.model.AbstractUser;
import com.adpro.backend.modules.authmodule.model.Admin;
import com.adpro.backend.modules.authmodule.model.Customer;
import com.adpro.backend.modules.authmodule.model.RegistrationRequest;
import com.adpro.backend.modules.authmodule.provider.AuthProvider;
import com.adpro.backend.modules.authmodule.provider.JwtProvider;
import com.adpro.backend.modules.authmodule.service.UserService;
import com.adpro.backend.modules.commonmodule.util.ResponseHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private UserService<Admin> adminService;

    @Autowired
    private UserService<Customer> customerService;



    AuthController(){
    }

    @PostMapping("/login/admin")
    public ResponseEntity<Object> loginAdmin(@RequestBody JsonNode requestBody) {
        return login(requestBody, UserType.ADMIN);
    }

    @PostMapping("/login/customer")
    public ResponseEntity<Object> loginCustomer(@RequestBody JsonNode requestBody) {
        return login(requestBody, UserType.CUSTOMER);
    }

    public ResponseEntity<Object> login(JsonNode requestBody, UserType userType) {
        String username = requestBody.get("username").asText();
        String password = requestBody.get("password").asText();
        System.out.println(username);
        String role = userType.getUserType();
        boolean isValidAuthenticated = userType.equals(UserType.ADMIN) ?
                adminService.authenticateUser(username, password) :
                customerService.authenticateUser(username, password);
        if(!isValidAuthenticated){
            return ResponseHandler.generateResponse("Maaf username atau password tidak sesuai", HttpStatus.UNAUTHORIZED, new HashMap<>());
        }
        return generateValidLoginResponse(username, role);
    }
   
    private ResponseEntity<Object> generateValidLoginResponse(String username, String role) {
            Map<String, Object> objectMap = new HashMap<>();
            Object userData = role.equals(UserType.ADMIN.getUserType()) ? adminService.findByUsername(username) : customerService.findByUsername(username);;
            objectMap.put("user", userData);
            objectMap.put("token", generateJwtToken(username, role));
    
            HttpStatus status = HttpStatus.ACCEPTED;
            String message = role.equals(UserType.ADMIN.getUserType()) ? "Login sebagai Admin berhasil" : "Login sebagai Customer berhasil";
            return ResponseHandler.generateResponse(message, status, objectMap);
        
    }

    private String generateJwtToken(String username, String role) {
        ObjectMapper objectMapper = new ObjectMapper();
        return JwtProvider.getInstance().createJwtToken(
                objectMapper.createObjectNode()
                        .put("username", username)
                        .put("role", role)
                        .toString());
    }

    @PostMapping("/register/customer")
    public ResponseEntity<?> registerCustomer(@RequestBody RegistrationRequest<Customer> request) {
        return register(request.getUser(), request.getPasswordConfirmation());
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegistrationRequest<Admin> request) {
        System.out.println(request.getUser());
        return register(request.getUser(), request.getPasswordConfirmation());
    }

    public <T extends AbstractUser> ResponseEntity<?> register(T user, String passwordConfirmation) {
        Map<String, Object> response = new HashMap<>();
        if (!validateUser(user, passwordConfirmation, response)) {
            return ResponseHandler.generateResponse((String) response.get("message"), HttpStatus.UNAUTHORIZED, response);
        }
        saveUser(user, user.getRole());
        response.put("message", "Berhasil mendaftarkan " + user.getRole());
        return ResponseHandler.generateResponse((String) response.get("message"), HttpStatus.ACCEPTED, response);
    }
    
    private <T extends AbstractUser> boolean validateUser(T user, String passwordConfirmation,  Map<String, Object> response) {
        user.setPassword(user.getPassword());
        if (!user.isValid()) {
            response.put("message", "Field " + user.getRole().toLowerCase() + " tidak valid");
            return false;
        }
        if (!AuthProvider.getInstance().matches(passwordConfirmation, user.getPassword())) {
            response.put("message", "Konfirmasi password tidak sesuai");
            return false;
        }
        return true;
    }
    
    private void saveUser(AbstractUser user, String userType) {
        if (userType.equals(UserType.CUSTOMER.getUserType())) {
            customerService.addUser((Customer) user);
        } else {
            adminService.addUser((Admin) user);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        Map<String, Object> response = new HashMap<>();
        generateLogoutResponse(token, response);
        return ResponseHandler.generateResponse((String) response.get("message"),
                (HttpStatus) response.get("status"), response);
    }

    private void generateLogoutResponse(String token, Map<String, Object> response) {
        if (token == null) {
            response.put("message", "Token tidak ditemukan.");
            response.put("status", HttpStatus.BAD_REQUEST);
        } else {
            JwtProvider.getInstance().revokeJwtToken(token);
            response.put("message", "Berhasil logout");
            response.put("status", HttpStatus.ACCEPTED);
        }
    }              

}

