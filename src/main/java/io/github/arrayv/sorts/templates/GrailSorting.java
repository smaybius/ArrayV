/*
 *
The MIT License (MIT)

Copyright (c) 2013 Andrey Astrelin

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

/********* Grail sorting *********************************/
/*                                                       */
/* (c) 2013 by Andrey Astrelin                           */
/* Refactored by MusicTheorist                           */
/*                                                       */
/* Stable sorting that works in O(N*log(N)) worst time   */
/* and uses O(1) extra memory                            */
/*                                                       */
/* Define int / SortComparator                           */
/* and then call GrailSort() function                    */
/*                                                       */
/* For sorting w/ fixed external buffer (512 items)      */
/* use GrailSortWithBuffer()                             */
/*                                                       */
/* For sorting w/ dynamic external buffer (sqrt(length)) */
/* use GrailSortWithDynBuffer()                          */
/*                                                       */
/*********************************************************/
package io.github.arrayv.sorts.templates;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.insert.BlockInsertionSort;
import io.github.arrayv.utils.Rotations;

// Credit to phoenixbound for this clever idea
enum Subarray {
    LEFT,
    RIGHT;
}

public abstract class GrailSorting extends Sort {
    private int currBlockLen;
    private Subarray currBlockOrigin;

    protected GrailSorting(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void grailSwap(int[] array, int a, int b) {
        Writes.swap(array, a, b, 1, true, false);
    }

    private void grailBlockSwap(int[] array, int a, int b, int blockLen) {
        for (int i = 0; i < blockLen; i++) {
            grailSwap(array, a + i, b + i);
        }
    }

    // Swaps the order of two adjacent blocks whose lengths may or may not be equal.
    // Variant of the Gries-Mills algorithm, which is basically recursive block
    // swaps.
    public void grailRotate(int[] array, int start, int leftLen, int rightLen) {
        Rotations.cycleReverse(array, start, leftLen, rightLen, 0.5, true, false);
    }

    // Variant of Insertion Sort that utilizes swaps instead of overwrites.
    // Also known as "Optimized Gnomesort".
    private void grailInsertSort(int[] array, int start, int length) {
        BlockInsertionSort grailInsertSorter = new BlockInsertionSort(arrayVisualizer);
        grailInsertSorter.customInsertSort(array, start, length, 1, false);
    }

    // return Searches.rightBinSearch(array, start, length, target, 0);
    private int grailBinarySearchLeft(int[] array, int start, int length, int target) {
        int left = 0;
        int right = length;

        while (left < right) {
            // equivalent to (left + right) / 2 with added overflow protection
            int middle = left + ((right - left) / 2);

            if (Reads.compareValues(array[start + middle], target) < 0) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }
        return left;
    }

    // Credit to Anonymous0726 for debugging
    private int grailBinarySearchRight(int[] array, int start, int length, int target) {
        int left = 0;
        int right = length;

        while (left < right) {
            // equivalent to (left + right) / 2 with added overflow protection
            int middle = left + ((right - left) / 2);

            if (Reads.compareValues(array[start + middle], target) > 0) {
                right = middle;
            } else {
                left = middle + 1;
            }
        }
        // OFF-BY-ONE BUG FIXED: used to be `return right - 1;`
        return right;
    }

    // cost: 2 * length + idealKeys^2 / 2
    public int grailCollectKeys(int[] array, int start, int length, int idealKeys) {
        int keysFound = 1; // by itself, the first item in the array is our first unique key
        int firstKey = 0; // the first item in the array is at the first position in the array
        int currKey = 1; // the index used for finding potentially unique items ("keys") in the array

        while (currKey < length && keysFound < idealKeys) {

            // Find the location in the key-buffer where our current key can be inserted in
            // sorted order.
            // If the key at insertPos is equal to currKey, then currKey isn't unique and we
            // move on.
            int insertPos = grailBinarySearchLeft(array, start + firstKey, keysFound, array[start + currKey]);

            // The second part of this conditional does the equal check we were just talking
            // about; however,
            // if currKey is larger than everything in the key-buffer (meaning insertPos ==
            // keysFound),
            // then that also tells us it wasn't *equal* to anything in the key-buffer.
            // Magic! :)
            if (insertPos == keysFound || Reads.compareIndices(array, start + currKey,
                    start + firstKey + insertPos, 0.2, true) != 0) {

                // First, rotate the key-buffer over to currKey's immediate left...
                // (this helps save a TON of swaps/writes!!!)
                grailRotate(array, start + firstKey, keysFound, currKey - (firstKey + keysFound));

                // Update the new position of firstKey...
                firstKey = currKey - keysFound;

                // Then, "insertion sort" currKey to its spot in the key-buffer!
                grailRotate(array, start + firstKey + insertPos, keysFound - insertPos, 1);

                // One step closer to idealKeys.
                keysFound++;
            }
            // Move on and test the next key...
            currKey++;
        }

        // Bring however many keys we found back to the beginning of our array,
        // and return the number of keys collected.
        grailRotate(array, start, firstKey, keysFound);
        return keysFound;
    }

    private void grailPairwiseSwaps(int[] array, int start, int length) {
        int index;
        for (index = 1; index < length; index += 2) {
            int left = start + index - 1;
            int right = start + index;

            if (Reads.compareIndices(array, left, right, 0.1, true) > 0) {
                grailSwap(array, left - 2, right);
                grailSwap(array, right - 2, left);
            } else {
                grailSwap(array, left - 2, left);
                grailSwap(array, right - 2, right);
            }
        }

        int left = start + index - 1;
        if (left < start + length) {
            grailSwap(array, left - 2, left);
        }
    }

    // array[buffer .. start - 1] <=> "scrolling buffer"
    //
    // "scrolling buffer" + array[start, middle - 1] + array[middle, end - 1]
    // --> array[buffer, buffer + end - 1] + "scrolling buffer"
    private void grailMergeForwards(int[] array, int start, int leftLen, int rightLen,
            int bufferOffset) {
        int buffer = start - bufferOffset;
        int left = start;
        int middle = start + leftLen;
        int right = middle;
        int end = middle + rightLen;

        while (right < end) {
            if (left == middle || Reads.compareIndices(array, left, right, 0.1, true) > 0) {
                grailSwap(array, buffer, right);
                right++;
            } else {
                grailSwap(array, buffer, left);
                left++;
            }
            buffer++;
        }

        if (buffer != left) {
            grailBlockSwap(array, buffer, left, middle - left);
        }
    }

    // credit to 666666t for thorough bug-checking/fixing
    private void grailMergeBackwards(int[] array, int start, int leftLen, int rightLen,
            int bufferOffset) {
        // used to be '= start'
        int end = start - 1;
        // used to be '= start + leftLen - 1'
        int left = end + leftLen;
        int middle = left;
        // OFF-BY-ONE BUG FIXED: used to be `int right = middle + rightLen - 1;`
        int right = middle + rightLen;
        // OFF-BY-ONE BUG FIXED: used to be `int buffer = right + bufferOffset - 1;`
        int buffer = right + bufferOffset;

        // used to be 'left >= end'
        while (left > end) {
            if (right == middle || Reads.compareIndices(array, left, right, 0.1, true) > 0) {
                grailSwap(array, buffer, left);
                left--;
            } else {
                grailSwap(array, buffer, right);
                right--;
            }
            buffer--;
        }

        if (right != buffer) {
            while (right > middle) {
                grailSwap(array, buffer, right);
                buffer--;
                right--;
            }
        }
    }

    // array[buffer .. start - 1] <=> "free space"
    //
    // "free space" + array[start, middle - 1] + array[middle, end - 1]
    // --> array[buffer, buffer + end - 1] + "free space"
    //
    // FUNCTION RENAMED: More consistent with "out-of-place" being at the end

    private void grailBuildInPlace(int[] array, int start, int length, int currentLen, int bufferLen) {
        for (int mergeLen = currentLen; mergeLen < bufferLen; mergeLen *= 2) {
            int fullMerge = 2 * mergeLen;

            int mergeIndex;
            int mergeEnd = start + length - fullMerge;
            int bufferOffset = mergeLen;

            for (mergeIndex = start; mergeIndex <= mergeEnd; mergeIndex += fullMerge) {
                grailMergeForwards(array, mergeIndex, mergeLen, mergeLen, bufferOffset);
            }

            int leftOver = length - (mergeIndex - start);

            if (leftOver > mergeLen) {
                grailMergeForwards(array, mergeIndex, mergeLen, leftOver - mergeLen, bufferOffset);
            } else {
                grailRotate(array, mergeIndex - mergeLen, mergeLen, leftOver);
            }

            start -= mergeLen;
        }

        int fullMerge = 2 * bufferLen;
        int lastBlock = length % fullMerge;
        int lastOffset = start + length - lastBlock;

        if (lastBlock <= bufferLen) {
            grailRotate(array, lastOffset, lastBlock, bufferLen);
        } else {
            grailMergeBackwards(array, lastOffset, bufferLen, lastBlock - bufferLen, bufferLen);
        }

        for (int mergeIndex = lastOffset - fullMerge; mergeIndex >= start; mergeIndex -= fullMerge) {
            grailMergeBackwards(array, mergeIndex, bufferLen, bufferLen, bufferLen);
        }
    }

    // build blocks of length 'bufferLen'
    // input: [start - mergeLen, start - 1] elements are buffer
    // output: first 'bufferLen' elements are buffer, blocks (2 * bufferLen) and
    // last subblock sorted
    private void grailBuildBlocks(int[] array, int start, int length, int bufferLen) {

        grailPairwiseSwaps(array, start, length);
        grailBuildInPlace(array, start - 2, length, 2, bufferLen);
    }

    // Returns the final position of 'medianKey'.
    // MINOR CHANGES: Change comparison order to emphasize "less-than" relation;
    // fewer variables (Credit to Anonymous0726 for better variable names!)
    private int grailBlockSelectSort(int[] array, int firstKey, int start, int medianKey,
            int blockCount, int blockLen) {
        for (int firstBlock = 0; firstBlock < blockCount; firstBlock++) {
            int selectBlock = firstBlock;

            for (int currBlock = firstBlock + 1; currBlock < blockCount; currBlock++) {
                int compare = Reads.compareIndices(array, start + (currBlock * blockLen),
                        start + (selectBlock * blockLen), 0.5, true);

                if (compare < 0 || (compare == 0 && Reads.compareIndices(array, firstKey + currBlock,
                        firstKey + selectBlock, 0.5, true) < 0)) {
                    selectBlock = currBlock;
                }
            }

            if (selectBlock != firstBlock) {
                // Swap the left and right selected blocks...
                grailBlockSwap(array, start + (firstBlock * blockLen), start + (selectBlock * blockLen), blockLen);

                // Swap the keys...
                grailSwap(array, firstKey + firstBlock, firstKey + selectBlock);

                // ...and follow the 'medianKey' if it was swapped

                // ORIGINAL LOC: if(midkey==u-1 || midkey==p) midkey^=(u-1)^p;
                // MASSIVE, MASSIVE credit to lovebuny for figuring this one out!
                if (medianKey == firstBlock) {
                    medianKey = selectBlock;
                } else if (medianKey == selectBlock) {
                    medianKey = firstBlock;
                }
            }
        }

        return medianKey;
    }

    // Swaps Grailsort's "scrolling buffer" from the right side of the array all the
    // way back to 'start'.
    // Costs O(n) swaps.
    //
    // OFF-BY-ONE BUG FIXED: used to be `int index = start + resetLen`; credit to
    // 666666t for debugging
    // RESTRUCTED, BETTER NAMES: 'resetLen' is now 'length' and 'bufferLen' is now
    // 'bufferOffset'
    // SWAPPED NAMES: 'buffer' is now 'index' and vice versa
    private void grailInPlaceBufferReset(int[] array, int start, int length, int bufferOffset) {
        int buffer = start + length - 1;
        int index = buffer - bufferOffset;

        while (buffer >= start) {
            grailSwap(array, index, buffer);
            buffer--;
            index--;
        }
    }

    // Rewinds Grailsort's "scrolling buffer" to the left of any items belonging to
    // the left subarray block
    // left over by a "smart merge". This is used to continue an ongoing merge that
    // has run out of buffer space.
    // Costs O(sqrt n) swaps in the *absolute* worst-case.
    //
    // BETTER ORDER-OF-OPERATIONS, NAMING IMPROVED: the left over items (now called
    // 'leftBlock') are in the
    // middle of the merge while the buffer is at the end
    private void grailInPlaceBufferRewind(int[] array, int start, int leftBlock, int buffer) {
        while (leftBlock >= start) {
            grailSwap(array, buffer, leftBlock);
            leftBlock--;
            buffer--;
        }
    }

    // Rewinds Grailsort's out-of-place buffer to the left of any items belonging to
    // the left subarray block
    // left over by a "smart merge". This is used to continue an ongoing merge that
    // has run out of buffer space.
    // Costs O(sqrt n) writes in the *absolute* worst-case.
    //
    // BETTER ORDER, INCORRECT ORDER OF PARAMETERS BUG FIXED: `leftOvers` (now
    // called 'leftBlock') should be
    // the middle, and `buffer` should be the end

    private Subarray grailGetSubarray(int[] array, int currentKey, int medianKey) {
        if (Reads.compareIndices(array, currentKey, medianKey, 0.1, true) < 0) {
            return Subarray.LEFT;
        } else {
            return Subarray.RIGHT;
        }
    }

    // FUNCTION RE-RENAMED: last/final left blocks are used to calculate the length
    // of the final merge
    private int grailCountLastMergeBlocks(int[] array, int offset, int blockCount, int blockLen) {
        int blocksToMerge = 0;

        int lastRightFrag = offset + (blockCount * blockLen);
        int prevLeftBlock = lastRightFrag - blockLen;

        while (blocksToMerge < blockCount && Reads.compareIndices(array, lastRightFrag, prevLeftBlock, 0.1, true) < 0) {
            blocksToMerge++;
            prevLeftBlock -= blockLen;
        }

        return blocksToMerge;
    }

    private void grailSmartMerge(int[] array, int start, int leftLen, Subarray leftOrigin,
            int rightLen, int bufferOffset) {
        int buffer = start - bufferOffset;
        int left = start;
        int middle = start + leftLen;
        int right = middle;
        int end = middle + rightLen;

        if (leftOrigin == Subarray.LEFT) {
            while (left < middle && right < end) {
                if (Reads.compareIndices(array, left, right, 0.1, true) <= 0) {
                    grailSwap(array, buffer, left);
                    left++;
                } else {
                    grailSwap(array, buffer, right);
                    right++;
                }
                buffer++;
            }
        } else {
            while (left < middle && right < end) {
                if (Reads.compareIndices(array, left, right, 0.1, true) < 0) {
                    grailSwap(array, buffer, left);
                    left++;
                } else {
                    grailSwap(array, buffer, right);
                    right++;
                }
                buffer++;
            }
        }

        if (left < middle) {
            this.currBlockLen = middle - left;
            // UPDATED ARGUMENTS: 'middle' and 'end' now 'middle - 1' and 'end - 1'
            grailInPlaceBufferRewind(array, left, middle - 1, end - 1);
        } else {
            this.currBlockLen = end - right;
            if (leftOrigin == Subarray.LEFT) {
                this.currBlockOrigin = Subarray.RIGHT;
            } else {
                this.currBlockOrigin = Subarray.LEFT;
            }
        }
    }

    // MINOR CHANGE: better naming -- 'insertPos' is now 'mergeLen' -- and "middle"
    // calculation simplified
    private void grailSmartLazyMerge(int[] array, int start, int leftLen, Subarray leftOrigin, int rightLen) {
        int middle = start + leftLen;

        if (leftOrigin == Subarray.LEFT) {
            if (Reads.compareIndices(array, middle - 1, middle, 0.1, true) > 0) {
                while (leftLen != 0) {
                    int mergeLen = grailBinarySearchLeft(array, middle, rightLen, array[start]);

                    if (mergeLen != 0) {
                        grailRotate(array, start, leftLen, mergeLen);

                        start += mergeLen;
                        middle += mergeLen;
                        rightLen -= mergeLen;
                    }

                    if (rightLen == 0) {
                        this.currBlockLen = leftLen;
                        return;
                    } else {
                        do {
                            start++;
                            leftLen--;
                        } while (leftLen != 0 && Reads.compareIndices(array, start, middle, 0.1, true) <= 0);
                    }
                }
            }
        } else {
            if (Reads.compareIndices(array, middle - 1, middle, 0.1, true) >= 0) {
                while (leftLen != 0) {
                    int mergeLen = grailBinarySearchRight(array, middle, rightLen, array[start]);

                    if (mergeLen != 0) {
                        grailRotate(array, start, leftLen, mergeLen);

                        start += mergeLen;
                        middle += mergeLen;
                        rightLen -= mergeLen;
                    }

                    if (rightLen == 0) {
                        this.currBlockLen = leftLen;
                        return;
                    } else {
                        do {
                            start++;
                            leftLen--;
                        } while (leftLen != 0 && Reads.compareIndices(array, start, middle, 0.1, true) < 0);
                    }
                }
            }
        }

        this.currBlockLen = rightLen;
        if (leftOrigin == Subarray.LEFT) {
            this.currBlockOrigin = Subarray.RIGHT;
        } else {
            this.currBlockOrigin = Subarray.LEFT;
        }
    }

    // Credit to Anonymous0726 for better variable names such as "nextBlock"
    // Also minor change: removed unnecessary "currBlock = nextBlock" lines
    private void grailMergeBlocks(int[] array, int firstKey, int medianKey, int start,
            int blockCount, int blockLen, int lastMergeBlocks,
            int lastLen) {
        int buffer;

        int currBlock;
        int nextBlock = start + blockLen;

        this.currBlockLen = blockLen;
        this.currBlockOrigin = grailGetSubarray(array, firstKey, medianKey);

        for (int keyIndex = 1; keyIndex < blockCount; keyIndex++, nextBlock += blockLen) {
            Subarray nextBlockOrigin;

            currBlock = nextBlock - this.currBlockLen;
            nextBlockOrigin = grailGetSubarray(array, firstKey + keyIndex, medianKey);

            if (nextBlockOrigin == this.currBlockOrigin) {
                buffer = currBlock - blockLen;

                grailBlockSwap(array, buffer, currBlock, this.currBlockLen);
                this.currBlockLen = blockLen;
            } else {
                this.grailSmartMerge(array, currBlock, this.currBlockLen, this.currBlockOrigin,
                        blockLen, blockLen);
            }
        }

        currBlock = nextBlock - this.currBlockLen;
        buffer = currBlock - blockLen;

        if (lastLen != 0) {
            if (this.currBlockOrigin == Subarray.RIGHT) {
                grailBlockSwap(array, buffer, currBlock, this.currBlockLen);

                currBlock = nextBlock;
                this.currBlockLen = blockLen * lastMergeBlocks;
                this.currBlockOrigin = Subarray.LEFT;
            } else {
                this.currBlockLen += blockLen * lastMergeBlocks;
            }

            grailMergeForwards(array, currBlock, this.currBlockLen, lastLen, blockLen);
        } else {
            grailBlockSwap(array, buffer, currBlock, this.currBlockLen);
        }
    }

    private void grailLazyMergeBlocks(int[] array, int firstKey, int medianKey, int start,
            int blockCount, int blockLen, int lastMergeBlocks,
            int lastLen) {
        int currBlock;
        int nextBlock = start + blockLen;

        this.currBlockLen = blockLen;
        this.currBlockOrigin = grailGetSubarray(array, firstKey, medianKey);

        for (int keyIndex = 1; keyIndex < blockCount; keyIndex++, nextBlock += blockLen) {
            Subarray nextBlockOrigin;

            currBlock = nextBlock - this.currBlockLen;
            nextBlockOrigin = grailGetSubarray(array, firstKey + keyIndex, medianKey);

            if (nextBlockOrigin == this.currBlockOrigin) {
                this.currBlockLen = blockLen;
            } else {
                // These checks were included in the original code... but why???
                if (blockLen != 0 && this.currBlockLen != 0) {
                    this.grailSmartLazyMerge(array, currBlock, this.currBlockLen, this.currBlockOrigin,
                            blockLen);
                }
            }
        }

        currBlock = nextBlock - this.currBlockLen;

        if (lastLen != 0) {
            if (this.currBlockOrigin == Subarray.RIGHT) {
                currBlock = nextBlock;
                this.currBlockLen = blockLen * lastMergeBlocks;
                this.currBlockOrigin = Subarray.LEFT;
            } else {
                this.currBlockLen += blockLen * lastMergeBlocks;
            }

            grailLazyMerge(array, currBlock, this.currBlockLen, lastLen);
        }
    }

    // TODO: Double-check "Merge Blocks" arguments
    private void grailCombineInPlace(int[] array, int firstKey, int start, int length,
            int subarrayLen, int blockLen,
            int mergeCount, int lastSubarrays,
            boolean buffer) { // TODO: Do collisions with hanging indents like these affect readability?

        int fullMerge = 2 * subarrayLen;
        // SLIGHT OPTIMIZATION: 'blockCount' only needs to be calculated once for
        // regular merges
        int blockCount = fullMerge / blockLen;

        for (int mergeIndex = 0; mergeIndex < mergeCount; mergeIndex++) {
            int offset = start + (mergeIndex * fullMerge);

            grailInsertSort(array, firstKey, blockCount);

            // INCORRECT PARAMETER BUG FIXED: `block select sort` should be using `offset`,
            // not `start`
            int medianKey = subarrayLen / blockLen;
            medianKey = grailBlockSelectSort(array, firstKey, offset, medianKey, blockCount, blockLen);

            if (buffer) {
                this.grailMergeBlocks(array, firstKey, firstKey + medianKey, offset, blockCount, blockLen, 0, 0);
            } else {
                this.grailLazyMergeBlocks(array, firstKey, firstKey + medianKey, offset, blockCount, blockLen, 0, 0);
            }
        }

        // INCORRECT CONDITIONAL/PARAMETER BUG FIXED: Credit to 666666t for debugging.
        if (lastSubarrays != 0) {
            int offset = start + (mergeCount * fullMerge);
            blockCount = lastSubarrays / blockLen;

            grailInsertSort(array, firstKey, blockCount + 1);

            // INCORRECT PARAMETER BUG FIXED: `block select sort` should be using `offset`,
            // not `start`
            int medianKey = subarrayLen / blockLen;
            medianKey = grailBlockSelectSort(array, firstKey, offset, medianKey, blockCount, blockLen);

            // MISSING BOUNDS CHECK BUG FIXED: `lastFragment` *can* be 0 if the last two
            // subarrays are evenly
            // divided into blocks. This prevents Grailsort from going out-of-bounds.
            int lastFragment = lastSubarrays - (blockCount * blockLen);
            int lastMergeBlocks;
            if (lastFragment != 0) {
                lastMergeBlocks = grailCountLastMergeBlocks(array, offset, blockCount, blockLen);
            } else {
                lastMergeBlocks = 0;
            }

            int smartMerges = blockCount - lastMergeBlocks;

            // TODO: Double-check if this micro-optimization works correctly like the
            // original
            if (smartMerges == 0) {
                // MINOR CHANGE: renamed for consistency (used to be 'leftLength')
                int leftLen = lastMergeBlocks * blockLen;

                // INCORRECT PARAMETER BUG FIXED: these merges should be using `offset`, not
                // `start`
                if (buffer) {
                    grailMergeForwards(array, offset, leftLen, lastFragment, blockLen);
                } else {
                    grailLazyMerge(array, offset, leftLen, lastFragment);
                }
            } else {
                if (buffer) {
                    this.grailMergeBlocks(array, firstKey, firstKey + medianKey, offset,
                            smartMerges, blockLen, lastMergeBlocks, lastFragment);
                } else {
                    this.grailLazyMergeBlocks(array, firstKey, firstKey + medianKey, offset,
                            smartMerges, blockLen, lastMergeBlocks, lastFragment);
                }
            }
        }

        if (buffer) {
            grailInPlaceBufferReset(array, start, length, blockLen);
        }
    }

    // 'keys' are on the left side of array. Blocks of length 'subarrayLen'
    // combined. We'll combine them in pairs
    // 'subarrayLen' is a power of 2. (2 * subarrayLen / blockLen) keys are
    // guaranteed
    //
    // IMPORTANT RENAME: 'lastSubarray' is now 'lastSubarrays' because it includes
    // the length of the last left
    // subarray AND last right subarray (if there is a right subarray at all).
    //
    // *Please also check everything surrounding 'if(lastSubarrays != 0)' inside
    // 'combine in-/out-of-place' methods for other renames!!*
    private void grailCombineBlocks(int[] array, int firstKey, int start, int length,
            int subarrayLen, int blockLen, boolean buffer) {
        int fullMerge = 2 * subarrayLen;
        int mergeCount = length / fullMerge;
        int lastSubarrays = length - (fullMerge * mergeCount);

        if (lastSubarrays <= subarrayLen) {
            length -= lastSubarrays;
            lastSubarrays = 0;
        }

        // INCOMPLETE CONDITIONAL BUG FIXED: In order to combine blocks out-of-place, we
        // must check if a full-sized
        // block fits into our external buffer.

        this.grailCombineInPlace(array, firstKey, start, length, subarrayLen, blockLen,
                mergeCount, lastSubarrays, buffer);

    }

    // "Classic" in-place merge sort using binary searches and rotations
    //
    // cost: min(leftLen, rightLen)^2 + max(leftLen, rightLen)
    // MINOR CHANGES: better naming -- 'insertPos' is now 'mergeLen' -- and
    // "middle"/"end" calculations simplified
    public void grailLazyMerge(int[] array, int start, int leftLen, int rightLen) {
        if (leftLen < rightLen) {
            int middle = start + leftLen;

            while (leftLen != 0) {
                int mergeLen = grailBinarySearchLeft(array, middle, rightLen, array[start]);

                if (mergeLen != 0) {
                    grailRotate(array, start, leftLen, mergeLen);

                    start += mergeLen;
                    middle += mergeLen;
                    rightLen -= mergeLen;
                }

                if (rightLen == 0) {
                    break;
                } else {
                    do {
                        start++;
                        leftLen--;
                    } while (leftLen != 0 && Reads.compareIndices(array, start, middle, 0.1, true) <= 0);
                }
            }
            // INDEXING BUG FIXED: Credit to Anonymous0726 for debugging.
        } else {
            int end = start + leftLen + rightLen - 1;

            while (rightLen != 0) {
                int mergeLen = grailBinarySearchRight(array, start, leftLen, array[end]);

                if (mergeLen != leftLen) {
                    grailRotate(array, start + mergeLen, leftLen - mergeLen, rightLen);

                    end -= leftLen - mergeLen;
                    leftLen = mergeLen;
                }

                if (leftLen == 0) {
                    break;
                } else {
                    int middle = start + leftLen;
                    do {
                        rightLen--;
                        end--;
                    } while (rightLen != 0 && Reads.compareIndices(array, middle - 1, end, 0.1, true) <= 0);
                }
            }
        }
    }

    public void grailLazyStableSort(int[] array, int start, int length) {
        for (int index = 1; index < length; index += 2) {
            int left = start + index - 1;
            int right = start + index;

            if (Reads.compareIndices(array, left, right, 0.1, true) > 0) {
                grailSwap(array, left, right);
            }
        }
        for (int mergeLen = 2; mergeLen < length; mergeLen *= 2) {
            int fullMerge = 2 * mergeLen;

            int mergeIndex;
            int mergeEnd = length - fullMerge;

            for (mergeIndex = 0; mergeIndex <= mergeEnd; mergeIndex += fullMerge) {
                grailLazyMerge(array, start + mergeIndex, mergeLen, mergeLen);
            }

            int leftOver = length - mergeIndex;
            if (leftOver > mergeLen) {
                grailLazyMerge(array, start + mergeIndex, mergeLen, leftOver - mergeLen);
            }
        }
    }

    // Calculates the minimum between numKeys and cbrt(2 * subarrayLen * keysFound).
    // Math will be further explained later, but just like in grailCommonSort, this
    // loop is rendered completely useless by the scrolling buffer optimization;
    // minKeys will always equal numKeys.
    //
    // Code still here for preservation purposes.
    /*
     * private static int grailCalcMinKeys(int numKeys, long subarrayKeys) {
     * int minKeys = 1;
     * while(minKeys < numKeys && subarrayKeys != 0) {
     * minKeys *= 2;
     * subarrayKeys /= 8;
     * }
     * return minKeys;
     * }
     */

    public void grailCommonSort(int[] array, int start, int length) {
        if (length < 16) {
            grailInsertSort(array, start, length);
            return;
        }

        int blockLen = 1;

        // find the smallest power of two greater than or equal to
        // the square root of the input's length
        while ((blockLen * blockLen) < length) {
            blockLen *= 2;
        }

        // '((a - 1) / b) + 1' is actually a clever and very efficient
        // formula for the ceiling of (a / b)
        //
        // credit to Anonymous0726 for figuring this out!
        int keyLen = ((length - 1) / blockLen) + 1;

        // Grailsort is hoping to find `2 * sqrt(n)` unique items
        // throughout the array
        int idealKeys = keyLen + blockLen;

        // TODO: Clean up `start +` offsets
        int keysFound = grailCollectKeys(array, start, length, idealKeys);

        boolean idealBuffer;
        if (keysFound < idealKeys) {
            if (keysFound < 4) {
                // GRAILSORT STRATEGY 3 -- No block swaps or scrolling buffer; resort to Lazy
                // Stable Sort
                grailLazyStableSort(array, start, length);
                return;
            } else {
                // GRAILSORT STRATEGY 2 -- Block swaps with small scrolling buffer and/or lazy
                // merges
                keyLen = blockLen;
                blockLen = 0;
                idealBuffer = false;

                while (keyLen > keysFound) {
                    keyLen /= 2;
                }
            }
        } else {
            // GRAILSORT STRATEGY 1 -- Block swaps with scrolling buffer
            idealBuffer = true;
        }

        int bufferEnd = blockLen + keyLen;
        int subarrayLen;
        if (idealBuffer) {
            subarrayLen = blockLen;
        } else {
            subarrayLen = keyLen;
        }

        this.grailBuildBlocks(array, start + bufferEnd, length - bufferEnd, subarrayLen);

        while ((length - bufferEnd) > (2 * subarrayLen)) {
            subarrayLen *= 2;

            int currentBlockLen = blockLen;
            boolean scrollingBuffer = idealBuffer;

            // Huge credit to Anonymous0726, phoenixbound, and DeveloperSort for their
            // tireless efforts
            // towards deconstructing this math.
            if (!idealBuffer) {
                int keyBuffer = keyLen / 2;

                // TODO: Rewrite explanation for this math
                if (keyBuffer >= ((2 * subarrayLen) / keyBuffer)) {
                    currentBlockLen = keyBuffer;
                    scrollingBuffer = true;
                } else {
                    // This is a very recent discovery, and the math will be spelled out later, but
                    // this
                    // "minKeys" calculation is *completely unnecessary*. "minKeys" would be less
                    // than
                    // "keyLen" iff ((keyBuffer >= (2 * subarrayLen)) / keyBuffer)... but this
                    // situation
                    // is already covered by our scrolling buffer optimization right above!!
                    // Consequently,
                    // "minKeys" will *always* be equal to "keyLen" when Grailsort resorts to smart
                    // lazy
                    // merges. Removing this loop is by itself a decent optimization, as well!
                    //
                    // Code still here for preservation purposes.
                    /*
                     * long subarrayKeys = ((long) subarrayLen * keysFound) / 2;
                     * int minKeys = grailCalcMinKeys(keyLen, subarrayKeys);
                     *
                     * currentBlockLen = (2 * subarrayLen) / minKeys;
                     */

                    currentBlockLen = (2 * subarrayLen) / keyLen;
                }
            }

            // WRONG VARIABLE BUG FIXED: 4th argument should be `length - bufferEnd`, was
            // `length - bufferLen` before.
            // Credit to 666666t and Anonymous0726 for debugging.
            this.grailCombineBlocks(array, start, start + bufferEnd, length - bufferEnd,
                    subarrayLen, currentBlockLen, scrollingBuffer);
        }

        grailInsertSort(array, start, bufferEnd);
        grailLazyMerge(array, start, bufferEnd, length - bufferEnd);
    }

    public void grailSortInPlace(int[] array, int start, int length) {
        this.grailCommonSort(array, start, length);
    }

}
