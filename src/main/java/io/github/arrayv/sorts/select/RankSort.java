package io.github.arrayv.sorts.select;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class RankSort extends Sort {
  public RankSort(ArrayVisualizer arrayVisualizer) {
    super(arrayVisualizer);

    this.setSortListName("Rank");
    this.setRunAllSortsName("Rank Sort");
    this.setRunSortName("Rank Sort");
    this.setCategory("Selection Sorts");
    this.setBucketSort(false);
    this.setRadixSort(false);
    this.setUnreasonablySlow(false);
    this.setUnreasonableLimit(0);
    this.setBogoSort(false);
  }

  private void rankSort(int a[], int step, int start, int end, int[] b) {
    for (int i = start; i < end; i += step) {
      int index = 0;
      for (int j = 0; j < end; j++) {
        if (Reads.compareIndices(a, i, j, 1, true) > 0 || (Reads.compareIndices(a, i, j, 1, true) == 0 && (j < i))) {
          index++;
        }
      }
      Writes.write(b, index, a[i], 1, true, true);
    }
  }

  @Override
  public void runSort(int[] array, int length, int bucketCount) {
    int[] b = Writes.createExternalArray(length);
    rankSort(array, 1, 0, length, b);
    Writes.arraycopy(b, 0, array, 0, length, 1, true, false);
    Writes.deleteExternalArray(b);
  }
}
