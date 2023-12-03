<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<digi:instance property="aimIndicatorForm" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/common.js"/>"></script>

<script language="JavaScript">
    <!--
    function searchIndicators(){	
    	<digi:context name="searchInd" property="context/ampModule/moduleinstance/searchIndicators.do?showAddIndPage=false" />
        document.aimIndicatorForm.action = "<%=searchInd%>";
        document.aimIndicatorForm.target = "_self";
        document.aimIndicatorForm.submit();	
    }

    function addIndicatorTL(addbutton) {
    	var emptychk = false;
        if(addbutton == '1')
        	emptychk = doesItHaveValue1();
        if(addbutton == '2')
        	emptychk = doesItHaveValue2()
        if(emptychk == true) {
        	<digi:context name="addInd" property="context/ampModule/moduleinstance/addIndicatorsTL.do"/>
            document.aimIndicatorForm.action = "<%=addInd%>~forStep9=true";
            document.aimIndicatorForm.target = window.opener.name;
            document.aimIndicatorForm.submit();
            window.close();
        }        
    }

    function addNewIndicatorTL(selectedSectsSize) {	
    	var valid = validateForm(selectedSectsSize);
        if (valid == true) {
        	<digi:context name="addNewInd" property="context/ampModule/moduleinstance/addNewIndicatorTL.do"/>
            document.aimIndicatorForm.action = "<%=addNewInd%>";
            document.aimIndicatorForm.target = "_self";
            document.aimIndicatorForm.submit();
        }
        return valid;
    }

    function unload(){}    

    function validateForm(selectedSectsSize) {        						
    	var values = document.getElementsByTagName("forEach");                
        if (document.aimIndicatorForm.indicatorName.value.length == 0) {
        	alert("Please enter indicator name");
            document.aimIndicatorForm.indicatorName.focus();
            return false;
        }

    	if (document.aimIndicatorForm.indicatorCode.value.length == 0) {
        	alert("Please enter indicator code");
            document.aimIndicatorForm.indicatorCode.focus();
            return false;
        }

        if(selectedSectsSize!=1) {
        	alert("Please add Sectors");
            return false;
        }
        return true;
    }

    function isSearchKeyGiven() {
    	if(trim(document.aimIndicatorForm.searchkey.value).length == 0) {
        	alert("Please give a Keyword to search");
            document.aimIndicatorForm.searchkey.focus();
            return false;
        }
        return true;
    }

    function doesItHaveValue1() {
    	if(document.aimIndicatorForm.selectedIndicators.value == '') {
        	alert("Please select an Indicator");
            document.aimIndicatorForm.selectedIndicators.focus();
            return false;
        }
        return true;
    }

    function doesItHaveValue2() {
    	if(document.aimIndicatorForm.selIndicators.value == null) {
        	alert("Please select an Indicator");
            return false;
        }
        return true;
    }        

    function closeWindow () {
    	window.close();
    }        

    function clearform() {
    	<digi:context name="searchInd" property="context/ampModule/moduleinstance/searchIndicators.do?action=clear"/>
        document.aimIndicatorForm.action = "<%= searchInd%>";
        document.aimIndicatorForm.submit();
    }
        
    function addSectors() {		
    	<digi:context name="addSector" property="context/ampModule/moduleinstance/sectorActions.do?actionType=loadSectors&sectorReset=true" />
        openURLinWindow('<%= addSector%>',550,400);          
        <digi:context name="justSubmit" property="context/ampModule/moduleinstance/sectorActions.do?actionType=justSubmit" />
        aimIndicatorForm.action = "<%=justSubmit%>";  
        aimIndicatorForm.submit();    
    }

    function removeSelSectors() {	
    	if (validateSector()) {
        	<digi:context name="remSec" property="context/ampModule/moduleinstance/sectorActions.do?actionType=removeSelectedSectors" />
            document.aimIndicatorForm.action = "<%= remSec%>";
            document.aimIndicatorForm.target = "_self"
            document.aimIndicatorForm.submit();
            return true;			
        }else {
        	return false;
        }
    }        

    function validateSector(){
    	if (document.aimIndicatorForm.selActivitySector.checked != null) {
        	if (document.aimIndicatorForm.selActivitySector.checked == false) {
            	alert("Please choose a sector to remove");
                return false;
            }
        } else {
        	var length = document.aimIndicatorForm.selActivitySector.length;
            var flag = 0;
            for (i = 0;i < length;i ++) {
            	if (document.aimIndicatorForm.selActivitySector[i].checked == true) {
                	flag = 1;
                    break;
                }
            }
            if (flag == 0) {
            	alert("Please choose a sector to remove");
                return false;
            }
        }
        return true;	
 	}
    
    function gotoCreateIndPage() {
    	<digi:context name="addIndPage" property="context/ampModule/moduleinstance/searchIndicators.do?clear=true&addInd=true"/>
        document.aimIndicatorForm.action = "<%=addIndPage%>";		
        document.aimIndicatorForm.submit();
    }
    -->
</script>

<digi:form action="/selectCreateIndicators.do">
    
    <html:hidden property="activityId" />
    <html:hidden property="addswitch" />
    
    <jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>
    <c:set target="${bcparams}" property="tId" value="-1"/>
    <c:set target="${bcparams}" property="dest" value="teamLead"/>
    <table bgColor="#ffffff" cellPadding="0" cellSpacing="0" width="772">
        <tr>
            <td class="r-dotted-lg" width="14">&nbsp;</td>
            <td align="left" class="r-dotted-lg" vAlign="top" width="750">
                <table cellPadding="5" cellSpacing="0" width="100%" border="0">            
                    <tr>
                        <td vAlign="top">
                            <table bgcolor="#f4f4f2" cellPadding="5" cellSpacing="5" width="100%" class="box-border-nopadding">
                                <tr>
                                    <td align="left" vAlign="top">
                                    <table bgcolor="#f4f4f2" cellPadding="0" cellSpacing="0" width="100%" class="box-border-nopadding">
                                        <tr bgcolor="#006699">
                                            <td vAlign="middle" width="100%" align ="center" class="textalb" height="20">
                                                <digi:trn>Search Indicators</digi:trn>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td align="center" bgcolor="#ECF3FD">
                                                <table cellSpacing="2" cellPadding="2">
                                                    <tr>
                                                        <td><digi:trn>Select Sector</digi:trn></td>
                                                        <td>
                                                            <html:select property="sectorName" styleClass="inp-text">
                                                                <html:option value="-1">-<digi:trn key="aim:selsector">Select sector</digi:trn>-</html:option>
                                                                <c:if test="${!empty aimIndicatorForm.allSectors}">
                                                                    <html:optionsCollection name="aimIndicatorForm" property="allSectors" value="name" label="name" />						
                                                                </c:if>
                                                            </html:select>	
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td><digi:trn>Enter a keyword</digi:trn></td>
                                                        <td>
                                                            <html:text property="searchkey" size="20" styleClass="inp-text"/>&nbsp;&nbsp;	
                                                        </td>
                                                    </tr>												
                                                    <tr>
                                                        <td align="center" colspan=2>&nbsp;														
                                                            <html:button  styleClass="dr-menu" property="searchIndicatorKeyword" onclick="return searchIndicators()">
                                                                <digi:trn>Search</digi:trn> 
                                                            </html:button>&nbsp;
                                                            <html:button  styleClass="dr-menu" property="submitButton" onclick="clearform()" >
                                                                <digi:trn>Clear</digi:trn> 
                                                            </html:button>&nbsp;
                                                            <html:button  styleClass="dr-menu" property="submitButton" onclick="closeWindow()">
                                                                <digi:trn>Close</digi:trn> 
                                                            </html:button>&nbsp;														
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <table width="100%" cellspacing="0" cellSpacing="0" border="0">
                                            <tr>
                                                <td noWrap  vAlign="top">
                                                    
                                                    <table width="100%" align="center" border="0" style="font-family:verdana;font-size:11px;">
                                                        <tr bgcolor="#006699">
                                                            <td vAlign="center" width="100%" align ="center" class="textalb" height="20" colspan="2">
                                                                <b><digi:trn> Pick from the List</digi:trn></b>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td align="center" bgcolor="#ECF3FD">
                                                                <table cellSpacing="2" cellPadding="3" vAlign="top" align="center">
                                                                    <logic:notEmpty name="aimIndicatorForm" property="searchReturn">
                                                                        <tr>
                                                                            <td  align="right" valign="center"><digi:trn>Indicator Name</digi:trn></td>
                                                                            <td  align="left">
                                                                                <html:select property="selectedIndicators" styleClass="inp-text"size="6" multiple="true">																								
                                                                                    <logic:notEmpty name="aimIndicatorForm" property="searchReturn">
                                                                                        <html:optionsCollection name="aimIndicatorForm"	property="searchReturn" value="indicatorId" label="name"/>
                                                                                    </logic:notEmpty>
                                                                                </html:select>
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td  align="center" colspan="2">
                                                                                <html:button  styleClass="dr-menu" property="addFromList"  onclick="addIndicatorTL(1)">
                                                                                    <digi:trn>Add</digi:trn> 
                                                                                </html:button>
                                                                            </td>
                                                                        </tr>
                                                                    </logic:notEmpty>
                                                                    <logic:empty name="aimIndicatorForm" property="searchReturn">
                                                                        <tr>
                                                                            <td><digi:trn>No Indicators in the List</digi:trn></td>
                                                                        </tr>
                                                                    </logic:empty>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr><td>
                                        <table bgcolor="#f4f4f2" cellPadding="0" cellSpacing="0" width="100%" class="box-border-nopadding">
                                            <tr bgcolor="#006699">
                                                <td vAlign="center" width="100%" align ="center" class="textalb" height="20">
                                                    <b><digi:trn>Create a New Indicator</digi:trn></b>
                                                </td>
                                            </tr>	
                                            <c:if test="${not empty aimIndicatorForm.showAddInd && aimIndicatorForm.showAddInd=='false'}">
                                                <tr>
                                                    <td bgcolor="#ECF3FD" align="center" colspan="2" >
                                                        <html:button  styleClass="dr-menu" property=""  onclick="gotoCreateIndPage()">
                                                            <digi:trn>Add new Indicator</digi:trn> 
                                                        </html:button>
                                                    </td>
                                                </tr>									
                                            </c:if>								
                                            <c:if test="${not empty aimIndicatorForm.showAddInd && aimIndicatorForm.showAddInd=='true'}">
                                                <tr>
                                                    <td align="center" bgcolor="#ECF3FD">
                                                        <table cellSpacing="2" cellPadding="2">
                                                            <tr>
                                                                <td><digi:trn>Indicator Name</digi:trn><font color="red">*</font></td>
                                                                <td>
                                                                    <html:text property="indicatorName" size="20" styleClass="inp-text"/>	
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td><digi:trn>Description</digi:trn></td>
                                                                <td>
                                                                    <html:textarea property="indicatorDesc" cols="35" rows="2" styleClass="inp-text"/>&nbsp;&nbsp;	
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <digi:trn>Indicator Code</digi:trn><font color="red">*</font>
                                                                </td>
                                                                <td>
                                                                    <html:text property="indicatorCode" size="20" styleClass="inp-text"/>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td><digi:trn>Indicator Type</digi:trn></td>
                                                                <td>
                                                                    <html:select property="ascendingInd" styleClass="inp-text">
                                                                        <html:option value="A"><digi:trn>ascending</digi:trn></html:option>
                                                                        <html:option value="D"><digi:trn>descending</digi:trn></html:option>
                                                                    </html:select>
                                                                </td>
                                                            </tr>	
                                                            <tr>
                                                                <td><digi:trn>Sectors</digi:trn><font color="red">*</font></td>
                                                                <td>	<!-- sectors start -->
                                                                    <table cellPadding="5" cellSpacing="1" border="0" width="100%"	bgcolor="#d7eafd">
                                                                        <tr>
                                                                            <td bgcolor="#ECF3FD" width="100%">
                                                                                <table cellPadding="1" cellSpacing="1" border="0" bgcolor="#ffffff" width="100%">
                                                                                    <c:if test="${empty aimIndicatorForm.selectedSectorsForInd}">
                                                                                        <tr>
                                                                                            <td bgcolor="#ECF3FD">
                                                                                                <input type="button" class="dr-menu" onclick="addSectors();" value='<digi:trn jsFriendly="true">Add Sectors</digi:trn>' />
                                                                                            </td>
                                                                                        </tr>
                                                                                    </c:if>
                                                                                    <c:if test="${!empty aimIndicatorForm.selectedSectorsForInd}">
                                                                                        <c:set var="selectedSectsSize" value="1"></c:set> <!-- used for sectors validation -->
                                                                                        <tr>
                                                                                            <td>
                                                                                                <table cellSpacing="0" cellPadding="0" border="0" bgcolor="#ffffff" width="100%">
                                                                                                    <c:forEach var="activitySectors" items="${aimIndicatorForm.selectedSectorsForInd}" varStatus="varS">
                                                                                                        <tr>
                                                                                                            <td>
                                                                                                                <table width="100%" cellSpacing="1" cellPadding="1" vAlign="top" align="left">
                                                                                                                    <tr bgcolor="#ECF3FD">
                                                                                                                        <td width="3%" vAlign="center">
                                                                                                                            <html:multibox property="selActivitySector">
                                                                                                                                <c:if test="${activitySectors.subsectorLevel1Id == -1}">
                                                                                                                                ${activitySectors.sectorId}
                                                                                                                                </c:if>
                                                                                                                                <c:if test="${activitySectors.subsectorLevel1Id != -1 && activitySectors.subsectorLevel2Id == -1}">
                                                                                                                                ${activitySectors.subsectorLevel1Id}
                                                                                                                                </c:if>
                                                                                                                                <c:if test="${activitySectors.subsectorLevel1Id != -1 && activitySectors.subsectorLevel2Id != -1}">
                                                                                                                                ${activitySectors.subsectorLevel2Id}
                                                                                                                                </c:if>
                                                                                                                            </html:multibox>
                                                                                                                        </td>
                                                                                                                        <td  width="87%" vAlign="center" align="left">
                                                                                                                            <c:if test="${!empty activitySectors.sectorName}">
                                                                                                                                [${activitySectors.sectorName}]
                                                                                                                            </c:if>
                                                                                                                            <c:if test="${!empty activitySectors.subsectorLevel1Name}">
                                                                                                                                [${activitySectors.subsectorLevel1Name}]
                                                                                                                            </c:if>
                                                                                                                            <c:if test="${!empty activitySectors.subsectorLevel2Name}">
                                                                                                                                [${activitySectors.subsectorLevel2Name}]
                                                                                                                            </c:if>
                                                                                                                        </td>
                                                                                                                    </tr>
                                                                                                                </table>
                                                                                                            </td>
                                                                                                        </tr>
                                                                                                    </c:forEach>
                                                                                                    <tr bgcolor="#ECF3FD">
                                                                                                        <td>
                                                                                                            <table cellSpacing="2" cellPadding="2">
                                                                                                                <tr>
                                                                                                                    <logic:notEmpty name="MS" scope="application">
                                                                                                                        <td>
                                                                                                                            <input type="button" value="<digi:trn>Add Sectors</digi:trn>" class="dr-menu"  onclick="addSectors();">
                                                                                                                        </td>
                                                                                                                    </logic:notEmpty>
                                                                                                                    <td >
                                                                                                                        <input type="button" class="dr-menu" onclick="return removeSelSectors()" value='<digi:trn jsFriendly="true">Remove Sector</digi:trn>' />
                                                                                                                    </td>
                                                                                                                </tr>
                                                                                                            </table>
                                                                                                        </td>
                                                                                                    </tr>
                                                                                                </table>
                                                                                            </td>
                                                                                        </tr>
                                                                                    </c:if>
                                                                                </table>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                    <!-- sectors end --> 
                                                                </td>													
                                                            </tr>	
                                                            <tr>
                                                                <td>
                                                                    <digi:trn>Creation date:</digi:trn>
                                                                </td>
                                                                <td>
                                                                    <html:text property="creationDate" disabled="true" styleId="txtCreationDate" style="font-family:verdana;font-size:11px;width:80px;"/>
                                                                </td>
                                                            </tr>											
                                                            <tr>
                                                                <td align="center" colspan=2>&nbsp;														
                                                                    <html:button  styleClass="dr-menu" property="addnewIndicator"  onclick="addNewIndicatorTL(${selectedSectsSize})">
                                                                        <digi:trn>Add</digi:trn> 
                                                                    </html:button>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </c:if>								
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>    
</digi:form>