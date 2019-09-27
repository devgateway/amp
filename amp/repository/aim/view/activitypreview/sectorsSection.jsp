<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>

<digi:instance property="aimEditActivityForm" />
<%--@elvariable id="aimEditActivityForm" type="org.digijava.module.aim.form.EditActivityForm"--%>

<module:display name="/Activity Form/Sectors" parentModule="/Activity Form">
    <fieldset>
        <legend>
		<span class=legend_label id="sectorslink" style="cursor: pointer;">
			<digi:trn>Sectors</digi:trn>
		</span>
        </legend>
        <div id="sectorsdiv" class="toggleDiv">
            <c:forEach var="config" items="${aimEditActivityForm.sectors.classificationConfigs}" varStatus="ind">
                <bean:define id="emptySector" value="Sector"/>
                <module:display name="/Activity Form/Sectors/${config.name} Sectors" parentModule="/Activity Form/Sectors">
                    <c:set var="hasSectors">false</c:set>
                    <c:forEach var="actSect" items="${aimEditActivityForm.sectors.activitySectors}">
                        <c:if test="${actSect.configId==config.id}">
                            <c:set var="hasSectors">true</c:set>
                        </c:if>
                    </c:forEach>
                    <c:if test="${hasSectors}">
                        <digi:trn key="aim:addactivitysectors:${config.name} Sector">
                            <span class="word_break"><c:out value="${config.name} Sector" /></span>
                        </digi:trn>
                        <br/>
                    </c:if>
                    <c:if test="${!empty aimEditActivityForm.sectors.activitySectors}">
                        <c:forEach var="sectors" items="${aimEditActivityForm.sectors.activitySectors}">
                            <c:if test="${sectors.configId==config.id}">
                                <module:display name="/Activity Form/Sectors" parentModule="/Activity Form">
                                    <table width="100%" cellSpacing="2" cellPadding="1" style="font-size:10px;" >
                                        <tr>
                                            <td width=85%>
                                                <b><c:out value="${sectors.sectorScheme}" /></b>
                                                <c:if test="${!empty sectors.sectorName}">
                                                    -
                                                    <span class="word_break bold"><c:out value="${sectors.sectorName}"/></span>
                                                </c:if>
                                                <c:if test="${!empty sectors.subsectorLevel1Name}">
                                                    <!-- Sub sector field not found -->
                                                    -
                                                    <span class="word_break bold"><c:out value="${sectors.subsectorLevel1Name}"/></span>
                                                </c:if>
                                                <c:if test="${!empty sectors.subsectorLevel2Name}">
                                                    -
                                                    <span class="word_break bold"><c:out value="${sectors.subsectorLevel2Name}"/></span>
                                                </c:if>
                                            </td>
                                            <td width=15% align=right valign=top>
                                                <c:if test="${sectors.sectorPercentage!='' && sectors.sectorPercentage!='0'}">
									<span class="word_break bold">(<c:out value="${sectors.sectorPercentage}"/>)
                                        %</span>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </table>
                                    <hr>
                                </module:display>
                            </c:if>
                        </c:forEach>
                    </c:if>
                </module:display>
            </c:forEach>
            <c:if test="${not empty aimEditActivityForm.components.activityComponentes}">
                <digi:trn>Components</digi:trn>:&nbsp;
                <table>
                    <c:forEach var="compo" items="${aimEditActivityForm.components.activityComponentes}">
                        <tr>
                            <td width="100%"><span class="word_break">${compo.sectorName}</span></td>
                            <td align="right"><span class="word_break">${compo.sectorPercentage} %</span></td>
                        </tr>
                    </c:forEach>
                </table>
                <hr />
            </c:if>
        </div>
    </fieldset>
</module:display>
