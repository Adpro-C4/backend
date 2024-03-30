package com.adpro.backend.modules.authmodule.model;

import com.adpro.backend.modules.authmodule.enums.UserType;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Customer extends AbstractUser {
    private String name;
    private String phoneNumber;

    public Customer(String username, String password, String email, String name, String phoneNumber){
        super(username, password, email, UserType.CUSTOMER.getUserType());
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public Customer() {
        setRole(UserType.CUSTOMER.getUserType());
    }

    @Override
    public boolean isValid() {
        return super.isValid() &&
                isNotNullOrEmpty(name) &&
                isNotNullOrEmpty(phoneNumber) &&
                isAllDigits(phoneNumber);
    }

}
