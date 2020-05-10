package com.appln.search;

import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.Arrays;
import java.util.Hashtable;


class RankingAlgo {
    private File file;
    private Hashtable<String, Integer> indices;

    RankingAlgo(final String file) {
        this.file = new File(file);
       
        try {
        	if(this.file.exists())
            initializeTable(file);
        } catch (IOException e) {
            System.out.println("Search Datasets not available: " + file);
        }
    }

    private void initializeTable(String resourcesPath) throws IOException {
        indices = new Hashtable<>();
        try {
        	//file.getAbsolutePath()
            FileReader fileReader = new FileReader(resourcesPath);
            BufferedReader reader = new BufferedReader(fileReader);

            String line = null;
            while((line = reader.readLine()) != null) {
            	 line = removePunctuation(line);
                String[] tokens = line.split(" ");
                for (String token : tokens) {
                	if(token.length()>3) {
                    if(indices.contains(token)) {
                    	int updatecount = indices.get(token);
                        indices.put(token, updatecount++);
                    } else {
                        indices.put(token, 1);
                    }
                	}
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private String removePunctuation(String line) {
    	String punctuation = "!#$./\'%@^&*";
    	line = line.replaceAll(punctuation,""); 
    	line = line.replaceAll("\\s\\s","\\s"); 
		return line;
	}

	/**
     * @return The FileRanker's associated file name
     */
    String getFileName() {
        return file.getName();
    }

    
    int getRanking(String phrase) {
        String[] tokens = phrase.split(" ");

        int totalTokens = tokens.length;
        int tokensFound = 0;

        for (String token: tokens) {
            if(indices.containsKey(token)) {
                tokensFound++;
            }
        }

        int percent = (tokensFound * 100) / totalTokens;
        return Math.min(100, percent);
    }
}
