package me.ofir.fitme.Entites;

public class User_Profile {
    String email;
    int age;
    int kg;
    int cm;
    boolean isMale = false;

    public User_Profile() {
    }

    public User_Profile(int age, int kg, int cm,boolean gender,String mail) {
        this.age = age;
        this.kg = kg;
        this.cm = cm;
        isMale = gender;
        email = mail;
    }


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getKg() {
        return kg;
    }

    public void setKg(int kg) {
        this.kg = kg;
    }

    public int getCm() {
        return cm;
    }

    public void setCm(int cm) {
        this.cm = cm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    @Override
    public String toString() {
        return "User_Profile{" +
                "email='" + email + '\'' +
                ", age=" + age +
                ", kg=" + kg +
                ", cm=" + cm +
                ", isMale=" + isMale +
                '}';
    }
}
