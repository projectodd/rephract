package org.projectodd.linkfusion;

import java.lang.invoke.MethodType;
import java.util.List;

public interface InvocationRequest {
    
    boolean isFusionRequest();
    Operation getOperation();
    
    String getName();
    MethodType type();
    Object receiver();
    Object[] arguments();
    List<Object> argumentsList();
    
}
