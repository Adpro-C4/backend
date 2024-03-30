package com.adpro.backend.modules.authmodule.model;

import com.adpro.backend.modules.authmodule.enums.UserType;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Admin extends AbstractUser {
    public Admin(String username, String password, String email){
        super(username, password, email, UserType.ADMIN.getUserType());
    }

    public Admin() {
        super();
        setRole(UserType.ADMIN.getUserType());
    }
}
