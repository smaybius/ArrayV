package io.github.arrayv.utils;

import io.github.arrayv.main.ArrayVisualizer;

/**
 *
 * Generates inputs that cause the worst case for quicksort variants.
 * https://www.cs.dartmouth.edu/~doug/mdmspe.pdf
 *
 * @author M. D. McIlroy
 */
public class AntiQSort {
    private ArrayVisualizer arrayVisualizer;
    private int[] data;
    private int nmemb;
    private int gas;
    private int frozen;

    private boolean hasCandidate;
    private int candidate;

    /**
     *
     * Loads the ArrayVisualizer instance
     *
     * @param arrayVisualizer automatic
     */
    public AntiQSort(ArrayVisualizer arrayVisualizer) {
        this.arrayVisualizer = arrayVisualizer;
    }

    /**
     *
     * ???
     *
     * @param ap Left
     * @param bp Right
     * @return the comparison result
     */
    public int compare(int ap, int bp) {
        int a;
        int b;
        if (!this.hasCandidate) {
            this.candidate = 0;
            this.hasCandidate = true;
        }

        a = ap;
        b = bp;

        if (data[a] == gas && data[b] == gas) {
            if (a == candidate)
                data[a] = frozen++;
            else
                data[b] = frozen++;
        }

        if (data[a] == gas) {
            candidate = a;
            return 1;
        }

        if (data[b] == gas) {
            candidate = b;
            return -1;
        }

        if (data[a] < data[b])
            return -1;
        if (data[a] > data[b])
            return 1;
        return 0;
    }

    /**
     *
     * Starts the whole thing
     *
     * @param refs  The input array
     * @param nmemb Length
     */
    public void beginSort(int[] refs, int nmemb) {
        this.hasCandidate = false;
        this.frozen = 1;

        this.nmemb = nmemb;
        this.data = arrayVisualizer.getWrites().createExternalArray(nmemb);
        arrayVisualizer.getWrites().changeAllocAmount(-nmemb);
        this.gas = nmemb;
        for (int i = 0; i < nmemb; i++) {
            refs[i] = i;
            data[i] = gas;
        }
    }

    /**
     *
     * Loads the ArrayVisualizer instance
     *
     * @return The array being processed
     */
    public int[] getResult() {
        return this.data;
    }

    /**
     *
     * Keep it secret, keep it safe
     *
     */
    public void hideResult() {
        arrayVisualizer.getWrites().changeAllocAmount(nmemb);
        arrayVisualizer.getWrites().deleteExternalArray(this.data);
    }
}
