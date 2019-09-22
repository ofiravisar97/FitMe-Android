package me.ofir.fitme.Entites;

import android.os.Parcel;
import android.os.Parcelable;

public class Goal implements Parcelable {

    private String goalTitle;
    private String goalDescription;
    private String goalID;
    private boolean isComplete = false;


    public Goal(String goalTitle, String goalDescription, String goalID) {
        this.goalTitle = goalTitle;
        this.goalDescription = goalDescription;
        this.goalID = goalID;
    }

    public Goal() {
    }

    protected Goal(Parcel in) {
        goalTitle = in.readString();
        goalDescription = in.readString();
        goalID = in.readString();
        isComplete = in.readByte() != 0;
    }

    public static final Creator<Goal> CREATOR = new Creator<Goal>() {
        @Override
        public Goal createFromParcel(Parcel in) {
            return new Goal(in);
        }

        @Override
        public Goal[] newArray(int size) {
            return new Goal[size];
        }
    };

    public String getGoalTitle() {
        return goalTitle;
    }

    public void setGoalTitle(String goalTitle) {
        this.goalTitle = goalTitle;
    }

    public String getGoalDescription() {
        return goalDescription;
    }

    public void setGoalDescription(String goalDescription) {
        this.goalDescription = goalDescription;
    }

    public String getGoalID() {
        return goalID;
    }

    public void setGoalID(String goalID) {
        this.goalID = goalID;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "goalTitle='" + goalTitle + '\'' +
                ", goalDescription='" + goalDescription + '\'' +
                ", goalID='" + goalID + '\'' +
                ", isComplete=" + isComplete +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(goalTitle);
        parcel.writeString(goalDescription);
        parcel.writeString(goalID);
        parcel.writeByte((byte) (isComplete ? 1 : 0));
    }
}
