package ru.otus.dataprocessor;

import com.google.gson.Gson;
import ru.otus.model.Measurement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FileLoader implements Loader {
    private final Gson gson = new Gson();
    private final String fileName;

    public FileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() throws IOException {
        try (var bufferedReader = new BufferedReader(new FileReader(ClassLoader.getSystemResource(fileName).getFile()))) {
            return Arrays.asList(
                gson.fromJson(
                    bufferedReader,
                    Measurement[].class
                )
            );
        }
    }
}
