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

package com.redhat.tools.nexus.serial;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomFormatDateConverter
    implements Converter
{

    private final String format;

    public CustomFormatDateConverter( final String format )
    {
        this.format = format;
    }

    public void marshal( final Object source, final HierarchicalStreamWriter writer, final MarshallingContext context )
    {
        writer.setValue( new SimpleDateFormat( format ).format( (Date) source ) );
    }

    public Object unmarshal( final HierarchicalStreamReader reader, final UnmarshallingContext context )
    {
        final String value = reader.getValue();
        try
        {
            return new SimpleDateFormat( format ).parseObject( value );
        }
        catch ( final ParseException e )
        {
            throw new IllegalArgumentException( String.format( "Cannot parse date: '%s' using format: '%s'", value,
                                                               format ), e );
        }
    }

    public boolean canConvert( @SuppressWarnings( "rawtypes" ) final Class type )
    {
        return Date.class.isAssignableFrom( type );
    }

}
