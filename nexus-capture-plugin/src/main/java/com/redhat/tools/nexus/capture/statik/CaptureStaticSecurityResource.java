package com.redhat.tools.nexus.capture.statik;

import org.sonatype.security.realms.tools.AbstractStaticSecurityResource;
import org.sonatype.security.realms.tools.StaticSecurityResource;

import javax.inject.Named;

@Named( "captureSecurity" )
public class CaptureStaticSecurityResource
    extends AbstractStaticSecurityResource
    implements StaticSecurityResource
{

    @Override
    protected String getResourcePath()
    {
        return "/META-INF/nexus/capture-security.xml";
    }

}
