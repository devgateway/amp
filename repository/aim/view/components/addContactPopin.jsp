<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/aim"prefix="aim"%>

<digi:context name="digiContext" property="context"/>
<digi:instance property="aimAddContactForm"/>

<digi:form name="contactForm" type="aimAddContactForm" action="/addAmpContactInfo.do" method="post">
    <html:hidden name="aimAddContactForm" property="contactId" styleId="contactId"/>
    <html:hidden name="aimAddContactForm" property="temporaryId" styleId="temporaryId"/>
    	<table cellpadding="2" cellspacing="5" width="100%" border="1" height="100%">
			<tr height="5px"><td colspan="2"/></tr>
			<!-- page logic row start -->                
            <tr>
			<!-- create new contact td start -->
			<td width="50%" height="100%">
				<table cellpadding="2" cellspacing="5" width="100%" height="100%" class="box-border-nopadding" >
					<tr height="5px"><td colspan="2"/></tr>		
					<tr>
						<td align="right"><strong><digi:trn>Title</digi:trn></strong></td>
						<td align="left">
              <c:set var="translation">
              	<digi:trn>Please select from below</digi:trn>
              </c:set>
              <category:showoptions multiselect="false" firstLine="${translation}" name="aimAddContactForm" property="title"  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.CONTACT_TITLE_KEY%>" styleClass="selectStyle" outerid="contactTitle"/>
						</td>
					</tr>
					<tr>
						<td align="right"><strong><digi:trn>Firstname</digi:trn></strong><font color="red">*</font></td>
						<td align="left"><html:text property="firstName" size="30" styleId="firstName"/></td>
					</tr>
					<tr>
						<td align="right"><strong><digi:trn>Lastname</digi:trn></strong><font color="red">*</font></td>
						<td align="left"><html:text property="lastname" size="30" styleId="lastname"/></td>
					</tr>
					<tr>
						<td align="right"><strong><digi:trn>Email</digi:trn></strong>
						<td align="left" nowrap="nowrap">
							<logic:notEmpty name="aimAddContactForm" property="emails">
								 <logic:iterate name="aimAddContactForm" property="emails" id="foo" indexId="ctr">
								 	<div>
								 		<html:text name="aimAddContactForm" property="emails[${ctr}].value" size="30" styleId="email_${ctr}"/>																																 		
											 <a href="javascript:removeData('email',${ctr})"> 
										 		<img src= "/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" vspace="2" border="0"/>
								 		 	</a>
								 		<c:if test="${ctr==aimAddContactForm.emailsSize-1}">
								 			<c:set var="trnadd"><digi:trn>Add New</digi:trn></c:set>
      										<input id="addEmailBtn" style="font-family:verdana;font-size:11px;" type="button" name="addValBtn" value="${trnadd}" onclick="addNewData('email')">
								 		</c:if>
								 	</div>																										                    
								</logic:iterate>
							</logic:notEmpty>
							<logic:empty name="aimAddContactForm" property="emails">
								<c:set var="trnadd"><digi:trn>Add New</digi:trn></c:set>
      							<input id="addEmailBtn" style="font-family:verdana;font-size:11px;" type="button" name="addValBtn" value="${trnadd}" onclick="addNewData('email')">
							</logic:empty>
						</td>				
					</tr>										
					<tr>
						<td align="right"><strong><digi:trn>Function</digi:trn></strong></td>
                        <td align="left"><html:text property="function" size="30" styleId="function"/></td>
					</tr>	
					<tr>
						<td align="right"><strong><digi:trn>Organization</digi:trn></strong></td>
                        <td align="left"><html:text property="organisationName" size="30" styleId="organisationName"/></td>
					</tr>
					<c:if test="${not empty aimAddContactForm.addOrgButtonState && aimAddContactForm.addOrgButtonState=='visible'}">
						<tr>
	                    	<td colspan="2" align="center">
	                        	<c:choose>
	                            	<c:when test="${empty aimAddContactForm.organizations}">
	                                	<aim:addOrganizationButton refreshParentDocument="false" callBackFunction="addOrganizations2Contact()" collection="organizations"  form="${aimAddContactForm}" styleClass="dr-menu"><digi:trn>Add Organizations</digi:trn></aim:addOrganizationButton>
	                                </c:when>
								    <c:otherwise>
	                                	<table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
	                                    	<c:forEach var="organization" items="${aimAddContactForm.organizations}">
	                                        	<tr>
	                                            	<td width="3px">
	                                                	<html:multibox property="selContactOrgs">
	                                                    	<bean:write name="organization" property="ampOrgId" />
	                                                    </html:multibox>
	                                                 </td>
	                                                 <td align="left">
	                                                 	<bean:write name="organization" property="name" />
	                                                 </td>
	                                             </tr>
	                                          </c:forEach>
	                                          <tr>
	                                          	<td colspan="2">
	                                            	<aim:addOrganizationButton refreshParentDocument="false" callBackFunction="addOrganizations2Contact()" collection="organizations"  form="${aimAddContactForm}" styleClass="dr-menu"><digi:trn>Add Organizations</digi:trn></aim:addOrganizationButton>
	                                                <input type="button" class="dr-menu" onclick="javascript:removeContactOrgs();" value='<digi:trn>Remove Organization(s)</digi:trn>' />
	                                            </td>
	                                    	   </tr>
	                                	</table>
	                            	</c:otherwise>
	                        	</c:choose>
	            			</td>
	                    </tr>
					</c:if>
					<c:if test="${empty aimAddContactForm.addOrgButtonState || aimAddContactForm.addOrgButtonState=='hidden'}">
						<c:if test="${not empty aimAddContactForm.orgsToShowOnPage}">
							<tr>
								<td>&nbsp;</td>
		                    	<td align="left">		                    		
	                            	<c:forEach var="organization" items="${aimAddContactForm.orgsToShowOnPage}">
	                            		<div>
	                            			<bean:write name="organization" property="name" />
	                            		</div>
	                                </c:forEach>
		                    	</td>
	                    	</tr>
						</c:if>
					</c:if>
                    <tr>
						<td align="right" valign="top" ><strong><digi:trn>Phone Number</digi:trn></strong></td>
						<td align="left" nowrap="nowrap">
							<logic:notEmpty name="aimAddContactForm" property="phones">
								<logic:iterate name="aimAddContactForm" property="phones" id="foo" indexId="ctr">
									<div>
										
										<c:set var="translationNone">
              				<digi:trn>None</digi:trn>
              			</c:set>
										
              			<category:showoptions multiselect="false" firstLine="${translationNone}" name="aimAddContactForm" property="phones[${ctr}].phoneTypeId"  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.CONTACT_PHONE_TYPE_KEY%>" styleClass="selectStyle" outerid="phoneType_${ctr}"/>
										<%--
										<html:text name="aimAddContactForm" property="phones[${ctr}].phoneType" size="10" styleId="phoneType_${ctr}"/>																															 																																	 	
										--%>
									   	<html:text name="aimAddContactForm" property="phones[${ctr}].value" size="16" styleId="phoneNum_${ctr}"/>
									  	<a href="javascript:removeData('phone',${ctr})"> 
									 		<img src= "/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" vspace="2" border="0"/>
									 	</a>
									  	<c:if test="${aimAddContactForm.phonesSize==0 ||  ctr==aimAddContactForm.phonesSize-1}">
											<c:set var="trnadd"><digi:trn>Add New</digi:trn></c:set>
      										<input id="addPhoneBtn" style="font-family:verdana;font-size:11px;" type="button" name="addValBtn" value="${trnadd}" onclick="addNewData('phone')">    	
									   	</c:if>
									</div>																				                    
								</logic:iterate>
							</logic:notEmpty>
							<logic:empty name="aimAddContactForm" property="phones">
								<c:set var="trnadd"><digi:trn>Add New</digi:trn></c:set>
      							<input id="addPhoneBtn" style="font-family:verdana;font-size:11px;" type="button" name="addValBtn" value="${trnadd}" onclick="addNewData('phone')">
							</logic:empty>
						</td>
					</tr>
					<tr>
						<td align="right" valign="top"><strong><digi:trn>Fax</digi:trn></strong></td>
						<td align="left" nowrap="nowrap">
							<logic:notEmpty name="aimAddContactForm" property="faxes">
								 <logic:iterate name="aimAddContactForm" property="faxes" id="foo" indexId="ctr">
								 	<div>
								 		<html:text name="aimAddContactForm" property="faxes[${ctr}].value" size="30" styleId="fax_${ctr}"/>																												                    																												                    
								         <a href="javascript:removeData('fax',${ctr})"> 
									 		<img src= "/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" vspace="2" border="0"/>
									 	</a>
								        <c:if test="${ctr==aimAddContactForm.faxesSize-1}">
								           	<c:set var="trnadd"><digi:trn>Add New</digi:trn></c:set>
	      									<input id="addFaxBtn" style="font-family:verdana;font-size:11px;" type="button" name="addValBtn" value="${trnadd}" onclick="addNewData('fax')">
								        </c:if>
								 	</div>							         
							     </logic:iterate>
							</logic:notEmpty>
							<logic:empty name="aimAddContactForm" property="faxes">
								<c:set var="trnadd"><digi:trn>Add New</digi:trn></c:set>
      							<input id="addFaxBtn" style="font-family:verdana;font-size:11px;" type="button" name="addValBtn" value="${trnadd}" onclick="addNewData('fax')">
							</logic:empty>
						</td>
					</tr>
					<tr>
						<td align="right" valign="top"><strong><digi:trn>Office Address</digi:trn></strong></td>
                        <td align="left"><html:textarea property="officeaddress" cols="36" rows="3" styleId="officeaddress"/></td>
					</tr>	
					<tr height="5px"><td colspan="2"/></tr>
                    <tr>
                        <td colspan="2" align="center"><html:button property="" styleClass="dr-menu" onclick="saveContact()"><digi:trn>Save</digi:trn></html:button>
                            <c:if test="${aimAddContactForm.action=='edit'}">
                                <html:button styleClass="dr-menu" property="submitButton" onclick="myPanelContact.hide()">
                                    <digi:trn>Close</digi:trn>
                                </html:button>
                            </c:if>
                        </td>
                    </tr>
				</table>
			</td>
			<!-- create new contact td end -->
			<!-- filter start -->
			<c:if test="${aimAddContactForm.action=='add' || aimAddContactForm.action=='search'}">
				<td width="50%" height="100%" bordercolor="#f4f4f2">						
					<table width="100%" cellSpacing="5" cellPadding="5" vAlign="top" border="0" height="100%" class="box-border-nopadding" >
						<tr><td vAlign="top">
							<table bgcolor="#f4f4f2" cellPadding="5" cellSpacing="5" width="100%" class="box-border-nopadding">
								<tr>
									<td align="left" vAlign="top">
										<table bgcolor="#f4f4f2" cellPadding="0" cellSpacing="0" width="100%" class="box-border-nopadding" height="100px">
											<tr bgcolor="#006699">
												<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
													<digi:trn>Search Contacts</digi:trn>
												</td>
											</tr>
											<tr>
												<td align="center" bgcolor="#ECF3FD">
													<table cellSpacing="2" cellPadding="2">
														<tr>
															<td>
																<digi:trn>Enter a keyword </digi:trn>
															</td>
															<td>
																<html:text property="keyword"  styleClass="inp-text" styleId="keyword"/>
															</td>
														</tr>
														<tr>
															<td align="center" colspan=2>
																<html:button  styleClass="dr-menu" property="submitButton" onclick="return searchContact()">
																	<digi:trn>Search</digi:trn> 
																</html:button>
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>				
								<tr>
								<td align="left" vAlign="top" width="100%">
									<table bgcolor="#f4f4f2" cellPadding="0" cellSpacing="0" width="100%" height="100%" class="box-border-nopadding" border="0" vAlign="top">
										<tr bgcolor="#006699">
											<td vAlign="center" width="100%" align ="center" class="textalb" height="20" >
												<digi:trn>List of Contacts</digi:trn>
											</td>
				                         </tr>
				                         <tr>
				                            <td>
				                               <digi:errors/>
				                            </td>
				                         </tr>				                        
				                         <c:if test="${not empty aimAddContactForm.contacts}">
											<tr height="150px">
												<td rowspan="1" align="left" vAlign="top">
													<div style="overflow: scroll;height: 150px" >
														<table width="100%" cellPadding="3" cellspacing="0" border="0" style="overflow: scroll;height: 150px" >
														<c:forEach var="contact" items="${aimAddContactForm.contacts}">
															<tr>
																<td bgcolor="#ECF3FD" colspan="5" style="font:11px">
																	&nbsp;&nbsp;
																	<html:multibox property="selContactIds">
																		${contact.id}
																	</html:multibox>&nbsp;
																	${contact.name}&nbsp;${contact.lastname}&nbsp;-
																	<c:if test="${not empty contact.organisationName}">:&nbsp;${contact.organisationName}</c:if>
																	<c:forEach var="email" items="${contact.properties}">
																		<c:if test="${email.name=='contact email'}">
																			<a	href="mailto:${email.value}"><font color="black">${email.value}</font></a>; &nbsp;	
																		</c:if>
																	</c:forEach>
																</td>
															</tr>
														</c:forEach>														
													</table>
													</div>													
												</td>
											</tr>
										</c:if>				                        										
										<c:if test="${empty aimAddContactForm.contacts && aimAddContactForm.action=='search'}">
											<tr>
												<td>
													<br><br>&nbsp;&nbsp;&nbsp;
													<digi:trn>No records found, matching to your query......</digi:trn>
													<br><br>
												</td>
											</tr>
										</c:if>
									</table>
								</td>
							</tr>							
					</table>
					</td>
					</tr>
					<!-- buttons start -->
					<tr>
						<td align="center" colspan="3">
							<c:if test="${not empty aimAddContactForm.contacts}">
								<table cellPadding="5">
									<tr>
										<td>
											<html:button styleClass="dr-menu" property="addButton" onclick="addSelectedContacts()">
												<digi:trn>Add</digi:trn>
											</html:button>	
										</td>
										<td>
											<html:reset  styleClass="dr-menu" property="submitButton">
												<digi:trn>Clear</digi:trn>
											</html:reset>
										</td>
									</tr>
								</table>
							</c:if>				
						</td>
					</tr>
					<!-- buttons end -->
				</table>				
			</td>
			</c:if>			
			<!-- filter end -->
		</tr>
        <c:if test="${aimAddContactForm.action!='edit'}">
		<tr>
			<td colspan="2" align="center">
				<html:button styleClass="dr-menu" property="submitButton" onclick="myPanelContact.hide()">
						<digi:trn>Close</digi:trn>
				</html:button>
			</td>
		</tr>
        </c:if>
		<!-- page logic row end -->		
	</table>
</digi:form>
