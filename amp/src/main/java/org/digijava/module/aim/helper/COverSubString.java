package org.digijava.module.aim.helper ;

public class COverSubString
{
    public static String getCOverSubString(String cstring , char flag)
    {
        String text=cstring;
        if(flag == 'D' && cstring.length()>200)
                text=cstring.substring(0,199);
        if(flag == 'O' && cstring.length()>50)
                text=cstring.substring(0,49);
        return text;
    }

    public static int getCOverSubStringLength(String cstring , char flag)
    {
        int lflag=0;
        if(flag == 'D' && cstring.length()>200)
                lflag=1;
        if(flag == 'O' && cstring.length()>50)
                lflag=2;
        return lflag;
    }
}
