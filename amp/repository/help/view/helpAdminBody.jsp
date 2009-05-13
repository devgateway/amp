<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<style type="text/css">

.highlight {background-color:silver; }


</style>

<digi:instance property="helpForm" />
<table width="100%" align="center" cellpadding="5" cellspacing="0" border="0">
          <tr>
            <td height="16" valign="center"><c:if test="${helpForm.blankPage==false}">
              <c:if test="${not empty helpForm.helpErrors}">
                <c:forEach var="error" items="${helpForm.helpErrors}"> <font color="red">${error}</font><br />
                </c:forEach>
              </c:if>
            </c:if></td>
          </tr>
  <tr >
            <c:if test="${not empty helpForm.searched}">
            <td height="12" valign="center"  style="background:#222E5D;"><span style="font-family: serif; color: white" >
              
              <digi:trn key="help:searchedTpc">Search result</digi:trn>
            </span> </td>
            </c:if>
  </tr>
          <tr>
            <td>
      			
                               <c:forEach var="sarched" items="${helpForm.searched}">
				               <c:if test="${not empty sarched}">
				                <div style="padding: 12px">
				                	<font size="1px"><b>
				                	<a href=""> ${sarched.label}</a>
				                	</b></font><br>
				                
			              				       ${sarched.value}
			          		   </div>
			          		     </c:if>
			          		    </c:forEach>    	   
			              	
			              
		               
                   
                      	<c:if test="${!helpForm.flag && empty helpForm.searched}"><b>
                       		<digi:edit key="help:topic:default">no topic selected</digi:edit></b>
                        </c:if>
                   
                      <c:if test="${helpForm.flag}">
                          <c:if test="${helpForm.topicKey!=''}"><b><digi:trn key="${helpForm.titleTrnKey}" ></digi:trn></b></c:if>
                      </c:if>
                          
                  	  <c:if test="${helpForm.flag}">
                      	<c:if test="${helpForm.topicKey!=''}">
                        	<digi:edit key="${helpForm.bodyEditKey}">no text preview</digi:edit>
                    	</c:if>
                      </c:if> 
         		</td>
          </tr>
        </table>
