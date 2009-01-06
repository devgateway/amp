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

<script type="text/javascript">

    function validate(topickey){
		if(!topickey){
			alert("Default topic is not tree topic");
		return false;
	}else{
            return true;
        }
}
  function getKey(){
    var topicKey = document.getElementById("key").innerHTML;
    var key = topicKey.slice(topicKey.indexOf("y:")+2);
    return key;
  }

function editTopic(){

    if(validate(getKey())){

        <digi:context name="editTopic" property="context/module/moduleinstance/helpActions.do~actionType=editHelpTopic"/>
		document.helpForm.action = "<%=editTopic%>~topicKey="+getKey()+"~wizardStep=0";
		document.helpForm.target = "_self";
		document.helpForm.submit();

    }

}
</script>

<digi:instance property="helpForm" />
<table width="100%" align="center" cellpadding="5" cellspacing="0" border="0">
          <tr>
            <td height="16" valign="center"><c:if test="${helpForm.blankPage==false}">
              <c:if test="${not empty helpForm.helpErrors}">
                <c:forEach var="error" items="${helpForm.helpErrors}"> <font color="red">${error}</font><br />
                </c:forEach>
               </c:if>
             </c:if>
            </td>
          </tr>
  		<tr>

		    <div id="title">
	            <c:if test="${not empty helpForm.searched}">
		           <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
		            	<ul class="yui-nav">&nbsp;
		            	     <li class="selected" style="width:100%">
	                          	<a title='<digi:trn key="aim:PortfolioOfReports">Search Results</digi:trn>'>
	                          		<div style="border-left-width:1px">
	                          	      <digi:trn key="aim:PortfolioOfReports">Search Results</digi:trn>
	                                </div>
	                            </a>
	                         </li>
	                    </ul>
		            </div>
	           </c:if>
          </div>
      </tr>
          <tr>
             <td>

               <div id="bodyhelp" style="background-color:white;overflow:auto;display: block; text-align: left;" >
		    </div>         

                 <a onclick="editTopic();">E</a>
                 <c:forEach var="sarched" items="${helpForm.searched}">
		                <c:if test="${not empty sarched}">
		                	<div id="searchedTitle" style="padding: 12px">
		                			<font size="1px"><b><a href=""> ${sarched.label}</a>
		                			</b></font><br>${sarched.value}
		                	</div>
	          		     </c:if>
	          		   </c:forEach>

		            <div id="searchedBody">
	                      	<c:if test="${!helpForm.flag && empty helpForm.searched}"><b>
	                       		<digi:edit key="help:topic:default">no topic selected</digi:edit></b>
	                        </c:if>
                             
                              <c:if test="${helpForm.flag}">
	                          <c:if test="${helpForm.topicKey!=''}"><b><digi:trn >${helpForm.titleTrnKey}</digi:trn></b></c:if>
	                      	</c:if>
	                     	<c:if test="${helpForm.flag}">
	                      		<c:if test="${helpForm.topicKey!=''}">
	                        		<digi:edit key="${helpForm.bodyEditKey}"></digi:edit>
                                </c:if>
                                 <c:if test="${helpForm.topicKey == ''}"><digi:trn>no text preview</digi:trn></c:if>
                          </c:if>
                     </div>
         		</td>
       	   </tr>
        </table>
<div id="key" style="display:none;"></div>