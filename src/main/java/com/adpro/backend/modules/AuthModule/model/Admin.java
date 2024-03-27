package com.adpro.backend.modules.authmodule.model;

import com.adpro.backend.modules.authmodule.enums.UserType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Admin extends AbstractUser {
    public Admin(String username, String password, String email){
        super(username, password, email, UserType.ADMIN.getUserType());
    }
}
