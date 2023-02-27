package managers;

import tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> historyList;
    private final HashMap<Integer, Node<Task>> historyMap;
    private final CustomLinkedList<Task> taskCustomLinkedList;

    InMemoryHistoryManager() {
        historyList = new ArrayList<>();
        historyMap = new HashMap<>();
        taskCustomLinkedList = new CustomLinkedList<>();
    }

    @Override
    public void add(Task task) {
        taskCustomLinkedList.linkLast(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return taskCustomLinkedList.getTasks();
    }

    @Override
    public void remove(int id) {
        if (historyMap.containsKey(id)) {
            Node historyNode = historyMap.get(id);
            if (historyNode == taskCustomLinkedList.head) {
                taskCustomLinkedList.head = historyNode.next;
            } else if (historyNode == taskCustomLinkedList.tail) {
                taskCustomLinkedList.tail = historyNode.prev;
            }
            Node.removeNode(historyNode);
            historyMap.remove(id);
        }
    }

    public class CustomLinkedList<T> {
        private Node<tasks.Task> head = null;
        private Node<Task> tail = null;
        private Node<Task> prev = null;

        public void linkLast(Task value) {
            if (head == null) {
                head = new Node<>(null, value, null);
                tail = head;
                historyMap.put(value.getId(), head);
                return;
            }
            int id = value.getId();
            if (historyMap.containsKey(id)) {
                Node historyNode = historyMap.get(id);
                if (historyNode == head) {
                    head = historyNode.next;
                } else if (historyNode == tail) {
                    tail = historyNode.prev;
                }
                Node.removeNode(historyNode);
                historyMap.remove(id);
            }
            prev = tail;
            final Node<Task> newNode = new Node<>(prev, value, null);
            prev.next = newNode;
            tail = newNode;
            historyMap.put(id, newNode);
        }

        public ArrayList<Task> getTasks() {
            historyList.clear();
            if (head != null) {
                historyList.add(head.item);
                Node node = head.next;
                while (node != null) {
                    historyList.add((Task) node.item);
                    node = node.next;
                }
            }
            return historyList;
        }
    }
}

