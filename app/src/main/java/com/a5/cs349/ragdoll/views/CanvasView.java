package com.a5.cs349.ragdoll.views;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import com.a5.cs349.ragdoll.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static android.view.MotionEvent.INVALID_POINTER_ID;

// https://www.youtube.com/watch?v=sb9OEl4k9Dk
// https://developer.android.com/training/gestures/scale
// https://codingexplained.com/coding/java/enum-to-integer-and-integer-to-enum
public class CanvasView extends View {
    private Vector<Sprite> sprites;
    private Sprite interactiveSprite; // Sprite with which user is interacting

    protected enum BodyPart {
        TORSO(0),
        LEFT_L_UP(1),
        LEFT_L_DOWN(2),
        LEFT_L_FOOT(3),
        RIGHT_L_UP(4),
        RIGHT_L_DOWN(5),
        RIGHT_L_FOOT(6),
        LEFT_A_UP(7),
        LEFT_A_DOWN(8),
        LEFT_A_HAND(9),
        RIGHT_A_UP(10),
        RIGHT_A_DOWN(11),
        RIGHT_A_HAND(12),
        HEAD(13);

        private int value;
        private static Map map = new HashMap<>();

        BodyPart(int value) {
            this.value = value;
        }

        static {
            for (BodyPart pageType : BodyPart.values()) {
                map.put(pageType.value, pageType);
            }
        }

        public static BodyPart valueOf(int pageType) {
            return (BodyPart) map.get(pageType);
        }

        public int getValue() {
            return value;
        }

    }

    private int mActivePointerId = INVALID_POINTER_ID;
    private ScaleGestureDetector mScaleDetector;

    public CanvasView(Context context) {
        super(context);
        mScaleDetector = new ScaleGestureDetector(context, mScaleGestureListener);
        init(null);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScaleDetector = new ScaleGestureDetector(context, mScaleGestureListener);
        init(attrs);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScaleDetector = new ScaleGestureDetector(context, mScaleGestureListener);
        init(attrs);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mScaleDetector = new ScaleGestureDetector(context, mScaleGestureListener);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {
        sprites = new Vector<>();
        interactiveSprite = null;

        Drawable torsoDrawable = getResources().getDrawable(R.drawable.scyther_body, null);
        int w = 499;
        int h = 574;
        Vector<PointF> torsoPoints = new Vector<>(Arrays.asList(BODY_POINTS[BodyPart.TORSO.getValue()]));
        torsoDrawable.setBounds(-w/2 - w/25, -h/2, w/2 - w/25, h/2);
        Sprite torso = new TranslationSprite(torsoDrawable, torsoPoints);
        sprites.add(torso);

        Sprite leftUpperLeg = buildSprite(R.drawable.scyther_left_upper_leg,
                BodyPart.LEFT_L_UP.getValue(), new Point(-115, 150),
                new PointF(-0.3f, -0.15f),90);
        leftUpperLeg.setScalable(true);
        torso.addChild(leftUpperLeg);

        Sprite leftLowerLeg = buildSprite(R.drawable.scyther_left_lower_leg,
                BodyPart.LEFT_L_DOWN.getValue(), new Point(-45, 72),
                new PointF(0, 0.4f),90);
        leftLowerLeg.setScalable(true);
        leftUpperLeg.addChild(leftLowerLeg);

        Sprite leftLegFoot = buildSprite(R.drawable.scyther_left_leg_foot,
                BodyPart.LEFT_L_FOOT.getValue(), new Point(-67, 155),
                new PointF(-0.3f, 0.5f),35);
        leftLowerLeg.addChild(leftLegFoot);

        Sprite rightUpperLeg = buildSprite(R.drawable.scyther_right_upper_leg,
                BodyPart.RIGHT_L_UP.getValue(), new Point(110, 140),
                new PointF(0.3f, -0.15f),90);
        rightUpperLeg.setScalable(true);
        torso.addChild(rightUpperLeg);

        Sprite rightLowerLeg = buildSprite(R.drawable.scyther_right_lower_leg,
                BodyPart.RIGHT_L_DOWN.getValue(), new Point(28, 65),
                new PointF(0, 0.4f),90);
        rightLowerLeg.setScalable(true);
        rightUpperLeg.addChild(rightLowerLeg);

        Sprite rightLegFoot = buildSprite(R.drawable.scyther_right_leg_foot,
                BodyPart.RIGHT_L_FOOT.getValue(), new Point(49, 135),
                new PointF(0.3f, 0.5f),35);
        rightLowerLeg.addChild(rightLegFoot);

        Sprite leftUpperArm = buildSprite(R.drawable.scyther_left_upper_arm,
                BodyPart.LEFT_A_UP.getValue(), new Point(-125, -45),
                new PointF(-0.4f, 0),180);
        torso.addChild(leftUpperArm);

        Sprite leftLowerArm = buildSprite(R.drawable.scyther_left_lower_arm,
                BodyPart.LEFT_A_DOWN.getValue(), new Point(-83, 80),
                new PointF(-0.4f, 0.5f),135);
        leftUpperArm.addChild(leftLowerArm);

        Sprite leftArmHand = buildSprite(R.drawable.scyther_left_arm_hand,
                BodyPart.LEFT_A_HAND.getValue(), new Point(-210, 260),
                new PointF(-0.45f, 0.45f),35);
        leftLowerArm.addChild(leftArmHand);

        Sprite rightUpperArm = buildSprite(R.drawable.scyther_right_upper_arm,
                BodyPart.RIGHT_A_UP.getValue(), new Point(125, -45),
                new PointF(0.4f, 0),180);
        torso.addChild(rightUpperArm);

        Sprite rightLowerArm = buildSprite(R.drawable.scyther_right_lower_arm,
                BodyPart.RIGHT_A_DOWN.getValue(), new Point(70, 80),
                new PointF(0.4f, 0.5f),135);
        rightUpperArm.addChild(rightLowerArm);

        Sprite rightArmHand = buildSprite(R.drawable.scyther_right_arm_hand,
                BodyPart.RIGHT_A_HAND.getValue(), new Point(175, 280),
                new PointF(0.45f, 0.45f),35);
        rightLowerArm.addChild(rightArmHand);

        Vector<PointF> headPoints = new Vector<>(Arrays.asList(BODY_POINTS[BodyPart.HEAD.getValue()]));
        Drawable headDrawable = getResources().getDrawable(R.drawable.scyther_head, null);
        w = headDrawable.getIntrinsicWidth();
        h = headDrawable.getIntrinsicHeight();
        headDrawable.setBounds(-w/2, 75 - h, w/2, 75);
        Sprite head = new RotatableSprite(headDrawable, headPoints);
        head.setMaxRotate(50);
        head.translate(0,-75 - 48);
        torso.addChild(head);
    }

    public void reset() {
        sprites.removeAllElements();
        init(null);
        sprites.get(0).translate(getWidth()/2, getHeight()/2);
        invalidate();
    }

    private Sprite buildSprite(int resourceId, int index, Point adj, PointF rotate, float maxRotate) {
        Drawable drawable = getResources().getDrawable(resourceId, null);
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        float rOffsetX = w*rotate.x;
        float rOffsetY = h*rotate.y;

//        Vector<PointF> points = new Vector<>(Arrays.asList(BODY_POINTS[index]));
        Vector<PointF> points = new Vector<>();
        points.add(new PointF(-w/2 + rOffsetX,-h/2 + rOffsetY));
        points.add(new PointF(w/2 + rOffsetX,-h/2 + rOffsetY));
        points.add(new PointF(w/2 + rOffsetX,h/2 + rOffsetY));
        points.add(new PointF(-w/2 + rOffsetX,h/2 + rOffsetY));

        drawable.setBounds(
                -w/2 + (int)rOffsetX,  -h/2 + (int)rOffsetY,
                w/2 + (int)rOffsetX,  h/2 + (int)rOffsetY);
        Sprite sprite = new RotatableSprite(drawable, points);
        sprite.setMaxRotate(maxRotate);
        sprite.translate(adj.x - rOffsetX,adj.y - rOffsetY);
        return sprite;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (sprites.size() > 0) {
            sprites.get(0).translate(getWidth()/2, getHeight()/2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 1. Setup
        Paint paint = new Paint();
        paint.setStrokeWidth(3);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);

        // 2. Redraw
        for (Sprite sprite : sprites) {
            sprite.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mScaleDetector.onTouchEvent(ev);
        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);

                for (Sprite sprite : sprites) {
                    interactiveSprite = sprite.getSpriteHit(x, y);
                    if (interactiveSprite != null) {
                        interactiveSprite.handleMouseDownEvent(x, y);
                        break;
                    }
                }

                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex =
                        MotionEventCompat.findPointerIndex(ev, mActivePointerId);

                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);

                if (interactiveSprite != null) {
                // && interactiveSprite.getSpriteHit(x, y) != null) {
                    interactiveSprite.handleMouseDragEvent(x, y);
                    invalidate();
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                interactiveSprite = null;
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                interactiveSprite = null;
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);

                if (pointerId == mActivePointerId) {
                    interactiveSprite = null;
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                    final float x = MotionEventCompat.getX(ev, newPointerIndex);
                    final float y = MotionEventCompat.getY(ev, newPointerIndex);

                    for (Sprite sprite : sprites) {
                        interactiveSprite = sprite.getSpriteHit(x, y);
                        if (interactiveSprite != null) {
                            interactiveSprite.handleMouseDownEvent(x, y);
                            break;
                        }
                    }
                }
                break;
            }
        }
        return true;
    }

    /**
     * The scale listener, used for handling multi-finger scale gestures.
     */
    private final ScaleGestureDetector.OnScaleGestureListener mScaleGestureListener
            = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        /**
         * This is the active focal point in terms of the viewport. Could be a local
         * variable but kept here to minimize per-frame allocations.
         */
        private PointF viewportFocus = new PointF();

        // Detects that new pointers are going down.
        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            float focusX = scaleGestureDetector.getFocusX();
            float focusY = scaleGestureDetector.getFocusY();

            for (Sprite sprite : sprites) {
                Sprite temp = sprite.getSpriteHit(focusX, focusY);
                if (temp != null
                        && temp.isScalable()) {
                    interactiveSprite = temp;
                    break;
                }
            }
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            if (interactiveSprite != null) {
                float spanX = scaleGestureDetector.getCurrentSpanX();
                float spanY = scaleGestureDetector.getCurrentSpanY();
                float prevSpanX = scaleGestureDetector.getPreviousSpanX();
                float prevSpanY = scaleGestureDetector.getPreviousSpanY();

                interactiveSprite.scale(scaleGestureDetector.getScaleFactor());
                ViewCompat.postInvalidateOnAnimation(CanvasView.this);
            }
            return true;

        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
        }
    };

    private PointF [][] BODY_POINTS = {
            {
                    new PointF(-499/6, -574/10), new PointF(-40, 80),
                    new PointF(-499/6, 574/3), new PointF(499/8, 574/3),
                    new PointF(40, 80), new PointF(499/8, -574/10)
            },
            {
                    new PointF(-114,-156), new PointF(114,-156),
                    new PointF(114,156), new PointF(-114,156),
            },
            {
                    new PointF(-100,-100), new PointF(100,-100),
                    new PointF(100,100), new PointF(-100,100),
            },
            {
                    new PointF(-100,-100), new PointF(100,-100),
                    new PointF(100,100), new PointF(-100,100),
            },
            {
                    new PointF(-100,-100), new PointF(100,-100),
                    new PointF(100,100), new PointF(-100,100),
            },
            {
                    new PointF(-100,-100), new PointF(100,-100),
                    new PointF(100,100), new PointF(-100,100),
            },
            {
                    new PointF(-100,-100), new PointF(100,-100),
                    new PointF(100,100), new PointF(-100,100),
            },
            {
                    new PointF(-100,-100), new PointF(100,-100),
                    new PointF(100,100), new PointF(-100,100),
            },
            {
                    new PointF(-100,-100), new PointF(100,-100),
                    new PointF(100,100), new PointF(-100,100),
            },
            {
                    new PointF(-100,-100), new PointF(100,-100),
                    new PointF(100,100), new PointF(-100,100),
            },
            {
                    new PointF(-100,-100), new PointF(100,-100),
                    new PointF(100,100), new PointF(-100,100),
            },
            {
                    new PointF(-100,-100), new PointF(100,-100),
                    new PointF(100,100), new PointF(-100,100),
            },
            {
                    new PointF(-100,-100), new PointF(100,-100),
                    new PointF(100,100), new PointF(-100,100),
            },
            {
                    new PointF(0,75 + -225),
                    new PointF(30,75 + -175),
                    new PointF(125,75 + -175),
                    new PointF(75,75 + -100),
                    new PointF(100,75 + -50),
                    new PointF(75,75 + -50),
                    new PointF(0,75 + 25),
                    new PointF(-75,75 + -50),
                    new PointF(-100,75 + -50),
                    new PointF(-75,75 + -100),
                    new PointF(-125,75 + -175),
                    new PointF(-30,75 + -175)
            },
    };
}
