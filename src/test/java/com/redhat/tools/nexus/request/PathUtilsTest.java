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

import static com.redhat.tools.nexus.request.PathUtils.buildUri;
import static com.redhat.tools.nexus.request.PathUtils.joinFile;
import static com.redhat.tools.nexus.request.PathUtils.joinPath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.File;

public class PathUtilsTest
{

    @Test
    public void buildUri_ReturnNullWhenBasePathIsNull()
    {
        assertNull( buildUri( null, "path", "to", "something" ) );
    }

    @Test
    public void buildUri_ConcatenatePathPartsNotAlreadyTerminatedInSlash()
    {
        assertEquals( "http://www.google.com/path/to/something", buildUri( "http://www.google.com", "path", "to",
                                                                           "something" ) );
    }

    @Test
    public void buildUri_ConcatenatePathPartsWithMixedSlashTerminals()
    {
        assertEquals( "http://www.google.com/path/to/something", buildUri( "http://www.google.com/", "/path", "to/",
                                                                           "something" ) );
    }

    @Test
    public void joinPath_ReturnNullWhenBasePathIsNull()
    {
        assertNull( joinPath( '/', null, "path", "to", "something" ) );
    }

    @Test
    public void joinPath_ConcatenatePathPartsNotAlreadyTerminatedInSlash()
    {
        assertEquals( "http://www.google.com/path/to/something", joinPath( '/', "http://www.google.com", "path", "to",
                                                                           "something" ) );
    }

    @Test
    public void joinPath_ConcatenatePathPartsWithMixedSlashTerminals()
    {
        assertEquals( "http://www.google.com/path/to/something", joinPath( '/', "http://www.google.com/", "/path",
                                                                           "to/", "something" ) );
    }

    @Test
    public void joinFile_ReturnNullWhenBaseDirIsNull()
    {
        assertNull( joinFile( (File) null, "path", "to", "something" ) );
    }

    @Test
    public void joinFile_ConcatenatePathPartsNotAlreadyTerminatedInSlash()
    {
        assertEquals( new File( formatPlatform( "/tmp/path/to/something" ) ),
                      joinFile( new File( formatPlatform( "/tmp" ) ), "path", "to", "something" ) );
    }

    @Test
    public void joinFile_ConcatenatePathPartsWithMixedSlashTerminals()
    {
        assertEquals( new File( formatPlatform( "/tmp/path/to/something" ) ),
                      joinFile( new File( formatPlatform( "/tmp" ) ), formatPlatform( "/path" ),
                                formatPlatform( "to/" ), "something" ) );
    }

    private String formatPlatform( final String src )
    {
        if ( File.separatorChar != '/' )
        {
            return src.replace( '/', File.separatorChar );
        }

        return src;
    }

}
