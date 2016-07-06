package com.agar.command.constraint;

import com.agar.Command;
import com.agar.data.JsonConstraint;

import java.io.IOException;

/**
 * Created by SDEV2 on 06/07/2016.
 */
public class AddConstraint implements Command {

    public AddConstraint(String jsonFileName, JsonConstraint jsonConstraint) {
        JsonFileName = jsonFileName;
        this.jsonConstraint = jsonConstraint;
    }

    public AddConstraint(JsonConstraint jsonConstraint) {
        this.jsonConstraint = jsonConstraint;
    }

    public String getConstraint() {
        return constraint;
    }

    public void setConstraint(String constraint) {
        this.constraint = constraint;
    }

    public String getJsonFileName() {
        return JsonFileName;
    }

    public void setJsonFileName(String jsonFileName) {
        JsonFileName = jsonFileName;
    }

    private String constraint;
    private JsonConstraint jsonConstraint;
    private String JsonFileName;

    public AddConstraint(JsonConstraint jsonConstraint, String JsonFileName, String constraint){
        this.constraint = constraint;
        this.jsonConstraint = jsonConstraint;
        this.JsonFileName = JsonFileName;
    }


    @Override
    public void execute() throws IOException {
        this.jsonConstraint.addConstraint(this.JsonFileName, this.constraint);
    }
}
