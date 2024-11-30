package com.oblitus.routemap.sh.projects.core;

import java.io.IOException;
import java.util.List;

import com.oblitus.routemap.sh.projects.models.Task;
import com.oblitus.routemap.sh.projects.models.TaskStatus;

public class TaskTracker {
    private TaskMenager manager = new TaskMenager();

    public TaskTracker() {
        try {
            manager.loadData();
        } catch (IOException e) {
        }
    }

    public void doAction(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("No arguments were provided.");
            System.out.println("For add task use 'add <task description>'");
            System.out.println("For update task use 'update <task id> <task description>'");
            System.out.println("For delete task use 'delete <task id>'");
            System.out.println("For change task status to 'in progress' use 'mark-in-progress <task id>'");
            System.out.println("For change task status to 'done' use 'mark-done <task id>'");
            System.out.println("For list tasks use 'list'");
            System.out.println("For list tasks with status 'todo' use 'list todo'");
            System.out.println("For list tasks with status 'in progress' use 'list in-progress'");
            System.out.println("For list tasks with status 'done' use 'list done'");
            return;
        }
        switch (args[0]) {
            case "add":
            if (args.length == 1) {
                System.out.println("Provide description for a new task. Ex: add 'Get some snack'");
                return;
            }
            System.out.println(manager.create(args[1]).toConsole(true));
            System.out.println("Task added!");
            break;
            case "update":
            if (args.length == 2 || args.length == 1) {
                System.out.println("Provide id and new description for a task. Ex: update 1 'Get some juice'");
                return;
            }
            manager.update(Integer.parseInt(args[1]), args[2]);
            System.out.println("Task updated!");
            break;
            case "delete":
            if (args.length == 1) {
                System.out.println("Provide id for a task. Ex: delete 1");
                return;
            }
            manager.delete(Integer.parseInt(args[1]));
            System.out.println("Task deleted!");
            break;
            case "mark-in-progress":
            if (args.length == 1) {
                System.out.println("Provide id for a task. Ex: mark-in-progress 1");
                return;
            }
            manager.update(Integer.parseInt(args[1]), TaskStatus.IN_PROGRESS);
            System.out.println("Task status updated!");
            break;
            case "mark-done":
            if (args.length == 1) {
                System.out.println("Provide id for a task. Ex: mark-done 1");
                return;
            }
            manager.update(Integer.parseInt(args[1]), TaskStatus.DONE);
            System.out.println("Task status updated!");
            break;
            case "list":
            if(args.length == 1){
                boolean first = true;
                for (Task task : manager.getTasks()) {
                    System.out.println(task.toConsole(first));
                    if(first){
                        first = false;
                    }
                }
                return;
            }
            if(args[1] == "done"){
                boolean first = true;
                for (Task task : manager.getTasks(TaskStatus.DONE)) {
                    System.out.println(task.toConsole(first));
                    if(first){
                        first = false;
                    }
                }
            }
            if(args[1] == "todo"){
                boolean first = true;
                for (Task task : manager.getTasks(TaskStatus.TODO)) {
                    System.out.println(task.toConsole(first));
                    if(first){
                        first = false;
                    }
                }
            }
            if(args[1] == "in-progress"){
                boolean first = true;
                for (Task task : manager.getTasks(TaskStatus.IN_PROGRESS)) {
                    System.out.println(task.toConsole(first));
                    if(first){
                        first = false;
                    }
                }
            }
            break;
            default:
            break;
        }
        manager.saveData();
        return;
    }
}
