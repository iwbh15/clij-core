package net.haesleinhuepf.clij.kernels;

import ij.ImagePlus;
import ij.process.AutoThresholder;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.utilities.AffineTransform;
import net.haesleinhuepf.clij.utilities.CLKernelExecutor;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.realtransform.AffineTransform2D;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;

import java.nio.FloatBuffer;
import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.*;


/**
 * This class contains convenience access functions for OpenCL based
 * image processing.
 * <p>
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * March 2018
 */
public class Kernels {
    public static boolean absolute(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }

        return clij.execute(Kernels.class,"math.cl", "absolute_" + src.getDimension() + "d", parameters);
    }

    public static boolean absolute(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }

        return clij.execute(Kernels.class, "math.cl", "absolute_" + src.getDimension() + "d", parameters);
    }


    public static boolean addImages(CLIJ clij, ClearCLImage src, ClearCLImage src1, ClearCLImage dst) {
        assertDifferent(src, dst);
        assertDifferent(src1, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImages)");
        }
        return clij.execute(Kernels.class, "math.cl", "addPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean addImages(CLIJ clij, ClearCLBuffer src, ClearCLBuffer src1, ClearCLBuffer dst) {
        assertDifferent(src, dst);
        assertDifferent(src1, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImages)");
        }
        return clij.execute(Kernels.class, "math.cl", "addPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean addImageAndScalar(CLIJ clij, ClearCLImage src, ClearCLImage dst, Float scalar) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("scalar", scalar);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }

        return clij.execute(Kernels.class, "math.cl", "addScalar_" + src.getDimension() + "d", parameters);
    }


    public static boolean addImageAndScalar(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float scalar) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("scalar", scalar);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }

        return clij.execute(Kernels.class, "math.cl", "addScalar_" + src.getDimension() + "d", parameters);
    }

    public static boolean addImagesWeighted(CLIJ clij, ClearCLImage src, ClearCLImage src1, ClearCLImage dst, Float factor, Float factor1) {
        assertDifferent(src, dst);
        assertDifferent(src1, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("factor", factor);
        parameters.put("factor1", factor1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }
        return clij.execute(Kernels.class, "math.cl", "addWeightedPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean addImagesWeighted(CLIJ clij, ClearCLBuffer src, ClearCLBuffer src1, ClearCLBuffer dst, Float factor, Float factor1) {
        assertDifferent(src, dst);
        assertDifferent(src1, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("factor", factor);
        parameters.put("factor1", factor1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }
        return clij.execute(Kernels.class, "math.cl", "addWeightedPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean affineTransform2D(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, float[] matrix) {
        assertDifferent(src, dst);

        ClearCLBuffer matrixCl = clij.createCLBuffer(new long[]{matrix.length, 1, 1}, NativeTypeEnum.Float);

        FloatBuffer buffer = FloatBuffer.wrap(matrix);
        matrixCl.readFrom(buffer, true);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("input", src);
        parameters.put("output", dst);
        parameters.put("mat", matrixCl);

        boolean result = clij.execute(Kernels.class, "affineTransforms2D.cl", "affine_2D", parameters);

        matrixCl.close();

        return result;
    }

    public static boolean affineTransform2D(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, AffineTransform2D at) {
        assertDifferent(src, dst);

        at = at.inverse();
        float[] matrix = AffineTransform.matrixToFloatArray2D(at);
        return affineTransform2D(clij, src, dst, matrix);
    }

    public static boolean affineTransform2D(CLIJ clij, ClearCLImage src, ClearCLImage dst, float[] matrix) {
        assertDifferent(src, dst);

        ClearCLBuffer matrixCl = clij.createCLBuffer(new long[]{matrix.length, 1, 1}, NativeTypeEnum.Float);

        FloatBuffer buffer = FloatBuffer.wrap(matrix);
        matrixCl.readFrom(buffer, true);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("input", src);
        parameters.put("output", dst);
        parameters.put("mat", matrixCl);

        boolean result = clij.execute(Kernels.class, "affineTransforms_interpolate2D.cl", "affine_interpolate2D", parameters);

        matrixCl.close();

        return result;
    }

    public static boolean affineTransform2D(CLIJ clij, ClearCLImage src, ClearCLImage dst, AffineTransform2D at) {
        assertDifferent(src, dst);

        at = at.inverse();
        float[] matrix = AffineTransform.matrixToFloatArray2D(at);
        return affineTransform2D(clij, src, dst, matrix);
    }

    @Deprecated // use affineTransform2D or affineTransform3D instead
    public static boolean affineTransform(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, float[] matrix) {
        return affineTransform3D(clij, src, dst, matrix);
    }

    @Deprecated // use affineTransform2D or affineTransform3D instead
    public static boolean affineTransform(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, AffineTransform3D at) {
        return affineTransform3D(clij, src, dst, at);
    }

    @Deprecated // use affineTransform2D or affineTransform3D instead
    public static boolean affineTransform(CLIJ clij, ClearCLImage src, ClearCLImage dst, float[] matrix) {
        return affineTransform3D(clij, src, dst, matrix);
    }

    @Deprecated // use affineTransform2D or affineTransform3D instead
    public static boolean affineTransform(CLIJ clij, ClearCLImage src, ClearCLImage dst, AffineTransform3D at) {
        return affineTransform3D(clij, src, dst, at);
    }

    public static boolean affineTransform3D(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, float[] matrix) {
        assertDifferent(src, dst);

        ClearCLBuffer matrixCl = clij.createCLBuffer(new long[]{matrix.length, 1, 1}, NativeTypeEnum.Float);

        FloatBuffer buffer = FloatBuffer.wrap(matrix);
        matrixCl.readFrom(buffer, true);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("input", src);
        parameters.put("output", dst);
        parameters.put("mat", matrixCl);

        boolean result = clij.execute(Kernels.class, "affineTransforms.cl", "affine", parameters);

        matrixCl.close();

        return result;
    }

    public static boolean affineTransform3D(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, AffineTransform3D at) {
        assertDifferent(src, dst);

        at = at.inverse();
        float[] matrix = AffineTransform.matrixToFloatArray(at);
        return affineTransform3D(clij, src, dst, matrix);
    }

    public static boolean affineTransform3D(CLIJ clij, ClearCLImage src, ClearCLImage dst, float[] matrix) {
        assertDifferent(src, dst);

        ClearCLBuffer matrixCl = clij.createCLBuffer(new long[]{matrix.length, 1, 1}, NativeTypeEnum.Float);

        FloatBuffer buffer = FloatBuffer.wrap(matrix);
        matrixCl.readFrom(buffer, true);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("input", src);
        parameters.put("output", dst);
        parameters.put("mat", matrixCl);

        boolean result = clij.execute(Kernels.class, "affineTransforms_interpolate.cl", "affine_interpolate", parameters);

        matrixCl.close();

        return result;
    }

    public static boolean affineTransform3D(CLIJ clij, ClearCLImage src, ClearCLImage dst, AffineTransform3D at) {
        assertDifferent(src, dst);

        at = at.inverse();
        float[] matrix = AffineTransform.matrixToFloatArray(at);
        return affineTransform3D(clij, src, dst, matrix);
    }

    public static boolean applyVectorfield(CLIJ clij, ClearCLImage src, ClearCLImage vectorX, ClearCLImage vectorY, ClearCLImage dst) {
        assertDifferent(src, dst);
        assertDifferent(vectorX, dst);
        assertDifferent(vectorY, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("vectorX", vectorX);
        parameters.put("vectorY", vectorY);

        boolean result = clij.execute(Kernels.class, "deform_interpolate.cl", "deform_2d_interpolate", parameters);
        return result;
    }

    public static boolean applyVectorfield(CLIJ clij, ClearCLImage src, ClearCLImage vectorX, ClearCLImage vectorY, ClearCLImage vectorZ, ClearCLImage dst) {
        assertDifferent(src, dst);
        assertDifferent(vectorX, dst);
        assertDifferent(vectorY, dst);
        assertDifferent(vectorZ, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("vectorX", vectorX);
        parameters.put("vectorY", vectorY);
        parameters.put("vectorZ", vectorZ);

        boolean result = clij.execute(Kernels.class, "deform_interpolate.cl", "deform_3d_interpolate", parameters);
        return result;
    }

    public static boolean applyVectorfield(CLIJ clij, ClearCLBuffer src, ClearCLBuffer vectorX, ClearCLBuffer vectorY, ClearCLBuffer dst) {
        assertDifferent(src, dst);
        assertDifferent(vectorX, dst);
        assertDifferent(vectorY, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("vectorX", vectorX);
        parameters.put("vectorY", vectorY);

        boolean result = clij.execute(Kernels.class, "deform.cl", "deform_2d", parameters);
        return result;
    }

    public static boolean applyVectorfield(CLIJ clij, ClearCLBuffer src, ClearCLBuffer vectorX, ClearCLBuffer vectorY, ClearCLBuffer vectorZ, ClearCLBuffer dst) {
        assertDifferent(src, dst);
        assertDifferent(vectorX, dst);
        assertDifferent(vectorY, dst);
        assertDifferent(vectorZ, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("vectorX", vectorX);
        parameters.put("vectorY", vectorY);
        parameters.put("vectorZ", vectorZ);

        boolean result = clij.execute(Kernels.class, "deform.cl", "deform_3d", parameters);
        return result;
    }

    public static boolean automaticThreshold(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, String userSelectedMethod) {
        assertDifferent(src, dst);

        Float minimumGreyValue = 0f;
        Float maximumGreyValue = 0f;
        Integer numberOfBins = 256;

        if (src.getNativeType() == NativeTypeEnum.UnsignedByte) {
            minimumGreyValue = 0f;
            maximumGreyValue = 255f;
        } else {
            minimumGreyValue = null;
            maximumGreyValue = null;
        }

        return automaticThreshold(clij, src, dst, userSelectedMethod, minimumGreyValue, maximumGreyValue, 256);
    }

    public static boolean automaticThreshold(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, String userSelectedMethod, Float minimumGreyValue, Float maximumGreyValue, Integer numberOfBins) {
        assertDifferent(src, dst);

        if (minimumGreyValue == null)
        {
            minimumGreyValue = new Double(Kernels.minimumOfAllPixels(clij, src)).floatValue();
        }

        if (maximumGreyValue == null)
        {
            maximumGreyValue = new Double(Kernels.maximumOfAllPixels(clij, src)).floatValue();
        }


        ClearCLBuffer histogram = clij.createCLBuffer(new long[]{numberOfBins,1,1}, NativeTypeEnum.Float);
        Kernels.fillHistogram(clij, src, histogram, minimumGreyValue, maximumGreyValue);
        //releaseBuffers(args);

        //System.out.println("CL sum " + clij.op().sumPixels(histogram));

        // the histogram is written in args[1] which is supposed to be a one-dimensional image
        ImagePlus histogramImp = clij.convert(histogram, ImagePlus.class);
        histogram.close();

        // convert histogram
        float[] determinedHistogram = (float[])(histogramImp.getProcessor().getPixels());
        int[] convertedHistogram = new int[determinedHistogram.length];

        long sum = 0;
        for (int i = 0; i < determinedHistogram.length; i++) {
            convertedHistogram[i] = (int)determinedHistogram[i];
            sum += convertedHistogram[i];
        }
        //System.out.println("Sum: " + sum);


        String method = "Default";

        for (String choice : AutoThresholder.getMethods()) {
            if (choice.toLowerCase().compareTo(userSelectedMethod.toLowerCase()) == 0) {
                method = choice;
            }
        }
        //System.out.println("Method: " + method);

        float threshold = new AutoThresholder().getThreshold(method, convertedHistogram);

        // math source https://github.com/imagej/ImageJA/blob/master/src/main/java/ij/process/ImageProcessor.java#L692
        threshold = minimumGreyValue + ((threshold + 1.0f)/255.0f)*(maximumGreyValue-minimumGreyValue);

        //System.out.println("Threshold: " + threshold);

        Kernels.threshold(clij, src, dst, threshold);

        return true;
    }


    public static boolean argMaximumZProjection(CLIJ clij, ClearCLImage src, ClearCLImage dst_max, ClearCLImage dst_arg) {
        assertDifferent(src, dst_max);
        assertDifferent(src, dst_arg);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_max", dst_max);
        parameters.put("dst_arg", dst_arg);

        return clij.execute(Kernels.class, "projections.cl", "arg_max_project_3d_2d", parameters);
    }

    public static boolean argMaximumZProjection(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst_max, ClearCLBuffer dst_arg) {
        assertDifferent(src, dst_max);
        assertDifferent(src, dst_arg);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_max", dst_max);
        parameters.put("dst_arg", dst_arg);

        return clij.execute(Kernels.class, "projections.cl", "arg_max_project_3d_2d", parameters);
    }

    public static boolean binaryAnd(CLIJ clij, ClearCLImage src1, ClearCLImage src2, ClearCLImage dst) {
        assertDifferent(src1, dst);
        assertDifferent(src2, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("src2", src2);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "binaryProcessing.cl", "binary_and_" + src1.getDimension() + "d", parameters);
    }

    public static boolean binaryAnd(CLIJ clij, ClearCLBuffer src1, ClearCLBuffer src2, ClearCLBuffer dst) {
        assertDifferent(src1, dst);
        assertDifferent(src2, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("src2", src2);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "binaryProcessing.cl", "binary_and_" + src1.getDimension() + "d", parameters);
    }

    public static boolean binaryXOr(CLIJ clij, ClearCLImage src1, ClearCLImage src2, ClearCLImage dst) {
        assertDifferent(src1, dst);
        assertDifferent(src2, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("src2", src2);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "binaryProcessing.cl", "binary_xor_" + src1.getDimension() + "d", parameters);
    }

    public static boolean binaryXOr(CLIJ clij, ClearCLBuffer src1, ClearCLBuffer src2, ClearCLBuffer dst) {
        assertDifferent(src1, dst);
        assertDifferent(src2, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("src2", src2);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "binaryProcessing.cl", "binary_xor_" + src1.getDimension() + "d", parameters);
    }

    public static boolean binaryNot(CLIJ clij, ClearCLImage src1, ClearCLImage dst) {
        assertDifferent(src1, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "binaryProcessing.cl", "binary_not_" + src1.getDimension() + "d", parameters);
    }

    public static boolean binaryNot(CLIJ clij, ClearCLBuffer src1, ClearCLBuffer dst) {
        assertDifferent(src1, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "binaryProcessing.cl", "binary_not_" + src1.getDimension() + "d", parameters);
    }

    public static boolean binaryOr(CLIJ clij, ClearCLImage src1, ClearCLImage src2, ClearCLImage dst) {
        assertDifferent(src1, dst);
        assertDifferent(src2, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("src2", src2);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "binaryProcessing.cl", "binary_or_" + src1.getDimension() + "d", parameters);
    }

    public static boolean binaryOr(CLIJ clij, ClearCLBuffer src1, ClearCLBuffer src2, ClearCLBuffer dst) {
        assertDifferent(src1, dst);
        assertDifferent(src2, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("src2", src2);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "binaryProcessing.cl", "binary_or_" + src1.getDimension() + "d", parameters);
    }

    public static boolean blur(CLIJ clij, ClearCLImage src, ClearCLImage dst, Float blurSigmaX, Float blurSigmaY) {
        return executeSeparableKernel(clij, src, dst, "blur.cl", "gaussian_blur_sep_image" + src.getDimension() + "d", sigmaToKernelSize(blurSigmaX), sigmaToKernelSize(blurSigmaY), sigmaToKernelSize(0), blurSigmaX, blurSigmaY, 0, src.getDimension());
    }

    public static boolean blur(CLIJ clij, ClearCLImage src, ClearCLBuffer dst, Float blurSigmaX, Float blurSigmaY) {
        return executeSeparableKernel(clij, src, dst, "blur.cl", "gaussian_blur_sep_image" + src.getDimension() + "d", sigmaToKernelSize(blurSigmaX), sigmaToKernelSize(blurSigmaY), sigmaToKernelSize(0), blurSigmaX, blurSigmaY, 0, src.getDimension());
    }

    public static boolean blur(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float blurSigmaX, Float blurSigmaY) {
        return executeSeparableKernel(clij, src, dst, "blur.cl", "gaussian_blur_sep_image" + src.getDimension() + "d", sigmaToKernelSize(blurSigmaX), sigmaToKernelSize(blurSigmaY), sigmaToKernelSize(0), blurSigmaX, blurSigmaY, 0, src.getDimension());
    }

    public static boolean blur(CLIJ clij, ClearCLImage src, ClearCLImage dst, Float blurSigmaX, Float blurSigmaY, Float blurSigmaZ) {
        return executeSeparableKernel(clij, src, dst, "blur.cl", "gaussian_blur_sep_image" + src.getDimension() + "d", sigmaToKernelSize(blurSigmaX), sigmaToKernelSize(blurSigmaY), sigmaToKernelSize(blurSigmaZ), blurSigmaX, blurSigmaY, blurSigmaZ, src.getDimension());
    }

    public static boolean blur(CLIJ clij, ClearCLImage src, ClearCLBuffer dst, Float blurSigmaX, Float blurSigmaY, Float blurSigmaZ) {
        return executeSeparableKernel(clij, src, dst, "blur.cl", "gaussian_blur_sep_image" + src.getDimension() + "d", sigmaToKernelSize(blurSigmaX), sigmaToKernelSize(blurSigmaY), sigmaToKernelSize(blurSigmaZ), blurSigmaX, blurSigmaY, blurSigmaZ, src.getDimension());
    }

    public static boolean blur(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float blurSigmaX, Float blurSigmaY, Float blurSigmaZ) {
        return executeSeparableKernel(clij, src, dst, "blur.cl", "gaussian_blur_sep_image" + src.getDimension() + "d", sigmaToKernelSize(blurSigmaX), sigmaToKernelSize(blurSigmaY), sigmaToKernelSize(blurSigmaZ), blurSigmaX, blurSigmaY, blurSigmaZ, src.getDimension());
    }

    public static boolean countNonZeroPixelsLocally(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radiusX, Integer radiusY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", radiusToKernelSize(radiusX));
        parameters.put("Ny", radiusToKernelSize(radiusY));
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "binaryCounting.cl", "count_nonzero_image2d", parameters);
    }

    public static boolean countNonZeroPixelsLocallySliceBySlice(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radiusX, Integer radiusY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", radiusToKernelSize(radiusX));
        parameters.put("Ny", radiusToKernelSize(radiusY));
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "binaryCounting.cl", "count_nonzero_slicewise_image3d", parameters);
    }

    public static boolean countNonZeroVoxelsLocally(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radiusX, Integer radiusY, Integer radiusZ) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", radiusToKernelSize(radiusX));
        parameters.put("Ny", radiusToKernelSize(radiusY));
        parameters.put("Nz", radiusToKernelSize(radiusZ));
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "binaryCounting.cl", "count_nonzero_image3d", parameters);
    }

    public static boolean countNonZeroPixelsLocally(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radiusX, Integer radiusY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", radiusToKernelSize(radiusX));
        parameters.put("Ny", radiusToKernelSize(radiusY));
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "binaryCounting.cl", "count_nonzero_image2d", parameters);
    }

    public static boolean countNonZeroPixelsLocallySliceBySlice(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radiusX, Integer radiusY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", radiusToKernelSize(radiusX));
        parameters.put("Ny", radiusToKernelSize(radiusY));
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "binaryCounting.cl", "count_nonzero_slicewise_image3d", parameters);
    }

    public static boolean countNonZeroVoxelsLocally(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radiusX, Integer radiusY, Integer radiusZ) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", radiusToKernelSize(radiusX));
        parameters.put("Ny", radiusToKernelSize(radiusY));
        parameters.put("Nz", radiusToKernelSize(radiusZ));
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "binaryCounting.cl", "count_nonzero_image3d", parameters);
    }

    private static boolean executeSeparableKernel(CLIJ clij, Object src, Object dst, String clFilename, String kernelname, int kernelSizeX, int kernelSizeY, int kernelSizeZ, float blurSigmaX, float blurSigmaY, float blurSigmaZ, long dimensions) {
        assertDifferent(src, dst);

        int[] n = new int[]{kernelSizeX, kernelSizeY, kernelSizeZ};
        float[] blurSigma = new float[]{blurSigmaX, blurSigmaY, blurSigmaZ};

        Object temp1;
        Object temp2;
        if (src instanceof ClearCLBuffer) {
            temp1 = clij.create(((ClearCLBuffer) src).getDimensions(), NativeTypeEnum.Float);
            temp2 = clij.create(((ClearCLBuffer) src).getDimensions(), NativeTypeEnum.Float);
        } else if (src instanceof ClearCLImage) {
            temp1 = clij.create(((ClearCLImage) src).getDimensions(), ImageChannelDataType.Float);
            temp2 = clij.create(((ClearCLImage) src).getDimensions(), ImageChannelDataType.Float);
        } else {
            throw new IllegalArgumentException("Error: Wrong type of images in blurFast");
        }

        HashMap<String, Object> parameters = new HashMap<>();

        if (blurSigma[0] > 0) {
            parameters.clear();
            parameters.put("N", n[0]);
            parameters.put("s", blurSigma[0]);
            parameters.put("dim", 0);
            parameters.put("src", src);
            if (dimensions == 2) {
                parameters.put("dst", temp1);
            } else {
                parameters.put("dst", temp2);
            }
            clij.execute(Kernels.class, clFilename, kernelname, parameters);
        } else {
            if (dimensions == 2) {
                Kernels.copyInternal(clij, src, temp1, 2, 2);
            } else {
                Kernels.copyInternal(clij, src, temp2, 3, 3);
            }
        }

        if (blurSigma[1] > 0) {
            parameters.clear();
            parameters.put("N", n[1]);
            parameters.put("s", blurSigma[1]);
            parameters.put("dim", 1);
            if (dimensions == 2) {
                parameters.put("src", temp1);
                parameters.put("dst", dst);
            } else {
                parameters.put("src", temp2);
                parameters.put("dst", temp1);
            }
            clij.execute(Kernels.class, clFilename, kernelname, parameters);
        } else {
            if (dimensions == 2) {
                Kernels.copyInternal(clij, temp1, dst, 2, 2);
            } else {
                Kernels.copyInternal(clij, temp2, temp1, 3, 3);
            }
        }

        if (dimensions == 3) {
            if (blurSigma[2] > 0) {
                parameters.clear();
                parameters.put("N", n[2]);
                parameters.put("s", blurSigma[2]);
                parameters.put("dim", 2);
                parameters.put("src", temp1);
                parameters.put("dst", dst);
                clij.execute(Kernels.class,
                        clFilename,
                        kernelname,
                        parameters);
            } else {
                Kernels.copyInternal(clij, temp1, dst,3, 3);
            }
        }

        if (temp1 instanceof ClearCLBuffer) {
            ((ClearCLBuffer) temp1).close();
            ((ClearCLBuffer) temp2).close();
        } else if (temp1 instanceof ClearCLImage) {
            ((ClearCLImage) temp1).close();
            ((ClearCLImage) temp2).close();
        }

        return true;
    }

    public static boolean blurSliceBySlice(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY, Float sigmaX, Float sigmaY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("sx", sigmaX);
        parameters.put("sy", sigmaY);
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "blur.cl", "gaussian_blur_slicewise_image3d", parameters);
    }

    public static boolean blurSliceBySlice(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, int kernelSizeX, int kernelSizeY, float sigmaX, float sigmaY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("sx", sigmaX);
        parameters.put("sy", sigmaY);
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "blur.cl", "gaussian_blur_slicewise_image3d", parameters);
    }

    public static double[] centerOfMass(CLIJ clij, ClearCLBuffer input) {
        ClearCLBuffer multipliedWithCoordinate = clij.create(input.getDimensions(), NativeTypeEnum.Float);
        double sum = clij.op().sumPixels(input);
        double[] resultCenterOfMass;
        if (input.getDimension() > 2L && input.getDepth() > 1L) {
            resultCenterOfMass = new double[3];
        } else {
            resultCenterOfMass = new double[2];
        }

        clij.op().multiplyImageAndCoordinate(input, multipliedWithCoordinate, 0);
        double sumX = clij.op().sumPixels(multipliedWithCoordinate);
        resultCenterOfMass[0] = sumX / sum;
        clij.op().multiplyImageAndCoordinate(input, multipliedWithCoordinate, 1);
        double sumY = clij.op().sumPixels(multipliedWithCoordinate);
        resultCenterOfMass[1] = sumY / sum;
        if (input.getDimension() > 2L && input.getDepth() > 1L) {
            clij.op().multiplyImageAndCoordinate(input, multipliedWithCoordinate, 2);
            double sumZ = clij.op().sumPixels(multipliedWithCoordinate);
            resultCenterOfMass[2] = sumZ / sum;
        }

        multipliedWithCoordinate.close();
        return resultCenterOfMass;
    }


    public static double[] centerOfMass(CLIJ clij, ClearCLImage input) {
        ClearCLImage multipliedWithCoordinate = clij.create(input.getDimensions(), ImageChannelDataType.Float);
        double sum = clij.op().sumPixels(input);
        double[] resultCenterOfMass;
        if (input.getDimension() > 2L && input.getDepth() > 1L) {
            resultCenterOfMass = new double[3];
        } else {
            resultCenterOfMass = new double[2];
        }

        clij.op().multiplyImageAndCoordinate(input, multipliedWithCoordinate, 0);
        double sumX = clij.op().sumPixels(multipliedWithCoordinate);
        resultCenterOfMass[0] = sumX / sum;
        clij.op().multiplyImageAndCoordinate(input, multipliedWithCoordinate, 1);
        double sumY = clij.op().sumPixels(multipliedWithCoordinate);
        resultCenterOfMass[1] = sumY / sum;
        if (input.getDimension() > 2L && input.getDepth() > 1L) {
            clij.op().multiplyImageAndCoordinate(input, multipliedWithCoordinate, 2);
            double sumZ = clij.op().sumPixels(multipliedWithCoordinate);
            resultCenterOfMass[2] = sumZ / sum;
        }

        multipliedWithCoordinate.close();
        return resultCenterOfMass;
    }


    public static boolean copy(CLIJ clij, ClearCLImage src, ClearCLBuffer dst) {
        return copyInternal(clij, src, dst, src.getDimension(), dst.getDimension());
    }

    private static boolean copyInternal(CLIJ clij, Object src, Object dst, long srcNumberOfDimensions, long dstNumberOfDimensions) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(srcNumberOfDimensions, dstNumberOfDimensions)) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "duplication.cl", "copy_" + srcNumberOfDimensions + "d", parameters);
    }

    public static boolean copy(CLIJ clij, ClearCLBuffer src, ClearCLImage dst) {
        return copyInternal(clij, src, dst, src.getDimension(), dst.getDimension());
    }

    public static boolean copy(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        return copyInternal(clij, src, dst, src.getDimension(), dst.getDimension());
    }

    public static boolean copy(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        return copyInternal(clij, src, dst, src.getDimension(), dst.getDimension());
    }

    public static boolean copySlice(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer planeIndex) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("slice", planeIndex);
        if (src.getDimension() == 2 && dst.getDimension() == 3) {
            return clij.execute(Kernels.class, "duplication.cl", "putSliceInStack", parameters);
        } else if (src.getDimension() == 3 && dst.getDimension() == 2) {
            return clij.execute(Kernels.class, "duplication.cl", "copySlice", parameters);
        } else {
            throw new IllegalArgumentException("Images have wrong dimension. Must be 3D->2D or 2D->3D.");
        }
    }

    public static boolean copySlice(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer planeIndex) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("slice", planeIndex);
        //return clij.execute(Kernels.class, "duplication.cl", "copySlice", parameters);
        if (src.getDimension() == 2 && dst.getDimension() == 3) {
            return clij.execute(Kernels.class, "duplication.cl", "putSliceInStack", parameters);
        } else if (src.getDimension() == 3 && dst.getDimension() == 2) {
            return clij.execute(Kernels.class, "duplication.cl", "copySlice", parameters);
        } else {
            throw new IllegalArgumentException("Images have wrong dimension. Must be 3D->2D or 2D->3D.");
        }
    }

    public static boolean crop(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer startX, Integer startY, Integer startZ) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("start_x", startX);
        parameters.put("start_y", startY);
        parameters.put("start_z", startZ);
        return clij.execute(Kernels.class, "duplication.cl", "crop_3d", parameters);
    }


    public static boolean crop(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer startX, Integer startY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("start_x", startX);
        parameters.put("start_y", startY);
        return clij.execute(Kernels.class, "duplication.cl", "crop_2d", parameters);
    }

    public static boolean crop(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer startX, Integer startY, Integer startZ) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("start_x", startX);
        parameters.put("start_y", startY);
        parameters.put("start_z", startZ);
        return clij.execute(Kernels.class, "duplication.cl","crop_3d", parameters);
    }

    public static boolean crop(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer startX, Integer startY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("start_x", startX);
        parameters.put("start_y", startY);
        return clij.execute(Kernels.class, "duplication.cl", "crop_2d", parameters);
    }

    public static boolean crossCorrelation(CLIJ clij, ClearCLBuffer src1, ClearCLBuffer meanSrc1, ClearCLBuffer src2, ClearCLBuffer meanSrc2, ClearCLBuffer dst, int radius, int deltaPos, int dimension) {
        assertDifferent(src1, dst);
        assertDifferent(src2, dst);
        assertDifferent(meanSrc1, dst);
        assertDifferent(meanSrc2, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("mean_src1", meanSrc1);
        parameters.put("src2", src2);
        parameters.put("mean_src2", meanSrc2);
        parameters.put("dst", dst);
        parameters.put("radius", radius);
        parameters.put("i", deltaPos);
        parameters.put("dimension", dimension);
        return clij.execute(Kernels.class, "cross_correlation.cl", "cross_correlation_3d", parameters);
    }

    public static boolean crossCorrelation(CLIJ clij, ClearCLImage src1, ClearCLImage meanSrc1, ClearCLImage src2, ClearCLImage meanSrc2, ClearCLImage dst, int radius, int deltaPos, int dimension) {
        assertDifferent(src1, dst);
        assertDifferent(src2, dst);
        assertDifferent(meanSrc1, dst);
        assertDifferent(meanSrc2, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("mean_src1", meanSrc1);
        parameters.put("src2", src2);
        parameters.put("mean_src2", meanSrc2);
        parameters.put("dst", dst);
        parameters.put("radius", radius);
        parameters.put("i", deltaPos);
        parameters.put("dimension", dimension);
        return clij.execute(Kernels.class, "cross_correlation.cl", "cross_correlation_3d", parameters);
    }

    public static boolean detectMaximaBox(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius) {
        return detectOptima(clij, src, dst, radius, true);
    }

    public static boolean detectMaximaBox(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius) {
        return detectOptima(clij, src, dst, radius, true);
    }

    public static boolean detectMaximaSliceBySliceBox(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius) {
        return detectOptimaSliceBySlice(clij, src, dst, radius, true);
    }

    public static boolean detectMaximaSliceBySliceBox(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius) {
        return detectOptimaSliceBySlice(clij, src, dst, radius, true);
    }

    public static boolean detectMinimaBox(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius) {
        return detectOptima(clij, src, dst, radius, false);
    }

    public static boolean detectMinimaBox(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius) {
        return detectOptima(clij, src, dst, radius, false);
    }

    public static boolean detectMinimaSliceBySliceBox(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius) {
        return detectOptimaSliceBySlice(clij, src, dst, radius, false);
    }

    public static boolean detectMinimaSliceBySliceBox(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius) {
        return detectOptimaSliceBySlice(clij, src, dst, radius, false);
    }

    public static boolean detectOptima(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius, Boolean detectMaxima) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);
        parameters.put("detect_maxima", detectMaxima ? 1 : 0);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (detectOptima)");
        }
        return clij.execute(Kernels.class, "detection.cl", "detect_local_optima_" + src.getDimension() + "d", parameters);
    }

    public static boolean detectOptima(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius, Boolean detectMaxima) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);
        parameters.put("detect_maxima", detectMaxima ? 1 : 0);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (detectOptima)");
        }
        return clij.execute(Kernels.class, "detection.cl", "detect_local_optima_" + src.getDimension() + "d", parameters);
    }

    public static boolean detectOptimaSliceBySlice(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius, Boolean detectMaxima) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);
        parameters.put("detect_maxima", detectMaxima ? 1 : 0);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (detectOptima)");
        }
        return clij.execute(Kernels.class, "detection.cl", "detect_local_optima_" + src.getDimension() + "d_slice_by_slice", parameters);
    }

    public static boolean detectOptimaSliceBySlice(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius, Boolean detectMaxima) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);
        parameters.put("detect_maxima", detectMaxima ? 1 : 0);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (detectOptima)");
        }
        return clij.execute(Kernels.class, "detection.cl", "detect_local_optima_" + src.getDimension() + "d_slice_by_slice", parameters);
    }

    public static boolean differenceOfGaussian(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius, Float sigmaMinuend, Float sigmaSubtrahend) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);
        parameters.put("sigma_minuend", sigmaMinuend);
        parameters.put("sigma_subtrahend", sigmaSubtrahend);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "differenceOfGaussian.cl", "subtract_convolved_images_" + src.getDimension() + "d_fast", parameters);
    }

    public static boolean differenceOfGaussianSliceBySlice(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius, Float sigmaMinuend, Float sigmaSubtrahend) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);
        parameters.put("sigma_minuend", sigmaMinuend);
        parameters.put("sigma_subtrahend", sigmaSubtrahend);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "differenceOfGaussian.cl", "subtract_convolved_images_" + src.getDimension() + "d_slice_by_slice", parameters);
    }

    public static boolean dilateBox(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "binaryProcessing.cl", "dilate_box_neighborhood_" + src.getDimension() + "d", parameters);
    }

    public static boolean dilateBox(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "binaryProcessing.cl", "dilate_box_neighborhood_" + src.getDimension() + "d", parameters);
    }

    public static boolean dilateBoxSliceBySlice(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "binaryProcessing.cl", "dilate_box_neighborhood_slice_by_slice", parameters);
    }

    public static boolean dilateBoxSliceBySlice(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "binaryProcessing.cl", "dilate_box_neighborhood_slice_by_slice", parameters);
    }

    public static boolean dilateSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "binaryProcessing.cl", "dilate_diamond_neighborhood_" + src.getDimension() + "d", parameters);
    }

    public static boolean dilateSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "binaryProcessing.cl", "dilate_diamond_neighborhood_" + src.getDimension() + "d", parameters);
    }

    public static boolean dilateSphereSliceBySlice(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "binaryProcessing.cl", "dilate_diamond_neighborhood_slice_by_slice", parameters);
    }

    public static boolean dilateSphereSliceBySlice(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "binaryProcessing.cl", "dilate_diamond_neighborhood_slice_by_slice", parameters);
    }

    public static boolean divideImages(CLIJ clij, ClearCLImage src, ClearCLImage src1, ClearCLImage dst) {
        assertDifferent(src, dst);
        assertDifferent(src1, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }

        return clij.execute(Kernels.class, "math.cl", "dividePixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean divideImages(CLIJ clij, ClearCLBuffer src, ClearCLBuffer src1, ClearCLBuffer dst) {
        assertDifferent(src, dst);
        assertDifferent(src1, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }

        return clij.execute(Kernels.class, "math.cl", "dividePixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean downsample(CLIJ clij, ClearCLImage src, ClearCLImage dst, Float factorX, Float factorY, Float factorZ) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("factor_x", 1.f / factorX);
        parameters.put("factor_y", 1.f / factorY);
        parameters.put("factor_z", 1.f / factorZ);
        return clij.execute(Kernels.class, "downsampling.cl", "downsample_3d_nearest", parameters);
    }

    public static boolean downsample(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float factorX, Float factorY, Float factorZ) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("factor_x", 1.f / factorX);
        parameters.put("factor_y", 1.f / factorY);
        parameters.put("factor_z", 1.f / factorZ);
        return clij.execute(Kernels.class, "downsampling.cl", "downsample_3d_nearest", parameters);
    }

    public static boolean downsample(CLIJ clij, ClearCLImage src, ClearCLImage dst, Float factorX, Float factorY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("factor_x", 1.f / factorX);
        parameters.put("factor_y", 1.f / factorY);
        return clij.execute(Kernels.class, "downsampling.cl", "downsample_2d_nearest", parameters);
    }

    public static boolean downsample(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float factorX, Float factorY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("factor_x", 1.f / factorX);
        parameters.put("factor_y", 1.f / factorY);
        return clij.execute(Kernels.class, "downsampling.cl", "downsample_2d_nearest", parameters);
    }

    public static boolean downsampleSliceBySliceHalfMedian(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "downsampling.cl", "downsample_xy_by_half_median", parameters);
    }

    public static boolean downsampleSliceBySliceHalfMedian(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "downsampling.cl", "downsample_xy_by_half_median", parameters);
    }

    public static boolean erodeSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }

        return clij.execute(Kernels.class, "binaryProcessing.cl", "erode_diamond_neighborhood_" + src.getDimension() + "d", parameters);
    }

    public static boolean erodeSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }

        return clij.execute(Kernels.class, "binaryProcessing.cl", "erode_diamond_neighborhood_" + src.getDimension() + "d", parameters);
    }

    public static boolean erodeSphereSliceBySlice(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }

        return clij.execute(Kernels.class, "binaryProcessing.cl", "erode_diamond_neighborhood_slice_by_slice", parameters);
    }

    public static boolean erodeSphereSliceBySlice(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }

        return clij.execute(Kernels.class, "binaryProcessing.cl", "erode_diamond_neighborhood_slice_by_slice", parameters);
    }

    public static boolean erodeBox(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }

        return clij.execute(Kernels.class, "binaryProcessing.cl", "erode_box_neighborhood_" + src.getDimension() + "d", parameters);
    }

    public static boolean erodeBox(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }

        return clij.execute(Kernels.class, "binaryProcessing.cl", "erode_box_neighborhood_" + src.getDimension() + "d", parameters);
    }

    public static boolean erodeBoxSliceBySlice(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }

        return clij.execute(Kernels.class, "binaryProcessing.cl", "erode_box_neighborhood_slice_by_slice", parameters);
    }

    public static boolean erodeBoxSliceBySlice(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }

        return clij.execute(Kernels.class, "binaryProcessing.cl", "erode_box_neighborhood_slice_by_slice", parameters);
    }

    public static boolean fillHistogram(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dstHistogram, Float minimumGreyValue, Float maximumGreyValue) {
        assertDifferent(src, dstHistogram);

        int stepSizeX = 1;
        int stepSizeY = 1;
        int stepSizeZ = 1;

        long[] globalSizes = new long[]{src.getHeight() / stepSizeZ, 1, 1};

        long numberOfPartialHistograms = globalSizes[0] * globalSizes[1] * globalSizes[2];
        long[] histogramBufferSize = new long[]{dstHistogram.getWidth(), 1, numberOfPartialHistograms};

        long timeStamp = System.currentTimeMillis();

        // allocate memory for partial histograms
        ClearCLBuffer  partialHistograms = clij.createCLBuffer(histogramBufferSize, dstHistogram.getNativeType());

        //
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_histogram", partialHistograms);
        parameters.put("minimum", minimumGreyValue);
        parameters.put("maximum", maximumGreyValue);
        parameters.put("step_size_x", stepSizeX);
        parameters.put("step_size_y", stepSizeY);
        if (src.getDimension() > 2) {
            parameters.put("step_size_z", stepSizeZ);
        }
        clij.execute(Kernels.class,
                "histogram.cl",
                "histogram_image_" + src.getDimension() + "d",
                globalSizes,
                parameters);

        Kernels.sumZProjection(clij, partialHistograms, dstHistogram);
        //IJ.log("Histogram generation took " + (System.currentTimeMillis() - timeStamp) + " msec");

        partialHistograms.close();
        return true;
    }

    public static boolean gradientX(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "neighbors.cl", "gradientX_" + src.getDimension() + "d", parameters);
    }

    public static boolean gradientY(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "neighbors.cl", "gradientY_" + src.getDimension() + "d", parameters);
    }

    public static boolean gradientZ(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "neighbors.cl", "gradientZ_" + src.getDimension() + "d", parameters);
    }

    public static boolean gradientX(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "neighbors.cl", "gradientX_" + src.getDimension() + "d", parameters);
    }

    public static boolean gradientY(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "neighbors.cl", "gradientY_" + src.getDimension() + "d", parameters);
    }

    public static boolean gradientZ(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "neighbors.cl", "gradientZ_" + src.getDimension() + "d", parameters);
    }

    public static float[] histogram(CLIJ clij, ClearCLBuffer image, Float minGreyValue, Float maxGreyValue, Integer numberOfBins) {
        ClearCLBuffer histogram = clij.createCLBuffer(new long[]{numberOfBins, 1, 1}, NativeTypeEnum.Float);

        if (minGreyValue == null) {
            minGreyValue = new Double(Kernels.minimumOfAllPixels(clij, image)).floatValue();
        }
        if (maxGreyValue == null) {
            maxGreyValue = new Double(Kernels.maximumOfAllPixels(clij, image)).floatValue();
        }

        Kernels.fillHistogram(clij, image, histogram, minGreyValue, maxGreyValue);

        ImagePlus histogramImp = clij.convert(histogram, ImagePlus.class);
        histogram.close();

        float[] determinedHistogram = (float[])(histogramImp.getProcessor().getPixels());
        return determinedHistogram;
    }


    public static boolean flip(CLIJ clij, ClearCLImage src, ClearCLImage dst, Boolean flipx, Boolean flipy, Boolean flipz) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("flipx", flipx ? 1 : 0);
        parameters.put("flipy", flipy ? 1 : 0);
        parameters.put("flipz", flipz ? 1 : 0);
        return clij.execute(Kernels.class, "flip.cl", "flip_3d", parameters);
    }

    public static boolean flip(CLIJ clij, ClearCLImage src, ClearCLImage dst, Boolean flipx, Boolean flipy) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("flipx", flipx ? 1 : 0);
        parameters.put("flipy", flipy ? 1 : 0);
        return clij.execute(Kernels.class, "flip.cl", "flip_2d", parameters);
    }

    public static boolean flip(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Boolean flipx, Boolean flipy, Boolean flipz) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("flipx", flipx ? 1 : 0);
        parameters.put("flipy", flipy ? 1 : 0);
        parameters.put("flipz", flipz ? 1 : 0);
        return clij.execute(Kernels.class, "flip.cl", "flip_3d", parameters);
    }

    public static boolean flip(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Boolean flipx, Boolean flipy) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("flipx", flipx ? 1 : 0);
        parameters.put("flipy", flipy ? 1 : 0);
        return clij.execute(Kernels.class, "flip.cl", "flip_2d", parameters);
    }

    public static boolean invert(CLIJ clij, ClearCLImage input3d, ClearCLImage output3d) {
        assertDifferent(input3d, output3d);

        return multiplyImageAndScalar(clij, input3d, output3d, -1f);
    }

    public static boolean invert(CLIJ clij, ClearCLBuffer input3d, ClearCLBuffer output3d) {
        assertDifferent(input3d, output3d);

        return multiplyImageAndScalar(clij, input3d, output3d, -1f);
    }

    public static boolean localThreshold(CLIJ clij, ClearCLImage src, ClearCLImage dst, ClearCLImage threshold) {
        assertDifferent(src, dst);
        assertDifferent(threshold, dst);

        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("local_threshold", threshold);
        parameters.put("src", src);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }

        return clij.execute(Kernels.class, "thresholding.cl", "apply_local_threshold_" + src.getDimension() + "d", parameters);
    }


    public static boolean localThreshold(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, ClearCLBuffer threshold) {
        assertDifferent(src, dst);
        assertDifferent(threshold, dst);

        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("local_threshold", threshold);
        parameters.put("src", src);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }

        return clij.execute(Kernels.class, "thresholding.cl", "apply_local_threshold_" + src.getDimension() + "d", parameters);
    }

    public static boolean mask(CLIJ clij, ClearCLImage src, ClearCLImage mask, ClearCLImage dst) {
        assertDifferent(src, dst);
        assertDifferent(mask, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("mask", mask);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (mask)");
        }
        return clij.execute(Kernels.class, "mask.cl", "mask_" + src.getDimension() + "d", parameters);
    }

    public static boolean mask(CLIJ clij, ClearCLBuffer src, ClearCLBuffer mask, ClearCLBuffer dst) {
        assertDifferent(src, dst);
        assertDifferent(mask, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("mask", mask);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (mask)");
        }
        return clij.execute(Kernels.class, "mask.cl", "mask_" + src.getDimension() + "d", parameters);
    }

    public static boolean maskStackWithPlane(CLIJ clij, ClearCLImage src, ClearCLImage mask, ClearCLImage dst) {
        assertDifferent(src, dst);
        assertDifferent(mask, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("mask", mask);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "mask.cl", "maskStackWithPlane", parameters);
    }

    public static boolean maskStackWithPlane(CLIJ clij, ClearCLBuffer src, ClearCLBuffer mask, ClearCLBuffer dst) {
        assertDifferent(src, dst);
        assertDifferent(mask, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("mask", mask);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "mask.cl", "maskStackWithPlane", parameters);
    }

    public static boolean maximumSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "maximum_image2d", parameters);
    }

    public static boolean maximumSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "maximum_image2d", parameters);
    }

    public static boolean maximumSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "maximum_image3d", parameters);
    }

    public static boolean maximumSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "maximum_image3d", parameters);
    }

    @Deprecated
    public static boolean maximumIJ(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);

        return clij.execute(Kernels.class, "filtering.cl", "maximum_image2d_ij", parameters);
    }

    @Deprecated
    public static boolean maximumIJ(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);

        return clij.execute(Kernels.class, "filtering.cl", "maximum_image2d_ij", parameters);
    }

    public static boolean maximumSliceBySliceSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "maximum_slicewise_image3d", parameters);
    }

    public static boolean maximumBox(CLIJ clij, ClearCLImage src, ClearCLImage dst, int radiusX, int radiusY, int radiusZ) {
        return executeSeparableKernel(clij, src, dst, "filtering.cl", "max_sep_image" + src.getDimension() + "d", radiusToKernelSize(radiusX), radiusToKernelSize(radiusY), radiusToKernelSize(radiusZ), radiusX, radiusY, radiusZ, src.getDimension());
    }

    public static boolean maximumBox(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, int radiusX, int radiusY, int radiusZ) {
        return executeSeparableKernel(clij, src, dst, "filtering.cl", "max_sep_image" + src.getDimension() + "d", radiusToKernelSize(radiusX), radiusToKernelSize(radiusY), radiusToKernelSize(radiusZ), radiusX, radiusY, radiusZ, src.getDimension());
    }

    public static boolean maximumSliceBySliceSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "maximum_slicewise_image3d", parameters);
    }

    public static boolean maximumImages(CLIJ clij, ClearCLImage src, ClearCLImage src1, ClearCLImage dst) {
        assertDifferent(src, dst);
        assertDifferent(src1, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (maximumImages)");
        }
        return clij.execute(Kernels.class, "math.cl", "maxPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean maximumImages(CLIJ clij, ClearCLBuffer src, ClearCLBuffer src1, ClearCLBuffer dst) {
        assertDifferent(src, dst);
        assertDifferent(src1, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (maximumImages)");
        }
        return clij.execute(Kernels.class, "math.cl", "maxPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean maximumImageAndScalar(CLIJ clij, ClearCLImage src, ClearCLImage dst, Float valueB) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("valueB", valueB);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (maximumImages)");
        }
        return clij.execute(Kernels.class, "math.cl", "maxPixelwiseScalar_" + src.getDimension() + "d", parameters);
    }

    public static boolean maximumImageAndScalar(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float valueB) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("valueB", valueB);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (maximumImages)");
        }
        return clij.execute(Kernels.class, "math.cl", "maxPixelwiseScalar_" + src.getDimension() + "d", parameters);
    }

    public static boolean minimumImages(CLIJ clij, ClearCLImage src, ClearCLImage src1, ClearCLImage dst) {
        assertDifferent(src, dst);
        assertDifferent(src1, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (minimumImages)");
        }
        return clij.execute(Kernels.class, "math.cl", "minPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean minimumImages(CLIJ clij, ClearCLBuffer src, ClearCLBuffer src1, ClearCLBuffer dst) {
        assertDifferent(src, dst);
        assertDifferent(src1, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (minimumImages)");
        }
        return clij.execute(Kernels.class, "math.cl", "minPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean minimumImageAndScalar(CLIJ clij, ClearCLImage src, ClearCLImage dst, Float valueB) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("valueB", valueB);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (minimumImageAndScalar)");
        }
        return clij.execute(Kernels.class, "math.cl", "minPixelwiseScalar_" + src.getDimension() + "d", parameters);
    }

    public static boolean minimumImageAndScalar(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float valueB) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("valueB", valueB);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (minimumImageAndScalar)");
        }
        return clij.execute(Kernels.class, "math.cl", "minPixelwiseScalar_" + src.getDimension() + "d", parameters);
    }


    public static boolean maximumZProjection(CLIJ clij, ClearCLImage src, ClearCLImage dst_max) {
        assertDifferent(src, dst_max);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_max", dst_max);

        clij.execute(Kernels.class, "projections.cl", "max_project_3d_2d", parameters);

        return true;
    }

    public static boolean maximumZProjection(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst_max) {
        assertDifferent(src, dst_max);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_max", dst_max);

        clij.execute(Kernels.class, "projections.cl", "max_project_3d_2d", parameters);

        return true;
    }

    public static boolean minimumZProjection(CLIJ clij, ClearCLImage src, ClearCLImage dst_min) {
        assertDifferent(src, dst_min);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_min", dst_min);

        clij.execute(Kernels.class, "projections.cl", "min_project_3d_2d", parameters);

        return true;
    }

    public static boolean minimumZProjection(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst_min) {
        assertDifferent(src, dst_min);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_min", dst_min);

        clij.execute(Kernels.class, "projections.cl", "min_project_3d_2d", parameters);

        return true;
    }

    public static boolean meanZProjection(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        clij.execute(Kernels.class, "projections.cl", "mean_project_3d_2d", parameters);

        return true;
    }

    public static boolean meanZProjection(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        clij.execute(Kernels.class, "projections.cl", "mean_project_3d_2d", parameters);

        return true;
    }


    public static boolean maximumXYZProjection(CLIJ clij, ClearCLImage src, ClearCLImage dst_max, Integer projectedDimensionX, Integer projectedDimensionY, Integer projectedDimension) {
        assertDifferent(src, dst_max);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_max", dst_max);
        parameters.put("projection_x", projectedDimensionX);
        parameters.put("projection_y", projectedDimensionY);
        parameters.put("projection_dim", projectedDimension);

        clij.execute(Kernels.class, "projections.cl", "max_project_dim_select_3d_2d", parameters);

        return true;
    }

    public static boolean maximumXYZProjection(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst_max, Integer projectedDimensionX, Integer projectedDimensionY, Integer projectedDimension) {
        assertDifferent(src, dst_max);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_max", dst_max);
        parameters.put("projection_x", projectedDimensionX);
        parameters.put("projection_y", projectedDimensionY);
        parameters.put("projection_dim", projectedDimension);

        clij.execute(Kernels.class, "projections.cl", "max_project_dim_select_3d_2d", parameters);

        return true;
    }

    public static boolean meanSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "mean_image2d", parameters);
    }

    public static boolean meanSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "mean_image2d", parameters);
    }

    @Deprecated
    public static boolean meanIJ(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);

        return clij.execute(Kernels.class, "filtering.cl", "mean_image2d_ij", parameters);
    }

    @Deprecated
    public static boolean meanIJ(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);

        return clij.execute(Kernels.class, "filtering.cl", "mean_image2d_ij", parameters);
    }

    public static boolean meanSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "mean_image3d", parameters);
    }

    public static boolean meanSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "mean_image3d", parameters);
    }

    public static boolean meanBox(CLIJ clij, ClearCLImage src, ClearCLImage dst, int radiusX, int radiusY, int radiusZ) {
        return executeSeparableKernel(clij, src, dst, "filtering.cl", "mean_sep_image" + src.getDimension() + "d", radiusToKernelSize(radiusX), radiusToKernelSize(radiusY), radiusToKernelSize(radiusZ), radiusX, radiusY, radiusZ, src.getDimension());
    }

    public static boolean meanBox(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, int radiusX, int radiusY, int radiusZ) {
        return executeSeparableKernel(clij, src, dst, "filtering.cl", "mean_sep_image" + src.getDimension() + "d", radiusToKernelSize(radiusX), radiusToKernelSize(radiusY), radiusToKernelSize(radiusZ), radiusX, radiusY, radiusZ, src.getDimension());
    }


    public static boolean meanSliceBySliceSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "mean_slicewise_image3d", parameters);
    }

    public static boolean meanSliceBySliceSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "mean_slicewise_image3d", parameters);
    }

    public static boolean medianSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        assertDifferent(src, dst);

        if (kernelSizeX * kernelSizeY > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the medianSphere filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "median_image2d", parameters);
    }

    public static boolean medianSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        assertDifferent(src, dst);

        if (kernelSizeX * kernelSizeY > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the medianSphere filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "median_image2d", parameters);
    }

    public static boolean medianSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        assertDifferent(src, dst);

        if (kernelSizeX * kernelSizeY * kernelSizeZ > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the medianSphere filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "median_image3d", parameters);
    }

    public static boolean medianSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        assertDifferent(src, dst);

        if (kernelSizeX * kernelSizeY * kernelSizeZ > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the medianSphere filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "median_image3d", parameters);
    }

    public static boolean medianSliceBySliceSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        assertDifferent(src, dst);

        if (kernelSizeX * kernelSizeY > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the medianSphere filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "median_slicewise_image3d", parameters);
    }

    public static boolean medianSliceBySliceSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        assertDifferent(src, dst);

        if (kernelSizeX * kernelSizeY > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the medianSphere filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "median_slicewise_image3d", parameters);
    }

    public static boolean medianBox(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        assertDifferent(src, dst);

        if (kernelSizeX * kernelSizeY > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the medianSphere filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "median_box_image2d", parameters);
    }

    public static boolean medianBox(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        assertDifferent(src, dst);

        if (kernelSizeX * kernelSizeY > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the medianSphere filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "median_box_image2d", parameters);
    }

    public static boolean medianBox(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        assertDifferent(src, dst);

        if (kernelSizeX * kernelSizeY * kernelSizeZ > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the medianSphere filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "median_box_image3d", parameters);
    }

    public static boolean medianBox(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        assertDifferent(src, dst);

        if (kernelSizeX * kernelSizeY * kernelSizeZ > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the medianSphere filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "median_box_image3d", parameters);
    }

    public static boolean medianSliceBySliceBox(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        assertDifferent(src, dst);

        if (kernelSizeX * kernelSizeY > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the medianSphere filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "median_box_slicewise_image3d", parameters);
    }

    public static boolean medianSliceBySliceBox(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        assertDifferent(src, dst);

        if (kernelSizeX * kernelSizeY > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the medianSphere filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "median_box_slicewise_image3d", parameters);
    }


    public static boolean minimumSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "minimum_image2d", parameters);
    }

    public static boolean minimumSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "minimum_image2d", parameters);
    }

    public static boolean minimumSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "minimum_image3d", parameters);
    }

    public static boolean minimumSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "minimum_image3d", parameters);
    }

    @Deprecated
    public static boolean minimumIJ(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);

        return clij.execute(Kernels.class, "filtering.cl", "minimum_image2d_ij", parameters);
    }

    @Deprecated
    public static boolean minimumIJ(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);

        return clij.execute(Kernels.class, "filtering.cl", "minimum_image2d_ij", parameters);
    }


    public static boolean minimumBox(CLIJ clij, ClearCLImage src, ClearCLImage dst, int radiusX, int radiusY, int radiusZ) {
        return executeSeparableKernel(clij, src, dst, "filtering.cl", "min_sep_image" + src.getDimension() + "d", radiusToKernelSize(radiusX), radiusToKernelSize(radiusY), radiusToKernelSize(radiusZ), radiusX, radiusY, radiusZ, src.getDimension());
    }

    public static boolean minimumBox(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, int radiusX, int radiusY, int radiusZ) {
        return executeSeparableKernel(clij, src, dst, "filtering.cl", "min_sep_image" + src.getDimension() + "d", radiusToKernelSize(radiusX), radiusToKernelSize(radiusY), radiusToKernelSize(radiusZ), radiusX, radiusY, radiusZ, src.getDimension());
    }

    public static boolean minimumSliceBySliceSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "minimum_slicewise_image3d", parameters);
    }

    public static boolean minimumSliceBySliceSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "minimum_slicewise_image3d", parameters);
    }

    public static boolean multiplyImages(CLIJ clij, ClearCLImage src, ClearCLImage src1, ClearCLImage dst) {
        assertDifferent(src, dst);
        assertDifferent(src1, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }

        return clij.execute(Kernels.class, "math.cl", "multiplyPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean multiplyImages(CLIJ clij, ClearCLBuffer src, ClearCLBuffer src1, ClearCLBuffer dst) {
        assertDifferent(src, dst);
        assertDifferent(src1, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }
        return clij.execute(Kernels.class, "math.cl", "multiplyPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean multiplyImageAndCoordinate(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer dimension) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dimension", dimension);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (multiplyImageAndCoordinate)");
        }
        return clij.execute(Kernels.class, "math.cl", "multiply_pixelwise_with_coordinate_3d", parameters);
    }

    public static boolean multiplyImageAndCoordinate(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer dimension) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dimension", dimension);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (multiplyImageAndCoordinate)");
        }
        return clij.execute(Kernels.class, "math.cl", "multiply_pixelwise_with_coordinate_3d", parameters);
    }

    public static boolean multiplyImageAndScalar(CLIJ clij, ClearCLImage src, ClearCLImage dst, Float scalar) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("scalar", scalar);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }
        return clij.execute(Kernels.class, "math.cl", "multiplyScalar_" + src.getDimension() + "d", parameters);
    }

    public static boolean multiplyImageAndScalar(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float scalar) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("scalar", scalar);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }
        return clij.execute(Kernels.class, "math.cl", "multiplyScalar_" + src.getDimension() + "d", parameters);
    }

    public static boolean multiplySliceBySliceWithScalars(CLIJ clij, ClearCLImage src, ClearCLImage dst, float[] scalars) {
        assertDifferent(src, dst);

        if (dst.getDimensions()[2] != scalars.length) {
            throw new IllegalArgumentException("Error: Wrong number of scalars in array.");
        }

        FloatBuffer buffer = FloatBuffer.allocate(scalars.length);
        buffer.put(scalars);

        ClearCLBuffer clBuffer = clij.createCLBuffer(new long[]{scalars.length}, NativeTypeEnum.Float);
        clBuffer.readFrom(buffer, true);
        buffer.clear();

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("src", src);
        map.put("scalars", clBuffer);
        map.put("dst", dst);
        boolean result = clij.execute(Kernels.class, "math.cl", "multiplySliceBySliceWithScalars", map);

        clBuffer.close();

        return result;
    }

    public static boolean multiplySliceBySliceWithScalars(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, float[] scalars) {
        assertDifferent(src, dst);

        if (dst.getDimensions()[2] != scalars.length) {
            throw new IllegalArgumentException("Error: Wrong number of scalars in array.");
        }

        FloatBuffer buffer = FloatBuffer.allocate(scalars.length);
        buffer.put(scalars);

        ClearCLBuffer clBuffer = clij.createCLBuffer(new long[]{scalars.length}, NativeTypeEnum.Float);
        clBuffer.readFrom(buffer, true);
        buffer.clear();

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("src", src);
        map.put("scalars", clBuffer);
        map.put("dst", dst);
        boolean result = clij.execute(Kernels.class, "math.cl", "multiplySliceBySliceWithScalars", map);

        clBuffer.close();

        return result;
    }

    public static boolean multiplyStackWithPlane(CLIJ clij, ClearCLImage input3d, ClearCLImage input2d, ClearCLImage output3d) {
        assertDifferent(input2d, output3d);
        assertDifferent(input3d, output3d);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", input3d);
        parameters.put("src1", input2d);
        parameters.put("dst", output3d);
        return clij.execute(Kernels.class, "math.cl", "multiplyStackWithPlanePixelwise", parameters);
    }

    public static boolean multiplyStackWithPlane(CLIJ clij, ClearCLBuffer input3d, ClearCLBuffer input2d, ClearCLBuffer output3d) {
        assertDifferent(input2d, output3d);
        assertDifferent(input3d, output3d);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", input3d);
        parameters.put("src1", input2d);
        parameters.put("dst", output3d);
        return clij.execute(Kernels.class, "math.cl", "multiplyStackWithPlanePixelwise", parameters);
    }


    public static boolean power(CLIJ clij, ClearCLImage src, ClearCLImage dst, Float exponent) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("exponent", exponent);
        return clij.execute(Kernels.class, "math.cl", "power_" + src.getDimension() + "d", parameters);
    }

    public static boolean power(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float exponent) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("exponent", exponent);

        return clij.execute(Kernels.class, "math.cl", "power_" + src.getDimension() + "d", parameters);
    }

    public static boolean radialProjection(CLIJ clij, ClearCLImage src, ClearCLImage dst, Float deltaAngle) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("deltaAngle", deltaAngle);

        return clij.execute(Kernels.class, "projections.cl", "radialProjection3d", parameters);
    }

    public static boolean radialProjection(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float deltaAngle) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("deltaAngle", deltaAngle);

        return clij.execute(Kernels.class, "projections.cl", "radialProjection3d", parameters);
    }

    public static boolean resliceBottom(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "reslicing.cl", "reslice_bottom_3d", parameters);
    }

    public static boolean resliceBottom(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "reslicing.cl", "reslice_bottom_3d", parameters);
    }

    public static boolean resliceLeft(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "reslicing.cl", "reslice_left_3d", parameters);
    }

    public static boolean resliceLeft(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "reslicing.cl", "reslice_left_3d", parameters);
    }

    public static boolean resliceRight(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "reslicing.cl", "reslice_right_3d", parameters);
    }

    public static boolean resliceRight(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "reslicing.cl", "reslice_right_3d", parameters);
    }

    public static boolean resliceTop(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "reslicing.cl", "reslice_top_3d", parameters);
    }


    public static boolean resliceTop(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "reslicing.cl", "reslice_top_3d", parameters);
    }

    public static boolean rotateLeft(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "rotate.cl", "rotate_left_" + dst.getDimension() + "d", parameters);
    }

    public static boolean rotateLeft(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "rotate.cl", "rotate_left_" + dst.getDimension() + "d", parameters);
    }

    public static boolean rotateRight(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "rotate.cl", "rotate_right_" + dst.getDimension() + "d", parameters);
    }

    public static boolean rotateRight(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "rotate.cl", "rotate_right_" + dst.getDimension() + "d", parameters);
    }

    public static boolean set(CLIJ clij, ClearCLImage clImage, Float value) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("dst", clImage);
        parameters.put("value", value);

        return clij.execute(Kernels.class, "set.cl", "set_" + clImage.getDimension() + "d", parameters);
    }

    public static boolean set(CLIJ clij, ClearCLBuffer clImage, Float value) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("dst", clImage);
        parameters.put("value", value);

        return clij.execute(Kernels.class, "set.cl", "set_" + clImage.getDimension() + "d", parameters);
    }

    public static boolean splitStack(CLIJ clij, ClearCLImage clImageIn, ClearCLImage... clImagesOut) {
        for (int i = 0; i < clImagesOut.length; i++) {
            assertDifferent(clImageIn, clImagesOut[i]);
        }
        if (clImagesOut.length > 12) {
            throw new IllegalArgumentException("Error: splitStack does not support more than 12 stacks.");
        }
        if (clImagesOut.length == 1) {
            return copy(clij, clImageIn, clImagesOut[0]);
        }
        if (clImagesOut.length == 0) {
            throw new IllegalArgumentException("Error: splitstack didn't get any output images.");
        }

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", clImageIn);
        for (int i = 0; i < clImagesOut.length; i++) {
            parameters.put("dst" + i, clImagesOut[i]);
        }

        return clij.execute(Kernels.class, "stacksplitting.cl", "split_" + clImagesOut.length + "_stacks", parameters);
    }

    public static boolean splitStack(CLIJ clij, ClearCLBuffer clImageIn, ClearCLBuffer... clImagesOut) {
        for (int i = 0; i < clImagesOut.length; i++) {
            assertDifferent(clImageIn, clImagesOut[i]);
        }

        if (clImagesOut.length > 12) {
            throw new IllegalArgumentException("Error: splitStack does not support more than 12 stacks.");
        }
        if (clImagesOut.length == 1) {
            return copy(clij, clImageIn, clImagesOut[0]);
        }
        if (clImagesOut.length == 0) {
            throw new IllegalArgumentException("Error: splitstack didn't get any output images.");
        }

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", clImageIn);
        for (int i = 0; i < clImagesOut.length; i++) {
            parameters.put("dst" + i, clImagesOut[i]);
        }

        return clij.execute(Kernels.class, "stacksplitting.cl", "split_" + clImagesOut.length + "_stacks", parameters);
    }

    /**
     *
     * Deprecated: use subtractImages instead
     */
    @Deprecated
    public static boolean subtract(CLIJ clij, ClearCLImage source1, ClearCLImage source2, ClearCLImage destination) {
        return addImagesWeighted(clij, source1, source2, destination, 1f, -1f);
    }

    /**
     *
     * Deprecated: use subtractImages instead
     */
    @Deprecated
    public static boolean subtract(CLIJ clij, ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination) {
        return addImagesWeighted(clij, source1, source2, destination, 1f, -1f);
    }

    public static boolean subtractImages(CLIJ clij, ClearCLImage subtrahend, ClearCLImage minuend, ClearCLImage destination) {
        return addImagesWeighted(clij, subtrahend, minuend, destination, 1f, -1f);
    }

    public static boolean subtractImages(CLIJ clij, ClearCLBuffer subtrahend, ClearCLBuffer minuend, ClearCLBuffer destination) {
        return addImagesWeighted(clij, subtrahend, minuend, destination, 1f, -1f);
    }


    public static double maximumOfAllPixels(CLIJ clij, ClearCLImage clImage) {
        ClearCLImage clReducedImage = clImage;
        if (clImage.getDimension() == 3) {
            clReducedImage = clij.createCLImage(new long[]{clImage.getWidth(), clImage.getHeight()}, clImage.getChannelDataType());

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("src", clImage);
            parameters.put("dst_max", clReducedImage);
            clij.execute(Kernels.class, "projections.cl", "max_project_3d_2d", parameters);
        }

        RandomAccessibleInterval rai = clij.convert(clReducedImage, RandomAccessibleInterval.class);
        Cursor cursor = Views.iterable(rai).cursor();
        float maximumGreyValue = -Float.MAX_VALUE;
        while (cursor.hasNext()) {
            float greyValue = ((RealType) cursor.next()).getRealFloat();
            if (maximumGreyValue < greyValue) {
                maximumGreyValue = greyValue;
            }
        }

        if (clImage != clReducedImage) {
            clReducedImage.close();
        }
        return maximumGreyValue;
    }

    public static double maximumOfAllPixels(CLIJ clij, ClearCLBuffer clImage) {
        ClearCLBuffer clReducedImage = clImage;
        if (clImage.getDimension() == 3) {
            clReducedImage = clij.createCLBuffer(new long[]{clImage.getWidth(), clImage.getHeight()}, clImage.getNativeType());

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("src", clImage);
            parameters.put("dst_max", clReducedImage);
            clij.execute(Kernels.class, "projections.cl", "max_project_3d_2d", parameters);
        }

        RandomAccessibleInterval rai = clij.convert(clReducedImage, RandomAccessibleInterval.class);
        Cursor cursor = Views.iterable(rai).cursor();
        float maximumGreyValue = -Float.MAX_VALUE;
        while (cursor.hasNext()) {
            float greyValue = ((RealType) cursor.next()).getRealFloat();
            if (maximumGreyValue < greyValue) {
                maximumGreyValue = greyValue;
            }
        }

        if (clImage != clReducedImage) {
            clReducedImage.close();
        }
        return maximumGreyValue;
    }

    public static double minimumOfAllPixels(CLIJ clij, ClearCLImage clImage) {
        ClearCLImage clReducedImage = clImage;
        if (clImage.getDimension() == 3) {
            clReducedImage = clij.createCLImage(new long[]{clImage.getWidth(), clImage.getHeight()}, clImage.getChannelDataType());

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("src", clImage);
            parameters.put("dst_min", clReducedImage);
            clij.execute(Kernels.class, "projections.cl", "min_project_3d_2d", parameters);
        }

        RandomAccessibleInterval rai = clij.convert(clReducedImage, RandomAccessibleInterval.class);
        Cursor cursor = Views.iterable(rai).cursor();
        float minimumGreyValue = Float.MAX_VALUE;
        while (cursor.hasNext()) {
            float greyValue = ((RealType) cursor.next()).getRealFloat();
            if (minimumGreyValue > greyValue) {
                minimumGreyValue = greyValue;
            }
        }

        if (clImage != clReducedImage) {
            clReducedImage.close();
        }
        return minimumGreyValue;
    }

    public static double minimumOfAllPixels(CLIJ clij, ClearCLBuffer clImage) {
        ClearCLBuffer clReducedImage = clImage;
        if (clImage.getDimension() == 3) {
            clReducedImage = clij.createCLBuffer(new long[]{clImage.getWidth(), clImage.getHeight()}, clImage.getNativeType());

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("src", clImage);
            parameters.put("dst_min", clReducedImage);
            clij.execute(Kernels.class, "projections.cl", "min_project_3d_2d", parameters);
        }

        RandomAccessibleInterval rai = clij.convert(clReducedImage, RandomAccessibleInterval.class);
        Cursor cursor = Views.iterable(rai).cursor();
        float minimumGreyValue = Float.MAX_VALUE;
        while (cursor.hasNext()) {
            float greyValue = ((RealType) cursor.next()).getRealFloat();
            if (minimumGreyValue > greyValue) {
                minimumGreyValue = greyValue;
            }
        }

        if (clImage != clReducedImage) {
            clReducedImage.close();
        }
        return minimumGreyValue;
    }

    public static double sumPixels(CLIJ clij, ClearCLImage clImage) {
        ClearCLImage clReducedImage = clImage;
        if (clImage.getDimension() == 3) {
            clReducedImage = clij.createCLImage(new long[]{clImage.getWidth(), clImage.getHeight()}, clImage.getChannelDataType());

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("src", clImage);
            parameters.put("dst", clReducedImage);
            clij.execute(Kernels.class, "projections.cl", "sum_project_3d_2d", parameters);
        }

        RandomAccessibleInterval rai = clij.convert(clReducedImage, RandomAccessibleInterval.class);
        Cursor cursor = Views.iterable(rai).cursor();
        float sum = 0;
        while (cursor.hasNext()) {
            sum += ((RealType) cursor.next()).getRealFloat();
        }

        if (clImage != clReducedImage) {
            clReducedImage.close();
        }
        return sum;
    }

    public static double sumPixels(CLIJ clij, ClearCLBuffer clImage) {
        ClearCLBuffer clReducedImage = clImage;
        if (clImage.getDimension() == 3) {
            clReducedImage = clij.createCLBuffer(new long[]{clImage.getWidth(), clImage.getHeight()}, clImage.getNativeType());

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("src", clImage);
            parameters.put("dst", clReducedImage);
            clij.execute(Kernels.class, "projections.cl", "sum_project_3d_2d", parameters);
        }

        RandomAccessibleInterval rai = clij.convert(clReducedImage, RandomAccessibleInterval.class);
        Cursor cursor = Views.iterable(rai).cursor();
        float sum = 0;
        while (cursor.hasNext()) {
            sum += ((RealType) cursor.next()).getRealFloat();
        }

        if (clImage != clReducedImage) {
            clReducedImage.close();
        }
        return sum;
    }

    public static double[] sumPixelsSliceBySlice(CLIJ clij, ClearCLImage input) {
        if (input.getDimension() == 2) {
            return new double[]{sumPixels(clij, input)};
        }

        int numberOfImages = (int) input.getDepth();
        double[] result = new double[numberOfImages];

        ClearCLImage slice = clij.createCLImage(new long[]{input.getWidth(), input.getHeight()}, input.getChannelDataType());
        for (int z = 0; z < numberOfImages; z++) {
            copySlice(clij, input, slice, z);
            result[z] = sumPixels(clij, slice);
        }
        slice.close();
        return result;
    }

    public static double[] sumPixelsSliceBySlice(CLIJ clij, ClearCLBuffer input) {
        if (input.getDimension() == 2) {
            return new double[]{sumPixels(clij, input)};
        }

        int numberOfImages = (int) input.getDepth();
        double[] result = new double[numberOfImages];

        ClearCLBuffer slice = clij.createCLBuffer(new long[]{input.getWidth(), input.getHeight()}, input.getNativeType());
        for (int z = 0; z < numberOfImages; z++) {
            copySlice(clij, input, slice, z);
            result[z] = sumPixels(clij, slice);
        }
        slice.close();
        return result;
    }

    public static boolean sumZProjection(CLIJ clij, ClearCLImage clImage, ClearCLImage clReducedImage) {
        assertDifferent(clImage, clReducedImage);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", clImage);
        parameters.put("dst", clReducedImage);
        return clij.execute(Kernels.class, "projections.cl", "sum_project_3d_2d", parameters);
    }

    public static boolean sumZProjection(CLIJ clij, ClearCLBuffer clImage, ClearCLBuffer clReducedImage) {
        assertDifferent(clImage, clReducedImage);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", clImage);
        parameters.put("dst", clReducedImage);
        return clij.execute(Kernels.class, "projections.cl", "sum_project_3d_2d", parameters);
    }

    public static boolean tenengradWeightsSliceBySlice(CLIJ clij, ClearCLImage clImageOut, ClearCLImage clImageIn) {
        assertDifferent(clImageIn, clImageOut);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", clImageIn);
        parameters.put("dst", clImageOut);
        return clij.execute(Kernels.class,"tenengradFusion.cl", "tenengrad_weight_unnormalized_slice_wise", parameters);
    }

    public static boolean tenengradFusion(CLIJ clij, ClearCLImage clImageOut, float[] blurSigmas, ClearCLImage... clImagesIn) {
        return tenengradFusion(clij, clImageOut, blurSigmas, 1.0f, clImagesIn);
    }

    public static boolean tenengradFusion(CLIJ clij, ClearCLImage clImageOut, float[] blurSigmas, float exponent, ClearCLImage... clImagesIn) {
        for (int i = 0; i < clImagesIn.length; i++) {
            assertDifferent(clImagesIn[i], clImageOut);
        }
        if (clImagesIn.length > 12) {
            throw new IllegalArgumentException("Error: tenengradFusion does not support more than 12 stacks.");
        }
        if (clImagesIn.length == 1) {
            return copy(clij, clImagesIn[0], clImageOut);
        }
        if (clImagesIn.length == 0) {
            throw new IllegalArgumentException("Error: tenengradFusion didn't get any output images.");
        }
        if (!clImagesIn[0].isFloat()) {
            System.out.println("Warning: tenengradFusion may only work on float images!");
        }

        HashMap<String, Object> lFusionParameters = new HashMap<>();

        ClearCLImage temporaryImage = clij.createCLImage(clImagesIn[0]);
        ClearCLImage temporaryImage2 = null;
        if (Math.abs(exponent - 1.0f) > 0.0001) {
            temporaryImage2 = clij.createCLImage(clImagesIn[0]);
        }

        ClearCLImage[] temporaryImages = new ClearCLImage[clImagesIn.length];
        for (int i = 0; i < clImagesIn.length; i++) {
            HashMap<String, Object> parameters = new HashMap<>();
            temporaryImages[i] = clij.createCLImage(clImagesIn[i]);
            parameters.put("src", clImagesIn[i]);
            parameters.put("dst", temporaryImage);

            clij.execute(Kernels.class, "tenengradFusion.cl", "tenengrad_weight_unnormalized", parameters);

            if (temporaryImage2 != null) {
                power(clij, temporaryImage, temporaryImage2, exponent);
                blur(clij, temporaryImage2, temporaryImages[i], blurSigmas[0], blurSigmas[1], blurSigmas[2]);
            } else {
                blur(clij, temporaryImage, temporaryImages[i], blurSigmas[0], blurSigmas[1], blurSigmas[2]);
            }

            lFusionParameters.put("src" + i, clImagesIn[i]);
            lFusionParameters.put("weight" + i, temporaryImages[i]);
        }

        lFusionParameters.put("dst", clImageOut);
        lFusionParameters.put("factor", (int) (clImagesIn[0].getWidth() / temporaryImages[0].getWidth()));

        boolean success = clij.execute(Kernels.class, "tenengradFusion.cl", String.format("tenengrad_fusion_with_provided_weights_%d_images", clImagesIn.length), lFusionParameters);

        temporaryImage.close();
        for (int i = 0; i < temporaryImages.length; i++) {
            temporaryImages[i].close();
        }

        if (temporaryImage2 != null) {
            temporaryImage2.close();
        }

        return success;
    }

    public static boolean threshold(CLIJ clij, ClearCLImage src, ClearCLImage dst, Float threshold) {
        assertDifferent(src, dst);
        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("threshold", threshold);
        parameters.put("src", src);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }

        return clij.execute(Kernels.class, "thresholding.cl", "apply_threshold_" + src.getDimension() + "d", parameters);
    }

    public static boolean threshold(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float threshold) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("threshold", threshold);
        parameters.put("src", src);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }

        return clij.execute(Kernels.class, "thresholding.cl", "apply_threshold_" + src.getDimension() + "d", parameters);
    }

    private static boolean checkDimensions(long... numberOfDimensions) {
        for (int i = 0; i < numberOfDimensions.length - 1; i++) {
            if (!(numberOfDimensions[i] == numberOfDimensions[i + 1])) {
                return false;
            }
        }
        return true;
    }

}

