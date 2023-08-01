package org.digijava.module.aim.helper;

import org.apache.taglibs.standard.resources.Resources;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import java.text.NumberFormat;

/**
 *
 * @author Mauricio Coria
 */

public class FormatHelperTag extends BodyTagSupport {

    //*********************************************************************
    // Protected state

    protected Object value;                    // 'value' attribute
    protected boolean valueSpecified;          // status
    protected boolean isGroupingUsed;          // 'groupingUsed' attribute
    protected boolean groupingUsedSpecified;
    protected int maxIntegerDigits;            // 'maxIntegerDigits' attribute
    protected boolean maxIntegerDigitsSpecified;
    protected int minIntegerDigits;            // 'minIntegerDigits' attribute
    protected boolean minIntegerDigitsSpecified;
    protected int maxFractionDigits;           // 'maxFractionDigits' attribute
    protected boolean maxFractionDigitsSpecified;
    protected int minFractionDigits;           // 'minFractionDigits' attribute
    protected boolean minFractionDigitsSpecified;

    //*********************************************************************
    // Constructor and initialization


    public FormatHelperTag() {
    super();
    init();
    }

    private void init() {
    valueSpecified = false;
    groupingUsedSpecified = false;
    maxIntegerDigitsSpecified = minIntegerDigitsSpecified = false;
    maxFractionDigitsSpecified = minFractionDigitsSpecified = false;
    }

    //*********************************************************************
    // Tag logic

    public int doEndTag() throws JspException {
    String formatted = null;
        Object input = null;

        // determine the input by...
        if (valueSpecified) {
        // ... reading 'value' attribute
        input = value;
    } else {
        // ... retrieving and trimming our body
        if (bodyContent != null && bodyContent.getString() != null)
            input = bodyContent.getString().trim();
    }

    if ((input == null) || input.equals("")) {
        // Spec says:
            // If value is null or empty, remove the scoped variable 
            // if it is specified (see attributes var and scope).
        return EVAL_PAGE;
    }

    /*
     * If 'value' is a String, it is first parsed into an instance of
     * java.lang.Number
     */
    if (input instanceof String) {
        try {
        if (((String) input).indexOf('.') != -1) {
            input = Double.valueOf((String) input);
        } else {
            input = Long.valueOf((String) input);
        }
        } catch (NumberFormatException nfe) {
        throw new JspException(
                    Resources.getMessage("FORMAT_NUMBER_PARSE_ERROR", input),
            nfe);
        }
    }


    // Create formatter 
    NumberFormat formatter = FormatHelper.getDecimalFormat();
        
    configureFormatter(formatter);
    
    formatted = formatter.format(input);


    try {
        pageContext.getOut().print(formatted);
    } catch (IOException ioe) {
    throw new JspTagException(ioe.toString(), ioe);
    }


    return EVAL_PAGE;
    }

    // Releases any resources we may have (or inherit)
    public void release() {
    init();
    }


    //*********************************************************************
    // Private utility methods

    /*
     * Applies the 'groupingUsed', 'maxIntegerDigits', 'minIntegerDigits',
     * 'maxFractionDigits', and 'minFractionDigits' attributes to the given
     * formatter.
     */
    private void configureFormatter(NumberFormat formatter) {
    if (groupingUsedSpecified)
        formatter.setGroupingUsed(isGroupingUsed);
    if (maxIntegerDigitsSpecified)
        formatter.setMaximumIntegerDigits(maxIntegerDigits);
    if (minIntegerDigitsSpecified)
        formatter.setMinimumIntegerDigits(minIntegerDigits);
    if (maxFractionDigitsSpecified)
        formatter.setMaximumFractionDigits(maxFractionDigits);
    if (minFractionDigitsSpecified)
        formatter.setMinimumFractionDigits(minFractionDigits);
    }

    //*********************************************************************
    // Accessor methods

    // 'value' attribute
    public void setValue(Object value) throws JspTagException {
        this.value = value;
    this.valueSpecified = true;
    }




    // 'groupingUsed' attribute
    public void setGroupingUsed(boolean isGroupingUsed)
    throws JspTagException {
        this.isGroupingUsed = isGroupingUsed;
    this.groupingUsedSpecified = true;
    }

    // 'maxIntegerDigits' attribute
    public void setMaxIntegerDigits(int maxDigits) throws JspTagException {
        this.maxIntegerDigits = maxDigits;
    this.maxIntegerDigitsSpecified = true;
    }

    // 'minIntegerDigits' attribute
    public void setMinIntegerDigits(int minDigits) throws JspTagException {
        this.minIntegerDigits = minDigits;
    this.minIntegerDigitsSpecified = true;
    }

    // 'maxFractionDigits' attribute
    public void setMaxFractionDigits(int maxDigits) throws JspTagException {
        this.maxFractionDigits = maxDigits;
    this.maxFractionDigitsSpecified = true;
    }

    // 'minFractionDigits' attribute
    public void setMinFractionDigits(int minDigits) throws JspTagException {
        this.minFractionDigits = minDigits;
    this.minFractionDigitsSpecified = true;
    }
    
}
