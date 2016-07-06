package com.agar.command.constraint;

import com.agar.Command;
import com.agar.data.JsonConstraint;
import com.agar.model.Constraint;

import java.io.IOException;
import java.util.List;

/**
 * Created by SDEV2 on 06/07/2016.
 */
public class GetListConstraint implements Command {

    private String JsonFileName;

    public List<Constraint> getListConstraint() {
        return listConstraint;
    }

    private List<Constraint> listConstraint;

    public JsonConstraint getJsonConstraint() {
        return jsonConstraint;
    }

    public void setJsonConstraint(JsonConstraint jsonConstraint) {
        this.jsonConstraint = jsonConstraint;
    }

    public String getJsonFileName() {
        return JsonFileName;
    }

    public void setJsonFileName(String jsonFileName) {
        JsonFileName = jsonFileName;
    }

    private JsonConstraint jsonConstraint;

    public GetListConstraint(String JsonFileName, JsonConstraint jsonConstraint) {
        this.JsonFileName = JsonFileName;
        this.jsonConstraint = jsonConstraint;
    }

    public GetListConstraint(JsonConstraint jsonConstraint) {
        this.jsonConstraint = jsonConstraint;
    }

    @Override
    public void execute() throws IOException {
        this.listConstraint = this.jsonConstraint.getConstraints(this.JsonFileName);
    }
}
