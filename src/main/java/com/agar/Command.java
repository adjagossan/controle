package com.agar;

import java.io.IOException;

/**
 * Created by SDEV2 on 06/07/2016.
 */
@FunctionalInterface
public interface Command {
    void execute() throws IOException;
}
