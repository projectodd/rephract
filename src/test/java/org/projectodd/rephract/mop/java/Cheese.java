package org.projectodd.rephract.mop.java;

import java.util.Arrays;

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
    
    public String melt(String[] forRecipes) {
        return "melting for: " + Arrays.toString( forRecipes );
    }
    
    public String melt(Person byPerson) {
        return "melted by: " + byPerson.getName();
    }

    public String shred(String forRecipe) {
        return "shredding for: " + forRecipe;
    }

    public static String slice(String forRecipe) {
        return "slicing for: " + forRecipe;
    }

    public static String grate(String forRecipe) {
        return "grating for: " + forRecipe;
    }

}
