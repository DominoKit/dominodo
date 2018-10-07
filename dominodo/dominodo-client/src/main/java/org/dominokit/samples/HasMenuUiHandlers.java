package org.dominokit.samples;

public interface HasMenuUiHandlers {
    void onAllSelected();
    void onTodaySelected();
    void onNextWeekSelected();
    void onPrioritySelected(Priority priority);
    void onProjectSelected(String projectName);

    void onListResolved();
}
