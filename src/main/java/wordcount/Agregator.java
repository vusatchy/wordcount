package wordcount;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Agregator {

    private static final String SPLIT_REGEX = "[\\s@&.,?$+-]+";
    private String textPath;
    private String stopwordsPath;

    public Agregator(String textPath, String stopwordsPath) {
        this.textPath = textPath;
        this.stopwordsPath = stopwordsPath;
    }

    private List<String> readTokens(String filepath) throws IOException {
        return Files.lines(Paths.get(filepath))
            .map(String::toLowerCase)
            .map(line -> line.split(SPLIT_REGEX))
            .flatMap(Stream::of)
            .collect(Collectors.toList());
    }

    private Map<String, Long> aggregate(List<String> textTokens, List<String> stopwords) {
        return textTokens.stream()
            .filter(token -> !stopwords.contains(token))
            .collect(groupingBy(name -> name, counting()))
            .entrySet()
            .stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    public Map<String, Long> process() throws IOException {
        List<String> textTokens = readTokens(textPath);
        List<String> stopwords = readTokens(stopwordsPath);
        return aggregate(textTokens, stopwords);
    }

}
