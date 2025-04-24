package ru.itis.inf400;

import java.io.*;
import java.util.*;

public class TestDataGenerator {
    private static final String TEST_DIR = "boyer_moore_tests_sorted";
    private static final Random RANDOM = new Random();
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) {
        try {
            // 1. Генерация тестовых данных
            generateTestCases(100, 100, 10_000);
            System.out.println("Generated 100 test cases in bm_test_data/");

        } catch (IOException e) {
            System.err.println("Error during testing: " + e.getMessage());
        }
    }


    private static void generateTestCases(int numTests, int minLength, int maxLength) throws IOException {
        File dir = new File(TEST_DIR);
        if (!dir.exists()) dir.mkdir();

        // Генерируем длины текстов заранее для равномерного распределения
        List<Integer> sizes = new ArrayList<>();
        for (int i = 0; i < numTests; i++) {
            sizes.add(minLength + i * (maxLength - minLength) / numTests);
        }
        Collections.shuffle(sizes);

        for (int i = 0; i < numTests; i++) {
            String text = generateRandomString(sizes.get(i));
            String pattern = generateRandomString(RANDOM.nextInt(sizes.get(i) / 4) + 1);

            try (PrintWriter out = new PrintWriter(TEST_DIR + "/test_" + (i+1) + ".txt")) {
                out.println(text);
                out.println(pattern);
            }
        }
    }

    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHAR_POOL.charAt(RANDOM.nextInt(CHAR_POOL.length())));
        }
        return sb.toString();
    }

}