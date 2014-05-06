package org.projectodd.rephract.java;

/**
 * @author Bob McWhirter
 */
public class Person {

    private String name;
    private int age;

    public Person(String name, int age) {
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

    public void eat(String food) {

    }

    public void eat(int number) {

    }

    public void drink(String food) {

    }

    public void drink(int number) {

    }
}
