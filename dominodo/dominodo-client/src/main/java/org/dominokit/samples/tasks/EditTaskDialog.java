package org.dominokit.samples.tasks;

import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.datepicker.DateBox;
import org.dominokit.domino.ui.forms.*;
import org.dominokit.domino.ui.grid.Row;
import org.dominokit.domino.ui.icons.Icons;
import org.dominokit.domino.ui.modals.ModalDialog;
import org.dominokit.domino.ui.tag.TagsInput;
import org.dominokit.samples.Constants;
import org.dominokit.samples.Priority;
import org.dominokit.samples.Project;
import org.dominokit.samples.Task;
import org.dominokit.samples.attachments.FileUploadComponent;
import org.gwtproject.editor.client.Editor;
import org.gwtproject.editor.client.SimpleBeanEditorDriver;
import org.gwtproject.editor.client.annotation.IsDriver;

import java.util.Date;
import java.util.function.Consumer;

import static java.util.Objects.nonNull;
import static org.jboss.elemento.Elements.h;

public class EditTaskDialog implements Editor<Task>, HasTask {

    private final Driver driver;
    private Task task;

    @IsDriver
    interface Driver extends SimpleBeanEditorDriver<Task, EditTaskDialog> {
    }

    private ModalDialog modalDialog;

    TextBox title;
    TextArea description;
    DateBox dueDate;
    Select<Priority> priority;
    Select<Project> project;
    TagsInput<String> tags;

    private Consumer<Task> onCreateHandler = task -> {
    };

    private FieldsGrouping fieldsGrouping = FieldsGrouping.create();

    public EditTaskDialog(String dialogTitle) {

        driver = new EditTaskDialog_Driver_Impl();

        title = TextBox.create("Title")
                .setRequired(true)
                .setAutoValidation(true)
                .groupBy(fieldsGrouping)
                .setPlaceholder("Task headline")
                .floating()
                .setLeftAddon(Icons.ALL.label());

        description = TextArea.create("description")
                .setRequired(true)
                .setAutoValidation(true)
                .groupBy(fieldsGrouping)
                .setPlaceholder("Describe the task")
                .floating()
                .setLeftAddon(Icons.ALL.description())
                .autoSize()
                .setRows(2);

        dueDate = DateBox.create("Due date", new Date())
                .apply(element -> element.getDatePicker()
                .addDateDayClickHandler((date, dateTimeFormatInfo) -> dueDate.close()))
                .setRequired(true)
                .setAutoValidation(true)
                .groupBy(fieldsGrouping)
                .setHelperText("Should not be in the past.")
                .setPlaceholder("Pick a Due date")
                .floating()
                .setLeftAddon(Icons.ALL.event());

        priority = Select.<Priority>create("Priority")
                .appendChild(SelectOption.create(Priority.IMPORTANT, "Important"))
                .appendChild(SelectOption.create(Priority.NORMAL, "Normal"))
                .selectAt(1)
                .setRequired(true)
                .setAutoValidation(true)
                .groupBy(fieldsGrouping)
                .setLeftAddon(Icons.ALL.low_priority());

        project = Select.<Project>create("Project")
                .appendChild(SelectOption.create(TasksRepository.PROJECTS.get(Constants.DOMINO_UI), Constants.DOMINO_UI))
                .appendChild(SelectOption.create(TasksRepository.PROJECTS.get(Constants.GWT), Constants.GWT))
                .appendChild(SelectOption.create(TasksRepository.PROJECTS.get(Constants.NALU_MVP), Constants.NALU_MVP))
                .appendChild(SelectOption.create(TasksRepository.PROJECTS.get(Constants.MOVIES), Constants.MOVIES))
                .setRequired(true)
                .setAutoValidation(true)
                .groupBy(fieldsGrouping)
                .setLeftAddon(Icons.ALL.collections_bookmark())
                .selectAt(0);

        tags = TagsInput.<String>create("Tags")
                .floating()
                .setPlaceholder("Things related to this task")
                .setLeftAddon(Icons.ALL.bookmark());

        modalDialog = ModalDialog.create(dialogTitle)
                .setAutoClose(false)
                .styler(style -> style.add("task-modal"))
                .appendChild(Row.create()
                        .fullSpan(column -> column.appendChild(title)))
                .appendChild(Row.create()
                        .fullSpan(column -> column.appendChild(description)))
                .appendChild(Row.create()
                        .fullSpan(column -> column.appendChild(dueDate)))
                .appendChild(Row.create()
                        .fullSpan(column -> column.appendChild(priority)))
                .appendChild(Row.create()
                        .fullSpan(column -> column.appendChild(project)))
                .appendChild(Row.create()
                        .fullSpan(column -> column.appendChild(tags)))
                .appendChild(Row.create()
                        .fullSpan(column -> column.appendChild(h(5)
                                .textContent("ATTACHMENTS"))
                                .appendChild(FileUploadComponent.create(EditTaskDialog.this)
                                )))
                .appendFooterChild(Button.create(Icons.ALL.clear())
                        .linkify()
                        .setContent("CANCEL")
                        .styler(style -> style.setMinWidth("100px"))
                        .addClickListener(evt -> modalDialog.close()))
                .appendFooterChild(Button.createPrimary(Icons.ALL.save())
                        .setContent("SAVE")
                        .styler(style -> style.setMinWidth("100px"))
                        .addClickListener(evt -> onSave()));

    }

    private void onSave() {
        if (fieldsGrouping.validate().isValid()) {
            if (nonNull(onCreateHandler)) {
                onCreateHandler.accept(driver.flush());
                modalDialog.close();
            }
        }
    }

    public static EditTaskDialog create(String dialogTitle, Task task) {
        EditTaskDialog editTaskDialog = new EditTaskDialog(dialogTitle);
        editTaskDialog.edit(task);
        return editTaskDialog;
    }

    public static EditTaskDialog create(String dialogTitle) {
        EditTaskDialog editTaskDialog = new EditTaskDialog(dialogTitle);
        editTaskDialog.edit(new Task());
        editTaskDialog.fieldsGrouping.clearInvalid();
        return editTaskDialog;
    }

    public ModalDialog getModalDialog() {
        return modalDialog;
    }

    void edit(Task task) {
        driver.initialize(this);
        driver.edit(task);

        this.task = task;
    }

    @Override
    public Task getTask() {
        return this.task;
    }

    public EditTaskDialog onSave(Consumer<Task> onCreateHandler) {
        this.onCreateHandler = onCreateHandler;
        return this;
    }

}
