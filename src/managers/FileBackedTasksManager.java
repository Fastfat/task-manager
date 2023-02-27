package managers;

import exceptions.ManagerSaveException;
import tasks.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File dir;
    private static final String FAILED_SAVE_HISTORY = "Ошибка при попытке сохранения менеджера истории запросов";
    private static final String FAILED_SAVE_UPDATED_TASK = "Ошибка при попытке сохранения обновленной задачи";
    private static final String FAILED_SAVE_UPDATED_EPIC = "Ошибка при попытке сохранения обновленного эпика";
    private static final String FAILED_SAVE_UPDATED_SUBTASK = "Ошибка при попытке сохранения обновленной подзадачи";
    private static final String FAILED_SAVE_DELETE_TASK = "Ошибка при попытке сохранения удаления задачи";
    private static final String FAILED_SAVE_DELETE_EPIC = "Ошибка при попытке сохранения удаления эпика";
    private static final String FAILED_SAVE_DELETE_SUBTASK = "Ошибка при попытке сохранения удаления подзадачи";
    private static final String FAILED_SAVE_DELETE_ALL_TASKS = "Ошибка при попытке сохранения удаления всех задач";
    private static final String FAILED_SAVE_DELETE_ALL_EPICS = "Ошибка при попытке сохранения удаления всех эпиков";
    private static final String FAILED_SAVE_DELETE_ALL_SUBTASKS = "Ошибка при попытке сохранения удаления " +
            "всех подзадач";
    private static final String FAILED_SAVE_NEW_TASK = "Ошибка при попытке сохранения новой задачи";
    private static final String FAILED_SAVE_NEW_EPIC = "Ошибка при попытке сохранения нового эпика";
    private static final String FAILED_SAVE_NEW_SUBTASK = "Ошибка при попытке сохранения новой подзадачи";
    private static final String REMOTE_TASK = "Задача была удалена";
    private static final String REMOTE_EPIC = "Эпик был удален";
    private static final String REMOTE_SUBTASK = "Подзадача была удалена";
    private static final String EMPTY_TASK_LIST = "Список задач пуст";
    private static final String EMPTY_SUBTASK_LIST = "Список подзадач пуст";
    private static final String EMPTY_EPIC_LIST = "Список эпиков пуст";
    private static final String UNSUCCSESSFUL_RECOVERY = "Восстановление не было произведено успешно!";
    private static final String SUCCSESSFUL_RECOVERY = "Восстановление было произведено успешно!";
    private static final String FAILED_SAVE = "Осуществить сохранение не удалось!";
    private static final String FAILED_SAVE2 = "Осуществить сохранение не удалось, но мы пытались";
    private static final String UNREADABLE_FILE = "Невозможно прочитать файл. Возможно, " +
            "файл не находится в нужной директории.";

    public FileBackedTasksManager(File file) {
        super();
        dir = file;
    }

    public static void main(String[] args) {
        FileBackedTasksManager manager = (FileBackedTasksManager) Managers.getDefault();
        System.out.println("Были добавлены: ");
        System.out.println(manager.addTask(new Task("theme1", "description1")));
        manager.addEpic(new Epic("theme2", "description2"));
        manager.addSubtask(new Subtask("theme3", "description3", 1));
        Subtask subtask = manager.getSubtask(2);
        manager.setSubtask(new Subtask(subtask.getTheme(),
                subtask.getDescription(), 2, Status.DONE, 1));
        manager.addSubtask(new Subtask("theme4", "description4", 1));
        ArrayList<Epic> epics = manager.getEpics();
        for (Epic i : epics) {
            System.out.println(i);
        }
        ArrayList<Subtask> subtasks = manager.getSubtasks();
        for (Subtask i : subtasks) {
            System.out.println(i);
        }
        System.out.println("\nБыли просмотрены: ");
        System.out.println(manager.getEpic(1));
        System.out.println(manager.getSubtask(2));
        System.out.println(manager.getTask(0));
        System.out.println("\nИстория просмотров");
        for (Task task : manager.getHistoryList()) {
            System.out.println(task);
        }
        System.out.println("\nЗагрузка из файла");
        manager = FileBackedTasksManager.loadFromFile(new File("resources\\loadingFile.csv"));
        System.out.println("\nБыли загружены: ");
        ArrayList<Task> tasks = manager.getTasks();
        for (Task i : tasks) {
            System.out.println(i);
        }
        epics = manager.getEpics();
        for (Epic i : epics) {
            System.out.println(i);
        }
        subtasks = manager.getSubtasks();
        for (Subtask i : subtasks) {
            System.out.println(i);
        }
        System.out.println("\nЗагруженная история просмотров: ");
        for (Task task : manager.getHistoryList()) {
            System.out.println(task);
        }
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.getMessage();
            System.out.println(FAILED_SAVE_HISTORY);
        } finally {
            return epic;
        }
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.getMessage();
            System.out.println(FAILED_SAVE_HISTORY);
        } finally {
            return task;
        }
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.getMessage();
            System.out.println(FAILED_SAVE_HISTORY);
        } finally {
            return subtask;
        }
    }

    @Override
    public Task setTask(Task task) {
        super.setTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.getMessage();
            System.out.println(FAILED_SAVE_UPDATED_TASK);
        } finally {
            return task;
        }
    }

    @Override
    public Epic setEpic(Epic epic) {
        super.setEpic(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.getMessage();
            System.out.println(FAILED_SAVE_UPDATED_EPIC);
        } finally {
            return epic;
        }
    }

    @Override
    public Subtask setSubtask(Subtask subtask) {
        super.setSubtask(subtask);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.getMessage();
            System.out.println(FAILED_SAVE_UPDATED_SUBTASK);
        } finally {
            return subtask;
        }
    }

    @Override
    public String removeTask(Task task) {
        super.removeTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.getMessage();
            System.out.println(FAILED_SAVE_DELETE_TASK);
        } finally {
            return REMOTE_TASK;
        }
    }

    @Override
    public String removeEpic(Epic epic) {
        super.removeEpic(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.getMessage();
            System.out.println(FAILED_SAVE_DELETE_EPIC);
        } finally {
            return REMOTE_EPIC;
        }
    }

    @Override
    public String removeSubtask(Subtask subtask) {
        super.removeSubtask(subtask);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.getMessage();
            System.out.println(FAILED_SAVE_DELETE_SUBTASK);
        } finally {
            return REMOTE_SUBTASK;
        }
    }

    @Override
    public String deleteTasks() {
        super.deleteTasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            e.getMessage();
            System.out.println(FAILED_SAVE_DELETE_ALL_TASKS);
        } finally {
            return EMPTY_TASK_LIST;
        }
    }

    @Override
    public String deleteSubtasks() {
        super.deleteSubtasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            e.getMessage();
            System.out.println(FAILED_SAVE_DELETE_ALL_SUBTASKS);
        } finally {
            return EMPTY_SUBTASK_LIST;
        }
    }

    @Override
    public String deleteEpics() {
        super.deleteEpics();
        try {
            save();
        } catch (ManagerSaveException e) {
            e.getMessage();
            System.out.println(FAILED_SAVE_DELETE_ALL_EPICS);
        } finally {
            return EMPTY_EPIC_LIST;
        }
    }

    @Override
    public Task addTask(Task task) {
        super.addTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.getMessage();
            System.out.println(FAILED_SAVE_NEW_TASK);
        } finally {
            return task;
        }
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.getMessage();
            System.out.println(FAILED_SAVE_NEW_SUBTASK);
        } finally {
            return subtask;
        }
    }

    @Override
    public Epic addEpic(Epic epic) {
        super.addEpic(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.getMessage();
            System.out.println(FAILED_SAVE_NEW_EPIC);
        } finally {
            return epic;
        }
    }

    public String toString(Task task) { // перевод задачи в строку
        return task.saveString();
    }

    public Task toTask(String value) { // создает задачу из строки
        if (!value.isEmpty()) {
            String[] mas = value.split(",");
            if (Type.valueOf(mas[1]).equals(Type.TASK)) {
                //String theme, String description, Integer id, Status status
                Task task = new Task(mas[2], mas[4], Integer.parseInt(mas[0]), Status.valueOf(mas[3]));
                taskMap.put(task.getId(), task);
                if (id <= task.getId()) {
                    id = task.getId() + 1;
                }
                return task;
            } else if (Type.valueOf(mas[1]).equals(Type.SUBTASK)) {
                //String theme, String description, Integer id, Status status, int epicNum
                Subtask subtask = new Subtask(mas[2], mas[4], Integer.parseInt(mas[0]), Status.valueOf(mas[3]),
                        Integer.parseInt(mas[5]));
                subtaskMap.put(subtask.getId(), subtask);
                if (id <= subtask.getId()) {
                    id = subtask.getId() + 1;
                }
                return subtask;
            }
            //String theme, String description, Integer id, Status status, ArrayList<Integer> subtasksId
            Epic epic = new Epic(mas[2], mas[4], Integer.parseInt(mas[0]), Status.valueOf(mas[3]), new ArrayList<>());
            epicMap.put(epic.getId(), epic);
            if (id <= epic.getId()) {
                id = epic.getId() + 1;
            }
            return epic;
        }
        return null;
    }

    public static String toString(HistoryManager manager) { // сохраняет менеджер истории
        List<String> id = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            id.add(Integer.toString(task.getId()));
        }
        if (id.isEmpty()){
            return "  ";
        }
        return String.join(",", id);
    }

    public static List<Integer> fromString(String value) { // восстанавливает менеджер истории
        List<Integer> list = new ArrayList<>();
        if (value.length() == 1) {
            list = List.of(Integer.parseInt(value));
        } else if (!value.isBlank()){
            String[] mas = value.split(",");
            for (String id : mas) {
                list.add(Integer.parseInt(id));
            }
        }
        return list;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        fileBackedTasksManager.totalRecovery();
        return fileBackedTasksManager;
    }

    public void save() {
        try (Writer fileWriter = new FileWriter(dir)) {
            fileWriter.write("id,type,name,status,description,epic\n");
        } catch (IOException e) {
            throw new ManagerSaveException(FAILED_SAVE);
        }
        try (Writer fileWriter = new FileWriter(dir, true)) {
            for (int i = 0; i < id; i++) {
                if (isTaskExist(i)) {
                    fileWriter.write(toString(taskMap.get(i)) + "\n");
                } else if (isSubtaskExist(i)) {
                    fileWriter.write(toString(subtaskMap.get(i)) + "\n");
                } else if (isEpicExist(i)) {
                    fileWriter.write(toString(epicMap.get(i)) + "\n");
                }
            }
            String stringId = toString(historyManager);
            fileWriter.write("\n" + stringId + "\n");
        } catch (IOException e) {
            throw new ManagerSaveException(FAILED_SAVE2);
        }
    }

    private String readFileContentsOrNull(String path)
    {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            System.out.println(UNREADABLE_FILE);
            return null;
        }
    }

    private void totalRecovery() {
        String result = readFileContentsOrNull(dir.getPath());
        if (result.equals(null)) {
            System.out.println(UNSUCCSESSFUL_RECOVERY);
            return;
        }
        List<String> allFutureTask = List.of(result.split("\\n"));
        List<Integer> historyTask = new ArrayList<>();
        int size = allFutureTask.size();
        for (int i = 1; i < allFutureTask.size(); i++) {
            if (!allFutureTask.get(i).isEmpty() && i != allFutureTask.size() - 1) {
                toTask(allFutureTask.get(i));
            } else if (i == size - 1) {
                historyTask = fromString(allFutureTask.get(i));
            }
        }
        if (!subtaskMap.isEmpty()) {
            for (int id : subtaskMap.keySet()) {
                int epicId = subtaskMap.get(id).getEpicNum();
                if (isEpicExist(epicId)) {
                    epicMap.get(epicId).addSubtaskId(id);
                }
            }
        }
        for (int id : historyTask) {
            if (isTaskExist(id)) {
                historyManager.add(taskMap.get(id));
            } else if (isSubtaskExist(id)) {
                historyManager.add(subtaskMap.get(id));
            } else if (isEpicExist(id)) {
                historyManager.add(epicMap.get(id));
            }
        }
        System.out.println(SUCCSESSFUL_RECOVERY);
    }
}
