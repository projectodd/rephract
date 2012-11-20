package org.projectodd.linkfusion.strategy.javabeans;

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
    
    public void melt() {
        
    }

}
