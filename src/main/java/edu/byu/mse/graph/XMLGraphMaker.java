package edu.byu.mse.graph;

import edu.byu.mse.exception.EntityImportException;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class XMLGraphMaker {

    private NodeList nodeList;
    private GraphStructure graph;

    public XMLGraphMaker(NodeList nodeList) {
        this.nodeList = nodeList;
        graph = new URLGraph(nodeList);
    }

    public void importObjects() throws EntityImportException {
        graph.importObjects();
    }

    //TODO: implement this
    public List<String> readObjects() {
        return new ArrayList<>();
    }
}
