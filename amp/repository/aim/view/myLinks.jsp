<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>

<c:set var="translation">
	<digi:trn key="aim:clickToViewMoreResources">Click here to view more resources</digi:trn>
</c:set>
<module:display name="Content Repository" parentModule="Resources">
	<div class="right_menu" style="margin-top:20px;">
	<div class="right_menu_header">
		<div class="right_menu_header_cont">
			<digi:trn key="aim:resources">Resources</digi:trn>
		</div>
	</div>
	<div class="right_menu_box">
		<div class="right_menu_cont">
			<logic:notEmpty name="recentResources" scope="session">
				<logic:iterate name="recentResources" id="doc" scope="session" type="org.digijava.module.contentrepository.helper.DocumentData"> 
					<c:if test="${doc.webLink == null}" >
			        	<a  href="/contentrepository/downloadFile.do?uuid=<bean:write name='doc' property='uuid'/>">
			        		<li class="tri"><bean:write name='doc' property='title'/></li>
			        	</a>
			        </c:if>
			        <c:if test="${doc.webLink != null}" >
						<a href="<bean:write name="doc" property="webLink"/>">
			            	<bean:write name='doc' property='title'/>
						</a>
					</c:if>
			    </logic:iterate>
				<div title="Click here to view More Resources" style="padding-top:10px;margin-left:12px;margin-top:5px; margin-bottom: 7px">
			   	<a href="/contentrepository/documentManager.do?type=team">
		       		<digi:trn key="aim:moreResources">More resources</digi:trn>
				</a>	 	
				</div>
	      </logic:notEmpty>    
	      <logic:empty name="recentResources" scope="session">
          	<digi:trn key="aim:noResources">No Resources Viewed</digi:trn>
          </logic:empty>       		
          </div>
		</div>
	</div>
</module:display>

    