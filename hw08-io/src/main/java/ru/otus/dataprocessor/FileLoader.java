package ru.otus.dataprocessor;

import com.google.gson.Gson;
import ru.otus.model.Measurement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class FileLoader implements Loader {
    private final Gson gson = new Gson();
    private final String path;

    public FileLoader(String path) {
        this.path = path;
    }

    @Override
    public List<Measurement> load() throws IOException {
        try (var bufferedReader = new BufferedReader(new FileReader(Paths.get(path).toFile()))) {
            return Arrays.asList(
                gson.fromJson(
                    bufferedReader,
                    Measurement[].class
                )
            );
        }
    }
}
