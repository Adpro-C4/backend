package com.adpro.backend.modules.authmodule.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.regex.Pattern;

@Getter
@Setter
public abstract class AbstractUser {
    private String username;
    private String password;
    private String email;
    @Setter(AccessLevel.PRIVATE)
    private String role;

    public AbstractUser(String username, String password, String email, String role){
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public boolean isValid(){
        return isNotNullOrEmpty(getUsername()) &&
                isNotNullOrEmpty(getPassword()) &&
                isNotNullOrEmpty(getEmail()) && isValidEmail(getEmail());
    };

    public boolean authenticate(String password){
        return this.password.equals(password);
    }

    protected boolean isNotNullOrEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    protected boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);

        return pattern.matcher(email).matches();
    }

    protected boolean isAllDigits(String value) {
        return value.matches("\\d+");
    }
}
