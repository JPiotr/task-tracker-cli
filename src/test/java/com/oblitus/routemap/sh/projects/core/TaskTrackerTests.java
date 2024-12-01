package com.oblitus.routemap.sh.projects.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.oblitus.routemap.sh.projects.models.TaskStatus;

public class TaskTrackerTests {
    //fixme lunching all tests cause issue propably with slow IO operations and file is no created on time.
    TaskTracker taskTracker = new TaskTracker();

    //source: https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println#:~:text=private%20final%20ByteArrayOutputStream,System.setErr(originalErr)%3B%0A%7D
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    PrintStream originalErr = System.err;
    
    @AfterEach
    public void cleanFile() throws IOException {
        File file = Paths.get("database.json").toFile();
        file.delete();
        file.createNewFile();
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
    
    @BeforeEach
    public void addEntry() throws Exception {
        System.setErr(new PrintStream(errContent));
        System.setOut(new PrintStream(outContent));
    }
    
    @Test
    public void e2eAdd() throws Exception {
        String[] input = { "add", "test" };
        taskTracker.doAction(input);
        var tasks = taskTracker.getMenager().getTasks();
        assertEquals(true, outContent.toString().contains("Task added!"));
        assertEquals(1, tasks.size());
    }

    @Test
    public void e2eUpdate() throws Exception {
        String[] input = { "add", "test" };
        taskTracker.doAction(input);
        String[] update = { "update", "1", "updated" };

        taskTracker.doAction(update);

        var tasks = taskTracker.getMenager().getTasks();
        assertEquals("updated", tasks.getFirst().description);
        assertEquals(1, tasks.size());
        assertEquals(true, outContent.toString().contains("Task updated!"));
        
    }
    
    @Test
    public void e2eDelete() throws Exception {
        String[] input = { "add", "test" };
        taskTracker.doAction(input);
        String[] delete = { "delete", "1" };
        
        taskTracker.doAction(delete);
        
        var tasks = taskTracker.getMenager().getTasks();
        assertEquals(0, tasks.size());
        assertEquals(true, outContent.toString().contains("Task deleted!"));
    }

    @Test
    public void e2eList() throws Exception {
        String[] input = { "add", "test" };
        taskTracker.doAction(input);
        String[] list = { "list" };

        var tasks = taskTracker.getMenager().getTasks();
        taskTracker.doAction(list);

        assertEquals(1, tasks.size());
        assertEquals(true, outContent.toString().contains(
            "| id | status | description |    createdAt     |    updatedAt     |\n" + //
            "| 1  | todo   | test        |"));

    }
    @Test
    public void e2eListTodo() throws Exception {
        String[] input = { "add", "test" };
        taskTracker.doAction(input);
        String[] list = { "list","todo" };
        
        var tasks = taskTracker.getMenager().getTasks();
        taskTracker.doAction(list);

        assertEquals(1, tasks.size());
        assertEquals(true, outContent.toString().contains("| 1  | todo   | test        |"));

    }
    @Test
    public void e2eMarkInProgress() throws Exception{
        String[] input = { "add", "test" };
        taskTracker.doAction(input);
        String[] mark = {"mark-in-progress", "1"};
        
        var tasks = taskTracker.getMenager().getTasks();
        assertEquals(TaskStatus.TODO, tasks.getFirst().status);
        taskTracker.doAction(mark);
        
        assertEquals(TaskStatus.IN_PROGRESS, tasks.getFirst().status);
        assertEquals(true, outContent.toString().contains("Task status updated"));
    }
    @Test
    public void e2eListInProgress() throws Exception {
        String[] input = { "add", "test" };
        taskTracker.doAction(input);
        String[] mark = {"mark-in-progress", "1"};
        String[] list = { "list","in-progress" };
        
        var tasks = taskTracker.getMenager().getTasks();
        assertEquals(TaskStatus.TODO, tasks.getFirst().status);
        taskTracker.doAction(mark);
        taskTracker.doAction(list);
        
        assertEquals(1, tasks.size());
        assertEquals(TaskStatus.IN_PROGRESS, tasks.getFirst().status);
        assertEquals(true, outContent.toString().contains("| 1  | in-progress       | test        |"));

    }
    @Test
    public void e2eMarkDone() throws Exception{
        String[] input = { "add", "test" };
        taskTracker.doAction(input);
        String[] mark = {"mark-done", "1"};
        
        var tasks = taskTracker.getMenager().getTasks();
        taskTracker.doAction(mark);
        
        assertEquals(TaskStatus.DONE, tasks.getFirst().status);
        assertEquals(true, outContent.toString().contains("Task status updated"));
    }
    @Test
    public void e2eListDone() throws Exception {
        String[] input = { "add", "test" };
        taskTracker.doAction(input);
        String[] mark = {"mark-done", "1"};
        String[] list = { "list","done" };
        
        var tasks = taskTracker.getMenager().getTasks();
        assertEquals(TaskStatus.TODO, tasks.getFirst().status);
        taskTracker.doAction(mark);
        taskTracker.doAction(list);
        
        assertEquals(1, tasks.size());
        assertEquals(TaskStatus.DONE, tasks.getFirst().status);
        assertEquals(true, outContent.toString().contains("| 1  | done   | test        |"));

    }
}
