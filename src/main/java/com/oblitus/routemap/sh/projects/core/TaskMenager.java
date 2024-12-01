package com.oblitus.routemap.sh.projects.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.oblitus.routemap.sh.projects.models.Task;
import com.oblitus.routemap.sh.projects.models.TaskStatus;

public class TaskMenager {
    private List<Task> Tasks = new ArrayList<>();
    private Path FilePath = Paths.get("database.json");
    private ObjectMapper objectMapper = new ObjectMapper();
    public TaskMenager() {
        objectMapper.registerModule(new JavaTimeModule());
    }
    private boolean isFileExisting(){
        File file = FilePath.toFile();
        return file.exists() && file.isFile();
    }
    public void loadData() throws IOException{
        if(isFileExisting()){
            this.Tasks = objectMapper.readValue(FilePath.toFile(), new TypeReference<ArrayList<Task>>(){});
            return;
        }
        createFile();
    }
    public void saveData() throws IOException{
        if(!isFileExisting()){
            createFile();
        }
        objectMapper.writeValue(FilePath.toFile(), this.Tasks);
    }
    private void createFile() throws IOException{
        Files.createFile(FilePath);
    }
    public void add(Task data) {
        this.Tasks.add(data);
    }

    public Task get(int id) throws Exception {
        Iterator<Task> iterator = this.Tasks.iterator();
        while(iterator.hasNext()){
            Task task = iterator.next();
            if(task.getId() == id){
                return task;
            }
        }
        throw new Exception("There is no record with id "+id);
    }

    public void update(int id, String data) throws Exception {
        Task task = get(id);
        var index = this.Tasks.indexOf(task);
        task.setDescription(data);
        this.Tasks.set(index, task);
    }
    public void update(int id, TaskStatus status) throws Exception {
        Task task = get(id);
        var index = this.Tasks.indexOf(task);
        task.setStatus(status);
        this.Tasks.set(index, task);
    }
    public void delete(int id) throws Exception{
        Task task = get(id);
        var index = this.Tasks.indexOf(task);
        this.Tasks.remove(index);
    }
    public Task create(String data) {
        Task task = new Task();
        task.id = Task.counter;
        task.description = data;
        task.status = TaskStatus.TODO;
        task.createdAt = LocalDateTime.now();
        task.updatedAt = LocalDateTime.now();
        this.Tasks.add(task);
        return task;
    }
    public List<Task> getTasks(TaskStatus status){
        List<Task> temp = new ArrayList<>();
        Iterator<Task> iterator = this.Tasks.iterator();
        while(iterator.hasNext()){
            Task task = iterator.next();
            if(task.status == status){
                temp.add(task);
            }
        }
        return temp;
    }
    public List<Task> getTasks(){
        return this.Tasks;
    }
    
}
