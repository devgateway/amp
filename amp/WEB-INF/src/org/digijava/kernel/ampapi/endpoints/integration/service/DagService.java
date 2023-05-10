package org.digijava.kernel.ampapi.endpoints.integration.service;

import org.digijava.kernel.ampapi.endpoints.integration.dto.DagRunsRequestDTO;
import org.digijava.kernel.ampapi.endpoints.integration.dto.DagRunsResponseDTO;

public interface DagService {


    DagRunsResponseDTO dagRuns(DagRunsRequestDTO dagRunsRequest);
}
