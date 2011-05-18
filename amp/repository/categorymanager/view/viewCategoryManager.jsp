<%@ page pageEncoding="UTF-8" %> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ page import="java.util.List"%>
 
<%@page import="org.digijava.module.categorymanager.util.CategoryManagerUtil"%>
<digi:instance property="cmCategoryManagerForm" />
<bean:define id="myForm" name="cmCategoryManagerForm" toScope="page" type="org.digijava.module.categorymanager.form.CategoryManagerForm" />

<!--  AMP Admin Logo -->
<%-- <jsp:include page="teamPagesHeader.jsp"  /> --%>
<c:set var="translation">
				<digi:trn key="aim:categoryDeleteConfirm">Are you sure you want to delete the category?</digi:trn>
</c:set>
<script type="text/javascript">
function confirmDelete() {
	var ret		= confirm('${translation}');
	return ret;
}
</script>

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="90%" class="box-border-nopadding">
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%" border=0>
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" contextPath="/aim">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						
						
						<digi:trn key="aim:categoryManager">
							Category Manager
						</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<span class=subtitle-blue>
							<digi:trn key="aim:categoryManager">
								Category Manager
							</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<digi:errors />
					</td>
				</tr>
				<tr>
				<td>
				
				<logic:notEmpty name="myForm" property="categories">
					<table border="1px" >
						<tr align="center">
							<td bgcolor="#006699" class="textalb" align="center">
								<digi:trn key="aim:categoryName">
									Category Name
								</digi:trn>
							</td>
							<td bgcolor="#006699" class="textalb" align="center">
								<digi:trn key="aim:categoryDescription">
									Category Description
								</digi:trn>
							
							</td>
							<td bgcolor="#006699" class="textalb" align="center">
								<digi:trn key="aim:categoryPossibleValues">
									Possible Values
								</digi:trn>
							</td>
							<td bgcolor="#006699" class="textalb" align="center">
								<digi:trn key="aim:categoryOptions">
									Category Options
								</digi:trn>
							</td>
							<td bgcolor="#006699" class="textalb" align="center">
								<digi:trn key="aim:categoryActions">
									Actions
								</digi:trn>
							</td>
						</tr>
						<logic:iterate name="myForm" property="categories" id="category" type="org.digijava.module.categorymanager.dbentity.AmpCategoryClass">
						<tr align="center">
							<td>
								<digi:trn key='<%= CategoryManagerUtil.getTranslationKeyForCategoryName( category.getKeyName() ) %>'>
									<bean:write name="category" property="name" />
								</digi:trn>
								<br />
								(
								<digi:trn key="aim:categoryKeyIs">
									category key is 
								</digi:trn>
								<i><bean:write name="category" property="keyName" /></i>
								)
							</td>
							<td align="left">
								<digi:trn key='<%= CategoryManagerUtil.getTranslationKeyForCategoryName( category.getDescription() ) %>'>
									<bean:write name="category" property="description" /> &nbsp;
								</digi:trn>
								&nbsp;
							</td>
							<td align="left">
								<ul>
								<logic:iterate name="category" property="possibleValues" id="categoryValue" type="org.digijava.module.categorymanager.dbentity.AmpCategoryValue">
								<logic:notEmpty name="categoryValue">
								<% String keyForValue	= CategoryManagerUtil.getTranslationKeyForCategoryValue(categoryValue); %>
									<li>
										<digi:trn key='<%=keyForValue%>'>
											<bean:write name="categoryValue" property="value" />
										</digi:trn>
									</li>
								</logic:notEmpty>
								</logic:iterate>
								</ul>
							</td>
							<td align="left">
								<% if (category.isMultiselect()) {%>
									<img src= "../ampTemplate/images/bullet_green.gif" border=0>
								<% }
									else { %>
									<img src= "../ampTemplate/images/bullet_red.gif" border=0>
								<%} %>
								&nbsp;
								<digi:trn key='aim:categoryIsMultiselect'>
									Multiselect
								</digi:trn>
								<br />
								<% if (category.isOrdered()) {%>
									<img src= "../ampTemplate/images/bullet_green.gif" border=0>
								<% }
									else { %>
									<img src= "../ampTemplate/images/bullet_red.gif" border=0>
								<%} %>
								&nbsp;
								<digi:trn key='aim:categoryIsOrdered'>
									Ordered
								</digi:trn>
							</td>
							<td align="left">
								<ul>
									<li>
										<digi:link paramId="edit" paramName="category" paramProperty="id"  href='/categoryManager.do'>
											<digi:trn key="aim:categoryManagerEditAction">
												Edit Category
											</digi:trn>
										</digi:link>
									</li>
									<li>
										<digi:link paramId="delete" paramName="category" paramProperty="id"  href='/categoryManager.do' onclick="return confirmDelete()">
											<digi:trn key="aim:categoryManagerDeleteAction">
												Delete Category
											</digi:trn>
										</digi:link>
									</li>
								</ul>
							</td>
						</tr>
						</logic:iterate>
					</table>
				
				</logic:notEmpty>
<c:set var="translation">
	<digi:trn key="aim:categoryManagerAddNewCategoryTitle">Click here to add a new category with specified values</digi:trn>
</c:set>
<digi:link href="/categoryManager.do?new=true" title="${translation}">
	<digi:trn key="aim:categoryManagerAddNewCategory">Add New Category</digi:trn>
</digi:link>


				</td>
				</tr>
		</table>
	</td>
	</tr>
</table>