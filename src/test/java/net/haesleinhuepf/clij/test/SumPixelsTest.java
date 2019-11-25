package net.haesleinhuepf.clij.test;

import ij.ImageJ;
import ij.gui.WaitForUserDialog;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * SumPixelsTest
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 *         September 2019
 */
public class SumPixelsTest {
    @Test
    public void sumPixelsTest() {

        int w = 3;
        int h = 3;
        int d = 3;

        byte[] arr = new byte[w*h*d];
        double sumRef = 0;
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 100;
            sumRef += arr[i];
        }
        Img<UnsignedByteType> a = ArrayImgs.unsignedBytes(arr, new long[]{w,h,d});

        CLIJ clij = CLIJ.getInstance();

        ClearCLBuffer clA = clij.push(a);

        double sum = clij.op().sumPixels(clA);
        System.out.println("SumRef " + sumRef);
        System.out.println("Sum " + sum);
        assertEquals(sumRef, sum, 0);
    }
}
