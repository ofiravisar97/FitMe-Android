package me.ofir.fitme.Entites;

import android.os.Parcel;
import android.os.Parcelable;

public class Meal implements Parcelable {
    String name;
    String Content;
    String Hour;

    public Meal(String name, String content, String hour) {
        this.name = name;
        Content = content;
        Hour = hour;
    }

    public Meal() {
    }

    protected Meal(Parcel in) {
        name = in.readString();
        Content = in.readString();
        Hour = in.readString();
    }

    public static final Creator<Meal> CREATOR = new Creator<Meal>() {
        @Override
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getHour() {
        return Hour;
    }

    public void setHour(String hour) {
        Hour = hour;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "name='" + name + '\'' +
                ", Content='" + Content + '\'' +
                ", Hour='" + Hour + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(Content);
        parcel.writeString(Hour);
    }
}
