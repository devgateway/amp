<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/calendar.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addFunding.js"/>"></script>
<jsp:include page="../../aim/view/scripts/newCalendar.jsp"  />


<script language="JavaScript">      

      function validateValue(valId){
    	var num=document.getElementById(valId);
    	var amount=num.value;
    	var validChars= "0123456789.";
    	var dotsAmount=0; //there should be only one '.' character and not at the beggining like .3 or 0.2.6
    	var errorMsg="<digi:trn>Please enter numeric value only</digi:trn>";
    	var errorAppeared=false;
    	for (var i = 0;  i < amount.length;  i++) {
    		var ch = amount.charAt(i);
    		if (validChars.indexOf(ch)==-1){
    			errorAppeared=true;
    			break;    			    			
    		}else{
        		if(ch=='.'){
        			dotsAmount++;
        		}        		
    		}
    	}
    	if(dotsAmount>1 || (dotsAmount==1 &&  amount.indexOf('.')==0)){
    		errorAppeared=true;
		}
    	if(errorAppeared){
    		alert(errorMsg);
			num.value="";
			return false;
    	}
    	return true;  
      }
      
    function addData(){
  		<digi:context name="addEditIndVal" property="/widget/indSectRegManager.do~actType=addValue" />
        document.gisIndicatorSectorRegionForm.action = "${addEditIndVal}";
        document.gisIndicatorSectorRegionForm.submit();
      }


      function deleteData(ind){
          var flag = confirm("Delete this data?");
          if(flag == true){
            <digi:context name="deleleteValue" property="/widget/indSectRegManager.do~actType=removeValue" />
              document.gisIndicatorSectorRegionForm.action = "${deleleteValue}~deleteValIndex="+ind;
              document.gisIndicatorSectorRegionForm.submit();
          }
      }

      function cancel(){
        <digi:context name="addEditIndVal" property="/widget/indSectRegManager.do~actType=cancel" />
          document.gisIndicatorSectorRegionForm.action = "${addEditIndVal}";
          document.gisIndicatorSectorRegionForm.submit();
      }



      function validation(){
          var values=document.getElementsByTagName("select");
          var baseValue=0;
          var targetValue=0;
          if(values!=null){
              for (var i=0;i<values.length;i++){
                        
                  if (values[i].selectedIndex==1){
                      baseValue++;
                  }else if(values[i].selectedIndex==2){
                      targetValue++;
                  }
              }	
          }
          //for every actual value we should have base and target values
          if(baseValue==0||targetValue==0){
              var msg='<digi:trn key="gis:addEditValue:enterBaseAndTargetValues">Please ensure that you enter at least 1 base and 1 target value</digi:trn>';
              alert(msg);
              return false;	
          }
          return true;
      }
</script>

<digi:instance property="gisIndicatorSectorRegionForm" />
<digi:form action="/indSectRegManager.do~actType=saveValue" method="post">
    <table width="55%" border="0" cellpadding="15">
        <tr>
            <td>
                <span class="crumb">
                    <c:set var="translation">
                        <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
                    </c:set>
                    <html:link  href="/aim/admin.do" styleClass="comment" title="${translation}" >
                        <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
                    </html:link>&nbsp;&gt;&nbsp;
                     <html:link  href="/widget/indSectRegManager.do~actType=viewAll" styleClass="comment">
                    <digi:trn key="gis:Navigation:ResultsDashboardDataManager">Results Dashboard Data Manager</digi:trn>
                     </html:link>
                    &nbsp;&gt;&nbsp;
                    <digi:trn key="admin:Navigation:addEditValue">Add/Edit Data</digi:trn>
                </span>
            </td>
        </tr>
        <tr>
            <td>
                <span class="subtitle-blue"><digi:trn key="gis:addEditValue:pageHeader">Add/Edit Data For: </digi:trn>${gisIndicatorSectorRegionForm.indicatorName}/${gisIndicatorSectorRegionForm.sectorName}/${gisIndicatorSectorRegionForm.regionName}</span>
            </td>
        </tr>
        <tr>
            <td>
                <html:hidden name="gisIndicatorSectorRegionForm" property="indSectId"/>
                <table width="70%">
                    <tr bgColor="#d7eafd">
                        <td align="center" valign="middle" nowrap>
                            <b><digi:trn key="gis:addEditValue:actualbasetarget">Actual/Base/Target</digi:trn></b>
                        </td>
                        <td align="center" valign="middle">
                            <b><digi:trn key="gis:addEditValue:value">Value</digi:trn></b>
                        </td>
                        <td align="center" valign="middle">
                            <b><digi:trn key="gis:addEditValue:creationdate">Date</digi:trn></b>
                        </td>
                        <td align="center" valign="middle" width="10%">
                            <b><digi:trn key="gis:addEditValue:operation">Operation</digi:trn></b>
                        </td>
                    </tr>
                     <tr>
                        <td colspan="4">
                            &nbsp;
                        </td>
                    </tr>
                    <tr>
                    <c:if test="${!empty gisIndicatorSectorRegionForm.values}">
                        <c:forEach var="values" varStatus="index" items="${gisIndicatorSectorRegionForm.values}">
                            <tr>
                                <td  height="10" align="center" width="10%">
                                    <html:select name="values" property="valueType" styleClass="inp-text" indexed="true">
                                        <html:option value="1"><digi:trn key="gis:addEditValue:actual">Actual</digi:trn></html:option>
                                        <html:option value="2"><digi:trn key="gis:addEditValue:base">Base</digi:trn></html:option>
                                        <html:option value="0"><digi:trn key="gis:addEditValue:target">Target</digi:trn></html:option>
                                    </html:select>
                                </td>
                                
                                <td height="10" align="center" width="10%">                                    
                                    <html:text name="values" property="value" indexed="true" styleClass="amt" styleId="val_${index.index}" onblur="validateValue('val_${index.index}')"/>
                                </td>
                                
                                <td  height="10" align="center" nowrap="nowrap">
                                    <html:text name="values" property="valueDateString" indexed="true" styleId="txtDate${index.count-1}" readonly="true" style="width:80px;"/>
                                    <a id="date${index.count-1}" href='javascript:pickDateById("date${index.count-1}","txtDate${index.count-1}")'>
                                        <img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0> 
                                    </a> 
                                    
                                </td>
                                
                                
                                <td align="center">
                                    <a href="javascript:deleteData('${index.count-1}')"> 
                                        <!-- newly added value has no id in db before the save operation, so to refer values we will use their order id in the list -->
                                        <img src="../ampTemplate/images/trash_16.gif" border="0" alt="Delete indicator value" />
                                    </a>
                                </td>
                            </tr>        
                        </c:forEach>   
                    </c:if>
                    <c:if test="${empty gisIndicatorSectorRegionForm.values}">
                        <tr><td colspan="4" align="center">
                            <b>
                            <digi:trn key="aim:noIndicatorValuesPresent">No data present</digi:trn></b></td>
                        </tr>
                        <tr colspan="4">
                            <td>
                                &nbsp;
                            </td>
                        </tr>
                    </c:if>
                    
                    
                    <tr>
                        <td colspan="4">
                            <hr>
                        </td>
                    </tr>
                    
                    <tr>
                        <td  align="center" colspan="4">
                            <input style="font-family:verdana;font-size:11px;" type="button" name="addValBtn" value="<digi:trn key='gis:addEditValue:adddata'>Add Value</digi:trn>" onclick="addData()"/>&nbsp;&nbsp;
                        </td>
                    </tr>  
                    <tr>
                        <td colspan="4">
                            <c:set var="trn"><digi:trn key="aim:btn:save">Save</digi:trn></c:set> 
                            <html:submit onclick="return validation()" styleClass="dr-menu">
                                ${trn}
                            </html:submit>     
                            &nbsp;&nbsp;
                           <input class="dr-menu" type="button" value="<digi:trn key='gis:addEditValue:cancelBtn'>Cancel</digi:trn>" onclick="cancel()"/>
                            
                        </td>
                    </tr>
                    <tr><td width="100%" colspan="4"><br>
                            <font color="red"> *<digi:trn key="gis:addEditValue:enterBaseAndTargetValues">Please ensure that you enter at least 1 base and 1 target value</digi:trn></font> 
                    </td></tr>
                    
                </table>
                
            </td>
        </tr>
    </table>
    
    
    
    
</digi:form>

