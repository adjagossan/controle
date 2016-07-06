package com.agar.command.modelImport;

import com.agar.Command;
import com.agar.data.JsonModelImport;
import com.agar.model.ModelImport;

import java.io.IOException;
import java.util.List;

/**
 * Created by SDEV2 on 06/07/2016.
 */
public class GetListModelImport implements Command {

    public GetListModelImport(JsonModelImport jsonModelImport) {
        this.jsonModelImport = jsonModelImport;
    }

    public JsonModelImport getJsonModelImport() {
        return jsonModelImport;
    }

    public void setJsonModelImport(JsonModelImport jsonModelImport) {
        this.jsonModelImport = jsonModelImport;
    }

    public String getJsonFileName() {
        return JsonFileName;
    }

    public void setJsonFileName(String jsonFileName) {
        JsonFileName = jsonFileName;
    }

    private JsonModelImport jsonModelImport;
    private String JsonFileName;

    public List<ModelImport> getListModelImport() {
        return listModelImport;
    }

    private List<ModelImport> listModelImport;

    public GetListModelImport(JsonModelImport jsonModelImport, String JsonFileName){
        this.jsonModelImport = jsonModelImport;
        this.JsonFileName = JsonFileName;
    }

    @Override
    public void execute() throws IOException {
        this.listModelImport = this.jsonModelImport.getModelImports(this.JsonFileName);
    }
}
