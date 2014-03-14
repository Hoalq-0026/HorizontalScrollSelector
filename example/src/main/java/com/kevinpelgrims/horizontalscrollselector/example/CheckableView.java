package com.kevinpelgrims.horizontalscrollselector.example;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.TextView;

public class CheckableView extends FrameLayout implements Checkable {
    private static final int[] CHECKED_STATE_SET = { android.R.attr.state_checked };

    private boolean checked;

    public CheckableView(Context context) {
        super(context);
        setup();
    }

    public CheckableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public CheckableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    private void setup() {
        inflate(getContext(), R.layout.item, this);
    }

    @Override
    public void setChecked(boolean checked) {
        if (this.checked != checked) {
            this.checked = checked;
            refreshDrawableState();
        }
        refreshStyle();
    }

    @Override
    public boolean isChecked() {
        return this.checked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    private void refreshStyle() {
        findViewById(R.id.item_container).setBackgroundColor(getResources().getColor(isChecked() ? R.color.holo_blue_dark : R.color.holo_blue_bright));
    }
}