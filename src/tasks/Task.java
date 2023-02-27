package tasks;

import java.util.Locale;

public class Task {
    protected Type type;
    protected Status status;
    protected String theme;
    protected String description;
    protected Integer id;

    public Task(String theme, String description) {
        this.theme = theme;
        this.description = description;
        this.status = Status.NEW;
        type = Type.TASK;
    }

    public Task(String theme, String description, Integer id, Status status) {
        this.theme = theme;
        this.description = description;
        this.id = id;
        this.status = status;
        type = Type.TASK;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public String getTheme() {
        return theme;
    }

    public String getDescription() {
        return description;
    }

    public String saveString() {
        return String.format("%d,%s,%s,%s,%s", id, type, theme, status, description);
    }

    @Override
    public String toString() {
        return "type: " + type + "\ntheme: " + theme + "\ndescription: " + description + "\nstatus: "
            + status.toString().toLowerCase(Locale.ROOT) + "\nid: " + id;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
