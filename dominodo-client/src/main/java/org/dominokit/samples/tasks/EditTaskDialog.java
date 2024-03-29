package org.dominokit.samples.tasks;

import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.datepicker.DateBox;
import org.dominokit.domino.ui.forms.FieldsGrouping;
import org.dominokit.domino.ui.forms.Select;
import org.dominokit.domino.ui.forms.SelectOption;
import org.dominokit.domino.ui.forms.TextArea;
import org.dominokit.domino.ui.forms.TextBox;
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
                .addLeftAddOn(Icons.ALL.label())
                .setFixErrorsPosition(true);

        description = TextArea.create("description")
                .setRequired(true)
                .setAutoValidation(true)
                .groupBy(fieldsGrouping)
                .setPlaceholder("Describe the task")
                .floating()
                .addLeftAddOn(Icons.ALL.description())
                .autoSize()
                .setRows(2)
                .setFixErrorsPosition(true);

        dueDate = DateBox.create("Due date", new Date())
                .apply(element -> element.getDatePicker()
                .addDateDayClickHandler((date, dateTimeFormatInfo) -> dueDate.close()))
                .setRequired(true)
                .setAutoValidation(true)
                .groupBy(fieldsGrouping)
                .setHelperText("Should not be in the past.")
                .setPlaceholder("Pick a Due date")
                .floating()
                .addLeftAddOn(Icons.ALL.event())
                .setFixErrorsPosition(true);

        priority = Select.<Priority>create("Priority")
                .appendChild(SelectOption.create(Priority.IMPORTANT, "Important"))
                .appendChild(SelectOption.create(Priority.NORMAL, "Normal"))
                .selectAt(1)
                .setRequired(true)
                .setAutoValidation(true)
                .groupBy(fieldsGrouping)
                .addLeftAddOn(Icons.ALL.low_priority())
                .setFixErrorsPosition(true);

        project = Select.<Project>create("Project")
                .appendChild(SelectOption.create(TasksRepository.PROJECTS.get(Constants.DOMINO_UI), Constants.DOMINO_UI))
                .appendChild(SelectOption.create(TasksRepository.PROJECTS.get(Constants.GWT), Constants.GWT))
                .appendChild(SelectOption.create(TasksRepository.PROJECTS.get(Constants.NALU_MVP), Constants.NALU_MVP))
                .appendChild(SelectOption.create(TasksRepository.PROJECTS.get(Constants.MOVIES), Constants.MOVIES))
                .setRequired(true)
                .setAutoValidation(true)
                .groupBy(fieldsGrouping)
                .addLeftAddOn(Icons.ALL.collections_bookmark())
                .selectAt(0)
                .setFixErrorsPosition(true);

        tags = TagsInput.<String>create("Tags")
                .floating()
                .setPlaceholder("Things related to this task")
                .addLeftAddOn(Icons.ALL.bookmark())
                .setFixErrorsPosition(true);

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
