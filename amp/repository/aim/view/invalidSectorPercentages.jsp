<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<digi:instance property="invalidDataList" />

<style type="text/css">

a.itr:hover {
	border-width: 0; /* IE */
}

.itr {
	position: relative;
	text-decoration: none;
	
}

.itr:hover .bpop 
{
	display: block;
	position: absolute;
	width: 100px;
	background-color: white;
	padding: 3px 5px 4px 5px;
	border: 1px Silver solid;
	
	left: 8em;
	top: 0.6em;
}
.itr {
	display: inline-block;
	top: 0.15em;
	line-height: 1.05;
}
.bpop {
	display: none;
}
</style>

<script type="text/javascript">
	function saveAsDraft(actId){		
		<digi:context name="save" property="context/module/moduleinstance/invalidSectorPercentages.do" />
	    document.invalidDataList.action = "<%= save %>?saveDraft=true&actId="+actId;
	    document.invalidDataList.submit();
	}
</script>

<digi:form action="/invalidSectorPercentages.do">
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=800 border="0">
    <tr>
      <td class=r-dotted-lg width=14>&nbsp;</td>
      <td align=left class=r-dotted-lg valign="top" width=800>
        <table cellPadding=5 cellspacing="0" width="100%" border="0">
          <tr>
            <td height=33><span class=crumb>
              <c:set var="translation">
                <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
              </c:set>
              <digi:link href="/admin.do" styleClass="comment" title="${translation}" >
                <digi:trn key="aim:AmpAdminHome">
                Admin Home
                </digi:trn>
              </digi:link>&nbsp;&gt;&nbsp;
               <digi:trn>Invalid Sector Percentages</digi:trn>
            </td>
          </tr><%-- End navigation --%>
          <tr>
            <td height="16" vAlign="middle" width="100%">
              <span class="subtitle-blue">
                <digi:trn>Invalid Sector Percentages</digi:trn>
              </span>
            </td>
          </tr>         
          <tr>
            <td noWrap width="100%" vAlign="top">
              <table width="100%" cellspacing="0" cellSpacing="0" border="0">
                <tr>
                  <td noWrap width="100%" vAlign="top">
                    <table bgColor=#d7eafd cellpadding="0" cellspacing="0" width="100%" valign="top">
                      <tr bgColor=#ffffff>
                        <td vAlign="top" width="100%">
                          <table width="100%" cellspacing="0" cellpadding="0" valign="top" align="left" border="0">
                            <tr>
                              <td>
                                <table style="font-family:verdana;font-size:11px;" border="0" width="100%">                                 
                                  <tr>
                                    <td colspan="6" width="100%" align="center">
                                      <table width="100%" align="center"  border="0" style="font-family:verdana;font-size:11px;">
                                        <tr bgColor="#d7eafd">
                                          <td width="60%" nowrap="nowrap">
                                              <b><digi:trn>Activity Name</digi:trn></b>                                                                                        
                                          </td>
                                          <td width="10%" align="left" nowrap="nowrap">
                                              <b><digi:trn>AMP ID</digi:trn></b>
                                          </td>
                                          <td width="10%" nowrap="nowrap" align="left">
                                          	<b><digi:trn>Number Of Sectors</digi:trn></b>
                                          </td>
                                          <td width="5%">&nbsp;</td>
                                          <td width="10%" nowrap="nowrap" align="right">
                                          	<b><digi:trn>Total Percentage</digi:trn></b>
                                          </td>
                                          <td width="5%">&nbsp;</td>
                                        </tr>
                                        <c:if test="${empty invalidDataList.invalidSectorpercentages}">
                                        	<tr>
                                        		<td colspan="4"><digi:trn>No Indicators Present</digi:trn> </td>
                                        	</tr>
                                        </c:if>
                                        <c:if test="${!empty invalidDataList.invalidSectorpercentages}">
                                          <c:forEach var="act" items="${invalidDataList.invalidSectorpercentages}">
                                            <tr onmouseover="style.backgroundColor='#dddddd';" onmouseout="style.backgroundColor='white'">
                                              <td width="65%" nowrap="nowrap" align="left">
	                                            <a class="itr" href="/aim/selectActivityTabs.do~ampActivityId=${act.activityId}">${act.activityName}</a>
                                               </td>
                                               <td width="10%" align="left">
	                                                ${act.ampId}
	                                          </td>
                                              <td align="center" width="10%">
                                             	${act.numOfSectors}
											  </td>
											  <td width="5%">&nbsp;</td>
											  <td width="10%" align="center">
											  	${act.totalPercentage}
											  </td>
											  <c:set var="draftTrn"><digi:trn>Save as Draft</digi:trn></c:set>
											  <td width="5%">
											  	<a href="javascript:saveAsDraft(${act.activityId})"><digi:img border="0" src="/repository/aim/images/draft.png" title="${draftTrn}"/> </a>
											  </td>
				                           </tr>
                                          </c:forEach>
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
