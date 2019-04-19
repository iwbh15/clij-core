package net.haesleinhuepf.clij.test;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.type.numeric.real.FloatType;
import org.junit.Test;

import java.lang.reflect.Array;

import static junit.framework.TestCase.assertTrue;

/**
 * AffineTransformTest
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 02 2019
 */
public class AffineTransformTest {
    @Test
    public void testTranslateBuffer() {
        float[] imageFlt = {
                1, 4,
                6, 9
        };

        float[] referenceImageFlt = {
                0, 0, 0, 0, 1, 4,
                0, 0, 0, 0, 6, 9
        };

        Img<FloatType> img = ArrayImgs.floats(
                imageFlt, 2, 2
        );

        Img<FloatType> referenceImg = ArrayImgs.floats(
                referenceImageFlt, 6, 2
        );

        CLIJ clij = CLIJ.getInstance();
        ClearCLBuffer input = clij.convert(img, ClearCLBuffer.class);
        ClearCLBuffer reference = clij.convert(referenceImg, ClearCLBuffer.class);

        ClearCLBuffer output = clij.create(reference);

        AffineTransform3D at = new AffineTransform3D();
        at.translate(4, 0, 0);

        Kernels.affineTransform(clij, input, output, at);

        TestUtilities.printBuffer(clij, output);

        assertTrue(TestUtilities.clBuffersEqual(clij, output, reference, 0));
    }

    @Test
    public void testTranslateImage() {
        float[] imageFlt = {
                1, 4,
                6, 9
        };

        float[] referenceImageFlt = {
                0, 0, 0, 0, 1, 4,
                0, 0, 0, 0, 6, 9
        };

        Img<FloatType> img = ArrayImgs.floats(
                imageFlt, 2, 2
        );

        Img<FloatType> referenceImg = ArrayImgs.floats(
                referenceImageFlt, 6, 2
        );

        CLIJ clij = CLIJ.getInstance();
        ClearCLImage input = clij.convert(img, ClearCLImage.class);
        ClearCLImage reference = clij.convert(referenceImg, ClearCLImage.class);

        ClearCLImage output = clij.create(reference);

        AffineTransform3D at = new AffineTransform3D();
        at.translate(4, 0, 0);

        Kernels.affineTransform(clij, input, output, at);

        ClearCLBuffer bufferOutput = clij.convert(output, ClearCLBuffer.class);
        ClearCLBuffer referenceOutput = clij.convert(reference, ClearCLBuffer.class);

        TestUtilities.printBuffer(clij, bufferOutput);

        assertTrue(TestUtilities.clBuffersEqual(clij, bufferOutput, referenceOutput, 0));
    }
}
