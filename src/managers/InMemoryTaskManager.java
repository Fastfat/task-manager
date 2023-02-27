package managers;

import tasks.Status;
import tasks.Task;
import tasks.Subtask;
import tasks.Epic;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    protected HashMap<Integer, Task> taskMap;
    protected HashMap<Integer, Subtask> subtaskMap;
    protected HashMap<Integer, Epic> epicMap;
    protected int id;
    private static final String REMOTE_TASK = "Задача была удалена";
    private static final String REMOTE_EPIC = "Эпик был удален";
    private static final String REMOTE_SUBTASK = "Подзадача была удалена";
    private static final String EMPTY_TASK_LIST = "Список задач пуст";
    private static final String EMPTY_SUBTASK_LIST = "Список подзадач пуст";
    private static final String EMPTY_EPIC_LIST = "Список эпиков пуст";

    public InMemoryTaskManager() {
        id = 0;
        taskMap = new HashMap<>();
        subtaskMap = new HashMap<>();
        epicMap = new HashMap<>();
    }

    @Override
    public ArrayList getTasks() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public ArrayList getSubtasks() {
        return new ArrayList<>(subtaskMap.values());
    }

    @Override
    public ArrayList getEpics() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epicMap.get(id);
        if(epic != null){
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Task getTask(int id) {
        Task task = taskMap.get(id);
        if(task != null){
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtaskMap.get(id);
        if(subtask != null){
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public ArrayList<Task> getHistoryList() {
        return historyManager.getHistory();
    }

    @Override
    public Task setTask(Task task) {
        if (taskMap.containsKey(task.getId())) {
            taskMap.put(task.getId(), task);
            return task;
        }
        return null;
    }

    @Override
    public Epic setEpic(Epic epic) {
        if (epicMap.containsKey(epic.getId())) {
            epicMap.put(epic.getId(), epic);
            return epic;
        }
        return null;
    }

    @Override
    public Subtask setSubtask(Subtask subtask) {
        if (subtaskMap.containsKey(subtask.getId())) {
            subtaskMap.put(subtask.getId(), subtask);
            statusCatcher(subtaskMap.get(subtask.getId()).getEpicNum());
            return subtask;
        }
        return null;
    }

    @Override
    public String removeById(int id) {
        if (taskMap.containsKey(id)) {
            return removeTask(taskMap.get(id));
        } else if (epicMap.containsKey(id)) {
            return removeEpic(epicMap.get(id));
        } else if (subtaskMap.containsKey(id)) {
            return removeSubtask(subtaskMap.get(id));
        }
        return null;
    }

    @Override
    public String removeTask(Task task) {
        if (taskMap.containsKey(task.getId())) {
            int id = task.getId();
            historyManager.remove(id);
            taskMap.remove(id);
            return REMOTE_TASK;
        }
        return null;
    }

    @Override
    public String removeEpic(Epic epic) {
        if (epicMap.containsKey(epic.getId())) {
            int id = epic.getId();
            ArrayList<Integer> subtasksId = epic.getSubtasksId();
            for (Integer i : subtasksId) {
                historyManager.remove(i);
                subtaskMap.remove(i);
            }
            historyManager.remove(id);
            epicMap.remove(id);
            return REMOTE_EPIC;
        }
        return null;
    }

    @Override
    public String removeSubtask(Subtask subtask) {
        if (subtaskMap.containsKey(subtask.getId())) {
            int id = subtask.getId();
            int epicNum = subtaskMap.get(id).getEpicNum();
            Epic epic = epicMap.get(epicNum);
            if (!epic.getSubtasksId().isEmpty()) {
                ArrayList<Integer> subtasksId = epic.getSubtasksId();
                for (int i = 0; i < subtasksId.size(); i++) {
                    if (id == subtasksId.get(i)) {
                        epic.removeSubtask(i);
                    }
                }
            }
            historyManager.remove(id);
            subtaskMap.remove(id);
            statusCatcher(epicNum);
            return REMOTE_SUBTASK;
        }
        return null;
    }

    @Override
    public String deleteTasks() {
        if (taskMap.isEmpty()) {
            return EMPTY_TASK_LIST;
        }
        for (Integer id : taskMap.keySet()) {
            historyManager.remove(id);
        }
        taskMap.clear();
        return EMPTY_TASK_LIST;
    }

    @Override
    public String deleteSubtasks() {
        if (subtaskMap.isEmpty()) {
            return EMPTY_SUBTASK_LIST;
        }
        for (Epic epic : epicMap.values()) {
            epic.deleteSubtasksId();
        }
        for (Integer id : subtaskMap.keySet()) {
            historyManager.remove(id);
        }
        subtaskMap.clear();
        return EMPTY_SUBTASK_LIST;
    }

    @Override
    public String deleteEpics() {
        if (epicMap.isEmpty()) {
            return EMPTY_EPIC_LIST;
        }
        for (Integer id : epicMap.keySet()) {
            historyManager.remove(id);
        }
        for (Integer id : subtaskMap.keySet()) {
            historyManager.remove(id);
        }
        epicMap.clear();
        subtaskMap.clear();
        return EMPTY_EPIC_LIST;
    }

    @Override
    public ArrayList getSubtasksByEpicId(int id) {
        if (!epicMap.containsKey(id)) {
            return null;
        }
        ArrayList<Integer> subtasksId = epicMap.get(id).getSubtasksId();
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (Integer i : subtasksId) {
            historyManager.add(subtaskMap.get(i));
            subtasks.add(subtaskMap.get(i));
        }
        return subtasks;
    }

    @Override
    public boolean isTaskExist(int taskNum) {
        return taskMap.containsKey(taskNum);
    }

    @Override
    public boolean isSubtaskExist(int subtaskNum) {
        return subtaskMap.containsKey(subtaskNum);
    }

    @Override
    public boolean isEpicExist(int epicNum) {
        return epicMap.containsKey(epicNum);
    }

    @Override
    public Task addTask(Task task) {
        task.setId(id);
        taskMap.put(id, task);
        id++;
        return task;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        Epic epic = epicMap.get(subtask.getEpicNum());
        if (epic != null) {
            subtask.setId(id);
            subtaskMap.put(id, subtask);
            id++;
            epicMap.get(subtask.getEpicNum()).addSubtaskId(subtask.getId());
            statusCatcher(subtask.getEpicNum());
        }
        return subtask;
    }

    @Override
    public Epic addEpic(Epic epic) {
        epic.setId(id);
        epicMap.put(id, epic);
        id++;
        return epic;
    }

    private void statusCatcher(int epicNum) {
        int newCount = 0;
        int doneCount = 0;
        Epic epic = epicMap.get(epicNum);
        ArrayList<Integer> subtasks = epic.getSubtasksId();
        if (!subtasks.isEmpty()) {
            for (int i : subtasks) {
                Subtask subtask = subtaskMap.get(i);
                if (subtask.getStatus().equals(Status.NEW)) {
                    newCount++;
                } else if (subtask.getStatus().equals(Status.DONE)) {
                    doneCount++;
                } else {
                    epic.setStatus(Status.IN_PROGRESS);
                    return;
                }
            }
            if (newCount == subtasks.size()) {
                epic.setStatus(Status.NEW);
            } else if (doneCount == subtasks.size()) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        } else {
            epic.setStatus(Status.NEW);
        }
    }
}
