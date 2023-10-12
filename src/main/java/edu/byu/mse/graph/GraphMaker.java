package edu.byu.mse.graph;

import edu.byu.mse.exception.EntityImportException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.List;

public class GraphMaker {

    private Node head;
    private NodeList nodeList;

    public GraphMaker(NodeList nodeList) {
        this.nodeList = nodeList;
    }

    public void importObjects() throws EntityImportException {

        for(int i = 0; i < 10 /*nList.getLength()*/; i++) {
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

    private void insertIntoTree(Node parent, Node newNode) {

        String[] path = ((String) newNode.getValue()).split("/");

        recursiveInsert(parent, newNode, path, 1);
    }

    private void recursiveInsert(Node parent, Node newNode, String[] path, int index) {

        for(Node child : (List<Node>) parent.getChildren()) {

            //TODO: make this work right
            if(child.getValue().equals(path[index])) {
                //go into node recursively
                recursiveInsert(child, newNode, path, index + 1);
            } else {
                //create new node using that path and go into it recursively
                if(index < path.length - 1)
                    recursiveInsert(new URLNode(path[index], null), newNode, path, index + 1);
                else {
                    newNode.setParent(parent);
                    parent.addChildren(newNode);
                }
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

        return new URLNode(url, changeFreq);
    }
}
