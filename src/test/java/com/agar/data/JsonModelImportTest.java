package com.agar.data;

import org.junit.Assert;
import org.junit.Test;

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
        int i = 0;
        for(String modelImport: modelImports) {
                modelFields.get(i).forEach(s -> Assert.assertTrue(JsonModelImport.addModelImport("model-import.json", modelImport, s/*field*/)));
                i++;
        }
    }
}
