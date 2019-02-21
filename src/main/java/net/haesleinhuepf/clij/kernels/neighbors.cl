__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__kernel void displacement_binary_image2d
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src1, DTYPE_IMAGE_IN_2D src2,
  const int Nx, const int Ny
)
{
  const int i = get_global_id(0), j = get_global_id(1);
  const int2 coord = (int2){i,j};

  const int4   e = (int4)  {(Nx-1)/2, (Ny-1)/2, 0, 0 };
  int count = 0;
  float sum = 0;

  float aSquared = e.x * e.x;
  float bSquared = e.y * e.y;

  float minDistance = e.x;

  if (((float)(READ_IMAGE_2D(src1,sampler,coord).x)) != ((float)0)) {
      for (int x = -e.x; x <= e.x; x++) {
          float xSquared = x * x;
          for (int y = -e.y; y <= e.y; y++) {
              float ySquared = y * y;
              if (xSquared / aSquared + ySquared / bSquared <= 1.0) {
                  DTYPE_OUT value = (DTYPE_OUT)READ_IMAGE_2D(src2,sampler,coord+((int2){x,y})).x;
                  if (value != 0) {
                      float distance = sqrt(xSquared + ySquared);
                      if (minDistance > distance) {
                          minDistance = distance;
                      }
                  }
              }
          }
      }
  } else {
      minDistance = 0;
  }
  DTYPE_OUT res = minDistance;
  WRITE_IMAGE_2D(dst, coord, res);
}

__kernel void displacement_image2d
(
  DTYPE_IMAGE_OUT_2D dstX, DTYPE_IMAGE_OUT_2D dstY, DTYPE_IMAGE_IN_2D src1, DTYPE_IMAGE_IN_2D src2,
  const int Nx, const int Ny
)
{
  const int i = get_global_id(0), j = get_global_id(1);
  const int2 coord = (int2){i,j};

  const int4   e = (int4)  {(Nx-1)/2, (Ny-1)/2, 0, 0 };
  int count = 0;
  float sum = 0;

  float aSquared = e.x * e.x;
  float bSquared = e.y * e.y;

  bool initialized = false;
  float minDistance = 0;
  DTYPE_OUT deltaX = 0;
  DTYPE_OUT deltaY = 0;

  DTYPE_OUT localValue = ((DTYPE_OUT)(READ_IMAGE_2D(src1,sampler,coord).x));

  for (int x = -e.x; x <= e.x; x++) {
      float xSquared = x * x;
      for (int y = -e.y; y <= e.y; y++) {
          float ySquared = y * y;
          if (xSquared / aSquared + ySquared / bSquared <= 1.0) {
              DTYPE_OUT value = (DTYPE_OUT)READ_IMAGE_2D(src2,sampler,coord+((int2){x,y})).x;
              float distance = (value - localValue);
              if (distance < 0) {
                  distance = -distance;
              }

              if (distance < minDistance || !initialized) {
                  minDistance = distance;
                  deltaX = x;
                  deltaY = y;
                  initialized = true;
              }
          }
      }
  }

  WRITE_IMAGE_2D(dstX, coord, deltaX);
  WRITE_IMAGE_2D(dstY, coord, deltaY);
}


__kernel void gradientX_2d
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src
)
{
  const int i = get_global_id(0), j = get_global_id(1);
  const int2 coord = (int2){i,j};
  const int2 coordA = (int2){i-1,j};
  const int2 coordB = (int2){i+1,j};

  DTYPE_OUT valueA = (DTYPE_OUT)READ_IMAGE_2D(src, sampler, coordA).x;
  DTYPE_OUT valueB = (DTYPE_OUT)READ_IMAGE_2D(src, sampler, coordB).x;
  DTYPE_OUT res = valueB - valueA;

  WRITE_IMAGE_2D(dst, coord, res);
}


__kernel void gradientY_2d
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src
)
{
  const int i = get_global_id(0), j = get_global_id(1);
  const int2 coord = (int2){i,j};
  const int2 coordA = (int2){i,j-1};
  const int2 coordB = (int2){i,j+1};

  DTYPE_OUT valueA = (DTYPE_OUT)READ_IMAGE_2D(src, sampler, coordA).x;
  DTYPE_OUT valueB = (DTYPE_OUT)READ_IMAGE_2D(src, sampler, coordB).x;
  DTYPE_OUT res = valueB - valueA;

  WRITE_IMAGE_2D(dst, coord, res);
}

__kernel void gradientX_3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src
)
{
  const int i = get_global_id(0);
  const int j = get_global_id(1);
  const int k = get_global_id(2);
  const int4 coord  = (int4){i, j, k, 0};
  const int4 coordA = (int4){i-1, j, k, 0};
  const int4 coordB = (int4){i+1, j, k, 0};

  DTYPE_OUT valueA = (DTYPE_OUT)READ_IMAGE_3D(src, sampler, coordA).x;
  DTYPE_OUT valueB = (DTYPE_OUT)READ_IMAGE_3D(src, sampler, coordB).x;
  DTYPE_OUT res = valueB - valueA;

  WRITE_IMAGE_3D(dst, coord, res);
}

__kernel void gradientY_3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src
)
{
  const int i = get_global_id(0);
  const int j = get_global_id(1);
  const int k = get_global_id(2);
  const int4 coord  = (int4){i, j, k, 0};
  const int4 coordA = (int4){i, j-1, k, 0};
  const int4 coordB = (int4){i, j+1, k, 0};

  DTYPE_OUT valueA = (DTYPE_OUT)READ_IMAGE_3D(src, sampler, coordA).x;
  DTYPE_OUT valueB = (DTYPE_OUT)READ_IMAGE_3D(src, sampler, coordB).x;
  DTYPE_OUT res = valueB - valueA;

  WRITE_IMAGE_3D(dst, coord, res);
}

__kernel void gradientZ_3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src
)
{
  const int i = get_global_id(0);
  const int j = get_global_id(1);
  const int k = get_global_id(2);
  const int4 coord  = (int4){i, j, k, 0};
  const int4 coordA = (int4){i, j, k-1, 0};
  const int4 coordB = (int4){i, j, k+1, 0};

  DTYPE_OUT valueA = (DTYPE_OUT)READ_IMAGE_3D(src, sampler, coordA).x;
  DTYPE_OUT valueB = (DTYPE_OUT)READ_IMAGE_3D(src, sampler, coordB).x;
  DTYPE_OUT res = valueB - valueA;

  WRITE_IMAGE_3D(dst, coord, res);
}

