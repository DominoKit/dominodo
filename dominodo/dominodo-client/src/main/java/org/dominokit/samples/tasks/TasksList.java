package org.dominokit.samples.tasks;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.animations.Animation;
import org.dominokit.domino.ui.animations.Transition;
import org.dominokit.domino.ui.grid.Column;
import org.dominokit.domino.ui.grid.Row;
import org.dominokit.domino.ui.header.BlockHeader;
import org.dominokit.domino.ui.icons.Icons;
import org.dominokit.domino.ui.layout.EmptyState;
import org.dominokit.domino.ui.style.Color;
import org.dominokit.domino.ui.utils.BaseDominoElement;
import org.dominokit.samples.HasTaskUiHandlers;
import org.dominokit.samples.Task;

import java.util.List;

import static org.jboss.elemento.Elements.div;

public class TasksList extends BaseDominoElement<HTMLDivElement, TasksList> {

    private int DURATION = 400;
    private final Column column = Column.span8()
            .offset2();
    private HTMLDivElement element = div().element();
    private int delay = 100;
    private String title;
    private final List<Task> tasks;
    private final HasTaskUiHandlers hasTaskUiHandlers;

    public TasksList(String title, List<Task> tasks, HasTaskUiHandlers hasTaskUiHandlers) {
        this.title = title;
        this.tasks = tasks;
        this.hasTaskUiHandlers = hasTaskUiHandlers;

        element.appendChild(Row.create()
                .appendChild(column).element());
    }

    public static TasksList create(String title, List<Task> tasks, HasTaskUiHandlers hasTaskUiHandlers){
        return new TasksList(title,tasks,hasTaskUiHandlers);
    }

    public TasksList update(boolean animate) {

        column.apply(element -> {
            element.appendChild(BlockHeader.create(title));
            if (tasks.isEmpty()) {
                element.appendChild(EmptyState.create(Icons.ALL.event_available())
                        .setIconColor(Color.GREY_LIGHTEN_1)
                        .setTitle("No tasks found")
                        .setDescription("If you are a developer then something wrong, you must have something to do.!")
                        .styler(style -> style.setMarginTop("10%")));
            } else {
                tasks.forEach(task -> {
                    TaskComponent taskComponent = TaskComponent.create(task, hasTaskUiHandlers);
                    if(animate) {
                        taskComponent.hide();
                        element.appendChild(taskComponent);
                        Animation.create(taskComponent)
                                .delay(delay)
                                .beforeStart(component -> taskComponent.show())
                                .duration(DURATION)
                                .transition(Transition.SLIDE_IN_UP)
                                .animate();

                        delay = delay + DURATION;
                        DURATION = 200;
                    }else{
                        element.appendChild(taskComponent);
                    }

                });
            }
        });

        return this;
    }

    @Override
    public HTMLDivElement element() {
        return element;
    }
}
