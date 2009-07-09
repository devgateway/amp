<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<jsp:include page="viewComponentTypesPopin.jsp" flush="true" />

<script langauage="JavaScript">
function onDelete() {
  var flag = confirm('<digi:trn key="aim:deletethisType">Delete this Type</digi:trn>');
  return flag;
}

function deleteType(id){
  if (onDelete()){
	<digi:context name="delComponentType" property="context/module/moduleinstance/updateComponentType.do?event=delete" />
    document.aimComponentsTypeForm.action = "<%= delComponentType %>&id="+id;
    document.aimComponentsTypeForm.target = "_self";
    document.aimComponentsTypeForm.submit();
  }
}
</script>

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



<digi:instance property="aimComponentsTypeForm" />
<digi:form action="/updateComponentType.do" method="post">
 
 <digi:context name="digiContext" property="context" />

  <!--  AMP Admin Logo -->
  <jsp:include page="teamPagesHeader.jsp" flush="true" />
  <!-- End of Logo -->


  <table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
    <tr>
      <td  width=14>&nbsp;</td>
      <td align=left  vAlign=top width=750>
        <table cellPadding=5 cellSpacing=0 width="100%" border=0>
          <tr>
            <!-- Start Navigation -->
            <td ><span class="crumb">
              <c:set var="ToViewAdmin">
                <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
              </c:set>
              <digi:link href="/admin.do" styleClass="comment" title="${ToViewAdmin}" >
                <digi:trn key="aim:AmpAdminHome">
                Admin Home
                </digi:trn>
              </digi:link>&nbsp;&gt;&nbsp;
              <digi:trn key="aim:componentTypeManager">
              Component Type Manager
              </digi:trn>
            </td>
            <!-- End navigation -->
          </tr>
          <tr>
           <c:set var="title">
           <digi:trn key="aim:componentTypesTitle">Component Types Manager</digi:trn>
          </c:set>

            <td vAlign=center width=571><span class=subtitle-blue><c:out value="${componentTypesTitle}"/></span>
      </td>
          </tr>
          <tr>
            <tdvAlign=center width=571>
              <digi:errors />
            </td>
          </tr>
          <tr>
            <td noWrap width=100% vAlign="top">
            
          <tr>
            <td noWrap width=600 vAlign="top"><table bgcolor="#d7eafd" cellpadding="3" cellspacing="0" width="100%" valign="top">
              <tr bgcolor="#ffffff">
                <td valign="top" width="100%"><table width="100%" cellspacing="1" cellpadding="1" valign="top" align="left">
                    <tr>
                      <td ><!-- Table title bgColor=#999999 class="box-title" style = "color:#000; height:20px;"  -->
                         <!--  <digi:trn key="aim:componentTypes">
                            <center>Component Types </center>
                          </digi:trn>
                          end table title -->                      </td>
                    </tr>
                    <tr>
                      <td>
					<div style = "color:#000; height:20px; background-color:#999999;" class="box-title">
					<digi:trn key="aim:componentTypes">
                            <center>Component Types </center>
                          </digi:trn>

					</div>
                     <div style="overflow:auto; height:180px; border:#999999 1px solid; ">
                     <table width="100%" cellspacing="0" cellpadding="0" valign="top" align="left" id="dataTable">
                          <logic:empty name="aimComponentsTypeForm" property="componentTypesList">
                            <tr >
                              <td colspan="5" align="center"><b>
                                <digi:trn key="aim:noTypes"> No types present </digi:trn>
                              </b></td>
                            </tr>
                          </logic:empty>
                          <logic:notEmpty name="aimComponentsTypeForm" property="componentTypesList">
                            <logic:iterate name="aimComponentsTypeForm" property="componentTypesList" id="componentTypesList"
                            type="org.digijava.module.aim.dbentity.AmpComponentType">
                              <tr>
                                <td height="20" >
                                
                            <jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
	                                    <c:set var="ToEditComponents">
	                                      <digi:trn key="aim:clickToEdit">Click here to Edit</digi:trn>
	                                    </c:set>
	                                    <bean:write  name="componentTypesList" property="code"/>                                     </td>
                                <td height="20" >
                                <bean:write  name="componentTypesList" property="name"/>                                </td>
                                <td width="100" height="20" align="center" nowrap="nowrap" >
                                  <c:set var="ToEditType">
                                  <digi:trn key="aim:clickToEditType">Click here to Edit Type</digi:trn>
                                </c:set>
                                <c:set var="edit">
                                  <digi:trn key="aim:edit">Edit</digi:trn>
                                </c:set>
                                <a href="javascript:editType(<bean:write name="componentTypesList" property="type_id"/>)" style="text-decoration:none;" >
									<img border="0" align="absmiddle" vspace="2" src="/repository/message/view/images/edit.gif"/>
									<c:out value="${edit}"/>
							    </a>
								<c:set var="delete">
                                    <digi:trn key="aim:deleteType">Delete</digi:trn>
                                  </c:set>
								  <c:set var="ForDelete">
                                    <digi:trn key="aim:cantDeleteType">Can't delete type because it's assigned to an Compoenent</digi:trn>
                                  </c:set>
                                  <c:if test="${!empty componentTypesList.components}">
                                   <a title="${ForDelete}"  style="text-decoration:none;color:#999999">
									<img border="0" align="absmiddle" vspace="2" src="/repository/message/view/images/trash_12.gif"/>
										${delete}
									</a>
                                  </c:if>
                                 <c:set var="ForDelete">
                                   <digi:trn key="aim:clickToDeleteType">Click here to delete Type</digi:trn>
                                 </c:set>
                                  <c:if test="${empty componentTypesList.components}">
                                    <a title="${ForDeleteComponent}"  href="javascript:deleteType(${componentTypesList.type_id});" style="text-decoration:none;">
                                    <img border="0" align="absmiddle" vspace="2" src="/repository/message/view/images/trash_12.gif"/>
										<c:out value="${delete}"/>
									</a></c:if>                                
                                 </td>
                              </tr>
                            </logic:iterate>
                          </logic:notEmpty>
                          <!-- end page logic -->
                      </table>
						<script language="javascript">
							setStripsTable("dataTable", "tableEven", "tableOdd");
							setHoveredTable("dataTable", false);
						</script>



					</div>
					</td>
                    </tr>
                </table></td>
              </tr>
              
            </table></td>

<td noWrap vAlign="top" >&nbsp;
  


<table align=center cellPadding=0 cellSpacing=0 width="90%" border=0>
							<tr>
								<td bgColor=#999999 class="box-title" style = "color:#000; height:16px;">
									<!-- Other Links -->
									
												<digi:trn key="aim:otherLinks">
												Links
												</digi:trn>
									
								</td>
							</tr>
							<tr>
								<td bgColor=#ffffff class=box-border>
									<table cellPadding=5 cellSpacing=1 width="100%">
										<tr>
											<td>
												<div style="width:100px; ">
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												
												
												
											     
											     <c:set var="translation">
							                      <digi:trn key="btn:componentTypeManagerAddTypeTitle">Add Type</digi:trn>
							                    </c:set>
							                    <digi:link href="#" onclick="javascript:addType(); return false;" title="${translation}" styleClass="dr-menu" property="submitButton"   >
													<digi:trn key="btn:componentTypeManagerAddType">Add Type</digi:trn>
												</digi:link>
							                    
							             
												</div>
											</td>
										</tr>
																				
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
													<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
												</c:set>
												<digi:link href="/admin.do" title="${translation}" >
												<digi:trn key="aim:AmpAdminHome">
												Admin Home
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										<!-- end of other links -->
									</table>
</td></tr></table>






</td></tr>
              </table>
</td>
                    </tr>
        </table>
</td>
                </tr></digi:form>

