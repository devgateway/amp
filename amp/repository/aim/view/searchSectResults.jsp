<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:errors/>
<digi:instance property="aimSearchSectorForm" />
<digi:context name="digiContext" property="context" />

<c:set var="translation">
	<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
</c:set>
<digi:link href="/admin.do" title="${translation}" >
	Admin Home Page
</digi:link><br>
<table width="100%" border="0" cellspacing="4">
	<tr>
		<td bgcolor="#006699">
			<b><font color="#FFFFFF">
				<digi:trn key="aim:searchSectorResults">Search Results</digi:trn>	
			</font></b>
		</td>
	</tr>
	<logic:notEmpty name="aimSearchSectorForm" property="results">
		<tr>
			<td width="80%" align="center">
				<table width="100%" border="1">
					<tr>
						<td width="20%">
							<b>
							<digi:trn key="aim:sectorCode">
							Sector Code
							</digi:trn>
							</b>
						</td>
						<td width="80%">
							<b>
							<digi:trn key="aim:sectorName">
							Sector Name
							</digi:trn>
							</b>
						</td>
					</tr>
					<logic:iterate name="aimSearchSectorForm" property="results" id="results" 
					type="org.digijava.module.aim.dbentity.AmpSector">
						<tr>
							<td>
								<bean:write name="results" property="sectorCode" />
							</td>
							<td>
								<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
								<c:set target="${urlParams}" property="id">
									<bean:write name="results" property="ampSectorId" />
								</c:set>
								<c:set var="translation">
									<digi:trn key="aim:clickToViewSectorDetails">Click here to view Sector Details</digi:trn>
								</c:set>
								<digi:link href="/viewSectorDetails.do" name="urlParams" title="${translation}" >
									<bean:write name="results" property="name" />
								</digi:link>
							</td>
						</tr>
					</logic:iterate>
				</table>
			</td>
		</tr>
		<logic:notEmpty name="aimSearchSectorForm" property="pages">
			<tr>
				<td width="100%">
					<digi:trn>Pages:</digi:trn>
					<logic:iterate name="aimSearchSectorForm" property="pages" id="pages" type="java.lang.Integer">
						<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
						<c:set target="${urlParams1}" property="page">
							<%=pages%>
						</c:set>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewNextPage">Click here to goto Next Page</digi:trn>
						</c:set>
						<digi:link href="/sectorSearchResults.do" name="urlParams1" title="${translation}" >
							<%=pages%>
						</digi:link> |&nbsp;
					</logic:iterate>
				</td>
			</tr>
		</logic:notEmpty>
	</logic:notEmpty>
	<logic:empty name="aimSearchSectorForm" property="results">
		<tr><td>
			<b>No search Results</b>
		</td></tr>
	</logic:empty>
	<tr>
		<td align="center">
			<c:set var="translation">
				<digi:trn key="aim:clickToGoBackToSectorManager">Click here to go back to Sector Manager</digi:trn>
			</c:set>
			<digi:link href="/sectorManager.do" title="${translation}" >
				Back to Sector Manager
			</digi:link>
		</td>
	</tr>	
</table>



