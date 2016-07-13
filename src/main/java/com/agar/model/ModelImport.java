package com.agar.model;

import java.util.*;

/**
 * Created by SDEV2 on 27/06/2016.
 */
public class ModelImport {

    public ModelImport(String name) {
        this.model.put(name, new HashSet/*ArrayList*/<>());
    }

    public ModelImport() {
    }

    public Map<String, /*List*/Set<String>> getModel() {
        return model;
    }

    public void setModel(Map<String, /*List*/Set<String>> model) {
        this.model = model;
    }

    private Map<String, /*List*/Set<String>> model = new HashMap<>();
}
