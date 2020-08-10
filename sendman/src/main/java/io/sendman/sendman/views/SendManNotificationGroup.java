package io.sendman.sendman.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import io.sendman.sendman.R;
import io.sendman.sendman.models.SendManCategory;

public class SendManNotificationGroup extends LinearLayout {
    public SendManNotificationGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SendManNotificationGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SendManNotificationGroup(Context context) {
        super(context);
    }

    public void setup(SendManCategory category, SendManColors sendManColors) {
        ArrayList<SendManCategory> subCategories = new ArrayList<>();
        this.setBackgroundColor(sendManColors.getBackgroundColor());

        if (category.getDefaultValue() != null) {
            subCategories.add(category);
        }

        if (category.getSubCategories() != null && category.getSubCategories().size() > 0) {
            if (category.getName() != null && category.getName().length() > 0) {
                TextView headerTextView = findViewById(R.id.categoryGroupHeader);
                headerTextView.setText(category.getName());
                headerTextView.setTextColor(sendManColors.getTitleColor());
                headerTextView.setVisibility(VISIBLE);
            }
            if (category.getDescription() != null && category.getDescription().length() > 0) {
                TextView footerTextView = findViewById(R.id.categoryGroupFooter);
                footerTextView.setText(category.getDescription());
                footerTextView.setTextColor(sendManColors.getDescriptionColor());
                footerTextView.setVisibility(VISIBLE);
            }

            subCategories = category.getSubCategories();
        }

        LinearLayout categoriesListLayout = findViewById(R.id.categoriesList);
        for (SendManCategory subCategory :  subCategories) {
            SendManNotificationRow subCategoryView = (SendManNotificationRow) LayoutInflater.from(SendManNotificationGroup.this.getContext()).inflate(R.layout.sendman_notification_row, null);
            subCategoryView.setup(subCategory, sendManColors);
            categoriesListLayout.addView(subCategoryView);
        }
    }
}