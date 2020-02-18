package org.dominokit.samples.attachments;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.badges.Badge;
import org.dominokit.domino.ui.chips.Chip;
import org.dominokit.domino.ui.grid.flex.FlexItem;
import org.dominokit.domino.ui.grid.flex.FlexLayout;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.icons.Icons;
import org.dominokit.domino.ui.style.Color;
import org.dominokit.domino.ui.style.ColorScheme;
import org.dominokit.domino.ui.utils.BaseDominoElement;
import org.dominokit.domino.ui.utils.DominoElement;
import org.dominokit.samples.Task;

import static java.util.Objects.nonNull;
import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.h;

public class AttachmentPanelComponent extends BaseDominoElement<HTMLDivElement, AttachmentPanelComponent> {

    private final FlexItem attachmentsItemsFlexItem;
    private final Task task;
    private final Badge badge;
    private HTMLDivElement element = div().element();
    private DominoElement<HTMLDivElement> listContainer = DominoElement.of(div());
    private Icon attachmentsCountIcon;
    private final ColorScheme projectColor;

    public static AttachmentPanelComponent create(Task task) {
        return new AttachmentPanelComponent(task);
    }

    public AttachmentPanelComponent(Task task) {
        init(this);
        this.task = task;

        projectColor = ColorScheme.valueOf(task.getProject().getColor());

        attachmentsCountIcon = Icons.ALL.arrow_drop_down()
                .setToggleIcon(Icons.ALL.arrow_drop_up())
                .styler(style -> style.pullRight()
                        .setLineHeight("10px"))
                .hide();

        badge = Badge.create("0 Files");
        attachmentsItemsFlexItem = FlexItem.create();
        appendChild(h(5).textContent("ATTACHMENTS")
                .add(badge
                        .addClickListener(evt -> {
                            if (task.getAttachments().size() > 0) {
                                listContainer.toggleDisplay();
                                attachmentsCountIcon.toggleIcon();
                            }
                        })
                        .styler(style -> style.pullRight()
                                .setCursor("pointer"))
                        .setBackground(projectColor.color())
                        .appendChild(attachmentsCountIcon)
                ))
                .appendChild(listContainer
                        .styler(style -> style.add("attachments-panel", projectColor.lighten_5().getBackground()))
                        .hide()
                        .appendChild(FlexLayout.create()
                                .appendChild(attachmentsItemsFlexItem)));

        update();
    }

    public void update() {
        if (nonNull(task.getAttachments())) {
            attachmentsItemsFlexItem.clearElement();
            attachmentsCountIcon.toggleDisplay(task.getAttachments().size() > 0);
            task.getAttachments()
                    .forEach(attachment -> attachmentsItemsFlexItem.appendChild(Chip.create(attachment)
                            .setRemovable(true)
                            .setColor(Color.BLUE_GREY)
                            .setLeftIcon(Icons.ALL.file_download())
                            .addRemoveHandler(() -> {
                                task.getAttachments().remove(attachment);
                                badge.setText(task.getAttachments().size() + " Files");
                                attachmentsCountIcon.toggleDisplay(task.getAttachments().size() > 0);
                                listContainer.toggleDisplay(task.getAttachments().size() > 0);
                            }))
                    );

            badge.setText(task.getAttachments().size() + " Files");
        }
    }

    @Override
    public HTMLDivElement element() {
        return element;
    }
}
