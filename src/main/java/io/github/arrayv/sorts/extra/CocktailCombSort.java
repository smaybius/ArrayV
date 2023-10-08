package io.github.arrayv.sorts.extra;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.exchange.OptimizedCocktailShakerSort;
import io.github.arrayv.sorts.templates.Sort;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

@SortMeta(name = "Cocktail Comb")
final public class CocktailCombSort extends Sort {
    public CocktailCombSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
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
        // int[] offsetConst = new int[]{964, 402, 319, 185, 146, 93, 65, 39, 36, 28,
        // 19, 15, 10, 4, 3, 2, 2, 1};
        // int[] offsetConst = new int[]{1008, 477, 306, 151, 110, 85, 68, 52, 28, 21,
        // 13, 11, 9, 6, 4, 3, 1};

        LinkedList<Integer> tmp = new LinkedList<Integer>(Arrays.asList(18100, 6453, 5116, 3215, 2498, 2129, 1494, 931,
                695, 445, 332, 269, 189, 127, 105, 85, 46, 30, 34, 19, 17, 11, 12, 7, 6, 4, 3, 2, 1));
        while (tmp.get(0) < length / 2) {
            tmp.push((int) (tmp.get(1) * 2.3));
        }
        int[] offsetConst = tmp.stream().mapToInt(Integer::valueOf).toArray();

        // int[] offsetConst = new int[]{1575, 1211, 931, 716, 550, 423, 325, 250, 192,
        // 147, 113, 86, 66, 50, 38, 29, 22, 16, 12, 9, 6, 4, 3, 2};

        int offsetMaxIndex = 0;
        // 获得更好的gap - 循环开始
        while (true) {
            int constIndex = 0;
            boolean firstOffset1 = true;
            boolean isForward = true;
            while (true) {
                int swapTime = 0;
                int offset = constIndex < offsetConst.length ? offsetConst[constIndex++] : 1;
                if (offset != 1 || firstOffset1) {
                    if (offset == 1) {
                        firstOffset1 = false;
                    }
                    if (isForward) {
                        for (int i = 0; i < length - 1; i++) {
                            int index = i + offset;
                            if (index >= length) {
                                break;
                            }
                            if (Reads.compareIndices(array, i, index, 0, true) == 1) {
                                swapTime++;
                                Writes.swap(array, i, index, sleep, true, false);
                            }
                        }
                    } else {
                        for (int i = length - 1; i > 0; i--) {
                            int index = i - offset;
                            if (index < 0) {
                                break;
                            }
                            if (Reads.compareIndices(array, i, index, 0, true) == -1) {
                                swapTime++;
                                Writes.swap(array, i, index, sleep, true, false);
                            }
                        }
                    }
                    isForward = !isForward;
                } else {
                    // int start = bubbleSortedStartIndex;
                    // int end = bubbleSortedEndIndex;
                    // for (int i = bubbleSortedEndIndex; i > bubbleSortedStartIndex; i--) {
                    // int index = i - 1;
                    // if (Reads.compareIndices(array, i, index, sleep, true) == -1) {
                    // swapTime++;
                    // Writes.swap(array, i, index, sleep, true, false);
                    // start = Math.max(index, 0);
                    // end = end == bubbleSortedEndIndex ? Math.min(i + 1, length - 1) : end;
                    // }
                    // }
                    // bubbleSortedStartIndex = start;
                    // bubbleSortedEndIndex = end;
                    OptimizedCocktailShakerSort optimizedCocktailShakerSort = new OptimizedCocktailShakerSort(
                            arrayVisualizer);
                    optimizedCocktailShakerSort.runSort(array, length, 0);
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
                int upperBound = randomIndex - 2 < 0 ? length - 1 : offsetConst[randomIndex - 2];
                int lowerBound = randomIndex + 2 >= offsetConst.length ? 1 : offsetConst[randomIndex + 2];
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
