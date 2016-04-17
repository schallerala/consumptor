package team.metropolia.fi.consumptor;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * iConnect iCR
 * <p/>
 * Created by Roman Laitarenko on 4/16/16.
 * Copyright (c) 2016 iConnect POS. All rights reserved
 */
public class TitleTextBehavior extends CoordinatorLayout.Behavior<TextView> {

    private Context context;
    private int initialToolbarHeight;

    public TitleTextBehavior(Context context, AttributeSet attributeSet) {

        this.context = context;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, TextView child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, TextView child, View dependency) {
        initializeIfNeeded(dependency);

        Toolbar toolbar = (Toolbar)dependency.findViewById(R.id.toolbar);
        Log.i("zzz", "toolbar height" + String.valueOf(dependency.getY()));

        return true;
    }

    private void initializeIfNeeded(View dependency) {

        if (initialToolbarHeight == 0) {
            initialToolbarHeight = dependency.getHeight();
        }
    }

//    @Override
//    public boolean onDependentViewChanged(CoordinatorLayout parent, CircleImageView child, View dependency) {
//        maybeInitProperties(child, dependency);
//
//        final int maxScrollDistance = (int) (mStartToolbarPosition);
//        float expandedPercentageFactor = dependency.getY() / maxScrollDistance;
//
//        if (expandedPercentageFactor < mChangeBehaviorPoint) {
//            float heightFactor = (mChangeBehaviorPoint - expandedPercentageFactor) / mChangeBehaviorPoint;
//
//            float distanceXToSubtract = ((mStartXPosition - mFinalXPosition)
//                    * heightFactor) + (child.getHeight()/2);
//            float distanceYToSubtract = ((mStartYPosition - mFinalYPosition)
//                    * (1f - expandedPercentageFactor)) + (child.getHeight()/2);
//
//            child.setX(mStartXPosition - distanceXToSubtract);
//            child.setY(mStartYPosition - distanceYToSubtract);
//
//            float heightToSubtract = ((mStartHeight - mCustomFinalHeight) * heightFactor);
//
//            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
//            lp.width = (int) (mStartHeight - heightToSubtract);
//            lp.height = (int) (mStartHeight - heightToSubtract);
//            child.setLayoutParams(lp);
//        } else {
//            float distanceYToSubtract = ((mStartYPosition - mFinalYPosition)
//                    * (1f - expandedPercentageFactor)) + (mStartHeight/2);
//
//            child.setX(mStartXPosition - child.getWidth()/2);
//            child.setY(mStartYPosition - distanceYToSubtract);
//
//            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
//            lp.width = (int) (mStartHeight);
//            lp.height = (int) (mStartHeight);
//            child.setLayoutParams(lp);
//        }
//        return true;
//    }

//    private void maybeInitProperties(CircleImageView child, View dependency) {
//        if (mStartYPosition == 0)
//            mStartYPosition = (int) (dependency.getY());
//
//        if (mFinalYPosition == 0)
//            mFinalYPosition = (dependency.getHeight() /2);
//
//        if (mStartHeight == 0)
//            mStartHeight = child.getHeight();
//
//        if (mStartXPosition == 0)
//            mStartXPosition = (int) (child.getX() + (child.getWidth() / 2));
//
//        if (mFinalXPosition == 0)
//            mFinalXPosition = mContext.getResources().getDimensionPixelOffset(R.dimen.abc_action_bar_content_inset_material) + ((int) mCustomFinalHeight / 2);
//
//        if (mStartToolbarPosition == 0)
//            mStartToolbarPosition = dependency.getY();
//
//        if (mChangeBehaviorPoint == 0) {
//            mChangeBehaviorPoint = (child.getHeight() - mCustomFinalHeight) / (2f * (mStartYPosition - mFinalYPosition));
//        }
//    }
}
