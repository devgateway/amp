<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Transform appliedPatch conditions to custom condition -->
	<xsl:template match="condition[@type='appliedPatch']">
		<condition type="custom">
 		<script returnValue="p">
  			<lang type="hql">FROM AmpXmlPatch p WHERE p.patchId="<xsl:value-of select="."/>"</lang>
		 </script>
 		<test>p!=null</test>
		</condition>
	</xsl:template>		
		<!-- Transform dbname conditions to custom condition -->
		<xsl:template match="condition[@type='dbname']">
		<condition type="custom">
 			<script returnValue="db">
				<lang type="mysql">SELECT database()</lang>
				<lang type="oracle">SELECT sys_context('USERENV', 'CURRENT_SCHEMA') FROM dual</lang>
				<lang type="postgres">SELECT current_database()</lang>
			</script>
 			<test>db.equalsIgnoreCase("<xsl:value-of select="."/>")</test>
		</condition>
	</xsl:template>
	<!-- standard copy template -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>	
</xsl:stylesheet>