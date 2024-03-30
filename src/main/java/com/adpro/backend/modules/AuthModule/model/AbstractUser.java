package com.adpro.backend.modules.authmodule.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.regex.Pattern;

import com.adpro.backend.modules.authmodule.provider.AuthProvider;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;

    public AbstractUser(String username, String password, String email, String role){
        this.username = username;
        setPassword(password);
        this.email = email;
        this.role = role;
    }

    public AbstractUser() {

    }

    public void setPassword(String password){
        this.password = AuthProvider.getInstance().encode(password);
    }

    public boolean isValid(){
        return isNotNullOrEmpty(getUsername()) &&
                isNotNullOrEmpty(getPassword()) &&
                isNotNullOrEmpty(getEmail()) && isValidEmail(getEmail());
    };

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
