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
<%-- <jsp:include page="teamPagesHeader.jsp" flush="true" /> --%>
<c:set var="translation">
				<digi:trn key="aim:categoryDeleteConfirm">Are you sure you want to delete the category?</digi:trn>
</c:set>
<style type="text/css">
		.jlien{
			text-decoration:none;
		}
		.jtextfont{
		font-family:verdana;font-size:11px;
		}
		
		.tableEven {
			background-color:#dbe5f1;
			font-size:8pt;
			padding:2px;
		}

		.tableOdd {
			background-color:#FFFFFF;
			font-size:8pt;!important
			padding:2px;
		}
		 
		.Hovered {
			background-color:#a5bcf2;
		}
		.jtabletitle{
		  font-size:10px; font-weight:bold;
		}
		
		
		
</style>

<script type="text/javascript">
function confirmDelete() {
	var ret		= confirm('${translation}');
	return ret;
}


function setStripsTable(tableId, classOdd, classEven) {
	var tableElement = document.getElementById(tableId);
	rows = tableElement.getElementsByTagName('tr');
	for(var i = 0, n = rows.length; i < n; ++i) {
		if(i%2 == 0)
			rows[i].className = classEven;
		else
			rows[i].className = classOdd;
	}
	rows = null;
}
function setHoveredTable(tableId, hasHeaders) {

	var tableElement = document.getElementById(tableId);
	if(tableElement){
    	var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        rows      = tableElement.getElementsByTagName('tr');

		for(var i = 0, n = rows.length; i < n; ++i) {
			rows[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			rows[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		rows = null;
	}
}



</script>


			<table cellPadding=5 cellSpacing=0 width="100%" border=0 >
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
                        <br><br>
						<c:set var="translation">
						<digi:trn key="aim:categoryManagerAddNewCategoryTitle">Click here to add a new category with specified values</digi:trn>
						</c:set>
						<digi:link href="/categoryManager.do?new=true" title="${translation}">
						   <img src="/TEMPLATE/ampTemplate/images/green_plus.png" style="height:16px; vertical-align: text-bottom; border:0px;"  />
						   <digi:trn key="aim:categoryManagerAddNewCategory"> Add New Category</digi:trn>
						</digi:link>
					</td>
					
					

					
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<digi:errors />
					</td>
				</tr>
			</table>

				<logic:notEmpty name="myForm" property="categories">
					<div style="width:830px;padding-left:7px;">
					<table border="0px" cellPadding="0px" cellSpacing="0px" style=" width:830px; _width:822px;" >
						<tr align="center" style="background-color:#999999; color:#000; height:30px; font-size:10px; font-weight:normal; ">
							<td  align="center" class="jtabletitle" width="170px;" style="" >
								<digi:trn key="aim:categoryName" >
									Category Name
								</digi:trn>
							</td>
							<td  align="left" class="jtabletitle" width="150px;" style=" " >
								<digi:trn key="aim:categoryDescription">
									Category Description
								</digi:trn>
							
							</td>
							<td   class="jtabletitle" width="100px;" style="padding-left:30px; padding-right:80px; ">
								<digi:trn key="aim:categoryPossibleValues">
									Possible Values
								</digi:trn>
							</td>
							<td   align="center" class="jtabletitle" style=" " width="150px;">
								<digi:trn key="aim:categoryOptions">
									Category Options
								</digi:trn>
							</td>
							<td   align="center" class="jtabletitle">
								<digi:trn key="aim:categoryActions">
									Actions
								</digi:trn>
							</td>
						</tr>
						</table>
						<!-- ============ -->
						<div style="overflow:auto; height:330px; border:#999999 1px solid; ">
						
						
						<table border="0px" cellPadding="0px" cellSpacing="0px" id="dataTable" style="" >
						
						<logic:iterate name="myForm" property="categories" id="category" type="org.digijava.module.categorymanager.dbentity.AmpCategoryClass">
						<tr align="left">
							<td >
								<div style="padding-right:10px;">
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
							</div>
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
							<td align="left" style="" width="15">
							<div style="height:15px; width:90px; margin-bottom:5px;">
								<% if (category.isMultiselect()) {%>
									<img src= "../ampTemplate/images/bullet_green.gif" border=0>
								<% }
									else { %>
									<img src= "../ampTemplate/images/bullet_red.gif" border=0>
								<%} %>
								
								<digi:trn key='aim:categoryIsMultiselect'>
									Multiselect
								</digi:trn>
							</div>
							
							<div style="height:15px; width:90px; ">
								<% if (category.isOrdered()) {%>
									<img src= "../ampTemplate/images/bullet_green.gif" border=0>
								<% }
									else { %>
									<img src= "../ampTemplate/images/bullet_red.gif" border=0>
								<%} %>
								
								<digi:trn key='aim:categoryIsOrdered'>
									Ordered
								</digi:trn>
							</div>
							</td>
							<td align="left">
								<div style="padding-left:50px;">
								<div style="width:110px; ">
									
										<digi:link paramId="edit" style="text-decoration:none;" paramName="category" paramProperty="id"  href='/categoryManager.do'>
											<img vspace="2" border="0" align="absmiddle" src="/repository/message/view/images/edit.gif"/>
											<digi:trn key="aim:categoryManagerEditAction">
												Edit     
											</digi:trn>
										</digi:link>
									
								
									
										<digi:link paramId="delete" style="text-decoration:none;" paramName="category" paramProperty="id"  href='/categoryManager.do' onclick="return confirmDelete()">
											<img vspace="2" border="0" align="absmiddle" src="/repository/message/view/images/trash_12.gif"/>
											<digi:trn key="aim:categoryManagerDeleteAction">
												Delete 
											</digi:trn>
										</digi:link>
									
								</div>
								</div>
							</td>
						</tr>
						</logic:iterate>
					</table>
					</div>
				</div>
				</logic:notEmpty>

<script language="javascript">
	setStripsTable("dataTable", "tableEven", "tableOdd");
	setHoveredTable("dataTable", false);
</script>
<br>
<digi:trn key="aim:tablelegend">
	Legend :
</digi:trn>
<img src= "../ampTemplate/images/bullet_green.gif" border=0>
<digi:trn key="aim:tableunable">
	Unable 
</digi:trn>
<img src= "../ampTemplate/images/bullet_red.gif" border=0>
<digi:trn key="aim:tabledisable">
	Disable 
</digi:trn>

