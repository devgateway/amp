<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>


<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/common.js"/>"></script>
	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/EnterHitBinder.js'/>" >.</script>

<script language="JavaScript">
	<!--
    function addDefaultProgram() {
      <digi:context name="selPrg" property="context/module/moduleinstance/addProgram.do?edit=true"/>
      var urlParams;
      var defaultProgramId = document.getElementsByName("defaultProgramId")[0];

      urlParams="<%=selPrg%>&themeid="+defaultProgramId.value+"&op=add";

      document.aimEditActivityForm.action = urlParams;
      document.aimEditActivityForm.target=window.opener.name;
      document.aimEditActivityForm.submit();
      window.close();
      return true;
    }

    function addProgram(pType) {
      <digi:context name="selPrg" property="context/module/moduleinstance/addProgram.do?edit=true"/>
      var prgSels=document.getElementsByName("programs.selPrograms");
      var urlParams;
      var flag=false;

      if(prgSels!=null){
        if(prgSels[prgSels.length-1].value==-1){
          var i=0;
          for(i=prgSels.length-1;i>-1;i--){
             if(prgSels[i].value!=-1){
               urlParams="<%=selPrg%>&themeid="+prgSels[i].value+"&op=add&programType="+pType;
               flag=true;
               break;
             }
          }
          if(!flag){
            return false;
          }
        }else{
          urlParams="<%=selPrg%>&themeid="+prgSels[prgSels.length-1].value+"&op=add&programType="+pType;
        }
      }

      document.aimEditActivityForm.action = urlParams;
      document.aimEditActivityForm.target=window.opener.name;
      document.aimEditActivityForm.submit();
      window.close();
      return true;
    }

    function reloadProgram(selectedProgram) {
       	<digi:context name="selProgram" property="context/module/moduleinstance/addProgram.do?edit=true"/>

        var prgSels=document.getElementsByName("selPrograms");
        var flag=false;
        var i=0;
        //alert(selectedProgram.value);
        if(selectedProgram.value==-1){
          for(i=0;i<prgSels.length;i++){
            if(prgSels[i].value==-1){
              urlParams="<%=selProgram%>&themeid="+prgSels[i].value+"&selPrgLevel="+(i+1);
              flag=true
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

    function resetResults(){
        <digi:context name="resetPrg" property="context/module/moduleinstance/addProgram.do?edit=true"/>
        var urlParams="<%=resetPrg%>";
	    document.aimEditActivityForm.action = urlParams;
  		document.aimEditActivityForm.submit();
    }

    function closeWindow(){
      window.close();
    }
    
    var enterBinder	= new EnterHitBinder('addActProgram');
	-->
</script>

<digi:instance property="aimEditActivityForm" />
<digi:form action="/addProgram.do" method="post">
	<c:set var="noDefaultProgram">
		<digi:trn key="aim:noDefaultProgram">
Default program is not selected
</digi:trn>
	</c:set>
	<c:set var="defaultProgram">
		<digi:trn key="aim:defaultProgram">
Default Program
</digi:trn>
	</c:set>



	<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border=0>
		<tr>
			<td vAlign="top">
			<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%"
				class=box-border-nopadding>
				<tr>
					<td align=left vAlign=top>
					<table bgcolor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%"
						class=box-border-nopadding id="tblSlo">
						<tr bgcolor="#006699">
							<td vAlign="center" width="100%" align="center" class="textalb"
								height="20"><digi:trn key="aim:selectProgram">Select Program</digi:trn>
							</td>
						</tr>
						<tr>
							<td align="center" bgcolor=#ECF3FD>
							<table cellSpacing=2 cellPadding=2>
								<tr>
									<td colspan="2" align="center"><c:choose>
										<c:when test="${aimEditActivityForm.programs.programType==1}">
											<c:choose>
												<c:when
													test="${aimEditActivityForm.programs.nationalSetting.defaultHierarchy!=null}">
													<c:out value="${defaultProgram}" />:&nbsp;<c:out
														value="${aimEditActivityForm.programs.nationalSetting.defaultHierarchy.name}" />
														<html:hidden property="defaultProgramId" value="${aimEditActivityForm.programs.nationalSetting.defaultHierarchy.ampThemeId}"/>
														<c:set var="enableDefaultProgram" value="true" />
												</c:when>
												<c:otherwise>
													<c:out value="${noDefaultProgram}" />
												</c:otherwise>
											</c:choose>

										</c:when>

										<c:when test="${aimEditActivityForm.programs.programType==2}">

											<c:choose>
												<c:when
													test="${aimEditActivityForm.programs.primarySetting.defaultHierarchy!=null}">
													<c:out value="${defaultProgram}" />:&nbsp;<c:out
														value="${aimEditActivityForm.programs.primarySetting.defaultHierarchy.name}" />
														<html:hidden property="defaultProgramId" value="${aimEditActivityForm.programs.primarySetting.defaultHierarchy.ampThemeId}"/>
														<c:set var="enableDefaultProgram" value="true" />
												</c:when>
												<c:otherwise>
													<c:out value="${noDefaultProgram}" />
												</c:otherwise>
											</c:choose>

										</c:when>


										<c:otherwise>

											<c:choose>
												<c:when
													test="${aimEditActivityForm.programs.secondarySetting.defaultHierarchy!=null}">
													<c:out value="${defaultProgram}" />:&nbsp;<c:out
														value="${aimEditActivityForm.programs.secondarySetting.defaultHierarchy.name}" />
														<html:hidden property="defaultProgramId" value="${aimEditActivityForm.programs.secondarySetting.defaultHierarchy.ampThemeId}"/>
														<c:set var="enableDefaultProgram" value="true" />
												</c:when>
												<c:otherwise>
													<c:out value="${noDefaultProgram}" />
												</c:otherwise>
											</c:choose>

										</c:otherwise>
									</c:choose></td>
								</tr>
								<c:if test="${!empty aimEditActivityForm.programs.programLevels}">
									<c:forEach var="prgLevels" varStatus="varSt"
										items="${aimEditActivityForm.programs.programLevels}">
										<tr>
											<td width="120" align="right"><c:if
												test="${varSt.count==1}">
												<digi:trn key="aim:programScheme">Program scheme</digi:trn>
											</c:if> <c:if test="${varSt.count!=1}">
												<digi:trn key="aim:subProgramLevel">Sub program level </digi:trn>${varSt.count-1}
                            </c:if></td>
											<td id="slo${varSt.count}"><html:select
												property="programs.selPrograms" onchange="reloadProgram(this)"
												styleClass="inp-text" >
												<option value="-1"><digi:trn
													key="aim:selectProgramOpt">-Select Program-</digi:trn></option>
												<html:optionsCollection name="prgLevels" value="ampThemeId"
													label="name" />
											</html:select></td>
										</tr>
									</c:forEach>


									<tr>
										<td align="center" colspan=2>
										<table cellPadding=5>
											<tr>

												<td>													
													<html:button styleId="addActProgram" styleClass="dr-menu" property="submitButton" onclick="addProgram('${aimEditActivityForm.programs.programType}')">
														<digi:trn>Add</digi:trn>
													</html:button>
												</td>
												<td><html:button styleClass="dr-menu"
													property="submitButton" onclick="resetResults()">
													<digi:trn key="btn:reset">Reset</digi:trn>
												</html:button></td>
												<td><html:button styleClass="dr-menu"
													property="submitButton" onclick="closeWindow()">
													<digi:trn key="btn:close">Close</digi:trn>
												</html:button></td>
											</tr>
											<c:if test="${enableDefaultProgram != null}">
											<tr>
												<td colspan="3" align="center">
												<html:button styleClass="dr-menu"
													property="submitButton" onclick="addDefaultProgram()">
													<digi:trn key="btn:addDefault">Add default program</digi:trn>
												</html:button>
												</td>	
											</tr>										
											</c:if>
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
