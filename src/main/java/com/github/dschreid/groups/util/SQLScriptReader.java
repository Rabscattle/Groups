package com.github.dschreid.groups.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class SQLScriptReader {
    private final InputStream stream;

    public SQLScriptReader(InputStream stream) {
        this.stream = stream;
    }

    public List<String> getQueries() throws IOException {
        List<String> queries = new LinkedList<>();

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("--") || line.startsWith("#")) {
                    continue;
                }
                sb.append(line);
                if (line.endsWith(";")) {
                    sb.deleteCharAt(sb.length() - 1);
                    String result = sb.toString().trim();
                    if (!result.isEmpty()) {
                        queries.add(result);
                    }
                    sb = new StringBuilder();
                }
            }
        }

        return queries;
    }
}
