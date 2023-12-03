<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<digi:instance property="aimIndicatorForm" />
<script language="JavaScript">
    <!--
    function searchIndicators()
    {	
                        <digi:context name="searchInd" property="context/ampModule/moduleinstance/searchIndicators.do" />
                                document.aimIndicatorForm.action = "<%=searchInd%>?isForStep9=true";
                                document.aimIndicatorForm.target = "_self";
                                document.aimIndicatorForm.submit();
                
                            }
        
                            function gotoCreateIndPage() {
                <digi:context name="addIndPage" property="context/ampModule/moduleinstance/searchIndicators.do?clear=true&addInd=true"/>
                        document.aimIndicatorForm.action = "<%=addIndPage%>";		
                        document.aimIndicatorForm.submit();
                    }
        
                    function addIndicatorTL(addbutton)
                    {
                        var emptychk = false;
                        if(addbutton == '1')
                            emptychk = doesItHaveValue1();
                        if(addbutton == '2')
                            emptychk = doesItHaveValue2()
                        if(emptychk == true)
                        {
                        <digi:context name="addInd" property="context/ampModule/moduleinstance/addIndicatorsTL.do"/>
                                    document.aimIndicatorForm.action = "<%=addInd%>~forStep9=true";
                                    document.aimIndicatorForm.target = window.opener.name;
                                    document.aimIndicatorForm.submit();
                                }
                
                                window.close();
                            }

        
                            function addNewIndicatorTL()
                            {
                                var valid = validateForm();
                                if (valid == true) {
                        <digi:context name="addNewInd" property="context/ampModule/moduleinstance/addNewIndicatorTL.do"/>
                                    document.aimIndicatorForm.action = "<%=addNewInd%>";
                                    document.aimIndicatorForm.target = "_self";
                                    document.aimIndicatorForm.submit();				  
                                }
                                return valid;
                            }
                            function unload(){}

                            function validateForm() {
                                if (trim(document.aimIndicatorForm.indicatorName.value).length == 0) {
                                    alert("Please enter indicator name");
                                    document.aimIndicatorForm.indicatorName.focus();
                                    return false;
                                }

                                if (trim(document.aimIndicatorForm.indicatorCode.value).length == 0) {
                                    alert("Please enter indicator code");
                                    document.aimIndicatorForm.indicatorCode.focus();
                                    return false;
                                }
                                return true;
                            }
                            function isSearchKeyGiven()
                            {
                                if(trim(document.aimIndicatorForm.searchkey.value).length == 0)
                                {
                                    alert("Please give a Keyword to search");
                                    document.aimIndicatorForm.searchkey.focus();
                                    return false;
                                }
                                return true;
                            }
                            function doesItHaveValue1()
                            {
                                if(document.aimIndicatorForm.selectedIndicators.value == '')
                                {
                                    alert("Please select an Indicator");
                                    document.aimIndicatorForm.selectedIndicators.focus();
                                    return false;
                                }
                                return true;
                            }
                            function doesItHaveValue2()
                            {
                                if(document.aimIndicatorForm.selIndicators.value == null)
                                {
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

                    -->
</script>

<digi:form action="/selectCreateIndicators.do">
    <digi:errors/>
    <html:hidden property="activityId" />
    <html:hidden property="addswitch" />
   
    <jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>
    <c:set target="${bcparams}" property="tId" value="-1"/>
    <c:set target="${bcparams}" property="dest" value="teamLead"/>
    <table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
        <tr>
            <td class=r-dotted-lg width=14>&nbsp;</td>
            <td align=left class=r-dotted-lg valign="top" width=750>
                <table cellPadding=5 cellspacing="0" width="100%" border="0">            
                    <tr>
                        <td vAlign="top">
                            <table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
                                <tr>
                                    <td align=left valign="top">
                                        <table bgcolor=#f4f4f2 cellpadding="0" cellspacing="0" width="100%" class=box-border-nopadding>
                                            <tr bgcolor="#006699">
                                                <td vAlign="center" width="100%" align ="center" class="textalb" height="20">
                                                    <digi:trn key="aim:searchind">Search Indicators</digi:trn>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td align="center" bgcolor=#ECF3FD>
                                                    <table cellSpacing=2 cellPadding=2>
                                                        <tr>
                                                            <td><digi:trn key="aim:selsector">Select Sector</digi:trn></td>
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
                                                            <td><digi:trn key="aim:enterKeyword">Enter a keyword</digi:trn></td>
                                                            <td>
                                                                <html:text property="searchkey" size="20" styleClass="inp-text"/>&nbsp;&nbsp;	
                                                            </td>
                                                        </tr>												
                                                        <tr>
                                                            <td align="center" colspan=2>&nbsp;														
                                                                <html:button  styleClass="dr-menu" property="searchIndicatorKeyword" onclick="return searchIndicators()">
                                                                    <digi:trn key="btn:search">Search</digi:trn> 
                                                                </html:button>&nbsp;
                                                                <html:button  styleClass="dr-menu" property="submitButton" onclick="clearform()" >
                                                                    <digi:trn key="btn:clear">Clear</digi:trn> 
                                                                </html:button>&nbsp;
                                                                <html:button  styleClass="dr-menu" property="submitButton" onclick="closeWindow()">
                                                                    <digi:trn key="btn:close">Close</digi:trn> 
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
                                <table width="100%" cellspacing="0" cellspacing="0" border="0">
                                    <tr bgcolor="#006699">
                                        <td vAlign="center" width="100%" align ="center" class="textalb" height="20" colspan="2">
                                            <b><digi:trn key="aim:PickfromList"> Pick from the List</digi:trn></b>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="center" bgcolor="#ECF3FD">
                                            <table cellSpacing=2 cellPadding=3 vAlign="top" align="center" bgcolor="#ECF3FD">
                                                <logic:notEmpty name="aimIndicatorForm" property="nondefaultindicators">
                                                    <tr>
                                                        <td  align="right" valign="center">
                                                            <digi:trn key="aim:meindicatorname">Indicator Name</digi:trn>
                                                        </td>
                                                        <td  align="left">
                                                            <html:select property="selectedIndicators" styleClass="inp-text"size="6" multiple="true">																								
                                                                <logic:notEmpty name="aimIndicatorForm" property="nondefaultindicators">
                                                                    <html:optionsCollection name="aimIndicatorForm"	property="nondefaultindicators" value="indicatorId" label="name"/>
                                                                </logic:notEmpty>
                                                            </html:select>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td align="center" colspan="2">
                                                            <html:button  styleClass="dr-menu" property="addFromList"  onclick="addIndicatorTL(1)">
                                                                <digi:trn key="btn:add">Add</digi:trn> 
                                                            </html:button>
                                                        </td>
                                                    </tr>
                                                </logic:notEmpty>
                                                <logic:empty name="aimIndicatorForm" property="nondefaultindicators">
                                                    <tr>
                                                        <td>
                                                            <digi:trn key="aim:noindicatorinlist">No Indicators in the List</digi:trn>
                                                        </td>
                                                    </tr>
                                                </logic:empty>
                                            </table>
                                        </td>
                                    </tr>
                                    
                                </table>
                                <tr>
                                    <td>
                                        <table bgcolor=#f4f4f2 cellPadding="0" cellSpacing="0" width="100%" class=box-border-nopadding>
                                            <tr bgcolor="#006699">
                                                <td vAlign="center" width="100%" align ="center" class="textalb" height="20">
                                                    <b><digi:trn key="aim:NewIndicatorCreation">Create a New Indicator</digi:trn></b>
                                                </td>
                                            </tr>	
                                            <tr>
                                                <td bgcolor="#ECF3FD" align="center" colspan="2" >
                                                    <html:button  styleClass="dr-menu" property=""  onclick="gotoCreateIndPage()">
                                                        <digi:trn key="btn:crtInd">Add new Indicator</digi:trn> 
                                                    </html:button>
                                                </td>
                                            </tr>																							
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






 