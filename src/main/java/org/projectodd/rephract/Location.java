package org.projectodd.rephract;

/**
* @author Bob McWhirter
*/
public class Location {
    public final String file;
    public final int line;
    public final int column;

    public Location(String file, int line, int column) {
        this.file = file;
        this.line = line;
        this.column = column;
    }

    public String toString() {
        return this.file + ":" + this.line + ":" + this.column;
    }

}
