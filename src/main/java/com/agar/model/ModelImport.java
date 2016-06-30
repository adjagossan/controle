package com.agar.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SDEV2 on 27/06/2016.
 */
public class ModelImport {

    public ModelImport(String name) {
        this.model.put(name, new ArrayList<>());
    }

    public ModelImport() {
    }

    public Map<String, List<String>> getModel() {
        return model;
    }

    public void setModel(Map<String, List<String>> model) {
        this.model = model;
    }

    private Map<String, List<String>> model = new HashMap<>();
}
