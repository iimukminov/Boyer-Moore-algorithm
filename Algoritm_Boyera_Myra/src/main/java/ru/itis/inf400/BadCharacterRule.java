package ru.itis.inf400;

import java.util.Arrays;

public final class BadCharacterRule {
    private final int[] badCharTable;
    private final int patternLength;

    public BadCharacterRule(String pattern) {
        this.patternLength = pattern.length();
        this.badCharTable = new int[256];
        Arrays.fill(badCharTable, -1);

        // Заполняем таблицу последних вхождений
        for (int i = 0; i < patternLength; i++) {
            badCharTable[pattern.charAt(i)] = i;
        }
    }

    public int getShift(char badChar, int mismatchPosition) {
        // Сдвиг = позиция несовпадения - последнее вхождение символа в паттерне
        int lastOccurrence = badCharTable[badChar];
        return Math.max(1, mismatchPosition - lastOccurrence);
    }
}