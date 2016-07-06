package com.agar.utils;

import com.agar.Invoker;
import com.agar.command.constraint.AddConstraint;
import com.agar.command.constraint.GetListConstraint;
import com.agar.command.modelImport.AddModelImport;
import com.agar.command.modelImport.GetListModelImport;
import com.agar.data.JsonConstraint;
import com.agar.data.JsonModelImport;
import javafx.scene.control.TreeView;

/**
 * Created by SDEV2 on 28/06/2016.
 */
public class Utils
{
    public static final String modelImportJsonFileName = "model-import.json";
    public static final String constraintJsonFileName = "constraint.json";

    public static void configCommand(){
        /**
         * Initialisation des commandes
         */
        AddConstraint addConstraint = new AddConstraint(Utils.constraintJsonFileName, JsonConstraint.getInstance());
        GetListConstraint getListConstraint = new GetListConstraint(Utils.constraintJsonFileName, JsonConstraint.getInstance());

        AddModelImport addModelImport = new AddModelImport(JsonModelImport.getInstance(), Utils.modelImportJsonFileName);
        GetListModelImport getListModelImport = new GetListModelImport(JsonModelImport.getInstance(), Utils.modelImportJsonFileName);

        Invoker invoker = Invoker.getInstance();
        invoker.addCommand(Cmd.ADD_CONSTRAINT, addConstraint);
        invoker.addCommand(Cmd.GET_CONSTRAINT, getListConstraint);
        invoker.addCommand(Cmd.ADD_MODEL_IMPORT, addModelImport);
        invoker.addCommand(Cmd.GET_MODEL_IMPORT, getListModelImport);
    }

    public enum Cmd{
        ADD_CONSTRAINT, GET_CONSTRAINT, ADD_MODEL_IMPORT, GET_MODEL_IMPORT;
    }
}
