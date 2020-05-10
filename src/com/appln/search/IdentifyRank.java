package com.appln.search;

import java.util.*;
import java.util.stream.Collectors;
import static java.util.Map.Entry.comparingByValue;

/**
 * SearchRanker handles and manages all logic related to sorting and
 * relating the search results from FileRankers into a map that can
 * output the related order. This keeps the sorting logic separate
 * from the determination logic and allows both classes to be smaller
 * and more specialized
 *
 * The sorting is managed and handled by a pair of Collections
 * History is a HashTable that keeps prior results stored to try
 * and minimize the need for a stream and collection sort
 *
 * Sorting when needed is done by a Hashmap, which is streamed, ordered,
 * and then returned in the form of the proper format string of a list
 * of files with their relative 'hit' percentage.
 */
class IdentifyRank {
    private Map<String, Integer> searchMap;
    private Hashtable<String, String> history;

    IdentifyRank() {
        searchMap = new HashMap<>();
        history = new Hashtable<>();
    }

    /**
     * Adds a new entry to the search map
     * @param fileName the name of the file
     * @param value the percentage value represented as an integer
     */
    void addSearchEntry(final String fileName, final Integer value)
    {
        searchMap.put(fileName, value);
    }

    /**
     * Ranks all known file entries pertaining to a passed in line
     * @param line the string or phrase being ranked against
     * @return String formatted with each file name and the respective ranking
     */
    String rankFileSearch(final String line) {
        if(history.contains(line)) {
            return history.get(line);
        }

        Map<String,Integer> sorted = searchMap.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        StringBuilder sb = new StringBuilder();
        for (Map.Entry entry : sorted.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("%\n");
        }

        history.put(line, sb.toString());

        //flush the map to manage the size and avoid collision
        searchMap.clear();
        return sb.toString();
    }
}