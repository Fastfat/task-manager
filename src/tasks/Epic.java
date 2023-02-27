package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId;

    public Epic(String theme, String description) {
        super(theme, description);
        subtasksId = new ArrayList<>();
        type = Type.EPIC;
    }

    public Epic(String theme, String description, Integer id, Status status, ArrayList<Integer> subtasksId) {
        super(theme, description, id, status);
        this.subtasksId = subtasksId;
        type = Type.EPIC;
    }

    public ArrayList getSubtasksId() {
        return subtasksId;
    }

    public void addSubtaskId(int subtaskId) {
        subtasksId.add(subtaskId);
    }

    public void deleteSubtasksId() {
        subtasksId.clear();
        status = Status.NEW;
    }

    @Override
    public String toString() {
        return super.toString() + "\nsubtasksId: " + subtasksId;
    }

    public void removeSubtask(int id) {
        subtasksId.remove(id);
        if (subtasksId.isEmpty()) {
            status = Status.NEW;
        }
    }
}
