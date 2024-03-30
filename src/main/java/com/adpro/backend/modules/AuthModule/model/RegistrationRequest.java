package com.adpro.backend.modules.authmodule.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest <T extends AbstractUser> {
    private T user;
    private String passwordConfirmation;
}
