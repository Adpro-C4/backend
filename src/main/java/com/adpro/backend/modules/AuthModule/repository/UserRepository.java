package com.adpro.backend.modules.authmodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.adpro.backend.modules.authmodule.model.AbstractUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository <T extends AbstractUser> extends JpaRepository<T, Long>{

    @Query("SELECT u FROM #{#entityName} u WHERE u.username = :username")
    T findByUsername(@Param("username") String username);
    @Override
    List<T> findAll();
    @Query("DELETE FROM #{#entityName} u WHERE u.username = :username")
    void deleteByUsername(@Param("username") String username);

    @Query("SELECT u FROM #{#entityName} u WHERE u.username = :username")
    T findUseUsernameOptimize(@Param("username") String username);
}
