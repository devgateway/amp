package org.dgfoundation.amp.asciidoc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorCollector;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * Class used to generate the ascii doc used in swagger documentation. The class is executed from mvn
 *
 *
 * @author Viorel Chihai
 */
public class ApiErrorAsciiDocCreator {
    
    public static void main(String[] args) throws IOException {
        ApiErrorAsciiDocCreator apiDocsPreprocessor = new ApiErrorAsciiDocCreator();
        apiDocsPreprocessor.writeToFile(args[0], args[1]);
    }
    
    private void writeToFile(String dir, String fileName) throws IOException {
        File fileDir = FileUtils.getFile(dir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File file = FileUtils.getFile(dir, fileName);
        PrintWriter writer = new PrintWriter(new FileWriter(file, false));
    
        writer.println("== API Errors");
        for (Map.Entry<String, Class> entry : ApiError.ERROR_NAME_CLASSES.entrySet()) {
            generateTableForErrorClass(writer, entry.getKey(), entry.getValue());
        }
    
        writer.close();
    }
    
    private void generateTableForErrorClass(PrintWriter writer, String errorClassName, Class errorClass) {
        ApiErrorCollector errorCollector = new ApiErrorCollector();
        List<ApiErrorMessage> errors = errorCollector.collect(errorClass);
    
        writer.println("." + errorClassName);
        writer.println("[cols=\"1,5\"]");
        writer.println("|===");
        writer.println("|Code |Description");
        writer.println("");
    
        for (ApiErrorMessage error : errors) {
            writer.println("|*" + error.getErrorId() + "*");
            writer.println("|" + error.description + "");
        }
        
        writer.println("|===");
    }
}
