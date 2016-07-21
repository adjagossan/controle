package com.agar.data;

import com.agar.model.Mapping;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by SDEV2 on 18/07/2016.
 */
public class JsonMappingTest {
    @Test
    public void addMappingTest(){
        String databaseName = "ReportServerTempDB";

        Map<String, String> table = new HashMap<>();
        table.put("ChunkData", "Chunk Data");

        Map<String, String> table_ = new HashMap<>();
        table_.put("Segment", "seg-ment");

        Map<String, String> fields = new HashMap<>();
        fields.put("SnapshotDataID", "SnapshotData ID");
        fields.put("ChunkID", "Chunk ID");

        Map<String, String> fields_ = new HashMap<>();
        fields_.put("Longueur", "long-ueur");
        fields_.put("Largeur", "lar-geur");

        Mapping.Component component =  new Mapping.Component(table, fields);
        Mapping.Component component_ =  new Mapping.Component(table_, fields_);

        String jsonFileName = "mapping.json";
        JsonMapping jsonMapping = JsonMapping.getInstance(jsonFileName);
        try {
            Assert.assertTrue(jsonMapping.addMapping(databaseName, component, true));
            Assert.assertTrue(jsonMapping.addMapping(databaseName, component_, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMappingTest(){
        String jsonFileName = "mapping.json";
        JsonMapping jsonMapping = JsonMapping.getInstance(jsonFileName);
        try {
            Assert.assertNotNull(jsonMapping.getMappings());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
