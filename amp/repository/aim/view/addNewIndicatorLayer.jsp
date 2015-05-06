<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<digi:instance property="aimIndicatorLayerManagerForm" />


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/jquery.ddslick.min.js"/>"></script>

<script language="JavaScript">

var colorRamp = [
                 ["#e9ced2", "#e4b9c3", "#e1a4b6", "#e08cab","#e071a4", "#e050a0", "#cb4196", "#b6338c", "#a12580", "#8d1874"],
                 ["#e9cfc1", "#e5bca8", "#e3a78d", "#e39070", "#e07958", "#d76448", "#cf4c3a", "#c72d2e", "#b02137", "#981538"],
                 ["#ddd69d", "#d1c58a", "#c5b478", "#b9a367", "#ac9357", "#a08248", "#947239", "#87632c", "#7a541f", "#6c4513"],
                 ["#aee29c", "#a3d189", "#9ac078", "#90af67", "#869e57", "#7c8e47", "#727e39", "#676e2c", "#5d5f1f", "#525013"],
                 ["#a1e2ca", "#8ed2b7", "#7bc3a3", "#6ab48f", "#59a47a", "#499566", "#3a8751", "#2c783a", "#1f6920", "#225913"],
                 ["#a4dfe4", "#91cfd2", "#7ec0c0", "#6db0ae", "#5ca19c", "#4c928a", "#3d8379", "#2f7568", "#216658", "#155848"],
                 ["#c6d6e8", "#a9c8e2", "#82bcdf", "#70adcb", "#5f9eb7", "#4f8fa4", "#3f8191", "#31727f", "#23646d", "#16565c"],
                 [ "#d9d1e8", "#c8c0e2", "#b7afde", "#a29fdc", "#8a8fdb", "#6b81db", "#4775d0", "#366aaf", "#275e94", "#18517b"],
                 ["#e8cde1", "#e3b7db", "#dfa0d9", "#dc87db", "#d36fda", "#c857da", "#b348ce", "#9f39c3", "#8a2bb7", "#741eaa"]];
$( document ).ready(function() {
	var color = "";
	var select =	"<select id='colorRampSelect'>";
	for (var i=0;i<colorRamp.length;i++) {
	   for (var j=0;j<colorRamp[i].length;j++) {
		  color += "<span style='width:60px;background-color:"+colorRamp[i][j]+"'>&nbsp;&nbsp;&nbsp;</span>";
		}
	   var selected = "";
	   if (i == Number(${aimIndicatorLayerManagerForm.selectedColorRampIndex})) {
	      selected ="selected = 'selected'";
	    }
	   select += "<option value='"+i+"'"+ selected +
       " data-description=\""+color+"\"</option>";
    	color = "";
	
	}
  select +=  "</select>";
  $(select).appendTo('#colorRampDiv');
  $('#colorRampSelect').ddslick({
    width: 130,
    onSelected: function(selectedData){
        $('#colorRampHidden').val(selectedData.selectedIndex);
    }   
  });
  		
	document.aimIndicatorLayerManagerForm.name.focus();
	
});


function closePopup() {
  window.close();
}

function isDigit (value) {
	return /^\d+$/.test(value);
}

function validateAndSave() {
	$("#errors").html("");
	var newErrorContent ="<table><tr><td style='color:red;'>";
	if ($('[name=name]')[0].value ==""){
		newErrorContent+='<digi:trn jsFriendly="true">Please enter the Indicator Layer name</digi:trn></td></tr></table>';
		$(newErrorContent).appendTo($("#errors"));
		return false;
	}
	if ($('[name=numberOfClasses]')[0].value ==""){
		newErrorContent+='<digi:trn jsFriendly="true">Please enter the Indicator Layer number of classes</digi:trn></td></tr></table>';
		$(newErrorContent).appendTo($("#errors"));
		return false;
	}
	if ($('[name=admLevelId] option:selected').val()=='null') {
		newErrorContent+='<digi:trn jsFriendly="true">Please enter select the Adm level </digi:trn></td></tr></table>';
		$(newErrorContent).appendTo($("#errors"));
		return false;
	}

	var numberOfClassesValue = $('[name=numberOfClasses]')[0].value;
	var minNumOfClasses = <%=org.digijava.module.aim.util.ColorRampUtil.MIN_CLASSES_INDEX%>;

	if (!isDigit(numberOfClassesValue)) {
		newErrorContent+='<digi:trn jsFriendly="true">Number of classes is not a digit</digi:trn></td></tr></table>';
		$(newErrorContent).appendTo($("#errors"));
		return false;
	}

	if (numberOfClassesValue < minNumOfClasses) {
		newErrorContent+='<digi:trn jsFriendly="true">Number of classes should be greater than</digi:trn>&nbsp;'
		    + minNumOfClasses +'</td></tr></table>';
		$(newErrorContent).appendTo($("#errors"));
		return false;
	}

	
    document.aimIndicatorLayerManagerForm.target = window.opener.name;
    document.aimIndicatorLayerManagerForm.submit();
    closePopup();
}


</script>


<digi:form action="/indicatorLayerManager.do">
<html:hidden property="event" value="save"/>
<html:hidden property="idOfIndicator"/>
  <table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
    <tr>
      <td align=left valign="top">
        <table bgcolor=#aaaaaa cellpadding="0" cellspacing="0" width="100%" class=box-border-nopadding>
          <tr bgcolor="#aaaaaa">
            <td vAlign="center" width="100%" align ="center" class="textalb" height="20">
              <digi:trn>Indicator Layer</digi:trn>
            </td></tr>
            <tr>
              <td vAlign="center" width="100%" align ="center" height="20" id="errors">
                <c:if test="${!empty aimIndicatorLayerForm.errors}">
                  <table>
                    <c:forEach var="ms" items="${aimIndicatorLayerForm.errors}">
                      <tr>
                        <td style="color:red;">
                        ${ms}
                        </td>
                      </tr>
                    </c:forEach>
                  </table>
                </c:if>
              </td></tr>
              <tr>
                <td align="center">
                  <table border="0" cellpadding="2" cellspacing="1" width="100%">
                    <tr bgcolor="#f4f4f2">
                      <td align="right" valign="middle" width="50%">
                        <FONT color=red>*</FONT>
                        <digi:trn>Name:</digi:trn>&nbsp;
                      </td>
                      <td align="left" valign="middle">
                        <html:text property="name" styleClass="inp-text" size="50"/>
                      </td>
                    </tr>
                    <tr bgcolor="#f4f4f2">
                      <td align="right" valign="middle" width="50%">
                        <digi:trn>Description</digi:trn>&nbsp;
                      </td>
                      <td align="left" valign="middle">
                        <html:textarea property="description" styleClass="inp-text" rows="4" cols="50"/>
                      </td>
                    </tr>
                     <tr bgcolor="#f4f4f2">
                      <td align="right" valign="middle" width="50%">
                        <digi:trn>Unit</digi:trn>&nbsp;
                      </td>
                      <td align="left" valign="middle">
                        <html:text property="unit" styleClass="inp-text" size="15"/>
                      </td>
                    </tr>
                    <tr bgcolor="#f4f4f2">
                      <td align="right" valign="middle" width="50%">
                        <FONT color=red>*</FONT>
                        <digi:trn>Number of classes</digi:trn>&nbsp;
                      </td>
                      <td align="left" valign="middle">
                        <html:text property="numberOfClasses" styleClass="inp-text" size="8"/>
                      </td>
                    </tr>
                    <tr bgcolor="#f4f4f2">
                      <td align="right" valign="middle" width="50%">
                        <digi:trn>Color</digi:trn>&nbsp;
                      </td>
                      <td align="left" valign="middle">
                      <html:hidden property="selectedColorRamp" styleId="colorRampHidden" value="0"/> 
                      <div id="colorRampDiv">
                      </div>
                     </td>
                    </tr>
                    <tr bgcolor="#f4f4f2">
                      <td align="right" valign="middle" width="50%">
                        <FONT color=red>*</FONT>
                        <digi:trn>ADM Level</digi:trn>&nbsp;
                      </td>
                      <td align="left" valign="middle">
                       <c:set var="trnSelectAdm">
                                <digi:trn >Select an ADM Level</digi:trn>&nbsp;
                        </c:set>
	                    <html:select property="admLevelId">
							<html:option value="null">- ${trnSelectAdm} -</html:option>
								<html:optionsCollection property="admLevelList" value="id" label="value"/>
						</html:select>
					   </td>
                    </tr>
                      <tr bgcolor="#ffffff">
                      <td colspan="2">
                        <table width="100%" cellpadding="3" cellspacing="3" border="0">
                          <tr>
                            <td align="right">
                              <c:set var="trnSaveBtn">
                                <digi:trn key="aim:btnSave">Save</digi:trn>&nbsp;
                              </c:set>
                              <input type="button" value="${trnSaveBtn}" onclick="return validateAndSave()" class="dr-menu">
                            </td>
                            <td align="left">
                              <c:set var="trnCloseBtn">
                                <digi:trn key="aim:btnClose">Close</digi:trn>&nbsp;
                              </c:set>
                              <input type="button" value="${trnCloseBtn}" onclick="return closePopup()" class="dr-menu">
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
              <tr>
                <td>&nbsp;&nbsp;
                    <digi:trn>All fields marked with an </digi:trn><FONT color=red><B><BIG>*</BIG></B></FONT><digi:trn> are required. </digi:trn>
       
                </td>
              </tr>
        </table>
      </td>
            </tr>
  </table>
</digi:form>
