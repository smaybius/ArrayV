package io.github.arrayv.sorts.extra;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.insert.InsertionSort;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.m;

import java.util.Random;

/*
 * 
MIT License

Copyright (c) 2020-2021 Lu

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */
@SortMeta(name = "Random Swap Shrinking Range")
final public class RandomSwapShrinkingRangeSort extends Sort {
    private final double minComparisonWritesRatio = 1;
    private final double maxComparisonWritesRatio = 2.4;
    private final double rangeLengthRatio = 2.0 / 3;
    private double initRange;
    private double enterInsertionSortPercentage;

    public RandomSwapShrinkingRangeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int length, int threshold) {
        initRange = length * rangeLengthRatio;
        enterInsertionSortPercentage = getEnterInsertionSortPercentage(length);
        this.randomSwapShrinkingRangeSort(array, length, 0.05, false);
    }

    private void randomSwapShrinkingRangeSort(int[] array, final int length, double sleep, boolean auxwrite) {
        int comparison = 1;
        int write = 1;
        Random random = new Random();
        int insertionSortCountdown = Math.max(1, length / 10);

        while (true) {
            int randomRange = getRandomRange(comparison, write);
            if (insertionSortCountdown <= 0 && (initRange - randomRange) / initRange >= enterInsertionSortPercentage) {
                // System.out.println("WastedComparison = " + wastedComparison);
                // System.out.println("beforeInsertionSortComparison = " +
                // Reads.getComparisons());
                // System.out.println("beforeInsertionSortWrites = " + Writes.getWritesLong());
                // System.out.println("beforeInsertionSortWasteComparisonPercent = " +
                // (wastedComparison * 1.0 / Reads.getComparisons()));
                Highlights.clearAllMarks();
                InsertionSort insertionSort = new InsertionSort(arrayVisualizer);
                insertionSort.runSort(array, length, 0);
                break;
            } else {
                int left = random.nextInt(length);
                // 1. randomly pick right index with shrinking pick range, and equalize
                // the probability of selectable indices as possible.
                int right;
                if (left < randomRange && length - 1 - left < randomRange) {
                    right = random.nextInt(length - 1);
                    if (right >= left) {
                        right = right + 1;
                    }
                } else if (left < randomRange) {
                    right = random.nextInt(randomRange);
                    if (right >= left) {
                        right = left + 1 + random.nextInt(randomRange);
                    }
                } else if (length - 1 - left < randomRange) {
                    int i = random.nextInt(randomRange);
                    if (i < length - 1 - left) {
                        right = length - 1 - i;
                    } else {
                        right = left - 1 - random.nextInt(randomRange);
                    }
                } else {
                    boolean isAdd = random.nextBoolean();
                    right = isAdd ? left + 1 + random.nextInt(randomRange) : left - 1 - random.nextInt(randomRange);
                }

                // 2. randomly pick right index with shrinking pick range
                // boolean isAdd = random.nextBoolean();
                // int right = isAdd ? left + 1 + random.nextInt(randomRange) : left - 1 -
                // random.nextInt(randomRange);
                // if (right < 0) {
                // right = 0;
                // } else if (right >= length) {
                // right = length - 1;
                // }
                // if (left == right) {
                // continue;
                // }

                // 3. randomly pick right index
                // int right = random.nextInt(length);

                comparison++;
                if (left > right) {
                    if (Reads.compareIndices(array, left, right, sleep, true) < 0) {
                        Writes.swap(array, left, right, sleep, true, auxwrite);
                        write += 2;
                    } else {
                    }
                } else if (Reads.compareIndices(array, left, right, sleep, true) > 0) {
                    Writes.swap(array, left, right, sleep, true, auxwrite);
                    write += 2;
                } else {
                }
                insertionSortCountdown--;
            }
        }
    }

    public int getRandomRange(int comparison, int write) {
        // int tmp = (int) (averageRandomSwapTime(length) / (swap / 490.0 + 14)) - 200;
        // int tmp = (int) ((length >> 1) - (((length >> 1) - 1.0) /
        // (averageRandomSwapTime * 0.8)) * swap);
        double currentComparisonWritesRatio = comparison * 1.0 / write;
        int tmp = (int) (((1 - initRange) * (currentComparisonWritesRatio - minComparisonWritesRatio))
                / (maxComparisonWritesRatio - minComparisonWritesRatio) + initRange);
        return Math.max(tmp, 1);
    }

    public double getEnterInsertionSortPercentage(int length) {
        return -23.6845 * m.pow(length, -1.00559) + 1.00008;
    }
}