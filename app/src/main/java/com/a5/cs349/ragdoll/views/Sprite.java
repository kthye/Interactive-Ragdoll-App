package com.a5.cs349.ragdoll.views;

import android.graphics.*;
import com.snatik.polygon.Polygon;

import java.util.Vector;

/**
 * A building block for creating your own shapes that can be
 * transformed and that can respond to input. This class is
 * provided as an example; you will likely need to modify it
 * to meet the assignment requirements.
 *
 * Michael Terry & Jeff Avery
 */

// Example extracted from scene_graph
public abstract class Sprite {
    /**
     * Tracks our current interaction mode after a mouse-down
     */
    protected enum InteractionMode {
        IDLE,
        DRAGGING,
        SCALING,
        ROTATING
    }

    private Sprite parent = null;                                       // Pointer to our parent
    private Vector<Sprite> children = new Vector<Sprite>();

    private Matrix transform = new Matrix();
    private PointF translate = new PointF(0,0);
    private PointF anchor = new PointF(0,0);

    private float MAX_ROTATE = 180;
    private float rotate = 0;
    private float anchorRotate = 0;

    private float scale = 1.0f;
    private boolean isScalable = false;

    protected float mLastTouchX = 0;
    protected float mLastTouchY = 0;

    protected InteractionMode interactionMode = InteractionMode.IDLE;   // current state

    protected InteractionMode mainInteractionMode;

    public Sprite(InteractionMode m) {
        mainInteractionMode = m;
    }

    public void addChild(Sprite s) {
        children.add(s);
        s.setParent(this);
        s.updateAnchors(anchor.x + translate.x, anchor.y + translate.y,
                anchorRotate + rotate);
    }
    public Sprite getParent() {
        return parent;
    }
    private void setParent(Sprite s) {
        this.parent = s;
    }

    /**
     * Test whether a point, in world coordinates, is within our sprite.
     */
    public abstract boolean pointInside(float x, float y);

    /**
     * Handles a mouse down event, assuming that the event has already
     * been tested to ensure the mouse point is within our sprite.
     */
    protected void handleMouseDownEvent(float x, float y) {
        mLastTouchX = x;
        mLastTouchY = y;

        interactionMode = mainInteractionMode;
    }

    /**
     * Handle mouse drag event, with the assumption that we have already
     * been "selected" as the sprite to interact with.
     * This is a very simple method that only works because we
     * assume that the coordinate system has not been modified
     * by scales or rotations. You will need to modify this method
     * appropriately so it can handle arbitrary transformations.
     */
    // Upon mouse drag
    protected void handleMouseDragEvent(float x, float y) {
        switch (interactionMode) {
            case IDLE:
                // no-op (shouldn't get here)
                break;
            case DRAGGING:
                float x_diff = x - mLastTouchX;
                float y_diff = y - mLastTouchY;
                translate.x += x_diff;
                translate.y += y_diff;
                if (x_diff != 0 || y_diff != 0) {
                    for (Sprite child : children) {
                        child.updateAnchors(x_diff, y_diff, 0);
                    }
                }
                break;
            case ROTATING:
                double angle = Math.toDegrees(Math.atan2(
                        y - translate.y - anchor.y, x - translate.x - anchor.x))
                        - Math.toDegrees(Math.atan2(
                                mLastTouchY - translate.y - anchor.y, mLastTouchX - translate.x - anchor.x));

                if (angle < 0) angle += 360;
                double test = (rotate + angle) % 360;
                if ((test) <= MAX_ROTATE
                        || (test) >= (360 - MAX_ROTATE)) {
                    rotate = (float) test;
                    for (Sprite child : children) {
                        child.updateAnchors(0, 0, (float) angle);
                        child.rotate(angle);
                    }
                }
                break;
        }

        mLastTouchX = x;
        mLastTouchY = y;
    }

    protected void handleMouseUp(float x, float y) {
        interactionMode = InteractionMode.IDLE;
        // Do any other interaction handling necessary here
    }

    /**
     * Locates the sprite that was hit by the given event.
     * You *may* need to modify this method, depending on
     * how you modify other parts of the class.
     *
     * @return The sprite that was hit, or null if no sprite was hit
     */
    public Sprite getSpriteHit(float x, float y) {
        for (Sprite sprite : children) {
            Sprite s = sprite.getSpriteHit(x, y);
            if (s != null) {
                return s;
            }
        }
        if (this.pointInside(x, y)) {
            return this;
        }
        return null;
    }

    /**
     * Returns the full transform to this object from the root
     */
    public Matrix getFullTransform() {
        Matrix returnTransform = new Matrix();
        Sprite curSprite = this;
        while (curSprite != null) {
            returnTransform.preConcat(curSprite.getLocalTransform());
            curSprite = curSprite.getParent();
        }
        return returnTransform;
    }

    /**
     * Returns our local transform, without scaling, because scaling should not
     * carry over
     */
    public Matrix getLocalTransform() {
        transform.reset();

        Matrix parentTransform = new Matrix();
        if (parent != null) {
            parentTransform = parent.getFullTransform();
            parentTransform.invert(transform);
        }

        transform.postRotate(rotate);
        transform.postConcat(parentTransform);

        transform.postTranslate(translate.x, translate.y);

        return transform;
    }

    public Matrix getLocalWithScaleTransform() {
        transform.reset();

        Matrix parentTransform = new Matrix();
        if (parent != null) {
            parentTransform = parent.getFullTransform();
            parentTransform.invert(transform);
        }

        transform.postScale(1,scale);
        transform.postRotate(rotate);
        transform.postConcat(parentTransform);
        transform.postTranslate(translate.x, translate.y);
        return transform;
    }

    /**
     * Performs an arbitrary transform on this sprite
     */
    public void transform(Matrix t) {
        transform.postConcat(t);
    }

    /**
     * Translate relative to parent
     */
    public void translate(float x, float y) {
        translate.x += x;
        translate.y += y;
        for (Sprite child : children) {
            child.updateAnchors(x, y, 0);
        }
    }

    /**
     * Rotate relative to parent
     */
    public void rotate(double angle) {
        Matrix rotateTransform = new Matrix();
        rotateTransform.postRotate((float)angle);
        rotate(rotateTransform);
    }

    private void rotate(Matrix rotateTransform) {
        float [] point = { translate.x, translate.y };
        rotateTransform.mapPoints(point);
        translate = new PointF(point[0], point[1]);
        for (Sprite child : children) {
            child.rotate(rotateTransform);
        }
    }

    /**
     * Scale relative to parent
     */
    public void scale(float scaleFactor) {
        if (isScalable && scale * scaleFactor <= 5
                && scale * scaleFactor >= 0.2) {
            scale *= scaleFactor;

            for (Sprite child : children) {
                child.realScale(scaleFactor, anchorRotate + rotate);
            }
        }
    }

    private void realScale(float scaleFactor, float angle) {
        Matrix scaleTransform = new Matrix();
        scaleTransform.postRotate(-angle);
        scaleTransform.postScale(1, scaleFactor);
        scaleTransform.postRotate(angle);

        float [] point = { translate.x, translate.y };
        scaleTransform.mapPoints(point);
        translate = new PointF(point[0], point[1]);
        if (isScalable) {
            scale(scaleFactor);
        }
    }

    /**
     * Draws the sprite. This method will call drawSprite after
     * the transform has been set up for this sprite.
     */
    public void draw(Canvas canvas) {
        Matrix oldTransform = canvas.getMatrix();

        // Set to our transform
        Matrix currentAT = canvas.getMatrix();
        if (parent != null) {
            currentAT.postConcat(parent.getFullTransform());
        }
        currentAT.postConcat(getLocalWithScaleTransform());
        canvas.setMatrix(currentAT);

        // Draw the sprite (delegated to sub-classes)
        this.drawSprite(canvas);

        // Restore original transform
        canvas.setMatrix(oldTransform);

        // Draw children
        for (Sprite sprite : children) {
            sprite.draw(canvas);
        }
    }

    public void setMaxRotate(float angle) {
        MAX_ROTATE = angle;
    }

    public void setScalable(boolean isScalable) {
        this.isScalable = isScalable;
    }
    public boolean isScalable() {
        return isScalable;
    }

    protected abstract void drawSprite(Canvas canvas);

    protected void updateAnchors(float x_diff, float y_diff, float ang_diff) {
        anchor.x += x_diff;
        anchor.y += y_diff;
        anchorRotate = (anchorRotate + ang_diff) % 360;
        for (Sprite child : children) {
            child.updateAnchors(x_diff, y_diff, ang_diff);
        }
    }

    protected static Path getPath(Vector<PointF> shapePoints) {
        Path path = new Path();
        path.moveTo(shapePoints.get(0).x, shapePoints.get(0).y);
        for (PointF p : shapePoints) {
            path.lineTo(p.x, p.y);
        }
        path.close();
        return path;
    }

    protected static Polygon getPolygon(Vector<PointF> polyPath) {
        Polygon.Builder polyBuilder = Polygon.Builder();
        for (PointF p : polyPath) {
            polyBuilder.addVertex(new com.snatik.polygon.Point(p.x, p.y));
        }
        return polyBuilder.build();
    }
}
