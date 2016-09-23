package com.beesham.technews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Beesham on 9/22/2016.
 */
public class StoryAdapter extends ArrayAdapter<Story> {

    static class ViewHolder{
        TextView title;
        TextView publishedDate;
    }

    public StoryAdapter(Context context, ArrayList<Story> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View listItemView = convertView;

        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) listItemView.findViewById(R.id.title_textview);
            viewHolder.publishedDate = (TextView) listItemView.findViewById(R.id.published_date_textview);

            listItemView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) listItemView.getTag();
        }

        Story story = getItem(position);
        if(story != null){
            viewHolder.title.setText(story.getmTitle());
            viewHolder.publishedDate.setText(getContext().getString(R.string.published, story.getmPubDate()));
        }

        return listItemView;
    }
}
