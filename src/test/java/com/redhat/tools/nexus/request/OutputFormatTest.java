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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.restlet.data.MediaType;

public class OutputFormatTest
{

    @Test
    public void retrieveTextPlainUsingPlainFormatId()
    {
        assertEquals( OutputFormat.plain, OutputFormat.find( "plain" ) );
    }

    @Test
    public void retrieveTextPlainUsingTextFormatId()
    {
        assertEquals( OutputFormat.plain, OutputFormat.find( "text" ) );
    }

    @Test
    public void retrieveTextPlainUsingTextPlainMediaType()
    {
        assertEquals( OutputFormat.plain, OutputFormat.find( MediaType.TEXT_PLAIN.getName() ) );
    }

    @Test
    public void retrieveAppXmlUsingXMLFormatId()
    {
        assertEquals( OutputFormat.xml, OutputFormat.find( "XML" ) );
    }

    @Test
    public void retrieveAppXmlUsing_xml_FormatId()
    {
        assertEquals( OutputFormat.xml, OutputFormat.find( "xml" ) );
    }

    @Test
    public void retrieveAppXmlUsingAppXmlMediaType()
    {
        assertEquals( OutputFormat.xml, OutputFormat.find( MediaType.APPLICATION_XML.getName() ) );
    }

    @Test
    public void retrieveAppXmlUsingTextXmlMediaType()
    {
        assertEquals( OutputFormat.xml, OutputFormat.find( MediaType.TEXT_XML.getName() ) );
    }

    @Test
    public void retrieveAppJsonUsingJSONFormatId()
    {
        assertEquals( OutputFormat.json, OutputFormat.find( "JSON" ) );
    }

    @Test
    public void retrieveAppJsonUsing_json_FormatId()
    {
        assertEquals( OutputFormat.json, OutputFormat.find( "json" ) );
    }

    @Test
    public void retrieveAppJsonUsingAppJsonMediaType()
    {
        assertEquals( OutputFormat.json, OutputFormat.find( MediaType.APPLICATION_JSON.getName() ) );
    }

}
