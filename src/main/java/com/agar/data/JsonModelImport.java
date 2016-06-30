package com.agar.data;

import com.agar.model.ModelImport;
import com.google.gson.Gson;
//import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by SDEV2 on 27/06/2016.
 */
public class JsonModelImport {
    //private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(JsonModelImport.class);

    /**
     *
     * @param JsonFileName
     * @return
     */
    public static List<ModelImport> getModelImports(String JsonFileName)
    {
        BufferedReader reader;
        String content = "";
        List<ModelImport> modelImportList = new ArrayList<>();
        Gson gson = new Gson();
        try
        {
            reader = new BufferedReader(new FileReader(JsonFileName));
            String line;
            while ((line = reader.readLine()) != null)
                content += line;
            reader.close();

            if(!content.contentEquals(""))
                modelImportList = new ArrayList<>(Arrays.asList(gson.fromJson(content, ModelImport[].class)));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
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
    public static boolean addModelImport(String JsonFileName, String modelImportName, String field)
    {
        //BufferedReader reader;
        BufferedWriter writer;
        //String content = "";
        List<ModelImport> modelImportList/* = new ArrayList<>()*/;
        Gson gson = new Gson();

        try
        {
            modelImportList = getModelImports(JsonFileName);

            if(modelImportName != null)
            {
                if(!containsModel(modelImportList, modelImportName, null))
                    modelImportList.add(new ModelImport(modelImportName));
                if(field != null)
                    containsModel(modelImportList, modelImportName, field);
            }

            writer = new BufferedWriter(new FileWriter(JsonFileName));
            writer.write(gson.toJson(modelImportList));
            writer.close();
            return true;
        }
        catch(FileNotFoundException e) {
            //logger.error(e.getMessage());
            return false;
        }
        catch (IOException e) {
            //logger.error(e.getMessage());
            return false;
        }
    }

    private static boolean containsModel(List<ModelImport> modelImportList, String modelImportName, String field)
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
}
