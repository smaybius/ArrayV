package io.github.arrayv.utils;

import io.github.arrayv.main.ArrayVisualizer;

public final class Searches {
    private static final Delays Delays = ArrayVisualizer.getInstance().getDelays();
    private static final Highlights Highlights = ArrayVisualizer.getInstance().getHighlights();
    private static final Reads Reads = ArrayVisualizer.getInstance().getReads();
    private static final Writes Writes = ArrayVisualizer.getInstance().getWrites();

    private Searches() {
    }

    public static int leftBinSearch(int[] array, int start, int end, int val, double delay) {
        while (start < end) {
            int mid = start + (end - start) / 2;
            Highlights.markArray(1, start);
            Highlights.markArray(2, mid);
            Highlights.markArray(3, end);
            Delays.sleep(delay);
            if (Reads.compareValues(val, array[mid]) <= 0)
                end = mid;
            else
                start = mid + 1;
        }
        Highlights.clearAllMarks();
        return start;
    }

    public static int rightBinSearch(int[] array, int start, int end, int val, double delay) {
        while (start < end) {
            int mid = start + (end - start) / 2;
            Highlights.markArray(1, start);
            Highlights.markArray(2, mid);
            Highlights.markArray(3, end);

            Delays.sleep(delay);
            if (Reads.compareValues(val, array[mid]) < 0)
                end = mid;
            else
                start = mid + 1;
        }
        Highlights.clearAllMarks();
        return start;
    }

    public static int leftExpSearch(int[] array, int start, int end, int val, double delay) {
        int position = 1;
        Highlights.markArray(1, start);
        Highlights.markArray(2, end);
        while (start - 1 + position < end && Reads.compareValues(val, array[start - 1 + position]) > 0) {
            Highlights.markArray(3, start - 1 + position);
            Highlights.markArray(4, position);
            Delays.sleep(delay);
            position *= 2;
        }
        Highlights.clearAllMarks();
        return leftBinSearch(array, start + position / 2, Math.min(end, start - 1 + position), val, delay);
    }

    public static int rightExpSearch(int[] array, int start, int end, int val, double delay) {
        int position = 1;
        while (end - position >= start && Reads.compareValues(val, array[end - position]) < 0) {
            Highlights.markArray(3, end - position);
            Highlights.markArray(4, position);
            Delays.sleep(delay);
            position *= 2;
        }
        Highlights.clearAllMarks();
        return rightBinSearch(array, Math.max(start, end - position + 1), end - position / 2, val, delay);
    }

    public static int rightIntpSearch(int[] array, int start, int end, int val, double searchdelay) {
        while (start < end) {
            int min = array[start];
            int max = array[end - 1];

            if (min == max) {
                Highlights.markArray(2, start);
                Highlights.markArray(3, end);
                Delays.sleep(searchdelay);

                if (Reads.compareValues(val, min) < 0)
                    end = start;
                else
                    start = end;
            } else {
                int m = start
                        + (int) ((end - start - 1) * ((double) Math.max(0, Math.min(val, max) - min) / (max - min)));
                Highlights.markArray(2, m);
                Delays.sleep(searchdelay);

                if (Reads.compareValues(val, array[m]) < 0)
                    end = m;
                else
                    start = m + 1;
            }
        }
        Highlights.clearAllMarks();
        return start;
    }

    public static void insertTo(int[] array, int start, int end, double delay, boolean auxwrite) {
        int temp = array[start];
        while (start > end)
            Writes.write(array, start, array[--start], delay / 2, true, auxwrite);
        Writes.write(array, end, temp, delay / 2, true, auxwrite);
    }
}
