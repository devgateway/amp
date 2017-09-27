<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<iati-activities>
			<xsl:attribute name="version">
				<xsl:text>1.0</xsl:text>
			</xsl:attribute>
			<xsl:for-each select="activities/activity">
				<iati-activity>
					<xsl:attribute name="last-updated-datetime">
						<xsl:value-of select="activity/@date" />
					</xsl:attribute>
					<!-- Default currency attribute here-->
					<reporting-org>
						<!--
							Value of the reporting organization not clear how to take it from IDML
						-->
					</reporting-org>
					<other-identifier>
						<xsl:value-of select="activity/@dbKey" />
					</other-identifier>
					<title>
						<xsl:value-of select="title" />
					</title>
					<xsl:if test="count(objectives)!=0">
						<description>
							<xsl:attribute name="type">
								<xsl:text>objectives</xsl:text>
							</xsl:attribute>
							<xsl:value-of select="objective" />
						</description>
					</xsl:if>
					<xsl:if test="count(description)!=0">
						<description>
							<xsl:attribute name="type">
								<xsl:text>summary</xsl:text>
							</xsl:attribute>
							<xsl:value-of select="description" />
						</description>
					</xsl:if>
					<recipient-country>
						<xsl:attribute name="code">
							<xsl:value-of select="/location/locationName/@code" />
						</xsl:attribute>
						<xsl:attribute name="ref">
							<xsl:value-of select="location/@iso3" />
						</xsl:attribute>
						<xsl:value-of select="location/@countryName" />
					</recipient-country>
			
					<!--participating organizations -->
					<xsl:for-each select="relatedOrgs">
						<participating-org>
							<xsl:choose>
								<xsl:when test="@type='Responsible Organization'">
									<xsl:attribute name="role">
										 <xsl:text>donor</xsl:text>
									</xsl:attribute>
								</xsl:when>
								<xsl:when test="@type='Implementing Agency'">
									<xsl:attribute name="role">
										 <xsl:text>implementing</xsl:text>
									</xsl:attribute>
								</xsl:when>
								<xsl:when test="@type='Beneficiary Agency'">
									<xsl:attribute name="role">
										 <xsl:text>beneficiary</xsl:text>
									</xsl:attribute>
								</xsl:when>
							</xsl:choose>
							<xsl:attribute name="ref">
      						<xsl:value-of select="@code" />  
    					</xsl:attribute>
							<xsl:value-of select="." />
						</participating-org>
					</xsl:for-each>
					
					<!--activity-status -->
					<activity-status>
						<xsl:value-of select="status"/>
					</activity-status>
					
					<!-- Sector -->
					<xsl:for-each select="sectors">
						<sectors>
							<xsl:attribute name="percentage">
								<xsl:value-of select="@percentage" />
							</xsl:attribute>
							<xsl:attribute name="ref">
            					<xsl:value-of select="@code" />
          					</xsl:attribute>
							<xsl:value-of select="." />
						</sectors>
					</xsl:for-each>
					<!-- End Sectors -->
					<!-- recipient-region-->
					<!-- End recipient-region-->
					
					<!-- Funding Section -->
					<xsl:for-each select="funding">
						<xsl:choose>
      						<xsl:when test="commitments/@type='Planned'">
      							<budget-planned>
      								<xsl:attribute name="type">
      									<xsl:text>original-budget</xsl:text> 
      								</xsl:attribute>
      								<xsl:attribute name="period">
      									<xsl:text>annual</xsl:text> 
      								</xsl:attribute>
      							<value>
      								<xsl:attribute name="value-date">
      									<xsl:value-of select="date"/>
      								</xsl:attribute>
      								<xsl:value-of select="amount"/>
      							</value>
      							</budget-planned>
      						</xsl:when>
      						<xsl:when test="disbursements/@type='Planned'">
      							<budget-planned>
      								<xsl:attribute name="type">
      									<xsl:text>Planned Disbursement</xsl:text> 
      								</xsl:attribute>
      								<xsl:attribute name="period">
      									<xsl:text>annual</xsl:text> 
      								</xsl:attribute>
      							<value>
      								<xsl:attribute name="value-date">
      									<xsl:value-of select="date"/>
      								</xsl:attribute>
      								<xsl:value-of select="amount"/>
      							</value>
      							</budget-planned>
      						</xsl:when>
      						<xsl:when test="commitments/@type='Actual'">
      							<transaction>
      								<xsl:attribute name="flow">
      									 <xsl:text>incoming</xsl:text>
      								</xsl:attribute>
      								<xsl:attribute name="type">
      									 <xsl:text>commitment</xsl:text>
      								</xsl:attribute>
      								<value>
      									<xsl:attribute name="currency">
      										<xsl:value-of select="commitments/@currency"/>
      									</xsl:attribute>
      									<xsl:attribute name="value-date">
      										<xsl:value-of select="commitments/@date"/>
      									</xsl:attribute>
      									<xsl:value-of select="amount"/>
      								</value>
      								<provider-org>
      									<xsl:attribute name="provider-activity-id"/>
      									<xsl:attribute name="ref">
      										<xsl:value-of select="fundingOrg/@code"/>
      									</xsl:attribute>
      									<xsl:value-of select="fundingOrg"/>
      								</provider-org>
      								<aid-type>
      									<xsl:value-of select="assistanceType"/>
      								</aid-type>
      							</transaction> 
      						</xsl:when>
      						<xsl:when test="disbursements/@type='Actual'">
      						<transaction>
      								<xsl:attribute name="flow">
      									 <xsl:text>incoming</xsl:text>
      								</xsl:attribute>
      								<xsl:attribute name="type">
      									 <xsl:text>disbursement</xsl:text>
      								</xsl:attribute>
      								<value>
      									<xsl:attribute name="currency">
      										<xsl:value-of select="commitments/@currency"/>
      									</xsl:attribute>
      									<xsl:attribute name="value-date">
      										<xsl:value-of select="commitments/@date"/>
      									</xsl:attribute>
      									<xsl:value-of select="amount"/>
      								</value>
      								<provider-org>
      									<xsl:attribute name="provider-activity-id"/>
      									<xsl:attribute name="ref">
      										<xsl:value-of select="fundingOrg/@code"/>
      									</xsl:attribute>
      									<xsl:value-of select="fundingOrg"/>
      								</provider-org>
      								<aid-type>
      									<xsl:value-of select="assistanceType"/>
      								</aid-type>
      							</transaction> 
      						</xsl:when>
      					</xsl:choose>
					</xsl:for-each>
					
				</iati-activity>
			</xsl:for-each>
		</iati-activities>
	</xsl:template>
</xsl:stylesheet>