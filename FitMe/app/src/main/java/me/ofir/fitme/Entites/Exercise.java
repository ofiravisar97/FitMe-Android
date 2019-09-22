package me.ofir.fitme.Entites;

import android.os.Parcel;
import android.os.Parcelable;

public class Exercise implements Parcelable {

    //Vars
   private String exerciseName;
   private String workoutID;
   private int sets;
   private int reps;
   private float lastWeight;

    //Ctor
    public Exercise(String exerciseName, int sets, int reps,float lastWeight) {
        this.exerciseName = exerciseName;
        this.sets = sets;
        this.reps = reps;
        this.lastWeight = lastWeight;
    }

    protected Exercise(Parcel in) {
        exerciseName = in.readString();
        workoutID = in.readString();
        sets = in.readInt();
        reps = in.readInt();
        lastWeight = in.readFloat();
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    public String getWorkoutID() {
        return workoutID;
    }

    public void setWorkoutID(String workoutID) {
        this.workoutID = workoutID;
    }

    public Exercise() {
    }

    //Getters n Setters
    public String getExerciseName() {
        return exerciseName;
    }
    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }
    public int getSets() {
        return sets;
    }
    public void setSets(int sets) {
        this.sets = sets;
    }
    public int getReps() {
        return reps;
    }
    public void setReps(int reps) {
        this.reps = reps;
    }
    public float getLastWeight() {
        return lastWeight;
    }
    public void setLastWeight(int lastWeight) {
        this.lastWeight = lastWeight;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "exerciseName='" + exerciseName + '\'' +
                ", sets=" + sets +
                ", reps=" + reps +
                ", lastWeight=" + lastWeight +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(exerciseName);
        parcel.writeString(workoutID);
        parcel.writeInt(sets);
        parcel.writeInt(reps);
        parcel.writeFloat(lastWeight);
    }
}
