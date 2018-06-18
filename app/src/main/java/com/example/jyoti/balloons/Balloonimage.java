package com.example.jyoti.balloons;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.example.jyoti.balloons.utilis.Pixel_helper;

public class Balloonimage extends android.support.v7.widget.AppCompatImageView implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    private ValueAnimator mAminator;
    private BalloonListener mListener;
    private boolean mpoped;
    private int color;

    public Balloonimage(Context context, int color, int rawHeight) {
        super(context);
        this.color=color;
        mListener = (BalloonListener) context;
        this.setImageResource(R.drawable.balloon);
        this.setColorFilter(color);
        int rawWidth = rawHeight / 2;
        int dpHeight = Pixel_helper.pixelsToDp(rawHeight, context);
        int dpWidth = Pixel_helper.pixelsToDp(rawWidth, context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(dpWidth, dpHeight);
        setLayoutParams(params);
    }

    public void releaseBalloon(int screenHeight, int duration) {
        mAminator = new ValueAnimator();
        mAminator.setDuration(duration);
        mAminator.setFloatValues(screenHeight, 0f);
        mAminator.setInterpolator(new LinearInterpolator()); //defines the rate of change of an animation
        mAminator.setTarget(this);
        mAminator.addListener(this);
        mAminator.addUpdateListener(this);
        mAminator.start();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        setY((float) animation.getAnimatedValue());
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (!mpoped) {
            mListener.popBalloon(this, false, color);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mpoped && event.getAction() == MotionEvent.ACTION_DOWN) {
            mListener.popBalloon(this, true, color);
            mpoped = true;
            mAminator.cancel();

        }
        return super.onTouchEvent(event);

    }

    public void setPoped(boolean poped) {
        mpoped = poped;
        if (poped) {
            mAminator.cancel();
        }
    }


    public interface BalloonListener {
        void popBalloon(Balloonimage balloon, boolean userTouch, int color);
    }

}
