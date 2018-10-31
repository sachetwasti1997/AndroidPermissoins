package com.sachet.contentproviderexample1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

class ContentAdapter extends ArrayAdapter<String> {
    List<String> resource;

    ContentAdapter(Context context, List<String> resource){
        super(context,R.layout.contact_name,resource);
        this.resource = resource;
    }

    class ViewHolder{
        TextView mTextView;
        public ViewHolder(View v){
            mTextView = v.findViewById(R.id.names);
        }
    }

    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        View row = convertView;

        ViewHolder viewHolder = null;

        if(row == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.contact_name,parent,false);
            viewHolder = new ViewHolder(row);
            row.setTag(viewHolder);
        }else viewHolder = (ViewHolder)row.getTag();

        viewHolder.mTextView.setText(resource.get(position));
        return row;
    }
}
