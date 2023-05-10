package org.digijava.kernel.ampapi.endpoints.integration;

import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class IntegraionUploadsDirectoryConfig {


//    @Value("${file.upload-dir}")
    private static IntegraionUploadsDirectoryConfig config;
    private static String uploadsDir = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.INTEGRATION_FILE_UPLOAD_DIR);//"/opt/etl/"; //FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.INTEGRATION_FILE_UPLOAD_DIR);;

    private IntegraionUploadsDirectoryConfig(){
    }

    public static IntegraionUploadsDirectoryConfig getInstance(){
        if (config == null){
           config =  new IntegraionUploadsDirectoryConfig();
            createUploadsDirectory();

        }
        return config;
    }


    private static void createUploadsDirectory() {
        File file = new File(uploadsDir);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public String getUploadsDir() {
        return uploadsDir;
    }
}