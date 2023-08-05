package com.google.zxing;

public abstract class LuminanceSource {
    private final int height;
    private final int width;

    public abstract byte[] getMatrix();

    public abstract byte[] getRow(int i, byte[] bArr);

    protected LuminanceSource(int width2, int height2) {
        this.width = width2;
        this.height = height2;
    }

    public final int getWidth() {
        return this.width;
    }

    public final int getHeight() {
        return this.height;
    }

    public boolean isCropSupported() {
        return false;
    }

    public LuminanceSource crop(int left, int top, int width2, int height2) {
        throw new UnsupportedOperationException("This luminance source does not support cropping.");
    }

    public boolean isRotateSupported() {
        return false;
    }

    public LuminanceSource invert() {
        return new InvertedLuminanceSource(this);
    }

    public LuminanceSource rotateCounterClockwise() {
        throw new UnsupportedOperationException("This luminance source does not support rotation by 90 degrees.");
    }

    public LuminanceSource rotateCounterClockwise45() {
        throw new UnsupportedOperationException("This luminance source does not support rotation by 45 degrees.");
    }

    public final String toString() {
        char c;
        StringBuilder result = new StringBuilder(this.height * (this.width + 1));
        byte[] row = new byte[this.width];
        int y = 0;
        int i = 0;
        while (y < this.height) {
            row = getRow(y, row);
            int luminance = i;
            for (int x = 0; x < this.width; x++) {
                int i2 = row[x] & 255;
                luminance = i2;
                if (i2 < 64) {
                    c = '#';
                } else if (luminance < 128) {
                    c = '+';
                } else if (luminance < 192) {
                    c = '.';
                } else {
                    c = ' ';
                }
                result.append(c);
            }
            result.append(10);
            y++;
            i = luminance;
        }
        return result.toString();
    }
}
