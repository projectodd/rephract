package org.projectodd.linkfusion;

import java.lang.invoke.MethodType;
import java.util.List;

public interface InvocationRequest {
    
    boolean isFusionRequest();
    List<Operation> getOperations();
    
    String getName();
    MethodType type();
    Object receiver();
    Object[] arguments();
    List<Object> argumentsList();
    
}
