package com.ghyrosoft.server.common;

public class StringUtil
{
    public static String format(String format, Object... arg)
    {
        int placeHolders = 0;
        int index;

        if (arg == null)
        {
            return format;
        }

        do
        {
            String plc = "{" + placeHolders + "}";
            index = format.indexOf(plc);
            if (index != -1)
            {
                placeHolders++;
            }
        }
        while (index != -1);

        if (arg.length < placeHolders)
        {
            return format;
        }

        for (int i=0; i<placeHolders; i++)
        {
            String plc = "{" + i + "}";
            
            String target;
            if (arg[i] == null)
            {
            	target = "<NULL>";
            }
            else
            {
            	target = arg[i].toString();
            }
            
            format = format.replace(plc, target);
        }

        return format;
    }
}
