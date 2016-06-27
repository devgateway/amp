/*
 * Copyright (c) 2013 Development Gateway (www.developmentgateway.org)
 */

package org.digijava.module.dataExchange.jobs;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpInterchangeableResult;
import org.digijava.module.aim.dbentity.AmpInterchangeableResult.AmpResultStatus;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.message.jobs.ConnectionCleaningJob;
import org.digijava.module.dataExchange.util.DbUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import java.sql.Date;

import static java.lang.System.currentTimeMillis;
import static org.digijava.module.aim.dbentity.AmpInterchangeableResult.AmpResultStatus.*;


/**
 * Job to call the import processor
 *
 * @author apicca
 *
 */
public class ImportProcessorCallJob extends ConnectionCleaningJob implements StatefulJob {

    private static final String PROJECT_CODE_FIELD = "project_code";
    private static final String ACTIVITY_ID_FIELD = "activity_id";
    private static final String OPERATION_FIELD = "operation";
    private static final String ERROR_DETAILS_FIELD = "error_details";
    private static final Logger logger = Logger.getLogger(ImportProcessorCallJob.class);

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
            if(jsonResponse.getStatus() >= 200 && jsonResponse.getStatus() < 300) {
                JsonNode body = jsonResponse.getBody();
                logger.info(body);
                saveResults(body, INSERTED);
                saveResults(body, UPDATED);
                saveResults(body, FAILED);
            } else {
                JsonNode body = jsonResponse.getBody();
                logger.warn("Error [" + jsonResponse.getStatus() + "] response=" + body);
                saveError(body);
            }
        } catch (UnirestException e) {
            logger.error("Error trying to import url: " + url, e);
        }
    }

    private void saveError(JsonNode body) {
        AmpInterchangeableResult ampInterchangeableResult = new AmpInterchangeableResult();
        JSONObject jsonObject = body.getObject();
        ampInterchangeableResult.setErrorDetails(jsonObject.getString("message"));
        ampInterchangeableResult.setOperation(jsonObject.getString("error"));
        ampInterchangeableResult.setDate(new Date(jsonObject.getLong("timestamp")));
        ampInterchangeableResult.setStatus(FAILED_COMPLETE);
        DbUtil.saveObject(ampInterchangeableResult);
    }

    private void saveResults(JsonNode body, AmpResultStatus status) {
        if(body.getObject().get(status.getType()) == null) {
            logger.info("Could not find type: " + status.getType());
            return;
        }
        JSONArray results = body.getObject().getJSONArray(status.getType());
        for (int i = 0; i < results.length(); i++) {
            JSONObject jsonObject = results.getJSONObject(i);
            AmpInterchangeableResult ampInterchangeableResult = new AmpInterchangeableResult();
            ampInterchangeableResult.setProjectId(jsonObject.getString(PROJECT_CODE_FIELD));
            ampInterchangeableResult.setStatus(status);
            ampInterchangeableResult.setDate(new Date(currentTimeMillis()));
            if(status == UPDATED) {
                ampInterchangeableResult.setAmpActivityId(jsonObject.getString(ACTIVITY_ID_FIELD));
            } else if(status == FAILED) {
                ampInterchangeableResult.setOperation(jsonObject.getString(OPERATION_FIELD));
                ampInterchangeableResult.setErrorDetails(jsonObject.getString(ERROR_DETAILS_FIELD));
            }
            try {
                DbUtil.saveObject(ampInterchangeableResult);
            } catch (Exception e) {
                logger.error("Error trying to save result", e);
            }
        }
    }

}
