package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SearchPear {
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        String targetWord = "Пьер";
        String fileName = "src/main/resources/Толстой Лев. Война и мир. Книга.txt";

        ExecutorService executor = Executors.newFixedThreadPool(10);
        Future<Integer> future = executor.submit(() -> countWordInFile(fileName, targetWord));

        int count = future.get();
        executor.shutdown();

        System.out.println("Word '" + targetWord + "' found " + count + " times in the file.");
    }

    private static int countWordInFile(String fileName, String word) throws IOException {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                count += countWordInLine(line, word);
            }
        }
        return count;
    }

    private static int countWordInLine(String line, String word) {
        int count = 0;
        int index = 0;
        while ((index = line.indexOf(word, index)) != -1) {
            count++;
            index += word.length();
        }
        return count;
    }

}