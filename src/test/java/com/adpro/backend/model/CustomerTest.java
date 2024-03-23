package com.adpro.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.adpro.backend.type.UserType;

public class CustomerTest {
    private Customer customer;

    @BeforeEach
    public void setUp() {
        customer = new Customer("myusername", "mypassword", "myemail@gmail.com", "myname", "myphonenumber");
    }

    @Test 
    public void getCustomerType(){
        assertEquals("CUSTOMER", UserType.CUSTOMER.getUserType());
    }

    @Test
    public void isExistCustomerType(){
        assertTrue(UserType.contains(UserType.CUSTOMER.getUserType()));;
    }

    @Test
    public void testCreateCustomer() {
        assertNotNull(customer);
        assertEquals("myusername", customer.getUsername());
        assertEquals("mypassword", customer.getPassword());
        assertEquals("myemail@gmail.com", customer.getEmail());
        assertEquals("myname", customer.getName());
        assertEquals("myphonenumber", customer.getPhoneNumber());
        assertEquals(UserType.CUSTOMER.getUserType(), customer.getRole());
    }

    @Test
    public void testAuthenticateSuccess() {
        assertTrue(customer.authenticate("mypassword"));
    }

    @Test
    public void testAuthenticateFailure() {
        assertFalse(customer.authenticate("wrongpassword"));
    }

}
