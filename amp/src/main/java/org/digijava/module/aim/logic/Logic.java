package org.digijava.module.aim.logic;

import org.digijava.module.aim.logic.boliviaimpl.BoliviaLogicFactory;
import org.digijava.module.aim.logic.defaultimpl.DefaultLogicFactory;

/**
 * 
 * @author Mauricio Coria - coriamauricio@gmail.com
 */
public class Logic {
    public static LogicFactory DEFAULT_FACTORY = new DefaultLogicFactory();
    public static LogicFactory BOLIVIA_FACTORY = new BoliviaLogicFactory();
    
    private static LogicFactory instance;
    
    static{
        instance =  DEFAULT_FACTORY;
    }
    
    public static LogicFactory getInstance(){
        return instance;
    }
    
    public static void switchLogic(LogicFactory theInstance){
        instance = theInstance;
    }
}
