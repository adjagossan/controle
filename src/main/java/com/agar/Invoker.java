package com.agar;

import com.agar.utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by SDEV2 on 06/07/2016.
 */
public class Invoker {
    private Map<Utils.Cmd, Command> map;

    private static Invoker invoker = new Invoker();

    private Invoker(){
        map = new HashMap<>();
    }

    public static Invoker getInstance(){
        return invoker;
    }

    public void addCommand(Utils.Cmd key, Command cmd){

        Objects.requireNonNull(key);
        Objects.requireNonNull(cmd);
        this.map.put(key, cmd);
    }

    public void invoke(Utils.Cmd key) throws IOException {
        if(map.containsKey(key))
            map.get(key).execute();
        else
            throw new IllegalArgumentException(key+" isn't a valid command");
    }

    public Command getCommand(Utils.Cmd key){
        Command cmd = null;
        if(map.containsKey(key))
            cmd = map.get(key);
        return cmd;
    }
}
