package io.sendman.sendman.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


public class SendManSwitch extends Switch {

    private SendManColors sendManColors;

    public SendManSwitch(Context context) {
        super(context);
        initView();
    }

    public SendManSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SendManSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SendManSwitch(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        setBackgroundColor(Color.TRANSPARENT);
    }

    public void setSendManColors(SendManColors sendManColors) {
        this.sendManColors = sendManColors;
    }

    @Override
    public void setOnCheckedChangeListener(@Nullable final OnCheckedChangeListener listener) {
        OnCheckedChangeListener combinedListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (listener != null) listener.onCheckedChanged(buttonView, isChecked);
                setSwitchColors(SendManSwitch.this, isChecked());

            }
        };
        super.setOnCheckedChangeListener(combinedListener);
    }

    void setSwitchColors(Switch switchButton, boolean isChecked) {
        if (sendManColors == null) {
            return;
        }

        int thumbColor = isChecked ? sendManColors.getSwitchOnThumbColor() : sendManColors.getSwitchOffThumbColor();
        switchButton.getThumbDrawable().setColorFilter(thumbColor, PorterDuff.Mode.MULTIPLY);

        int trackColor = isChecked ? sendManColors.getSwitchOnTrackColor() : sendManColors.getSwitchOffTrackColor();
        switchButton.getTrackDrawable().setColorFilter(trackColor, PorterDuff.Mode.MULTIPLY);

    }
}
