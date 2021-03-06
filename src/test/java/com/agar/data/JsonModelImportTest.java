package com.agar.data;

import com.agar.factory.DaoFactory;
import com.agar.utils.Utils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by SDEV2 on 28/06/2016.
 */
public class JsonModelImportTest
{
    @Test
    public void addModelImportTest()
    {
        List<String> modelImports = Arrays.asList("patient", "dossier");
        List<String> fieldOne = Arrays.asList("nom", "prenom", "civilite");
        List<String> fieldTwo = Arrays.asList("adresse", "mail", "ville");
        List<List<String>> modelFields = Arrays.asList(fieldOne, fieldTwo);
        DaoFactory.setDatabaseName("sakila");
        int i = 0;

        for(String modelImport: modelImports) {
                modelFields.get(i).forEach(s -> {
                    try {
                        Assert.assertTrue(JsonModelImport.getInstance().addModelImport(Utils.modelImportJsonFileName, modelImport, s/*field*/));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                i++;
        }
    }

    @Test
    public void getModelImportTest(){
        try {
            JsonModelImport.getInstance().getModelImports("model-import.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getUserHomeDirectory(){
        System.out.println(System.getProperty("user.home"));
    }
}
