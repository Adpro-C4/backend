package com.adpro.backend.modules.authmodule.service;

import com.adpro.backend.modules.authmodule.model.AbstractUser;

public abstract class UserService<T extends AbstractUser> implements IUserCRUDService<T>, IUserAuthService {
}

