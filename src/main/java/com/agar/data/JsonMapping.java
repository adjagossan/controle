package com.agar.data;

import com.agar.model.Mapping;
import com.agar.model.ModelImport;
import com.google.gson.Gson;

import java.io.*;
import java.util.*;

/**
 * Created by SDEV2 on 18/07/2016.
 */
public class JsonMapping {
    private static JsonMapping jsonMapping = new JsonMapping();
    private static String jsonFileName;

    private JsonMapping(){}

    public static JsonMapping getInstance(String jsonFileName){
        JsonMapping.jsonFileName = jsonFileName;
        return jsonMapping;
    }

    public boolean addMapping(String databaseName, Mapping.Component component) throws IOException {
        Map<String, String> mapTable = component.getTable();
        String key = null;
        if(mapTable != null)
            key = mapTable.keySet().stream().findFirst().get();
        Map<String, String> mapFields = component.getFields();
        List<Mapping> mappings = getMappings();
        Mapping mapping_ = null;

        for(Mapping map : mappings){
            if(map.getDatabase().contentEquals(databaseName)){
                mapping_ = map;
                break;
            }
        }

        BufferedWriter writer = null;
        Gson gson = new Gson();
        Mapping mapping;
        if(mapping_ == null){
            mapping = new Mapping();
            mapping.setDatabase(databaseName);
            mapping.addComponent(component);
            mappings.add(mapping);
        }
        else{
            if(mapTable != null){
                for(Mapping.Component comp : mapping_.getComponents()){
                    if(comp.getTable().containsKey(key))
                        comp.getTable().replace(key, mapTable.get(key));
                }
            }
        }
        return false;
    }

    public List<Mapping> getMappings() throws IOException {
        BufferedReader reader;
        String content = "";
        List<Mapping> mappings = new ArrayList<>();
        Gson gson = new Gson();
        try
        {
            reader = new BufferedReader(new FileReader(Objects.requireNonNull(JsonMapping.jsonFileName, "the Json file name can't be null")));
            try
            {
                String line;
                while ((line = reader.readLine()) != null)
                    content += line;
                if(!content.contentEquals(""))
                    mappings = new ArrayList<>(Arrays.asList(gson.fromJson(content, Mapping[].class)));
            }finally{
                if(reader != null) reader.close();
            }
        }
        catch (FileNotFoundException e)
        {
            throw e;
        }
        catch (IOException e)
        {
            throw e;
        }
        return mappings;
    }
}
