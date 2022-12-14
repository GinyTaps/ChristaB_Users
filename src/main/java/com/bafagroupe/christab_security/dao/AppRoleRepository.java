package com.bafagroupe.christab_security.dao;

import com.bafagroupe.christab_security.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
    public AppRole findByRolename(String rolename);

    @Query("SELECT IFNULL(MAX(idAppRole), 0)+1 AS NEXTID FROM AppRole")
    public long findMaxId();

    @Query("SELECT R FROM AppRole R WHERE R.idAppRole LIKE :y ")
    public AppRole findOneById(@Param("y") long id);

    @Query("DELETE FROM AppRole R WHERE R.idAppRole LIKE :y ")
    public Void deleteOneById(@Param("y") long id);
}
