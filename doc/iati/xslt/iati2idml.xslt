<?xml version="1.0"?>
<!-- 
   TODO

   - support reporting-org
   - support iati-identifier
-->
<!--
  IATI-to-IDML conversion script for UN-OCHA to Haiti AMP reporting.

  David Megginson, 2010-04-27

  This script will convert an XML document in IATI's draft format to
  version 2.0 of the International Development Markup Language (IDML)
  as currently used by Haiti's AMP (from the Development Gateway).
  Note that the output has *not* been tested, and may contain
  incorrect assumptions about how the AMP uses IDML.
  
  Please feel free to modify this script as required.  If you run into
  strange bugs after making modifications, try removing the
  xml:space="preserve" attribute from the root element: it results
  in nicely-formatted output for humans to read, but makes the
  XSLT very sensitive to whitespace changes.

  This script is meant only as a stopgap, until the Development
  Gateway AMP can import IATI data directly.
-->
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
        <xsl:value-of select="other-identifier"/>
      </xsl:attribute>
      <id>
        <xsl:attribute name="uniqID">
          <xsl:value-of select="other-identifier"/>
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
        <xsl:value-of select="description[@type='summary']"/>
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
      <xsl:if test="activity-date[@type='start']!=''">
        <proposedStartDate>
          <xsl:attribute name="date">
            <xsl:value-of select="activity-date[@type='start']/@iso-date"/>
          </xsl:attribute>
        </proposedStartDate>
      </xsl:if>
      <xsl:if test="activity-date[@type='completion']!=''">
        <closingDate>
          <xsl:attribute name="date">
            <xsl:value-of select="activity-date[@type='completion']/@iso-date"/>
          </xsl:attribute>
        </closingDate>
      </xsl:if>
      <!--Modified-->
      <status>
        <xsl:value-of select="activity-status/@ref"/>
      </status>
      <!--Modified, no itereation before only one sector was taken-->
      <xsl:for-each select="sector">
        <sectors>
          <xsl:attribute name="percentage">
           <xsl:value-of select="@percentage"/>
          </xsl:attribute>
          <xsl:attribute name="code">
            <xsl:value-of select="@ref"/>
          </xsl:attribute>
          <xsl:value-of select="."/>
        </sectors>
      </xsl:for-each>
  
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
      <xsl:value-of select="../aid-type"/>
    </assistanceType>
     <!--IATI schema Does Not Have This Field-->
    <financingInstrument>Direct Project Support</financingInstrument>
    <xsl:choose>
      <xsl:when test="@type='Planned Disbursement'">
        <disbursements>
            <!--Not Currency for this element I Guess will have to add it in IATI-->
            <xsl:attribute name="currency">
              <xsl:value-of select="../@default-currency"/>
            </xsl:attribute>
            <!--Transaction amount-->
            <xsl:attribute name="amount">
              <xsl:value-of select="value"/>
            </xsl:attribute>
            <!--Date to use in the rate calculations-->
            <xsl:attribute name="date">
              <xsl:value-of select="value/@value-date"/>
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
              <xsl:value-of select="value"/>
            </xsl:attribute>
            <!--Date to use in the rate calculations-->
            <xsl:attribute name="date">
              <xsl:value-of select="value/@value-date"/>
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
  <funding>
    <!--There is not code for funding in the IATI schema-->
    <xsl:attribute name="code">
      <xsl:value-of select="@ref"/>
    </xsl:attribute>
    <fundingOrg>
      <xsl:attribute name="code">
        <xsl:value-of select="provider-org/@ref"/>
      </xsl:attribute>
      <xsl:value-of select="provider-org"/>
    </fundingOrg>
    <assistanceType>
      <xsl:if test="count(aid-type)!=0">     
        <xsl:value-of select="aid-type" />
      </xsl:if>
      <xsl:if test="count(aid-type)=0">   
        <xsl:value-of select="../aid-type"/>
      </xsl:if>
    </assistanceType>
    <!--IATI schema Does Not Have This Field-->
    <financingInstrument>Direct Project Support</financingInstrument>
    <xsl:choose>
      <xsl:when test="@type='disbursment'">
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
              <xsl:value-of select="value/@value-date"/>
            </xsl:attribute>
            <!--Allways Actual Here -->
            <xsl:attribute name="type">
              <xsl:text>Actual</xsl:text>   
            </xsl:attribute>
         </disbursements>
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
              <xsl:value-of select="value/@value-date"/>
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
      <xsl:when test="@role='donor'">
        <xsl:attribute name="type">
          <xsl:text>Responsible Organization</xsl:text>
        </xsl:attribute> 
      </xsl:when>
      <xsl:when test="@role='beneficiary'">
        <xsl:attribute name="type">
          <xsl:text>Beneficiary Agency</xsl:text>
        </xsl:attribute> 
      </xsl:when>
      <xsl:when test="@role='implementing'">
        <xsl:attribute name="type">
          <xsl:text>Implementing Agency</xsl:text>
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
      
  <!--No chahnges here-->
    <xsl:apply-templates select="incoming-contribution[status='Commitment' or status='Paid contribution']"/>
      <additional field="ocha-activities" type="String">
        <xsl:value-of select="description[@type='activities']"/>
      </additional>
      <additional field="ocha-outputs" type="String">
        <xsl:value-of select="description[@type='outputs']"/>
      </additional>
      <additional field="ocha-appeal-id" type="String">
        <xsl:value-of select="related-initiative[@type='un-ocha-appeal']/@ref"/>
      </additional>
      <additional field="ocha-appeal-name" type="String">
        <xsl:value-of select="related-initiative[@type='un-ocha-appeal']"/>
      </additional>
      <additional field="ocha-cluster-code" type="String">
        <xsl:value-of select="sector[@type='cluster']/@ref"/>
      </additional>
      <additional field="ocha-cluster-name" type="String">
        <xsl:value-of select="sector[@type='cluster']"/>
      </additional>
      <additional field="ocha-total-cost" type="Number">
        <xsl:value-of select="total-cost"/>
      </additional>
      <xsl:if test="aid-type">
        <additional field="ocha-aid-type" type="String">
          <xsl:value-of select="aid-type"/>
        </additional>
      </xsl:if>
      <additional field="ocha-total-cost-currency" type="String">
        <xsl:value-of select="total-cost/@currency"/>
      </additional>
  
  </activity>
  </xsl:template>
</xsl:stylesheet>
