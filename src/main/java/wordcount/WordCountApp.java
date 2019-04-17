package wordcount;

import static java.text.MessageFormat.format;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class WordCountApp {

    public static void main(String[] args) throws IOException {

        test("text", "stopwords");

        String pathToText = "text";
        String pathToStopwords = "stopwords";
        Agregator agregator = new Agregator(pathToText, pathToStopwords);
        Map<String, Long> data = agregator.process();
        data.forEach((key, value) -> {
            System.out.println(key + " : " + value);
        });
    }

    public static void test(String pathToText, String pathToStopwords) throws IOException {
        Agregator agregator = new Agregator(pathToText, pathToStopwords);
        Map<String, Long> data = agregator.process();
        Map<String, Long> expected = new LinkedHashMap<>();
        expected.put("girl", 3L);
        expected.put("andriana", 3L);
        expected.put("was", 2L);
        System.out.println("Starting tests");
        data.entrySet()
            .stream()
            .limit(3)
            .forEach(entry -> {
                long expectedValue = expected.get(entry.getKey());
                long realValue = entry.getValue();
                System.out.println(
                    format("Comparing real value {0} with expected {1} for key  {2}", realValue, expectedValue,
                           entry.getKey()));
                if (expectedValue != realValue) {
                    throw new RuntimeException(
                        format("Expected value {0} while real value is {1} for key {2}", expectedValue, realValue,
                               entry.getKey()));
                }
            });
        System.out.println("Test successfully passed");
    }
}
