package io.github.arrayv.sorts.extra;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

@SortMeta(name = "Better Comb")
final public class BetterCombSort extends Sort {
    public BetterCombSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    public void insertionSort(int[] array, int from, int to, double sleep, boolean auxwrite) throws Exception {

    }

    @Override
    public void runSort(int[] array, int length, int part) throws Exception {

        int[] copyArray = array.clone();
        boolean test = false;
        double sleep = test ? 0.01 : 1;

        Random random = new Random();

        // int[] offsetConst = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        // 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        // 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        // 1好
        // int[] offsetConst = new int[]{749, 400, 370, 236, 179, 134, 91, 74, 56, 47,
        // 37, 24, 17, 12, 9, 8, 6, 4, 3, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        // int[] offsetConst = new int[]{716, 550, 423, 325, 250, 192, 147, 113, 86, 66,
        // 50, 38, 29, 22, 16, 12, 9, 6, 4, 3, 2};

        // 2好
        // int[] offsetConst = new int[]{744, 635, 379, 241, 206, 191, 110, 86, 53, 40,
        // 31, 19, 15, 10, 7, 5, 4, 3, 2, 1};
        // int[] offsetConst = new int[]{1096, 729, 416, 299, 209, 184, 144, 83, 50, 48,
        // 36, 29, 21, 16, 10, 7, 5, 4, 3, 2, 1};
        // int[] offsetConst = new int[]{6642, 5817, 2742, 1680, 1056, 896, 616, 477,
        // 255, 231, 204, 137, 95, 62, 61, 38, 25, 21, 16, 10, 8, 5, 4, 3, 2, 1, 1, 1};

        LinkedList<Integer> tmp = new LinkedList<>(Arrays.asList(9599, 6861, 4762, 3697, 2451, 2207, 1518, 1152, 874,
                505, 409, 304, 186, 137, 125, 70, 50, 32, 21, 13, 9, 8, 6, 4, 3, 2, 1));
        // LinkedList<Integer> tmp = new LinkedList<>(Arrays.asList(9599, 6861, 4762,
        // 3697, 2451, 2207, 1518, 1152, 749, 400, 370, 236, 179, 134, 91, 74, 56, 47,
        // 37, 24, 17, 12, 9, 8, 6, 4, 3, 2, 1));

        while (tmp.get(0) < length / 2) {
            tmp.push((int) (tmp.get(0) * 1.35));
        }
        int[] offsetConst = tmp.stream().mapToInt(Integer::valueOf).toArray();

        int offsetMaxIndex = 0;
        // 获得更好的gap - 循环开始
        while (true) {
            // int[] offsetConst = new int[]{1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1};
            // int[] offsetConst = new int[]{1575, 1211, 931, 716, 550, 423, 325, 250, 192,
            // 147, 113, 86, 66, 50, 38, 29, 22, 16, 12, 9, 6, 4, 3, 2};

            int constIndex = 0;
            int bubbleSortedStartIndex = 0;
            int bubbleSortedEndIndex = length - 1;
            boolean firstOffset1 = true;
            while (true) {
                // int middle = (start + end) >> 1;
                // System.out.println("------ round = " + (length - end) + " ------");
                int swapTime = 0;
                int offset = constIndex < offsetConst.length ? offsetConst[constIndex++] : 1;
                if (offset != 1 || firstOffset1) {
                    if (offset == 1) {
                        firstOffset1 = false;
                    }
                    for (int i = bubbleSortedStartIndex; i < bubbleSortedEndIndex; i++) {
                        int index = i + offset;
                        if (index >= length) {
                            break;
                        }
                        if (Reads.compareIndices(array, i, index, 0, false) == 1) {
                            swapTime++;
                            Writes.swap(array, i, index, sleep, true, false);
                            // if (offset == 1) {
                            // start = start == bubbleSortedStartIndex ? Math.max(i - 1, 0) : start;
                            // end = Math.min(index, length - 1);
                            // }
                        }
                    }
                } else {
                    int start = bubbleSortedStartIndex;
                    int end = bubbleSortedEndIndex;
                    for (int i = bubbleSortedEndIndex; i > bubbleSortedStartIndex; i--) {
                        int index = i - 1;
                        if (Reads.compareIndices(array, i, index, sleep, true) == -1) {
                            swapTime++;
                            Writes.swap(array, i, index, sleep, true, false);
                            start = Math.max(index, 0);
                            end = end == bubbleSortedEndIndex ? Math.min(i + 1, length - 1) : end;
                        }
                    }
                    bubbleSortedStartIndex = start;
                    bubbleSortedEndIndex = end;
                }
                if (offset == 1 && swapTime == 0)
                    break;
                // if (swapTime != 0) {
                // System.out.print(decimalFormat.format(swapTime / (length - offset * 1.0)) +
                // ",");
                // } else {
                // System.out.print("0,");
                // }
                // Highlights.markArray(98, end - 1);
                // for (int i = end-1; i > start; i--) {
                // int index = random.nextInt(i - start) + start;
                // if (Reads.compareIndices(array, i, index, sleep, false) == -1) {
                // Writes.swap(array, i, index, sleep, true, false);
                // }
                // }
                // Highlights.markArray(99, start);
            }

            if (!test) {
                break;
            }

            // Shuffles.shuffle(copyArray, 0, length - 1, 0, Writes);

            Reads.resetStatistics();
            Writes.resetStatistics();

            while (true) {
                int randomIndex = random.nextInt(offsetMaxIndex + 1);
                int upperBound = randomIndex - 1 < 0 ? length - 1 : offsetConst[randomIndex - 1];
                int lowerBound = randomIndex + 1 >= offsetConst.length ? 1 : offsetConst[randomIndex + 1];
                int offset = random.nextInt(upperBound - lowerBound + 1) + lowerBound;
                offsetConst[randomIndex] = offset;
                // boolean plus = random.nextBoolean();
                // if (plus) {
                // offsetConst[randomIndex] = offsetConst[randomIndex] + offset;
                // if (randomIndex != 0 && offsetConst[randomIndex] >= offsetConst[randomIndex -
                // 1]) {
                // offsetConst[randomIndex] = offsetConst[randomIndex] - offset;
                // continue;
                // }
                // } else {
                // int preVal = offsetConst[randomIndex];
                // if (offsetConst[randomIndex] - offset <= 0) {
                // offsetConst[randomIndex] = 1;
                // } else {
                // offsetConst[randomIndex] = offsetConst[randomIndex] - offset;
                // }
                // if (randomIndex != offsetConst.length - 1 && offsetConst[randomIndex] <=
                // offsetConst[randomIndex + 1]) {
                // offsetConst[randomIndex] = preVal;
                // continue;
                // }
                // }
                if (randomIndex == offsetMaxIndex && offsetConst[randomIndex] != 1) {
                    offsetMaxIndex++;
                }
                break;
            }

            for (int i = 0; i < length; i++) {
                array[i] = copyArray[i];
            }

        }
    }
}
