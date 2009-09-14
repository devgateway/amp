<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="condition[@type='appliedPatch']">
		<condition type="custom">
 		<script returnValue="p">
  			<lang type="hql">FROM AmpXmlPatch p WHERE p.patchId="<xsl:value-of select="."/>"
  			</lang>
		 </script>
 		<test>p!=null</test>
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