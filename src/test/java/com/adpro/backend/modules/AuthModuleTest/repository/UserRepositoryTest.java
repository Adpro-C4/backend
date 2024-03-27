package com.adpro.backend.modules.AuthModuleTest.repository;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import com.adpro.backend.modules.authmodule.model.AbstractUser;
import com.adpro.backend.modules.authmodule.model.Admin;
import com.adpro.backend.modules.authmodule.model.Customer;
import com.adpro.backend.modules.authmodule.repository.UserRepository;


public class UserRepositoryTest {

    private UserRepository<Customer> customerRepository;
    private UserRepository<Admin> adminRepository;
    private AbstractUser user1;
    private AbstractUser user2;
    private AbstractUser user3;
    private AbstractUser user4;
    

    @BeforeEach
    public void setUp(){
        customerRepository = new UserRepository<>();
        adminRepository = new UserRepository<>();
        user1 = new Customer("username1", "password1", "email1", "name1", "additionalInfo1");
        user2 = new Customer("username2", "password2", "email2", "name2", "additionalInfo2");
        user3 = new Admin("username3", "password3", "email3");
        user4 = new Admin("username4", "password4", "email4");
    }
    @Test
    public void testAddUniqueValue() {
        customerRepository.add((Customer) user1);
        assertEquals(user1, customerRepository.findByUsername("username1"));

        adminRepository.add((Admin) user3);
        assertEquals(user3, adminRepository.findByUsername("username3"));
    }

    @Test
    public void testAddNotUniqueValue() {
        customerRepository.add((Customer) user1);
        assertThrows(IllegalArgumentException.class, () -> {
            customerRepository.add((Customer) user1);
        });

        adminRepository.add((Admin) user3);
        assertThrows(IllegalArgumentException.class, () -> {
            adminRepository.add((Admin) user3);
        });
    }

    @Test
    public void testAddNullValue() {
        assertThrows(NullPointerException.class, () -> {
            customerRepository.add(null);
        });

        assertThrows(NullPointerException.class, () -> {
            adminRepository.add(null);
        });
    }

    @Test
    public void testFindUsernameExist() {
        customerRepository.add((Customer) user1);
        assertNotNull(customerRepository.findByUsername("username1"));

        adminRepository.add((Admin) user3);
        assertNotNull(adminRepository.findByUsername("username3"));


    }

    @Test
    public void testFindUsernameDoesNotExist() {
        assertNull(customerRepository.findByUsername("nonexistentusername"));
        assertNull(adminRepository.findByUsername("nonexistentusername"));
    }

    @Test
    public void testFindAllNotEmpty() {
        customerRepository.add((Customer) user1);
        customerRepository.add((Customer) user2);
        assertFalse(customerRepository.findAll().isEmpty());

        adminRepository.add((Admin) user3);
        adminRepository.add((Admin) user4);
        assertFalse(adminRepository.findAll().isEmpty());
    }

    @Test
    public void testFindAllEmpty() {
        assertTrue(customerRepository.findAll().isEmpty());
        assertTrue(adminRepository.findAll().isEmpty());
    }

    @Test
    public void testUpdateNullValue() {
        assertThrows(NullPointerException.class, () -> {
            customerRepository.update(null);
        });
        assertThrows(NullPointerException.class, () -> {
            adminRepository.update(null);
        });
    }

    @Test
    public void testUpdateNonExistentValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            customerRepository.update((Customer) user1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            adminRepository.update((Admin) user3);
        });
    }

    @Test
    public void testUpdateExistentValue() {
        customerRepository.add((Customer) user1);
        ((Customer) user1).setEmail("updatedemail@example.com");
        customerRepository.update((Customer) user1);
        assertEquals("updatedemail@example.com", customerRepository.findByUsername("username1").getEmail());

        adminRepository.add((Admin) user3);
        ((Admin) user3).setEmail("updatedemail@example.com");
        adminRepository.update((Admin) user3);
        assertEquals("updatedemail@example.com", adminRepository.findByUsername("username3").getEmail());
    }

    @Test
    public void testDeleteNonExistentValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            customerRepository.delete("nonexistentusername");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            adminRepository.delete("nonexistentusername");
        });
    }
    @Test
    public void testDeleteExistentValue() {
        customerRepository.add((Customer) user1);
        customerRepository.delete("username1");
        assertNull(customerRepository.findByUsername("username1"));

        adminRepository.add((Admin) user3);
        adminRepository.delete("username3");
        assertNull(adminRepository.findByUsername("username3"));
    }

}
