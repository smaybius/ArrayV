package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.insert.InsertionSort;
import io.github.arrayv.sorts.templates.ParallelCircleSorting;

/*
 *
Copyright (c) rosettacode.org.
Permission is granted to copy, distribute and/or modify this document
under the terms of the GNU Free Documentation License, Version 1.2
or any later version published by the Free Software Foundation;
with no Invariant Sections, no Front-Cover Texts, and no Back-Cover
Texts.  A copy of the license is included in the section entitled "GNU
Free Documentation License".
 *
 */
@SortMeta(listName = "Intro Circle (Parallel)", runName = "Parallel Introspective Circle Sort")
public final class IntroCircleSortParallel extends ParallelCircleSorting {
    public IntroCircleSortParallel(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        this.array = array;
        this.end = sortLength;
        this.swapped = true;
        int iterations = 0;
        int threshold = 0, n = 1;
        for (; n < sortLength; n *= 2, threshold++)
            ;

        threshold /= 2;

        while (swapped) {
            this.swapCount = 0;
            swapped = false;
            iterations++;

            if (iterations >= threshold) {
                Highlights.clearAllMarks();
                InsertionSort binaryInserter = new InsertionSort(this.arrayVisualizer);
                binaryInserter.customInsertSort(array, 0, sortLength, 0.5, false);
                break;
            }
            this.circleSortRoutine(0, n, 1);
        }
    }
}
