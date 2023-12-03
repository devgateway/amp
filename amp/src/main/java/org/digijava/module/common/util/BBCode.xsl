<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:digi="http://www.digijava.org/XML/BBCode">
	<xsl:output method="html" version="4.0" encoding="UTF-8"/>
	<xsl:template match="/">
		<table>
			<tr>
				<td>
					<xsl:apply-templates/>
				</td>
			</tr>
		</table>
	</xsl:template>
	<xsl:template match="digi:b">
		<b>
			<xsl:apply-templates/>
		</b>
	</xsl:template>
	<xsl:template match="digi:i">
		<i>
			<xsl:apply-templates/>
		</i>
	</xsl:template>
	<xsl:template match="digi:u">
		<u>
			<xsl:apply-templates/>
		</u>
	</xsl:template>
	<xsl:template match="digi:br">
		<br/>
	</xsl:template>
	<xsl:template match="digi:size">
		<span>
			<xsl:attribute name="style"><xsl:variable name="fontstyle">font-size: <xsl:value-of select="@param"/>px; line-height: normal</xsl:variable><xsl:value-of select="$fontstyle"/><!--            <xsl:text disable-output-escaping="no">font-size: </xsl:text>
                <xsl:value-of select="@param"/>
                <xsl:text disable-output-escaping="no">px; line-height: normal</xsl:text> --></xsl:attribute>
			<xsl:apply-templates/>
		</span>
	</xsl:template>
	<xsl:template match="digi:color">
		<font>
			<xsl:attribute name="color"><xsl:value-of select="@param"/></xsl:attribute>
			<xsl:apply-templates/>
		</font>
	</xsl:template>
	<xsl:template match="digi:list">
		<xsl:choose>
			<xsl:when test="@param">
				<ol>
					<xsl:attribute name="type"><xsl:value-of select="@param"/></xsl:attribute>
					<xsl:for-each select="digi:li">
						<li>
							<xsl:apply-templates/>
						</li>
					</xsl:for-each>
				</ol>
			</xsl:when>
			<xsl:otherwise>
				<ul>
					<xsl:for-each select="digi:li">
						<li>
							<xsl:apply-templates/>
						</li>
					</xsl:for-each>
				</ul>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="digi:url">
		<xsl:choose>
			<xsl:when test="@param">
				<a>
					<xsl:attribute name="href"><xsl:value-of select="@param"/></xsl:attribute>
					<xsl:apply-templates/>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<a>
					<xsl:attribute name="href"><xsl:value-of select="."/></xsl:attribute>
					<xsl:apply-templates/>
				</a>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="digi:code">
		<table width="90%" cellspacing="1" cellpadding="3" border="0" align="center">
			<tr>
				<td>
					<span class="genmed">
						<b>code:</b>
					</span>
				</td>
			</tr>
			<tr>
				<td class="code" bgcolor="aliceblue">
					<xsl:apply-templates/>
				</td>
			</tr>
		</table>
	</xsl:template>
	<xsl:template match="digi:img">
		<img border="0">
			<xsl:attribute name="src"><xsl:value-of select="."/></xsl:attribute>
		</img>
	</xsl:template>
	<xsl:template match="digi:quote">
		<table border="0" cellpadding="3" cellspacing="1" align="center" width="90%">
			<tr>
				<td>
					<xsl:choose>
						<xsl:when test="@param">
							<b>
								<xsl:value-of select="@param"/> wrote:
								</b>
						</xsl:when>
						<xsl:otherwise>
							<b>Quote</b>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr>
				<td bgcolor="lavender">
					<xsl:apply-templates/>
				</td>
			</tr>
		</table>
	</xsl:template>
	<xsl:template match="digi:smile">
		<img border="0">
			<xsl:attribute name="src"><xsl:value-of select="."/></xsl:attribute>
		</img>
	</xsl:template>
	<!-- html tags -->
	<xsl:template match="b">
		<b>
			<xsl:apply-templates/>
		</b>
	</xsl:template>
	<xsl:template match="i">
		<i>
			<xsl:apply-templates/>
		</i>
	</xsl:template>
	<xsl:template match="u">
		<u>
			<xsl:apply-templates/>
		</u>
	</xsl:template>
	<xsl:template match="br">
		<br/>
	</xsl:template>
	<xsl:template match="a">
		<a>
			<xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
			<xsl:apply-templates/>
		</a>
	</xsl:template>
	<xsl:template match="pre">
		<pre>
			<xsl:apply-templates/>
		</pre>
	</xsl:template>
	<xsl:template match="img">
		<img border="0">
			<xsl:attribute name="src"><xsl:value-of select="@src"/></xsl:attribute>
		</img>
	</xsl:template>
	<xsl:template match="font">
		<span>
			<xsl:attribute name="style"><xsl:variable name="fontsize">font-size:<xsl:value-of select="@size"/>px;</xsl:variable><xsl:value-of select="$fontsize"/><xsl:variable name="fontface">font-family:<xsl:value-of select="@face"/>;</xsl:variable><xsl:value-of select="$fontface"/><xsl:variable name="fontcolor">color:<xsl:value-of select="@color"/>;</xsl:variable><xsl:value-of select="$fontcolor"/></xsl:attribute>
			<xsl:apply-templates/>
		</span>
	</xsl:template>
	<xsl:template match="list">
		<xsl:choose>
			<xsl:when test="@type">
				<ol>
					<xsl:attribute name="type"><xsl:value-of select="@type"/></xsl:attribute>
					<xsl:for-each select="li">
						<li>
							<xsl:apply-templates/>
						</li>
					</xsl:for-each>
				</ol>
			</xsl:when>
			<xsl:otherwise>
				<ul>
					<xsl:for-each select="li">
						<li>
							<xsl:apply-templates/>
						</li>
					</xsl:for-each>
				</ul>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
