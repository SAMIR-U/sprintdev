package edu.uptc.swi.sprintdev.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entidad JPA de Sprint. Es una estructura de datos simple: solo
 * atributos, relaciones y accesores. Las reglas de negocio (activar,
 * cerrar, agregar lectores, etc.) NO viven aquí; están en la capa de
 * servicio.
 */
@Entity
@Table(name = "sprints")
public class Sprint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sprintId;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 500)
    private String goal;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SprintStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sprint_readers",
            joinColumns = @JoinColumn(name = "sprint_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> readers = new HashSet<>();

    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

    @Version
    @Column(nullable = false)
    private int version;
    public int getSprintId() {
        return sprintId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public SprintStatus getStatus() {
        return status;
    }

    public void setStatus(SprintStatus status) {
        this.status = status;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Set<User> getReaders() {
        return readers;
    }


    public List<Task> getTasks() {
        return tasks;
    }

    public int getVersion() {
        return version;
    }

}
