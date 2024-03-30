package com.adpro.backend.modules.AuthModuleTest.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.adpro.backend.modules.authmodule.enums.UserType;
import com.adpro.backend.modules.authmodule.model.Customer;

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
        assertEquals("myemail@gmail.com", customer.getEmail());
        assertEquals("myname", customer.getName());
        assertEquals("myphonenumber", customer.getPhoneNumber());
        assertEquals(UserType.CUSTOMER.getUserType(), customer.getRole());
    }

}
