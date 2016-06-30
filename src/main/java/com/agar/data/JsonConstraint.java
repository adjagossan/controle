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

    /**
     *
     * @param JsonFileName
     * @return
     */
    public static List<Constraint> getConstraints(String JsonFileName)
    {
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
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
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
    public static boolean addConstraint(String JsonFileName, String constraint)
    {
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
            return true;
        }
        catch(FileNotFoundException e)
        {
            //logger.error(e.getMessage());
            return false;
        }
        catch (IOException e)
        {
            //logger.error(e.getMessage());
            return false;
        }
    }

    /**
     *
     * @param JsonFileName
     * @param constraint
     * @return
     */
    public static boolean updateConstraint(String JsonFileName, String constraint){

        return true;
    }
}
