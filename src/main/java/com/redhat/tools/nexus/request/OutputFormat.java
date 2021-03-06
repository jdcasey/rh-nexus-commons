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

package com.redhat.tools.nexus.request;

import org.restlet.data.MediaType;

import java.util.Arrays;

public enum OutputFormat
{

    plain( MediaType.TEXT_PLAIN, "text" ),
    xml( MediaType.APPLICATION_XML, MediaType.TEXT_XML.getName() ),
    json( MediaType.APPLICATION_JSON );

    private MediaType mt;

    private final String[] synonyms;

    private OutputFormat( final MediaType mt, final String... synonyms )
    {
        this.mt = mt;
        this.synonyms = synonyms;
        Arrays.sort( this.synonyms );
    }

    public MediaType mediaType()
    {
        return mt;
    }

    public String[] synonyms()
    {
        return synonyms;
    }

    public static OutputFormat find( final String fmt )
    {
        if ( fmt != null )
        {
            final MediaType mt = MediaType.valueOf( fmt );

            for ( final OutputFormat format : values() )
            {
                if ( mt != null && format.mediaType().equals( mt ) )
                {
                    return format;
                }
                else if ( fmt.toLowerCase().equals( format.name() ) )
                {
                    return format;
                }
                else if ( format.synonyms != null && Arrays.binarySearch( format.synonyms, fmt.toLowerCase() ) > -1 )
                {
                    return format;
                }
            }
        }

        return null;
    }

    public String mimeType()
    {
        return mt.getName();
    }

}
