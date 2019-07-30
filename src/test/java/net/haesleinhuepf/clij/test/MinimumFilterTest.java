package net.haesleinhuepf.clij.test;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import org.junit.Test;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.radiusToKernelSize;
import static org.junit.Assert.assertTrue;

public class MinimumFilterTest {
    private static boolean debug = false;

    @Test
    public void testFloatMinimumSphere2D() {
        ImagePlus imp = NewImage.createFloatImage("image", 512, 512, 1, NewImage.FILL_BLACK);
        IJ.run(imp,"Add...", "value=1");
        imp.getProcessor().setf(256,256,0);

        checkMinimumSphereFilter(imp, 25);
    }

    @Test
    public void testByteMinimumSphere2D() {
        ImagePlus imp = NewImage.createByteImage("image", 512, 512, 1, NewImage.FILL_BLACK);
        IJ.run(imp,"Add...", "value=1");
        imp.getProcessor().set(256,256,0);

        checkMinimumSphereFilter(imp, 25);
    }

    @Test
    public void testShortMinimumSphere2D() {
        ImagePlus imp = NewImage.createShortImage("image", 512, 512, 1, NewImage.FILL_BLACK);
        IJ.run(imp,"Add...", "value=1");
        imp.getProcessor().set(256,256,0);

        checkMinimumSphereFilter(imp, 25);
    }

    @Test
    public void testFloatMinimumSphere3D() {
        ImagePlus imp = NewImage.createFloatImage("image", 512, 512, 10, NewImage.FILL_BLACK);
        for (int z = 0; z < imp.getNSlices(); z++) {
            imp.setZ(z + 1);
            IJ.run(imp, "Add...", "value=1");
        }
        imp.setZ(imp.getNSlices() / 2);
        imp.getProcessor().setf(256,256,0);

        checkMinimumSphereFilter(imp, 25);
    }

    @Test
    public void testByteMinimumSphere3D() {
        ImagePlus imp = NewImage.createByteImage("image", 512, 512, 10, NewImage.FILL_BLACK);
        for (int z = 0; z < imp.getNSlices(); z++) {
            imp.setZ(z + 1);
            IJ.run(imp, "Add...", "value=1");
        }
        imp.setZ(imp.getNSlices() / 2);
        imp.getProcessor().set(256,256,0);

        checkMinimumSphereFilter(imp, 25);
    }

    @Test
    public void testShortMinimumSphere3D() {
        ImagePlus imp = NewImage.createShortImage("image", 512, 512, 10, NewImage.FILL_BLACK);
        for (int z = 0; z < imp.getNSlices(); z++) {
            imp.setZ(z + 1);
            IJ.run(imp, "Add...", "value=1");
        }
        imp.setZ(imp.getNSlices() / 2);
        imp.getProcessor().set(256,256,0);

        checkMinimumSphereFilter(imp, 25);
    }


    private void checkMinimumSphereFilter(ImagePlus imp, int maxRadius) {
        CLIJ clij = CLIJ.getInstance();
        ClearCLBuffer input = clij.push(imp);
        ClearCLBuffer output = clij.create(input);

        for (int radius = 1; radius <= maxRadius; radius += 5) {

            int kernelSize = radiusToKernelSize(radius);

            if (imp.getNSlices() > 1) {
                clij.op().minimumSphere(input, output, kernelSize, kernelSize, kernelSize);
            } else {
                clij.op().minimumSphere(input, output, kernelSize, kernelSize);
            }
            ImagePlus result = clij.pull(output);

            if (imp.getNSlices() > 1) {
                result.setZ(result.getNSlices() / 2);
            }

            if (debug) {
                result.show();
                result.setDisplayRange(0, 1);
                result.updateAndDraw();

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // check result
            int numberOfWhitePixels = (int) (result.getStatistics().mean * result.getWidth() * result.getHeight());

            int numberOfAllPixels = result.getWidth() * result.getHeight();
            int numberOfBlackPixels = numberOfAllPixels - numberOfWhitePixels;

            System.out.println("White: " + numberOfWhitePixels);
            System.out.println("Black: " + numberOfBlackPixels);

            assertTrue(numberOfWhitePixels > 0);
            assertTrue(numberOfBlackPixels > 1);
        }

        input.close();
        output.close();

    }

}
