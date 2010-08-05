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

import java.util.Arrays;
import java.util.List;

public enum RequestMode
{

    TABLE_OF_CONTENTS( "LIST", "TOC" ),
    DEFAULT( "DEF" );

    // private static final Logger logger = LoggerFactory.getLogger( RequestMode.class );

    private List<String> tokens;

    private RequestMode( final String... tokens )
    {
        this.tokens = Arrays.asList( tokens );
    }

    public static RequestMode find( String m )
    {
        if ( m != null )
        {
            m = m.toUpperCase();
            for ( final RequestMode mode : values() )
            {
                if ( mode.tokens.contains( m ) || mode.toString().equals( m ) )
                {
                    return mode;
                }
            }
        }

        return DEFAULT;
    }

}
