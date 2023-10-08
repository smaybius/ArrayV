package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.insert.InsertionSort;
import io.github.arrayv.sorts.templates.IterativeCircleSorting;

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
@SortMeta(listName = "Intro Circle (Iterative)", showcaseName = "Iterative Introspective Circle Sort", runName = "Iterative Introspective Circle Sort")
public final class IntroCircleSortIterative extends IterativeCircleSorting {
    public IntroCircleSortIterative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    public void circleSort(int[] array, int start, int length, double sleep) {
        this.end = length;
        int threshold = 0, n = start + 1;
        for (; n < length; n *= 2, threshold++)
            ;

        threshold /= 2;
        int iterations = 0;

        do {
            iterations++;

            if (iterations >= threshold) {
                Highlights.clearAllMarks();
                InsertionSort binaryInserter = new InsertionSort(this.arrayVisualizer);
                binaryInserter.customInsertSort(array, start, length, 0.5, false);
                break;
            }
        } while (this.circleSortRoutine(array, start, length, 1) != 0);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        circleSort(array, 0, length, 1);
    }
}
