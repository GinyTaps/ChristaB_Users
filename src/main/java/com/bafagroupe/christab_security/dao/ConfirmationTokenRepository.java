package com.bafagroupe.christab_security.dao;

import com.bafagroupe.christab_security.entities.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, String> {

    ConfirmationToken findByConfirmationToken(String confirmationToken);

    @Query("SELECT IFNULL(MAX(tokenid), 0)+1 AS NEXTID FROM ConfirmationToken ")
    public long findMaxId();
}
