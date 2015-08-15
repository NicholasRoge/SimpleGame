package com.strixa.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by nicho on 8/14/2015.
 */
public class ResourceUtil
{
    public static String ReadRawResourceAsString(Context context,String resource_name)
    {
        return ReadRawResourceAsString(context,resource_name,context.getPackageName());
    }

    public static String ReadRawResourceAsString(Context context,String resource_name,String package_name)
    {
        int resource_id;


        resource_id = context.getResources().getIdentifier(resource_name,"raw",package_name);
        if(resource_id == 0)
        {
            Log.e("[SG]","Failed to read resource.  Resource with name \"" + package_name + ":raw/" + resource_name + "\" could not be found.");

            return null;
        }

        return ReadRawResourceAsString(context,resource_id);
    }

    public static String ReadRawResourceAsString(Context context,int resource_id)
    {
        byte        buffer[];
        InputStream stream;


        try
        {
            stream = context.getResources().openRawResource(resource_id);
        }
        catch(Resources.NotFoundException e)
        {
            Log.e("[SG]","Failed to read resource.  Resource with resource id of \"" + resource_id + "\" could not be found.");

            return null;
        }

        try
        {
            buffer = new byte[stream.available()];
            stream.read(buffer);
            stream.close();
        }
        catch(IOException e)
        {
            Log.e("[SG]","Failed to read resource.  An IOException occurred while attempting to read the resource.  IOException Message:  " + e.getMessage());

            return null;
        }

        return new String(buffer);
    }
}
