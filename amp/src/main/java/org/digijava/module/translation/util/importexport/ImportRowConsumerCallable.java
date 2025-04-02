package org.digijava.module.translation.util.importexport;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.translation.importexport.ImportExportOption;
import org.digijava.module.translation.util.ImportExportUtil;

import java.util.concurrent.Callable;

public class ImportRowConsumerCallable implements Callable<ImportResult> {
    private static final Logger LOGGER = Logger.getLogger(ImportRowConsumerCallable.class);

    private ImportExportOption option;
    private Message message;

    public ImportRowConsumerCallable(ImportExportOption option, Message message) {
        this.message = message;
        this.option = option;
    }

    @Override
    public ImportResult call() {
        TranslatorWorker worker = TranslatorWorker.getInstance("");
        ImportResult result = new ImportResult(message.getKey());
        try {
            ImportExportUtil.saveMessage(message, option);
            worker.refresh(message);
        } catch (Exception e) {
            result.setError(e.getMessage());
            result.setSuccess(Boolean.FALSE);
        }
        return result;
    }
}