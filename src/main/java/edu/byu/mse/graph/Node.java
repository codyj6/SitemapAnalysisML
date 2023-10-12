package edu.byu.mse.graph;

import java.util.List;

public interface Node<T> {
    List<Node> getChildren();
    Node getParent();
    void setParent(Node node);
    void addChildren(Node node);
    T getValue();
}
