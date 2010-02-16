package com.redhat.rcm.nexus.capture;

import static com.redhat.rcm.nexus.capture.request.RequestUtils.headers;
import static com.redhat.rcm.nexus.capture.request.RequestUtils.mediaTypeOf;
import static com.redhat.rcm.nexus.capture.request.RequestUtils.query;

import java.util.List;

import org.codehaus.plexus.component.annotations.Component;
import org.jsecurity.SecurityUtils;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import org.sonatype.nexus.rest.AbstractNexusPlexusResource;
import org.sonatype.plexus.rest.resource.PathProtectionDescriptor;
import org.sonatype.plexus.rest.resource.PlexusResource;

@Component( role = PlexusResource.class, hint = "CaptureEmailLogResource" )
public class CaptureEchoResource
    extends AbstractNexusPlexusResource
    implements PlexusResource
{

    @Override
    public Object getPayloadInstance()
    {
        // if you allow PUT or POST you would need to return your object.
        return null;
    }

    @Override
    public PathProtectionDescriptor getResourceProtection()
    {
        return new PathProtectionDescriptor( "/capture/*/*/echo",
                                             String.format( "authcBasic,perms[%s]",
                                                            CaptureResourceConstants.CAPTURE_PERMISSION ) );
    }

    @Override
    public String getResourceUri()
    {
        return "/capture/{" + CaptureResourceConstants.BUILD_TAG_REPO_ID_KEY + "}/{"
                        + CaptureResourceConstants.CAPTURE_SOURCE_REPO_ID_KEY + "}/echo";
    }

    @Override
    public List<Variant> getVariants()
    {
        final List<Variant> result = super.getVariants();

        result.add( new Variant( MediaType.TEXT_PLAIN ) );

        return result;
    }

    @Override
    public Object get( final Context context, final Request request, final Response response, final Variant variant )
        throws ResourceException
    {
        final String buildTag =
            request.getAttributes().get( CaptureResourceConstants.BUILD_TAG_REPO_ID_KEY ).toString();

        final String captureSource =
            request.getAttributes().get( CaptureResourceConstants.CAPTURE_SOURCE_REPO_ID_KEY ).toString();

        final Form headers = headers( request );
        final Form query = query( request );

        final StringBuilder sb = new StringBuilder();

        sb.append( "\nHandler Instance: " ).append( this );
        sb.append( "\nBuild Tag: " ).append( buildTag );
        sb.append( "\nCapture Source: " ).append( captureSource );
        sb.append( "\nRoot URL: " ).append( request.getRootRef() );
        sb.append( "\nUser: " ).append( SecurityUtils.getSubject().getPrincipal() );

        final MediaType mt = mediaTypeOf( request, variant );
        sb.append( "\nInbound Response Media Type: " ).append( variant.getMediaType() );
        sb.append( "\nOverride Response Media Type: " ).append( mt == variant.getMediaType() ? "none" : mt );

        sb.append( "\n\nHTTP Headers:\n" );
        for ( final Parameter parameter : headers )
        {
            sb.append( "\n" ).append( parameter.getName() ).append( " = " ).append( parameter.getValue() );
        }

        sb.append( "\n\nQuery Parameters:\n" );
        for ( final Parameter parameter : query )
        {
            sb.append( "\n" ).append( parameter.getName() ).append( " = " ).append( parameter.getValue() );
        }

        if ( mt != MediaType.TEXT_PLAIN )
        {
            response.setStatus( Status.CLIENT_ERROR_NOT_ACCEPTABLE );
        }

        return new StringRepresentation( sb.toString(), mt );
    }

    @Override
    public Object post( final Context context, final Request request, final Response response, final Object payload )
        throws ResourceException
    {
        throw new ResourceException( Status.CLIENT_ERROR_METHOD_NOT_ALLOWED );
    }

    @Override
    public Object put( final Context context, final Request request, final Response response, final Object payload )
        throws ResourceException
    {
        throw new ResourceException( Status.CLIENT_ERROR_METHOD_NOT_ALLOWED );
    }

    @Override
    public void delete( final Context context, final Request request, final Response response )
        throws ResourceException
    {
        throw new ResourceException( Status.CLIENT_ERROR_METHOD_NOT_ALLOWED );
    }
}