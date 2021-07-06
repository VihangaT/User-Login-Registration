package com.vihanga.tech.reglogin.repositories;

import com.vihanga.tech.reglogin.appuser.AppUser;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;


@Repository
@Transactional
public interface UserRepositories {

    Optional<AppUser> findByEmail(String email);

}
