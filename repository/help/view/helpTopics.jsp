<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<script language="javascript" type="text/javascript">
	function toggleDiv(id,state){
		if (state==true){
			document.getElementById('uncollapse'+id).style.display='block';
			document.getElementById('collapse'+id).style.display='none';
		}
		if (state==false){
			document.getElementById('collapse'+id).style.display='block';
			document.getElementById('uncollapse'+id).style.display='none';
		}
	}
</script>
<digi:instance property="helpForm" />
<digi:context name="url" property="context/module/moduleinstance/helpActions.do?actionType=viewSelectedHelpTopic" />
<div id="content"  class="yui-skin-sam" style="width:100%;"> 
	<div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
               <ul class="yui-nav">
                          <li class="selected">
                          <a title='<digi:trn key="aim:PortfolioOfReports">Portfolio Reports </digi:trn>'>
                          <div>
                          	<digi:trn key="aim:helpTopic">Help Topics</digi:trn>
                          </div>
                          </a>
                          </li>
                        </ul>
                        <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
                        <c:if test="${ not empty helpForm.topicTree}">
                                  <c:if test="${not empty helpForm.parentId}">
                                    <c:if test="${helpForm.parentId!=''}">
                                      <c:set var="curTopicParentId">${helpForm.parentId}</c:set>
                                    </c:if>
                                  </c:if>
                                    <c:forEach var="parent" items="${helpForm.topicTree}">
                                      <c:if test="${curTopicParentId!=parent.helpTopicId}">
                                           <div id="collapse${parent.helpTopicId}" style="display:block; padding:3px">
                                            <c:if test="${not empty parent.children}">
                                            	<digi:img  src="images/tree_plus.gif" onclick="javascript:toggleDiv(${parent.helpTopicId},true);"/>
	                                        	   
                                            </c:if>
                                            <c:if test="${empty parent.children}">
                                            <digi:img  src="images/tree_minus.gif" onclick="javascript:toggleDiv(${parent.helpTopicId},true);"/>
                                            </c:if>
                                                <a href="${url}&amp;topicKey=${parent.topicKey}" onclick="alert(${url})">
                                                    <digi:trn key="${parent.titleTrnKey}"></digi:trn>
                                                  </a> 
                                            </div>
                                          
                                          
                                          
                                          <div id="uncollapse${parent.helpTopicId}" style="display:none;padding:3px">
                                          	<digi:img src="images/tree_minus.gif" onclick="javascript:toggleDiv(${parent.helpTopicId},false);"/>
                                                	<a href="${url}&amp;topicKey=${parent.topicKey}">
                                                    	<digi:trn key="${parent.titleTrnKey}"></digi:trn>
                                                    </a>                                                   
                                                      <c:if test="${not empty parent.children}">
                                                       	<c:forEach var="child" items="${parent.children}">
                                                        	<div id="item" style="padding-left:15px;padding-top:2px;padding-bottom:2px;">
                                                        		 <div id="collapse${child.helpTopicId}" style="display:block;padding:3px">
                                          							<c:if test="${empty child.children}">
                                          							<digi:img src="images/tree_minus.gif" onclick="javascript:toggleDiv(${child.helpTopicId},false);"/>
                                                              		</c:if>
                                                              		<c:if test="${not empty child.children}">
                                          							<digi:img src="images/tree_plus.gif" onclick="javascript:toggleDiv(${child.helpTopicId},true);"/>
                                                              		</c:if>
                                                              			<a href="${url}&amp;topicKey=${child.topicKey}">
                                                                			<digi:trn key="${child.titleTrnKey}"></digi:trn></a>
                                                                 		</div>
	                                                                 	
	                                                                 	<div id="uncollapse${child.helpTopicId}" style="display:none;padding:3px">	
	                                                              			<digi:img src="images/tree_minus.gif" onclick="javascript:toggleDiv(${child.helpTopicId},false);"/>
	                                                              			<a href="${url}&amp;topicKey=${child.topicKey}">
	                                                                			<digi:trn key="${child.titleTrnKey}"></digi:trn></a>
	                                                                 				<c:if test="${not empty child.children}">
		                                                               						<c:forEach var="childchild" items="${child.children}">
		                                                                						<div id="item" style="padding-left:15px;padding-top:2px;padding-bottom:2px;">
		                                                     
		                                                                							 <div id="collapse${childchild.helpTopicId}" style="display:block;padding:3px">
		                                                                								<c:if test="${empty childchild.children}">
                                          																	<digi:img src="images/tree_minus.gif" onclick="javascript:toggleDiv(${childchild.helpTopicId},false);"/>
                                                              											</c:if>
                                                              												<c:if test="${not empty childchild.children}">
                                          																		<digi:img src="images/tree_plus.gif" onclick="javascript:toggleDiv(${childchild.helpTopicId},true);"/>
                                                              											    </c:if>
		                                                                								<a href="${url}&amp;topicKey=${childchild.topicKey}">
		                                                                 		 							<digi:trn key="${childchild.titleTrnKey}"></digi:trn></a>
		                                                                 					    	</div>                 			
		                                                                 						</div>
		                                                                 						
		                                                                 						<div id="uncollapse${childchild.helpTopicId}" style="display:none;padding:1px">
			                                                                 						<div id="item" style="padding-left:15px;padding-top:1px;padding-bottom:1px;">
		                                                                							 		<digi:img src="images/tree_minus.gif" onclick="javascript:toggleDiv(${childchild.helpTopicId},false);"/>
                                                              											 <a href="${url}&amp;topicKey=${childchild.topicKey}">
		                                                                 		 							<digi:trn key="${childchild.titleTrnKey}"></digi:trn></a>
		                                                                 		 						  	            <c:if test="${not empty childchild.children}">
											                                                               						<c:forEach var="childchildchild" items="${childchild.children}">
											                                                                						<div id="item" style="padding-left:15px;padding-top:3px;padding-bottom:3px;">
											                                                                								<a href="${url}&amp;topicKey=${childchildchild.topicKey}">
											                                                                 		 							<digi:trn key="${childchildchild.titleTrnKey}"></digi:trn>
											                                                                 		 						</a>
											                                                                 						</div>
											                                                                 					</c:forEach>
										                                                                				</c:if>
		                                                                 					   		</div>
		                                                                 						</div>
		                                                                 						
		                                                                 					</c:forEach>
	                                                                			  </c:if>
	                                                                	  </div>
                                               		             </div>
                                                      	</c:forEach>
                                                      </c:if>
                                               </div>
                                          
                                          
                                        </c:if>
                                            <c:if test="${curTopicParentId==parent.helpTopicId}">
                                             <div id="collapse${parent.helpTopicId}" style="display:none;padding:3px">
                                              		<digi:img src="images/arrow_right.gif" onclick="javascript:toggleDiv(${parent.helpTopicId},true);"/>
                                                   <a href="${url}&amp;topicKey=${parent.topicKey}">
                                                      <digi:trn key="${parent.titleTrnKey}"></digi:trn>
                                                    </a> 
                                                   
                                              </div>
                                              <div id="uncollapse${parent.helpTopicId}" style="display: block;padding:3px">
                                                <digi:img src="images/arrow_down.gif" onclick="javascript:toggleDiv(${parent.helpTopicId},false);"/>
                                                      <a href="${url}&amp;topicKey=${parent.topicKey}">
														<digi:trn key="${parent.titleTrnKey}"></digi:trn>
                                                      </a>
                                                     
                                                        <c:if test="${not empty parent.children}">
                                                          <c:forEach var="child" items="${parent.children}">
                                                            <div id="item" style="padding-left:15px;padding-top:2px;padding-bottom:2px;">
                                                               <a href="${url}&amp;topicKey=${child.topicKey}">
                                                                  <digi:trn key="${child.titleTrnKey}"></digi:trn>
                                                                </a> 
                                                            </div>
                                                            </c:forEach>
                                                      
                                                        </c:if>
                                              </div>
                                            </c:if>
                                      
                                    </c:forEach>
                                
                              </c:if>
                           
</div>



