<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<!-- 
 -->
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg valign="top" width=750>
	
			<table cellPadding=5 cellspacing="0" width="100%" border="0">
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:AmpAdminHome">
							Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:luceneIndexing">
							Lucene Indexing
						</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 valign="center" width=571>
						<span class=subtitle-blue>
						<digi:trn key="aim:artyDebug">
							Arty Debug
						</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td height=16 valign="center" width=571>
						<digi:link href="/luceneIndex.do?action=checked">
							Generate Checked Ex
						</digi:link>
						|
						<digi:link href="/luceneIndex.do?action=unchecked">
							Generate Un-Checked Ex
						</digi:link>
						|
						<digi:link href="/luceneIndex.do?action=confluence">
							Confluence
						</digi:link>
						
					</td>
				</tr>
				<tr>
					<td height=16 valign="center" width=571>
						<digi:link href="/luceneIndex.do?action=create">
							Create
						</digi:link>
					</td>
				</tr>
				<tr>
					<td height=16 valign="center" width=571>
						<form action="/luceneIndex.do" method="post">
							Field:<input type="text" name="field" size="30" />
							String:<input type="text" name="search" size="30" />
							<input type="text" name="action" value="view"/>
							<input type="submit" value="Submit">
							
						</form>
					</td>
				</tr>
				
				
				<tr>
					<td noWrap width=80% vAlign="top">
					</td>
				
				<td valign="top">
				<!-- start  -->
				
				
				<!--  end -->
				</td>
					
				</tr>
				
</table>
