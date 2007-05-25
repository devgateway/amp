<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:instance property="cmsForm"/>
<digi:ref href="css/demoUI.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<digi:file src="module/cms/css/cms.css"/>">

	<table border="0" cellpadding="0" cellspacing="0" width="100%">
		<logic:present name="cmsForm" property="categoryList">

<bean:define id="catList" name="cmsForm" property="categoryList" type="java.util.List"/>
<bean:define id="listSize" value="<%= String.valueOf(catList.size()-1) %>"/>


<logic:iterate indexId="index" id="category" name="catList" type="org.digijava.module.cms.dbentity.CMSCategory">

<bean:define id="oddOrEven" value="<%= String.valueOf(index.intValue()%2) %>"/>
<bean:define id="strIndex" value="<%= String.valueOf(index.intValue()) %>"/>		
				<logic:equal name="oddOrEven" value="0">
				<tr>
				</logic:equal>
				<td valign="top" width="50%">
					<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
						<tr>
							<td width="1" nowrap>&#9675;&nbsp;</td>
							<td>
					      	  <digi:link styleClass="cmsCategory" href="/browseCategories.do" paramName="category" paramId="categoryId" paramProperty="id"><bean:write name="category" property="name" /></digi:link>
							</td>
						</tr>  
						<tr>
							<td width="1">&nbsp;</td>
							<td>
							  <logic:present name="category" property="subCategories">
								<logic:iterate length="3" id="subCategory" name="category" property="subCategories" type="org.digijava.module.cms.dbentity.CMSCategory">
								     <digi:link styleClass="cmsSubcategory" href="/browseCategories.do" paramName="subCategory"  paramId="categoryId" paramProperty="id"><bean:write name="subCategory" property="name" /></digi:link>,
							    </logic:iterate>
							  </logic:present>	
						  ...
							</td>
						</tr>
					</table>	
				</td>
				<logic:equal name="oddOrEven" value="1">
				</tr>
				</logic:equal>
				<logic:equal name="oddOrEven" value="0">
					<logic:equal name="strIndex" value="<%= listSize %>">
							<td width="50%">&nbsp;</td>
						</tr>
					</logic:equal>
				</logic:equal>
			</logic:iterate>
		</logic:present>
	</table>
<digi:link href="/index.do">Show all</digi:link>

	
