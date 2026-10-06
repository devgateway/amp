package org.digijava.module.aim.multistepwizard;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.multistepwizard.annotation.Checkbox;
import org.digijava.module.aim.multistepwizard.exception.ClassNotAllowedForCheckbox;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
/**
 * This ActionForm should be used for multi-step wizards where the scope of the form is "session". More 
 * specifically it is intended address the problem of checkboxes in session-scoped forms. 
 * For example: You have a checkbox at step 4 of a wizard with default value 'true' (selected). 
 * The user arrives at step 4 and decides that he wants the property of the checkbox to be 'false' so he deselects
 * the checkbox. The PROBLEM is that the browser doesn't send any information if the checkbox is deselected so the 
 * property of the ActionForm would remain 'true'. 
 * 
 * One should do the following in order to use it:
 * 1) Each jsp-form should contain a hidden property called "stepInWizard" who's value should be the 
 * number of the step.
 *  Ex. <html:hidden property="stepInWizard" value="4" />
 * 
 * 2) Your ActionForm class should extend this class (MultiStepActionForm) instead of ActionForm
 * 
 * 3) Annotate the property that corresponds to the checkbox with the @Checkbox Annotation.
 *  Ex. 
 *  @Checkbox(step=4, resetValue="false")
 *  private boolean publicReport = false;
 *  step - the step in the wizard at which this checkbox appears in (corresponding to the 'stepInWizard' property)
 *  resetValue - String represenation of the value that the property in the ActionForm (in our example: publicReport) 
 *          should have if the checkbox is deselected
 *  
 * 4) If you need to overwrite the function reset(ActionMapping mapping, HttpServletRequest request) please don't forget to call the
 * function reset from the super class as the first command in your function. 
 * Ex. super.reset(mapping,request); 
 * 
 * @author Alex Gartner
 *
 */
public abstract class MultiStepActionForm extends ActionForm {
    private static String[] allowedClasses  = {
        "int",
        "long",
        "float",
        "double",
        "boolean",
        "java.lang.Integer",
        "java.lang.Long",
        "java.lang.Float",
        "java.lang.Double",
        "java.lang.Boolean"
    };
    
    private int stepInWizard;

    public int getStepInWizard() {
        return stepInWizard;
    }

    public void setStepInWizard(int stepInWizard) {
        this.stepInWizard = stepInWizard;
    }
    
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        try {
            String stepStr  = request.getParameter("stepInWizard");
            if ( stepStr!=null ) {
                int step        = Integer.parseInt(stepStr);
                clearCheckboxes(step);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void clearCheckboxes (int stepInWizard) throws Exception {
        Class myClass       = this.getClass();
        Field[] fields      = myClass.getDeclaredFields();
        
        for (int i=0; i<fields.length; i++) {
            Checkbox checkboxAnn    = fields[i].getAnnotation( Checkbox.class );
            if ( checkboxAnn != null) {
                PropertyDescriptor beanProperty = new PropertyDescriptor(fields[i].getName(), myClass);
                Class propertyClass             = beanProperty.getPropertyType();
                if ( !checkAllowedClass(propertyClass) ) {
                    throw new ClassNotAllowedForCheckbox(propertyClass + " not allowed");
                }
                if ( stepInWizard == checkboxAnn.step() ) { // we are at the right step for a reset
                    if ( propertyClass.isArray() ) {
                        Object[] params     = new Object[1];
                        params[0]           = null;
                        beanProperty.getWriteMethod().invoke(this, params );
                        continue;
                    }
                    
                    if ( propertyClass.isPrimitive() ) {
                        String primitive    = propertyClass.getName();
                        String methodName   = "parse";
                        methodName          += primitive.substring(0, 1).toUpperCase();
                        methodName          += primitive.substring(1);
                        Class[] paramTypes  = new Class[1];
                        paramTypes[0]       = String.class;
                        
                        Class nonPrimitiveClass = findClassForPrimitive(propertyClass);
                        Method parseMethod      = nonPrimitiveClass.getDeclaredMethod(methodName, paramTypes);
                        
                        Object[] parseParams    = new Object[1];
                        parseParams[0]          = checkboxAnn.resetValue();
                        
                        Object[] params         = new Object[1];
                        params[0]               = parseMethod.invoke(null, parseParams);
                        
                        beanProperty.getWriteMethod().invoke(this, params);
                        continue;
                    }
                    
                    /* If it is a normal allowed Class....not primitive nor array */
                    Class[] constructorParamTypes       = new Class[1];
                    constructorParamTypes[0]            = String.class;
                    Constructor constructor             = propertyClass.getConstructor(constructorParamTypes);
                    
                    Object[] constructorParams          = new Object[1];
                    constructorParams[0]                = checkboxAnn.resetValue();
                    
                    Object[] params                     = new Object[1];
                    params[0]                           = constructor.newInstance( constructorParams );
                    
                    beanProperty.getWriteMethod().invoke(this, params);
                }               
            }
        }
        
    }
    
    private boolean checkAllowedClass(Class classObject) {
        String className    = classObject.getName();
        if ( classObject.isArray() )
                return true;
        
        for (int i=0; i<allowedClasses.length; i++) {
            if ( className.equals(allowedClasses[i]) ) {
                return true;
            }
        }
        return false;
    }
    
    private static Class findClassForPrimitive(Class primitiveClass) throws Exception {
        String primitiveClassName   = primitiveClass.getName();
        if (primitiveClassName.equals("int")) {
            return Class.forName( "java.lang.Integer" );
        }
        else {
            String className        = "java.lang.";
            className               += primitiveClassName.substring(0,1).toUpperCase();
            className               += primitiveClassName.substring(1);
            return Class.forName(className);
        }
    }
}
