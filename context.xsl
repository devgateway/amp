<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" version="1.0" indent="yes" />
	<xsl:param name="mysql.user"/>
	<xsl:param name="mysql.password"/>
	<xsl:param name="serverName"/>
	<xsl:param name="mysql.db"/>
	<xsl:param name="mysql.port"/>

	<xsl:template match="@url">
		<xsl:attribute name="url">jdbc:mysql://localhost:<xsl:value-of select="$mysql.port"/>/<xsl:value-of select="$mysql.db"/>?autoReconnect=true&amp;useUnicode=true&amp;characterEncoding=UTF-8&amp;jdbcCompliantTruncation=false</xsl:attribute>
	</xsl:template>
	
	<xsl:template match="@username">
		<xsl:attribute name="username"><xsl:value-of select="$mysql.user"/></xsl:attribute>
	</xsl:template>
	
	<xsl:template match="@password">
		<xsl:attribute name="password"><xsl:value-of select="$mysql.password"/></xsl:attribute>
	</xsl:template>
	
	<xsl:template match="@prefix">
		<xsl:attribute name="prefix"><xsl:value-of select="$serverName"/></xsl:attribute>
	</xsl:template>
	

	<!-- Standard copy template. Copy the rest of the nodes, unchanged -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*" />
			<xsl:apply-templates />
		</xsl:copy>
	</xsl:template>


</xsl:stylesheet>