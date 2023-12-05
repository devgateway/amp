/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.async;

import org.digijava.kernel.ampapi.endpoints.common.JsonApiResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Request Data to be stored in the cache
 *
 */
public class AsyncResult {
    
    private String resultId;
    
    private AsyncStatus status = AsyncStatus.RUNNING;
    
    private List<JsonApiResponse> results = new ArrayList<>();
    
    public AsyncResult(String resultId) {
        this.resultId = resultId;
    }
    
    public String getResultId() {
        return resultId;
    }
    
    public List<JsonApiResponse> getResults() {
        return results;
    }
    
    public AsyncStatus getStatus() {
        return status;
    }
    
    public void setStatus(AsyncStatus status) {
        this.status = status;
    }
}
