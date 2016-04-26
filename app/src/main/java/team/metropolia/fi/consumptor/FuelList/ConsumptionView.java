package team.metropolia.fi.consumptor.FuelList;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import team.metropolia.fi.consumptor.R;

/**
 * iConnect iCR
 * <p/>
 * Created by Roman Laitarenko on 4/26/16.
 * Copyright (c) 2016 iConnect POS. All rights reserved
 */
public class ConsumptionView extends FrameLayout {

    private TextView headlineTextView;
    private TextView titleTextView;
    private TextView valueTextView;

    public ConsumptionView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.ConsumptionView);

        initialize(context, a);

        a.recycle();
    }

    public TextView getHeadlineTextView() {
        return headlineTextView;
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public TextView getValueTextView() {
        return valueTextView;
    }

    private void initialize(Context context, TypedArray attrs) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.view_consumption, this, true);

        headlineTextView = (TextView) findViewById(R.id.headline_text_view);
        titleTextView = (TextView) findViewById(R.id.title_text_view);
        valueTextView = (TextView) findViewById(R.id.value_text_view);

        String headlineText = attrs.getString(R.styleable.ConsumptionView_headlineText);

        headlineTextView.setText(headlineText);
    }
}
