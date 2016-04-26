package team.metropolia.fi.consumptor.FuelList;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import team.metropolia.fi.consumptor.R;

/**
 * iConnect iCR
 * <p/>
 * Created by Roman Laitarenko on 4/16/16.
 * Copyright (c) 2016 iConnect POS. All rights reserved
 */
public class TitleTextBehavior extends CoordinatorLayout.Behavior<ConsumptionView> {
    private static final float FINAL_TEXT_SIZE_SCALE = 0.4f;
    private static final int INITIAL_TEXT_SIZE = 80;

    private final int actionBarContentInset;
    private final int actionBarHeight;

    public TitleTextBehavior(Context context, AttributeSet attributeSet) {

        actionBarContentInset = context.getResources()
                .getDimensionPixelOffset(R.dimen.abc_action_bar_content_inset_material);
        actionBarHeight = context.getResources()
                .getDimensionPixelOffset(R.dimen.abc_action_bar_default_height_material);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ConsumptionView child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ConsumptionView child, View dependency) {

        final float parentHorizontalCenter = dependency.getWidth() / 2;
        final float childHorizontalCenter = child.getWidth() / 2;
        final float childVerticalCenter = child.getHeight() / 2;
        final float actionBarVerticalCenter = actionBarHeight / 2;
        final float expandedPercentageFactor = Math.abs(dependency.getY() / dependency.getHeight());
        final float visiblePartHeight = dependency.getHeight() - Math.abs(dependency.getY());

        // FIXME: 4/20/16 this is clearly wrong
        // although the text view is perfectly centered
        // it looks a bit off-centered because of bottom text row
        // can't help it right now, so just hardcoded small vertical offset right now
        final int verticalOffset = 60;

        child.setY(visiblePartHeight / 2 - childVerticalCenter - verticalOffset);
        child.setY(Math.max(child.getY(), actionBarVerticalCenter - childVerticalCenter));

        if (expandedPercentageFactor > 0.5f) {

            float remindingPercentageFactor = 1 - ((1 - expandedPercentageFactor) * 2f);
            float textSizePercentageFactor = ((1 - remindingPercentageFactor) * (1 - FINAL_TEXT_SIZE_SCALE))
                    + FINAL_TEXT_SIZE_SCALE;

            child.getValueTextView().setTextSize(INITIAL_TEXT_SIZE * textSizePercentageFactor);
            // TODO: 4/26/16 instead of hiding the label move it to the side or at least hide with animation
            child.getTitleTextView().setVisibility(View.GONE);

            float currentX = (parentHorizontalCenter - childHorizontalCenter) -
                    (parentHorizontalCenter - childHorizontalCenter) * remindingPercentageFactor;

            child.setX(currentX);
            child.setX(Math.max(child.getX(), actionBarContentInset));

        } else {

            child.setX(parentHorizontalCenter - childHorizontalCenter);
            child.getValueTextView().setTextSize(INITIAL_TEXT_SIZE);
            child.getTitleTextView().setVisibility(View.VISIBLE);
        }

        child.getHeadlineTextView().setVisibility(View.GONE);

        return true;
    }
}
