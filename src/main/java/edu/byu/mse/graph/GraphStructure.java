package edu.byu.mse.graph;

import edu.byu.mse.exception.EntityImportException;

import java.util.List;

public interface GraphStructure<T> {
    void importObject(String[] arr) throws EntityImportException;
    List<T> readObjects();
}
