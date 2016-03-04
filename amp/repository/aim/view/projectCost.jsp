<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/category" prefix="category"%>


<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>

<module:display name="/Activity Form/Funding/Overview Section/${param.costName}" parentModule="/Activity Form/Funding/Overview Section">
<fieldset>
	<legend>
		<span class=legend_label id="proposedcostlink" style="cursor: pointer;">
			<digi:trn>${param.costName}</digi:trn>
		</span>	</legend>
	<div id="proposedcostdiv" class="toggleDiv">
		<c:if test='${param.projCost!=""}'>
			<table cellspacing="1" cellPadding="3" bgcolor="#aaaaaa" width="100%" >
				<tr bgcolor="#f0f0f0">
					<td>
						<digi:trn key="aim:cost">Cost</digi:trn>					
					</td>
					<td bgcolor="#f0f0f0" align="left">
						<c:if test='${param.funAmount!=""}'>
							<b>${param.funAmount}</b>						
						</c:if>&nbsp;
						<c:if test='${param.currencyCode!=""}'> 
							<b>${param.currencyCode}</b>						
						</c:if>					
					</td>
				</tr>
				<tr bgcolor="#f0f0f0">
					<c:if test='${param.funDate!=""}'>
						<td>
							<digi:trn>Date</digi:trn>					
						</td>
						<td bgcolor="#f0f0f0" align="left" width="150">
								<b>${param.funDate}</b>						
						</td>
					</c:if>
					<c:if test='${param.funDate==""}'>
						<td>*<digi:trn>cost could not be exchanged to workspace currency because date is not set</digi:trn></td>
					</c:if>	
				</tr>
             </table>
		</c:if>
	</div>
	
	<c:if test = '${param.costName.equals("Proposed Project Cost")}' >
		<module:display name="/Activity Form/Funding/Overview Section/Proposed Project Cost/Annual Proposed Project Cost" 
		parentModule="/Activity Form/Funding/Overview Section/Proposed Project Cost">
		
		
		<c:if
			test="${aimEditActivityForm.funding.proposedAnnualBudgets!=null 
			&& aimEditActivityForm.funding.proposedAnnualBudgets.size()>0}">
			<table cellspacing="1" cellPadding="3" bgcolor="#aaaaaa"
				width="100%">
				<tr bgcolor="#f0f0f0">
					<td><b><digi:trn key="aim:cost">Cost</digi:trn></b></td>
					<td><b><digi:trn key="aim:cost">Year</digi:trn></b></td>
				</tr>
				<c:forEach var="annualBudget"
					items="${aimEditActivityForm.funding.proposedAnnualBudgets}">
					<tr bgcolor="#f0f0f0">
						<td>${annualBudget.funAmount}
							${annualBudget.currencyCode}</td>
						<td>${annualBudget.funDate}</td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
		</module:display>
	</c:if>
	</fieldset>
</module:display>