package org.digijava.kernel.ampapi.endpoints.integration.repository;

import org.dgfoundation.amp.algo.AmpCollections;
import org.digijava.kernel.entity.integration.FileInformation;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class FileInformationRepository {

    private static FileInformationRepository fileInformationRepository;

    public static FileInformationRepository getInstance() {
        if (fileInformationRepository == null) {
            fileInformationRepository = new FileInformationRepository();
        }
        return fileInformationRepository;
    }

    public List<FileInformation> findAll(int offset, int limit) {
        Session session = PersistenceManager.getSession();
        Query query = session.createQuery("FROM FileInformation");
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.list();
    }

    public Optional<FileInformation> findById(Long id) {
        Session session = PersistenceManager.getSession();
        Query query = session.createQuery("SELECT fi FROM FileInformation fi " +
                "LEFT JOIN FETCH fi.fileRecords fr WHERE fi.id = :id");
        query.setParameter("id", id);
        return Optional.ofNullable((FileInformation) query.uniqueResult());

    }

    public Optional<FileInformation> findByName(String fileName) {
        Session session = PersistenceManager.getSession();
        Query query = session.createQuery("FROM FileInformation where fileName=:fileName");
        query.setParameter("fileName", fileName);

        return Optional.ofNullable((FileInformation) query.uniqueResult());

    }
}