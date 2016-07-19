package com.agar.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SDEV2 on 18/07/2016.
 */
public class Mapping {
    private String database;
    private List<Component> components = new ArrayList<>();

    public Mapping(){}

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void addComponent(Component component){
        this.components.add(component);
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public class Component{
        private Map<String, String> table = new HashMap<>();
        private Map<String, String> fields = new HashMap<>();

        public Component(Map<String, String> table, Map<String, String> fields) {
            this.table = table;
            this.fields = fields;
        }

        public Map<String, String> getTable() {
            return table;
        }

        public void setTable(Map<String, String> table) {
            this.table = table;
        }

        public Map<String, String> getFields() {
            return fields;
        }

        public void setFields(Map<String, String> fields) {
            this.fields = fields;
        }
    }
}
