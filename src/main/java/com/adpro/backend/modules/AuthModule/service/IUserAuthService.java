package com.adpro.backend.modules.authmodule.service;

import com.adpro.backend.modules.authmodule.model.AbstractUser;

public interface IUserAuthService {
    boolean authenticateUser(String username, String password);
    String createJwtToken(String key);
    void logout(AbstractUser user);
    boolean isJwtTokenValid(String jwt);
    String getDataFromJwt(String jwt);
}
