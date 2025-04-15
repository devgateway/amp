/*
 * ObjectPersister.java 
 */

package org.digijava.module.aim.helper;

import java.io.*;

public class ObjectPersister {
    
    public static void saveObject(Object obj,
            String fileName) throws IOException {
        //////System.out.println("File Name in saveObject:" + fileName);
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream outputObject = new ObjectOutputStream(fos);
        outputObject.writeObject(obj);
    }
    
    public static Object loadObject(String fileName) 
    throws IOException,ClassNotFoundException {
        //////System.out.println("File Name in loadObject:" + fileName);
        File file = new File(fileName);
        if(file.exists())
        {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream inputObject = new ObjectInputStream(fis);
            return inputObject.readObject();
        }
        return null;
    }
    
    public static boolean removeObject(String fileName) throws Exception {
        //////System.out.println("File Name in removeObject:" + fileName);
        File file = new File(fileName);
        return file.delete();
    }
}
