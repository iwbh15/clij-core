package net.haesleinhuepf.clij.test;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.WaitForUserDialog;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import org.junit.Test;

public class BlurUnsignedByteImageTest {
    @Test
    public void testBlur(){
        ImagePlus imp = IJ.openImage("src/main/resources/blobs.tif");

        CLIJ clij = CLIJ.getInstance();
        ClearCLImage input = clij.convert(imp, ClearCLImage.class);
        ClearCLBuffer output1 = clij.createCLBuffer(input.getDimensions(), input.getNativeType());
        ClearCLBuffer output2 = clij.createCLBuffer(input.getDimensions(), input.getNativeType());

        clij.op().blur(input, output1, 10f, 10f);
        clij.op().blur(input, output2, 10f, 10f, 0f);

        TestUtilities.clBuffersEqual(clij, output1, output2, 1);

        input.close();
        output1.close();
        output2.close();
    }
}
