package com.fivehundredpx.troubledpixels.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ResizableImageButton extends ImageButton {

    public ResizableImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override 
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
         Drawable d = getBackground();
         
         if(d!=null){
                 int width = MeasureSpec.getSize(widthMeasureSpec);
                 int height = width * d.getIntrinsicHeight() / d.getIntrinsicWidth();
                 setMeasuredDimension(width, height);
         }else{
                 super.onMeasure(widthMeasureSpec, heightMeasureSpec);
         }
    }

}