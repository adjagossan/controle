package com.agar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by SDEV2 on 06/07/2016.
 */
public class Invoker {
    private Map<String, Command> map;
    private static Invoker invoker = new Invoker();

    private Invoker(){
        map = new HashMap<>();
    }

    public Invoker getInstance(){
        return invoker;
    }

    public void addCommand(String key, Command cmd){
        Objects.requireNonNull(key);
        Objects.requireNonNull(cmd);
        this.map.put(key, cmd);
    }

    public void invoke(String key) throws IOException {
        if(map.containsKey(key))
            map.get(key).execute();
        else
            throw new IllegalArgumentException(key+" isn't a valid command");
    }
}
