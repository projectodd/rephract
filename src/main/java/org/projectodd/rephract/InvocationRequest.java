package org.projectodd.rephract;

import java.lang.invoke.MethodType;
import java.util.List;

public interface InvocationRequest {
    
    boolean isRephractRequest();
    Operation getOperation();
    
    String getName();
    MethodType type();
    Object receiver();
    Object[] arguments();
    List<Object> argumentsList();
    
}
