package org.digijava.module.translation.taglib;

import org.apache.commons.lang3.StringUtils;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.digijava.kernel.translator.TranslatorWorker;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

/**
 * @author Octavian Ciubotaru
 */
public class ColNameTrnTag extends BodyTagSupport {

    @Override
    public int doEndTag() throws JspException {
        try {
            String text = null;
            if (getBodyContent() != null) {
                text = getBodyContent().getString();
            }

            pageContext.getOut().print(translateColName(text));

            return super.doEndTag();
        } catch (IOException e) {
            throw new JspException("Could not write translation.", e);
        }
    }

    private String translateColName(String columnName) {
        if (columnName == null) {
            return "";
        }
        columnName = StringUtils.trim(columnName);

        NiReportColumn<?> niReportColumn = AmpReportsSchema.getInstance().getColumns().get(columnName);
        if (niReportColumn != null) {
            return niReportColumn.label.toString();
        } else {
            return TranslatorWorker.translateText(columnName);
        }
    }
}
