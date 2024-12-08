package com.oblitus.routemap.sh.projects.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task{
    public String description;
    public int id;
    public static int counter = 0;
    public TaskStatus status;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    private static boolean largerOccured = false;
    public static boolean isInProgress = false;

    
    // @JsonCreator(mode = Mode.DELEGATING)
    public Task(String description, int id, TaskStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.description = description;
        this.id = id;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        counter = id;
    }

    public Task() {
        counter++;
    }

    public String toConsole(boolean withHeader){
        StringBuilder builder = new StringBuilder();
        if(withHeader){
            builder.append("| id");
            if(id > 99){
                builder.append(" ");
            }
            builder.append(" | status");
            if(status == TaskStatus.IN_PROGRESS || isInProgress){
                builder.append("           ");
            }
            builder.append(" | description");
            if(description.length() > 11){
                builder.append("                ");
            }
            builder.append(" |    createdAt    ");
            builder.append(" |    updatedAt    ");
            builder.append(" |");
            builder.append('\n');
        }
        builder.append("| ");
        builder.append(id);
        if(id < 9){
            builder.append(" ");
        }
        builder.append(" | ");
        builder.append(status.value);
        if(status == TaskStatus.IN_PROGRESS){
            builder.append("      ");
        }
        else if(isInProgress){
            builder.append("             ");
        }
        else{
            builder.append("  ");
        }
        builder.append(" | ");
        if(description.length() < 11 && !largerOccured){
            builder.append(description);
            for(int i = 0; i < 11-description.length(); i++){
                builder.append(" ");
            }
        }else if(largerOccured || (description.length() < 26 && description.length() > 11)){
            String temp = "                          ";
            var sb1 = temp.substring(description.length()-1);
            builder.append(description + sb1);
            largerOccured = true;
        }
        
        builder.append(" | ");
        builder.append(createdAt.format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:MM")));
        builder.append(" | ");
        builder.append(updatedAt.format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:MM")));
        builder.append(" |");
        return builder.toString();
    }

    public int getId() {
        return this.id;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public void setStatus(TaskStatus status){
        this.status = status;
    }
    
}

