/*
 * Copyright 2013 Evelio Tarazona CÃ¡ceres <evelio@evelio.info>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xwk.misc;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * A Drawable that draws an oval with given {@link Bitmap}
 */
public class RoundedDrawable extends Drawable {
  private final Bitmap mBitmap;
  private final Paint mPaint;
  private final RectF mRectF;
  private final int mBitmapWidth;
  private final int mBitmapHeight;

  public RoundedDrawable(Bitmap bitmap) {
    mBitmap = bitmap;
    mRectF = new RectF();
    mPaint = new Paint();
    mPaint.setAntiAlias(true);
    mPaint.setDither(true);
    final BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    mPaint.setShader(shader);

    // NOTE: we assume bitmap is properly scaled to current density
    mBitmapWidth = mBitmap.getWidth();
    mBitmapHeight = mBitmap.getHeight();
  }

  @Override
  public void draw(Canvas canvas) {
    canvas.drawOval(mRectF, mPaint);
  }

  @Override
  protected void onBoundsChange(Rect bounds) {
    super.onBoundsChange(bounds);

    mRectF.set(bounds);
  }

  @Override
  public void setAlpha(int alpha) {
    if (mPaint.getAlpha() != alpha) {
      mPaint.setAlpha(alpha);
      invalidateSelf();
    }
  }

  @Override
  public void setColorFilter(ColorFilter cf) {
    mPaint.setColorFilter(cf);
  }

  @Override
  public int getOpacity() {
    return PixelFormat.TRANSLUCENT;
  }

  @Override
  public int getIntrinsicWidth() {
    return mBitmapWidth;
  }

  @Override
  public int getIntrinsicHeight() {
    return mBitmapHeight;
  }

  public void setAntiAlias(boolean aa) {
    mPaint.setAntiAlias(aa);
    invalidateSelf();
  }

  @Override
  public void setFilterBitmap(boolean filter) {
    mPaint.setFilterBitmap(filter);
    invalidateSelf();
  }

  @Override
  public void setDither(boolean dither) {
    mPaint.setDither(dither);
    invalidateSelf();
  }

  public Bitmap getBitmap() {
    return mBitmap;
  }
  
  private Bitmap fastblur(Bitmap sentBitmap, int radius, int width, int height) {

      // Stack Blur v1.0 from
      // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
      //
      // Java Author: Mario Klingemann <mario at quasimondo.com>
      // http://incubator.quasimondo.com
      // created Feburary 29, 2004
      // Android port : Yahel Bouaziz <yahel at kayenko.com>
      // http://www.kayenko.com
      // ported april 5th, 2012

      // This is a compromise between Gaussian Blur and Box blur
      // It creates much better looking blurs than Box Blur, but is
      // 7x faster than my Gaussian Blur implementation.
      //
      // I called it Stack Blur because this describes best how this
      // filter works internally: it creates a kind of moving stack
      // of colors whilst scanning through the image. Thereby it
      // just has to add one new block of color to the right side
      // of the stack and remove the leftmost color. The remaining
      // colors on the topmost layer of the stack are either added on
      // or reduced by one, depending on if they are on the right or
      // on the left side of the stack.
      //
      // If you are using this algorithm in your code please add
      // the following line:
      //
      // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

  	Bitmap resizeBitmap = getResizedBitmap(sentBitmap, height, width);
      Bitmap bitmap = resizeBitmap.copy(resizeBitmap.getConfig(), true);

      if (radius < 1) {
          return null;
      }

      int w = bitmap.getWidth();
      int h = bitmap.getHeight();

      int[] pix = new int[w * h];
      bitmap.getPixels(pix, 0, w, 0, 0, w, h);

      int wm = w - 1;
      int hm = h - 1;
      int wh = w * h;
      int div = radius + radius + 1;

      int r[] = new int[wh];
      int g[] = new int[wh];
      int b[] = new int[wh];
      int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
      int vmin[] = new int[Math.max(w, h)];

      int divsum = (div + 1) >> 1;
      divsum *= divsum;
      int dv[] = new int[256 * divsum];
      for (i = 0; i < 256 * divsum; i++) {
          dv[i] = (i / divsum);
      }

      yw = yi = 0;

      int[][] stack = new int[div][3];
      int stackpointer;
      int stackstart;
      int[] sir;
      int rbs;
      int r1 = radius + 1;
      int routsum, goutsum, boutsum;
      int rinsum, ginsum, binsum;

      for (y = 0; y < h; y++) {
          rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
          for (i = -radius; i <= radius; i++) {
              p = pix[yi + Math.min(wm, Math.max(i, 0))];
              sir = stack[i + radius];
              sir[0] = (p & 0xff0000) >> 16;
              sir[1] = (p & 0x00ff00) >> 8;
              sir[2] = (p & 0x0000ff);
              rbs = r1 - Math.abs(i);
              rsum += sir[0] * rbs;
              gsum += sir[1] * rbs;
              bsum += sir[2] * rbs;
              if (i > 0) {
                  rinsum += sir[0];
                  ginsum += sir[1];
                  binsum += sir[2];
              } else {
                  routsum += sir[0];
                  goutsum += sir[1];
                  boutsum += sir[2];
              }
          }
          stackpointer = radius;

          for (x = 0; x < w; x++) {

              r[yi] = dv[rsum];
              g[yi] = dv[gsum];
              b[yi] = dv[bsum];

              rsum -= routsum;
              gsum -= goutsum;
              bsum -= boutsum;

              stackstart = stackpointer - radius + div;
              sir = stack[stackstart % div];

              routsum -= sir[0];
              goutsum -= sir[1];
              boutsum -= sir[2];

              if (y == 0) {
                  vmin[x] = Math.min(x + radius + 1, wm);
              }
              p = pix[yw + vmin[x]];

              sir[0] = (p & 0xff0000) >> 16;
              sir[1] = (p & 0x00ff00) >> 8;
              sir[2] = (p & 0x0000ff);

              rinsum += sir[0];
              ginsum += sir[1];
              binsum += sir[2];

              rsum += rinsum;
              gsum += ginsum;
              bsum += binsum;

              stackpointer = (stackpointer + 1) % div;
              sir = stack[(stackpointer) % div];

              routsum += sir[0];
              goutsum += sir[1];
              boutsum += sir[2];

              rinsum -= sir[0];
              ginsum -= sir[1];
              binsum -= sir[2];

              yi++;
          }
          yw += w;
      }
      for (x = 0; x < w; x++) {
          rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
          yp = -radius * w;
          for (i = -radius; i <= radius; i++) {
              yi = Math.max(0, yp) + x;

              sir = stack[i + radius];

              sir[0] = r[yi];
              sir[1] = g[yi];
              sir[2] = b[yi];

              rbs = r1 - Math.abs(i);

              rsum += r[yi] * rbs;
              gsum += g[yi] * rbs;
              bsum += b[yi] * rbs;

              if (i > 0) {
                  rinsum += sir[0];
                  ginsum += sir[1];
                  binsum += sir[2];
              } else {
                  routsum += sir[0];
                  goutsum += sir[1];
                  boutsum += sir[2];
              }

              if (i < hm) {
                  yp += w;
              }
          }
          yi = x;
          stackpointer = radius;
          for (y = 0; y < h; y++) {
              // Preserve alpha channel: ( 0xff000000 & pix[yi] )
              pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

              rsum -= routsum;
              gsum -= goutsum;
              bsum -= boutsum;

              stackstart = stackpointer - radius + div;
              sir = stack[stackstart % div];

              routsum -= sir[0];
              goutsum -= sir[1];
              boutsum -= sir[2];

              if (x == 0) {
                  vmin[y] = Math.min(y + r1, hm) * w;
              }
              p = x + vmin[y];

              sir[0] = r[p];
              sir[1] = g[p];
              sir[2] = b[p];

              rinsum += sir[0];
              ginsum += sir[1];
              binsum += sir[2];

              rsum += rinsum;
              gsum += ginsum;
              bsum += binsum;

              stackpointer = (stackpointer + 1) % div;
              sir = stack[stackpointer];

              routsum += sir[0];
              goutsum += sir[1];
              boutsum += sir[2];

              rinsum -= sir[0];
              ginsum -= sir[1];
              binsum -= sir[2];

              yi += w;
          }
      }

      bitmap.setPixels(pix, 0, w, 0, 0, w, h);

      return bitmap;
  }
  
  private Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
  	 
  	int width = bm.getWidth();
  	 
  	int height = bm.getHeight();
  	 
  	float scaleWidth = ((float) newWidth) / width;
  	 
  	float scaleHeight = ((float) newHeight) / height;
  	 
  	// CREATE A MATRIX FOR THE MANIPULATION
  	 
  	Matrix matrix = new Matrix();
  	 
  	// RESIZE THE BIT MAP
  	 
  	matrix.postScale(scaleWidth, scaleHeight);
  	 
  	// RECREATE THE NEW BITMAP
  	 
  	Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
  	 
  	return resizedBitmap;
  	 
  	}

  // TODO allow set and use target density, mutate, constant state, changing configurations, etc.
}