package com.agar.data;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by SDEV2 on 27/06/2016.
 */
public class JsonConstraintTest {

    @Test
    public void addConstraint(){
        for(String constraint: new String[]{"champ non vide", "champ numerique"})
            Assert.assertTrue(JsonConstraint.addConstraint("constraint.json", constraint));
    }
}
