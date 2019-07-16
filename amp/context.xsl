<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" version="1.0" indent="yes" />
	<xsl:param name="jdbc.user"/>
	<xsl:param name="jdbc.password"/>
	<xsl:param name="serverName"/>
	<xsl:param name="jdbc.db"/>
	<xsl:param name="jdbc.host"/>
	<xsl:param name="jdbc.port"/>
	<xsl:param name="dbName"/>
	<xsl:param name="jdbc.driverClassName"/>
	
	<xsl:param name="antilock"/>

	<xsl:template match="Environment[@name='hostname']">
	 <xsl:copy>
		<xsl:apply-templates select="@*|node()"/>
		<xsl:attribute name="value"><xsl:value-of select="$serverName"/></xsl:attribute>
	</xsl:copy>
	</xsl:template>


	<xsl:template match="@url">
		<xsl:attribute name="url">jdbc:<xsl:value-of select="$dbName"/>://<xsl:value-of select="$jdbc.host"/>:<xsl:value-of select="$jdbc.port"/>/<xsl:value-of select="$jdbc.db"/>?useUnicode=true&amp;characterEncoding=UTF-8&amp;jdbcCompliantTruncation=false</xsl:attribute>
	</xsl:template>
	
	<xsl:template match="@username">
		<xsl:attribute name="username"><xsl:value-of select="$jdbc.user"/></xsl:attribute>
	</xsl:template>
	
	<xsl:template match="@password">
		<xsl:attribute name="password"><xsl:value-of select="$jdbc.password"/></xsl:attribute>
	</xsl:template>

	<xsl:template match="@prefix">
		<xsl:attribute name="prefix"><xsl:value-of select="$serverName"/></xsl:attribute>
	</xsl:template>
	
	<xsl:template match="@antiJARLocking">
		<xsl:attribute name="antiJARLocking"><xsl:value-of select="$antilock"/></xsl:attribute>
	</xsl:template>
	
	<xsl:template match="@antiResourceLocking">
		<xsl:attribute name="antiResourceLocking"><xsl:value-of select="$antilock"/></xsl:attribute>
	</xsl:template>
	

<!-- <xsl:template match="@driverClassName">
		<xsl:attribute name="driverClassName"><xsl:value-of select="$jdbc.driverClassName"/></xsl:attribute>
	</xsl:template>
-->	

	<!-- Standard copy template. Copy the rest of the nodes, unchanged -->
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@*" />
			<xsl:apply-templates />
		</xsl:copy>
	</xsl:template>


</xsl:stylesheet>