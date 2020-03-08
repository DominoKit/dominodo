package org.dominokit.samples.menu;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.icons.Icons;
import org.dominokit.domino.ui.style.Color;
import org.dominokit.domino.ui.tree.Tree;
import org.dominokit.domino.ui.tree.TreeItem;
import org.dominokit.domino.ui.utils.BaseDominoElement;
import org.dominokit.samples.Constants;
import org.dominokit.samples.HasMenuUiHandlers;
import org.dominokit.samples.Priority;

public class MenuComponent extends BaseDominoElement<HTMLDivElement, MenuComponent> {
    private final Tree<String> menu = Tree.create("Todo");

    public static MenuComponent create(HasMenuUiHandlers menuUiHandlers, String puppa) {
        return create(menuUiHandlers);
    }

    public static MenuComponent create(HasMenuUiHandlers menuUiHandlers) {
        return new MenuComponent(menuUiHandlers);
    }

    public MenuComponent(HasMenuUiHandlers menuUiHandlers) {

        menu
                .apply(element -> element.getRoot().style().add("menu-flow"))
                .appendChild(TreeItem.create("All Tasks", Icons.ALL.inbox())
                .addClickListener(evt -> menuUiHandlers.onAllSelected()))

                .appendChild(TreeItem.create("Today's tasks", Icons.ALL.event())
                        .addClickListener(evt -> menuUiHandlers.onTodaySelected()))

                .appendChild(TreeItem.create("Next week", Icons.ALL.date_range())
                        .addClickListener(evt -> menuUiHandlers.onNextWeekSelected()))

                .addSeparator()

                .appendChild(TreeItem.create("Important", Icons.ALL.priority_high().setColor(Color.RED))
                        .addClickListener(evt -> menuUiHandlers.onPrioritySelected(Priority.IMPORTANT)))

                .appendChild(TreeItem.create("Normal", Icons.ALL.low_priority().setColor(Color.TEAL))
                        .addClickListener(evt -> menuUiHandlers.onPrioritySelected(Priority.NORMAL)))

                .addSeparator()

                .appendChild(TreeItem.create(Constants.GWT, Icons.ALL.inbox().setColor(Color.PINK))
                        .addClickListener(evt -> menuUiHandlers.onProjectSelected(Constants.GWT)))

                .appendChild(TreeItem.create("Domino UI", Icons.ALL.inbox().setColor(Color.INDIGO))
                        .addClickListener(evt -> menuUiHandlers.onProjectSelected(Constants.DOMINO_UI)))

                .appendChild(TreeItem.create("Nalu project", Icons.ALL.inbox().setColor(Color.BLUE))
                        .addClickListener(evt -> menuUiHandlers.onProjectSelected(Constants.NALU_MVP)))

                .appendChild(TreeItem.create("Movies", Icons.ALL.inbox().setColor(Color.BROWN))
                        .addClickListener(evt -> menuUiHandlers.onProjectSelected(Constants.MOVIES)))
        .addSeparator()
                .appendChild(TreeItem.create("Resolved", Icons.ALL.done_all().setColor(Color.GREEN))
                        .addClickListener(evt -> menuUiHandlers.onListResolved()));
    }

    @Override
    public HTMLDivElement element() {
        return menu.element();
    }
}
