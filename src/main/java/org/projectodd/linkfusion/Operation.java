package org.projectodd.linkfusion;

public interface Operation {
    
    public enum Type {
        GET_PROPERTY,
        SET_PROPERTY,
        GET_ELEMENT,
        SET_ELEMENT,
        GET_METHOD,
        CALL,
        CONSTRUCT
    }
    
    Type getType();
    String getParameter();

}
