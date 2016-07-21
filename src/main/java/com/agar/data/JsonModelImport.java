package com.agar.data;

import com.agar.Subject;
import com.agar.Subscriber;
import com.agar.factory.DaoFactory;
import com.agar.model.Mapping;
import com.agar.model.ModelImport;
import com.agar.view.ListViewModelImport;
import com.agar.view.alert.ExceptionHandler;
import com.google.gson.Gson;
//import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by SDEV2 on 27/06/2016.
 */
public class JsonModelImport implements Subject{
    //private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(JsonModelImport.class);
    private List<Subscriber> subscribers = new ArrayList<>();
    private /*String*/Info selectedModel;
    private static JsonModelImport jsonModelImport = new JsonModelImport();

    private JsonModelImport(){}

    public static JsonModelImport getInstance(){
        return jsonModelImport;
    }
    /**
     * @param JsonFileName
     * @return
     */
    public Set/*List*/<ModelImport> getModelImports(String JsonFileName) throws IOException {
        BufferedReader reader;
        String content = "";
        /*List*/Set<ModelImport> modelImportList = new HashSet<>()/*ArrayList<>()*/;
        Gson gson = new Gson();
        try
        {
            reader = new BufferedReader(new FileReader(Objects.requireNonNull(JsonFileName, "the Json file name can't be null")));
            try
            {
                String line;
                while ((line = reader.readLine()) != null)
                    content += line;
                if(!content.contentEquals(""))
                    modelImportList = new /*ArrayList<>*/HashSet<>(Arrays.asList(gson.fromJson(content, ModelImport[].class)));
            }finally{
                if(reader != null) reader.close();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            throw e;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw e;
        }
        System.out.println(gson.toJson(modelImportList));
        return modelImportList;
    }

    public void addDatabaseName(Set<ModelImport> modelImports, String databaseName){
        boolean found = false;
        for(ModelImport modelImport : modelImports){
            if(modelImport.getDatabaseName().contentEquals(databaseName)) {
                found = true;
                break;
            }
        }
        if(!found)
            modelImports.add(new ModelImport(databaseName));
    }
    /**
     *
     * @param JsonFileName
     * @param modelImportName
     * @param field
     * @return
     */
    public boolean addModelImport(String JsonFileName, String modelImportName, String field) throws IOException {
        BufferedWriter writer = null;
        Set<ModelImport> modelImportList/* = new ArrayList<>()*/;
        String databaseName;
        Gson gson = new Gson();

        try {
            modelImportList = getModelImports(JsonFileName);
            databaseName = DaoFactory.getDatabaseName();
            if(modelImportName != null && databaseName != null) {
                addDatabaseName(modelImportList, databaseName);
                /*if(!containsModel(modelImportList, modelImportName, null))
                    modelImportList.add(new ModelImport(modelImportName));*/
                containsModel(modelImportList, modelImportName, null);
                if(field != null)
                    containsModel(modelImportList, modelImportName, field);
            }

            try {
                writer = new BufferedWriter(new FileWriter(JsonFileName));
                writer.write(gson.toJson(modelImportList));
            }
            finally{
                if(writer != null) writer.close();
            }
        }
        catch(FileNotFoundException e) {
            throw e;
        }
        catch (IOException e) {
            throw e;
        }

        setValue(new Info(modelImportName, databaseName));
        return true;
    }

    private boolean containsModel(Set<ModelImport> modelImportList, String modelImportName, String field)
    {
        boolean found = false;
        String databaseName;
        ModelImport selectedModelImport = null;

        if(modelImportList !=null && !modelImportList.isEmpty()&& modelImportName != null)
        {
            /**************************************************************************/
            databaseName = DaoFactory.getDatabaseName();
            if(databaseName != null) {
                for (ModelImport model : modelImportList) {
                    if (model.getDatabaseName().contentEquals(databaseName)){
                        selectedModelImport = model;
                        break;
                    }
                }
                if(selectedModelImport != null) {
                    for (ModelImport.Component component : selectedModelImport.getComponents()) {
                        for(String key : component.getModel().keySet()){
                            if(key.contentEquals(modelImportName)){
                                if (field != null)
                                    component.getModel().get(key).add(field);
                                found = true;
                                break;
                            }
                        }
                        if (found) break;
                    }
                    if(!found)
                        selectedModelImport.getComponents().add(new ModelImport.Component(modelImportName));
                }
            }
            /**************************************************************************/
            /*for (ModelImport model : modelImportList) {
                for (String key : model.getModel().keySet()) {
                    if (key.contentEquals(modelImportName)) {
                        if (field != null)
                            model.getModel().get(key).add(field);
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }*/
        }
        return found;
    }

    @Override
    public void register(Subscriber subscriber) {
        if(!subscribers.contains(subscriber))subscribers.add(subscriber);
    }

    @Override
    public void unregister(Subscriber subscriber) {
        if(subscribers.contains(subscriber))subscribers.remove(subscriber);
    }

    @Override
    public boolean isAttached(Subscriber subscriber) {
        return subscribers.contains(subscriber);
    }

    @Override
    public void notifySubscribers() {
        subscribers.forEach(subscriber -> {
            try {
                subscriber.update(this);
            } catch (IOException e) {
                e.printStackTrace();
                new ExceptionHandler(e, e.getMessage(), null, null).showAndWait();
            }
        });
    }

    @Override
    public void setValue(Object object) {
        selectedModel = (JsonModelImport.Info)object;
        notifySubscribers();
    }

    @Override
    public Object getValue() {
        return this.selectedModel;
    }

    public static class Info{
        String databaseName;
        String tableName;

        public Info(String databaseName, String tableName){
            Info.this.databaseName = databaseName;
            Info.this.tableName = tableName;
        }

        public Info(){}

        public String getDatabaseName() {
            return databaseName;
        }

        public void setDatabaseName(String databaseName) {
            this.databaseName = databaseName;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }
    }
}