package com.project.logicapp.ui;

import android.content.Context;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import com.project.logicapp.game.Figure;

public class BrickFrameLayout extends FrameLayout {
    private Figure shape;



    public BrickFrameLayout(@NonNull Context context, Figure shape) {
        super(context);
        this.shape = shape;
    }


    public Figure getShape() {
        return shape;
    }
}
