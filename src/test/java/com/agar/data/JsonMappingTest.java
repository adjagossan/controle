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
        Map<String, String> fields = new HashMap<>();
        fields.put("SnapshotDataID", "SnapshotData ID");
        fields.put("ChunkID", "Chunk ID");
        Mapping mapping = new Mapping();
        Mapping.Component component =  mapping.new Component(table, fields);
        mapping.addComponent(component);

        String jsonFileName = "mapping.json";
        JsonMapping jsonMapping = JsonMapping.getInstance(jsonFileName);
        try {
            Assert.assertTrue(jsonMapping.addMapping(databaseName, component, true));
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
