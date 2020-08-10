package io.sendman.sendman.views;

import android.content.Context;
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

    private SendManColors sendManColors;

    private ArrayList<SendManCategory> allCategories;

    public SendManNotificationsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(attrs, defStyle);
    }

    public SendManNotificationsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs, 0);
    }

    public SendManNotificationsView(Context context) {
        super(context);
        initView(null, 0);
    }

    private void initView(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.sendman_notifications_view, this);
        this.sendManColors = new SendManColors(getContext(), attrs, defStyle);
        findViewById(R.id.notificationsView).setBackgroundColor(this.sendManColors.getBackgroundColor());
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
            item.setup(category, SendManNotificationsView.this.sendManColors);
            return item;
        }

    }

}