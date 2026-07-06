package edu.uptc.swi.sprintdev.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Persona registrada en SprintDev. Un mismo { User} puede ser
 * Creador de unos Sprints y Lector de otros: el rol no es un atributo
 * propio del usuario, sino de su relación con cada {Sprint}.
 */
//xd
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = "uk_user_email", columnNames = "email"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 120) //esta mal es username no nombrecompleto
    private String fullName;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "registration_date", nullable = false, updatable = false)
    private LocalDateTime registrationDate;

    /** Constructor exigido por JPA; no debe usarse desde el código de la aplicación. */
    protected User() {
    }

    public User(String fullName, String email, String passwordHash) {
        this.fullName = requireText(fullName, "Full name is required");
        this.email = requireText(email, "Email is required").toLowerCase();
        this.passwordHash = requireText(passwordHash, "Password is required");
        this.registrationDate = LocalDateTime.now();
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    /**
     * Reemplaza el hash de contraseña vigente. La generación del hash es
     * responsabilidad de la capa de aplicación (ver {PasswordEncoder}),
     * no de esta entidad.
     */
    public void updatePassword(String newHash) {
        this.passwordHash = requireText(newHash, "Password is required");
    }

    public boolean hasEmail(String otherEmail) {
        return otherEmail != null && this.email.equalsIgnoreCase(otherEmail.trim());
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    /**
     * Igualdad basada en el correo: es la clave natural del usuario y es
     * estable antes y después de persistir, a diferencia del id generado.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof User other)) {
            return false;
        }
        return email.equals(other.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "User{id=%d, email='%s'}".formatted(id, email);
    }
}