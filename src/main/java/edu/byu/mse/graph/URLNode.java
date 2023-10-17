package edu.byu.mse.graph;

import java.util.ArrayList;
import java.util.List;

public class URLNode<String> implements Node {

    private String value;
    private NodeData data;
    private Node parent;
    private List<Node> children;

    public URLNode(String url) {
        this.value = url;
        this.data = new URLData();
        this.children = new ArrayList<>();
    }

    @Override
    public List<Node> getChildren() {
        return children;
    }

    @Override
    public Node getParent() {
        return parent;
    }

    @Override
    public void setParent(Node node) {
        parent = node;
    }

    @Override
    public void addChildren(Node node) {
        children.add(node);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (String) value;
    }

    @Override
    public NodeData getNodeData() {
        return data;
    }

    @Override
    public void setNodeData(NodeData data) {
        this.data = data;
    }

}
