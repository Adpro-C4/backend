package com.adpro.backend.modules.AuthModuleTest.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.adpro.backend.modules.authmodule.provider.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.jsonwebtoken.Claims;
import com.adpro.backend.modules.authmodule.model.AbstractUser;
import com.adpro.backend.modules.authmodule.model.Admin;
import com.adpro.backend.modules.authmodule.model.Customer;
import com.adpro.backend.modules.authmodule.repository.UserRepository;
import com.adpro.backend.modules.authmodule.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl<Admin> adminService;
    @InjectMocks
    private UserServiceImpl<Customer> customerService;
    @Mock
    private UserRepository<Admin> adminRepository;
    @Mock
    private UserRepository<Customer> customerRepository;
    AbstractUser admin1;
    AbstractUser customer1;

    @BeforeEach
    void setUp(){
        admin1 = new Admin("seorang_admin", "adminPass", "admin1@gmail.com");
        customer1 = new Customer("seorang_customer", "customerPass", "customer1@gmail.com", "nama customer", "0823561528");
    }
    @Test
    public void testAuthenticateUserValid(){
        
        when(adminRepository.findByUsername(admin1.getUsername())).thenReturn((Admin) admin1);
        assertTrue(adminService.authenticateUser("seorang_admin", "adminPass"));

        
        when(customerRepository.findByUsername(customer1.getUsername())).thenReturn((Customer)customer1);
        assertTrue(customerService.authenticateUser("seorang_customer", "customerPass"));
    };
    @Test
    public void testAuthenticateUserInvalid(){

        when(adminRepository.findByUsername(admin1.getUsername())).thenReturn((Admin) admin1);
        assertFalse(adminService.authenticateUser("seorang_admin", "hehe"));

        
        when(customerRepository.findByUsername(customer1.getUsername())).thenReturn((Customer)customer1);
        assertFalse(customerService.authenticateUser("seorang_customer", "hehe"));
    };

    @Test
    public void testAuthenticateUserNotFound(){
        when(adminRepository.findByUsername("seorang_admin")).thenReturn(null);
        when(customerRepository.findByUsername("seorang_customer")).thenReturn(null);

        assertThrows(NullPointerException.class, () -> adminService.authenticateUser("seorang_admin", "adminPass"));
        assertThrows(NullPointerException.class, () -> customerService.authenticateUser("seorang_customer", "customerPass"));
    }

    @Test
    public void testCreateJwtTokenUsernameExist() {

        when(adminRepository.findByUsername(admin1.getUsername())).thenReturn((Admin) admin1);
        String token = adminService.
                createJwtToken(adminRepository.findByUsername(admin1.getUsername()).getUsername());

        verifyToken(token, admin1.getUsername());

        when(customerRepository.findByUsername(customer1.getUsername())).thenReturn((Customer) customer1);
        token = customerService.
                createJwtToken(customerRepository.
                        findByUsername(customer1.getUsername()).getUsername());
        verifyToken(token, customer1.getUsername());

    }

    @Test
    public void testCreateJwtTokenUsernameNotExist(){
        when(customerRepository.findByUsername(customer1.getUsername())).thenReturn(null);
        when(adminRepository.findByUsername(admin1.getUsername())).thenReturn(null);
        assertThrows(NullPointerException.class, () -> adminService.createJwtToken
                (adminService.findByUsername(admin1.getUsername()).getUsername()));
        assertThrows(NullPointerException.class, () -> customerService.
                createJwtToken(customerService.findByUsername(
                customer1.getUsername()).getUsername()));
    };

    private void verifyToken(String token, String expectedUsername) {
        assertNotNull(token);
        Claims claims = JwtProvider.getInstance().parseJwtToken(token);
        assertNotNull(claims);
        String username = claims.getSubject();
        assertNotNull(username);
        assertEquals(expectedUsername, username);
    }
    @Test
    public void testLogoutUserExist(){
        when(adminRepository.add((Admin) admin1)).thenReturn((Admin) admin1);
        adminService.addUser((Admin) admin1);
        when(adminRepository.findByUsername(admin1.getUsername())).thenReturn((Admin) admin1);
        String token = adminService.createJwtToken(admin1.getUsername());
        adminService.logout(admin1);
        assertNull(adminService.getDataFromJwt(token));
    };
    @Test
    public void testLogoutUserNotExist(){
        when(customerRepository.findByUsername(customer1.getUsername())).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> customerService.logout(customer1));
    };
    @Test
    public void testJwtTokenValid() {
        String token = adminService.createJwtToken(admin1.getUsername());
        assertTrue(adminService.isJwtTokenValid(token));
    }
    @Test
    public void testJwtTokenInvalid(){
        String emptyToken = "";
        String token = adminService.createJwtToken(admin1.getUsername());
        token = token.toUpperCase();
        assertFalse(adminService.isJwtTokenValid(emptyToken));
        assertFalse(adminService.isJwtTokenValid(token));
    };

    @Test
    public void testAddUserUnique(){
        when(customerRepository.add((Customer) customer1)).thenReturn((Customer) customer1);
        customerService.addUser((Customer) customer1);
        when(customerRepository.findByUsername(customer1.getUsername())).thenReturn((Customer) customer1);
        Customer getCustomer = customerService.findByUsername(customer1.getUsername());
        assertEquals(customer1.getUsername(), getCustomer.getUsername());
        assertEquals(customer1.getPassword(), getCustomer.getPassword());
    };
    @Test
    public void testAddUserNotUnique() {
        when(customerRepository.add((Customer) customer1)).thenReturn((Customer) customer1);
        customerService.addUser((Customer) customer1);
        when(customerRepository.findByUsername(customer1.getUsername())).thenReturn((Customer) customer1);
        assertThrows(IllegalArgumentException.class, () -> customerService.addUser((Customer) customer1));
    }
    @Test
    public void testAddUserNull() {

        assertThrows(IllegalArgumentException.class, () -> customerService.addUser(null));
    }

    @Test
    public void testAddUserNotValid(){
        Customer customer = new Customer("customer",
                "abcde", "hehe", "a", "0X");
        Customer customer2 = new Customer("",
                "", "", "", "");
        assertThrows(IllegalArgumentException.class, () -> customerService.addUser(customer));
        assertThrows(IllegalArgumentException.class, () -> customerService.addUser(customer2));
    }

    @Test
    public void testRemoveUserExist(){
        when(customerRepository.findByUsername(customer1.getUsername())).thenReturn(null);
        customerService.addUser((Customer) customer1);
        when(customerRepository.findByUsername(customer1.getUsername())).thenReturn((Customer) customer1);
        customerService.removeUser((Customer) customer1);
        when(customerRepository.findByUsername(customer1.getUsername())).thenReturn(null);
        assertNull(customerService.findByUsername(customer1.getUsername()));
    };

    @Test
    public void testRemoveUserNotExist(){
        assertThrows(NullPointerException.class, () ->
                customerService.removeUser((Customer) customer1));
    };

    @Test
    public void testFindByUsernameExist(){
        when(customerRepository.findByUsername(customer1.getUsername())).thenReturn((Customer) customer1);
        assertNotNull(customerService.findByUsername(customer1.getUsername()));
    };

    @Test
    public void testFindByUsernameNotExist(){
        when(customerRepository.findByUsername(customer1.getUsername())).thenReturn(null);
        assertNull(customerService.findByUsername(customer1.getUsername()));
    };

    @Test
    public void testgetAllUserNotEmpty(){
        when(customerRepository.add((Customer) customer1)).thenReturn((Customer) customer1);
        when(customerRepository.findAll()).thenReturn(List.of((Customer)customer1));
        customerService.addUser((Customer) customer1);
        assertEquals(1, customerService.getAllUsers().size());
    };

    @Test
    public void testgetAllUserEmpty(){
        assertEquals(0, customerService.getAllUsers().size());
    };

    @Test
    public void testUpdateUserExist(){
        when(customerRepository.findByUsername(customer1.getUsername())).thenReturn((Customer) customer1);

        when(customerRepository.update((Customer) customer1)).thenReturn((Customer) customer1);
        String newEmail = "emailbaru@gmail.com";
        customer1.setEmail(newEmail);
        customerService.updateUser((Customer) customer1);

        Customer updatedCustomer = customerService.findByUsername(customer1.getUsername());
        assertEquals(newEmail, updatedCustomer.getEmail());
    };
    @Test
    public void testUpdateUserNotExist() {
        when(customerRepository.findByUsername(customer1.getUsername())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> customerService.updateUser((Customer) customer1));
    }

    @Test
    public void testUpdateUserNull() {

        assertThrows(NullPointerException.class, () -> customerService.updateUser(null));
    }

}
