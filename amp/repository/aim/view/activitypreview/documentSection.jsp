<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>

<digi:instance property="aimEditActivityForm" />
<%--@elvariable id="aimEditActivityForm" type="org.digijava.ampModule.aim.form.EditActivityForm"--%>

<ampModule:display name="/Activity Form/Related Documents" parentModule="/Activity Form">
    <fieldset>
        <legend>
		<span class=legend_label id="documentslink" style="cursor: pointer;">
			<digi:trn>Related Documents</digi:trn>
		</span>
        </legend>
        <div id="documnetsdiv" class="toggleDiv">
            <c:if test="${ (!empty aimEditActivityForm.documents.documents) || (!empty aimEditActivityForm.documents.crDocuments)}">
                <table width="100%" cellSpacing="1" cellPadding="5" cellSpacing="0" cellPadding="0">
                    <logic:notEmpty name="aimEditActivityForm" property="documents.documents" >
                        <logic:iterate name="aimEditActivityForm" property="documents.documents" id="docs" type="org.digijava.ampModule.aim.helper.Documents">
                            <c:if test="${docs.isFile == true}">
                                <tr>
                                    <td>
                                        <table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
                                            <tr>
                                                <td vAlign="center">&nbsp;
                                                    <span class="word_break bold"><c:out value="${docs.title}"/></span>&nbsp;&nbsp;-&nbsp;&nbsp;<i>
                                                        <c:out value="${docs.fileName}"/></i>
                                                    <logic:notEqual name="docs" property="docDescription" value=" ">
                                                        <br/>&nbsp;
                                                        <digi:trn>Description</digi:trn>:
                                                        &nbsp;<span class="word_break bold"><bean:write name="docs" property="docDescription" /></span>
                                                    </logic:notEqual>
                                                    <logic:notEmpty name="docs" property="date">
                                                        <br />&nbsp;
                                                        <digi:trn>Date</digi:trn>:
                                                        <b>&nbsp;<c:out value="${docs.date}" /></b>
                                                    </logic:notEmpty>
                                                    <logic:notEmpty name="docs" property="docType">
                                                        <br />&nbsp;
                                                        <digi:trn>Document Type</digi:trn>:&nbsp;
                                                        <span class="word_break bold"><bean:write name="docs" property="docType" /></span>
                                                    </logic:notEmpty>
                                                </td>
                                            </tr>
                                        </table>
                                        <hr />
                                    </td>
                                </tr>
                            </c:if>
                        </logic:iterate>
                    </logic:notEmpty>
                    <logic:notEmpty name="aimEditActivityForm" property="documents.crDocuments" >
                        <tr>
                            <td>
                                <logic:iterate name="aimEditActivityForm" property="documents.crDocuments" id="crDoc">
                                    <table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
                                        <tr>
                                            <td vAlign="center">
                                                &nbsp;<b><c:out value="${crDoc.title}"/></b>&nbsp;&nbsp;-&nbsp;&nbsp;
                                                <i><c:out value="${crDoc.name}"/></i>
                                                <c:set var="translation">
                                                    <digi:trn>Click here to download document</digi:trn>
                                                </c:set>
                                                <a id="<c:out value="${crDoc.uuid}"/>" target="_blank" href="${crDoc.generalLink}" title="${translation}">
                                                    <img src="/repository/contentrepository/view/images/check_out.gif" border="0">
                                                </a>
                                                <logic:notEmpty name="crDoc" property="description">
                                                    <br/>&nbsp;
                                                    <digi:trn>Description</digi:trn>:&nbsp;
                                                    <b><bean:write name="crDoc" property="description" /></b>
                                                </logic:notEmpty>
                                                <logic:notEmpty name="crDoc" property="calendar">
                                                    <br/>&nbsp;
                                                    <digi:trn>Date</digi:trn>:
                                                    <b>&nbsp;<c:out value="${crDoc.calendar}" /></b>
                                                </logic:notEmpty>
                                            </td>
                                        </tr>
                                    </table>
                                    <hr />
                                </logic:iterate>
                            </td>
                        </tr>
                    </logic:notEmpty>
                </table>
            </c:if>
                <%--<c:if test="${!empty aimEditActivityForm.documents.linksList}">
		<table width="100%" cellSpacing="0" cellPadding="0">
			<c:forEach var="docList" items="${aimEditActivityForm.documents.linksList}" >
				<bean:define id="links" name="docList" property="relLink" />
				<tr>
					<td>
					<table width="100%" class="box-border-nopadding">
						<tr>
							<td width="2">
								<digi:img src="ampModule/aim/images/web-page.gif"/>							</td>
							<td align="left" vAlign="center">&nbsp; <b>
								<c:out value="${links.title}"/></b> - &nbsp;&nbsp;&nbsp;
								<i> <a href="<c:out value="${links.url}"/>">
									<c:out value="${links.url}" />
								</a></i>
								<br>
								&nbsp; <b><digi:trn>Description</digi:trn>:</b>
								&nbsp;<c:out value="${links.description}" />							</td>
						</tr>
					</table>					</td>
				</tr>
			</c:forEach>
		</table>
	</c:if> --%>
        </div>
    </fieldset>
</ampModule:display>
