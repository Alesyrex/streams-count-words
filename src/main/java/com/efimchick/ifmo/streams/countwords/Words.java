package com.efimchick.ifmo.streams.countwords;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Words {
    public static final String PAIR_FORMAT = " - ";
    public static final String LINE_SEPARATOR = "\n";
    public static final int MIN_REPEAT_WORDS = 10;
    private static final Pattern PATTERN_LONG_WORDS = Pattern.compile("\\p{IsAlphabetic}{4,}");
    private static final Pattern SPLIT_WORDS = Pattern.compile("[\\p{P}\\s]", Pattern.UNICODE_CHARACTER_CLASS);

    public String countWords(List<String> lines) {
        List<String> subString = lines.stream()
                .map(String::toLowerCase)
                .map(line -> Arrays.stream(SPLIT_WORDS.split(line))
                        .filter(word -> PATTERN_LONG_WORDS.matcher(word).matches())
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        Map<String, Integer> mapOfWords = subString.stream()
                .map(String::toLowerCase)
                .map(PATTERN_LONG_WORDS::matcher)
                .takeWhile(Matcher::find)
                .collect(Collectors.toMap(Matcher::group, value -> 1, Integer::sum, TreeMap::new));

        List<Map.Entry<String, Integer>> listOfWords = new ArrayList<>(mapOfWords.entrySet());

        return listOfWords.stream()
                .sorted((word1, word2) -> word2.getValue().compareTo(word1.getValue()))
                .filter(word -> word.getValue() >= MIN_REPEAT_WORDS)
                .map(word -> String.join(PAIR_FORMAT, word.getKey(), word.getValue().toString()))
                .collect(Collectors.joining(LINE_SEPARATOR));
    }
}
