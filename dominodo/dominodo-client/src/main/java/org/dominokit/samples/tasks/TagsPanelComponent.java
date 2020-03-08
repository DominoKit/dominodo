package org.dominokit.samples.tasks;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.chips.Chip;
import org.dominokit.domino.ui.grid.flex.FlexItem;
import org.dominokit.domino.ui.grid.flex.FlexLayout;
import org.dominokit.domino.ui.style.ColorScheme;
import org.dominokit.domino.ui.utils.BaseDominoElement;
import org.dominokit.samples.HasTaskUiHandlers;
import org.dominokit.samples.Task;

import static org.jboss.elemento.Elements.div;

public class TagsPanelComponent extends BaseDominoElement<HTMLDivElement, TagsPanelComponent> {

    private final FlexItem tagsContainer = FlexItem.create();
    private HTMLDivElement element = div().element();
    private Task task;
    private HasTaskUiHandlers taskUiHandlers;

    public static TagsPanelComponent create(Task task, HasTaskUiHandlers taskUiHandlers){
        return new TagsPanelComponent(task, taskUiHandlers);
    }

    public TagsPanelComponent(Task task, HasTaskUiHandlers taskUiHandlers) {
        this.task = task;
        this.taskUiHandlers = taskUiHandlers;

        element.appendChild(FlexLayout.create()
                .appendChild(tagsContainer).element());
        init(this);
        update();
    }

    public void update(){
        ColorScheme projectColor = ColorScheme.valueOf(task.getProject().getColor());
        task.getTags()
                .forEach(tag -> tagsContainer.appendChild(Chip.create(tag)
                        .setTooltip("Click to search")
                        .addClickListener(evt -> taskUiHandlers.onTagSelected(tag))
                        .addRemoveHandler(() -> task.getTags().remove(tag))
                        .setColorScheme(projectColor)));
    }

    @Override
    public HTMLDivElement element() {
        return element;
    }
}
