<%@page import="org.digijava.module.translation.util.MultilingualInputFieldValues"%>

<%@ page pageEncoding="UTF-8" %>
<%-- tabbed input for entering a field in multiple languages
	jspParams:
		MANDATORY: 
			attr_name. request.getAttribute[attr_name] will hold a populated MultilingualInputFieldValues instance
		OPTIONAL:
			onkeyup, onkeypress 
 --%>
 
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<%
	MultilingualInputFieldValues dta = (MultilingualInputFieldValues) request.getAttribute(request.getParameter("attr_name"));
	pageContext.setAttribute("dta", dta);
%>

<%--
	required files to be included by the including page:
		<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/tabview/assets/skins/sam/tabview.css">
		<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
		<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script>
		<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script>
		
	see more documentation here: http://yui.github.io/yui2/docs/yui_2.9.0_full/tabview/
 --%>
<c:if test="${fn:length(dta.locales) eq 1}">
	<div class="yui-skin-sam smallish">
		<div id="${dta.prefix}_holder" class="yui-navset">

		    <%--  Only one locale in this loop --%>
            <c:forEach items="${dta.locales}" var="cur_locale">
                <div>
                    <input type="text" name="${dta.prefix}_${cur_locale}"
                        value="<c:out value="${(!dta.translations[cur_locale]).equals('') ? dta.translations[cur_locale] : dta.translations.values().toArray()[0]}"/>"

                        <c:if test="${not empty param.onkeyup}">
                            onkeyup="${param.onkeyup}"
                        </c:if>
                        <c:if test="${not empty param.onkeypress}">
                            onkeypress="${param.onkeypress}"
                        </c:if>
                        style="font-size: 8pt; font-weight: bolder;"
                        class="inp-text multilingual_input_element" />
                </div>
            </c:forEach>
        </div>
	</div>
</c:if>
<c:if test="${fn:length(dta.locales) gt 1}">
	<div class="yui-skin-sam smallish">	
		<div id="${dta.prefix}_holder" class="yui-navset">
			<ul class="yui-nav">  <!-- a header tab for each language -->
				<c:forEach items="${dta.locales}" var="cur_locale">
					<li><a href="#${dta.prefix}_${cur_locale}" onclick="javascript:focusOnTitle('${cur_locale}')"><em>${cur_locale}</em></a></li>
				</c:forEach>
				<%-- <li class="selected"><a href="#${param.group_name}_en"><em>en</em></a></li>  --%>
			</ul>
			<div class="yui-content">
				<c:forEach items="${dta.locales}" var="cur_locale">  <!-- an input <div> for each language -->
					<c:if test="${fn:length(dta.locales) gt 1}">
						<c:set var="placeholder" value="${cur_locale}" />
					</c:if>
					<div>
						<input type="text" name="${dta.prefix}_${cur_locale}" 
							value="<c:out value="${dta.translations[cur_locale]}"/>"
							placeholder="(${placeholder})"
							<c:if test="${not empty param.onkeyup}">
								onkeyup="${param.onkeyup}"							
							</c:if> 
							<c:if test="${not empty param.onkeypress}">
								onkeypress="${param.onkeypress}"
							</c:if>
							style="font-size: 8pt; font-weight: bolder;" 
							class="inp-text multilingual_input_element" />
					</div>
				</c:forEach>	
			</div>
		</div>
	</div>
</c:if>
<c:if test="${fn:length(dta.locales) gt 1}">
<script type="text/javascript">
	initMultilingualInput("${dta.prefix}"); // does NOT work in ajax-loaded scenarios! (like for example tabs)
</script>
</c:if>