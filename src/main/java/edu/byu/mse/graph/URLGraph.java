package edu.byu.mse.graph;

import edu.byu.mse.exception.EntityImportException;
import java.util.*;

public class URLGraph implements GraphStructure<Node> {

    private Node head;
    private String[] headers;

    private final int PRIMARY_INDEX = 0;

    public URLGraph() { }

    public URLGraph(Node head) {
        this.head = head;
    }

    public Node getHead() { return head; }

    public void setHeaders(String[] arr) { this.headers = arr; }

    @Override
    public void importObject(String[] arr) throws EntityImportException {

        if(headers == null)
            throw new EntityImportException("Headers have not been set");

        if(head == null) {
            head = createNode(arr);
        } else {
            insertIntoTree(head, createNode(arr));
        }
    }

    @Override
    public List<Node> readObjects() {
        List<Node> urls = new ArrayList<>();

        if(head == null)
            return urls;

        Queue queue = new LinkedList();
        queue.add(head);

        while(!queue.isEmpty()) {
            Node node = (Node) queue.poll();

            if(node.getNodeData().getData().size() > 0) {
                urls.add(node);
                queue.addAll(node.getChildren());
            } else {
                queue.addAll(node.getChildren());
            }
        }

        return urls;
    }

    private void insertIntoTree(Node parent, Node newNode) {

        String[] path = ((String) newNode.getValue()).split("/");

        recursiveInsert(parent, newNode, path, 0);
    }

    private void recursiveInsert(Node parent, Node newNode, String[] path, int index) {

        /**
         * If the parent node has children then find among those children if its path is shared.
         * If so then traverse into that child, until destination is found.
         */
        if(parent.getChildren().size() > 0) {

            Node nextChild = null;

            for(Node child : (List<Node>) parent.getChildren()) {

                if(index < path.length - 1) {

                    if(child.getValue().equals(path[index])) {
                        nextChild = child;
                    }
                }
            }

            if(nextChild != null) {
                recursiveInsert(nextChild, newNode, path, index + 1);
            } else {

                if(index < path.length - 1) {
                    Node new_sub_node = new URLNode(path[index]);

                    new_sub_node.setParent(parent);
                    parent.addChildren(new_sub_node);

                    recursiveInsert(new_sub_node, newNode, path, index + 1);
                } else {
                    //check if already exists
                    boolean exists = false;
                    if(parent.getChildren().size() > 0) {

                        for(Node child : (List<Node>) parent.getChildren()) {

                            if(child.getValue().equals(path[index])) {
                                exists = true;
                                child.setNodeData(newNode.getNodeData());
                            }
                        }
                    }

                    if(!exists) {
                        newNode.setParent(parent);
                        parent.addChildren(newNode);
                    }
                }
            }

        } else {

            if(index < path.length - 1) {
                Node new_sub_node = new URLNode(path[index]);

                new_sub_node.setParent(parent);
                parent.addChildren(new_sub_node);

                recursiveInsert(new_sub_node, newNode, path, index + 1);
            } else {
                newNode.setParent(parent);
                parent.addChildren(newNode);
            }
        }
    }

    private Node createNode(String[] arr) throws EntityImportException {

        if(arr.length != headers.length)
            throw new EntityImportException("Row contents do not match number of column headers");

        String url = arr[PRIMARY_INDEX];

        String url_cleaned = url.replace("https://education.byu.edu/", "");

        if(url_cleaned.equals(""))
            url_cleaned = "root";

        Node url_node = new URLNode(url_cleaned);

        for(int i = 0; i < headers.length; i++)
            url_node.getNodeData().getData().put(headers[i], arr[i]);

        return url_node;
    }
}
