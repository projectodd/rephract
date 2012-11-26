package org.projectodd.linkfusion.mop.java;

public class Cheese {
    
    private String name;
    private int age;

    public Cheese(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public int getAge() {
        return this.age;
    }
    
    public String melt(String forRecipe) {
        return "melting for: " + forRecipe;
    }
    
    public String melt(Person byPerson) {
        return "melted by: " + byPerson.getName();
    }

}
