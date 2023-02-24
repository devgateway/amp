/*
 * Copyright (c) 2013 Development Gateway (www.developmentgateway.org)
 */

package org.digijava.module.message.jobs;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.HttpResponse;
import org.apache.log4j.Logger;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;


/**
 * Job to call the import processor
 *
 * @author apicca
 *
 */

public class ImportProcessorCallJob extends ConnectionCleaningJob implements StatefulJob{
    private static Logger logger = Logger.getLogger(ImportProcessorCallJob.class);

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String url = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.IMPORT_PROCESSOR_URL);
        HttpResponse<JsonNode> jsonResponse = null;
        try {
            jsonResponse = Unirest.post(url)
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Cache-Control", "no-cache")
                    .asJson();
        } catch (UnirestException e) {
            logger.error("Unirest call failed", e);
        }

        logger.info(jsonResponse.getBody());
    }

}
