package com.programmers.infrastructure.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.domain.repository.FileRepository;
import com.programmers.exception.unchecked.SystemErrorException;
import com.programmers.util.AppProperties;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JsonFileRepository<T> implements FileRepository<T> {
    protected final ObjectMapper objectMapper;
    protected final File repositoryFile;

    protected static final String currentDir = System.getProperty("user.dir");
    protected final String specifiedPath;

    public JsonFileRepository(ObjectMapper objectMapper, String filePathProperty) {
        this.objectMapper = objectMapper;
        this.specifiedPath = AppProperties.getProperty("file.specifiedPath");
        this.repositoryFile = new File(currentDir + File.separator + specifiedPath + File.separator + AppProperties.getProperty(filePathProperty));
    }

    public ConcurrentHashMap<Long, T> loadFromFile() {
        if (repositoryFile.exists()) {
            try {
                if (repositoryFile.length() > 0) {
                    ConcurrentHashMap<Long, T> loadedItems = objectMapper.readValue(
                            repositoryFile, new TypeReference<>() {
                            }
                    );
                    return loadedItems;
                }
            } catch (IOException e) {
                throw new SystemErrorException();
            }
        } else {
            try {
                repositoryFile.createNewFile();
            } catch (IOException e) {
                throw new SystemErrorException();
            }
        }
        return new ConcurrentHashMap<Long, T> ();
    }

    public void saveToFile(ConcurrentHashMap<Long, T> items) throws IOException {
        synchronized (this) {
            objectMapper.writeValue(repositoryFile, items);
        }
    }
}

