<?xml version="1.0" encoding="UTF-8"?>
<!-- *******************************************************-->
<!-- AID MANAGEMENT PLATFORM								-->
<!-- PATCHER 2 XSL TRANSFORMATION							-->
<!-- (c) 2009 Development Gateway Foundation				-->
<!-- author Mihai Postelnicu - mpostelnicu@dgfoundation.org	-->
<!-- *******************************************************-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <!-- Check if the specified patch has already been found and applied (patch dependency) -->
    <xsl:template match="condition[@type='appliedPatch']">
        <condition type="custom">
            <xsl:if test="@inverted">
                <xsl:attribute name="inverted">
                    <xsl:value-of select="@inverted"/>
                </xsl:attribute>
            </xsl:if>
            <script returnVar="p">
                <lang type="hql">FROM AmpXmlPatch p WHERE p.patchId='<xsl:value-of select="."/>'
                </lang>
            </script>
            <test>p!=null &amp;&amp; p.getState().intValue()==1</test>
        </condition>
    </xsl:template>


    <!-- Check if table index exists -->
    <xsl:template match="condition[@type='indexExists']">
        <condition type="custom">
            <xsl:if test="@inverted">
                <xsl:attribute name="inverted">
                    <xsl:value-of select="@inverted"/>
                </xsl:attribute>
            </xsl:if>
            <script returnVar="count">
                <lang type="mysql">
                    SELECT COUNT(index_name) FROM information_schema.statistics WHERE
                    index_name='<xsl:value-of select="."/>' AND table_schema=database();
                </lang>
            </script>
            <test>count.intValue()==1</test>
        </condition>
    </xsl:template>

    <!-- Check if table constraint exists -->
    <xsl:template match="condition[@type='constraintExists']">
        <condition type="custom">
            <xsl:if test="@inverted">
                <xsl:attribute name="inverted">
                    <xsl:value-of select="@inverted"/>
                </xsl:attribute>
            </xsl:if>
            <script returnVar="count">
                <lang type="mysql">
                    SELECT COUNT(constraint_name) FROM information_schema.table_constraints WHERE
                    constraint_name='<xsl:value-of select="."/>' AND table_schema=database();
                </lang>
            </script>
            <test>count.intValue()==1</test>
        </condition>
    </xsl:template>

    <!-- Check if table or view exists -->
    <xsl:template match="condition[@type='tableOrViewExists']">
        <condition type="custom">
            <xsl:if test="@inverted">
                <xsl:attribute name="inverted">
                    <xsl:value-of select="@inverted"/>
                </xsl:attribute>
            </xsl:if>
            <script returnVar="count">
                <lang type="postgres">
                    SELECT count(relname) FROM pg_class WHERE relname ='<xsl:value-of select="."/>';
                </lang>
            </script>
            <test>count.intValue()==1</test>
        </condition>
    </xsl:template>

    <!-- Check if column exists -->
    <xsl:template match="condition[@type='columnExists']">
        <condition type="custom">
            <xsl:if test="@inverted">
                <xsl:attribute name="inverted">
                    <xsl:value-of select="@inverted"/>
                </xsl:attribute>
            </xsl:if>
            <script returnVar="count">
                <lang type="postgres">
                    SELECT count(*) FROM pg_attribute WHERE attrelid = (SELECT oid FROM pg_class WHERE relname =
                    '<xsl:value-of select="@tablename"/>') AND attname = '<xsl:value-of select="@columnname"/>';
                </lang>
            </script>
            <test>count.intValue()==1</test>
        </condition>
    </xsl:template>

    <!-- Check if table is empty -->
    <xsl:template match="condition[@type='tableEmpty']">
        <condition type="custom">
            <xsl:if test="@inverted">
                <xsl:attribute name="inverted">
                    <xsl:value-of select="@inverted"/>
                </xsl:attribute>
            </xsl:if>
            <script returnVar="count">
                <lang type="sql">
                    SELECT count(*) FROM <xsl:value-of select="."/>;
                </lang>
            </script>
            <test>count.intValue()==0</test>
        </condition>
    </xsl:template>

    <!-- Check if the current database has the given name -->
    <xsl:template match="condition[@type='dbName']">
        <condition type="custom">
            <xsl:if test="@inverted">
                <xsl:attribute name="inverted">
                    <xsl:value-of select="@inverted"/>
                </xsl:attribute>
            </xsl:if>
            <script returnVar="db">
                <lang type="mysql">SELECT database()</lang>
                <lang type="oracle">SELECT sys_context('USERENV', 'CURRENT_SCHEMA') FROM dual</lang>
                <lang type="postgres">SELECT current_database()</lang>
            </script>
            <test>db.equalsIgnoreCase("<xsl:value-of select="."/>")
            </test>
        </condition>
    </xsl:template>

    <!-- Check if a certain entry exists in the a certain table -->
    <xsl:template match="condition[@type='entryInTableMissing']">
        <condition type="custom">
            <xsl:if test="@inverted">
                <xsl:attribute name="inverted">
                    <xsl:value-of select="@inverted"/>
                </xsl:attribute>
            </xsl:if>
            <script returnVar="count">
                <lang type="sql">
                    SELECT count(*) FROM
                    <xsl:value-of select="@tablename"/> where
                    <xsl:value-of select="@columnname"/> = '<xsl:value-of select="@columnvalue"/>'
                </lang>
            </script>
            <test>count.intValue()==0</test>
        </condition>
    </xsl:template>

    <!-- Check if a certain entry exists in the a certain table -->
    <xsl:template match="condition[@type='entryInTableExists']">
        <condition type="custom">
            <xsl:if test="@inverted">
                <xsl:attribute name="inverted">
                    <xsl:value-of select="@inverted"/>
                </xsl:attribute>
            </xsl:if>
            <script returnVar="count">
                <lang type="sql">
                    SELECT count(*) FROM
                    <xsl:value-of select="@tablename"/> where
                    <xsl:value-of select="@columnname"/> = '<xsl:value-of select="@columnvalue"/>'
                </lang>
            </script>
            <test>count.intValue()!=0</test>
        </condition>
    </xsl:template>
    <!-- Check if table is empty -->
    <xsl:template match="condition[@type='tableNotEmpty']">
        <condition type="custom">
            <xsl:if test="@inverted">
                <xsl:attribute name="inverted">
                    <xsl:value-of select="@inverted"/>
                </xsl:attribute>
            </xsl:if>
            <script returnVar="count">
                <lang type="sql">
                    SELECT count(*) FROM <xsl:value-of select="."/>;
                </lang>
            </script>
            <test>count.intValue()>0</test>
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