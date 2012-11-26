package org.projectodd.linkfusion.mop;

import org.junit.Before;
import org.junit.Test;
import org.projectodd.linkfusion.mop.java.JavaLinkStrategy;
import org.projectodd.linkfusion.mop.java.Person;

import static org.fest.assertions.Assertions.assertThat;


public class MetaObjectProtocolHandlerTest {
    
    private MetaObjectProtocolHandler handler;
    private Person javaBob;
    private LangObject langBob;

    @Before 
    public void setUp() throws Throwable {
        this.handler = new MetaObjectProtocolHandler();
        this.handler.addLinkStrategy( new MockContextualLinkStrategy() );
        this.handler.addLinkStrategy( new JavaLinkStrategy() );
        
        LangContext context = new LangContext();
        LangContext.setThreadContext(context);
        
        this.javaBob = new Person("bob", 39);
        this.langBob = new LangObject();
        
        this.langBob.put(context, "name", "lang: bob mcwhirter" );
        this.langBob.put(context, "age", 93 );
    }
    
    @Test
    public void testGetProperty() throws Throwable {
        
        Object result = null;
        
        result = handler.getProperty(javaBob, "name" );
        assertThat( result ).isEqualTo( "bob" );
        
        result = handler.getProperty(javaBob, "age" );
        assertThat( result ).isEqualTo( 39 );
        
        result = handler.getProperty(langBob, "name" );
        assertThat( result ).isEqualTo("lang: bob mcwhirter" );
        
        result = handler.getProperty(langBob, "age" );
        assertThat( result ).isEqualTo( 93 );
        
        result = handler.getProperty(javaBob, "name" );
        assertThat( result ).isEqualTo( "bob" );
        
        result = handler.getProperty(javaBob, "age" );
        assertThat( result ).isEqualTo( 39 );
        
        result = handler.getProperty(langBob, "name" );
        assertThat( result ).isEqualTo("lang: bob mcwhirter" );
        
        result = handler.getProperty(langBob, "age" );
        assertThat( result ).isEqualTo( 93 );
    }
    
    @Test
    public void testSetProperty() throws Throwable {
        Object result = null;
        
        handler.setProperty(javaBob, "name", "bob mcwhirter" );
        handler.setProperty(javaBob, "age", 40 );
        
        result = handler.getProperty(javaBob, "name" );
        assertThat( result ).isEqualTo( "bob mcwhirter" );
        
        result = handler.getProperty(javaBob, "age" );
        assertThat( result ).isEqualTo( 40 );
        
        assertThat( javaBob.getName() ).isEqualTo("bob mcwhirter" );
        assertThat( javaBob.getAge() ).isEqualTo( 40 );
        
        handler.setProperty( langBob, "name", "bob mcwhirter, esquire" );
        handler.setProperty( langBob, "age", 109 );
        
        result = handler.getProperty(langBob, "name" );
        assertThat( result ).isEqualTo("bob mcwhirter, esquire" );
        
        result = handler.getProperty(langBob, "age" );
        assertThat( result ).isEqualTo( 109 );
    }

}
