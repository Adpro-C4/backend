package com.adpro.backend.modules.authmodule.service;

import java.util.List;

import com.adpro.backend.modules.authmodule.model.AbstractUser;

public interface IUserCRUDService <T extends AbstractUser>{
    T findByUsername(String username);
    List<T> getAllUsers();
    T updateUser(T user);
    T addUser(T user);
    void removeUser(T user);
    
}
