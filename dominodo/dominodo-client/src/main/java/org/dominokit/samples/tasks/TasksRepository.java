package org.dominokit.samples.tasks;

import elemental2.core.JsDate;
import org.dominokit.domino.ui.icons.Icons;
import org.dominokit.domino.ui.style.Color;
import org.dominokit.samples.*;

import java.util.*;
import java.util.stream.Collectors;

public class TasksRepository {

    private List<Task> tasks = new ArrayList<>();

    public final static Map<String, Project> PROJECTS = new HashMap<>();

    private final int _1_day = 24 * 60 * 60 * 1000;

    public TasksRepository() {
        addInitialData();
    }

    private void addInitialData() {

        // ------------ PROJECTS --------------
        Project dominoUi = new Project();
        dominoUi.setName(Constants.DOMINO_UI);
        dominoUi.setColor(Color.INDIGO.getName());
        dominoUi.setIcon(Icons.ALL.widgets().getName());

        PROJECTS.put(dominoUi.getName(), dominoUi);

        Project nalue = new Project();
        nalue.setName(Constants.NALU_MVP);
        nalue.setColor(Color.BLUE.getName());
        nalue.setIcon(Icons.ALL.call_split().getName());

        PROJECTS.put(nalue.getName(), nalue);

        Project gwt = new Project();
        gwt.setName(Constants.GWT);
        gwt.setColor(Color.PINK.getName());
        gwt.setIcon(Icons.ALL.polymer().getName());

        PROJECTS.put(gwt.getName(), gwt);

        Project movies = new Project();
        movies.setName(Constants.MOVIES);
        movies.setColor(Color.BROWN.getName());
        movies.setIcon(Icons.ALL.camera_roll().getName());

        PROJECTS.put(movies.getName(), movies);


        // ------------ tasks ----------
        Task task1 = new Task();
        task1.setDueDate(new Date());
        task1.setPriority(Priority.NORMAL);
        task1.setDescription("TextBox setRightAddon, removeRightAddon and setRightAddon again throws Exception");
        task1.setTitle("Text box icons");
        task1.setProject(dominoUi);
        task1.setStatus(Status.ACTIVE);

        task1.getTags().addAll(Arrays.asList("Bug", "Milestone 1.0"));
        task1.getAttachments().addAll(Arrays.asList("File 1", "File 2"));

        tasks.add(task1);


        Task task2 = new Task();
        task2.setDueDate(new Date(new Date().getTime() + (2 * _1_day)));
        task2.setPriority(Priority.NORMAL);
        task2.setDescription("Router: create the possibility to work with permissions in case of routing");
        task2.setTitle("Router/Permissions");
        task2.setProject(nalue);
        task2.setStatus(Status.ACTIVE);

        task2.getTags().addAll(Arrays.asList("Enhancements", "Router", "Milestone 1.0"));
        task2.getAttachments().addAll(Arrays.asList("File 1", "File 2", "File 3"));

        tasks.add(task2);

        Task task3 = new Task();
        task3.setDueDate(new Date(new Date().getTime() + (_1_day)));
        task3.setPriority(Priority.IMPORTANT);
        task3.setDescription("Support for Microsoft IE 11 Browser)");
        task3.setTitle("IE 11");
        task3.setProject(dominoUi);
        task3.setStatus(Status.ACTIVE);

        task3.getTags().addAll(Arrays.asList("Bug", "Browser", "Milestone 1.0"));
        task3.getAttachments().addAll(Arrays.asList("File 1", "File 2"));

        tasks.add(task3);


        Task task4 = new Task();
        task4.setDueDate(new Date());
        task4.setPriority(Priority.NORMAL);
        task4.setDescription("Tag field enhancement (open the select list on focus, navigate the list with arrows)");
        task4.setTitle("Inter active tag field");
        task4.setProject(dominoUi);
        task4.setStatus(Status.ACTIVE);

        task4.getTags().addAll(Arrays.asList("Enhancements", "Milestone 1.0"));
        task4.getAttachments().addAll(Arrays.asList("File 1", "File 2", "File 3"));

        tasks.add(task4);

        Task task5 = new Task();
        task5.setDueDate(new Date(new Date().getTime() + (3 * _1_day)));
        task5.setPriority(Priority.IMPORTANT);
        task5.setDescription("FormPanel setEncoding charset is not working");
        task5.setTitle("Forms question");
        task5.setProject(gwt);
        task5.setStatus(Status.ACTIVE);

        task5.getTags().addAll(Arrays.asList("Question", "Invalid", "Milestone 3.0"));
        task5.getAttachments().addAll(Arrays.asList("File 1", "File 2", "File 3", "File 4"));

        tasks.add(task5);

        Task task6 = new Task();
        task6.setDueDate(new Date(new Date().getTime() + (3 * _1_day)));
        task6.setPriority(Priority.IMPORTANT);
        task6.setDescription("Spirited away was recommended to me by a friend");
        task6.setTitle("Watch spirited away");
        task6.setProject(movies);
        task6.setStatus(Status.ACTIVE);

        task6.getTags().addAll(Arrays.asList("Anime", "Ghibli"));
        task6.getAttachments().addAll(Arrays.asList("File 1"));

        tasks.add(task6);
    }

    public List<Task> listAll() {
        return tasks.stream().filter(Task::isActive).collect(Collectors.toList());
    }

    public List<Task> listByProjectName(String projectName) {
        return tasks.stream().filter(task -> task.getProject().getName().equals(projectName) && task.isActive()).collect(Collectors.toList());
    }

    public List<Task> listByPriority(Priority priority) {
        return tasks.stream().filter(task -> task.getPriority().equals(priority) && task.isActive()).collect(Collectors.toList());
    }

    public List<Task> listTodayTasks() {
        return tasks.stream().filter(task -> {
            JsDate todayDate = new JsDate();
            JsDate taskDate = new JsDate((double) task.getDueDate().getTime());
            return todayDate.getYear() == taskDate.getYear()
                    && todayDate.getMonth() == taskDate.getMonth()
                    && todayDate.getDate() == taskDate.getDate()
                    && task.isActive();

        }).collect(Collectors.toList());
    }

    public List<Task> listNextWeekTasks() {
        return tasks.stream().filter(task -> {
            JsDate todayDate = new JsDate();
            JsDate taskDate = new JsDate((double) task.getDueDate().getTime());
            double diff = taskDate.getTime() - todayDate.getTime();
            return diff > 0
                    && diff <= 604800000
                    && task.isActive();

        }).collect(Collectors.toList());
    }

    public List<Task> listResolved(){
        return tasks.stream().filter(task -> !task.isActive()).collect(Collectors.toList());
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public List<Task> findByTag(String tag){
        return tasks.stream().filter(task -> task.isActive() && task.getTags().stream().anyMatch(s -> s.toLowerCase().contains(tag.toLowerCase())))
                .collect(Collectors.toList());
    }

    public List<Task> findTasks(String searchText) {
        return tasks.stream().filter(task -> task.isActive() && (task.getTitle().contains(searchText.toLowerCase())
                || task.getDescription().toLowerCase().contains(searchText.toLowerCase())
                || task.getProject().getName().toLowerCase().contains(searchText.toLowerCase())
                || task.getTags().stream().anyMatch(tag -> tag.toLowerCase().contains(searchText.toLowerCase()))))
                .collect(Collectors.toList());
    }

}
