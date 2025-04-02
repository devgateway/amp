package org.dgfoundation.amp.onepager.components.upload;

import org.apache.log4j.Logger;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.upload.FileItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;


/**
 * A simple file manager that knows how to store, read and delete files
 * from the file system.
 */
public class FileManager {
    private static final Logger logger = Logger.getLogger(FileManager.class);
    private IModel<FileItem> fileItemModel;
    private String activityId;

    //private final Folder baseFolder;

    public FileManager(String activityId, IModel<FileItem> fileItemModel) {
        //this.baseFolder = new Folder(baseFolder);
        this.activityId=activityId;
        this.fileItemModel=fileItemModel;
    }

    public int save(FileItem fileItem) throws IOException {
//        File file = new File(baseFolder, fileItem.getName());
//        FileOutputStream fileOS = new FileOutputStream(file, false);
//        logger.warn("Saved file:" + baseFolder.getPath());
        fileItemModel.setObject(fileItem);
        logger.warn("Saved item for activity with id:" + activityId);
        return 1;//IOUtils.copy(fileItem.getInputStream(), fileOS);
    }

    public byte[] get(String fileName) throws IOException {
//        File file = new File(baseFolder, fileName);
//        return IOUtils.toByteArray(new FileInputStream(file));
        return null;
    }

    public boolean delete(String fileName)
    {
//        File file = new File(baseFolder, fileName);
//        return Files.remove(file);
        return true;
    }
}
