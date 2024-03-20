package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SearchPear {
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        String targetWord = "Пьер";
        String fileName = "src/main/resources/Толстой Лев. Война и мир. Книга.txt";

        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<List<Integer>>> futures = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int lineIndex = 0;
            while ((line = reader.readLine()) != null) {
                String finalLine = line;
                int finalLineIndex = lineIndex;
                futures.add(executor.submit(() -> findWordInLine(finalLine, targetWord, finalLineIndex)));
                lineIndex++;
            }
        }

        List<Integer> indices = new ArrayList<>();
        for (Future<List<Integer>> future : futures) {
            indices.addAll(future.get());
        }

        executor.shutdown();

        System.out.println("Слово '" + targetWord + "' найдено " + indices.size() + " раз.");
        System.out.println("Индексы вхождений: " + indices);
    }

    private static List<Integer> findWordInLine(String line, String word, int lineIndex) {
        List<Integer> indices = new ArrayList<>();
        int index = 0;
        while ((index = line.indexOf(word, index)) != -1) {
            indices.add(lineIndex * 1000 + index);
            index += word.length();
        }
        return indices;
    }

}