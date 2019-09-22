package me.ofir.fitme.Entites;

public class User {

   private String DisplayName;
   private String Email;
   private String Uid;
   private boolean haveProfile = false;
   private boolean emailVerficated = false;
   private float userRating = 0f;

    public User() {
    }

    public User(String displayName,String email, String uid) {
        DisplayName = displayName;
        Email = email;
        Uid = uid;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public boolean isHaveProfile() {
        return haveProfile;
    }

    public void setHaveProfile(boolean haveProfile) {
        this.haveProfile = haveProfile;
    }

    public boolean isEmailVerficated() {
        return emailVerficated;
    }

    public void setEmailVerficated(boolean emailVerficated) {
        this.emailVerficated = emailVerficated;
    }

    public float getUserRating() {
        return userRating;
    }

    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }

    @Override
    public String toString() {
        return "User{" +
                "DisplayName='" + DisplayName + '\'' +
                ", Email='" + Email + '\'' +
                ", Uid='" + Uid + '\'' +
                ", haveProfile=" + haveProfile +
                ", emailVerficated=" + emailVerficated +
                ", userRating=" + userRating +
                '}';
    }
}
