package com.vihanga.tech.reglogin.repositories;

import com.vihanga.tech.reglogin.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepositories extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser a SET a.enabled = TRUE WHERE a.email = ?1")
    int enableAppUser(String email);


    @Query(value = "SELECT u.enabled FROM AppUser u WHERE u.email = ?1",
            nativeQuery = true)
    boolean enableCheck(String email);
}
