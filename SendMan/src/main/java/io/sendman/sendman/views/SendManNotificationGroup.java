package io.sendman.sendman.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public void setup(SendManCategory category) {
        this.setBackgroundColor(Color.parseColor("#F0F0F6"));
        ArrayList<SendManCategory> subCategories = new ArrayList<>();
        if (category.getDefaultValue() != null) {
            subCategories.add(category);
        }

        if (category.getSubCategories() != null && category.getSubCategories().size() > 0) {
            if (category.getName() != null && category.getName().length() > 0) {
                TextView headerTextView = findViewById(R.id.categoryGroupHeader);
                headerTextView.setText(category.getName());
                headerTextView.setVisibility(VISIBLE);
            }
            if (category.getDescription() != null && category.getDescription().length() > 0) {
                TextView footerTextView = findViewById(R.id.categoryGroupFooter);
                footerTextView.setText(category.getDescription());
                footerTextView.setVisibility(VISIBLE);
            }

            subCategories = category.getSubCategories();
        }

        LinearLayout categoriesListLayout = findViewById(R.id.categoriesList);
        for (SendManCategory subCategory :  subCategories) {
            SendManNotificationRow subCategoryView = (SendManNotificationRow) LayoutInflater.from(SendManNotificationGroup.this.getContext()).inflate(R.layout.sendman_notification_row, null);
            subCategoryView.setup(subCategory);
            categoriesListLayout.addView(subCategoryView);
        }
    }



    private class SubCategoriesListAdapter extends SendManListAdapter {

        SubCategoriesListAdapter(ArrayList<SendManCategory> subCategories) {
            super(subCategories);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            final SendManCategory category = (SendManCategory) this.getItem(position);
            final Context context = SendManNotificationGroup.this.getContext();
            final SendManNotificationRow item;
            if (convertView != null) {
                item = (SendManNotificationRow) convertView;
            } else {
                item = (SendManNotificationRow) LayoutInflater.from(context).inflate(R.layout.sendman_notification_row, parent, false);
            }
            item.setup(category);
            return item;
        }

    }
}