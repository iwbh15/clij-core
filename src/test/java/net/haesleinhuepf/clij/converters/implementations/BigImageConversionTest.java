package net.haesleinhuepf.clij.converters.implementations;

import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import org.junit.Test;

public class BigImageConversionTest {
    public static void main(String... args) {
        ImagePlus imp = NewImage.createByteImage("test", 1024, 1024, 2048, NewImage.FILL_RAMP);

        CLIJ clij = CLIJ.getInstance();

        ClearCLBuffer buffer = clij.push(imp);
        ClearCLBuffer plane = clij.create(new long[] {buffer.getWidth(), buffer.getHeight()}, buffer.getNativeType());

        clij.op().maximumZProjection(buffer, plane);

        ImagePlus maxProjImp = clij.pull(plane);

        buffer.close();
        plane.close();
    }
}
