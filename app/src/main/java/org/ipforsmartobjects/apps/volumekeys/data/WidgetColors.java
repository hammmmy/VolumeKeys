package org.ipforsmartobjects.apps.volumekeys.data;

import android.os.Parcel;
import android.os.Parcelable;

public class WidgetColors implements Parcelable {

    public final static Parcelable.Creator<WidgetColors> CREATOR = new Creator<WidgetColors>() {


        @SuppressWarnings({
                "unchecked"
        })
        public WidgetColors createFromParcel(Parcel in) {
            WidgetColors instance = new WidgetColors();
            instance.id = ((Long) in.readValue((Long.class.getClassLoader())));
            instance.backgroundColor = ((int) in.readValue((String.class.getClassLoader())));
            instance.iconBackgroundColor = ((int) in.readValue((String.class.getClassLoader())));
            instance.isBlack = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            return instance;
        }

        public WidgetColors[] newArray(int size) {
            return (new WidgetColors[size]);
        }

    };
    private long id;
    private int backgroundColor;
    private int iconBackgroundColor;
    private Boolean isBlack;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getIconBackgroundColor() {
        return iconBackgroundColor;
    }

    public void setIconBackgroundColor(int iconBackgroundColor) {
        this.iconBackgroundColor = iconBackgroundColor;
    }

    public Boolean getIsBlack() {
        return isBlack;
    }

    public void setIsBlack(Boolean isBlack) {
        this.isBlack = isBlack;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(backgroundColor);
        dest.writeValue(iconBackgroundColor);
        dest.writeValue(isBlack);
    }

    public int describeContents() {
        return 0;
    }

}
