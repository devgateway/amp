<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">
	<!--
    function addProgram() {
      <digi:context name="selPrg" property="context/module/moduleinstance/addProgram.do?edit=true"/>
      var prgSels=document.getElementsByName("selPrograms");
      var urlParams;
      var flag=false;

      if(prgSels!=null){
        if(prgSels[prgSels.length-1].value==-1){
          var i=0;
          for(i=prgSels.length-1;i>-1;i--){
             if(prgSels[i].value!=-1){
               urlParams="<%=selPrg%>&themeid="+prgSels[i].value+"&op=add";
               flag=true;
               break;
             }
          }
          if(!flag){
            return false;
          }
        }else{
          urlParams="<%=selPrg%>&themeid="+prgSels[prgSels.length-1].value+"&op=add";
        }
      }

      document.aimEditActivityForm.action = urlParams;
      document.aimEditActivityForm.target=window.opener.name;
      document.aimEditActivityForm.submit();
      window.close();
      return true;
    }

    function reloadProgram(selectedProgram,reload) {

    	if(reload==1)
    	 {
		<digi:context name="selProgram" property="context/module/moduleinstance/addProgram.do?edit=true"/>

        var prgSels=document.getElementsByName("selPrograms");
        var flag=false;
        var i=0;
        //alert(selectedProgram.value);
        if(selectedProgram.value==-1){
          for(i=0;i<prgSels.length;i++){
            if(prgSels[i].value==-1){
              urlParams="<%=selProgram%>&themeid="+prgSels[i].value+"&selPrgLevel="+(i+1);
              flag=true;
              break;
            }
          }
        }

        if(!flag){
          var urlParams="<%=selProgram%>&themeid="+selectedProgram.value;
        }

        document.aimEditActivityForm.action = urlParams;
        document.aimEditActivityForm.submit();
        }

      }

    function resetResults(){
        <digi:context name="resetPrg" property="context/module/moduleinstance/addProgram.do?edit=true"/>
        var urlParams="<%=resetPrg%>";
	    document.aimEditActivityForm.action = urlParams;
  		document.aimEditActivityForm.submit();
    }

    function closeWindow(){
      window.close();
    }
	-->
</script>

<digi:instance property="aimEditActivityForm" />
<digi:form action="/addProgram.do" method="post">
<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border=0>
  <tr>
    <td vAlign="top">
      <table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
        <tr>
          <td align=left vAlign=top>
            <table bgcolor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding id="tblSlo">
              <tr bgcolor="#006699">
                <td vAlign="center" width="100%" align ="center" class="textalb" height="20">
                  <digi:trn key="aim:selectActivityProgram">Select Program</digi:trn>
                </td>
              </tr>
              <tr>
                <td align="center" bgcolor=#ECF3FD>
                  <table cellSpacing=2 cellPadding=2>
                    <c:if test="${!empty aimEditActivityForm.programLevels}">
                    <% int i=0; %>
                      <c:forEach var="prgLevels" varStatus="varSt" items="${aimEditActivityForm.programLevels}">
                      <%i++; %>
                        <tr>
                          <td width="120" align="right">
                            <c:if test="${varSt.count==1}">
                            <digi:trn key="aim:programScheme">Program scheme</digi:trn>
                            </c:if>
                            <c:if test="${varSt.count!=1}">
                            <digi:trn key="aim:subProgramLevel">Sub program level</digi:trn> ${varSt.count-1}
                            </c:if>
                            
                            <html:hidden name="aimEditActivityForm" property="visibleProgram" value="aaa"/>
                            
                          </td>
                          
                          <bean:define id="crtVisibleProgram" value="0"/>
                          <bean:define id="nxtVisibleProgram" value="0"/>
                          
                          <field:display name="Sub Program Level ${varSt.count}" feature="NPD Dashboard">
                          	<bean:define id="crtVisibleProgram" value="1"/>
                          </field:display>
                          <logic:lessEqual name="varSt" property="count" value="7">
                              <field:display name="Sub Program Level ${varSt.count+1}" feature="NPD Dashboard">
                          	  </field:display>
                          </logic:lessEqual>
                          <%
                          ServletContext x=session.getServletContext();
                          	org.dgfoundation.amp.visibility.AmpTreeVisibility atv=(org.dgfoundation.amp.visibility.AmpTreeVisibility)x.getAttribute("ampTreeVisibility");
                          	org.digijava.module.aim.dbentity.AmpFieldsVisibility field=(org.digijava.module.aim.dbentity.AmpFieldsVisibility)atv.getFieldByNameFromRoot("Sub Program Level "+i);
                          if(field!=null)	
                          if(field.isFieldActive(atv))
                        	  {%><bean:define id="nxtVisibleProgram" value="1"/>
                        	  <%}
                          else {%><bean:define id="nxtVisibleProgram" value="0"/>
                          <%} %>
							<td id="slo${varSt.count}">
                            <html:select property="selPrograms" onchange="reloadProgram(this,${nxtVisibleProgram})" styleClass="inp-text">
                              <option value="-1">-<digi:trn key="aim:selectProgram">Select Program</digi:trn>-</option>
                              <html:optionsCollection name="prgLevels" value="ampThemeId" label="name" />
                           	</html:select>
                          	</td>
                        </tr>
                      </c:forEach>
                    </c:if>
                    <tr>
                      <td align="center" colspan=2>
                        <table cellPadding=5>
                          <tr>
                            <td>
                              <input type="button" value="<digi:trn key='btn:add'>Add</digi:trn>" class="dr-menu" onclick="addProgram()">
                            </td>
                            <td>
                              <input type="button" value="<digi:trn key='btn:reset'>Reset</digi:trn>" class="dr-menu" onclick="resetResults()">
                            </td>
                            <td>
                              <input type="button" value="<digi:trn key='btn:close'>Close</digi:trn>" class="dr-menu" onclick="closeWindow()">
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
    </td>
  </tr>
</table>
<script language="JavaScript" type="text/javascript">
//var prgSels=document.getElementsByName("selPrograms");
//var maxLeng=0;
//for (var i = 0; i < prgSels.length; i++) {
//  alert(prgSels[i].width);
//  if(prgSels[i].width>maxLeng){
//    alert(prgSels[i].width);
//    maxLeng=prgSels[i].width;
//
//  }
//}
//alert(maxLeng);
//
//for (var i = 0; i < prgSels.length; i++) {
//    prgSels[i].width=maxLeng;
//}

var sels=document.getElementById("tblSlo");
if((sels.clientWidth)>document.body.clientWidth){
	window.moveTo(0,screen.height/2-document.body.clientHeight/2)
  	window.resizeTo(sels.clientWidth+150,650);
}
window.moveTo(screen.width/2-document.body.clientWidth/2,screen.height/2-document.body.clientHeight/2)
</script>
</digi:form>
