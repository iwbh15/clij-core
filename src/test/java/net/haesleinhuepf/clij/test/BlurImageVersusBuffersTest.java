package net.haesleinhuepf.clij.test;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.util.ElapsedTime;
import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import org.junit.Test;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.sigmaToKernelSize;

public class BlurImageVersusBuffersTest {
    @Test
    public void compareImagesAndBufferProcessingTime(){
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImage = TestUtilities.getRandomImage(100, 100, 100, 32, 0, 100);

        ClearCLBuffer inputBuffer = clij.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer outputBuffer = clij.createCLBuffer(inputBuffer);

        ClearCLImage inputImage = clij.convert(testImage, ClearCLImage.class);
        ClearCLImage outputImage = clij.createCLImage(inputImage);

        float sigma = 5;

        for (int i = 0; i < 3; i++) {
            ElapsedTime.measureForceOutput("blurSep buffer", () -> {
                Kernels.blur(clij, inputBuffer, outputBuffer, sigma, sigma, sigma);
            });
            ElapsedTime.measureForceOutput("blurSep image", () -> {
                Kernels.blur(clij, inputImage, outputImage, sigma, sigma, sigma);
            });
        }

        inputBuffer.close();
        inputImage.close();
        outputBuffer.close();
        outputImage.close();
        IJ.exit();
        clij.close();
    }
}
