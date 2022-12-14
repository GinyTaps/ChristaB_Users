package com.bafagroupe.christab_security.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "app_user", schema = "christaB_db_Users") // , catalog = "")
@AllArgsConstructor @NoArgsConstructor
public class AppUser {
    private Long idAppUser;
    private String email;
    private String password;
    private int passwordForget;
    private Timestamp limitPasswordTime;
    private boolean validated;
    private boolean activated;
    Collection<AppRole> roles = new ArrayList<>();


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_app_user")
    public Long getIdAppUser() {
        return idAppUser;
    }

    public void setIdAppUser(long idAppUser) {
        this.idAppUser = idAppUser;
    }

    public void setIdAppUser(Long idAppUser) {
        this.idAppUser = idAppUser;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    // @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //permet de cacher ce attribut à l'affichage
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "password_forget")
    public int getPasswordForget() {
        return passwordForget;
    }

    public void setPasswordForget(Integer passwordForget) {
        this.passwordForget = passwordForget;
    }

    public void setPasswordForget(int passwordForget) {
        this.passwordForget = passwordForget;
    }

    @Basic
    @Column(name = "limit_password_time")
    public Timestamp getLimitPasswordTime() {
        return limitPasswordTime;
    }

    public void setLimitPasswordTime(Timestamp limitPasswordTime) {
        this.limitPasswordTime = limitPasswordTime;
    }

    @Basic
    @Column(name = "activated")
    public boolean getActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @Column(name = "validated")
    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }


    @ManyToMany(fetch = FetchType.EAGER) // Eager permet de charger automatiquement les rôles de l'utilisateur
    public Collection<AppRole> getRoles() {
        return roles;
    }

    public void setRoles(Collection<AppRole> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return passwordForget == appUser.passwordForget &&
                validated == appUser.validated &&
                activated == appUser.activated &&
                Objects.equals(idAppUser, appUser.idAppUser) &&
                Objects.equals(email, appUser.email) &&
                Objects.equals(password, appUser.password) &&
                Objects.equals(limitPasswordTime, appUser.limitPasswordTime) &&
                Objects.equals(roles, appUser.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAppUser, email, password, passwordForget, limitPasswordTime, validated, activated, roles);
    }
}
