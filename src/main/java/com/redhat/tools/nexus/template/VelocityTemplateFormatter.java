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

package com.redhat.tools.nexus.template;

import static com.redhat.tools.nexus.request.PathUtils.joinFile;
import static com.redhat.tools.nexus.request.PathUtils.joinPath;
import static org.codehaus.plexus.util.FileUtils.fileRead;
import static org.codehaus.plexus.util.IOUtil.close;
import static org.codehaus.plexus.util.IOUtil.copy;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.codehaus.plexus.velocity.VelocityComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.configuration.application.ApplicationConfiguration;

import javax.inject.Inject;
import javax.inject.Named;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named( "velocity" )
public class VelocityTemplateFormatter
    implements TemplateFormatter, Initializable
{

    private static final String DEFAULT_TEMPLATE_NAME = "default.vm";

    private static final Logger logger = LoggerFactory.getLogger( VelocityTemplateFormatter.class );

    private ClassLoader localLoader;

    private static final String TEMPLATE_RESOURCE_MARKER_PATH = "META-INF/nexus/template-resource-marker.txt";

    @Inject
    private ApplicationConfiguration configuration;

    @Inject
    private VelocityComponent velocityComponent;

    private VelocityEngine velocity;

    private File templatesDir;

    public VelocityTemplateFormatter()
    {
    }

    // for testing.
    protected VelocityTemplateFormatter( final VelocityEngine velocity, final File templatesDir )
    {
        this.velocity = velocity;
        this.templatesDir = templatesDir;
    }

    @Override
    public String format( final String templateBasepath, String templateName, final Map<String, Object> context )
        throws TemplateException
    {
        init();

        if ( templateName == null )
        {
            templateName = DEFAULT_TEMPLATE_NAME;
        }
        else if ( !templateName.endsWith( ".vm" ) )
        {
            templateName += ".vm";
        }

        final String templatePath = joinPath( '/', templateBasepath, templateName );
        final VelocityContext ctx = new VelocityContext( new HashMap<String, Object>( context ) );
        final StringWriter writer = new StringWriter();

        final String template = readTemplate( templatePath );

        if ( template == null )
        {
            throw new TemplateException( "Template not found: %s", templateName );
        }

        try
        {
            velocity.evaluate( ctx, writer, templateName, template );
        }
        catch ( final ResourceNotFoundException e )
        {
            throw new TemplateException(
                                         "Failed to render capture session using Velocity template.\nTemplate: %s\nReason: %s",
                                         e, templatePath, e.getMessage() );
        }
        catch ( final ParseErrorException e )
        {
            throw new TemplateException(
                                         "Failed to render capture session using Velocity template.\nTemplate: %s\nReason: %s",
                                         e, templatePath, e.getMessage() );
        }
        catch ( final MethodInvocationException e )
        {
            throw new TemplateException(
                                         "Failed to render capture session using Velocity template.\nTemplate: %s\nReason: %s",
                                         e, templatePath, e.getMessage() );
        }
        catch ( final IOException e )
        {
            throw new TemplateException(
                                         "Failed to render capture session using Velocity template.\nTemplate: %s\nReason: %s",
                                         e, templatePath, e.getMessage() );
        }

        logger.info( "rendered output:\n\n" + writer.toString() + "\n\n" );

        return writer.toString();
    }

    private synchronized void init()
    {
        if ( velocity == null && velocityComponent != null )
        {
            velocity = velocityComponent.getEngine();
        }

        logger.info( "Checking template directory..." );
        if ( templatesDir == null && configuration != null )
        {
            templatesDir = joinFile( configuration.getConfigurationDirectory(), "templates" );
            try
            {
                final File d = templatesDir.getCanonicalFile();
                templatesDir = d;
            }
            catch ( final IOException e )
            {
            }

            if ( !templatesDir.exists() )
            {
                templatesDir.mkdirs();
            }

            logger.info( "Template directory: " + templatesDir );
        }
    }

    private String readTemplate( final String templatePath )
        throws TemplateException
    {
        String template = null;

        if ( templatesDir != null && templatesDir.exists() && templatesDir.isDirectory() )
        {
            File templateFile;
            if ( templatePath.startsWith( templatesDir.getName() ) )
            {
                templateFile = new File( templatesDir.getParentFile(), templatePath );
            }
            else
            {
                templateFile = new File( templatesDir, templatePath );
            }

            if ( templateFile.exists() && templateFile.isFile() )
            {
                try
                {
                    template = fileRead( templateFile );
                }
                catch ( final IOException e )
                {
                    throw new TemplateException( "Failed to read Velocity template from: %s\nReason: %s", e,
                                                 templateFile.getAbsolutePath(), e.getMessage() );
                }
            }
            else
            {
                if ( logger.isDebugEnabled() )
                {
                    logger.debug( String.format( "Template: %s NOT FOUND in directory: %s", templatePath, templatesDir ) );
                }
            }
        }

        if ( template == null )
        {
            final List<ClassLoader> cloaders = new ArrayList<ClassLoader>();
            if ( localLoader != null )
            {
                cloaders.add( localLoader );
            }
            cloaders.add( Thread.currentThread().getContextClassLoader() );

            for ( final ClassLoader cloader : cloaders )
            {
                final URL resource = cloader.getResource( templatePath );
                if ( resource != null )
                {
                    final StringWriter sw = new StringWriter();
                    InputStream stream = null;
                    try
                    {
                        stream = resource.openStream();
                        copy( stream, sw );

                        template = sw.toString();
                    }
                    catch ( final IOException e )
                    {
                        throw new TemplateException( "Failed to read Velocity template from: %s\nReason: %s", e,
                                                     resource.toExternalForm(), e.getMessage() );
                    }
                    finally
                    {
                        close( stream );
                    }
                }
                else
                {
                    if ( logger.isDebugEnabled() )
                    {
                        logger.debug( String.format( "Template: %s NOT FOUND in classloader: %s", templatePath, cloader ) );
                    }
                }

                if ( template != null )
                {
                    break;
                }
            }
        }

        return template;
    }

    @Override
    public void initialize()
        throws InitializationException
    {
        ClassLoader ucl = null;

        final ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = null;
        try
        {
            resources = cl.getResources( TEMPLATE_RESOURCE_MARKER_PATH );
        }
        catch ( final IOException e )
        {
            logger.error( "Cannot find classpath URL for: " + TEMPLATE_RESOURCE_MARKER_PATH
                + "; using thread-context classpath instead.\nReason: " + e.getMessage() );
        }

        if ( resources != null )
        {
            final List<URL> urls = new ArrayList<URL>();
            while ( resources.hasMoreElements() )
            {
                final URL resource = resources.nextElement();
                String path = resource.toExternalForm();
                final int idx = path.indexOf( '!' );
                if ( path.startsWith( "jar:" ) && idx > -1 )
                {
                    path = path.substring( "jar:".length(), idx );
                }

                try
                {
                    urls.add( new URL( path ) );
                }
                catch ( final MalformedURLException e )
                {
                    logger.error( String.format( "Cannot create base URL for local plugin jar: %s\nReason: %s", path,
                                                 e.getMessage() ), e );
                }
            }

            if ( urls != null && !urls.isEmpty() )
            {
                ucl = new URLClassLoader( urls.toArray( new URL[] {} ) );
            }
        }
        else
        {
            logger.error( "Cannot find classpath URL for: " + TEMPLATE_RESOURCE_MARKER_PATH
                + "; using thread-context classpath instead." );
        }

        localLoader = ucl == null ? cl : ucl;
    }

}
