<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<style type="text/css">

.highlight {background-color:silver; }

	.bodyFull { display : none; }
	.bodyShort { display : block; }

</style>

<script type="text/javascript">

function showBody(title){
    var topic=$(title).parents("div:eq(1)");
	$(topic).find("div.bodyShort").toggle();
	$(topic).find("div.bodyFull").toggle();
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
		    <div id="title" >
	            <c:if test="${not empty helpForm.searched}">
		           <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
		            	<ul class="yui-nav">&nbsp;
		            	     <li class="selected" style="width:100%">
	                          	<a title='<digi:trn jsFriendly="true" key="aim:PortfolioOfReports">Search Results</digi:trn>'>
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
                    <div id="bodyhelp" style="background-color:white;overflow:auto;display: block; text-align: left;">
		             </div>
                      <!-- <a onclick="editTopic();">E</a> -->
         		</td>
       	   </tr>
        </table>
<div id="key" style="display:none;"></div>
<div id="progress" align="center" style="display:block;" height="4px" width="4px"></div>

