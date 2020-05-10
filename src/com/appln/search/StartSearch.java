package com.appln.search;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class StartSearch {
	
    public static void main(String[] args) {
	    //can crawl domain based websites and store that dataset as well
    	//data sets directory path
	    String repoPath = "/Users/sukanyach/eclipse-workspace/JavaBasicSearch/src/com/appln/resources";
	    final File dirToSearch = new File(repoPath);
	    System.out.print("Fetch data");

        File[] files = dirToSearch.listFiles();
        if (files == null) {
            System.out.println("No data available");
            System.exit(0);
        }

        List<RankingAlgo> RankingAlgos = new LinkedList<>();
        for (File file : files) {
            RankingAlgos.add(new RankingAlgo(file.getAbsolutePath()));
        }
        System.out.println("done");

        IdentifyRank searchRanker = new IdentifyRank();
        Scanner in = new Scanner(System.in);
        while(true) {
            System.out.print("search>");
            final String line = in.nextLine();

            if(line.equals("quit")) {
                break;
            } else {
                rankFiles(line.toLowerCase(), searchRanker, RankingAlgos);
                String searchRank = searchRanker.rankFileSearch(line);
                System.out.println(searchRank);
            }
        }
        System.out.println("Exiting...");
    }

    private static void rankFiles(final String line, final IdentifyRank searchRanker, final List<RankingAlgo> RankingAlgos) {
        for (RankingAlgo ranker : RankingAlgos) {
            int ranking = ranker.getRanking(line);
            searchRanker.addSearchEntry(ranker.getFileName(), ranking);
        }
    }
}