package com.agar.data;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by SDEV2 on 27/06/2016.
 */
public class JsonConstraintTest {

    @Test
    public void addConstraint(){
        for(String constraint: new String[]{"champ non vide", "champ numerique"})
            try {
                Assert.assertTrue(JsonConstraint.addConstraint("constraint.json", constraint));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
