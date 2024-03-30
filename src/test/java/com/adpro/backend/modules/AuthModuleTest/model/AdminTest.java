package com.adpro.backend.modules.AuthModuleTest.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.adpro.backend.modules.authmodule.enums.UserType;
import com.adpro.backend.modules.authmodule.model.Admin;

public class AdminTest {
    private Admin admin;
    @BeforeEach
    public void setUp() {
        admin = new Admin("myusername", "mypassword", "myemail@gmail.com");
    }
    @Test 
    public void getAdminType(){
        assertEquals("ADMIN", UserType.ADMIN.getUserType());
    }

    @Test
    public void isExistAdminType(){
        assertTrue(UserType.contains(UserType.ADMIN.getUserType()));;
    }
    @Test
    public void testCreateAdmin() {
        assertNotNull(admin);
        assertEquals("myusername", admin.getUsername());
        assertEquals("myemail@gmail.com", admin.getEmail());
        assertEquals(UserType.ADMIN.getUserType(), admin.getRole());
    }
}
