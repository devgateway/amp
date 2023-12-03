<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<digi:instance property="aimEditActivityForm" />
<%--@elvariable id="aimEditActivityForm" type="org.digijava.ampModule.aim.form.EditActivityForm"--%>

<ampModule:display name="/Activity Form/Budget Structure" parentModule="/Activity Form">
    <fieldset>
        <legend>
		<span class=legend_label id="proposedcostlink" style="cursor: pointer;">
			<digi:trn>Budget Structure</digi:trn>
		</span>	</legend>
        <div id="budgetstructurediv" class="toggleDiv">
            <c:if test="${aimEditActivityForm.budgetStructure!=null}">
                <table cellspacing="1" cellPadding="3" bgcolor="#aaaaaa" width="100%" >
                    <tr bgcolor="#f0f0f0">
                        <td>
                            <digi:trn key="aim:name">Name</digi:trn>
                        </td>
                        <td>
                            <digi:trn key="aim:percentage">Percentage</digi:trn>
                        </td>
                    </tr>
                    <c:forEach var="budgetStructure" items="${aimEditActivityForm.budgetStructure}" >
                        <tr bgcolor="#f0f0f0">
                            <td bgcolor="#f0f0f0" align="left" width="150">
                                <span class="word_break bold">${budgetStructure.budgetStructureName}</span>
                            </td>
                            <td bgcolor="#f0f0f0" align="left" width="150">
                                <c:if test="${budgetStructure.budgetStructurePercentage != null && budgetStructure.budgetStructurePercentage.length() > 0}">
                                    <span class="word_break bold">${budgetStructure.budgetStructurePercentage}%</span>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>
        </div>
    </fieldset>
</ampModule:display>
