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
	font-size:8pt;!important
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

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/calendar/js/colorPicker.js"/>"></script>

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

var cp = new ColorPicker("window");
var objId=null;
var hexColor=null;
function pickColor(color) {
  if(objId!=null){
    document.getElementById(hexColor).value = color;
    var cl=document.getElementById(objId);
    cl.style.cssText ="width:25px;font-family:verdana;font-size:9pt;background:"+color+";";
   
  }
}

function showColors(objectId,hexColorObject){
  objId=objectId;
  hexColor=hexColorObject;
  var rs=cp.show('pick');
}

function chooseColor(){

}
</script>

<digi:form action="/eventTypes.do" method="post">

  <!--  AMP Admin Logo -->
  <jsp:include page="../../aim/view/teamPagesHeader.jsp" flush="true" />
  <!-- End of Logo -->

  <input type="hidden" name="eventTypeId" id="eventTypeId" value="-1" />

  <table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
    <tr>
      <td class=r-dotted-lg width=14>&nbsp;</td>
      <td>
        <table>
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
                <digi:trn key="calendar:eventTypes:page_header">Event Types</digi:trn>
              </span>
            </td>
          </tr>
          <tr>
            <td>
              <table>
                <tr>
                  <td>
                    <!--<fieldset>-->
                      <table id="dataTable" cellPadding="3" cellSpacing="3">
                        <c:if test="${!empty calendarEventTypeForm.eventTypes}">
                          <tr>
                            <td bgcolor="#999999" style="color:black"><digi:trn key="calendar:typeName"><b>Name</b></digi:trn></td>
                            <td bgcolor="#999999" style="color:black"><digi:trn key="calendar:typeColor"><b>Color</b></digi:trn></td>
                            <td bgcolor="#999999" style="color:black">&nbsp;</td>
							<td bgcolor="#999999" style="color:black">&nbsp;</td>
							<td bgcolor="#999999" style="color:black">&nbsp;</td>
							<td bgcolor="#999999" style="color:black">&nbsp;</td>
                          </tr>
                          <c:forEach items="${calendarEventTypeForm.eventTypes}" var="eventType" varStatus="varSt">
                            <tr>
                              <td>
                                <html:text name="eventType" styleId="eventTypeName${varSt.count}" property="name" indexed="true" />
                              </td>
                              <td>
                                <html:text name="eventType" styleId="eventTypeNameColor${varSt.count}" property="color" indexed="true"/>
                              </td>
                              <td>
                                <input type="text" style="width:25px;font-family:verdana;font-size:11px;background: ${eventType.color}" name="colorViwe${varSt.count}" id="colorViwe${varSt.count}" disabled="disabled" />
                              </td>
                              <td>
                                <a href=javascript:showColors("colorViwe${varSt.count}","eventTypeNameColor${varSt.count}"); >
                                  <img alt="" src="<digi:file src="module/calendar/images/colorImg.gif"/>" border="0" NAME="pick" ID="pick" width="15" height="15"/>
                                </a>
                              </td>
                              <td>
                                <c:set var="translation">
                                  <digi:trn key="calendar:SaveColorButton">
                                  Save
                                  </digi:trn>
                                </c:set>
                                <input type="button" value="${translation}" onclick="saveEventType('${eventType.id}');" />
                              </td>
                              <td>
                                <c:set var="translation">
                                  <digi:trn key="calendar:DeleteColorButton">
                                  Delete
                                  </digi:trn>
                                </c:set>
                                <input type="button" value="${translation}" onclick="setDeleteId('${eventType.id}');" />
                              </td>
                            </tr>
                          </c:forEach>
                        </c:if>
                        <c:if test="${empty calendarEventTypeForm.eventTypes}">
                          <digi:trn key="calendar:eventTypesStatus"><div style="font-family:Verdana:font-size:25pt;">No event types in data base</div></digi:trn>
                        </c:if>
                      </table>
                    <!--</fieldset>-->
                  </td>
                </tr>
              </table>
            </td>
          </tr>

          <tr>
            <td>
              <fieldset>
                <legend>
                  <digi:trn key="calendar:addNewType">Add a new type</digi:trn></legend>
                <table width="100%">
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
                </tr>
                <tr>
                  <td>
                    <digi:trn key="calendar:typeColor">
                      Color
                    </digi:trn>
                  </td>
                  <td>
                    <html:text property="eventTypeColor" styleId="hexColorNum" style="width:155px;font-family:verdana;font-size:11px;"/>
                    <input type="text" style="width:25px;font-family:verdana;font-size:11px;background:#FFF;" id="colorDisp" name="colorDisp" disabled="disabled"/>
                    <a href="javascript:showColors('colorDisp','hexColorNum');">
                      <img alt="" src="<digi:file src="module/calendar/images/colorImg.gif"/>" border="0" NAME="pick" ID="pick" width="15" height="15"/>
                    </a>
                  </td>
                </tr>
                <tr>
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
                      <br />
                      <b>For Example:</b> #FF3333
                    </digi:trn>
                  </td>
                </tr>
              </table>
            </fieldset>
          </td>
        </tr>
    </table>
  </td>
  <td class=r-dotted-lg width=14>&nbsp;</td>
</tr>
</table>
</digi:form>

<script language="javascript">
setStripsTable("dataTable", "tableEven", "tableOdd");
setHoveredTable("dataTable", false);
</script>