package com.redhat.rcm.nexus.capture.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.redhat.rcm.nexus.capture.serialize.CaptureSerializationConstants;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias( CaptureSerializationConstants.SESSION_ROOT )
public class CaptureSession
{

    @XStreamOmitField
    @Expose( deserialize = false, serialize = false )
    private File file;

    private final String user;

    @SerializedName( CaptureSerializationConstants.BUILD_TAG_FIELD )
    @XStreamAlias( CaptureSerializationConstants.BUILD_TAG_FIELD )
    private final String buildTag;

    @SerializedName( CaptureSerializationConstants.CAPTURE_SOURCE_FIELD )
    @XStreamAlias( CaptureSerializationConstants.CAPTURE_SOURCE_FIELD )
    private final String captureSource;

    @SerializedName( CaptureSerializationConstants.START_DATE_FIELD )
    @XStreamAlias( CaptureSerializationConstants.START_DATE_FIELD )
    private final Date started = new Date();

    @SerializedName( CaptureSerializationConstants.LAST_UPDATE_FIELD )
    @XStreamAlias( CaptureSerializationConstants.LAST_UPDATE_FIELD )
    private Date lastUpdated = started;

    private final List<CaptureTarget> targets;

    // used for gson deserialization
    @SuppressWarnings( "unused" )
    private CaptureSession()
    {
        this.user = null;
        this.buildTag = null;
        this.captureSource = null;
        this.targets = new ArrayList<CaptureTarget>();
    }

    public CaptureSession( final String user, final String buildTag, final String captureSource )
    {
        this.user = user;
        this.buildTag = buildTag;
        this.captureSource = captureSource;
        this.targets = new ArrayList<CaptureTarget>();
    }

    public CaptureSession add( final CaptureTarget record )
    {
        targets.add( record );
        lastUpdated = record.getResolutionDate();
        return this;
    }

    public Date getStartDate()
    {
        return started;
    }

    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    public List<CaptureTarget> getTargets()
    {
        return targets;
    }

    public String getUser()
    {
        return user;
    }

    public String getBuildTag()
    {
        return buildTag;
    }

    public String getCaptureSource()
    {
        return captureSource;
    }

    public File getFile()
    {
        return file;
    }

    public CaptureSession setFile( final File file )
    {
        this.file = file;
        return this;
    }

    public String key()
    {
        return key( buildTag, captureSource, user );
    }

    public static String key( final String buildTag, final String captureSource, final String user )
    {
        return String.format( "%s:%s:%s", user, buildTag, captureSource );
    }

}
