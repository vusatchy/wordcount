import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Agregator {

    private String textPath;

    private String stopwordsPath;

    public Agregator(String textPath, String stopwordsPath) {
        this.textPath = textPath;
        this.stopwordsPath = stopwordsPath;
    }

    private List<String> readTokens(String fileName) throws IOException {
        List<String> tokens = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = null;
        while ((line = reader.readLine()) != null) {
            line = line.toLowerCase();
            tokens.addAll(Arrays.asList(line.split("[\\s@&.,?$+-]+")));
        }
        return tokens;
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
