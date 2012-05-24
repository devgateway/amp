<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <!-- iterate through the IATI projects, ignoring anything else -->
  <xsl:template match="/iati-activities">
    <activities>
      <xsl:apply-templates select="iati-activity" mode="internal"/>
    </activities>
  </xsl:template>

  <xsl:template match="/iati-activity">
    <activities>
      <xsl:apply-templates select="/iati-activity" mode="internal"/>
    </activities>
  </xsl:template>

  <!-- transform IATI project IDML activity -->
  <xsl:template match="iati-activity" mode="internal">
    <activity>
      <xsl:attribute name="dbKey">
        <xsl:value-of select="iati-identifier"/>
      </xsl:attribute>
      <id>
        <xsl:attribute name="uniqID">
          <xsl:value-of select="iati-identifier"/>
        </xsl:attribute>
        <assigningOrg>
        <!--owner-ref is used for the organization code-->
          <xsl:attribute name="code">
            <xsl:value-of select="other-identifier/@owner-ref"/>
          </xsl:attribute>
            <xsl:value-of select="other-identifier/@owner-name"/>
        </assigningOrg>
      </id>
      <title>
        <xsl:value-of select="title"/>
      </title>
      <objective>
        <xsl:value-of select="description[@type='objectives']"/>
      </objective>
      <description>
        <xsl:value-of select="description"/>
      </description>
      
      <location locationType="Country">
        <xsl:attribute name="iso3">
          <xsl:value-of select="recipient-country/@ref"/>
        </xsl:attribute>
        <xsl:attribute name="countryName">
          <xsl:value-of select="recipient-country"/>
        </xsl:attribute>
        <locationName>
        <!--I will use code attibute for the country code it have to be changed-->
          <xsl:attribute name="code">
            <xsl:value-of select="recipient-country/@code"/>
          </xsl:attribute>
          <xsl:value-of select="recipient-country"/>
        </locationName>
      </location>
      
      <!-- Bill asked me to remove this section for now until they do the final 
         version which will include all locations levels, i'm leaving only country 
        section beacuse we need a location in order to import the activity
        
      <location locationType="Region">
        <xsl:attribute name="iso3">
          <xsl:value-of select="recipient-country/@ref"/>
        </xsl:attribute>
        <xsl:attribute name="countryName">
          <xsl:value-of select="recipient-country"/>
        </xsl:attribute>
        <xsl:attribute name="percentage">
          <xsl:value-of select="percentage"/>
        </xsl:attribute>
        <locationName>
          <xsl:attribute name="code">
            <xsl:value-of select="target-location/@vocabulary"/>
          </xsl:attribute>
          <xsl:value-of select="target-location[@type='un-ocha-province']"/>
        </locationName>
      </location>
      -->
      <!--I had to add a check here because the importer give and error-->
      <xsl:if test="activity-date[@type='start-planned']!=''">
        <proposedStartDate>
          <xsl:attribute name="date">
            <xsl:value-of select="activity-date[@type='start-planned']"/>
          </xsl:attribute>
        </proposedStartDate>
      </xsl:if>
      <xsl:if test="activity-date[@type='start-actual']!=''">
        <actualStartDate>
          <xsl:attribute name="date">
            <xsl:value-of select="activity-date[@type='start-actual']"/>
          </xsl:attribute>
        </actualStartDate>
      </xsl:if>
      <xsl:if test="activity-date[@type='end-actual']!=''">
        <closingDate>
          <xsl:attribute name="date">
            <xsl:value-of select="activity-date[@type='end-actual']"/>
          </xsl:attribute>
        </closingDate>
      </xsl:if>
      <!--Modified-->
      <status>
	      <xsl:if test="count(activity-status/@code)!=0">     
		<xsl:value-of select="activity-status" />
	      </xsl:if>
	      <xsl:if test="count(activity-status/@code)=0">   
		<xsl:text>à spécifier</xsl:text>
	      </xsl:if>
      </status>
      <!--Modified, no iteration before only one sector was taken-->
      <xsl:for-each select="sector">
        <sectors>
          <xsl:attribute name="percentage">
	   <xsl:if test="count(@percentage)=0">
	           <xsl:text>100</xsl:text>
	   </xsl:if>
	   <xsl:if test="count(@percentage)!=0">
		<xsl:value-of select="@percentage"/>
	   </xsl:if>
          </xsl:attribute>
          <xsl:attribute name="code">
            <xsl:value-of select="@code"/>
          </xsl:attribute>
          <xsl:value-of select="."/>
        </sectors>
      </xsl:for-each>
      <xsl:if test="count(sector)=0">   
	  <sectors>
	  <xsl:attribute name="percentage">
	   <xsl:text>100</xsl:text>
	  </xsl:attribute>
	  <xsl:attribute name="code">
	   <xsl:text>998</xsl:text>
	  </xsl:attribute>
	   <xsl:text>UNALLOCATED/ UNSPECIFIED</xsl:text>
        </sectors>
      </xsl:if>
  
  <!--Funding Section, everything was changed here-->    
  
  
  <!--Planed Commitment and  Disbursement
        Types : 
              original budged = Planned Commitment 
              updated budged = = Planned Commitment 
              Planned Disbursement =  Planned Disbursement
        Value :
                funding amount and date
   -->
  <xsl:for-each select="budget-planned">
  <funding>
    <!--There is not code for funding in the IATI schema-->
    <xsl:attribute name="code">
      <xsl:value-of select="@ref"/>
    </xsl:attribute>
    <!--Budged element has not Provider-Org 
      this might need changing but this is the way it is at the moment-->
    <fundingOrg>
      <xsl:attribute name="code">
        <xsl:value-of select="../participating-org[@role='donor']/@ref"/>
      </xsl:attribute>
      <xsl:value-of select="../participating-org[@role='donor']"/>
    </fundingOrg>
    <assistanceType>
      <xsl:if test="count(aid-type)!=0">     
        <xsl:value-of select="aid-type" />
      </xsl:if>
      <xsl:if test="count(aid-type)=0">   
	      <xsl:if test="count(../aid-type)!=0">     
		<xsl:value-of select="../aid-type" />
	      </xsl:if>
	      <xsl:if test="count(../aid-type)=0">   
		<xsl:text>Grant</xsl:text>
	      </xsl:if>
      </xsl:if>
    </assistanceType>
     <!--IATI schema Does Not Have This Field-->
    <financingInstrument>Appui projet/programme</financingInstrument>
    <xsl:choose>
      <xsl:when test="@type='Planned Disbursement'">
        <disbursements>
            <!--Not Currency for this element I Guess will have to add it in IATI-->
            <xsl:attribute name="currency">
              <xsl:value-of select="../@default-currency"/>
            </xsl:attribute>
            <!--Transaction amount-->
            <xsl:attribute name="amount">
		      <xsl:if test="count(value)!=0">     
			<xsl:value-of select="value" />
		      </xsl:if>
		      <xsl:if test="count(value)=0">   
			<xsl:text>0</xsl:text>
		      </xsl:if>
            </xsl:attribute>
            <!--Date to use in the rate calculations-->
            <xsl:attribute name="date">
	    	      <xsl:if test="count(value/@value-date)!=0">     
			<xsl:value-of select="value/@value-date" />
		      </xsl:if>
		      <xsl:if test="count(value/@value-date)=0">   
			<xsl:value-of select="period-start" />
		      </xsl:if>
            </xsl:attribute>
            <!--Allways Planed Here -->
            <xsl:attribute name="type">
              <xsl:text>Planned</xsl:text>   
            </xsl:attribute>
        </disbursements>
      </xsl:when>
    <xsl:otherwise>
      <commitments>
      <!--Not Currency for this element I Guess will have to add it in IATI-->
            <xsl:attribute name="currency">
              <xsl:value-of select="../@default-currency"/>
            </xsl:attribute>
            <!--Transaction amount-->
            <xsl:attribute name="amount">
		      <xsl:if test="count(value)!=0">     
			<xsl:value-of select="value" />
		      </xsl:if>
		      <xsl:if test="count(value)=0">   
			<xsl:text>0</xsl:text>
		      </xsl:if>
            </xsl:attribute>
            <!--Date to use in the rate calculations-->
            <xsl:attribute name="date">
	    	      <xsl:if test="count(value/@value-date)!=0">     
			<xsl:value-of select="value/@value-date" />
		      </xsl:if>
		      <xsl:if test="count(value/@value-date)=0">   
			<xsl:value-of select="period-start" />
		      </xsl:if>
            </xsl:attribute>
            <!--Allways Planed Here -->
            <xsl:attribute name="type">
              <xsl:text>Planned</xsl:text>   
            </xsl:attribute>
      </commitments>
      </xsl:otherwise>
    </xsl:choose>
  </funding>
  </xsl:for-each> 
  <!--End Planned -->
  
  <!--Actual Commitment and  Disbursement-->
  <xsl:for-each select="transaction">
  <funding code="">
    <fundingOrg>
      <xsl:if test="count(provider-org)!=0">     
	      <xsl:attribute name="code">
		<xsl:value-of select="provider-org/@ref"/>
	      </xsl:attribute>
	      <xsl:value-of select="provider-org"/>
      </xsl:if>
      <xsl:if test="count(provider-org)=0">   
	      <xsl:attribute name="code">
		<xsl:value-of select="../participating-org[@role='Funding']/@ref"/>
	      </xsl:attribute>
	      <xsl:value-of select="../participating-org[@role='Funding']"/>
      </xsl:if>

    </fundingOrg>
    <assistanceType>
      <xsl:if test="count(finance-type)!=0">     
        <xsl:value-of select="finance-type" />
      </xsl:if>
      <xsl:if test="count(aid-type)=0">   
	      <xsl:if test="count(../default-finance-type)!=0">     
		<xsl:value-of select="../default-finance-type" />
	      </xsl:if>
	      <xsl:if test="count(../default-finance-type)=0">   
		<xsl:text>Grant</xsl:text>
	      </xsl:if>
      </xsl:if>
    </assistanceType>

    <financingInstrument>
      <xsl:if test="count(aid-type)!=0">     
        <xsl:value-of select="aid-type" />
      </xsl:if>
      <xsl:if test="count(aid-type)=0">   
	      <xsl:if test="count(../default-aid-type)!=0">     
		<xsl:value-of select="../default-aid-type" />
	      </xsl:if>
	      <xsl:if test="count(../default-aid-type)=0">   
		<xsl:text>Grant</xsl:text>
	      </xsl:if>
      </xsl:if>
   </financingInstrument>

    <xsl:choose>
      <xsl:when test="transaction-type/@code='D'">
         <disbursements>
            <!--If currency code is empty then take the default currency-->
            <!--I am not sure if a transacation element could contain more than 1 value element-->
            <xsl:attribute name="currency">
              <xsl:choose>
                <xsl:when test="value/@currency!=''">
                  <xsl:value-of select="value/@currency"/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="../@default-currency"/>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:attribute>
            <!--Transaction amount-->
            <xsl:attribute name="amount">
              <xsl:value-of select="value"/>
            </xsl:attribute>
            <!--Date to use in the rate calculations-->
            <xsl:attribute name="date">
	    	      <xsl:if test="count(value/@value-date)!=0">     
			<xsl:value-of select="value/@value-date" />
		      </xsl:if>
		      <xsl:if test="count(value/@value-date)=0">   
		    	      <xsl:if test="count(../activity-date[@type='start']/@iso-date)!=0">     
				<xsl:value-of select="../activity-date[@type='start']/@iso-date" />
			      </xsl:if>
			      <xsl:if test="count(../activity-date[@type='start']/@iso-date)=0">   
				<xsl:value-of select="../budget-planned[@type='annual']/period-start" />
			      </xsl:if>
		      </xsl:if>
            </xsl:attribute>
            <!--Allways Actual Here -->
            <xsl:attribute name="type">
              <xsl:text>Actual</xsl:text>   
            </xsl:attribute>
         </disbursements>
      </xsl:when>
      <xsl:when test="transaction-type/@code='E'">
         <expenditures>
            <!--If currency code is empty then take the default currency-->
            <!--I am not sure if a transacation element could contain more than 1 value element-->
            <xsl:attribute name="currency">
              <xsl:choose>
                <xsl:when test="value/@currency!=''">
                  <xsl:value-of select="value/@currency"/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="../@default-currency"/>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:attribute>
            <!--Transaction amount-->
            <xsl:attribute name="amount">
              <xsl:value-of select="value"/>
            </xsl:attribute>
            <!--Date to use in the rate calculations-->
            <xsl:attribute name="date">
	    	      <xsl:if test="count(value/@value-date)!=0">     
			<xsl:value-of select="value/@value-date" />
		      </xsl:if>
		      <xsl:if test="count(value/@value-date)=0">   
		    	      <xsl:if test="count(../activity-date[@type='start']/@iso-date)!=0">     
				<xsl:value-of select="../activity-date[@type='start']/@iso-date" />
			      </xsl:if>
			      <xsl:if test="count(../activity-date[@type='start']/@iso-date)=0">   
				<xsl:value-of select="../budget-planned[@type='annual']/period-start" />
			      </xsl:if>
		      </xsl:if>
            </xsl:attribute>
            <!--Allways Actual Here -->
            <xsl:attribute name="type">
              <xsl:text>Actual</xsl:text>   
            </xsl:attribute>
         </expenditures>
      </xsl:when>
      <xsl:otherwise>
        <commitments>
        <!--If currency code is empty then take the default currency-->
          <xsl:attribute name="currency">
            <xsl:choose>
              <xsl:when test="value/@currency!=''">
                <xsl:value-of select="value/@currency"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="../@default-currency"/>
              </xsl:otherwise>
            </xsl:choose>
            </xsl:attribute>
            <!--Transaction amount-->
            <xsl:attribute name="amount">
              <xsl:value-of select="value"/>
            </xsl:attribute>
            <!--Date to use in the rate calculations-->
            <xsl:attribute name="date">
	    	      <xsl:if test="count(value/@value-date)!=0">     
			<xsl:value-of select="value/@value-date" />
		      </xsl:if>
		      <xsl:if test="count(value/@value-date)=0">   
		    	      <xsl:if test="count(../activity-date[@type='start']/@iso-date)!=0">     
				<xsl:value-of select="../activity-date[@type='start']/@iso-date" />
			      </xsl:if>
			      <xsl:if test="count(../activity-date[@type='start']/@iso-date)=0">   
				<xsl:value-of select="../budget-planned[@type='annual']/period-start" />
			      </xsl:if>
		      </xsl:if>
            </xsl:attribute>
            <!--Allways Actual Here -->
            <xsl:attribute name="type">
              <xsl:text>Actual</xsl:text>   
            </xsl:attribute>
         </commitments>
      </xsl:otherwise>
    </xsl:choose>
  </funding>
  </xsl:for-each>
  <!--End Actual -->
  <!--Funding Section-->
  
  <!--Iteration added here for related Orgs.-->
  <xsl:for-each select="participating-org"> 
    <relatedOrgs>
    <xsl:choose>
      <xsl:when test="@role='Extending'">
        <xsl:attribute name="type">
          <xsl:text>Executing Agency</xsl:text>
        </xsl:attribute> 
      </xsl:when>
      <xsl:when test="@role='Beneficiary'">
        <xsl:attribute name="type">
          <xsl:text>Beneficiary Agency</xsl:text>
        </xsl:attribute> 
      </xsl:when>
      <xsl:when test="@role='Implementing'">
        <xsl:attribute name="type">
          <xsl:text>Implementing Agency</xsl:text>
        </xsl:attribute> 
      </xsl:when>
      <xsl:when test="@role='Accountable'">
        <xsl:attribute name="type">
          <xsl:text>Responsible Organization</xsl:text>
        </xsl:attribute> 
      </xsl:when>
      <xsl:when test="@role='Funding'">
        <xsl:attribute name="type">
          <xsl:text>Funding Agency</xsl:text>
        </xsl:attribute> 
      </xsl:when>
    </xsl:choose>
    <xsl:attribute name="code">
      <!--We have to figure out where this value is in IATI-->
      <xsl:value-of select="@ref"/>  
    </xsl:attribute> 
    <xsl:value-of select="."/>
  </relatedOrgs>
  </xsl:for-each>    

   <!--Contact inforamtion-->
  	<xsl:for-each select="contact-info">
	<donorContacts>
	 	<firstName><xsl:value-of select="person-name" /></firstName>
		<lastName/>
		<email><xsl:value-of select="email" /></email>
	</donorContacts>
	</xsl:for-each>    
  </activity>
  </xsl:template>
</xsl:stylesheet>