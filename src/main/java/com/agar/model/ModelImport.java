package com.agar.model;

import java.util.*;

/**
 * Created by SDEV2 on 27/06/2016.
 */
public class ModelImport {

    public ModelImport(String databaseName) {
        this.databaseName = databaseName;
    }

    public ModelImport() {
    }

    private String databaseName;
    private List<ModelImport.Component> components = new ArrayList<>();

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public List<ModelImport.Component> getComponents() {
        return components;
    }

    public void setComponents(List<ModelImport.Component> components) {
        this.components = components;
    }

    public void addComponent(ModelImport.Component component){
        components.add(component);
    }

    public static class Component{
        private Map<String, Set<String>> model = new HashMap<>();

        public Component(Map<String, Set<String>> model) {
            this.model = model;
        }

        public Component() {
        }

        public Component(String tableName){
            model.put(tableName, new HashSet<>());
        }

        public Map<String, Set<String>> getModel() {
            return model;
        }

        public void setModel(Map<String, Set<String>> model) {
            this.model = model;
        }
    }
}
