<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<digi:instance property="aimEditActivityForm" />
<%--@elvariable id="aimEditActivityForm" type="org.digijava.ampModule.aim.form.EditActivityForm"--%>

<ampModule:display name="/Activity Form/Issues Section" parentModule="/Activity Form">
    <ampModule:display name="/Activity Form/Issues Section/Issue" parentModule="/Activity Form/Issues Section">
        <fieldset>
            <legend>
		<span class=legend_label id="issueslink" style="cursor: pointer;">
			<digi:trn>Issues</digi:trn>
		</span>
            </legend>
            <div id="issuesdiv" class="toggleDiv">
                <c:if test="${!empty aimEditActivityForm.issues.issues}">
                    <table width="100%" cellSpacing="2" cellPadding="2" border="0">
                        <c:forEach var="issue" items="${aimEditActivityForm.issues.issues}">
                            <tr>
                                <td valign="top" colspan="3">
                                    <li class="level1"><b>
                                        <digi:trn key="aim:issuename:${issue.id}">
                                            <span class="word_break"><c:out value="${issue.name}" /></span>
                                        </digi:trn>
                                        <ampModule:display name="/Activity Form/Issues Section/Issue/Date" parentModule="/Activity Form/Issues Section/Issue">
                                            <c:out value="${issue.issueDate}" />
                                        </ampModule:display>
                                    </b>						</li>					</td>
                            </tr>
                            <ampModule:display name="/Activity Form/Issues Section/Issue/Measure" parentModule="/Activity Form/Issues Section/Issue">
                                <c:if test="${!empty issue.measures}">
                                    <c:forEach var="measure" items="${issue.measures}">
                                        <tr>
                                            <td></td>
                                            <td colspan="2">
                                                <li class="level2"><i> <digi:trn key="aim:${measure.nameTrimmed}">
                                                    <span class="word_break"><c:out value="${measure.name}" /></digi:trn></span>
                                                    <ampModule:display name="/Activity Form/Issues Section/Issue/Measure/Date" parentModule="/Activity Form/Issues Section/Issue/Measure">
                                                        <c:out value="${measure.measureDate}" />
                                                    </ampModule:display>
                                                </i></li></td>
                                        </tr>
                                        <ampModule:display name="/Activity Form/Issues Section/Issue/Measure/Actor" parentModule="/Activity Form/Issues Section/Issue/Measure">
                                            <c:if test="${!empty measure.actors}">
                                                <c:forEach var="actor" items="${measure.actors}">
                                                    <tr>
                                                        <td></td><td></td>
                                                        <td>
                                                            <li class="level3">
                                                                <digi:trn key="aim:${actor.nameTrimmed}">
                                                                    <span class="word_break"><c:out value="${actor.name}" /></span>
                                                                </digi:trn>
                                                            </li></td>
                                                    </tr>
                                                </c:forEach>
                                            </c:if>
                                        </ampModule:display>
                                    </c:forEach>
                                </c:if>
                            </ampModule:display>
                        </c:forEach>
                    </table>
                </c:if>
            </div>
        </fieldset>
    </ampModule:display>
</ampModule:display>
