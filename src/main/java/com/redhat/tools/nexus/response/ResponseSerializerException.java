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

import java.text.MessageFormat;

public class ResponseSerializerException
    extends Exception
{
    private static final long serialVersionUID = 1L;

    private Object[] params;

    public ResponseSerializerException( final String messageFormat, final Throwable cause, final Object... params )
    {
        super( messageFormat, cause );
        this.params = params;
    }

    public ResponseSerializerException( final String messageFormat, final Object... params )
    {
        super( messageFormat );
        this.params = params;
    }

    public ResponseSerializerException( final String message, final Throwable cause )
    {
        super( message, cause );
    }

    public ResponseSerializerException( final String message )
    {
        super( message );
    }

    @Override
    public String getLocalizedMessage()
    {
        return getMessage();
    }

    @Override
    public String getMessage()
    {
        final String messageFormat = super.getMessage();
        if ( params == null )
        {
            return messageFormat;
        }
        else
        {
            try
            {
                return MessageFormat.format( messageFormat, params );
            }
            catch ( final Throwable t )
            {
                return messageFormat;
            }
        }
    }
}
