package com.pet.att.pickapet.recyclerview;

public class Color {
    private int red;
    private int green;
    private int blue;

    public Color() {

    }

    public Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getRGB(int red, int green, int blue) {
        return android.graphics.Color.rgb(red, green, blue);
    }
    public int getRGB() {
        return android.graphics.Color.rgb(this.red, this.green, this.blue);
    }
}
