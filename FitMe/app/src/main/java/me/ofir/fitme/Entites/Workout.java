package me.ofir.fitme.Entites;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Workout implements Parcelable {
    private String workoutID;
   private String workoutName;
   private String WorkoutDescription;
   private ArrayList<Exercise> exercises;

    public Workout(String workoutName, String workoutDescription, ArrayList<Exercise> exercises,String id) {
        this.workoutName = workoutName;
        WorkoutDescription = workoutDescription;
        this.exercises = exercises;
        this.workoutID = id;
    }

    public Workout() {
    }


    protected Workout(Parcel in) {
        workoutID = in.readString();
        workoutName = in.readString();
        WorkoutDescription = in.readString();
    }

    public static final Creator<Workout> CREATOR = new Creator<Workout>() {
        @Override
        public Workout createFromParcel(Parcel in) {
            return new Workout(in);
        }

        @Override
        public Workout[] newArray(int size) {
            return new Workout[size];
        }
    };

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public String getWorkoutDescription() {
        return WorkoutDescription;
    }

    public void setWorkoutDescription(String workoutDescription) {
        WorkoutDescription = workoutDescription;
    }

    public void setWorkoutID(String workoutID) {
        this.workoutID = workoutID;
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

    public String getWorkoutID() {
        return workoutID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(workoutID);
        parcel.writeString(workoutName);
        parcel.writeString(WorkoutDescription);
    }
}
