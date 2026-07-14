package edu.uptc.swi.sprintdev.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * JPA entity representing a sprint in the application.
 * This class is a simple data carrier for sprint attributes and relationships.
 */
@Entity
@Table(name = "sprints")
public class Sprint {

    /**
     * Primary key for the sprint entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sprintId;

    /**
     * Human-readable sprint title.
     */
    @Column(nullable = false, length = 120)
    private String name;

    /**
     * Description of the sprint goals.
     */
    @Column(nullable = false, length = 500)
    private String goal;

    /**
     * Sprint start date.
     */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /**
     * Sprint end date.
     */
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    /**
     * Current status of the sprint.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SprintStatus status;

    /**
     * The user who created the sprint.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    /**
     * Users who have read access to the sprint.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sprint_readers",
            joinColumns = @JoinColumn(name = "sprint_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> readers = new HashSet<>();

    /**
     * Tasks that belong to this sprint.
     */
    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

    /**
     * Optimistic locking version used by JPA.
     */
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

    public void setVersion(int version) {
        this.version = version;
    }

}
