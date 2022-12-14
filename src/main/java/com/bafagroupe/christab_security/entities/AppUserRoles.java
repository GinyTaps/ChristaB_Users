package com.bafagroupe.christab_security.entities;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "app_user_roles", schema = "christaB_db_Users", catalog = "")
public class AppUserRoles implements Serializable {
    private AppUser appUserByAppUserIdAppUser;
    private AppRole appRoleByRolesIdAppRole;

    @Id
    @ManyToOne
    @JoinColumn(name = "app_user_id_app_user", referencedColumnName = "id_app_user", nullable = false)
    public AppUser getAppUserByAppUserIdAppUser() {
        return appUserByAppUserIdAppUser;
    }

    public void setAppUserByAppUserIdAppUser(AppUser appUserByAppUserIdAppUser) {
        this.appUserByAppUserIdAppUser = appUserByAppUserIdAppUser;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "roles_id_app_role", referencedColumnName = "id_app_role", nullable = false)
    public AppRole getAppRoleByRolesIdAppRole() {
        return appRoleByRolesIdAppRole;
    }

    public void setAppRoleByRolesIdAppRole(AppRole appRoleByRolesIdAppRole) {
        this.appRoleByRolesIdAppRole = appRoleByRolesIdAppRole;
    }
}
