package com.surajbokankar.commonutil.util;

import android.graphics.drawable.GradientDrawable;
import android.view.View;

/**
 * Created by suraj.bokankar on 26/4/17.
 */

public class CustomDrawable {


    static CustomDrawable customDrawable=null;
    View layout;
    int shape;
    int strokeColor;
    int color;
    int width;
    float cornerRadius;
    float[] cornerArray;

    public static CustomDrawable getInstance(){

        customDrawable=new CustomDrawable();
        return customDrawable;
    }



    public CustomDrawable setDrawableShape(int shape){
        this.shape=shape;
        return this;
    }

    public CustomDrawable setDrawableStrokeColor(int stroke){
        this.strokeColor =stroke;
        return this;
    }


    public CustomDrawable setDrawableSolidColor(int color){
        this.color=color;
        return this;
    }

    public CustomDrawable setDrawableStrokeWidth(int   width){
        this.width=width;
        return this;
    }


    public CustomDrawable setDrawableCorner(int  radius){
        this.cornerRadius=radius;
        return this;
    }

    public CustomDrawable setDrawableCornerRadius(float[]  radius){
        this.cornerArray=radius;
        return this;
    }

    public GradientDrawable getDrawable(){
        GradientDrawable drawable=new GradientDrawable();
        drawable.setShape(shape);
        drawable.setStroke(width, strokeColor);
        drawable.setColor(color);
        drawable.setCornerRadius(cornerRadius);
        drawable.setCornerRadii(cornerArray);
        return drawable;
    }


}
