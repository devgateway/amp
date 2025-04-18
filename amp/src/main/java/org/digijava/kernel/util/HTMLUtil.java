package org.digijava.kernel.util;

import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;

public class HTMLUtil {
    /**
     * removes HTML formatting from a string, using a proper HTML parser
     * @param src
     * @param removeNewLines
     * @return
     */
    public final static String removeHtml(String src, boolean removeNewLines)
    {
        if (src == null)
            return null;
        src = src.trim();
        if (src.isEmpty())
            return null;
        
        Source htmlSource = new Source(src);
        Segment htmlSeg = new Segment(htmlSource, 0, htmlSource.length());
        Renderer htmlRend = new Renderer(htmlSeg);
        String result = htmlRend.toString();
        if (result == null)
            return null;
        result = result.trim();
        
        if (result.isEmpty())
            return null;
        
        if (removeNewLines)
            result = result.replace('\n', ' ').replace('\r', ' ').replaceAll("  ", " ");
        
        return result.replaceAll("  ", " ");
    }   


}
