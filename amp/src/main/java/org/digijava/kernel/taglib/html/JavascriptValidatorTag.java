/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.kernel.taglib.html;

import org.apache.commons.validator.*;
import org.apache.commons.validator.util.ValidatorUtils;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.Resources;
import org.apache.struts.validator.ValidatorPlugIn;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.taglib.util.TagUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.util.*;

public class JavascriptValidatorTag
    extends org.apache.struts.taglib.html.JavascriptValidatorTag {

    private static final long serialVersionUID = 1L;
    private String htmlBeginComment = "\n<!-- Begin \n";

    /**
     * Constructs the beginning &lt;script&gt; element depending on xhtml status.
     */
    private String getStartElement() {

//        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        StringBuffer start = new StringBuffer(
            "<script type=\"text/javascript\"");
        start.append("src=\"");
        String src = TagUtil.calculateURL("module/common/js/validator.js", false,
                                          pageContext);
        start.append(src);
        start.append("\">");
        start.append("</script>\n\r");
        start.append("<script type=\"text/javascript\"");

        // there is no language attribute in xhtml
        if (!this.isXhtml()) {
            start.append(" language=\"Javascript1.1\"");
        }

        if (this.src != null) {
            start.append(" src=\"" + src + "\"");
        }

        start.append("> \n");
        return start.toString();
    }

    /**
     * Returns the opening script element and some initial javascript.
     */
    protected String getJavascriptBegin(String methods) {
        StringBuffer sb = new StringBuffer();
        String name =
            formName.substring(0, 1).toUpperCase()
            + formName.substring(1, formName.length());

        sb.append(this.getStartElement());

        if (this.isXhtml() && "true".equalsIgnoreCase(this.cdata)) {
            sb.append("<![CDATA[\r\n");
        }

        if (!this.isXhtml() && "true".equals(htmlComment)) {
            sb.append(htmlBeginComment);
        }
        sb.append("\n     var bCancel = false; \n\n");

        if (methodName == null || methodName.length() == 0) {
            sb.append(
                "    function validate"
                + name
                +
                "(form) {                                                                   \n");
        }
        else {
            sb.append(
                "    function "
                + methodName
                +
                "(form) {                                                                   \n");
        }
        sb.append("        if (bCancel) \n");
        sb.append("      return true; \n");
        sb.append("        else \n");

        // Always return true if there aren't any Javascript validation methods
        if (methods == null || methods.length() == 0) {
            sb.append("       return true; \n");
        }
        else {
            sb.append("       return " + methods + "; \n");
        }

        sb.append("   } \n\n");

        return sb.toString();
    }

    protected String getJavascriptStaticMethods(ValidatorResources resources) {
        return new String("/* Methods are in validator.js */");
    }

    /**
     * Returns true if this is an xhtml page.
     */
    private boolean isXhtml() {
        return TagUtils.getInstance().isXhtml(this.pageContext);
    }

    /**
     *
     * @return int
     * @throws JspException
     */
    public int doStartTag() throws JspException {
        StringBuffer results = new StringBuffer();

        ModuleConfig config =  TagUtils.getInstance().getModuleConfig(pageContext);
        ValidatorResources resources =
            (ValidatorResources) pageContext.getAttribute(
                ValidatorPlugIn.VALIDATOR_KEY + config.getPrefix(),
                PageContext.APPLICATION_SCOPE);

        Locale locale = TagUtils.getInstance().getUserLocale(this.pageContext, null);

        Form form = resources.getForm(locale, formName);
        if (form != null) {
            if ("true".equalsIgnoreCase(dynamicJavascript)) {
                MessageResources messages =
                    (MessageResources) pageContext.getAttribute(
                        bundle + config.getPrefix(),
                        PageContext.APPLICATION_SCOPE);

                List lActions = new ArrayList();
                List lActionMethods = new ArrayList();

                // Get List of actions for this Form
                for (Iterator i = form.getFields().iterator(); i.hasNext(); ) {
                    Field field = (Field) i.next();

                    for (Iterator x = field.getDependencyList().iterator();
                         x.hasNext(); ) {
                        Object o = x.next();

                        if (o != null && !lActionMethods.contains(o)) {
                            lActionMethods.add(o);
                        }
                    }

                }

                // Create list of ValidatorActions based on lActionMethods
                for (Iterator i = lActionMethods.iterator(); i.hasNext(); ) {
                    String depends = (String) i.next();
                    ValidatorAction va = resources.getValidatorAction(depends);

                    // throw nicer NPE for easier debugging
                    if (va == null) {
                        throw new NullPointerException(
                            "Depends string \""
                            + depends
                            + "\" was not found in validator-rules.xml.");
                    }

                    String javascript = va.getJavascript();
                    if (javascript != null && javascript.length() > 0) {
                        lActions.add(va);
                    }
                    else {
                        i.remove();
                    }
                }

                Collections.sort(lActions, new Comparator() {
                    public int compare(Object o1, Object o2) {
                        ValidatorAction va1 = (ValidatorAction) o1;
                        ValidatorAction va2 = (ValidatorAction) o2;

                        if ( (va1.getDepends() == null ||
                              va1.getDepends().length() == 0)
                            &&
                            (va2.getDepends() == null ||
                             va2.getDepends().length() == 0)) {
                            return 0;
                        }
                        else if (
                            (va1.getDepends() != null &&
                             va1.getDepends().length() > 0)
                            &&
                            (va2.getDepends() == null ||
                             va2.getDepends().length() == 0)) {
                            return 1;
                        }
                        else if (
                            (va1.getDepends() == null ||
                             va1.getDepends().length() == 0)
                            &&
                            (va2.getDepends() != null &&
                             va2.getDepends().length() > 0)) {
                            return -1;
                        }
                        else {
                            return va1.getDependencyList().size() -
                                va2.getDependencyList().size();
                        }
                    }
                });

                String methods = null;
                for (Iterator i = lActions.iterator(); i.hasNext(); ) {
                    ValidatorAction va = (ValidatorAction) i.next();

                    if (methods == null) {
                        methods = va.getMethod() + "(form)";
                    }
                    else {
                        methods += " && " + va.getMethod() + "(form)";
                    }
                }

                results.append(getJavascriptBegin(methods));

                for (Iterator i = lActions.iterator(); i.hasNext(); ) {
                    ValidatorAction va = (ValidatorAction) i.next();
                    String jscriptVar = null;
                    String functionName = null;

                    if (va.getJsFunctionName() != null &&
                        va.getJsFunctionName().length() > 0) {
                        functionName = va.getJsFunctionName();
                    }
                    else {
                        functionName = va.getName();
                    }

                    results.append("    function " + functionName + " () { \n");
                    for (Iterator x = form.getFields().iterator(); x.hasNext(); ) {
                        Field field = (Field) x.next();

                        // Skip indexed fields for now until there is a good way to handle
                        // error messages (and the length of the list (could retrieve from scope?))
                        if (field.isIndexed()
                            || field.getPage() != page
                            || !field.isDependency(va.getName())) {

                            continue;
                        }

                        String message = getMessage(messages, locale, va, field);

                        message = (message != null) ? message : "";

                        jscriptVar = this.getNextVar(jscriptVar);

                        results.append(
                            "     this."
                            + jscriptVar
                            + " = new Array(\""
                            + field.getKey()
                            + "\", \""
                            + message
                            + "\", ");

                        results.append("new Function (\"varName\", \"");

                        Map vars = field.getVars();
                        // Loop through the field's variables.
                        Iterator varsIterator = vars.keySet().iterator();
                        while (varsIterator.hasNext()) {
                            String varName = (String) varsIterator.next();
                            Var var = (Var) vars.get(varName);
                            String varValue = var.getValue();
                            String jsType = var.getJsType();

                            // skip requiredif variables field, fieldIndexed, fieldTest, fieldValue
                            if (varName.startsWith("field")) {
                                continue;
                            }

                            if (Var.JSTYPE_INT.equalsIgnoreCase(jsType)) {
                                results.append(
                                    "this."
                                    + varName
                                    + "="
                                    + ValidatorUtils.replace(
                                        varValue,
                                        "\\",
                                        "\\\\")
                                    + "; ");
                            }
                            else if (Var.JSTYPE_REGEXP.equalsIgnoreCase(jsType)) {
                                results.append(
                                    "this."
                                    + varName
                                    + "=/"
                                    + ValidatorUtils.replace(
                                        varValue,
                                        "\\",
                                        "\\\\")
                                    + "/; ");
                            }
                            else if (Var.JSTYPE_STRING.equalsIgnoreCase(jsType)) {
                                results.append(
                                    "this."
                                    + varName
                                    + "='"
                                    + ValidatorUtils.replace(
                                        varValue,
                                        "\\",
                                        "\\\\")
                                    + "'; ");
                                // So everyone using the latest format doesn't need to change their xml files immediately.
                            }
                            else if ("mask".equalsIgnoreCase(varName)) {
                                results.append(
                                    "this."
                                    + varName
                                    + "=/"
                                    + ValidatorUtils.replace(
                                        varValue,
                                        "\\",
                                        "\\\\")
                                    + "/; ");
                            }
                            else {
                                results.append(
                                    "this."
                                    + varName
                                    + "='"
                                    + ValidatorUtils.replace(
                                        varValue,
                                        "\\",
                                        "\\\\")
                                    + "'; ");
                            }
                        }

                        results.append(" return this[varName];\"));\n");
                    }
                    results.append("    } \n\n");
                }
            }
            else if ("true".equalsIgnoreCase(staticJavascript)) {
                results.append(this.getStartElement());
                if ("true".equalsIgnoreCase(htmlComment)) {
                    results.append(htmlBeginComment);
                }
            }
        }

        if ("true".equalsIgnoreCase(staticJavascript)) {
            results.append(getJavascriptStaticMethods(resources));
        }

        if (form != null
            && ("true".equalsIgnoreCase(dynamicJavascript)
                || "true".equalsIgnoreCase(staticJavascript))) {

            results.append(getJavascriptEnd());
        }

        JspWriter writer = pageContext.getOut();
        try {
            writer.print(results.toString());
        }
        catch (IOException e) {
            throw new JspException(e.getMessage());
        }

        return (EVAL_BODY_TAG);

    }

    private String getNextVar(String input) {
        if (input == null) {
            return "aa";
        }

        input = input.toLowerCase();

        for (int i = input.length(); i > 0; i--) {
            int pos = i - 1;

            char c = input.charAt(pos);
            c++;

            if (c <= 'z') {
                if (i == 0) {
                    return c + input.substring(pos, input.length());
                }
                else if (i == input.length()) {
                    return input.substring(0, pos) + c;
                }
                else {
                    return input.substring(0, pos) + c +
                        input.substring(pos, input.length() - 1);
                }
            }
            else {
                input = replaceChar(input, pos, 'a');
            }

        }

        return null;

    }

    /**
     * Replaces a single character in a <code>String</code>
     */
    private String replaceChar(String input, int pos, char c) {
        if (pos == 0) {
            return c + input.substring(pos, input.length());
        }
        else if (pos == input.length()) {
            return input.substring(0, pos) + c;
        }
        else {
            return input.substring(0, pos) + c +
                input.substring(pos, input.length() - 1);
        }
    }

    public String getMessage(MessageResources messages, Locale locale,
                             ValidatorAction va, Field field) {

        String arg[] = Resources.getArgs(va.getName(), messages, locale, field);
        String msg = (field.getMsg(va.getName()) != null ?
                      field.getMsg(va.getName()) : va.getMsg());

        HttpServletRequest request = (HttpServletRequest) pageContext.
            getRequest();

        org.digijava.kernel.entity.Locale currentLocale = org.digijava.kernel.
            util.
            RequestUtils.getNavigationLanguage(request);
        Site site = org.digijava.kernel.util.RequestUtils.getSite(request);

        String newKey = "@" + currentLocale.getCode() + "." + site.getSiteId() +
            "." + msg;

        return messages.getMessage(locale, newKey, arg[0], arg[1], arg[2],
                                   arg[3]);
    }

}
