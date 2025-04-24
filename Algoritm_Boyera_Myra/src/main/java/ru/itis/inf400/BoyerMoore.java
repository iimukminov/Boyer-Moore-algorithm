package ru.itis.inf400;

import java.util.ArrayList;
import java.util.List;

public final class BoyerMoore {
    private final String pattern;
    private final int patternLength;
    private final BadCharacterRule badCharacterRule;
    private final GoodSuffixRule goodSuffixRule;
    private int comparisonCount;

    public BoyerMoore(String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern cannot be null or empty");
        }
        this.pattern = pattern;
        this.patternLength = pattern.length();
        this.badCharacterRule = new BadCharacterRule(pattern);
        this.goodSuffixRule = new GoodSuffixRule(pattern);
    }

    public List<Integer> search(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null");
        }

        List<Integer> matches = new ArrayList<>();
        comparisonCount = 0;
        final int textLength = text.length();

        if (textLength == 0 || textLength < patternLength) {
            return matches;
        }

        int index = 0; // Текущая позиция в тексте
        while (index <= textLength - patternLength) {
            int patternIndex = patternLength - 1;

            // Сравнение справа налево
            while (patternIndex >= 0 && pattern.charAt(patternIndex) == text.charAt(index + patternIndex)) {
                comparisonCount++;
                patternIndex--;
            }

            if (patternIndex < 0) {
                // Полное совпадение
                matches.add(index);
                index += goodSuffixRule.getShiftAfterFullMatch();
            } else {
                // Несовпадение
                comparisonCount++;
                char badChar = text.charAt(index + patternIndex);
                int badCharShift = badCharacterRule.getShift(badChar, patternIndex);
                int goodSuffixShift = goodSuffixRule.getShift(patternIndex);
                index += Math.max(badCharShift, goodSuffixShift);
            }
        }

        return matches;
    }

    public int getComparisonCount() {
        return comparisonCount;
    }

    public void resetComparisonCount() {
        comparisonCount = 0;
    }
}