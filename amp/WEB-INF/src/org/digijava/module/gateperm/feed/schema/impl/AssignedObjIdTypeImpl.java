//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.07.15 at 03:55:13 PM EEST 
//


package org.digijava.module.gateperm.feed.schema.impl;

public class AssignedObjIdTypeImpl implements org.digijava.module.gateperm.feed.schema.AssignedObjIdType, com.sun.xml.bind.JAXBObject, org.digijava.module.gateperm.feed.schema.impl.runtime.UnmarshallableObject, org.digijava.module.gateperm.feed.schema.impl.runtime.XMLSerializable, org.digijava.module.gateperm.feed.schema.impl.runtime.ValidatableObject
{

    protected java.lang.String _PermissibleClass;
    protected java.lang.String _Value;
    public final static java.lang.Class version = (org.digijava.module.gateperm.feed.schema.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (org.digijava.module.gateperm.feed.schema.AssignedObjIdType.class);
    }

    public java.lang.String getPermissibleClass() {
        return _PermissibleClass;
    }

    public void setPermissibleClass(java.lang.String value) {
        _PermissibleClass = value;
    }

    public java.lang.String getValue() {
        return _Value;
    }

    public void setValue(java.lang.String value) {
        _Value = value;
    }

    public org.digijava.module.gateperm.feed.schema.impl.runtime.UnmarshallingEventHandler createUnmarshaller(org.digijava.module.gateperm.feed.schema.impl.runtime.UnmarshallingContext context) {
        return new org.digijava.module.gateperm.feed.schema.impl.AssignedObjIdTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(org.digijava.module.gateperm.feed.schema.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        try {
            context.text(((java.lang.String) _Value), "Value");
        } catch (java.lang.Exception e) {
            org.digijava.module.gateperm.feed.schema.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
    }

    public void serializeAttributes(org.digijava.module.gateperm.feed.schema.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startAttribute("", "permissibleClass");
        try {
            context.text(((java.lang.String) _PermissibleClass), "PermissibleClass");
        } catch (java.lang.Exception e) {
            org.digijava.module.gateperm.feed.schema.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
    }

    public void serializeURIs(org.digijava.module.gateperm.feed.schema.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (org.digijava.module.gateperm.feed.schema.AssignedObjIdType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004na"
+"met\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000#com.sun.msv.da"
+"tatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun."
+"msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv"
+".datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatyp"
+"e.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/"
+"String;L\u0000\btypeNameq\u0000~\u0000\u000eL\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype"
+"/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w3.org/2001/XMLSche"
+"mat\u0000\u0006stringsr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$"
+"Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpacePr"
+"ocessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0001sr\u00000com.sun.msv.grammar.Expression$Null"
+"SetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.StringP"
+"air\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u000eL\u0000\fnamespaceURIq\u0000~\u0000\u000expq\u0000~\u0000\u0012q\u0000"
+"~\u0000\u0011sr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000"
+"\u0002L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;xq\u0000~\u0000\u0003ppq\u0000~\u0000\t"
+"sr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNa"
+"meq\u0000~\u0000\u000eL\u0000\fnamespaceURIq\u0000~\u0000\u000exr\u0000\u001dcom.sun.msv.grammar.NameClass"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0010permissibleClasst\u0000\u0000sr\u0000\"com.sun.msv.grammar.E"
+"xpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/E"
+"xpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.Expressio"
+"nPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parent"
+"t\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\u0001\u0001pq\u0000~\u0000\u0005x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends org.digijava.module.gateperm.feed.schema.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(org.digijava.module.gateperm.feed.schema.impl.runtime.UnmarshallingContext context) {
            super(context, "-----");
        }

        protected Unmarshaller(org.digijava.module.gateperm.feed.schema.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return org.digijava.module.gateperm.feed.schema.impl.AssignedObjIdTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        attIdx = context.getAttribute("", "permissibleClass");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 3;
                            eatText1(v);
                            continue outer;
                        }
                        break;
                    case  4 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        private void eatText1(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _PermissibleClass = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        attIdx = context.getAttribute("", "permissibleClass");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 3;
                            eatText1(v);
                            continue outer;
                        }
                        break;
                    case  4 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                }
                super.leaveElement(___uri, ___local, ___qname);
                break;
            }
        }

        public void enterAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        if (("permissibleClass" == ___local)&&("" == ___uri)) {
                            state = 1;
                            return ;
                        }
                        break;
                    case  4 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                }
                super.enterAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void leaveAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        attIdx = context.getAttribute("", "permissibleClass");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 3;
                            eatText1(v);
                            continue outer;
                        }
                        break;
                    case  4 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  2 :
                        if (("permissibleClass" == ___local)&&("" == ___uri)) {
                            state = 3;
                            return ;
                        }
                        break;
                }
                super.leaveAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void handleText(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                try {
                    switch (state) {
                        case  3 :
                            state = 4;
                            eatText2(value);
                            return ;
                        case  0 :
                            attIdx = context.getAttribute("", "permissibleClass");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                state = 3;
                                eatText1(v);
                                continue outer;
                            }
                            break;
                        case  4 :
                            revertToParentFromText(value);
                            return ;
                        case  1 :
                            state = 2;
                            eatText1(value);
                            return ;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Value = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
