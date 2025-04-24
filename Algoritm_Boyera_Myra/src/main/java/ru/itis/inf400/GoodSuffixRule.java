package ru.itis.inf400;

public final class GoodSuffixRule {
    private final int[] shiftTable;
    private final int[] borderTable;
    private final int patternLength;

    public GoodSuffixRule(String pattern) {
        this.patternLength = pattern.length();
        this.shiftTable = new int[patternLength + 1];
        this.borderTable = new int[patternLength + 1];
        preprocessPattern(pattern);
    }

    private void preprocessPattern(String pattern) {
        // Первый этап: обработка границ
        int i = patternLength;
        int j = patternLength + 1;
        borderTable[i] = j;

        while (i > 0) {
            while (j <= patternLength && pattern.charAt(i-1) != pattern.charAt(j-1)) {
                if (shiftTable[j] == 0) {
                    shiftTable[j] = j - i;
                }
                j = borderTable[j];
            }
            i--;
            j--;
            borderTable[i] = j;
        }

        // Второй этап: заполнение пропущенных значений
        j = borderTable[0];
        for (i = 0; i <= patternLength; i++) {
            if (shiftTable[i] == 0) {
                shiftTable[i] = j;
            }
            if (i == j) {
                j = borderTable[j];
            }
        }
    }

    public int getShift(int mismatchPosition) {
        return shiftTable[mismatchPosition + 1];
    }

    public int getShiftAfterFullMatch() {
        return shiftTable[0];
    }
}