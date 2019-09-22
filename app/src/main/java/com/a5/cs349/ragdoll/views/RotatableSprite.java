package com.a5.cs349.ragdoll.views;

import android.graphics.*;
import android.graphics.drawable.Drawable;

import java.util.Vector;

public class RotatableSprite extends Sprite {
    private static final InteractionMode INTERACTION_MODE = InteractionMode.ROTATING;
    private Drawable drawable;
    private Vector<PointF> poly;

    public RotatableSprite(Drawable d, Vector<PointF> p) {
        super(INTERACTION_MODE);
        drawable = d;
        poly = p;
    }

    public boolean pointInside(float x, float y) {
        Matrix fullTransform = this.getFullTransform();
        Matrix inverseTransform = new Matrix();

        fullTransform.invert(inverseTransform);

        float [] newPoints = {x,y};
        inverseTransform.mapPoints(newPoints);
        return getPolygon(poly).contains(new com.snatik.polygon.Point(newPoints[0], newPoints[1]));
    }

    protected void drawSprite(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStrokeWidth(3);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);

        drawable.draw(canvas);
//        canvas.drawPath(getPath(poly), paint);
    }
}
