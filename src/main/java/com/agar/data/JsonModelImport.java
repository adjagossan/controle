package com.agar.data;

import com.agar.Subject;
import com.agar.Subscriber;
import com.agar.model.ModelImport;
import com.agar.view.ListViewModelImport;
import com.agar.view.alert.ExceptionHandler;
import com.google.gson.Gson;
//import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Created by SDEV2 on 27/06/2016.
 */
public class JsonModelImport implements Subject{
    //private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(JsonModelImport.class);
    private List<Subscriber> subscribers = new ArrayList<>();
    private String selectedModel;
    private static JsonModelImport jsonModelImport = new JsonModelImport();

    private JsonModelImport(){}

    public static JsonModelImport getInstance(){
        if(jsonModelImport == null)
            jsonModelImport = new JsonModelImport();
        return jsonModelImport;
    }
    /**
     * @param JsonFileName
     * @return
     */
    public List<ModelImport> getModelImports(String JsonFileName) throws IOException {
        BufferedReader reader;
        String content = "";
        List<ModelImport> modelImportList = new ArrayList<>();
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
                    modelImportList = new ArrayList<>(Arrays.asList(gson.fromJson(content, ModelImport[].class)));
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
        return modelImportList;
    }

    /**
     *
     * @param JsonFileName
     * @param modelImportName
     * @param field
     * @return
     */
    public boolean addModelImport(String JsonFileName, String modelImportName, String field) throws IOException {
        String modelImport = null;
        if(modelImportName != null) modelImport = modelImportName.trim();
        BufferedWriter writer = null;
        List<ModelImport> modelImportList/* = new ArrayList<>()*/;
        Gson gson = new Gson();

        try {
            modelImportList = getModelImports(JsonFileName);

            if(modelImport != null) {
                if(!containsModel(modelImportList, modelImport, null))
                    modelImportList.add(new ModelImport(modelImport));
                if(field != null)
                    containsModel(modelImportList, modelImport, field);
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
            //logger.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
        catch (IOException e) {
            //logger.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
        /*
        TODO
        if(isAttached(ListViewModelImport.getInstance()))
        */
        setValue(modelImportName);
        return true;
    }

    private boolean containsModel(List<ModelImport> modelImportList, String modelImportName, String field)
    {
        boolean found = false;
        if(modelImportList !=null && !modelImportList.isEmpty()&& modelImportName != null)
        {
            for (ModelImport model : modelImportList) {
                for (String key : model.getModel().keySet()) {
                    if (key.contentEquals(modelImportName)) {
                        if (field != null)
                            model.getModel().get(key).add(field);
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }
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
        selectedModel = (String)object;
        notifySubscribers();
    }

    @Override
    public Object getValue() {
        return this.selectedModel;
    }
}