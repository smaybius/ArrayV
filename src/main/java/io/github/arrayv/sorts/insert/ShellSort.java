package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.ShellSorting;

// Shell sort variant retrieved from:
// https://www.cs.princeton.edu/~rs/talks/shellsort.ps
@SortMeta(name = "Shell", question = "Choose a gap sequence:\n1: Original\n2: Powers of 2 plus 1\n3: Powers of 2 minus 1\n"
        +
        "4: 3-Smooth\n5: Powers of 3\n6: Sedgewick-Incerpi\n7: Sedgewick\n8: Odd-Even Sedgewick\n9: Gonnet-Baeza-Yates\n"
        +
        "10: Tokada\n11: Ciura\n12 (default): Extended Ciura", defaultAnswer = 12)
public final class ShellSort extends ShellSorting {
    public ShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    public void finishQuickShell(int[] array, int currentLen) {
        this.quickShellSort(array, 0, currentLen);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.shellSort(array, currentLength, bucketCount);
    }
}
