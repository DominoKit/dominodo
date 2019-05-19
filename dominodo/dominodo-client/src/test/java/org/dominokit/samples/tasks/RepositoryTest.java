package org.dominokit.samples.tasks;

import com.google.j2cl.junit.apt.J2clTestInput;
import elemental2.promise.Promise;
import org.dominokit.samples.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

import static org.dominokit.samples.tasks.TasksRepository._1_day;

@J2clTestInput(RepositoryTest.class)
public class RepositoryTest {

    public static final String MILESTONE_1_0 = "Milestone 1.0";
    public static final String MILESTONE_3_0 = "Milestone 3.0";
    private TasksRepository tasksRepository;
    private Project dominoUi;
    private Project nalue;
    private Project gwt;
    private Project movies;
    private Task task1;
    private Task task2;
    private Task completedTask;
    private Task task4;
    private Task task5;
    private Task task6;

    @Before
    public void setUp() throws Exception {
        tasksRepository = new TasksRepository();

        // ------------ PROJECTS --------------
        dominoUi = new Project();
        dominoUi.setName(Constants.DOMINO_UI);
        dominoUi.setColor("INDIGO");
        dominoUi.setIcon("widgets");

        TasksRepository.PROJECTS.put(dominoUi.getName(), dominoUi);

        nalue = new Project();
        nalue.setName(Constants.NALU_MVP);
        nalue.setColor("BLUE");
        nalue.setIcon("call_split");

        TasksRepository.PROJECTS.put(nalue.getName(), nalue);

        gwt = new Project();
        gwt.setName(Constants.GWT);
        gwt.setColor("PINK");
        gwt.setIcon("polymer");

        TasksRepository.PROJECTS.put(gwt.getName(), gwt);

        movies = new Project();
        movies.setName(Constants.MOVIES);
        movies.setColor("BROWN");
        movies.setIcon("camera_roll");

        TasksRepository.PROJECTS.put(movies.getName(), movies);


        // ------------ tasks ----------
        task1 = new Task();
        task1.setDueDate(new Date());
        task1.setPriority(Priority.NORMAL);
        task1.setDescription("TextBox setRightAddon, removeRightAddon and setRightAddon again throws Exception");
        task1.setTitle("Text box icons");
        task1.setProject(dominoUi);
        task1.setStatus(Status.ACTIVE);

        task1.getTags().addAll(Arrays.asList("Bug", MILESTONE_1_0));
        task1.getAttachments().addAll(Arrays.asList("File 1", "File 2"));


        task2 = new Task();
        task2.setDueDate(new Date(new Date().getTime() + (2 * _1_day)));
        task2.setPriority(Priority.NORMAL);
        task2.setDescription("Router: create the possibility to work with permissions in case of routing");
        task2.setTitle("Router/Permissions");
        task2.setProject(nalue);
        task2.setStatus(Status.ACTIVE);

        task2.getTags().addAll(Arrays.asList("Enhancements", "Router", MILESTONE_1_0));
        task2.getAttachments().addAll(Arrays.asList("File 1", "File 2", "File 3"));


        completedTask = new Task();
        completedTask.setDueDate(new Date(new Date().getTime() + (_1_day)));
        completedTask.setPriority(Priority.IMPORTANT);
        completedTask.setDescription("Support for Microsoft IE 11 Browser)");
        completedTask.setTitle("IE 11");
        completedTask.setProject(dominoUi);
        completedTask.setStatus(Status.COMPLETED);

        completedTask.getTags().addAll(Arrays.asList("Bug", "Browser", MILESTONE_1_0));
        completedTask.getAttachments().addAll(Arrays.asList("File 1", "File 2"));


        task4 = new Task();
        task4.setDueDate(new Date());
        task4.setPriority(Priority.NORMAL);
        task4.setDescription("Tag field enhancement (open the select list on focus, navigate the list with arrows)");
        task4.setTitle("Inter active tag field");
        task4.setProject(dominoUi);
        task4.setStatus(Status.ACTIVE);

        task4.getTags().addAll(Arrays.asList("Enhancements", MILESTONE_1_0));
        task4.getAttachments().addAll(Arrays.asList("File 1", "File 2", "File 3"));


        task5 = new Task();
        task5.setDueDate(new Date(new Date().getTime() + (3 * _1_day)));
        task5.setPriority(Priority.IMPORTANT);
        task5.setDescription("FormPanel setEncoding charset is not working");
        task5.setTitle("Forms question");
        task5.setProject(gwt);
        task5.setStatus(Status.ACTIVE);

        task5.getTags().addAll(Arrays.asList("Question", "Invalid", MILESTONE_3_0));
        task5.getAttachments().addAll(Arrays.asList("File 1", "File 2", "File 3", "File 4"));


        task6 = new Task();
        task6.setDueDate(new Date(new Date().getTime() + (3 * _1_day)));
        task6.setPriority(Priority.IMPORTANT);
        task6.setDescription("Spirited away was recommended to me by a friend");
        task6.setTitle("Watch spirited away");
        task6.setProject(movies);
        task6.setStatus(Status.ACTIVE);

        task6.getTags().addAll(Arrays.asList("Anime", "Ghibli"));
        task6.getAttachments().addAll(Arrays.asList("File 1"));

    }

    @Test(timeout = 500)
    public Promise<String> testAddTask() {
        tasksRepository.addTask(task1);

        Assert.assertEquals(tasksRepository.listAll().size(), 1);
        Assert.assertEquals(tasksRepository.listAll().get(0), task1);

        return Promise.resolve("success").then(ignore -> Promise.resolve("pass"));
    }

    @Test(timeout = 500)
    public Promise<String> testRemoveTask() {
        tasksRepository.addTask(task1);
        tasksRepository.removeTask(task1);

        Assert.assertEquals(tasksRepository.listAll().size(), 0);

        return Promise.resolve("success").then(ignore -> Promise.resolve("pass"));
    }

    @Test(timeout = 500)
    public Promise<String> testListAll() {
        tasksRepository.addTask(task1);
        tasksRepository.addTask(task2);
        tasksRepository.addTask(completedTask);

        Assert.assertEquals(tasksRepository.listAll().size(), 2);
        Assert.assertTrue(tasksRepository.listAll().contains(task1));
        Assert.assertTrue(tasksRepository.listAll().contains(task2));
        Assert.assertFalse(tasksRepository.listAll().contains(completedTask));

        return Promise.resolve("success").then(ignore -> Promise.resolve("pass"));
    }

    @Test(timeout = 500)
    public Promise<String> testFindByTag() {
        tasksRepository.addTask(task1);
        tasksRepository.addTask(task2);
        tasksRepository.addTask(task4);

        Assert.assertEquals(tasksRepository.findByTag(MILESTONE_1_0).size(), 2);
        Assert.assertTrue(tasksRepository.findByTag(MILESTONE_1_0).contains(task1));
        Assert.assertTrue(tasksRepository.findByTag(MILESTONE_1_0).contains(task2));
        Assert.assertFalse(tasksRepository.findByTag(MILESTONE_1_0).contains(task4));

        return Promise.resolve("success").then(ignore -> Promise.resolve("pass"));
    }

    @Test(timeout = 500)
    public Promise<String> testFindTasks() {
        tasksRepository.addTask(task1);
        tasksRepository.addTask(task2);
        tasksRepository.addTask(task4);

        Assert.assertEquals(tasksRepository.findTasks(dominoUi.getName()).size(), 2);
        Assert.assertTrue(tasksRepository.findTasks(dominoUi.getName()).contains(task1));
        Assert.assertTrue(tasksRepository.findTasks(dominoUi.getName()).contains(task4));
        Assert.assertEquals(tasksRepository.findTasks("router").size(), 1);
        Assert.assertFalse(tasksRepository.findTasks("router").contains(task2));

        return Promise.resolve("success").then(ignore -> Promise.resolve("pass"));
    }

    @Test(timeout = 500)
    public Promise<String> testListByProjectName() {
        tasksRepository.addTask(task1);
        tasksRepository.addTask(task2);
        tasksRepository.addTask(task4);

        Assert.assertEquals(tasksRepository.listByProjectName(dominoUi.getName()).size(), 2);
        Assert.assertTrue(tasksRepository.listByProjectName(dominoUi.getName()).contains(task1));
        Assert.assertTrue(tasksRepository.listByProjectName(dominoUi.getName()).contains(task4));
        Assert.assertEquals(tasksRepository.listByProjectName(nalue.getName()).size(), 1);
        Assert.assertFalse(tasksRepository.listByProjectName(nalue.getName()).contains(task2));

        return Promise.resolve("success").then(ignore -> Promise.resolve("pass"));
    }

    @Test(timeout = 500)
    public Promise<String> testListByPriority() {
        tasksRepository.addTask(task1);
        tasksRepository.addTask(task2);
        tasksRepository.addTask(task5);

        Assert.assertEquals(tasksRepository.listByPriority(Priority.NORMAL).size(), 2);
        Assert.assertTrue(tasksRepository.listByPriority(Priority.NORMAL).contains(task1));
        Assert.assertTrue(tasksRepository.listByPriority(Priority.NORMAL).contains(task2));
        Assert.assertEquals(tasksRepository.listByPriority(Priority.IMPORTANT).size(), 1);
        Assert.assertFalse(tasksRepository.listByPriority(Priority.IMPORTANT).contains(task5));

        return Promise.resolve("success").then(ignore -> Promise.resolve("pass"));
    }
}
