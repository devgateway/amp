<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<digi:instance property="aimEditActivityForm" />
<%--@elvariable id="aimEditActivityForm" type="org.digijava.ampModule.aim.form.EditActivityForm"--%>

<ampModule:display name="/Activity Form/Activity Internal IDs" parentModule="/Activity Form">
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
                                <ampModule:display name="/Activity Form/Activity Internal IDs/Internal IDs/internalId" parentModule="/Activity Form">
                                    <b><c:out value="${internalObj.internalId}"/></b>
                                </ampModule:display>
                            </td>
                        </tr>
                    </table>
                    <hr/>
                </c:forEach>
            </c:if>
        </div>
    </fieldset>
</ampModule:display>
