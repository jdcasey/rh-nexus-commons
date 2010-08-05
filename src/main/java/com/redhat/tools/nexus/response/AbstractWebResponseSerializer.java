/*
 *  Copyright (c) 2010 Red Hat, Inc.
 *  
 *  This program is licensed to you under Version 3 only of the GNU
 *  General Public License as published by the Free Software 
 *  Foundation. This program is distributed in the hope that it will be 
 *  useful, but WITHOUT ANY WARRANTY; without even the implied 
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 *  PURPOSE.
 *  
 *  See the GNU General Public License Version 3 for more details.
 *  You should have received a copy of the GNU General Public License 
 *  Version 3 along with this program. 
 *  
 *  If not, see http://www.gnu.org/licenses/.
 */

package com.redhat.tools.nexus.response;

import static com.redhat.tools.nexus.request.RequestUtils.query;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;

import com.google.gson.Gson;
import com.redhat.tools.nexus.template.TemplateException;
import com.redhat.tools.nexus.template.TemplateFormatter;
import com.thoughtworks.xstream.XStream;

import javax.inject.Inject;
import javax.inject.Named;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractWebResponseSerializer
    implements WebResponseSerializer
{

    public static final String PARAM_TEMPLATE = "template";

    @Inject
    @Named( "velocity" )
    private TemplateFormatter templateFormatter;

    protected TemplateFormatter getTemplateFormatter()
    {
        return templateFormatter;
    }

    @Override
    public Representation serialize( final Object data, final MediaType mediaType, final Request request,
                                     final String templatesBasepath )
        throws ResourceException
    {
        String result = null;
        if ( mediaType == MediaType.APPLICATION_XML )
        {
            result = getXStream().toXML( data );
        }
        else if ( mediaType == MediaType.APPLICATION_JSON )
        {
            result = getGson().toJson( data );
        }
        else if ( mediaType == MediaType.TEXT_PLAIN )
        {
            final Map<String, Object> templateContext = new HashMap<String, Object>();
            templateContext.put( "data", data );

            final Form query = query( request );
            final String template = query.getFirstValue( PARAM_TEMPLATE );

            try
            {
                result = templateFormatter.format( templatesBasepath, template, templateContext );
            }
            catch ( final TemplateException e )
            {
                throw new ResourceException( Status.SERVER_ERROR_INTERNAL, e.getMessage(), e );
            }
        }
        else
        {
            throw new ResourceException( Status.CLIENT_ERROR_NOT_ACCEPTABLE );
        }

        return new StringRepresentation( result, mediaType );
    }

    protected abstract Gson getGson();

    protected abstract XStream getXStream();

}
