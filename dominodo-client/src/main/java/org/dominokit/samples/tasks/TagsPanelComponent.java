package org.dominokit.samples.tasks;

import static org.dominokit.domino.ui.style.DisplayCss.dui_flex;
import static org.dominokit.domino.ui.style.SpacingCss.dui_gap_1;
import static org.dominokit.domino.ui.utils.ElementsFactory.elements;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.chips.Chip;
import org.dominokit.domino.ui.elements.DivElement;
import org.dominokit.domino.ui.shaded.utils.BaseDominoElement;
import org.dominokit.domino.ui.style.ColorScheme;
import org.dominokit.samples.HasTaskUiHandlers;
import org.dominokit.samples.Task;

public class TagsPanelComponent extends BaseDominoElement<HTMLDivElement, TagsPanelComponent> {

    private final DivElement tagsContainer = elements.div();
    private DivElement element = elements.div();
    private Task task;
    private HasTaskUiHandlers taskUiHandlers;

    public static TagsPanelComponent create(Task task, HasTaskUiHandlers taskUiHandlers){
        return new TagsPanelComponent(task, taskUiHandlers);
    }

    public TagsPanelComponent(Task task, HasTaskUiHandlers taskUiHandlers) {
        this.task = task;
        this.taskUiHandlers = taskUiHandlers;

        element.appendChild(
            tagsContainer.addCss(dui_flex, dui_gap_1));
        init(this);
        update();
    }

    public void update(){
        ColorScheme projectColor = ColorScheme.valueOf(task.getProject().getColor());
        task.getTags()
            .forEach(tag -> tagsContainer
                .appendChild(Chip.create(tag).addCss(projectColor.color().getBackground())
                        .setTooltip("Click to search")
                        .addClickListener(evt -> taskUiHandlers.onTagSelected(tag))
                    .addOnRemoveListener((chip) -> task.getTags().remove(tag))
                    .element()
                )
            );
    }

    @Override
    public HTMLDivElement element() {
        return element.element();
    }
}
