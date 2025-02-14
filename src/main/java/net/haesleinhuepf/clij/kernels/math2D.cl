__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__kernel void multiplyPixelwise_2d(DTYPE_IMAGE_IN_2D  src,
                                 DTYPE_IMAGE_IN_2D  src1,
                          DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_OUT value = CONVERT_DTYPE_OUT(READ_IMAGE_2D(src, sampler, pos).x * READ_IMAGE_2D(src1, sampler, pos).x);

  WRITE_IMAGE_2D (dst, pos, value);
}

__kernel void dividePixelwise_2d(DTYPE_IMAGE_IN_2D  src,
                                 DTYPE_IMAGE_IN_2D  src1,
                          DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_OUT value = CONVERT_DTYPE_OUT(READ_IMAGE_2D(src, sampler, pos).x / READ_IMAGE_2D(src1, sampler, pos).x);

  WRITE_IMAGE_2D (dst, pos, value);
}

__kernel void addPixelwise_2d(DTYPE_IMAGE_IN_2D  src,
                                 DTYPE_IMAGE_IN_2D  src1,
                          DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_OUT value = CONVERT_DTYPE_OUT(READ_IMAGE_2D(src, sampler, pos).x + READ_IMAGE_2D(src1, sampler, pos).x);

  WRITE_IMAGE_2D (dst, pos, value);
}


__kernel void addScalar_2d(DTYPE_IMAGE_IN_2D  src,
                                 float scalar,
                          DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_OUT value = CONVERT_DTYPE_OUT(READ_IMAGE_2D(src, sampler, pos).x + scalar);

  WRITE_IMAGE_2D (dst, pos, value);
}

__kernel void multiplyScalar_2d(DTYPE_IMAGE_IN_2D  src,
                                 float scalar,
                          DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_OUT value = CONVERT_DTYPE_OUT(READ_IMAGE_2D(src, sampler, pos).x * scalar);

  WRITE_IMAGE_2D (dst, pos, value);
}

__kernel void maxPixelwise_2d(DTYPE_IMAGE_IN_2D  src,
                              DTYPE_IMAGE_IN_2D  src1,
                              DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_IN input = READ_IMAGE_2D(src, sampler, pos).x;
  const DTYPE_IN input1 = READ_IMAGE_2D(src1, sampler, pos).x;

  const DTYPE_OUT value = CONVERT_DTYPE_OUT(max(input, input1));

  WRITE_IMAGE_2D (dst, pos, value);
}

__kernel void minPixelwise_2d(DTYPE_IMAGE_IN_2D  src,
                              DTYPE_IMAGE_IN_2D  src1,
                              DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_IN input = READ_IMAGE_2D(src, sampler, pos).x;
  const DTYPE_IN input1 = READ_IMAGE_2D(src1, sampler, pos).x;

  const DTYPE_OUT value = CONVERT_DTYPE_OUT(min(input, input1));

  WRITE_IMAGE_2D (dst, pos, value);
}

__kernel void maxPixelwiseScalar_2d(DTYPE_IMAGE_IN_2D  src,
                              float valueB,
                              DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_IN input = READ_IMAGE_2D(src, sampler, pos).x;
  const DTYPE_IN input1 = valueB;

  const DTYPE_OUT value = CONVERT_DTYPE_OUT(max(input, input1));

  WRITE_IMAGE_2D (dst, pos, value);
}

__kernel void minPixelwiseScalar_2d(DTYPE_IMAGE_IN_2D  src,
                              float  valueB,
                              DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_IN input = READ_IMAGE_2D(src, sampler, pos).x;
  const DTYPE_IN input1 = valueB;

  const DTYPE_OUT value = CONVERT_DTYPE_OUT(min(input, input1));

  WRITE_IMAGE_2D (dst, pos, value);
}


__kernel void power_2d(DTYPE_IMAGE_IN_2D  src,
                              DTYPE_IMAGE_OUT_2D  dst,
                              float exponent
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_IN input = READ_IMAGE_2D(src, sampler, pos).x;

  const DTYPE_OUT value = CONVERT_DTYPE_OUT(pow(input, exponent));

  WRITE_IMAGE_2D (dst, pos, value);
}


__kernel void multiply_pixelwise_with_coordinate_2d(DTYPE_IMAGE_IN_2D  src,
                          DTYPE_IMAGE_OUT_2D  dst,
                          int dimension
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_OUT value = CONVERT_DTYPE_OUT(READ_IMAGE_2D(src, sampler, pos).x * get_global_id(dimension));

  WRITE_IMAGE_2D (dst, pos, value);
}

