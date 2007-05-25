<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:instance property="cmsForm"/>


<h1>CMS Teaser</h1>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td>
			Head
		</td>
	</tr>
		<logic:present name="cmsForm" property="categoryList">
			<logic:iterate indexId="index" 
						   id="category" 
						   name="cmsForm" 
						   property="categoryList" 
						   type="org.digijava.module.cms.dbentity.CMSCategory">
				<tr>
					<td>
						<digi:link href="<%= "/browseCategories.do?categoryId=" + String.valueOf (category.getId()) %>">
							<bean:write name="category" property="name"/>
						</digi:link>
					</td>
				</tr>
				<tr>
					<td>
						<logic:present name="category" property="subCategories">
							<logic:iterate length="3" 
										   name="category" 
										   property="subCategories" 
										   id="subCategory"
										   type="org.digijava.module.cms.dbentity.CMSCategory">
								<digi:link href="<%= "/browseCategories.do?categoryId=" + String.valueOf (subCategory.getId()) %>">
									<small><bean:write name="subCategory" property="name"/>,</small>
								</digi:link>
							</logic:iterate>
						</logic:present>
						...
					</td>
				</tr>				
			</logic:iterate>
		</logic:present>
	
</table>

<digi:link href="/index.do">Show all</digi:link>