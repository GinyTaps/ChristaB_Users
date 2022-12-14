package com.bafagroupe.christab_security.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Table(name = "app_role", schema = "christaB_db_Users", catalog = "")
public class AppRole {
    private Long idAppRole;
    private String rolename;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_app_role")
    public Long getIdAppRole() {
        return idAppRole;
    }

    public void setIdAppRole(long idAppRole) {
        this.idAppRole = idAppRole;
    }

    public void setIdAppRole(Long idAppRole) {
        this.idAppRole = idAppRole;
    }

    @Basic
    @Column(name = "rolename")
    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppRole appRole = (AppRole) o;
        return Objects.equals(idAppRole, appRole.idAppRole) &&
                Objects.equals(rolename, appRole.rolename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAppRole, rolename);
    }
}
