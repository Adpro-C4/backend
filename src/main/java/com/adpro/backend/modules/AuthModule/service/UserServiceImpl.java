package com.adpro.backend.modules.authmodule.service;

import java.util.List;

import com.adpro.backend.modules.authmodule.model.AbstractUser;
import com.adpro.backend.modules.authmodule.provider.AuthProvider;
import com.adpro.backend.modules.authmodule.provider.JwtProvider;
import com.adpro.backend.modules.authmodule.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl<T extends AbstractUser> extends UserService<T>{
    @Autowired
    UserRepository<T> userRepository;

    @PersistenceContext
    private EntityManager entityManager;

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
        verifyUserNotNull(user);
        verifyUserExists(user.getUsername());
        return  userRepository.save(user);
    }

    private void verifyUserNotNull(AbstractUser user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
    }

    private void verifyUserExists(String username) {
        if (userRepository.findByUsername(username) == null) {
            throw new IllegalArgumentException();
        }
    }
    private void verifyUserNotExists(String username) {
        if (userRepository.findByUsername(username) != null) {
            throw new IllegalArgumentException();
        }
    }

    private  void verifyUserIsValid(AbstractUser user) {
        if (!user.isValid()) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public T addUser(T user) {
        verifyUserNotNull(user);
        verifyUserIsValid(user);
        verifyUserNotExists(user.getUsername());
        return  userRepository.save(user);
    }

    @Override
    public void removeUser(T user) {
        verifyUserNotNull(user);
        verifyUserExists(user.getUsername());
        userRepository.deleteByUsername(user.getUsername());
    }

    @Override
    public boolean authenticateUser(String username, String password) {
        T user = userRepository.findByUsername(username);
        verifyUserNotNull(user);
        verifyUserExists(user.getUsername());
        return AuthProvider.getInstance().matches(password, user.getPassword());
    }

    @Override
    public  T authenticateAndGetUser(String username, String password, Class<T> entityClass) {
        String query = "SELECT u FROM " + entityClass.getSimpleName() + " u WHERE u.username = :username";
        T user = entityManager.createQuery(query, entityClass)
                .setParameter("username", username)
                .getSingleResult();

        if (user == null) {
            return null;
        }

        if (AuthProvider.getInstance().matches(password, user.getPassword())) {
            return user;
        }

        return null;
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
        verifyUserNotNull(user);
        verifyUserExists(user.getUsername());
        JwtProvider.getInstance().revokeJwtToken(JwtProvider.getInstance().getToken(user.getUsername()));
    }
    
}
