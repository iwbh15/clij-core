package net.haesleinhuepf.clij.converters.implementations;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.NewImage;
import ij.plugin.Duplicator;
import ij.plugin.RGBStackMerge;
import ij.process.ByteProcessor;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * ClearCLBufferToImagePlusConverter
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJConverterPlugin.class)
public class ClearCLBufferToImagePlusConverter extends AbstractCLIJConverter<ClearCLBuffer, ImagePlus> {

    final long SMALL_IMAGE_SIZE = Integer.MAX_VALUE;

    @Override
    public ImagePlus convert(ClearCLBuffer source) {
        //long time = System.currentTimeMillis();

        int width = (int) source.getWidth();
        int height = (int) source.getHeight();
        int depth = (int) source.getDepth();

        int numberOfPixelsPerPlane = width * height;
        long numberOfPixels = (long)numberOfPixelsPerPlane * (long)depth;

        ImagePlus result = null;

        if (numberOfPixels > SMALL_IMAGE_SIZE) {
            result = convertBigImage(source, numberOfPixelsPerPlane, width, height, depth);
        } else {

            //ImagePlus[] slices = new ImagePlus[depth];
            //ImageStack stack = new ImageStack(width, height, depth);
            //long timeBufferCreationStarts = System.currentTimeMillis();
            //ClearCLBuffer plane = clij.createCLBuffer(new long[]{width, height}, source.getNativeType());
            //long timeBufferCreationEnds = System.currentTimeMillis();

            //long timeImageCreationStarts = 0;
            //long timeImageCreationEnds = 0;

            //long timeLoopStarts = 0;
            //long timeLoopEnds = 0;

            if (source.getNativeType() == NativeTypeEnum.UnsignedByte) {
                result = NewImage.createByteImage("slice", width, height, depth, NewImage.FILL_BLACK);
                byte[] array = new byte[(int) numberOfPixels];
                ByteBuffer buffer = ByteBuffer.wrap(array);
                source.writeTo(buffer, true);

                for (int z = 0; z < depth; z++) {
                    result.setSlice(z + 1);
                    byte[] sliceArray = (byte[]) result.getProcessor().getPixels();
                    System.arraycopy(array, z * numberOfPixelsPerPlane, sliceArray, 0, sliceArray.length);
                }
            /*
            for (int z = 0; z < depth; z++) {
                Kernels.copySlice(clij, source, plane, z);
                result.setSlice(z + 1);
                byte[] array = (byte[]) result.getProcessor().getPixels();
                ByteBuffer buffer = ByteBuffer.wrap(array);
                plane.writeTo(buffer, 0, numberOfPixelsPerPlane, true);
            }*/
            } else if (source.getNativeType() == NativeTypeEnum.UnsignedShort) {

                //timeImageCreationStarts = System.currentTimeMillis();
                result = NewImage.createShortImage("slice", width, height, depth, NewImage.FILL_BLACK);
                //timeImageCreationEnds = System.currentTimeMillis();

                //timeLoopStarts = System.currentTimeMillis();
                short[] array = new short[(int) numberOfPixels];
                ShortBuffer buffer = ShortBuffer.wrap(array);
                source.writeTo(buffer, true);

                for (int z = 0; z < depth; z++) {
                    result.setSlice(z + 1);
                    short[] sliceArray = (short[]) result.getProcessor().getPixels();
                    System.arraycopy(array, z * numberOfPixelsPerPlane, sliceArray, 0, sliceArray.length);
                }
            /*
            for (int z = 0; z < depth; z++) {
                Kernels.copySlice(clij, source, plane, z);
                result.setSlice(z + 1);
                short[] array = (short[]) result.getProcessor().getPixels();
                ShortBuffer buffer = ShortBuffer.wrap(array);
                plane.writeTo(buffer, 0, numberOfPixelsPerPlane, true);
            }
            */
                //timeLoopEnds = System.currentTimeMillis();


            } else if (source.getNativeType() == NativeTypeEnum.Float) {
                result = NewImage.createFloatImage("slice", width, height, depth, NewImage.FILL_BLACK);

                float[] array = new float[(int) numberOfPixels];
                FloatBuffer buffer = FloatBuffer.wrap(array);
                source.writeTo(buffer, true);

                for (int z = 0; z < depth; z++) {
                    result.setSlice(z + 1);
                    float[] sliceArray = (float[]) result.getProcessor().getPixels();
                    System.arraycopy(array, z * numberOfPixelsPerPlane, sliceArray, 0, sliceArray.length);
                }
            /*
            for (int z = 0; z < depth; z++) {
                Kernels.copySlice(clij, source, plane, z);
                result.setSlice(z + 1);
                float[] array = (float[]) result.getProcessor().getPixels();
                FloatBuffer buffer = FloatBuffer.wrap(array);
                plane.writeTo(buffer, 0, numberOfPixelsPerPlane, true);
            }
            */
            }
        }
        //plane.close();

        if (result == null) {
            result = convertLegacy(source);
        }

        //ImagePlus imp = new ImagePlus("image", stack);
        //stack
        //RGBStackMerge.mergeStacks()
        //System.out.println("BI conversion took " + (System.currentTimeMillis() - time));
        //System.out.println("Buffer creation took " + (timeBufferCreationEnds - timeBufferCreationStarts));
        //System.out.println("Image creation took " + (timeImageCreationEnds - timeImageCreationStarts));
        //System.out.println("Loop took " + (timeLoopEnds - timeLoopStarts));
        return result;

    }

    private ImagePlus convertBigImage(ClearCLBuffer source, int numberOfPixelsPerPlane, int width, int height, int depth) {
        ImagePlus result = null;

        if (source.getNativeType() == NativeTypeEnum.UnsignedByte) {
            result = NewImage.createByteImage("slice", width, height, depth, NewImage.FILL_BLACK);

            byte[] array = new byte[numberOfPixelsPerPlane];
            for (int z = 0; z < depth; z++) {
                System.out.println("z" + z);
                ByteBuffer buffer = ByteBuffer.wrap(array);
                source.writeTo(buffer, new long[]{0, 0, z}, new long[]{0,0,0}, new long[]{width, height}, true);

                result.setSlice(z + 1);
                byte[] sliceArray = (byte[]) result.getProcessor().getPixels();
                System.arraycopy(array, 0, sliceArray, 0, sliceArray.length);
            }
        } else if (source.getNativeType() == NativeTypeEnum.UnsignedShort) {
            result = NewImage.createShortImage("slice", width, height, depth, NewImage.FILL_BLACK);
            //timeImageCreationEnds = System.currentTimeMillis();

            //timeLoopStarts = System.currentTimeMillis();
            short[] array = new short[numberOfPixelsPerPlane];

            for (int z = 0; z < depth; z++) {
                ShortBuffer buffer = ShortBuffer.wrap(array);
                source.writeTo(buffer, new long[]{0, 0, z}, new long[]{0,0,0}, new long[]{width, height}, true);
                result.setSlice(z + 1);
                short[] sliceArray = (short[]) result.getProcessor().getPixels();
                System.arraycopy(array, 0, sliceArray, 0, sliceArray.length);
            }
        } else if (source.getNativeType() == NativeTypeEnum.Float) {
            result = NewImage.createFloatImage("slice", width, height, depth, NewImage.FILL_BLACK);

            float[] array = new float[numberOfPixelsPerPlane];

            for (int z = 0; z < depth; z++) {
                FloatBuffer buffer = FloatBuffer.wrap(array);
                source.writeTo(buffer, new long[]{0, 0, z}, new long[]{0,0,0}, new long[]{width, height}, true);

                result.setSlice(z + 1);
                float[] sliceArray = (float[]) result.getProcessor().getPixels();
                System.arraycopy(array, 0, sliceArray, 0, sliceArray.length);
            }
        }
        return result;
    }

    public ImagePlus convertLegacy(ClearCLBuffer source) {
        ClearCLBufferToRandomAccessibleIntervalConverter cclbtraic = new ClearCLBufferToRandomAccessibleIntervalConverter();
        cclbtraic.setCLIJ(clij);
        RandomAccessibleInterval rai = cclbtraic.convert(source);
        return new Duplicator().run(ImageJFunctions.wrap(rai, "" + rai));
    }

    @Override
    public Class<ClearCLBuffer> getSourceType() {
        return ClearCLBuffer.class;
    }

    @Override
    public Class<ImagePlus> getTargetType() {
        return ImagePlus.class;
    }
}
