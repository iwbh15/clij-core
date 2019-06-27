package net.haesleinhuepf.clij.utilities;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * TypeFixer
 *
 * Author: @haesleinhuepf
 * 06 2019
 */
public class TypeFixer {
    CLIJ clij;

    Map<String, Object> map;


    HashMap<String, Object> inputMap = new HashMap<String, Object>();
    HashMap<String, Object> outputMap = new HashMap<String, Object>();

    public TypeFixer(CLIJ clij, Map<String, Object> map) {
        this.map = map;
        this.clij = clij;

        if (map.size() < 3) {
            return;
        }

        for (String key : map.keySet()) {
            Object object = map.get(key);
            if (object instanceof ClearCLBuffer || object instanceof ClearCLImage) {
                if ((key.contains("src") || key.contains("input"))) {
                    inputMap.put(key, object);
                } else {
                    outputMap.put(key, object);
                }
            }
        }
    }

    public void fix() {
        fix(inputMap);
        fix(outputMap);
    }

    public void unfix() {
        unfix(inputMap);
        unfix(outputMap);
    }

    private void fix(HashMap<String, Object> currentMap) {
        if (currentMap.size() < 2) {
            currentMap.clear();
            return;
        }

        boolean fixNecessary = false;
        NativeTypeEnum type = null;
        for (String key : currentMap.keySet()) {
            Object object = currentMap.get(key);
            NativeTypeEnum currentType = null;
            if (object instanceof ClearCLImage) {
                currentType = ((ClearCLImage) object).getNativeType();
            } else if (object instanceof ClearCLBuffer) {
                currentType = ((ClearCLBuffer) object).getNativeType();
            }
            if (type == null) {
                type = currentType;
            } else {
                if (type != currentType) {
                    fixNecessary = true;
                }
            }
        }

        if (!fixNecessary) {
            currentMap.clear();
            return;
        }

        for (String key : currentMap.keySet()) {
            Object object = currentMap.get(key);
            if (object instanceof ClearCLImage) {
                ClearCLImage inImage = (ClearCLImage) object;
                if (inImage.getNativeType() != NativeTypeEnum.Float) {
                    ClearCLImage image = clij.create(inImage.getDimensions(), ImageChannelDataType.Float);
                    if (currentMap == inputMap) {
                        //System.out.println("  copying");
                        clij.op().copy(inImage, image);
                    }
                    //System.out.println("  FIXING " + key + " " + inImage);
                    map.remove(key);
                    map.put(key, image);
                }

            } else if (object instanceof ClearCLBuffer) {

                ClearCLBuffer inBuffer =(ClearCLBuffer) object;
                if (inBuffer.getNativeType() != NativeTypeEnum.Float) {

                    ClearCLBuffer buffer = clij.create(inBuffer.getDimensions(), NativeTypeEnum.Float);
                    if (currentMap == inputMap) {
                        //System.out.println("  copying");
                        clij.op().copy(inBuffer, buffer);
                    }
                    //System.out.println("  FIXING " + key + " " + inBuffer);
                    map.remove(key);
                    map.put(key, buffer);
                }
            }
        }
    }

    public void unfix(HashMap<String, Object> currentMap) {
        for (String key : currentMap.keySet()) {
            Object object = currentMap.get(key);
            Object origin = map.get(key);
            if (object != origin) {
                if (object instanceof ClearCLImage) {
                    ClearCLImage outImage = (ClearCLImage) object;
                    ClearCLImage image = (ClearCLImage) origin;
                    if (currentMap == outputMap) {
                        //System.out.println("  copying");
                        clij.op().copy(image, outImage);
                    }
                    //System.out.println("unFIXING " + key + " " + outImage);
                    image.close();
                    map.remove(key);
                    map.put(key, outImage);
                } else if (object instanceof ClearCLBuffer) {
                    ClearCLBuffer outBuffer = (ClearCLBuffer) object;
                    ClearCLBuffer buffer = (ClearCLBuffer) origin;
                    if (currentMap == outputMap) {
                        //System.out.println("  copying");
                        clij.op().copy(buffer, outBuffer);
                    }
                    //System.out.println("unFIXING " + key + " " + outBuffer);
                    buffer.close();
                    map.remove(key);
                    map.put(key, outBuffer);
                }
            }
        }
        currentMap.clear();
    }
}
