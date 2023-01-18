package ru.job4j.cache;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirFileCache extends AbstractCache<String, String> {


    private String cachingDir;

    public DirFileCache(String cachingDir) {
        this.cachingDir = cachingDir;
    }

    public void setCachingDir(String cachingDir) {
        this.cachingDir = cachingDir;
    }

    public String getCachingDir() {
        return cachingDir;
    }

    @Override
    public String load(String key) {
        String textFromFile = null;
        try {
            textFromFile = Files.readString(Path.of(cachingDir));
        } catch (IOException e) {
            e.printStackTrace();
        }
    return textFromFile;
    }
}