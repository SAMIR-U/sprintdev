package edu.uptc.swi.sprintdev.net;

import java.util.List;

/**
 * Data transfer object used to submit task creation and update data from the web form.
 */
public class TaskForm {
    /** Sprint id to which the task belongs. */
    private int sprintid;
    /** Task title entered by the user. */
    private String title;
    /** Task description entered by the user. */
    private String description;
    /** Usernames of users assigned to the task. */
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
