// classe do aluno da academia

package com.tuon.entities;

public class GymUser {

    private Integer id;
    private String name;
    private Integer age;
    private Double height;
    private Double weight;

    // construtor vazio
    public GymUser(){

    }

    // construtor padrão
    public GymUser(Integer id, String name, Integer age, Double height, Double weight) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    // cálculo de IMC
    public Double calculateBMI() {
        if (height == null || height == 0 || weight == null || weight == 0) {
            return 0.0; // Avoid division by zero
        }
        return weight / (height * height);
    }

    // classificação de IMC
    public String bmiClassification() {

        double bmi = calculateBMI();

        if (bmi == 0.0) {
            return "Unknown";
        }

        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 25) {
            return "Normal weight";
        } else if (bmi < 30) {
            return "Overweight";
        } else {
            return "Obesity";
        }
    }

    // getters e setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    // toString
    @Override
    public String toString() {
        return "GymUser { "
                + "id= " + getId() + ", " + "name= " + getName() + ", "
                + "age= " + getAge() + ", "
                + "height= " + getHeight() + "m, "
                + "weight= " + getWeight() + "kg, "
                + "bmi= " + String.format("%.2f", calculateBMI()) + ", "
                + "classification= " + bmiClassification() + " }";
    }
}
