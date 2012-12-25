<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<root>
			
			
			<xsl:for-each select="report/row">
				<xsl:variable name="rowNum" select="position()"/>
				
				
				<xsl:if test="$rowNum > 2">
					<xsl:text disable-output-escaping="yes">&lt;</xsl:text>project description="<xsl:value-of select="column[3]/text()"/>"<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
						<element concept="FUNDS"><xsl:value-of select="column[1]/text()"/></element>
						<element concept="DIV"><xsl:value-of select="column[2]/text()"/></element>
						<element concept="ACT"><xsl:value-of select="column[3]/text()"/></element>
						<element concept="ITEM"><xsl:value-of select="column[4]/text()"/></element>
						<element concept="DIST"><xsl:value-of select="column[5]/text()"/></element>
						<budget fy="2012"><xsl:value-of select="column[6]/text()"/></budget>
						<budget fy="2013"><xsl:value-of select="column[7]/text()"/></budget>
						<budget fy="2014"><xsl:value-of select="column[8]/text()"/></budget>

					<xsl:text disable-output-escaping="yes">&lt;</xsl:text>/project<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
				</xsl:if>
				
			</xsl:for-each>
		</root>
	</xsl:template>
	
</xsl:stylesheet>
