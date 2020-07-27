package io.sendman.sendman.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


public class SendManListView extends ListView {

    public SendManListView(Context context) {
        super(context);
    }

    public SendManListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SendManListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec;
        if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
            heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        } else {
            heightSpec = heightMeasureSpec;
        }
        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}