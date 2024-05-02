package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.ShellSorting;

@SortMeta(listName = "Shell (Parallel)", runName = "Shell Sort (Parallel)", question = "Choose a gap sequence:\n1: Original\n2: Powers of 2 plus 1\n3: Powers of 2 minus 1\n"
		+
		"4: 3-Smooth\n5: Powers of 3\n6: Sedgewick-Incerpi\n7: Sedgewick\n8: Odd-Even Sedgewick\n9: Gonnet-Baeza-Yates\n"
		+
		"10: Tokada\n11: Ciura\n12 (default): Extended Ciura", defaultAnswer = 12)
public final class ShellSortParallel extends ShellSorting {
	public ShellSortParallel(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	private int[] array;
	private int[] gaps;

	private class GappedInsertionSort extends Thread {
		private int a, b, g;

		GappedInsertionSort(int a, int b, int g) {
			this.a = a;
			this.b = b;
			this.g = g;
		}

		@Override
		public void run() {
			ShellSortParallel.this.gappedInsertion(a, b, g);
		}
	}

	private void gappedInsertion(int a, int b, int g) {
		for (int i = a + g; i < b; i += g) {
			if (Reads.compareIndices(this.array, i - g, i, 1, true) > 0) {
				int tmp = this.array[i], j = i;
				Highlights.clearMark(2);

				do {
					Writes.write(this.array, j, this.array[j - g], 1, true, false);
					j -= g;
				} while (j - g >= a && Reads.compareValues(this.array[j - g], tmp) > 0);

				Writes.write(this.array, j, tmp, 1, true, false);
			}
		}
	}

	@Override
	public void runSort(int[] array, int currentLength, int bucketCount) {
		this.array = array;
		switch (bucketCount) {
			case 1:
				this.gaps = OriginalGaps;
				break;
			case 2:
				this.gaps = PowTwoPlusOneGaps;
				break;
			case 3:
				this.gaps = PowTwoMinusOneGaps;
				break;
			case 4:
				this.gaps = ThreeSmoothGaps;
				break;
			case 5:
				this.gaps = PowersOfThreeGaps;
				break;
			case 6:
				this.gaps = SedgewickIncerpiGaps;
				break;
			case 7:
				this.gaps = SedgewickGaps;
				break;
			case 8:
				this.gaps = OddEvenSedgewickGaps;
				break;
			case 9:
				this.gaps = GonnetBaezaYatesGaps;
				break;
			case 10:
				this.gaps = TokudaGaps;
				break;
			case 11:
				this.gaps = CiuraGaps;
				break;
			default:
				this.gaps = ExtendedCiuraGaps;
				break;
		}

		int k = 0;

		for (; this.gaps[k] >= currentLength; k++)
			;
		for (; k < this.gaps.length; k++) {
			int g = this.gaps[k];
			int t = Math.min(g, currentLength - g);

			GappedInsertionSort[] ins = new GappedInsertionSort[t];
			for (int i = 0; i < t; i++)
				ins[i] = new GappedInsertionSort(i, currentLength, g);

			for (GappedInsertionSort s : ins)
				s.start();
			for (GappedInsertionSort s : ins) {
				try {
					s.join();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}
}
