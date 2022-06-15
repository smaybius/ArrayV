package io.github.arrayv.sorts.select;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class RankSortParallel extends Sort {
  public RankSortParallel(ArrayVisualizer arrayVisualizer) {
    super(arrayVisualizer);

    this.setSortListName("Rank (Parallel)");
    this.setRunAllSortsName("Rank Sort (Parallel)");
    this.setRunSortName("Rank Sort (Parallel)");
    this.setCategory("Selection Sorts");
    this.setBucketSort(false);
    this.setRadixSort(false);
    this.setUnreasonablySlow(false);
    this.setUnreasonableLimit(0);
    this.setBogoSort(false);
  }

  public class RankSortSorter implements Runnable {
    private int[] a;
    private int[] b;
    private int step;
    private int start;
    private int end;

    public RankSortSorter(int[] a, int step, int start, int end, int[] b) {
      this.a = a;
      this.step = step;
      this.start = start;
      this.end = end;
      this.b = b;
    }

    public void run() {
      RankSortParallel.this.rankSort(a, step, start, end, b);
    }
  }

  private void parallelSort(int[] a, int[] b, int end, int nrOfThreads) {
    if (nrOfThreads == 2) {
      Thread lThread = new Thread(new RankSortSorter(a, 2, 0, end, b));
      Thread rThread = new Thread(new RankSortSorter(a, 2, 1, end, b));

      lThread.start();
      rThread.start();
      try {
        lThread.join();
        rThread.join();
      } catch (InterruptedException ie) {
      }
    } else if (nrOfThreads == 4) {
      Thread lThread = new Thread(new RankSortSorter(a, 4, 0, end, b));
      Thread rThread = new Thread(new RankSortSorter(a, 4, 1, end, b));
      Thread l2Thread = new Thread(new RankSortSorter(a, 4, 2, end, b));
      Thread r2Thread = new Thread(new RankSortSorter(a, 4, 3, end, b));

      lThread.start();
      rThread.start();
      l2Thread.start();
      r2Thread.start();

      try {
        lThread.join();
        rThread.join();
        l2Thread.join();
        r2Thread.join();

      } catch (InterruptedException ie) {
      }
    }
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
    parallelSort(array, b, length, 4);
    Writes.arraycopy(b, 0, array, 0, length, 1, true, false);
    Writes.deleteExternalArray(b);
  }
}
