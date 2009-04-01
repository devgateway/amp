<%@page contentType="text/html"%>
<%@ page language="java" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<digi:instance property="SaveHtmlForm" />

<link rel="stylesheet" href="stylesheet.css" type="text/css" />
<digi:form action="/SaveHtml.do">
<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="400" class=box-border-nopadding>
	<tr>
		<td align=left vAlign=top>
			<table bgcolor=#aaaaaa cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
				<tr bgcolor="#aaaaaa">
					<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
						<digi:trn>Save New Report</digi:trn>
					</td>
				</tr>
				<tr>
					<td align="letf">
						<table border="0" cellpadding="2" cellspacing="1" width="100%">
							<tr bgcolor="#f4f4f2">
								<td align="right" valign="middle" width="50%">
									<FONT color=red>*</FONT>
									<digi:trn>Report Name</digi:trn>&nbsp;
								</td>
								<td align="left" valign="middle" width="50%">
									<html:text property="reportname" size="15" styleClass="inp-text"/>
								</td>
							</tr>
							<tr bgcolor="#f4f4f2">
								<td align="right" valign="middle" width="50%">
									<c:set var="trnSaveBtn">
										<digi:trn key="aim:btnSave">Save</digi:trn>
                                    </c:set>
                                    <input type="button" value="${trnSaveBtn}" onclick="save()" style="dr-menu">
								</td>
								</td> 
								<td align="left" valign="middle" width="50%">
									 <c:set var="trnCloseBtn">
                                       	<digi:trn key="aim:btnClose">Close</digi:trn>
                                     </c:set>
									<input type="button" value="${trnCloseBtn}" onclick="closePopup()" style="dr-menu">
								</td>
							</tr>
							 <html:hidden property="action"/>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</digi:form>
