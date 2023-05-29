package org.digijava.kernel.ampapi.endpoints.integration.service;


import org.digijava.kernel.ampapi.endpoints.integration.dto.FileInformationDTO;

import java.util.List;
import java.util.Optional;

public interface FileInformationService {

    List<FileInformationDTO> getAllFileInformation(int page, int size);
//
    Optional<FileInformationDTO> getFileInformationById(Long id);

    Optional<FileInformationDTO> getFileInformationByFileName(String fileName);
//
//    FileInformationDTO createFileInformation(FileInformationDTO fileInformation);
//
//    FileInformationDTO updateFileInformation(UUID id, FileInformationDTO fileInformationDetails);
//
//    void deleteFileInformation(UUID id);
//
//    Optional<FileInformationDTO> getFileInformationByFileName(String fileName);
}
