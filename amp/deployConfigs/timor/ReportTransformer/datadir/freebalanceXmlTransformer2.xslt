<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<root>
			<xsl:variable name="rowCount" select="count(report/row)"/>
			<xsl:for-each select="report/row">
				<xsl:variable name="rowNum" select="position()"/>
				<xsl:variable name="prevRowNum" select="position() - 1"/>
				<xsl:if test="$rowNum = 2">
					<legend>
						<xsl:for-each select="column">
							<col>
								<xsl:value-of select="text()"/>
							</col>
						</xsl:for-each>
					</legend>
				</xsl:if>
				<xsl:if test="$rowNum > 2">
					
					<xsl:variable name="prevDonor">
						<xsl:value-of select="/report/row[$prevRowNum]/column[1]/text()"/>
					</xsl:variable>
					<xsl:variable name="prevBeneficiary">
						<xsl:value-of select="/report/row[$prevRowNum]/column[2]/text()"/>
					</xsl:variable>
					<xsl:variable name="prevProject">
						<xsl:value-of select="/report/row[$prevRowNum]/column[3]/text()"/>
					</xsl:variable>
					<xsl:variable name="prevDistrict">
						<xsl:value-of select="/report/row[$prevRowNum]/column[4]/text()"/>
					</xsl:variable>
					<xsl:variable name="prevCapitalRecurent">
						<xsl:value-of select="/report/row[$prevRowNum]/column[5]/text()"/>
					</xsl:variable>

					<xsl:variable name="donor">
						<xsl:value-of select="/report/row[$rowNum]/column[1]/text()"/>
					</xsl:variable>
					<xsl:variable name="beneficiary">
						<xsl:value-of select="/report/row[$rowNum]/column[2]/text()"/>
					</xsl:variable>
					<xsl:variable name="project">
						<xsl:value-of select="/report/row[$rowNum]/column[3]/text()"/>
					</xsl:variable>
					<xsl:variable name="district">
						<xsl:value-of select="/report/row[$rowNum]/column[4]/text()"/>
					</xsl:variable>
					<xsl:variable name="capitalRecurent">
						<xsl:value-of select="/report/row[$rowNum]/column[5]/text()"/>
					</xsl:variable>


					<xsl:if test="$rowNum = 3">
						<xsl:text disable-output-escaping="yes">&lt;</xsl:text>data<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
					</xsl:if>
					<!--
						<xsl:for-each select="column">
							<col>
								<xsl:value-of select="text()"/>
							</col>
						</xsl:for-each>
					-->
					<xsl:if test="not($donor = $prevDonor)">
						<xsl:if test="$rowNum > 3">
							<xsl:text disable-output-escaping="yes">&lt;</xsl:text>/location<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
							<xsl:text disable-output-escaping="yes">&lt;</xsl:text>/programme<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
							<xsl:text disable-output-escaping="yes">&lt;</xsl:text>/organization<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
							<xsl:text disable-output-escaping="yes">&lt;</xsl:text>/fund<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
						</xsl:if>
						
						<xsl:text disable-output-escaping="yes">&lt;</xsl:text>fund nameOrId="<xsl:value-of select="$donor"/>"<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
					</xsl:if>
					<xsl:if test="not($beneficiary = $prevBeneficiary) or ($beneficiary = $prevBeneficiary and not($donor = $prevDonor))">
						<xsl:if test="$rowNum > 3 and $donor = $prevDonor">
							<xsl:text disable-output-escaping="yes">&lt;</xsl:text>/location<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
							<xsl:text disable-output-escaping="yes">&lt;</xsl:text>/programme<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
							<xsl:text disable-output-escaping="yes">&lt;</xsl:text>/organization<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
						</xsl:if>
						<xsl:text disable-output-escaping="yes">&lt;</xsl:text>organization nameOrId="<xsl:value-of select="$beneficiary"/>"<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
					</xsl:if>
					<xsl:if test="not($project = $prevProject) or ($project = $prevProject and not($beneficiary = $prevBeneficiary)) or ($project = $prevProject and not($donor = $prevDonor))">
						<xsl:if test="$rowNum > 3 and $beneficiary = $prevBeneficiary and $donor = $prevDonor">
							<xsl:text disable-output-escaping="yes">&lt;</xsl:text>/location<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
							<xsl:text disable-output-escaping="yes">&lt;</xsl:text>/programme<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
						</xsl:if>
						<xsl:text disable-output-escaping="yes">&lt;</xsl:text>programme nameOrId="<xsl:value-of select="$project"/>"<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
					</xsl:if>
					
					<xsl:if test="not($district = $prevDistrict) or ($district = $prevDistrict and not($project = $prevProject)) or ($district = $prevDistrict and not($beneficiary = $prevBeneficiary)) or ($district = $prevDistrict and not($donor = $prevDonor))">
						<xsl:if test="$rowNum > 3 and $project = $prevProject and $beneficiary = $prevBeneficiary and $donor = $prevDonor">
							<xsl:text disable-output-escaping="yes">&lt;</xsl:text>/location<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
						</xsl:if>
						<xsl:text disable-output-escaping="yes">&lt;</xsl:text>location nameOrId="<xsl:value-of select="$district"/>"<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
					</xsl:if>
					
					<!--
					<xsl:if test="not($capitalRecurent = $prevCapitalRecurent)">
						<xsl:if test="$rowNum > 3 and $district = $prevDistrict and $project = $prevProject and $beneficiary = $prevBeneficiary and $donor = $prevDonor">
							<xsl:text disable-output-escaping="yes">&lt;</xsl:text>/capitalRecurent<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
						</xsl:if>
						<xsl:text disable-output-escaping="yes">&lt;</xsl:text>capitalRecurent nameOrId="<xsl:value-of select="$capitalRecurent"/>"<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
					</xsl:if>
					-->


					<xsl:text disable-output-escaping="yes">&lt;</xsl:text>capitalRecurent nameOrId="<xsl:value-of select="$capitalRecurent"/>"<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
						<item><xsl:value-of select="column[6]/text()"/></item>
						<item><xsl:value-of select="column[7]/text()"/></item>
						<item><xsl:value-of select="column[8]/text()"/></item>
					<xsl:text disable-output-escaping="yes">&lt;</xsl:text>/capitalRecurent <xsl:text disable-output-escaping="yes">&gt;</xsl:text>
					
					<xsl:if test="$rowNum = $rowCount">
						<xsl:text disable-output-escaping="yes">&lt;</xsl:text>/location<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
						<xsl:text disable-output-escaping="yes">&lt;</xsl:text>/programme<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
						<xsl:text disable-output-escaping="yes">&lt;</xsl:text>/organization<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
						<xsl:text disable-output-escaping="yes">&lt;</xsl:text>/fund<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
						<xsl:text disable-output-escaping="yes">&lt;</xsl:text>/data<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
					</xsl:if>
				</xsl:if>
			</xsl:for-each>
		</root>
	</xsl:template>
</xsl:stylesheet>
