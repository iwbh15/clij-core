package net.haesleinhuepf.clij.converters.implementations;

import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConverterOverflowTest {
    @Test
    public void test16to8bitConversion() {
        CLIJ clij = CLIJ.getInstance();

        ClearCLBuffer buffer = clij.create(new long[]{1,1,1}, NativeTypeEnum.UnsignedShort);
        ClearCLBuffer converted = clij.create(buffer.getDimensions(), NativeTypeEnum.UnsignedByte);

        clij.op().set(buffer, 255f);

        clij.op().copy(buffer, converted);

        ImagePlus imp = clij.pull(converted);

        assertEquals(imp.getProcessor().get(0,0), 255, 0);

        assertEquals(clij.op().sumPixels(converted), 255, 0);

        clij.op().addImageAndScalar(buffer, converted, 1.0f);

        imp = clij.pull(converted);

        assertEquals(imp.getProcessor().get(0,0), 255, 0);

        assertEquals(clij.op().sumPixels(converted), 255, 0);

        buffer.close();
        converted.close();
    }

    @Test
    public void test32to8bitConversion() {
        CLIJ clij = CLIJ.getInstance();

        ClearCLBuffer buffer = clij.create(new long[]{1,1,1}, NativeTypeEnum.Float);
        ClearCLBuffer converted = clij.create(buffer.getDimensions(), NativeTypeEnum.UnsignedByte);

        clij.op().set(buffer, 255f);

        clij.op().copy(buffer, converted);

        ImagePlus imp = clij.pull(converted);

        assertEquals(imp.getProcessor().get(0,0), 255, 0);

        assertEquals(clij.op().sumPixels(converted), 255, 0);

        clij.op().addImageAndScalar(buffer, converted, 1.0f);

        imp = clij.pull(converted);

        assertEquals(imp.getProcessor().get(0,0), 255, 0);

        assertEquals(clij.op().sumPixels(converted), 255, 0);

        buffer.close();
        converted.close();
    }

}
