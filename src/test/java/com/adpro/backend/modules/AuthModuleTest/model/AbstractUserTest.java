package com.adpro.backend.modules.AuthModuleTest.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;

import com.adpro.backend.modules.authmodule.enums.UserType;
import com.adpro.backend.modules.authmodule.model.AbstractUser;
import com.adpro.backend.modules.authmodule.model.Admin;
import com.adpro.backend.modules.authmodule.model.Customer;

public class AbstractUserTest {
    AbstractUser user;
    @Test
    public void getUserDataIfCustomer(){
        user = new Customer("mycususername", "mycuspassword", "mycusemail@gmail.com", "myname", "myphonenumber");
        assertNotNull(user);
        assertEquals("mycususername", user.getUsername());
        assertEquals("mycuspassword", user.getPassword());
        assertEquals("mycusemail@gmail.com", user.getEmail());
        assertEquals(UserType.CUSTOMER.getUserType(), user.getRole());
    }
    @Test
    public void getUserDataIfAdmin(){
        user = new Admin("myadusername", "myadpassword", "myademail@gmail.com");
        assertNotNull(user);
        assertEquals("myadusername", user.getUsername());
        assertEquals("myadpassword", user.getPassword());
        assertEquals("myademail@gmail.com", user.getEmail());
        assertEquals(UserType.ADMIN.getUserType(), user.getRole());
    }

    @Test
    public void setUserDataIfCustomer(){
        user = new Customer("cusmyusername0", "cusmypassword0", "cusmyemail0@gmail.com", "myname0", "myphonenumber0");
        user.setUsername("cusmyusername1");
        user.setPassword("cusmypassword1");
        user.setEmail("cusmyemail1@gmail.com");
        assertEquals("cusmyusername1", user.getUsername());
        assertEquals("cusmypassword1", user.getPassword());
        assertEquals("cusmyemail1@gmail.com", user.getEmail());
    }

    @Test
    public void setUserDataIfAdmin(){
        user = new Admin("admyusername0", "admypassword0", "admyemail0@gmail.com");
        user.setUsername("admyusername1");
        user.setPassword("admypassword1");
        user.setEmail("admyemail1@gmail.com");
        assertEquals("admyusername1", user.getUsername());
        assertEquals("admypassword1", user.getPassword());
        assertEquals("admyemail1@gmail.com", user.getEmail());
    }

}
