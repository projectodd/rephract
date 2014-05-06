package org.projectodd.rephract.java;

/**
 * @author Bob McWhirter
 */
public class MockObject {

    public String stringFoo;
    public Integer intFoo;
    public Long longFoo;

    public MockObject() {
        reset();
    }

    public void reset() {
        this.stringFoo = null;
        this.intFoo = null;
        this.longFoo = null;
    }

    public void foo(String arg) {
        this.stringFoo = arg;
    }

    public void foo(int arg) {
        this.intFoo = arg;
    }

    public void foo(long arg) {
        this.longFoo = arg;
    }
}
