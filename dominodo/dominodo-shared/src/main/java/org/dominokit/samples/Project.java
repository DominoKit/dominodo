package org.dominokit.samples;

import java.util.Objects;

public class Project {

    private String name;
    private String icon;
    private String color;

    public Project() {
    }

    public Project(String name, String icon, String color) {
        this.name = name;
        this.icon = icon;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(name, project.name) &&
                Objects.equals(icon, project.icon) &&
                Objects.equals(color, project.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, icon, color);
    }
}