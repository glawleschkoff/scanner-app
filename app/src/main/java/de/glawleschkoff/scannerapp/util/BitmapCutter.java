package de.glawleschkoff.scannerapp.util;


import android.graphics.Bitmap;

public class BitmapCutter {

    public static Bitmap imageWithMargin(Bitmap bitmap, int color, int maxMargin) {
        int maxTop = 0, maxBottom = 0, maxLeft = 0, maxRight = 0;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] bitmapArray = new int[width * height];
        bitmap.getPixels(bitmapArray, 0, width, 0, 0, width, height);

        // Find first non-color pixel from top of bitmap
        searchTopMargin:
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (bitmapArray[width * y + x] != color) {
                    maxTop = y > maxMargin ? y - maxMargin : 0;
                    break searchTopMargin;
                }
            }
        }

        // Find first non-color pixel from bottom of bitmap
        searchBottomMargin:
        for (int y = height - 1; y >= 0; y--) {
            for (int x = width - 1; x >= 0; x--) {
                if (bitmapArray[width * y + x] != color) {
                    maxBottom = y < height - maxMargin ? y + maxMargin : height;
                    break searchBottomMargin;
                }
            }
        }

        // Find first non-color pixel from left of bitmap
        searchLeftMargin:
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (bitmapArray[width * y + x] != color) {
                    maxLeft = x > maxMargin ? x - maxMargin : 0;
                    break searchLeftMargin;
                }
            }
        }

        // Find first non-color pixel from right of bitmap
        searchRightMargin:
        for (int x = width - 1; x >= 0; x--) {
            for (int y = height - 1; y >= 0; y--) {
                if (bitmapArray[width * y + x] != color) {
                    maxRight = x < width - maxMargin ? x + maxMargin : width;
                    break searchRightMargin;
                }
            }
        }
        System.out.println(maxLeft+" "+maxTop+" "+maxRight+" "+maxBottom);

        return Bitmap.createBitmap(bitmap, maxLeft, maxTop, maxRight - maxLeft, maxBottom - maxTop);
    }
}
