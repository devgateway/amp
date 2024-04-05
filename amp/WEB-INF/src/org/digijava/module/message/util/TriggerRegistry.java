package org.digijava.module.message.util;

import org.apache.struts.util.LabelValueBean;
import org.digijava.module.message.triggers.Trigger;

import java.util.*;
/**
 * @author Dare
 */
public class TriggerRegistry<T> {

    private static TriggerRegistry<Trigger> _instance;
    private Map<String,String> longNames;
    private Map<String,Class<? extends T>> classes;

    private TriggerRegistry(){
        longNames = new HashMap<String, String>();
        classes = new HashMap<String, Class<? extends T>>();

    }

    public static TriggerRegistry<Trigger> getInstance(){
        if (_instance==null){
            _instance= new TriggerRegistry<Trigger>();
        }
        return _instance;
    }

    public void register(Class<? extends T> clazz, String longName){
        classes.put(clazz.getName(), clazz);
        longNames.put(clazz.getName(), longName);
    }

    public List<LabelValueBean> getLabelValuesList(){
        Set<String> keys = classes.keySet();
        List<LabelValueBean> lvbList = new ArrayList<LabelValueBean>(keys.size());
        for (String key : keys) {
            Class<? extends T> c = classes.get(key);
            String longName = longNames.get(c.getName());
            LabelValueBean lvb = new LabelValueBean(longName,c.getName());
            lvbList.add(lvb);
        }
        return lvbList;
    }
}
