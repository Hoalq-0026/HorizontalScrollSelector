package com.kevinpelgrims.horizontalscrollselector.library;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.*;
import android.widget.Checkable;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.List;

public class Selector extends HorizontalScrollView {
    private LinearLayout linearLayout;
    private int selectedItemIndex = -1;
    private OnCheckedChangeListener onCheckedChangeListener;
    private OnItemClickListener onItemClickListener;

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public Selector(Context context) {
        super(context);
        setup();
    }

    public Selector(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public Selector(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    private void setup() {
        setHorizontalFadingEdgeEnabled(false);
        setHorizontalScrollBarEnabled(false);

        linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(layoutParams);
        addView(linearLayout);
    }

    public void setData(List<View> items) {
        clearCurrentChildViews();
        addNewChildViews(items);
    }

    private void clearCurrentChildViews() {
        linearLayout.removeAllViews();
    }

    private void addNewChildViews(List<View> items) {
        if (items == null || items.size() < 1) return;
        for (int i = 0; i < items.size(); i++) {
            final int index = i;
            linearLayout.addView(items.get(i));
            items.get(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view, index);
                    }
                    if (index != selectedItemIndex) setSelectedItem(index);
                }
            });
        }
        addEmptyViewsToLinearLayout();
    }

    private void addEmptyViewsToLinearLayout() {
        linearLayout.addView(createEmptyView(), 0);
        linearLayout.addView(createEmptyView());
    }

    private View createEmptyView() {
        View view = new View(getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(getContext().getResources().getDisplayMetrics().widthPixels / 2, 1);
        view.setLayoutParams(layoutParams);
        return view;
    }

    public int getSelectedItemIndex() {
        return this.selectedItemIndex;
    }

    public void setSelectedItem(int index) {
        setSelectedItem(index, false);
    }

    private void setSelectedItem(int index, boolean triggerOnCheckedChanged) {
        this.selectedItemIndex = index;

        int x = 0;
        index = index + 1; // + 1 because of the empty view on position 0
        if (index >= linearLayout.getChildCount()) index = getCount();
        if (index <= 0) index = 1;
        for (int i = 0; i < index; i++) {
            x += linearLayout.getChildAt(i).getWidth();
        }
        // 'x' is now the exact position of item on position 'index'
        // We want to center that item, so let's do some math
        x -= ((getWidth() / 2) - (linearLayout.getChildAt(index).getWidth() / 2));

        smoothScrollTo(x);

        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            if (linearLayout.getChildAt(i) instanceof Checkable) {
                ((Checkable) linearLayout.getChildAt(i)).setChecked(i == index);
            }
        }

        if (triggerOnCheckedChanged && onCheckedChangeListener != null) {
            onCheckedChangeListener.onCheckedChanged(this.selectedItemIndex, true);
        }
    }

    private int getCount() {
        return linearLayout.getChildCount() - 2; // - 2 because of the two empty views
    }

    private void smoothScrollTo(int x) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ObjectAnimator animator = ObjectAnimator.ofInt(this, "scrollX", x);
            animator.setDuration(300);
            animator.start();
        }
        else {
            smoothScrollTo(x, 0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (selectedItemIndex != -1) {
            setSelectedItem(selectedItemIndex);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                int width = linearLayout.getChildAt(i).getWidth();
                int[] viewLocation = new int[2];
                linearLayout.getChildAt(i).getLocationOnScreen(viewLocation);
                int[] viewGroupLocation = new int[2];
                getLocationOnScreen(viewGroupLocation);
                int viewXInViewGroup = viewLocation[0] - viewGroupLocation[0];
                int middle = getWidth() / 2;
                if (middle >= viewXInViewGroup && middle <= viewXInViewGroup + width) {
                    if (i == 0) i++;
                    if (i == linearLayout.getChildCount() - 1) i--;
                    setSelectedItem(i - 1, true);
                    return true;
                }
            }
        }
        return false;
    }

    public static interface OnCheckedChangeListener {
        void onCheckedChanged(int index, boolean isChecked);
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int index);
    }
}