package com.example.android.quakereport;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.widget.ArrayAdapter;

import com.example.android.quakereport.Earthquake;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by bandriya on 10-Sep-17.
 */
public class ListAdapter extends ArrayAdapter<Earthquake> {


    public ListAdapter(Context context, int resource, ArrayList<Earthquake> objects) {
        super(context,resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View listItemView =convertView;
//        View magnitudeView=listItemView.findViewById(R.id.magnitude);
        if(listItemView==null)
        {
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        final Earthquake currentEarthquake=getItem(position);

        TextView textView1 = (TextView)listItemView.findViewById(R.id.magnitude);
        textView1.setText(currentEarthquake.getMagToDisplay());
        TextView textView2=(TextView)listItemView.findViewById(R.id.primary_location);
        textView2.setText(currentEarthquake.primary_location());
        TextView textView5=(TextView)listItemView.findViewById(R.id.location_offset);
        textView5.setText(currentEarthquake.location_offset());
        TextView textView3=(TextView)listItemView.findViewById(R.id.date);
        textView3.setText(currentEarthquake.dateFormat());
        TextView textView4=(TextView)listItemView.findViewById(R.id.time);
        textView4.setText(currentEarthquake.timeFormat());
        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle=(GradientDrawable)textView1.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor=getMagnitudeColor(currentEarthquake.getMagnitude());

        magnitudeCircle.setColor(magnitudeColor);


        return listItemView;
    }

    public int getMagnitudeColor(double magnitude)
    {
        int backgroundColorResourceId;
        int magnitudeFloor=(int)Math.floor(magnitude);
        switch (magnitudeFloor)
        {
            case 0:
            case 1:
                backgroundColorResourceId=R.color.magnitude1;
                break;
            case 2:
                backgroundColorResourceId=R.color.magnitude2;
                break;
            case 3:
                backgroundColorResourceId=R.color.magnitude3;
                break;
            case 4:
                backgroundColorResourceId=R.color.magnitude4;
                break;
            case 5:
                backgroundColorResourceId=R.color.magnitude5;
                break;
            case 6:
                backgroundColorResourceId=R.color.magnitude6;
                break;
            case 7:
                backgroundColorResourceId=R.color.magnitude7;
                break;
            case 8:
                backgroundColorResourceId=R.color.magnitude8;
                break;
            case 9:
                backgroundColorResourceId=R.color.magnitude9;
                break;
            default:
                backgroundColorResourceId=R.color.magnitude10plus;
        }
        return ContextCompat.getColor(getContext(),backgroundColorResourceId);
    }
}
