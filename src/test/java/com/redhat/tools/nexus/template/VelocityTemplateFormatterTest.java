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

import static org.junit.Assert.assertEquals;

import org.apache.velocity.app.VelocityEngine;
import org.junit.Test;

import com.redhat.tools.nexus.template.TemplateException;
import com.redhat.tools.nexus.template.TemplateFormatter;
import com.redhat.tools.nexus.template.VelocityTemplateFormatter;

import edu.emory.mathcs.backport.java.util.Collections;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VelocityTemplateFormatterTest
{

    @Test
    public void format_directConstruction()
        throws Exception
    {
        final VelocityEngine velocity = new VelocityEngine();
        velocity.init();

        final VelocityTemplateFormatter formatter =
            new VelocityTemplateFormatter( velocity, new File( "does/not/exist" ) );

        assertFormatting( formatter );
    }

    @SuppressWarnings( "unchecked" )
    private void assertFormatting( final TemplateFormatter formatter )
        throws MalformedURLException, TemplateException
    {
        final String[] hosts = { "foo", "bar", "baz" };

        final List<URL> targets = new ArrayList<URL>();
        for ( final String host : hosts )
        {
            targets.add( new URL( "http://www." + host + ".com/path/to/artifact.jar" ) );
        }

        final Map<String, Object> ctx = Collections.singletonMap( "data", targets );

        final String result = formatter.format( TemplateFormatter.TEMPLATES_ROOT + "test", "default", ctx );

        final StringBuilder sb = new StringBuilder();
        for ( final String host : hosts )
        {
            sb.append( "http://www." ).append( host ).append( ".com/path/to/artifact.jar\n" );
        }

        System.out.println( result );

        assertEquals( sb.toString(), result );
    }

}
