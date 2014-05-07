package org.projectodd.rephract.java;

/**
 * @author Bob McWhirter
 */
public class Dog {

    public static String translation = "Chien";

    public static String bark(String language) {
        return "bark in " +language + "!";
    }

    private String name;
    private int age;

    public Dog(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
