//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.07.15 at 03:49:20 GMT+04:00 
//


package org.digijava.module.message.jaxb.impl;

public class AlertTemplateTypeImpl implements org.digijava.module.message.jaxb.AlertTemplateType, com.sun.xml.bind.JAXBObject, org.digijava.module.message.jaxb.impl.runtime.UnmarshallableObject, org.digijava.module.message.jaxb.impl.runtime.XMLSerializable, org.digijava.module.message.jaxb.impl.runtime.ValidatableObject
{

    protected java.lang.String _Received;
    protected java.lang.String _MsgDetails;
    protected java.lang.String _RelatedTrigger;
    protected java.lang.String _Name;
    public final static java.lang.Class version = (org.digijava.module.message.jaxb.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (org.digijava.module.message.jaxb.AlertTemplateType.class);
    }

    public java.lang.String getReceived() {
        return _Received;
    }

    public void setReceived(java.lang.String value) {
        _Received = value;
    }

    public java.lang.String getMsgDetails() {
        return _MsgDetails;
    }

    public void setMsgDetails(java.lang.String value) {
        _MsgDetails = value;
    }

    public java.lang.String getRelatedTrigger() {
        return _RelatedTrigger;
    }

    public void setRelatedTrigger(java.lang.String value) {
        _RelatedTrigger = value;
    }

    public java.lang.String getName() {
        return _Name;
    }

    public void setName(java.lang.String value) {
        _Name = value;
    }

    public org.digijava.module.message.jaxb.impl.runtime.UnmarshallingEventHandler createUnmarshaller(org.digijava.module.message.jaxb.impl.runtime.UnmarshallingContext context) {
        return new org.digijava.module.message.jaxb.impl.AlertTemplateTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(org.digijava.module.message.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeAttributes(org.digijava.module.message.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (_MsgDetails!= null) {
            context.startAttribute("", "msgDetails");
            try {
                context.text(((java.lang.String) _MsgDetails), "MsgDetails");
            } catch (java.lang.Exception e) {
                org.digijava.module.message.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        context.startAttribute("", "name");
        try {
            context.text(((java.lang.String) _Name), "Name");
        } catch (java.lang.Exception e) {
            org.digijava.module.message.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
        context.startAttribute("", "received");
        try {
            context.text(((java.lang.String) _Received), "Received");
        } catch (java.lang.Exception e) {
            org.digijava.module.message.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
        context.startAttribute("", "relatedTrigger");
        try {
            context.text(((java.lang.String) _RelatedTrigger), "RelatedTrigger");
        } catch (java.lang.Exception e) {
            org.digijava.module.message.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
    }

    public void serializeURIs(org.digijava.module.message.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (org.digijava.module.message.jaxb.AlertTemplateType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\u001dcom.sun.msv.grammar."
+"ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com.sun.msv.grammar.Attribut"
+"eExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/gramma"
+"r/NameClass;xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp"
+"\u0000psr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/rel"
+"axng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/"
+"util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000#com.sun.msv.datatype.xsd.StringT"
+"ype\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.datatype.xsd.B"
+"uiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.Conc"
+"reteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeIm"
+"pl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/String;L\u0000\btypeName"
+"q\u0000~\u0000\u0017L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpacePro"
+"cessor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0006stringsr\u00005com"
+".sun.msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xp\u0001sr\u00000com.sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tl"
+"ocalNameq\u0000~\u0000\u0017L\u0000\fnamespaceURIq\u0000~\u0000\u0017xpq\u0000~\u0000\u001bq\u0000~\u0000\u001asr\u0000#com.sun.msv"
+".grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0017L\u0000\fnames"
+"paceURIq\u0000~\u0000\u0017xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000"
+"\nmsgDetailst\u0000\u0000sr\u00000com.sun.msv.grammar.Expression$EpsilonExpr"
+"ession\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\r\u0001psq\u0000~\u0000\nppq\u0000~\u0000\u0012sq\u0000~\u0000#t\u0000\u0004nameq\u0000~"
+"\u0000\'sq\u0000~\u0000\nppq\u0000~\u0000\u0012sq\u0000~\u0000#t\u0000\breceivedq\u0000~\u0000\'sq\u0000~\u0000\nppq\u0000~\u0000\u0012sq\u0000~\u0000#t\u0000\u000er"
+"elatedTriggerq\u0000~\u0000\'sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$Clo"
+"sedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7"
+"j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/g"
+"rammar/ExpressionPool;xp\u0000\u0000\u0000\u0004\u0001pq\u0000~\u0000\u0006q\u0000~\u0000\u0005q\u0000~\u0000\u0007q\u0000~\u0000\tx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends org.digijava.module.message.jaxb.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(org.digijava.module.message.jaxb.impl.runtime.UnmarshallingContext context) {
            super(context, "-------------");
        }

        protected Unmarshaller(org.digijava.module.message.jaxb.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return org.digijava.module.message.jaxb.impl.AlertTemplateTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        attIdx = context.getAttribute("", "name");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 6;
                            continue outer;
                        }
                        break;
                    case  9 :
                        attIdx = context.getAttribute("", "relatedTrigger");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 12;
                            continue outer;
                        }
                        break;
                    case  6 :
                        attIdx = context.getAttribute("", "received");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText3(v);
                            state = 9;
                            continue outer;
                        }
                        break;
                    case  0 :
                        attIdx = context.getAttribute("", "msgDetails");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText4(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  12 :
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
                _Name = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _RelatedTrigger = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Received = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _MsgDetails = value;
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
                    case  3 :
                        attIdx = context.getAttribute("", "name");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 6;
                            continue outer;
                        }
                        break;
                    case  9 :
                        attIdx = context.getAttribute("", "relatedTrigger");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 12;
                            continue outer;
                        }
                        break;
                    case  6 :
                        attIdx = context.getAttribute("", "received");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText3(v);
                            state = 9;
                            continue outer;
                        }
                        break;
                    case  0 :
                        attIdx = context.getAttribute("", "msgDetails");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText4(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  12 :
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
                    case  3 :
                        if (("name" == ___local)&&("" == ___uri)) {
                            state = 4;
                            return ;
                        }
                        break;
                    case  9 :
                        if (("relatedTrigger" == ___local)&&("" == ___uri)) {
                            state = 10;
                            return ;
                        }
                        break;
                    case  6 :
                        if (("received" == ___local)&&("" == ___uri)) {
                            state = 7;
                            return ;
                        }
                        break;
                    case  0 :
                        if (("msgDetails" == ___local)&&("" == ___uri)) {
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
                    case  12 :
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
                    case  3 :
                        attIdx = context.getAttribute("", "name");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 6;
                            continue outer;
                        }
                        break;
                    case  9 :
                        attIdx = context.getAttribute("", "relatedTrigger");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 12;
                            continue outer;
                        }
                        break;
                    case  8 :
                        if (("received" == ___local)&&("" == ___uri)) {
                            state = 9;
                            return ;
                        }
                        break;
                    case  6 :
                        attIdx = context.getAttribute("", "received");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText3(v);
                            state = 9;
                            continue outer;
                        }
                        break;
                    case  0 :
                        attIdx = context.getAttribute("", "msgDetails");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText4(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  5 :
                        if (("name" == ___local)&&("" == ___uri)) {
                            state = 6;
                            return ;
                        }
                        break;
                    case  11 :
                        if (("relatedTrigger" == ___local)&&("" == ___uri)) {
                            state = 12;
                            return ;
                        }
                        break;
                    case  12 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  2 :
                        if (("msgDetails" == ___local)&&("" == ___uri)) {
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
                            attIdx = context.getAttribute("", "name");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText1(v);
                                state = 6;
                                continue outer;
                            }
                            break;
                        case  9 :
                            attIdx = context.getAttribute("", "relatedTrigger");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText2(v);
                                state = 12;
                                continue outer;
                            }
                            break;
                        case  1 :
                            eatText4(value);
                            state = 2;
                            return ;
                        case  10 :
                            eatText2(value);
                            state = 11;
                            return ;
                        case  6 :
                            attIdx = context.getAttribute("", "received");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText3(v);
                                state = 9;
                                continue outer;
                            }
                            break;
                        case  0 :
                            attIdx = context.getAttribute("", "msgDetails");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText4(v);
                                state = 3;
                                continue outer;
                            }
                            state = 3;
                            continue outer;
                        case  7 :
                            eatText3(value);
                            state = 8;
                            return ;
                        case  12 :
                            revertToParentFromText(value);
                            return ;
                        case  4 :
                            eatText1(value);
                            state = 5;
                            return ;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

    }

}
