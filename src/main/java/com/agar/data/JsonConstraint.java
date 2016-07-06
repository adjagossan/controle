package com.agar.data;

import com.agar.model.Constraint;
import com.google.gson.Gson;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Created by SDEV2 on 27/06/2016.
 */

public class JsonConstraint
{
   // private static Logger logger = LogManager.getLogger(JsonConstraint.class);
    private static JsonConstraint jsonConstraint = new JsonConstraint();

    private JsonConstraint(){}

    public static JsonConstraint getInstance(){
       return jsonConstraint;
    }
    /**
     *
     * @param JsonFileName
     * @return
     */
    public List<Constraint> getConstraints(String JsonFileName) throws IOException {
        BufferedReader reader;
        String content = "";
        List<Constraint> constraintList = new ArrayList<>();
        Gson gson = new Gson();

        try
        {
            reader = new BufferedReader(new FileReader(JsonFileName));
            String line;
            while ((line = reader.readLine()) != null)
                content += line;
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
        catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        if(!content.contentEquals(""))
            constraintList = new ArrayList<>(Arrays.asList(gson.fromJson(content, Constraint[].class)));
        return constraintList;
    }

    /**
     * add a constraint in the specified Json file
     * @param JsonFileName a JSon file name
     * @param constraint   a constaint
     * @return boolean
     */
    public boolean addConstraint(String JsonFileName, String constraint) throws IOException {
        constraint = constraint.trim();
        BufferedWriter writer;
        List<Constraint> constraintList;
        Gson gson = new Gson();
        try
        {
            constraintList = getConstraints(JsonFileName);
            constraintList.add(new Constraint(constraint));

            writer = new BufferedWriter(new FileWriter(JsonFileName));
            writer.write(gson.toJson(constraintList));
            writer.close();
        }
        catch(FileNotFoundException e)
        {
            //logger.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
        catch (IOException e)
        {
            //logger.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return true;
    }
}
