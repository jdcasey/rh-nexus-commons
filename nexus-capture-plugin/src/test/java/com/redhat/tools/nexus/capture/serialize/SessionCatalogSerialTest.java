package com.redhat.tools.nexus.capture.serialize;

import static com.redhat.tools.nexus.capture.model.ModelSerializationUtils.getGson;
import static com.redhat.tools.nexus.capture.model.ModelSerializationUtils.getXStreamForStore;

import org.junit.Test;

import com.redhat.tools.nexus.capture.model.CaptureSession;
import com.redhat.tools.nexus.capture.model.CaptureSessionCatalog;

import java.io.File;

public class SessionCatalogSerialTest
{

    @Test
    public void roundTripJSON()
        throws InterruptedException
    {
        final String user = "user";
        final String buildTag = "tag";
        final String captureSource = "external";

        final CaptureSessionCatalog cat = new CaptureSessionCatalog( buildTag, captureSource, user );

        cat.add( new CaptureSession( user, buildTag, captureSource ).setFile( new File( "/path/to/session-1.json" ) ) );

        Thread.sleep( 2000 );

        cat.add( new CaptureSession( user, buildTag, captureSource ).setFile( new File( "/path/to/session-2.json" ) ) );

        final String result = getGson().toJson( cat );
        System.out.println( result );

        getGson().fromJson( result, CaptureSessionCatalog.class );
    }

    @Test
    public void roundTripXML()
        throws InterruptedException
    {
        final String user = "user";
        final String buildTag = "tag";
        final String captureSource = "external";

        final CaptureSessionCatalog cat = new CaptureSessionCatalog( buildTag, captureSource, user );

        cat.add( new CaptureSession( user, buildTag, captureSource ).setFile( new File( "/path/to/session-1.json" ) ) );

        Thread.sleep( 2000 );

        cat.add( new CaptureSession( user, buildTag, captureSource ).setFile( new File( "/path/to/session-2.json" ) ) );

        final String result = getXStreamForStore().toXML( cat );
        System.out.println( result );

        getXStreamForStore().fromXML( result );
    }
}