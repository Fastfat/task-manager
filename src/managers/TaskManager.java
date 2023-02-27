package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import java.util.ArrayList;

public interface TaskManager {
    ArrayList getTasks();

    ArrayList getSubtasks();

    ArrayList getEpics();

    ArrayList getHistoryList();

    Epic getEpic(int id);

    Task getTask(int id);

    Subtask getSubtask(int id);

    Task setTask(Task task);

    Epic setEpic(Epic epic);

    Subtask setSubtask(Subtask subtask);

    String removeById(int id);

    String removeTask(Task task);

    String removeEpic(Epic epic);

    String removeSubtask(Subtask subtask);

    String deleteTasks();

    String deleteSubtasks();

    String deleteEpics();

    ArrayList getSubtasksByEpicId(int id);

    boolean isTaskExist(int taskNum);

    boolean isSubtaskExist(int subtaskNum);

    boolean isEpicExist(int epicNum);

    Task addTask(Task task);

    Subtask addSubtask(Subtask subtask);

    Epic addEpic(Epic epic);
}
