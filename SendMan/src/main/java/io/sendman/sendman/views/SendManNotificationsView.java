package io.sendman.sendman.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;

import io.sendman.sendman.R;
import io.sendman.sendman.SendMan;
import io.sendman.sendman.SendManAPIHandler;
import io.sendman.sendman.SendManDataCollector;
import io.sendman.sendman.models.SendManCategory;
import io.sendman.sendman.models.SendManSDKEvent;

public class SendManNotificationsView extends FrameLayout {

    private ArrayList<SendManCategory> allCategories;

    public SendManNotificationsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public SendManNotificationsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SendManNotificationsView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.sendman_notifications_view, this);
        findViewById(R.id.notificationsView).setBackgroundColor(Color.parseColor("#F0F0F6"));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        allCategories = SendMan.getCategories();
        SendManDataCollector.addSdkEvent(new SendManSDKEvent("User viewed categories", null));
        SendManAPIHandler.getCategories(new SendManAPIHandler.APICallback() {
            @Override
            public void onCategoriesRetrieved() {
                allCategories = SendMan.getCategories();
                SendManNotificationsView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        SendManNotificationsView.this.updateNotificationsList();

                    }
                });
            }
        });
        this.updateNotificationsList();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        SendManAPIHandler.updateCategories(allCategories);
    }

    private void updateNotificationsList() {

        SendManListView listView = findViewById(R.id.notificationsList);
        listView.setVerticalScrollBarEnabled(false);
        listView.setAdapter(new NotificationsListAdapter(allCategories));

    }

    private class NotificationsListAdapter extends SendManListAdapter {

        NotificationsListAdapter(ArrayList<SendManCategory> allCategories) {
            super(allCategories);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            final SendManCategory category = (SendManCategory) this.getItem(position);
            final Context context = SendManNotificationsView.this.getContext();
            final SendManNotificationGroup item;
            item = (SendManNotificationGroup) LayoutInflater.from(context).inflate(R.layout.sendman_notification_group, null);
            item.setup(category);
            return item;
        }

    }

}