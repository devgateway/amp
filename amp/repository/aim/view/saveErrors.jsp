<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %> 
<%@ taglib uri="/taglib/category" prefix="category" %>
            

<META http-equiv="refresh" content="10; URL=/aim/viewMyDesktop.do">
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>


<digi:instance property="aimEditActivityForm" />

<input type="hidden" name="edit" value="true">

<digi:errors/>
<p align="center">
<br/><br/>
<table width="70%" height="70%" border="0" cellspacing="2" cellpadding="2" align="center" class="box-border-nopadding">
	<tr>
		<td colspan="2" bgcolor="#FA0505" class="textalb" align="center">
			<b><digi:trn key="aim:saveErrors:recoverySave">Recovery Save</digi:trn></b>
		</td>
	</tr>
	<tr>
		<td>
			<p align="center"><img align="top" width="16px" height="16px" src="/repository/aim/view/scripts/ajaxtabs/save-loader.gif" /> You are being redirected to the <html:link href="/aim/viewMyDesktop.do"> <digi:trn key="aim:saveErrors:Desktop">Desktop</digi:trn></html:link> in 10 seconds ...</p>
			<br/><br/>
			<p align="center">
			<table align="center" width="50%">
			<tr>
				<td>
					<img align="top" width="48px" height="48px" src="/repository/aim/view/scripts/ajaxtabs/info.gif" />			
				</td>
				<td>
					<font size="3px">Activity Save failed and recovery save managed to save only the steps marked with
					<img align="top" width="16px" height="16px" src="/repository/aim/view/scripts/ajaxtabs/ok.gif" />. Also your activity was 
					saved as draft</font>
				</td>
			</tr>
			</table>
			</p>
			<br/>
			
			<a:href />
			<p align="center">
			<table align="center" cellspacing="2" cellpadding="2">
			<logic:iterate id="stepState" name="aimEditActivityForm" property="stepFailure" type="java.lang.Boolean" indexId="idx" >
				<tr>
					<c:if test="${stepState==false}" >
						<td>				 		
							<b><font size="2px">
							Step <c:out value="${aimEditActivityForm.stepText[idx]}" />
							</font></b>
						</td>
						<td>
					 		<img align="top" width="24px" height="24px" src="/repository/aim/view/scripts/ajaxtabs/ok.gif" />
					 	</td>
					</c:if>
					<c:if test="${stepState!=false}">
						<td>
							<b><font size="2px">
							Step <c:out value="${aimEditActivityForm.stepText[idx]}" />
							</font></b>
						</td>
						<td>
							<img align="top" width="24px" height="24px" src="/repository/aim/view/scripts/ajaxtabs/failed.gif" />
						</td>
					</c:if>
				</tr>
			</logic:iterate>			
			</table>
			</p>
			
		</td>
	</tr>
</table>
</p>
