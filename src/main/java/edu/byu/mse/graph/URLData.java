package edu.byu.mse.graph;

import java.util.HashMap;
import java.util.Map;

public class URLData implements NodeData<String> {
    Map<String, String> data;

    public URLData() {
        data = new HashMap<>();
    }
    @Override
    public Map<String, String> getData() {
        return data;
    }
}
