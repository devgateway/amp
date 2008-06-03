<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>

<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<bean:define id="ampTeam" name="teamWrk"
	type="org.digijava.module.aim.dbentity.AmpTeam" scope="request"
	toScope="page" />

<div style='position:relative;display:none;' id='team-<bean:write name="ampTeam" property="ampTeamId"/>'> 
	<ul>
		<li>
			
			<logic:present name="ampTeam" property="accessType">
				<bean:define id="workspaceType" name="ampTeam" property="type" type="org.digijava.module.aim.dbentity.AmpCategoryValue" />
				<bean:write name="workspaceType" property="value"/>
			</logic:present>
			<logic:notPresent name="ampTeam" property="accessType">
				<digi:trn key="aim:noWorkspaceType">No workspace type defined</digi:trn>
			</logic:notPresent>
		</li>
		
		<li>
		
			<logic:notEmpty name="ampTeam" property="organizations">
				<digi:trn key="aim:childrenOrganizations">Children (Organizations)</digi:trn>
				<ul>
				<logic:iterate id="org" name="ampTeam" property="organizations" type="org.digijava.module.aim.dbentity.AmpOrganisation">
					<li>
					<bean:write name="org" property="name" />
					</li>
				</logic:iterate>
				</ul>
			</logic:notEmpty>
			
			<logic:notEmpty name="ampTeam" property="childrenWorkspaces">
				<digi:trn key="aim:childrenWorkspaces">Children (Workspaces)</digi:trn>
				<ul>
				<logic:iterate id="team" name="ampTeam" property="childrenWorkspaces" type="org.digijava.module.aim.dbentity.AmpTeam">
					<li>
					<bean:write name="team" property="name" />
					</li>
				</logic:iterate>
				</ul>
			</logic:notEmpty>
			
		</li>
		
		<li><digi:trn key="aim:computation">Computation</digi:trn> : 
			<logic:equal name="ampTeam" property="computation" value="true">
				<digi:trn key="aim:yes">Yes</digi:trn>
			</logic:equal>
			<logic:equal name="ampTeam" property="computation" value="false">
				<digi:trn key="aim:no">No</digi:trn>
			</logic:equal>/
		</li>
	</ul>
</div>
<div align="left" width="2" style="display: inline"
	onMouseOver="stm(['<bean:write name="ampTeam" property="name"/>',document.getElementById('team-<bean:write name="ampTeam" property="ampTeamId"/>').innerHTML],Style[0])" 
	onMouseOut="htm()">
	<jsp:useBean id="urlParams22" type="java.util.Map" class="java.util.HashMap"/>
	<c:set target="${urlParams22}" property="tId">
		<bean:write name="ampTeam" property="ampTeamId" />
	</c:set>
	<c:set target="${urlParams22}" property="event" value="edit" />
	<c:set target="${urlParams22}" property="dest" value="admin" />
	<c:set var="translation">
		<digi:trn key="aim:clickToViewWorkspace">Click here to view Workspace</digi:trn>
	</c:set>
	<digi:link href="/getWorkspace.do" name="urlParams22" title="${translation}" >
		<bean:write name="ampTeam" property="name"/>
	</digi:link>
</div>
