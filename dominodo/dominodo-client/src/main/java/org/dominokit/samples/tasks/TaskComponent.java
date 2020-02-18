package org.dominokit.samples.tasks;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import org.dominokit.domino.ui.Typography.Paragraph;
import org.dominokit.domino.ui.animations.Animation;
import org.dominokit.domino.ui.animations.Transition;
import org.dominokit.domino.ui.badges.Badge;
import org.dominokit.domino.ui.cards.Card;
import org.dominokit.domino.ui.cards.HeaderAction;
import org.dominokit.domino.ui.datepicker.DatePicker;
import org.dominokit.domino.ui.dialogs.ConfirmationDialog;
import org.dominokit.domino.ui.dropdown.DropDownMenu;
import org.dominokit.domino.ui.dropdown.DropDownPosition;
import org.dominokit.domino.ui.dropdown.DropdownAction;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.icons.Icons;
import org.dominokit.domino.ui.modals.BaseModal;
import org.dominokit.domino.ui.modals.ModalDialog;
import org.dominokit.domino.ui.notifications.Notification;
import org.dominokit.domino.ui.popover.Popover;
import org.dominokit.domino.ui.popover.PopupPosition;
import org.dominokit.domino.ui.style.Color;
import org.dominokit.domino.ui.style.ColorScheme;
import org.dominokit.domino.ui.style.Styles;
import org.dominokit.domino.ui.utils.BaseDominoElement;
import org.dominokit.domino.ui.utils.ScreenMedia;
import org.dominokit.samples.HasTaskUiHandlers;
import org.dominokit.samples.Priority;
import org.dominokit.samples.Task;
import org.dominokit.samples.attachments.AttachDialogComponent;
import org.dominokit.samples.attachments.AttachmentPanelComponent;
import org.dominokit.samples.attachments.FileUploadComponent;
import org.gwtproject.i18n.shared.cldr.DateTimeFormatInfo;
import org.gwtproject.i18n.shared.cldr.impl.DateTimeFormatInfo_factory;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.Date;

import static org.jboss.elemento.Elements.hr;
import static org.jboss.elemento.Elements.small;


public class TaskComponent extends BaseDominoElement<HTMLDivElement, TaskComponent> implements HasTask {

    private final HasTaskUiHandlers taskUiHandlers;
    private HtmlContentBuilder<HTMLElement> dueDateElement;
    private DateTimeFormatInfo dateTimeFormatInfo = DateTimeFormatInfo_factory.create();
    private Card card;
    private Task task;
    private DatePicker datePicker;
    private Popover datePickerPopup;
    private final ColorScheme projectColor;
    private AttachmentPanelComponent attachmentPanel;
    private final Icon importantIcon;

    public static TaskComponent create(Task task, HasTaskUiHandlers taskUiHandlers) {
        return new TaskComponent(task, taskUiHandlers);
    }

    public TaskComponent(Task task, HasTaskUiHandlers taskUiHandlers) {
        this.task = task;
        this.taskUiHandlers = taskUiHandlers;

        projectColor = ColorScheme.valueOf(task.getProject().getColor());
        dueDateElement = small()
                .textContent(formatDate(task.getDueDate()))
                .css(Styles.pull_right, "due-date");

        datePicker = DatePicker.create()
                .hideHeaderPanel()
                .setDateTimeFormatInfo(dateTimeFormatInfo)
                .setColorScheme(projectColor)
                .hideCloseButton()
                .hideClearButton()

                .addDateDayClickHandler((date, dateTimeFormatInfo1) -> datePickerPopup.close())
                .addDateSelectionHandler((date, dateTimeFormatInfo) -> {
                    dueDateElement.textContent(formatDate(date));
                    task.setDueDate(date);
                });


        Badge projectName = Badge.create(task.getProject().getName())
                .setBackground(projectColor.color());

        importantIcon = Icons.ALL.priority_high()
                .setColor(Color.RED)
                .setTooltip("This task is important")
                .styler(style1 -> style1.add(Styles.pull_right))
                .hide();

        attachmentPanel = AttachmentPanelComponent.create(task);

        card = Card.create(task.getTitle())
                .styler(style -> style.setProperty("border-left", "5px solid " + projectColor.color().getHex()))
                .appendDescriptionChild(projectName)
                .appendDescriptionChild(dueDateElement)
                .addHeaderAction(HeaderAction.create(Icons.ALL.more_vert())
                        .hideOn(ScreenMedia.MEDIUM_AND_UP)
                        .apply(element -> {
                            DropDownMenu menu = createDropDownMenu(element);
                            element.addClickListener(evt -> {
                                evt.stopPropagation();
                                menu.open();
                            });
                        })
                )

                .addHeaderAction(HeaderAction.create(Icons.ALL.priority_high()
                        .setTooltip("Toggle priority"))
                        .hideOn(ScreenMedia.SMALL_AND_DOWN)
                        .addClickListener(evt -> updatePriority()))

                .addHeaderAction(HeaderAction.create(Icons.ALL.delete()
                        .setTooltip("Delete task"))
                        .hideOn(ScreenMedia.SMALL_AND_DOWN)
                        .addClickListener(evt -> showConfirmationDialog()))

                .addHeaderAction(HeaderAction.create(Icons.ALL.edit()
                        .setTooltip("Edit task"))
                        .hideOn(ScreenMedia.SMALL_AND_DOWN)
                        .addClickListener(evt -> taskUiHandlers.onEditTask(task)))

                .addHeaderAction(HeaderAction.create(Icons.ALL.attachment()
                        .setTooltip("Attach files"))
                        .hideOn(ScreenMedia.SMALL_AND_DOWN)
                        .addClickListener(evt -> AttachDialogComponent.create(FileUploadComponent.create(TaskComponent.this), this::update).open()))

                .addHeaderAction(HeaderAction.create(Icons.ALL.event()
                        .setTooltip("Pick Due date")
                        .hideOn(ScreenMedia.SMALL_AND_DOWN)
                        .apply(dateIcon -> datePickerPopup = Popover.createPicker(dateIcon, datePicker)
                                .position(PopupPosition.TOP_DOWN)
                                .styler(style -> style.setMaxWidth("300px"))))
                        .addClickListener(evt -> datePickerPopup.show()))
                .addHeaderAction(getStatusAction(task))

                .appendChild(importantIcon)
                .appendChild(Paragraph.create(task.getDescription()))
                .appendChild(TagsPanelComponent.create(task, taskUiHandlers))
                .appendChild(hr())
                .appendChild(attachmentPanel);

        init(this);
        update();

    }

    private DropDownMenu createDropDownMenu(HeaderAction element) {
        return DropDownMenu.create(element)
                .setPosition(DropDownPosition.BOTTOM_LEFT)
                .addAction(DropdownAction.<String>create("Toggle priority")
                        .addSelectionHandler(value -> updatePriority()))
                .addAction(DropdownAction.<String>create("Delete")
                        .addSelectionHandler(value -> showConfirmationDialog()))
                .addAction(DropdownAction.<String>create("Edit")
                        .addSelectionHandler( value -> taskUiHandlers.onEditTask(task)))
                .addAction(DropdownAction.<String>create("Attach")
                        .addSelectionHandler(value -> AttachDialogComponent.create(FileUploadComponent.create(TaskComponent.this), this::update).open()))
                .addAction(DropdownAction.<String>create("Pick due date")
                        .addSelectionHandler(value -> {
                            ModalDialog modal = datePicker
                                    .createModal("Duew date")
                                    .appendChild(datePicker)
                                    .open();
                            datePicker.addDateDayClickHandler((date, dateTimeFormatInfo1) -> {
                                modal.close();
                            });
                        }))
                .addAction(DropdownAction.<String>create(task.isActive() ? "Resolve" : "Unresolve")
                        .addSelectionHandler(value -> {
                            if (task.isActive()) {
                                resolve();
                            } else {
                                unresolve();
                            }
                        }));
    }

    private void unresolve() {
        taskUiHandlers.onUnResolve(task);
        Notification.createWarning("Oops! now You have more work to do. " + task.getTitle())
                .show();
    }

    private void resolve() {
        taskUiHandlers.onResolved(task);
        Notification.createSuccess("Congrats! Task [" + task.getTitle() + "] have been resolved now.")
                .show();
    }

    private HeaderAction getStatusAction(Task task) {
        if (task.isActive()) {
            return HeaderAction.create(Icons.ALL.done_all()
                    .setColor(Color.GREEN)
                    .setTooltip("Resolve")
                    .hideOn(ScreenMedia.SMALL_AND_DOWN)
                    .addClickListener(evt -> resolve()));
        } else {
            return HeaderAction.create(Icons.ALL.replay()
                    .setColor(Color.ORANGE)
                    .setTooltip("Unresolve")
                    .hideOn(ScreenMedia.SMALL_AND_DOWN)
                    .addClickListener(evt -> unresolve()));
        }

    }

    private void updatePriority() {
        if (Priority.IMPORTANT.equals(task.getPriority())) {
            task.setPriority(Priority.NORMAL);
        } else {
            task.setPriority(Priority.IMPORTANT);
        }
        update();
        taskUiHandlers.onTaskPriorityChange(task);
    }

    private ConfirmationDialog showConfirmationDialog() {
        return ConfirmationDialog.create("Confirm delete")
                .appendChild(Paragraph.create("Are you sure you want to delete this task?"))
                .apply(element -> element.getFooterElement()
                        .styler(style -> style.setBackgroundColor("#f3f3f3"))
                )
                .onConfirm(dialog -> {
                    dialog.close();
                    Animation.create(TaskComponent.this)
                            .transition(Transition.LIGHT_SPEED_OUT)
                            .duration(500)
                            .callback(element1 -> {
                                taskUiHandlers.onTaskDelete(task);
                            }).animate();

                })
                .onReject(BaseModal::close)
                .open();
    }

    private void update() {
        importantIcon.toggleDisplay(Priority.IMPORTANT.equals(task.getPriority()));
        attachmentPanel.update();
    }

    private String formatDate(Date date) {
        return DatePicker.Formatter.getFormat(this.dateTimeFormatInfo.dateFormatFull(), this.dateTimeFormatInfo).format(date);
    }

    @Override
    public Task getTask() {
        return this.task;
    }

    @Override
    public HTMLDivElement element() {
        return card.element();
    }
}
