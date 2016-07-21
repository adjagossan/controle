package com.agar.data;

import com.agar.model.Mapping;
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

    public boolean addMapping(String databaseName, Mapping.Component component, boolean saveInJsonFile) throws IOException {
        Map<String, String> mapTable = component.getTable();
        Map<String, String> mapFields = component.getFields();
        String tableKey = null;
        BufferedWriter writer = null;
        Gson gson = new Gson();

        if(mapTable != null)
            tableKey = mapTable.keySet().stream().findFirst().get();

        List<Mapping> mappings = getMappings();
        Mapping mapping_ = null;

        for(Mapping map : mappings){
            if(map.getDatabase().contentEquals(databaseName)){
                mapping_ = map;
                break;
            }
        }

        Mapping mapping;
        if(mapping_ == null){
            mapping = new Mapping();
            mapping.setDatabase(databaseName);
            mapping.addComponent(component);
            mappings.add(mapping);
        }
        else{
            if(mapTable != null){
                boolean foundTable = false;
                for(Mapping.Component comp : mapping_.getComponents()){
                    if(comp.getTable().containsKey(tableKey)){
                        comp.getTable().replace(tableKey, mapTable.get(tableKey));
                        foundTable = true;
                        break;
                    }
                    //else
                     //   comp.getTable().put(tableKey, mapTable.get(tableKey));
                }
                if(!foundTable){
                    Mapping.Component comp = new Mapping.Component();
                    comp.addTable(mapTable);
                    mapping_.addComponent(comp);
                }
            }
            if(mapFields != null){
                for(Mapping.Component comp : mapping_.getComponents()){
                    String tableName = comp.getTable().keySet().stream().findFirst().get();
                    if(tableName.contentEquals(tableKey)){
                        /*comp.getFields().forEach((s, s2) -> {
                            if(mapFields.containsKey(s))
                                comp.getFields().replace(s, s2);
                            else
                                comp.getFields().put(s, s2);
                        });*/
                        mapFields.forEach((s, s2) -> {
                            if(comp.getFields().containsKey(s))
                                comp.getFields().replace(s, s2);
                            else
                                comp.getFields().put(s, s2);
                        });
                    }
                }
            }
        }

        if(saveInJsonFile){
            try {
                writer = new BufferedWriter(new FileWriter(jsonFileName));
                writer.write(gson.toJson(mappings));
            }
            finally{
                if(writer != null) writer.close();
            }
        }
        return true;
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
        System.out.println(gson.toJson(mappings));
        return mappings;
    }
}
