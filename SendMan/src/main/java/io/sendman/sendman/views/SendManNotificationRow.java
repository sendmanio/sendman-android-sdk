package io.sendman.sendman.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.sendman.sendman.R;
import io.sendman.sendman.SendManDataCollector;
import io.sendman.sendman.models.SendManCategory;
import io.sendman.sendman.models.SendManSDKEvent;

public class SendManNotificationRow extends LinearLayout {

    public SendManNotificationRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SendManNotificationRow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SendManNotificationRow(Context context) {
        super(context);
    }

    public void setup(final SendManCategory category, SendManColors sendManColors) {
        this.setBackgroundColor(sendManColors.getRowBackgroundColor());

        if (category.getName() != null && category.getName().length() > 0) {
            TextView nameTextView = findViewById(R.id.subCategoryName);
            nameTextView.setText(category.getName());
            nameTextView.setTextColor(sendManColors.getTextColor());
            nameTextView.setVisibility(VISIBLE);

        }
        if (category.getDescription() != null && category.getDescription().length() > 0) {
            TextView descriptionTextView = findViewById(R.id.subCategoryDescription);
            descriptionTextView.setText(category.getDescription());
            descriptionTextView.setTextColor(sendManColors.getDescriptionColor());
            descriptionTextView.setVisibility(VISIBLE);
        }

        SendManSwitch valueSwitch = findViewById(R.id.simpleSwitch);
        valueSwitch.setSendManColors(sendManColors);
        valueSwitch.setSwitchColors(valueSwitch, category.getValue());

        valueSwitch.setChecked(category.getValue());
        valueSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SendManDataCollector.addSdkEvent(new SendManSDKEvent(isChecked ? "Category toggled on" : "Category toggled off", category.getId()));
                category.setValue(isChecked);
            }
        });
        findViewById(R.id.subCategoryDivider).setBackgroundColor(sendManColors.getBackgroundColor());
    }
}