<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fmt" prefix="fmt" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<style>

.tableEven {
	background-color:#dbe5f1;
	font-size:8pt;
	padding:2px;
}

.tableOdd {
	background-color:#FFFFFF;
	font-size:8pt;
	padding:2px;
}
 
.Hovered {
	background-color:#a5bcf2;
}
</style>

<script language="javascript">
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

<jsp:include page="/repository/calendar/view/colorPickerPopin.jsp" />
<script language="javaScript" type="">
function setActionMethod(methodName) {
  document.getElementById("method").value=methodName;
  return true;
}

function saveEventType(id) {
  document.getElementById('eventTypeId').value=id;
  setActionMethod('save');
  document.calendarEventTypeForm.submit();
  return true;
}

function setDeleteId(id) {
   <c:set var="message">
	<digi:trn key="calendar:deletingEventType">Are you sure about deleting event type?</digi:trn>						
    </c:set>
    <c:set var="msg">
	${fn:replace(message,'\\n',' ')}
    </c:set>
    
    
    
    <c:set var="quote">'</c:set>
    <c:set var="escapedQuote">\'</c:set>
    <c:set var="deletemsg">
	${fn:replace(msg,quote,escapedQuote)}
    </c:set>

  var quertion=window.confirm('${deletemsg}');
  if(!quertion){
    return false;
  }
  document.getElementById('eventTypeId').value=id;
  setActionMethod('delete');
  document.calendarEventTypeForm.submit();
  return true;
}

function setColorPalete() {
    
    var colors = new Array("#000000", "#000033", "#000066", "#000099",
                           "#0000CC", "#0000FF", "#330000", "#330033",
                           "#330066", "#330099", "#3300CC", "#3300FF",
                           "#660000", "#660033", "#660066", "#660099",
                           "#6600CC", "#6600FF", "#990000", "#990033",
                           "#990066", "#990099", "#9900CC", "#9900FF",
                           "#CC0000", "#CC0033", "#CC0066", "#CC0099",
                           "#CC00CC", "#CC00FF", "#FF0000", "#FF0033",
                           "#FF0066", "#FF0099", "#FF00CC", "#FF00FF",
                           "#003300", "#003333", "#003366", "#003399",
                           "#0033CC", "#0033FF", "#333300", "#333333",
                           "#333366", "#333399", "#3333CC", "#3333FF",
                           "#663300", "#663333", "#663366", "#663399",
                           "#6633CC", "#6633FF", "#993300", "#993333",
                           "#993366", "#993399", "#9933CC", "#9933FF",
                           "#CC3300", "#CC3333", "#CC3366", "#CC3399",
                           "#CC33CC", "#CC33FF", "#FF3300", "#FF3333",
                           "#FF3366", "#FF3399", "#FF33CC", "#FF33FF",
                           "#006600", "#006633", "#006666", "#006699",
                           "#0066CC", "#0066FF", "#336600", "#336633",
                           "#336666", "#336699", "#3366CC", "#3366FF",
                           "#666600", "#666633", "#666666", "#666699",
                           "#6666CC", "#6666FF", "#996600", "#996633",
                           "#996666", "#996699", "#9966CC", "#9966FF",
                           "#CC6600", "#CC6633", "#CC6666", "#CC6699",
                           "#CC66CC", "#CC66FF", "#FF6600", "#FF6633",
                           "#FF6666", "#FF6699", "#FF66CC", "#FF66FF",
                           "#009900", "#009933", "#009966", "#009999",
                           "#0099CC", "#0099FF", "#339900", "#339933",
                           "#339966", "#339999", "#3399CC", "#3399FF",
                           "#669900", "#669933", "#669966", "#669999",
                           "#6699CC", "#6699FF", "#999900", "#999933",
                           "#999966", "#999999", "#9999CC", "#9999FF",
                           "#CC9900", "#CC9933", "#CC9966", "#CC9999",
                           "#CC99CC", "#CC99FF", "#FF9900", "#FF9933",
                           "#FF9966", "#FF9999", "#FF99CC", "#FF99FF",
                           "#00CC00", "#00CC33", "#00CC66", "#00CC99",
                           "#00CCCC", "#00CCFF", "#33CC00", "#33CC33",
                           "#33CC66", "#33CC99", "#33CCCC", "#33CCFF",
                           "#66CC00", "#66CC33", "#66CC66", "#66CC99",
                           "#66CCCC", "#66CCFF", "#99CC00", "#99CC33",
                           "#99CC66", "#99CC99", "#99CCCC", "#99CCFF",
                           "#CCCC00", "#CCCC33", "#CCCC66", "#CCCC99",
                           "#CCCCCC", "#CCCCFF", "#FFCC00", "#FFCC33",
                           "#FFCC66", "#FFCC99", "#FFCCCC", "#FFCCFF",
                           "#00FF00", "#00FF33", "#00FF66", "#00FF99",
                           "#00FFCC", "#00FFFF", "#33FF00", "#33FF33",
                           "#33FF66", "#33FF99", "#33FFCC", "#33FFFF",
                           "#66FF00", "#66FF33", "#66FF66", "#66FF99",
                           "#66FFCC", "#66FFFF", "#99FF00", "#99FF33",
                           "#99FF66", "#99FF99", "#99FFCC", "#99FFFF",
                           "#CCFF00", "#CCFF33", "#CCFF66", "#CCFF99",
                           "#CCFFCC", "#CCFFFF", "#FFFF00", "#FFFF33",
                           "#FFFF66", "#FFFF99", "#FFFFCC", "#FFFFFF");
    var total = colors.length;
    var width = 18;
    var cp_contents = "";
    var windowRef = "";
   
    cp_contents += "<TABLE BORDER=1 CELLSPACING=0 CELLPADDING=0>";
    var use_highlight = (document.getElementById || document.all) ? true : false;
    for(var i = 0; i < total; i++){
        if((i % width) == 0) {
            cp_contents += "<TR>";
        }
        
        cp_contents += "<TD BGCOLOR = '"+colors[i]+"' width=20 height=20 onClick= ColorPicker_highLightColor('"+colors[i]+"');>&nbsp;&nbsp;</TD>";
            if(((i + 1) >= total) || (((i + 1) % width) == 0)){
            cp_contents += "</TR>";
        }
    }
	if(document.getElementById) {
        var width1 = Math.floor(width / 2);
        var width2 = width = width1;
        cp_contents += "<TR><TD height=20 COLSPAN='" + width1 + "' BGCOLOR='#ffffff' ID='colorPickerSelectColor'>&nbsp;</TD><TD height=20 COLSPAN='" + width2 + "' ALIGN='CENTER' ID='colorPickerSelectColorValue'>#FFFFFF</TD></TR>";
    }
    cp_contents += "</TABLE>";
	var displayColorPalete = document.getElementById('displayColorPalete');
	displayColorPalete.innerHTML = cp_contents;
}

function ColorPicker_highLightColor(c) {
    var d = document.getElementById("colorPickerSelectColor");
    d.style.backgroundColor = c;
    d = document.getElementById("colorPickerSelectColorValue");
    d.innerHTML = c;
}

</script>

<digi:form action="/eventTypes.do" method="post">

<!--  AMP Admin Logo -->
<jsp:include page="../../aim/view/teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<input type="hidden" name="eventTypeId" id="eventTypeId" value="-1" />

	<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=1100>
    	<tr>
      		
			<td>
				<table width=550>
					<tr>
					    <td>
					      <span class=crumb>
					        <c:set var="translation">
					          <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
					        </c:set>
					        <digi:link href="/../admin.do" styleClass="comment" title="${translation}" >
					          <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
					        </digi:link>&nbsp;&gt;&nbsp;
					        <digi:trn key="calendar:eventTypes">Event Type Manager</digi:trn>
					      </span>
					    </td>
				  	</tr>
				  	<tr>
				    	<td height=16 vAlign=center>
				      		<span class=subtitle-blue><br />
				        		<input type="hidden" name="method" id="method" value="NONE" />
				        		<digi:trn key="calendar:eventTypes:page_header">Event Type Manager</digi:trn>
				           </span>
				        </td>
				    </tr>
					<tr>
				        <td>
				        	<fieldset>
				             	<legend>
				               	<digi:trn key="calendar:addNewType">Add a new type</digi:trn></legend>
				        		<table width="500">
				        			<tr>
							          <td colspan="5" align="left">
							            <digi:errors />
							          </td>
							        </tr>
				        			<tr>
								    	<td>
								            <digi:trn key="calendar:typeName">
								              Name
								            </digi:trn>
								        </td>
								        <td>
								            <html:text property="eventTypeName" style="width:155px;font-family:verdana;font-size:11px;"/>
								        </td>
								        
								        <td>
								            <digi:trn key="calendar:typeColor">
								              Color
								            </digi:trn>
								        </td>
								        <td>
								            <html:text property="eventTypeColor" styleId="hexColorNum" style="width:155px;font-family:verdana;font-size:11px;"/>
								            <input type="text" style="width:25px;font-family:verdana;font-size:11px;background:#FFF;" id="colorDisp" name="colorDisp" disabled="disabled"/>
								            <a href="javascript:showPaleteContent('colorDisp','hexColorNum');">
								              <img alt="" src="<digi:file src="module/calendar/images/colorImg.gif"/>" border="0" NAME="pick" ID="pick" width="15" height="15"/>
								            </a>
								        </td>
				        
								        <td colspan="2" align="right">
								            <c:set var="translation">
									            <digi:trn key="calendar:addColorButton">
									              Add
									            </digi:trn>
								            </c:set>
								            <html:submit value="${translation}" onclick="setActionMethod('addType')" style="font-family:verdana;font-size:11px;" />
								        </td>
									</tr>
				            		<tr>
							            <td colspan="5">
							                <digi:trn key="calendar:addColorNote">
								              <b>NOTE:</b> Entered color format should be in Hexadecimal. 
								              <b>For Example:</b> #FF3333
							            	</digi:trn>
							            </td>
									</tr>
				            	</table>
				        	</fieldset>
				      	</td>
				    </tr>
				    <tr>
				        <td>
				          	<table  width=550>
				            	<tr>
				              		<td>
				                        <table cellPadding="3" cellSpacing="0">
							                <c:if test="${!empty calendarEventTypeForm.eventTypes}">
							                   	<tr>
													<td>
														<table  width=550 BORDER=0 cellpadding="3" cellspacing="0">
															<tr>
										                        <td bgcolor="#999999" height="30" width="280" style="color:black"><digi:trn key="calendar:typeName"><b>Name</b></digi:trn></td>
										                       	<td bgcolor="#999999" height="30" width="120" style="color:black"><digi:trn key="calendar:typeColor"><b>Color</b></digi:trn></td>
										                        <td bgcolor="#999999" height="30" width="30" style="color:black">&nbsp;</td>
																<td bgcolor="#999999" height="30" width="30" style="color:black">&nbsp;</td>
																<td bgcolor="#999999" height="30" width="30" style="color:black">&nbsp;</td>
																<td bgcolor="#999999" height="30" width="30" style="color:black">&nbsp;</td>
															</tr>
														</table>
													</td>
							                   	</tr>
												<tr>
													<td>
														<div  style="overflow:auto;width:100%;height:170px;max-height:170px;"  >
															<table  width=530 BORDER=0 cellpadding="3" cellspacing="0" id="dataTable"    >
											                     <c:forEach items="${calendarEventTypeForm.eventTypes}" var="eventType" varStatus="varSt">
											                      	<tr>
											                        	<td>
											                          		<html:text name="eventType" size="40" styleId="eventTypeName${varSt.count}" property="name" indexed="true" />
											                        	</td>
											                        	<td>
											                          		<html:text name="eventType" size="15" styleId="eventTypeNameColor${varSt.count}" property="color" indexed="true"/>
											                        	</td>
																		<td>
											                        		<input type="text" style="width:25px;font-family:verdana;font-size:11px;background: ${eventType.color}" name="colorViwe${varSt.count}" id="colorViwe${varSt.count}" disabled="disabled" />
											                        	</td>
											                        	<td width="50" align="center">
											                          		<a href=javascript:showPaleteContent("colorViwe${varSt.count}","eventTypeNameColor${varSt.count}"); >
											                            		<img alt="" src="<digi:file src="module/calendar/images/colorImg.gif"/>" border="0" NAME="pick" ID="pick" width="15" height="15"/>
											                          		</a>
											                        	</td>
											                        	<td  width="50" align="center">
											                          		<a href=javascript:; >
											                          			<img src= "/TEMPLATE/ampTemplate/images/save_16.png" vspace="2" border="0" align="absmiddle" onclick="saveEventType('${eventType.id}');"/>
																			</a>
											                        	</td>
											                        	<td width="50" align="center">
											                         		<a href=javascript:;>
																				<img src= "/TEMPLATE/ampTemplate/images/trash_16.gif" vspace="2" border="0" align="absmiddle" onclick="setDeleteId('${eventType.id}');"/>
																			</a>
											                        	</td>
											                      	</tr>
											                    </c:forEach>
															</table>
														</div> 
													</td>
												</tr>
							                </c:if>
							                <c:if test="${empty calendarEventTypeForm.eventTypes}">
							                  	<digi:trn key="calendar:eventTypesStatus"><div style="font-family:Verdana:font-size:25pt;">No event types in data base</div></digi:trn>
							                </c:if>
							            </table>
				            		</td>
				              	</tr>
				            </table>
				        </td>
				    </tr>
				
					<tr>
				        <td>
							<table>
				             	<tr>
				                 	<td colspan="2">
				                 		<strong><digi:trn key="aim:IconReference">Icons Reference</digi:trn></strong>
				       				</td>
				       			</tr>
				     			<tr>
				           			<td nowrap="nowrap"><img src= "/TEMPLATE/ampTemplate/images/save_16.png" vspace="2" border="0" align="absmiddle" />
				               			<digi:trn key="aim:ClickEditReport">Click on this icon to save the event type&nbsp;</digi:trn>
				               			<br />
				       				</td>
				       			</tr>
				        		<tr>
				           			<td nowrap="nowrap"><img src= "/TEMPLATE/ampTemplate/images/trash_16.gif" vspace="2" border="0" align="absmiddle" />
				               			<digi:trn key="aim:ClickDeleteReport">Click on this icon to delete the event type&nbsp;</digi:trn>
				                   		<br />
									</td>
				           		</tr>
				       		</table>
				     	</td>
				    </tr> 
				</table>
			</td>
			<td width="550">
				<div id="displayColorPalete"></div>
			</td>
		</tr>
	</table>
</digi:form>

<script language="javascript">
setStripsTable("dataTable", "tableEven", "tableOdd");
setHoveredTable("dataTable", false);
setColorPalete();
</script>