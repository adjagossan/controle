package com.agar;

import java.io.IOException;

/**
 * Created by SDEV2 on 30/06/2016.
 */
public interface Subscriber {
    void update(Subject subject) throws IOException;
}
