package org.dgfoundation.amp.reports.saiku.export;

import org.saiku.web.export.JSConverter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class AMPJSConverter extends JSConverter {

    private static final String UNDERSCORE_JS = "/../../TEMPLATE/ampTemplate/saikuui_reports/js/backbone/underscore.js";
    private static final String TABLE_RENDERER_JS = 
                    "/../../TEMPLATE/ampTemplate/saikuui_reports/js/saiku/render/AMPTableRenderer.js";
    
    private static final Logger logger = Logger.getLogger(AMPJSConverter.class);

    public static String convertToHtml(JsonBean jb, String type) throws IOException {
        logger.info("Start convertToHtml");
        ObjectMapper om = new ObjectMapper();
        StringWriter sw = new StringWriter();
        Context context = Context.enter();
        Scriptable globalScope = context.initStandardObjects();
        
        Reader underscoreReader = new InputStreamReader(AMPJSConverter.class.getResourceAsStream(UNDERSCORE_JS));
        context.evaluateReader(globalScope, underscoreReader, "underscore.js", 1, null);
        Reader ampReader = new InputStreamReader(AMPJSConverter.class.getResourceAsStream(TABLE_RENDERER_JS));
        context.evaluateReader(globalScope, ampReader, "AMPTableRenderer.js", 1, null);

        String data = om.writeValueAsString(jb);
        Object wrappedQr = Context.javaToJS(data, globalScope);
        ScriptableObject.putProperty(globalScope, "data", wrappedQr);
        Object wrappedOut = Context.javaToJS(sw, globalScope);
        ScriptableObject.putProperty(globalScope, "out", wrappedOut);
        String code = "eval('var cellset = ' + data); \nvar renderer = new AMPTableRenderer({type: '" + type
                + "'}); \nvar html = renderer.render(cellset, {}); out.write(html);";
        context.evaluateString(globalScope, code, "<mem>", 1, null);
        Context.exit();
        String content = sw.toString();
        logger.info("End convertToHtml");
        return content;
    }
}
