package org.dominokit.samples;

import org.dominokit.domino.ui.icons.lib.Icons;
import org.dominokit.domino.ui.layout.AppLayout;
import org.dominokit.domino.ui.shaded.button.Button;
import org.dominokit.domino.ui.shaded.layout.Layout;
import org.dominokit.domino.ui.shaded.layout.LayoutActionItem;
import org.dominokit.domino.ui.shaded.search.Search;
import org.dominokit.domino.ui.shaded.style.Color;
import org.dominokit.domino.ui.shaded.style.ColorScheme;
import org.dominokit.domino.ui.shaded.utils.DominoElement;
import org.dominokit.domino.ui.themes.DominoThemeAccent;
import org.dominokit.domino.ui.themes.DominoThemeDefault;
import org.dominokit.domino.ui.themes.DominoThemeLight;
import org.dominokit.domino.ui.themes.DominoThemeManager;
import org.dominokit.domino.ui.utils.PostfixAddOn;
import org.dominokit.domino.ui.utils.PrefixAddOn;
import org.dominokit.samples.menu.MenuComponent;
import org.dominokit.samples.settings.SettingsComponent;
import org.dominokit.samples.tasks.EditTaskDialog;
import org.dominokit.samples.tasks.TasksList;
import org.dominokit.samples.tasks.TasksRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.dominokit.samples.tasks.TasksRepository._1_day;
import static org.jboss.elemento.Elements.img;

public class DominoDo implements HasMenuUiHandlers, HasTaskUiHandlers {

    private final TasksRepository tasksRepository = new TasksRepository();
    private AppLayout layout;
    private HasTasks currentTaskView;

    public void run(String title) {

        DominoThemeManager.INSTANCE.apply(DominoThemeDefault.INSTANCE);
        DominoThemeManager.INSTANCE.apply(DominoThemeLight.INSTANCE);
        DominoThemeManager.INSTANCE.apply(DominoThemeAccent.BLUE);
        addInitialData();

        Search search = Search.create()
                .onSearch(this::onSearch);

        layout = AppLayout.create(title);

        layout
            .setFixLeftDrawer(true)
            .withHeader((parent, header) -> {
                header.insertFirst(search.element());
            })
            .withNavBar((parent, navBar) -> {
                navBar
                    .appendChild(PrefixAddOn.of(img("./todo.png").element()))
                    .appendChild(PostfixAddOn.of(Icons.magnify()
                        .clickable()
                        .addClickListener(evt -> search.open())
                    ))
                    .appendChild(PostfixAddOn.of(Icons.cog()
                        .clickable()
                        .addClickListener(evt -> layout.showRightDrawer())
                    ))
                    ;
            })
            .withLeftDrawer((parent, leftDrawer) -> {
                leftDrawer.appendChild(MenuComponent.create(DominoDo.this).element());
            });

        DominoElement.body().appendChild(layout.element());

        Button addButton = Button.create(org.dominokit.domino.ui.shaded.icons.Icons.ALL.add())
                .setBackground(Color.THEME)
                .setContent("ADD TASK")
                .styler(style -> style.add("add-button"))
                .addClickListener(evt -> showAddDialog());

        DominoElement.body().appendChild(addButton.element());

        listAllTasks();
    }

    private void listAllTasks() {
        onAllSelected();
    }

    private void showAddDialog() {
        EditTaskDialog.create("Add task")
                .onSave(task -> {
                    tasksRepository.addTask(task);
                    currentTaskView.update(false);
                })
                .getModalDialog()
                .open();
    }

    @Override
    public void onAllSelected() {
        this.currentTaskView = (animate) -> {
            List<Task> tasks = tasksRepository.listAll();

            layout.withContent((parent, content) -> content.setContent(TasksList.create("All Tasks", tasks, DominoDo.this)
                .update(animate).element()));
        };

        this.currentTaskView.update(true);
    }

    @Override
    public void onListResolved() {
        this.currentTaskView = (animate) -> {
            List<Task> tasks = tasksRepository.listResolved();
            layout.withContent((parent, content) -> content.setContent(TasksList.create("Resolved", tasks, DominoDo.this)
                    .update(animate).element()));
        };

        this.currentTaskView.update(true);
    }

    @Override
    public void onTodaySelected() {
        this.currentTaskView = (animate) -> {
            List<Task> tasks = tasksRepository.listTodayTasks();
            layout.withContent((parent, content) -> content.setContent(TasksList.create("Today's tasks", tasks, DominoDo.this)
                    .update(animate).element()));
        };

        this.currentTaskView.update(true);
    }

    @Override
    public void onNextWeekSelected() {
        this.currentTaskView = (animate) -> {
            List<Task> tasks = tasksRepository.listNextWeekTasks();
            layout.withContent((parent, content) -> content.setContent(TasksList.create("Next week tasks", tasks, DominoDo.this)
                    .update(animate).element()));
        };

        this.currentTaskView.update(true);
    }

    @Override
    public void onPrioritySelected(Priority priority) {

        this.currentTaskView = (animate) -> {
            List<Task> tasks = tasksRepository.listByPriority(priority);
            layout.withContent((parent, content) -> content.setContent(TasksList.create((Priority.IMPORTANT.equals(priority) ? "Important" : "Normal") + " tasks", tasks, DominoDo.this)
                    .update(animate).element()));
        };

        this.currentTaskView.update(true);
    }

    @Override
    public void onProjectSelected(String projectName) {
        this.currentTaskView = (animate) -> {
            List<Task> tasks = tasksRepository.listByProjectName(projectName);
            layout.withContent((parent, content) -> content.setContent(TasksList.create(projectName + " tasks", tasks, DominoDo.this)
                    .update(animate).element()));
        };

        this.currentTaskView.update(true);
    }

    @Override
    public void onTagSelected(String tag) {
        this.currentTaskView = (animate) -> {
            List<Task> tasks = tasksRepository.findByTag(tag);
            layout.withContent((parent, content) -> content.setContent(TasksList.create("Search tag -"+tag, tasks, DominoDo.this)
                    .update(animate).element()));
        };

        this.currentTaskView.update(true);
    }


    private void onSearch(String searchToken) {
        this.currentTaskView = (animate) -> {
            List<Task> tasks = tasksRepository.findTasks(searchToken);
            layout.withContent((parent, content) -> content.setContent(TasksList.create("Search results", tasks, DominoDo.this)
                    .update(animate).element()));
        };

        this.currentTaskView.update(true);
    }



    @Override
    public void onTaskDelete(Task task) {
        tasksRepository.removeTask(task);
        this.currentTaskView.update(false);
    }

    @Override
    public void onResolved(Task task) {
        task.setStatus(Status.COMPLETED);
        this.currentTaskView.update(false);
    }

    @Override
    public void onUnResolve(Task task) {
        task.setStatus(Status.ACTIVE);
        this.currentTaskView.update(false);
    }

    @Override
    public void onEditTask(Task task) {
        EditTaskDialog.create("Add task", task)
                .onSave(updatedTask -> {
                    tasksRepository.addTask(updatedTask);
                    currentTaskView.update(false);
                })
                .getModalDialog()
                .open();
    }

    @Override
    public void onTaskPriorityChange(Task task) {
        this.currentTaskView.update(false);
    }



    private void addInitialData() {

        // ------------ PROJECTS --------------
        Project dominoUi = new Project();
        dominoUi.setName(Constants.DOMINO_UI);
        dominoUi.setColor("INDIGO");
        dominoUi.setIcon("widgets");

        TasksRepository.PROJECTS.put(dominoUi.getName(), dominoUi);

        Project nalue = new Project();
        nalue.setName(Constants.NALU_MVP);
        nalue.setColor("BLUE");
        nalue.setIcon("call_split");

        TasksRepository.PROJECTS.put(nalue.getName(), nalue);

        Project gwt = new Project();
        gwt.setName(Constants.GWT);
        gwt.setColor("PINK");
        gwt.setIcon("polymer");

        TasksRepository.PROJECTS.put(gwt.getName(), gwt);

        Project movies = new Project();
        movies.setName(Constants.MOVIES);
        movies.setColor("BROWN");
        movies.setIcon("camera_roll");

        TasksRepository.PROJECTS.put(movies.getName(), movies);


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

        tasksRepository.addTask(task1);


        Task task2 = new Task();
        task2.setDueDate(new Date(new Date().getTime() + (2 * _1_day)));
        task2.setPriority(Priority.NORMAL);
        task2.setDescription("Router: create the possibility to work with permissions in case of routing");
        task2.setTitle("Router/Permissions");
        task2.setProject(nalue);
        task2.setStatus(Status.ACTIVE);

        task2.getTags().addAll(Arrays.asList("Enhancements", "Router", "Milestone 1.0"));
        task2.getAttachments().addAll(Arrays.asList("File 1", "File 2", "File 3"));

        tasksRepository.addTask(task2);

        Task task3 = new Task();
        task3.setDueDate(new Date(new Date().getTime() + (_1_day)));
        task3.setPriority(Priority.IMPORTANT);
        task3.setDescription("Support for Microsoft IE 11 Browser)");
        task3.setTitle("IE 11");
        task3.setProject(dominoUi);
        task3.setStatus(Status.ACTIVE);

        task3.getTags().addAll(Arrays.asList("Bug", "Browser", "Milestone 1.0"));
        task3.getAttachments().addAll(Arrays.asList("File 1", "File 2"));

        tasksRepository.addTask(task3);


        Task task4 = new Task();
        task4.setDueDate(new Date());
        task4.setPriority(Priority.NORMAL);
        task4.setDescription("Tag field enhancement (open the select list on focus, navigate the list with arrows)");
        task4.setTitle("Inter active tag field");
        task4.setProject(dominoUi);
        task4.setStatus(Status.ACTIVE);

        task4.getTags().addAll(Arrays.asList("Enhancements", "Milestone 1.0"));
        task4.getAttachments().addAll(Arrays.asList("File 1", "File 2", "File 3"));

        tasksRepository.addTask(task4);

        Task task5 = new Task();
        task5.setDueDate(new Date(new Date().getTime() + (3 * _1_day)));
        task5.setPriority(Priority.IMPORTANT);
        task5.setDescription("FormPanel setEncoding charset is not working");
        task5.setTitle("Forms question");
        task5.setProject(gwt);
        task5.setStatus(Status.ACTIVE);

        task5.getTags().addAll(Arrays.asList("Question", "Invalid", "Milestone 3.0"));
        task5.getAttachments().addAll(Arrays.asList("File 1", "File 2", "File 3", "File 4"));

        tasksRepository.addTask(task5);

        Task task6 = new Task();
        task6.setDueDate(new Date(new Date().getTime() + (3 * _1_day)));
        task6.setPriority(Priority.IMPORTANT);
        task6.setDescription("Spirited away was recommended to me by a friend");
        task6.setTitle("Watch spirited away");
        task6.setProject(movies);
        task6.setStatus(Status.ACTIVE);

        task6.getTags().addAll(Arrays.asList("Anime", "Ghibli"));
        task6.getAttachments().addAll(Arrays.asList("File 1"));

        tasksRepository.addTask(task6);
    }
}