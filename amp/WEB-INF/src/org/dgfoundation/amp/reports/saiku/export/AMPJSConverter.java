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
import org.saiku.web.rest.objects.resultset.QueryResult;

public class AMPJSConverter extends JSConverter {

	private static final Logger logger = Logger.getLogger(AMPJSConverter.class);

	public static String convertToHtml(JsonBean jb, String type) throws IOException {
		logger.info("Start convertToHtml");
		ObjectMapper om = new ObjectMapper();
		StringWriter sw = new StringWriter();
		Context context = Context.enter();
		Scriptable globalScope = context.initStandardObjects();
		Reader underscoreReader = new InputStreamReader(
				AMPJSConverter.class
						.getResourceAsStream("/../../TEMPLATE/ampTemplate/saikuui/js/backbone/underscore.js"));
		context.evaluateReader(globalScope, underscoreReader, "underscore.js", 1, null);
		Reader ampReader = new InputStreamReader(
				AMPJSConverter.class
						.getResourceAsStream("/../../TEMPLATE/ampTemplate/saikuui/js/saiku/render/AMPTableRenderer.js"));
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
