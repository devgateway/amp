package org.digijava.kernel.taglib.html;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.helper.EasternArabicService;

/**
 * @author Viorel Chihai
 */
public class EasternArabicNumberConverterTag extends BodyTagSupport {

    @Override
    public int doEndTag() throws JspException {
        try {
            String text = null;
            if (getBodyContent() != null) {
                text = getBodyContent().getString();
            }

            pageContext.getOut().print(convertNumber(text));

            return super.doEndTag();
        } catch (IOException e) {
            throw new JspException("Could not convert number.", e);
        }
    }

    private String convertNumber(String number) {
        if (number == null) {
            return "";
        }
        
        Locale currentLocale = TLSUtils.getCurrentSystemLocale();
        if (EasternArabicService.getInstance().isLocaleEasternArabic(currentLocale)) {
            return EasternArabicService.getInstance().convertWesternArabicToEasternArabic(number);
        }
        
        return number;
    }
}
