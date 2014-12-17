package org.dgfoundation.amp.reports.saiku.export;

import org.saiku.web.export.JSConverter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import org.codehaus.jackson.map.ObjectMapper;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.saiku.web.rest.objects.resultset.QueryResult;

public class AMPJSConverter extends JSConverter {
    public static String convertToHtml(QueryResult qr, boolean wrapcontent) throws IOException {
        ObjectMapper om = new ObjectMapper();
        StringWriter sw = new StringWriter();
        Context context = Context.enter();
        Scriptable globalScope = context.initStandardObjects();
        Reader underscoreReader = new InputStreamReader(AMPJSConverter.class.getResourceAsStream("/../../TEMPLATE/ampTemplate/saikuui/js/backbone/underscore.js"));
        context.evaluateReader(globalScope, underscoreReader, "underscore.js", 1, null);
        Reader srReader = new InputStreamReader(AMPJSConverter.class.getResourceAsStream("/../../TEMPLATE/ampTemplate/saikuui/js/saiku/render/SaikuRendererExport.js"));
        context.evaluateReader(globalScope, srReader, "SaikuRendererExport.js", 1, null);
        Reader strReader = new InputStreamReader(AMPJSConverter.class.getResourceAsStream("/../../TEMPLATE/ampTemplate/saikuui/js/saiku/render/SaikuTableRendererExport.js"));
        context.evaluateReader(globalScope, strReader, "SaikuTableRendererExport.js", 1, null);
        String data = om.writeValueAsString(qr);
        Object wrappedQr = Context.javaToJS(data, globalScope);
        ScriptableObject.putProperty(globalScope, "data", wrappedQr);
        Object wrappedOut = Context.javaToJS(sw, globalScope);
        ScriptableObject.putProperty(globalScope, "out", wrappedOut);
        String code = "eval('var cellset = ' + data); \nvar renderer = new SaikuTableRenderer(); \nvar html = renderer.render(cellset, { wrapContent : " + wrapcontent + " }); out.write(html);";
        context.evaluateString(globalScope, code, "<mem>", 1, null);
        Context.exit();
        String content = sw.toString();
        return content;
    }
    public static String convertToHtml(QueryResult qr) throws IOException {
        return convertToHtml(qr, false);
    }

}
