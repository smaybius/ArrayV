package io.github.arrayv.sorts.insert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.ArrayVList;

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
@SortMeta(name = "Patience")
public final class PatienceSort extends Sort {
	public PatienceSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	final private class Pile extends ArrayVList implements Comparable<Pile> {
		public Pile(boolean watch) {
			super(watch);
		}

		private static final long serialVersionUID = 1L;

		public int compare(Pile y) {
			return Reads.compareValues(peek(), y.peek());
		}

		@Override
		public int compareTo(Pile y) {
			return Reads.compareValues(peek(), y.peek());
		}
	}

	private void binarySearch(ArrayList<Pile> list, Pile find) {
		int at = list.size() / 2;
		int change = list.size() / 4;
		long compsBefore = Reads.getComparisons();
		while (list.get(at).compare(find) != 0 && change > 0) {
			Reads.setComparisons(compsBefore);
			Highlights.markArray(1, at);
			Delays.sleep(0.5);
			if (list.get(at).compare(find) < 0)
				at += change;
			else
				at -= change;

			change /= 2;
		}
		Reads.setComparisons(compsBefore);

		Highlights.markArray(1, at);
		Delays.sleep(0.5);
	}

	private ArrayVList flatpiles;

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		ArrayList<Pile> piles = new ArrayList<>();
		flatpiles = Writes.createMockExternalArray(length);
		// sort into piles
		for (int x = 0; x < length; x++) {
			Pile newPile = new Pile(false);
			Highlights.markArray(2, x);

			newPile.add(array[x]);

			int i = Collections.binarySearch(piles, newPile);
			if (!piles.isEmpty()) {
				this.binarySearch(piles, newPile);
			}
			if (i < 0)
				i = ~i;
			if (i != piles.size()) {
				piles.get(i).add(array[x]);
			} else {
				piles.add(newPile);
			}
			ArrayVList[] arrayPiles = new ArrayVList[piles.size()];
			Writes.fakeTranscribe(flatpiles, piles.toArray(arrayPiles), 0);
		}
		Highlights.clearMark(2);

		// priority queue allows us to retrieve least pile efficiently
		PriorityQueue<Pile> heap = new PriorityQueue<>(piles);
		ArrayVList flatheap = Writes.createMockExternalArray(length);
		for (int c = 0; c < length; c++) {
			Pile smallPile = heap.poll();
			Writes.write(array, c, smallPile.pop(), 1, true, false);

			if (!smallPile.isEmpty()) {
				Writes.mockWrite(length, Math.min(heap.size(), length - 1), smallPile.get(0), 0);
				heap.offer(smallPile);
			}
			ArrayVList[] arrayHeap = new ArrayVList[heap.size()];
			Writes.fakeTranscribe(flatheap, heap.toArray(arrayHeap), 0);
			ArrayVList[] arrayPiles = new ArrayVList[piles.size()];
			Writes.fakeTranscribe(flatpiles, piles.toArray(arrayPiles), 0);
		}
		Writes.deleteMockExternalArrays(flatheap, flatpiles);
		Writes.clearAllocAmount();
	}
}
