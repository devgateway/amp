<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/src/main/webapp/WEB-INF/moduleVisibility.tld" prefix="module"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/c.tld" prefix="c"%>

<digi:instance property="aimEditActivityForm" />
<%--@elvariable id="aimEditActivityForm" type="org.digijava.module.aim.form.EditActivityForm"--%>

<module:display name="/Activity Form/Activity Internal IDs" parentModule="/Activity Form">
    <fieldset>
        <legend>
			<span class=legend_label id="internallink" style="cursor: pointer;">
				<digi:trn>Agency Internal IDs</digi:trn>
			</span>
        </legend>
        <div id="internaldiv" class="toggleDiv">
            <c:if test="${!empty aimEditActivityForm.internalIds}">
                <c:forEach var="internalObj" items="${aimEditActivityForm.internalIds}">
                    <table width="100%" cellSpacing="2" cellPadding="1" style="font-size:11px;">
                        <tr>
                            <td width="85%">
                                <b>[${internalObj.organisation.name}]</b>
                            </td>
                            <td width="15%" align="right" valign=top>
                                <module:display name="/Activity Form/Activity Internal IDs/Internal IDs/internalId" parentModule="/Activity Form">
                                    <b><c:out value="${internalObj.internalId}"/></b>
                                </module:display>
                            </td>
                        </tr>
                    </table>
                    <hr/>
                </c:forEach>
            </c:if>
        </div>
    </fieldset>
</module:display>
