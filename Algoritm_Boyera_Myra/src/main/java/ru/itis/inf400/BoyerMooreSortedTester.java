package ru.itis.inf400;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BoyerMooreSortedTester {
    private static final String TEST_DIR = "boyer_moore_tests_sorted";
    private static final Random RANDOM = new Random();
    private static final String CHAR_POOL = "TGCA";

    public static void main(String[] args) {
        try {
            // 1. Запуск тестов
            List<TestResult> results = runTests();

            // 2. Сортировка результатов по textLength
            results.sort(Comparator.comparingInt(r -> r.textLength + r.patternLength));

            // 3. Вывод отсортированных результатов
            printResults(results);

            // 4. Сохранение в CSV
            saveResultsToCSV(results, "results_sorted.csv");

        } catch (IOException e) {
            System.err.println("Error during testing: " + e.getMessage());
        }
    }



    private static List<TestResult> runTests() throws IOException {
        File[] testFiles = new File(TEST_DIR).listFiles();
        List<TestResult> results = new ArrayList<>();
        if (testFiles == null) return results;

        for (File file : testFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String text = reader.readLine();
                String pattern = reader.readLine();

                // Прогрев JVM
                BoyerMoore warmupBM = new BoyerMoore(pattern);
                warmupBM.search(text);

                // Основной замер
                BoyerMoore bm = new BoyerMoore(pattern);
                long startTime = System.nanoTime();
                List<Integer> matches = bm.search(text);
                long duration = System.nanoTime() - startTime;

                results.add(new TestResult(
                        file.getName(),
                        text.length(),
                        pattern.length(),
                        matches.size(),
                        bm.getComparisonCount(),
                        duration
                ));
            }
        }
        return results;
    }

    private static void printResults(List<TestResult> results) {
        System.out.println("\nBoyer-Moore Algorithm Test Results (Sorted by Text Length)");
        System.out.println("==================================================================");
        System.out.printf("%-12s %-12s %-12s %-10s %-12s %-12s%n",
                "Test", "Text Len", "Pattern Len", "Matches", "Comparisons", "Time (ms)");
        System.out.println("------------------------------------------------------------------");

        for (TestResult result : results) {
            System.out.printf("%-12s %-12d %-12d %-10d %-12d %-12.3f%n",
                    result.testName,
                    result.textLength,
                    result.patternLength,
                    result.matchesCount,
                    result.comparisons,
                    result.timeNanos / 1_000_000.0);
        }

        //printStatistics(results);
    }

    private static void saveResultsToCSV(List<TestResult> results, String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(filename)) {
            writer.println("Test Name,Text Length,Pattern Length,Matches,Comparisons,Time (ms)");
            for (TestResult result : results) {
                writer.printf("%s,%d,%d,%d,%d,%.3f%n",
                        result.testName,
                        result.textLength,
                        result.patternLength,
                        result.matchesCount,
                        result.comparisons,
                        result.timeNanos / 1_000_000.0);
            }
        }
    }

    static class TestResult {
        final String testName;
        final int textLength;
        final int patternLength;
        final int matchesCount;
        final int comparisons;
        final long timeNanos;

        TestResult(String testName, int textLength, int patternLength,
                   int matchesCount, int comparisons, long timeNanos) {
            this.testName = testName;
            this.textLength = textLength;
            this.patternLength = patternLength;
            this.matchesCount = matchesCount;
            this.comparisons = comparisons;
            this.timeNanos = timeNanos;
        }
    }
}