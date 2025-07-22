<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category"%>

<digi:instance property="aimEditActivityForm" />
<%--@elvariable id="aimEditActivityForm" type="org.digijava.module.aim.form.EditActivityForm"--%>

<module:display name="/Activity Form/Location" parentModule="/Activity Form">
    <fieldset>
        <legend>
		<span class=legend_label id="locationlink" style="cursor: pointer;">
			<digi:trn>Location</digi:trn></span>
        </legend>
        <div id="locationdiv" class="toggleDiv">
            <module:display name="/Activity Form/Location/Implementation Location" parentModule="/Activity Form/Location">
                <c:if test="${!empty aimEditActivityForm.location.selectedLocs}">
                    <c:forEach var="selectedLocs" items="${aimEditActivityForm.location.selectedLocs}">
                        <table width="100%" cellSpacing="2" cellPadding="1" style="font-size:11px;">
                            <tr>
                                <td width="85%">
                                    <c:forEach var="ancestorLoc" items="${selectedLocs.ancestorLocationNames}">
                                        <b>[${ancestorLoc}]</b>
                                    </c:forEach>
                                </td>
                                <td width="15%" align="right" valign=top>
                                    <field:display name="Regional Percentage" feature="Location">
                                        <c:if test="${selectedLocs.showPercent}">
                                            <b><c:out value="${selectedLocs.percent}"/> %</b>
                                        </c:if>
                                    </field:display>
                                </td>
                            </tr>
                        </table>
                        <hr/>
                    </c:forEach>
                    <module:display name="/Activity Form/Map Options/Show Map In Activity Preview" parentModule="/Activity Form/Map Options">
                        <table width="100%" cellSpacing="2" cellPadding="1" style="font-size:11px;">
                            <tr> <td colspan="2">
                                <script type="text/javascript">
                                    <c:forEach var="selectedLocs" items="${aimEditActivityForm.location.selectedLocs}">
                                    coordinates.push('<c:out value="${selectedLocs.lat}"/>;<c:out value="${selectedLocs.lon}"/>;<c:out value="${selectedLocs.locationName}"/>');
                                    </c:forEach>
                                </script>
                                <jsp:include page="../previewmap.jsp"/>
                            </td> </tr>
                        </table>
                        <hr/>
                    </module:display>
                </c:if>
            </module:display>
            <module:display name="/Activity Form/Location/Implementation Level" parentModule="/Activity Form/Location">
                <table>
                    <tr>
                        <td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
                            <digi:trn key="aim:level">Implementation Level</digi:trn>:
                        </td>
                        <td bgcolor="#ffffff">
                            <c:if test="${aimEditActivityForm.location.levelId>0}">
                                <b><category:getoptionvalue categoryValueId="${aimEditActivityForm.location.levelId}"/></b>
                            </c:if>
                        </td>
                    </tr>
                </table>
                <hr/>
            </module:display>
            <module:display name="/Activity Form/Location/Implementation Location" parentModule="/Activity Form/Location">
                <table style="font-size:11px;">
                    <tr>
                        <td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
                            <digi:trn key="aim:implementationLocation">Implementation Location</digi:trn>:
                        </td>
                        <td bgcolor="#ffffff">
                            <c:if test="${aimEditActivityForm.location.implemLocationLevel>0}">
							<span class="word_break bold">
								<category:getoptionvalue categoryValueId="${aimEditActivityForm.location.implemLocationLevel}"/>
							</span>
                            </c:if>
                        </td>
                    </tr>
                </table>
                <hr/>
            </module:display>
        </div>
    </fieldset>
</module:display>
