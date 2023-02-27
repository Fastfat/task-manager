package tasks;

public class Subtask extends Task{
    private Integer epicNum;

    public Subtask(String theme, String description, int epicNum) {
        super(theme, description);
        this.epicNum = epicNum;
        type = Type.SUBTASK;
    }

    public Subtask(String theme, String description, Integer id, Status status, int epicNum) {
        super(theme, description, id, status);
        this.epicNum = epicNum;
        type = Type.SUBTASK;
    }

    public int getEpicNum() {
        return epicNum;
    }

    @Override
    public String saveString() {
        return super.saveString() + "," + epicNum;
    }

    @Override
    public String toString() {
        return super.toString() + "\nepicId: " + epicNum;
    }
}
