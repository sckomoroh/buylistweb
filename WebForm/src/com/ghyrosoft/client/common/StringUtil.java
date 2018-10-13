package com.ghyrosoft.client.common;

/**
 * User: sckomoroh
 * Date: 1/26/13
 * Time: 3:12 PM
 */
public class StringUtil {
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
            String target = arg[i].toString();
            format = format.replace(plc, target);
        }

        return format;
    }
}
