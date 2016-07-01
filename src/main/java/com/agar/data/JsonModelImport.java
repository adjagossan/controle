package com.agar.data;

import com.agar.model.ModelImport;
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
public class JsonModelImport {
    //private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(JsonModelImport.class);

    /**
     *
     * @param JsonFileName
     * @return
     */
    public static List<ModelImport> getModelImports(String JsonFileName) throws IOException {
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
    public static boolean addModelImport(String JsonFileName, String modelImportName, String field) throws IOException {
        BufferedWriter writer = null;
        List<ModelImport> modelImportList/* = new ArrayList<>()*/;
        Gson gson = new Gson();

        try {
            modelImportList = getModelImports(JsonFileName);

            if(modelImportName != null) {
                if(!containsModel(modelImportList, modelImportName, null))
                    modelImportList.add(new ModelImport(modelImportName));
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

            return true;
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
