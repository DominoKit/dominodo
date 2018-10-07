package org.dominokit.samples;

import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;
import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.icons.Icons;
import org.dominokit.domino.ui.layout.Layout;
import org.dominokit.domino.ui.layout.TopBarAction;
import org.dominokit.domino.ui.search.Search;
import org.dominokit.domino.ui.style.Color;
import org.dominokit.domino.ui.style.ColorScheme;
import org.dominokit.samples.menu.MenuComponent;
import org.dominokit.samples.settings.SettingsComponent;
import org.dominokit.samples.tasks.EditTaskDialog;
import org.dominokit.samples.tasks.TasksList;
import org.dominokit.samples.tasks.TasksRepository;

import java.util.List;

import static org.jboss.gwt.elemento.core.Elements.img;

public class App implements EntryPoint, HasMenuUiHandlers, HasTaskUiHandlers {

    private final TasksRepository tasksRepository = new TasksRepository();
    private Layout layout;
    private HasTasks currentTaskView;

    public void onModuleLoad() {

        Search search = Search.create()
                .onSearch(this::onSearch);

        layout = Layout.create("DominoDo");
        layout
                .navigationBar(navigationBar -> navigationBar.insertBefore(search, layout.getNavigationBar().firstChild()))
                .leftPanel(leftPanel -> leftPanel.appendChild(MenuComponent.create(App.this)))
                .rightPanel(rightPanel -> rightPanel.appendChild(new SettingsComponent()))
                .topBar(topBar -> topBar
                        .appendChild(TopBarAction.create(Icons.ALL.settings())
                                .addClickListener(evt -> layout.showRightPanel()))
                        .appendChild(TopBarAction.create(Icons.ALL.search())
                                .addClickListener(evt -> search.open())))
                .autoFixLeftPanel()
                .setLogo(img("./todo.png"))
                .show(ColorScheme.BLUE);


        Button addButton = Button.create(Icons.ALL.add())
                .setBackground(Color.THEME)
                .setContent("ADD TASK")
                .styler(style -> style.add("add-button"))
                .addClickListener(evt -> showAddDialog());


        DomGlobal.document.body.appendChild(addButton.asElement());

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
            layout.setContent(TasksList.create("All Tasks", tasks, App.this)
                    .update(animate));
        };

        this.currentTaskView.update(true);
    }

    @Override
    public void onListResolved() {
        this.currentTaskView = (animate) -> {
            List<Task> tasks = tasksRepository.listResolved();
            layout.setContent(TasksList.create("Resolved", tasks, App.this)
                    .update(animate));
        };

        this.currentTaskView.update(true);
    }

    @Override
    public void onTodaySelected() {
        this.currentTaskView = (animate) -> {
            List<Task> tasks = tasksRepository.listTodayTasks();
            layout.setContent(TasksList.create("Today's tasks", tasks, App.this)
                    .update(animate));
        };

        this.currentTaskView.update(true);
    }

    @Override
    public void onNextWeekSelected() {
        this.currentTaskView = (animate) -> {
            List<Task> tasks = tasksRepository.listNextWeekTasks();
            layout.setContent(TasksList.create("Next week tasks", tasks, App.this)
                    .update(animate));
        };

        this.currentTaskView.update(true);
    }

    @Override
    public void onPrioritySelected(Priority priority) {

        this.currentTaskView = (animate) -> {
            List<Task> tasks = tasksRepository.listByPriority(priority);
            layout.setContent(TasksList.create((Priority.IMPORTANT.equals(priority) ? "Important" : "Normal") + " tasks", tasks, App.this)
                    .update(animate));
        };

        this.currentTaskView.update(true);
    }

    @Override
    public void onProjectSelected(String projectName) {
        this.currentTaskView = (animate) -> {
            List<Task> tasks = tasksRepository.listByProjectName(projectName);
            layout.setContent(TasksList.create(projectName + " tasks", tasks, App.this)
                    .update(animate));
        };

        this.currentTaskView.update(true);
    }

    @Override
    public void onTagSelected(String tag) {
        this.currentTaskView = (animate) -> {
            List<Task> tasks = tasksRepository.findByTag(tag);
            layout.setContent(TasksList.create("Search tag -"+tag, tasks, App.this)
                    .update(animate));
        };

        this.currentTaskView.update(true);
    }


    private void onSearch(String searchToken) {
        this.currentTaskView = (animate) -> {
            List<Task> tasks = tasksRepository.findTasks(searchToken);
            layout.setContent(TasksList.create("Search results", tasks, App.this)
                    .update(animate));
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
}
