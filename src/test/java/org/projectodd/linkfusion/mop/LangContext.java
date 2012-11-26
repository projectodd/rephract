package org.projectodd.linkfusion.mop;


public class LangContext {
    
    private static ThreadLocal<LangContext> threadContext = new ThreadLocal<>();
    
    public static void setThreadContext(LangContext context) {
        threadContext.set(context);
    }
    
    public static LangContext getThreadContext() {
        return threadContext.get();
    }
    
}
