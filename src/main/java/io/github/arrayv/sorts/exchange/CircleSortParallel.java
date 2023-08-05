package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.ParallelCircleSorting;

/*
 * 
MIT License

Copyright (c) 2021 aphitorite

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
@SortMeta(listName = "Circle (Parallel)", showcaseName = "Circlesort (Parallel)", runName = "Circlesort (Parallel)", unreasonableLimit = 4096)
final public class CircleSortParallel extends ParallelCircleSorting {
	public CircleSortParallel(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	@Override
	public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
		this.array = array;
		this.end = sortLength;
		this.swapped = true;
		int n = 1;
		for (; n < sortLength; n *= 2)
			;

		while (swapped) {
			this.swapCount = 0;
			swapped = false;
			this.circleSortRoutine(0, n, 1);
		}
	}
}