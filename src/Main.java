import managers.FileBackedTasksManager;
import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("Пришло время практики!");
        TaskManager manager = Managers.getDefault();
        try{
            System.out.println("Были добавлены: ");
            System.out.println(manager.addTask(new Task("theme1", "description1")));
            manager.addEpic(new Epic("theme2", "description2"));
            manager.addSubtask(new Subtask("theme3", "description3", 1));
            Subtask subtask = manager.getSubtask(2);
            manager.setSubtask(new Subtask(subtask.getTheme(),
                    subtask.getDescription(), 2, Status.DONE, 1));
            manager.addSubtask(new Subtask("theme4", "description4", 1));
            System.out.println(manager.addTask(new Task("theme5", "description5")));
            manager.addEpic(new Epic("theme6", "description6"));
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
            ArrayList<Task> historyList = manager.getHistoryList();
            for (Task task : historyList) {
                System.out.println(task);
            }
            System.out.println("Список всех задач");
            if(manager.getTasks().isEmpty()) {
                System.out.println("Список задач пуст");
            } else {
                ArrayList<Task> tasks = manager.getTasks();
                for (Task i : tasks) {
                    System.out.println(i.toString());
                }
            }
            System.out.println("Список всех подзадач");
            if(manager.getSubtasks().isEmpty()) {
                System.out.println("Список подзадач пуст");
            } else {
                subtasks = manager.getSubtasks();
                for (Subtask i : subtasks) {
                    System.out.println(i.toString());
                }
            }
            System.out.println("Список всех эпиков");
            if(manager.getEpics().isEmpty()) {
                System.out.println("Список эпиков пуст");
            } else {
                epics = manager.getEpics();
                for (Epic i : epics) {
                    System.out.println(i.toString());
                }
            }
            System.out.println("Список всех подзадач по идентификатору эпика");
            if(manager.getSubtasksByEpicId(1).isEmpty()) {
                System.out.println("Список подзадач пуст");
            } else {
                subtasks = manager.getSubtasksByEpicId(1);
                for (Subtask i : subtasks) {
                    System.out.println(i.toString());
                }
            }
            System.out.println("Удаление задачи по идентификатору 4");
            manager.removeTask(manager.getTask(4));
            System.out.println("Существование задачи: " + manager.isTaskExist(4));
            System.out.println("Удаление эпика по идентификатору 5");
            manager.removeEpic(manager.getEpic(5));
            System.out.println("Существование эпика: " + manager.isEpicExist(5));
            System.out.println("Удаление подзадачи по идентификатору 3");
            manager.removeSubtask(manager.getSubtask(3));
            System.out.println("Существование подзадачи: " + manager.isSubtaskExist(3));
            Task newTask = manager.getTask(0);
            Subtask newSubtask =  manager.getSubtask(2);
            Epic newEpic = manager.getEpic(1);
            System.out.println("Информация задачи\n" + newTask + "\nбыла изменена на\n" +
                    manager.setTask(new Task("Тема1", "Описание1", newTask.getId(), Status.DONE)));
            System.out.println("Информация подзадачи\n" + manager.getSubtask(2) + "\nбыла изменена на\n" +
                    manager.setSubtask(new Subtask("Тема3", "Описание3", newSubtask.getId(),
                            Status.IN_PROGRESS, newSubtask.getEpicNum())));
            System.out.println("Информация эпика\n" + manager.getEpic(1) + "\nбыла изменена на\n" +
                    manager.setEpic(new Epic("Тема2", "Описание2", newEpic.getId(),
                            newEpic.getStatus(), newEpic.getSubtasksId())));
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
            historyList = manager.getHistoryList();
            for (Task i : historyList) {
                System.out.println(i);
            }
            System.out.println("Удаление всех задач, подзадач и эпиков");
            manager.deleteTasks();
            manager.deleteEpics();
            if(manager.getTasks().isEmpty()) {
                System.out.println("Список задач пуст");
            }
            if(manager.getEpics().isEmpty()) {
                System.out.println("Список эпиков пуст");
            }
            if(manager.getSubtasks().isEmpty()) {
                System.out.println("Список подзадач пуст");
            }
        } catch (Exception e) {
            System.out.println("Неправильный ввод команды.");
        }
    }
}
