package edu.uptc.swi.sprintdev.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * JPA entity representing an application user.
 * It contains only basic user attributes and persistence metadata.
 * Business rules like duplicate detection or lockout are handled elsewhere.
 */
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_name", columnNames = "user_name")
})
public class User {

    /**
     * Primary key for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    /**
     * Username used for authentication and identification.
     */
    @Column(name = "user_name", nullable = false, length = 120)
    private String userName;

    /**
     * Encrypted password for authentication.
     */
    @Column(name = "password", nullable = false)
    private String password;


    public int getId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
