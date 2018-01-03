package com.baeldung.lss.persistence;

import com.baeldung.lss.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by tracxn-lp-175 on 2/1/18.
 */
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}