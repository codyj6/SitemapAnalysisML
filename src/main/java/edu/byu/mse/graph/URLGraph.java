package edu.byu.mse.graph;

import edu.byu.mse.exception.EntityImportException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class URLGraph implements GraphStructure<URLNode> {

    private Node head;

    public NodeList nodeList;

    public URLGraph(NodeList list) {
        this.nodeList = list;
    }

    public URLGraph(Node head) {
        this.head = head;
    }

    @Override
    public void importObjects() throws EntityImportException {

        for(int i = 0; i < nodeList.getLength(); i++) {
            org.w3c.dom.Node node = nodeList.item(i);

            if(node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {

                if(i == 0) {
                    head = createNode(node);
                } else {
                    insertIntoTree(head, createNode(node));
                }
            }
        }
    }

    @Override
    public List<URLNode> readObjects() {
        List<URLNode> urls = new ArrayList<>();

        if(head == null)
            return urls;

        Queue queue = new LinkedList();
        queue.add(head);

        while(!queue.isEmpty()) {
            URLNode node = (URLNode) queue.poll();

            if(node.getChangeFreq() != null) {
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
                    Node new_sub_node = new URLNode(path[index], null);

                    new_sub_node.setParent(parent);
                    parent.addChildren(new_sub_node);

                    recursiveInsert(new_sub_node, newNode, path, index + 1);
                } else {
                    newNode.setParent(parent);
                    parent.addChildren(newNode);
                }
            }

        } else {

            if(index < path.length - 1) {
                Node new_sub_node = new URLNode(path[index], null);

                new_sub_node.setParent(parent);
                parent.addChildren(new_sub_node);

                recursiveInsert(new_sub_node, newNode, path, index + 1);
            } else {
                newNode.setParent(parent);
                parent.addChildren(newNode);
            }
        }
    }

    private Node createNode(org.w3c.dom.Node node) throws EntityImportException {
        Element elem = (Element) node;

        if(elem.getElementsByTagName("loc") == null || elem.getElementsByTagName("loc").getLength() == 0)
            throw new EntityImportException("Loc is null or empty");

        if(elem.getElementsByTagName("changefreq") == null || elem.getElementsByTagName("changefreq").getLength() == 0)
            throw new EntityImportException("Changefreq is null or empty");

        String url = elem.getElementsByTagName("loc").item(0).getTextContent();
        String changeFreq = elem.getElementsByTagName("changefreq").item(0).getTextContent();

        String url_cleaned = url.replace("https://education.byu.edu/", "");

        return new URLNode(url_cleaned, changeFreq);
    }
}
