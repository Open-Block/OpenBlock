package org.vector;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.vector.type.Vector3;

public class TestVector3 {

    @Test
    public void testSumSame() {
        //SETUP
        Vector3<Double> origin = Vector3.valueOf(1.0, 0, 0);
        Vector3<Double> sum = Vector3.valueOf(2.0, 0, 0);

        //ACT
        Vector3<Double> result = origin.plus(sum);

        //ASSERT
        Assertions.assertEquals(3.0, result.getX());
        Assertions.assertEquals(0.0, result.getY());
        Assertions.assertEquals(0.0, result.getZ());
    }
}
