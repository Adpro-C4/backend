package com.adpro.backend.modules.authmodule.service;

import java.util.List;

import com.adpro.backend.modules.authmodule.model.AbstractUser;
import com.adpro.backend.modules.authmodule.provider.JwtProvider;
import com.adpro.backend.modules.authmodule.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl<T extends AbstractUser> extends UserService<T>{
    @Autowired
    UserRepository<T> userRepository;

    @Override
    public T findByUsername(String username) {
        return  userRepository.findByUsername(username);
    }

    @Override
    public List<T> getAllUsers() {
        return  userRepository.findAll();
    }

    @Override
    public T updateUser(T user) {
        if(user == null){
            throw new NullPointerException();
        }
        if(userRepository.findByUsername(user.getUsername()) == null){
            throw  new IllegalArgumentException();
        }
        return  userRepository.update(user);
    }

    @Override
    public T addUser(T user) {
        if(user == null){
            throw new IllegalArgumentException();
        }

        if(!user.isValid()){
            throw new IllegalArgumentException();
        }

        if(userRepository.findByUsername(user.getUsername())!= null){
            throw  new IllegalArgumentException();
        }
        return  userRepository.add(user);
    }

    @Override
    public void removeUser(T user) {
        if(userRepository.findByUsername(user.getUsername())== null){
            throw  new NullPointerException();
        }
        userRepository.delete(user.getUsername());
    }

    @Override
    public boolean authenticateUser(String username, String password) {
        T user = userRepository.findByUsername(username);
        if(user == null){
            throw  new NullPointerException();
        }
        return userRepository.findByUsername(username).authenticate(password);
    }

    @Override
    public String createJwtToken(String key) {
        return JwtProvider.getInstance().createJwtToken(key);
    }

   
    @Override
    public boolean isJwtTokenValid(String jwt) {
        return  JwtProvider.getInstance().isJwtTokenValid(jwt);
    }

    @Override
    public String getDataFromJwt(String jwt) {
        return  JwtProvider.getInstance().getDataFromJwt(jwt);
    }

    @Override
    public void logout(AbstractUser user) {
        T currUser = userRepository.findByUsername(user.getUsername());
        if(currUser == null){
            throw new IllegalArgumentException();
        }
        JwtProvider.getInstance().revokeJwtToken(JwtProvider.getInstance().getToken(user.getUsername()));
    }
    
}
