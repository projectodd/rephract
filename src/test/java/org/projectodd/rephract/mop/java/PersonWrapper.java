package org.projectodd.rephract.mop.java;

public class PersonWrapper {
    private final Person inner;

    public PersonWrapper(Person inner) {
        this.inner = inner;
    }

    public Person getInner(){
        return inner;
    }
}
