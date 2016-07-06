package com.agar.command.modelImport;

import com.agar.Command;
import com.agar.data.JsonModelImport;

import java.io.IOException;

/**
 * Created by SDEV2 on 06/07/2016.
 */
public class AddModelImport implements Command {

    private JsonModelImport jsonModelImport;
    private String modelImport;

    public AddModelImport(JsonModelImport jsonModelImport) {
        this.jsonModelImport = jsonModelImport;
    }

    public AddModelImport(JsonModelImport jsonModelImport, String jsonFileName) {
        this.jsonModelImport = jsonModelImport;
        JsonFileName = jsonFileName;
    }

    public String getJsonFileName() {
        return JsonFileName;
    }

    public void setJsonFileName(String jsonFileName) {
        JsonFileName = jsonFileName;
    }

    public JsonModelImport getJsonModelImport() {
        return jsonModelImport;
    }

    public void setJsonModelImport(JsonModelImport jsonModelImport) {
        this.jsonModelImport = jsonModelImport;
    }

    public String getModelImport() {
        return modelImport;
    }

    public void setModelImport(String modelImport) {
        this.modelImport = modelImport;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    private String JsonFileName;
    private String field;

    public AddModelImport(JsonModelImport jsonModelImport, String modelImport, String field, String JsonFileName){
        this.jsonModelImport = jsonModelImport;
        this.modelImport = modelImport;
        this.JsonFileName = JsonFileName;
        this.field = field;
    }

    @Override
    public void execute() throws IOException {
        this.jsonModelImport.addModelImport(this.JsonFileName, this.modelImport, this.field);
    }
}
