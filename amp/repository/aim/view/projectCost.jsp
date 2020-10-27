<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>

<digi:instance property="aimEditActivityForm" />

<module:display name="/Activity Form/Funding/Overview Section/${costName}" parentModule="/Activity Form/Funding/Overview Section">
<fieldset>
	<legend>
		<span class=legend_label id="proposedcostlink" style="cursor: pointer;">
			<digi:trn>${costName}</digi:trn>
		</span>
	</legend>
	<div id="proposedcostdiv" class="toggleDiv">
		<c:if test="${not empty projCost}">
			<table cellspacing="1" cellPadding="3" bgcolor="#aaaaaa" width="100%" >
				<tr bgcolor="#f0f0f0">
					<td>
						<digi:trn key="aim:cost">Cost</digi:trn>
					</td>
					<td bgcolor="#f0f0f0" align="left">
						<c:if test="${not empty funAmount}">
							<b>${funAmount}</b>
						</c:if>&nbsp;
						<c:if test="${not empty currencyCode}"> 
							<b>${currencyCode}</b>
						</c:if>
					</td>
				</tr>
				<tr bgcolor="#f0f0f0">
					<c:if test="${not empty funDate}">
						<td>
							<digi:trn>Date</digi:trn>
						</td>
						<td bgcolor="#f0f0f0" align="left" width="150">
								<b>${funDate}</b>
						</td>
					</c:if>
					<c:if test="${empty funDate}">
						<td>*<digi:trn>cost could not be exchanged to workspace currency because date is not set</digi:trn></td>
					</c:if>	
				</tr>
             </table>
		</c:if>

	
	<c:if test = '${costName.equals("Proposed Project Cost")}' >
		<module:display name="/Activity Form/Funding/Overview Section/Proposed Project Cost/Annual Proposed Project Cost" 
		parentModule="/Activity Form/Funding/Overview Section/Proposed Project Cost">
		
		<c:if test="${not empty yearBudget}">
			<table cellspacing="1" cellPadding="3" bgcolor="#aaaaaa"
				width="100%">
				<tr>
					<td style="font-weight: bold"><digi:trn>Annual breakdown</digi:trn></td>
				</tr>
				<tr>&nbsp;</tr>
				<tr bgcolor="#f0f0f0">
					<th><digi:trn key="aim:cost">Cost</digi:trn></th>
					<th><digi:trn key="aim:cost">Year</digi:trn></th>
				</tr>
				<c:forEach var="annualBudget" items="${aimEditActivityForm.funding.proposedAnnualBudgets}">
					<tr bgcolor="#f0f0f0">
						<td>${annualBudget.funAmount} ${annualBudget.currencyCode}</td>
						<td>${annualBudget.funDate}</td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
		</module:display>
	</c:if>
		</div>
	</fieldset>
</module:display>