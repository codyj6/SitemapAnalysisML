package edu.byu.mse.graph;

import edu.byu.mse.exception.EntityImportException;

import java.util.List;

public interface GraphStructure<T> {
    void importObjects() throws EntityImportException;
    List<T> readObjects();
}
