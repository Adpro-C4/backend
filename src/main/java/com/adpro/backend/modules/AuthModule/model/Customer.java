package com.adpro.backend.modules.authmodule.model;

import com.adpro.backend.modules.authmodule.enums.UserType;

import lombok.Getter;
import lombok.Setter;

import java.util.regex.Pattern;

@Getter
@Setter
public class Customer extends AbstractUser {
    private String name;
    private String phoneNumber;

    public Customer(String username, String password, String email, String name, String phoneNumber){
        super(username, password, email, UserType.CUSTOMER.getUserType());
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean isValid() {
        return super.isValid() &&
                isNotNullOrEmpty(name) &&
                isNotNullOrEmpty(phoneNumber) &&
                isAllDigits(phoneNumber);
    }

}
