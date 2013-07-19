package org.projectodd.rephract;

import java.io.FileWriter;
import java.io.IOException;

public class FileLinkLogger implements LinkLogger {
    
    private FileWriter out;

    public FileLinkLogger(String filename) throws IOException {
        this.out = new FileWriter(filename);
    }
    
    public void close() throws IOException {
        this.out.close();
    }

    @Override
    public void log(String message, Object... arguments) {
        try {
            out.write( String.format(message, arguments) + "\n" );
            out.flush();
        } catch (IOException e) {
            System.err.println( message );
        }
    }

}
