package ru.job4j.cache;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirFileCache extends AbstractCache<String, String> {


    private final String cachingDir;

    public DirFileCache(String cachingDir) {
        this.cachingDir = cachingDir;
        load(cachingDir);
    }

    public String getCachingDir() {
        return cachingDir;
    }

    @Override
    protected String load(String key) {
        Path path = Paths.get(key);
            try (BufferedReader reader = new BufferedReader(new FileReader(key))) {
                String file = reader.readLine();
                put(path.getFileName().toString(), file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        return get(path.getFileName().toString());
    }

}