package io.sendman.sendman.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import io.sendman.sendman.R;

class SendManColors {

    private int backgroundColor;
    private int switchOnThumbColor;
    private int switchOnTrackColor;
    private int switchOffThumbColor;
    private int switchOffTrackColor;
    private int rowBackgroundColor;
    private int titleColor;
    private int descriptionColor;
    private int textColor;

    SendManColors(Context context, AttributeSet attrs, int defStyle) {
        final TypedArray params = context.obtainStyledAttributes(attrs, R.styleable.SendManNotificationsView, defStyle, 0);

        this.backgroundColor = params.getColor(R.styleable.SendManNotificationsView_backgroundColor, context.getResources().getColor(R.color.send_man_notification_background_color));
        this.switchOnThumbColor = params.getColor(R.styleable.SendManNotificationsView_switchOnThumbColor, context.getResources().getColor(R.color.send_man_switch_default_on_color));
        this.switchOnTrackColor = params.getColor(R.styleable.SendManNotificationsView_switchOnTrackColor, context.getResources().getColor(R.color.send_man_switch_default_on_color));
        this.switchOffThumbColor = params.getColor(R.styleable.SendManNotificationsView_switchOffThumbColor, context.getResources().getColor(R.color.send_man_switch_default_thumb_off_color));
        this.switchOffTrackColor = params.getColor(R.styleable.SendManNotificationsView_switchOffTrackColor, context.getResources().getColor(R.color.send_man_switch_default_track_off_color));
        this.rowBackgroundColor = params.getColor(R.styleable.SendManNotificationsView_rowBackgroundColor, Color.WHITE);
        this.titleColor = params.getColor(R.styleable.SendManNotificationsView_titleColor, Color.GRAY);
        this.descriptionColor = params.getColor(R.styleable.SendManNotificationsView_descriptionColor, Color.GRAY);
        this.textColor = params.getColor(R.styleable.SendManNotificationsView_textColor, Color.DKGRAY);

        params.recycle();
    }

    int getBackgroundColor() {
        return backgroundColor;
    }

    int getSwitchOnThumbColor() {
        return switchOnThumbColor;
    }

    int getSwitchOnTrackColor() {
        return switchOnTrackColor;
    }

    int getSwitchOffThumbColor() {
        return switchOffThumbColor;
    }

    int getSwitchOffTrackColor() {
        return switchOffTrackColor;
    }

    int getRowBackgroundColor() {
        return rowBackgroundColor;
    }

    int getTitleColor() {
        return titleColor;
    }

    int getDescriptionColor() {
        return descriptionColor;
    }

    int getTextColor() {
        return textColor;
    }
}
