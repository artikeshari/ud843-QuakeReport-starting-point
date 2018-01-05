package com.example.android.quakereport;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bandriya on 16-Sep-17.
 */
public class Earthquake
{
    private String place,primary_location,location_offset,url;
    private Long time;
    private Double magnitude;
    private DecimalFormat decimalFormat=new DecimalFormat("0.0");
    private Date date;
    private String[] strings;
    private final String LOCATION_SAPERATOR="of";
    Earthquake(Double magnitude,Long time,String place,String url)
    {
        this.url=url;
        this.magnitude=magnitude;
        this.time=time;
        this.place=place;
        date=new Date(getTime());
        String originalLocation=getPlace();
        if(originalLocation.contains(LOCATION_SAPERATOR))
        {
            strings=originalLocation.split(LOCATION_SAPERATOR);
            primary_location=strings[1];
            location_offset=strings[0]+LOCATION_SAPERATOR;
        }
        else
        {
            primary_location=originalLocation;
            location_offset="near the";
        }

    }

    public String getUrl() {
        return url;
    }

    public Long getTime() {
        return time;
    }

    public String getPlace() {
        return place;
    }

    public String getMagToDisplay() {

        return decimalFormat.format(magnitude);
    }
    public Double getMagnitude()
    {return magnitude;}
    public String timeFormat()
    {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("h:mm a");
        return simpleDateFormat.format(date);
    }
    public String dateFormat()
    {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEE MM, yyyy");
        return simpleDateFormat.format(date);
    }
    public String primary_location()
    {
        return primary_location;
    }
    public String location_offset()
    {
        return location_offset;
    }
    @Override
    public String toString() {
        return super.toString();
    }
}