package com.app.auth.model;

/**
 * Role Enum - User roles/authorities
 *
 * Defines the different permission levels in the system:
 * - ROLE_USER: Regular user with limited permissions
 * - ROLE_ADMIN: Administrator with full permissions
 */
public enum Role {
    /** Regular user role - limited permissions */
    ROLE_USER,

    /** Administrator role - full permissions */
    ROLE_ADMIN
}

