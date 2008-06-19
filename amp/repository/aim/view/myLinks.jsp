<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<br/>
<div id="content"  class="yui-skin-sam" style="width:100%;"> 
	
    <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
        <ul class="yui-nav">
          <li class="selected">
          <a title='<digi:trn key="aim:ListofRelatedLinks">Frequently Used Links for Desktop</digi:trn>'>
          <div>
            <digi:trn key="aim:resources">Resources</digi:trn>
          </div>
          </a>
          </li>
        </ul>
        <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
        	<div style="margin-top: 5px;margin-bottom: 2px">
	        	<digi:trn key="aim:webResources">Web Resources</digi:trn>
        	</div>
        	<c:set var="translation">
	            <digi:trn key="aim:clickToViewLinkDetails">Click here to view Link Details</digi:trn>
            </c:set>				
	        <logic:notEmpty name="myLinks" scope="session">
                <div title="${translation}">
                		<logic:iterate name="myLinks" id="link" scope="session" type="org.digijava.module.aim.helper.Documents"> 
                        <div style="margin: 2px">
                        	<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10>
                            <digi:link href="/viewQuickLinks.do">
                                <bean:write name="link" property="title"/>
                            </digi:link>
                        </div>
                </logic:iterate>
               
                <bean:size id="linkCount" name="myLinks" scope="session" />
			         <c:set var="translation">
                        <digi:trn key="aim:clickToViewMoreLinks">Click here to view more Links</digi:trn>
                    </c:set>					
				  <div title='${translation}'  style="margin-left:12px;margin-top:5px; margin-bottom:7px">
                        <digi:link href="/viewQuickLinks.do">
                            <digi:trn key="aim:moreLinks">More Links ...</digi:trn>
                        </digi:link>							
                    </div>
  				  </logic:notEmpty>
  				   <logic:empty name="myLinks" scope="session">
               		 <div style="margin-left:12px;margin-top:5px; margin-bottom:7px"><digi:trn key="aim:noDesktopLinks">No links</digi:trn></div>
           		  </logic:empty>
  				 
                 	 <logic:notEmpty name="myDocuments" scope="session">
  						<div style="margin-top: 5px;margin-bottom: 2px"><digi:trn key="aim:documents">Documents</digi:trn></div>
	  						<div title="${translation}" style="margin: 2px">
		               			 <logic:iterate name="myDocuments" id="doc" scope="session" type="org.digijava.module.contentrepository.helper.DocumentData"> 
		                    	   <div style="margin: 2px">
		                        	<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10>
		                            <a href="/contentrepository/documentManager.do">
		                                <bean:write name="doc" property="title"/>
		                            </a>
		                      	 </div>
		                </logic:iterate>
		                
		                 <c:set var="translation">
	                        <digi:trn key="aim:clickToViewMoreDocuments">Click here to view more Documents</digi:trn>
	                    </c:set>	
	               		
	               		 <div title='${translation}'  style="margin-left:12px;margin-top:5px; margin-bottom:7px">
	                          <a href="/contentrepository/documentManager.do">
	                            <digi:trn key="aim:moreDocuments">More Documents ...</digi:trn>
	                        </a>							
	                    </div>
	                    
              	 	 </logic:notEmpty>    
                       <logic:empty name="myDocuments" scope="session">
               			 <div style="margin-left:12px;margin-top:5px; margin-bottom:7px"> <digi:trn key="aim:noDcouments">No Documents</digi:trn></div>
           			  </logic:empty>
		</div>
</div>
                
                