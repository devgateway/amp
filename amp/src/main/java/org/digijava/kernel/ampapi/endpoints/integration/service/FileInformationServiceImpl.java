package org.digijava.kernel.ampapi.endpoints.integration.service;

import org.digijava.kernel.ampapi.endpoints.integration.dto.FileInformationDTO;
import org.digijava.kernel.ampapi.endpoints.integration.repository.FileInformationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FileInformationServiceImpl implements FileInformationService {

    private static FileInformationService fileInformationService;

    private FileInformationServiceImpl() {

    }

    public static FileInformationService getInstance() {
        if (fileInformationService == null) {
            fileInformationService = new FileInformationServiceImpl();
        }
        return fileInformationService;
    }


    public List<FileInformationDTO> getAllFileInformation(int page, int size) {
        return FileInformationRepository.getInstance().findAll(page, size).stream().map(fileInformation ->
                FileInformationDTO.fromEntity(fileInformation)
        ).collect(Collectors.toList());
    }

    @Override
    public Optional<FileInformationDTO> getFileInformationById(Long id) {
        return Optional.ofNullable(FileInformationRepository.getInstance().findById(id)
                .map(fileInformation -> FileInformationDTO.fromEntity(fileInformation))
                .orElse(null));
    }

    public Optional<FileInformationDTO> getFileInformationByFileName(String fileName) {
        return Optional.ofNullable(FileInformationRepository.getInstance().findByName(fileName)
                .map(fileInformation -> FileInformationDTO.fromEntity(fileInformation))
                .orElse(null));

    }
//    public FileInformationDTO getFileInformationById(Long id) {
//        return FileInformationRepository.getInstance().findById(id)
//                .map(fileInformation -> FileInformationDTO.fromEntity(fileInformation))
//                .orElseThrow(() -> new ResourceNotFoundException("FileInformation", "id", id));
//    }
//
//    public FileInformationDTO createFileInformation(FileInformationDTO fileInformation) {
//        return FileInformationDTO.fromEntity(fileInformationRepository.save(fileInformation.toEntity()));
//    }
//
//    public FileInformationDTO updateFileInformation(UUID id, FileInformationDTO fileInformationDetails) {
//        FileInformationDTO fileInformation = getFileInformationById(id);
//        fileInformation.setFileName(fileInformationDetails.getFileName());
//        fileInformation.setRecords(fileInformationDetails.getRecords());
//        fileInformation.setSuccess(fileInformationDetails.getSuccess());
//        fileInformation.setError(fileInformationDetails.getError());
//        fileInformation.setStatus(fileInformationDetails.getStatus());
//        return FileInformationDTO.fromEntity(fileInformationRepository.save(fileInformation.toEntity()));
//    }
//
//    public void deleteFileInformation(UUID id) {
//        fileInformationRepository.deleteById(id);
//    }
//
//    public Optional<FileInformationDTO> getFileInformationByFileName(String fileName) {
//        return fileInformationRepository.findByFileName(fileName)
//                .map(fileInformation -> FileInformationDTO.fromEntity(fileInformation));
//
//
//    }
}
