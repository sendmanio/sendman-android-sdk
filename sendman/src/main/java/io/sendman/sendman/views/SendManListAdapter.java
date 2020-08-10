package io.sendman.sendman.views;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import io.sendman.sendman.models.SendManIdentifiable;

public class SendManListAdapter<T extends SendManIdentifiable> extends BaseAdapter {

    private List<T> objects;

    public SendManListAdapter(List<T> objects) {
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return this.objects.size();
    }

    @Override
    public T getItem(int position) {
        if (position < 0 || position >= this.getCount()) {
            return null;
        }
        return this.objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        final SendManIdentifiable obj = this.getItem(position);
        return obj == null ? -1 : obj.getId().hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public List<T> getObjects() {
        return objects;
    }

}