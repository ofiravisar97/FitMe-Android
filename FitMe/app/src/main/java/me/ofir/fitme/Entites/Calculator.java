package me.ofir.fitme.Entites;

public class Calculator {

    //Ctor
    public Calculator() {
    }

    public double bmrMale(int weight, int height, int age){
        double result = 66+(13.7*weight)+(5*height)-(6.8*age);
        return Math.ceil(result);
    }

    public double bmrFemale(int weight, int height, int age){
        double result =  655 + (9.6 * weight) + (1.8 * height) - (4.7 * age);
        return Math.ceil(result);
    }

    public  double rmrMale(int weight,int height,int age){
        double result =  (9.99*weight)+(6.25*height)-(4.92*age)+5;
        return Math.ceil(result);
    }

    public  double rmrFemale(int weight,int height,int age){
        double result =  (9.99*weight)+(6.25*height)-(4.92*age)-161;
        return Math.ceil(result);
    }

    public  double bmiCalculate(int weight,int height){
        double result;
        float high = height;
        float kg = weight;
        result = high/100;
        result = result*result;
        result = weight/result;
        return Math.ceil(result);
    }

}
