package ru.otus.dataprocessor;

import com.google.gson.Gson;
import ru.otus.model.Measurement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class FileLoader implements Loader {
    private final Gson gson = new Gson();
    private final String fileName;

    public FileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() throws IOException {
        URL fileUrl = ClassLoader.getSystemResource(fileName);
        if (fileUrl == null) {
            throw new NoSuchElementException(String.format("Resource file %s not found", fileName));
        }

        try (var bufferedReader = new BufferedReader(new FileReader(fileUrl.getFile()))) {
            return Arrays.asList(
                gson.fromJson(
                    bufferedReader,
                    Measurement[].class
                )
            );
        }
    }
}
