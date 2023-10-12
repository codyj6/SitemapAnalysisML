package edu.byu.mse.graph;

import java.net.URL;
import java.util.List;

public class URLNode<String> implements Node {

    private String value;
    private String changeFreq;
    private Node parent;
    private List<Node> children;

    public URLNode(String url, String changeFreq) {
        this.value = url;
        this.changeFreq = changeFreq;
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
}
