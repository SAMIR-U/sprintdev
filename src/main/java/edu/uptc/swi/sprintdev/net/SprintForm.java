package edu.uptc.swi.sprintdev.net;

import java.time.LocalDate;

public class SprintForm {
    private String name;
    private String goal;
    private LocalDate startDate;
    private LocalDate endDate;
    public SprintForm() {
    }
    public SprintForm(String name, String goal, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.goal = goal;
        this.startDate = startDate;
        this.endDate = endDate;
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
    
}
