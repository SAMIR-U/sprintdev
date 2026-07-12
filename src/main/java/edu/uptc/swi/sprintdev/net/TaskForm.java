package edu.uptc.swi.sprintdev.net;

import java.util.List;

public class TaskForm {
    private int sprintid;
    private String title;
    private String description;
    private List<String> assignedUserNames;
    
    public TaskForm(int sprintid, String title, String description, List<String> assignedUserNames) {
        this.sprintid = sprintid;
        this.title = title;
        this.description = description;
        this.assignedUserNames = assignedUserNames;
    }
    public TaskForm() {
    }
    public int getSprintid() {
        return sprintid;
    }
    public void setSprintid(int sprintid) {
        this.sprintid = sprintid;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String tittle) {
        this.title = tittle;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public List<String> getAssignedUserNames() {
        return assignedUserNames;
    }
    public void setAssignedUserNames(List<String> assignedUsers) {
        this.assignedUserNames = assignedUsers;
    }
    
    
}
