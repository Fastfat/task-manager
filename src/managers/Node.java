package managers;

public class Node<T> {
    T item;
    Node<T> next;
    Node<T> prev;

    Node(Node<T> prev, T element, Node<T> next) {
        this.item = element;
        this.next = next;
        this.prev = prev;
    }

    public static void removeNode(Node node) {
        if (node.getPrev() == null && node.getNext() == null) {
            return;
        } else if (node.getPrev() == null) {
            node.getNext().setPrev(null);
        } else if (node.getNext() == null) {
            node.getPrev().setNext(null);
        } else {
            node.getPrev().setNext(node.getNext());
            node.getNext().setPrev(node.getPrev());
        }
    }

    private Node getPrev() {
        return this.prev;
    }

    private Node getNext() {
        return this.next;
    }

    private void setPrev(Node node) {
        this.prev = node;
    }

    private void setNext(Node node) {
        this.next = node;
    }
}
