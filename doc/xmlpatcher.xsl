<?xml version="1.0" encoding="UTF-8"?>
<!-- *******************************************************-->
<!-- AID MANAGEMENT PLATFORM								-->
<!-- PATCHER 2 XSL TRANSFORMATION							-->
<!-- (c) 2009 Development Gateway Foundation				-->
<!-- author Mihai Postelnicu - mpostelnicu@dgfoundation.org	-->
<!-- *******************************************************-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Transform appliedPatch conditions to "custom" condition -->
	<xsl:template match="condition[@type='appliedPatch']">
		<condition type="custom">
 		<script returnVar="p">
  			<lang type="hql">FROM AmpXmlPatch p WHERE p.patchId='<xsl:value-of select="."/>'</lang>
		 </script>
 		<test>p!=null &amp;&amp; p.getState().intValue()==1</test>
		</condition>
	</xsl:template>		
	
	<!-- Transform dbname conditions to "custom" condition -->
	<xsl:template match="condition[@type='dbName']">
		<condition type="custom">
 			<script returnVar="db">
				<lang type="mysql">SELECT database()</lang>
				<lang type="oracle">SELECT sys_context('USERENV', 'CURRENT_SCHEMA') FROM dual</lang>
				<lang type="postgres">SELECT current_database()</lang>
			</script>
 			<test>db.equalsIgnoreCase("<xsl:value-of select="."/>")</test>
		</condition>
	</xsl:template>
	
	<!-- Standard copy template. Copy the rest of the nodes, unchanged -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>	
</xsl:stylesheet>