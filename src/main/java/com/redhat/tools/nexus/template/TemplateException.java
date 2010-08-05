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

public class TemplateException
    extends Exception
{

    private static final long serialVersionUID = 1L;

    private Object[] params;

    public TemplateException( final String message, final Throwable cause )
    {
        super( message, cause );
    }

    public TemplateException( final String message )
    {
        super( message );
    }

    public TemplateException( final String message, final Throwable cause, final Object... params )
    {
        super( message, cause );
        this.params = params;
    }

    public TemplateException( final String message, final Object... params )
    {
        super( message );
        this.params = params;
    }

    @Override
    public String getLocalizedMessage()
    {
        return getMessage();
    }

    @Override
    public String getMessage()
    {
        if ( params == null )
        {
            return super.getMessage();
        }
        else
        {
            try
            {
                return String.format( super.getMessage(), params );
            }
            catch ( final Throwable t )
            {
                return super.getMessage();
            }
        }
    }

}
