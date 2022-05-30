package io.github.arrayv.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.select.*;
import io.github.arrayv.sorts.templates.PDQSorting;

/*
 *
MIT License

Copyright (c) 2020 ArrayV 4.0 Team

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

public enum Shuffles {
    RANDOM {
        // If you want to learn why the random shuffle was changed,
        // I highly encourage you read this. It's quite fascinating:
        // http://datagenetics.com/blog/november42014/index.html

        public String getName() {
            return "Randomly";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            shuffle(array, 0, currentLen, delay ? 1 : 0, Writes);
        }
    },
    REVERSE {
        public String getName() {
            return "Backwards";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            Writes.reversal(array, 0, currentLen - 1, delay ? 1 : 0, true, false);
        }
    },
    ALMOST {
        public String getName() {
            return "Slight Shuffle";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            Random random = new Random();

            for (int i = 0; i < Math.max(currentLen / 20, 1); i++) {
                Writes.swap(array, random.nextInt(currentLen), random.nextInt(currentLen), 0, true, false);

                if (arrayVisualizer.shuffleEnabled())
                    Delays.sleep(10);
            }
        }
    },
    ALREADY {
        public String getName() {
            return "No Shuffle";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            for (int i = 0; i < currentLen; i++) {
                Highlights.markArray(1, i);
                if (arrayVisualizer.shuffleEnabled())
                    Delays.sleep(1);
            }
        }
    },
    SORTED {
        public String getName() {
            return "Sorted";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            this.sort(array, 0, currentLen, delay ? 1 : 0, Writes);
        }
    },
    NAIVE {
        public String getName() {
            return "Naive Randomly";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            Random random = new Random();

            for (int i = 0; i < currentLen; i++)
                Writes.swap(array, i, random.nextInt(currentLen), delay ? 1 : 0, true, false);
        }
    },
    SHUFFLED_TAIL {
        public String getName() {
            return "Scrambled Tail";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            Random random = new Random();
            int[] aux = Writes.createExternalArray(currentLen);
            int i = 0, j = 0, k = 0;
            while (i < currentLen) {
                Highlights.markArray(2, i);
                if (random.nextDouble() < 1 / 7d)
                    Writes.write(aux, k++, array[i++], delay ? 1 : 0, true, true);
                else
                    Writes.write(array, j++, array[i++], delay ? 1 : 0, true, false);
            }
            Writes.arraycopy(aux, 0, array, j, k, delay ? 1 : 0, true, false);
            shuffle(array, j, currentLen, delay ? 2 : 0, Writes);
            Writes.deleteExternalArray(aux);
        }
    },
    SHUFFLED_HEAD {
        public String getName() {
            return "Scrambled Head";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            Random random = new Random();
            int[] aux = Writes.createExternalArray(currentLen);
            int i = currentLen - 1, j = currentLen - 1, k = 0;
            while (i >= 0) {
                Highlights.markArray(2, i);
                if (random.nextDouble() < 1 / 7d)
                    Writes.write(aux, k++, array[i--], delay ? 1 : 0, true, true);
                else
                    Writes.write(array, j--, array[i--], delay ? 1 : 0, true, false);
            }
            Writes.arraycopy(aux, 0, array, 0, k, delay ? 1 : 0, true, false);
            shuffle(array, 0, j, delay ? 2 : 0, Writes);
            Writes.deleteExternalArray(aux);
        }
    },
    SHUFFLED_BOTHSIDES {
        public String getName() {
            return "Scrambled Both Sides";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            Random random = new Random();
            int[] aux = Writes.createExternalArray(currentLen);
            // head
            int i = currentLen - 1, j = currentLen - 1, k = 0;
            while (i >= 0) {
                Highlights.markArray(2, i);
                if (random.nextDouble() < 1 / 7d)
                    Writes.write(aux, k++, array[i--], delay ? 1 : 0, true, true);
                else
                    Writes.write(array, j--, array[i--], delay ? 1 : 0, true, false);
            }
            Writes.arraycopy(aux, 0, array, 0, k, delay ? 1 : 0, true, false);
            shuffle(array, 0, j, delay ? 2 : 0, Writes);
            // tail
            i = 0;
            j = 0;
            k = 0;
            while (i < currentLen) {
                Highlights.markArray(2, i);
                if (random.nextDouble() < 1 / 7d)
                    Writes.write(aux, k++, array[i++], delay ? 1 : 0, true, true);
                else
                    Writes.write(array, j++, array[i++], delay ? 1 : 0, true, false);
            }
            Writes.arraycopy(aux, 0, array, j, k, delay ? 1 : 0, true, false);
            shuffle(array, j, currentLen, delay ? 2 : 0, Writes);
            Writes.deleteExternalArray(aux);
        }
    },
    MOVED_ELEMENT {
        public String getName() {
            return "Shifted Element";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            Random random = new Random();

            int start = random.nextInt(currentLen);
            int dest = random.nextInt(currentLen);
            if (dest < start) {
                IndexedRotations.holyGriesMills(array, dest, start, start + 1, delay ? 1 : 0, true, false);
            } else {
                IndexedRotations.holyGriesMills(array, start, start + 1, dest, delay ? 1 : 0, true, false);
            }
        }
    },
    RANDOM_SWAP {
        public String getName() {
            return "Random Swap";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            Random random = new Random();

            int start = random.nextInt(currentLen);
            int dest = random.nextInt(currentLen);
            Writes.swap(array, start, dest, delay ? 1 : 0, true, false);
        }
    },
    SWAPPED_ENDS {
        public String getName() {
            return "Swapped Ends";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            Writes.swap(array, 0, currentLen - 1, delay ? 1 : 0, true, false);
        }
    },
    SWAPPED_PAIRS {
        public String getName() {
            return "Swapped Pairs";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            for (int i = 1; i < currentLen; i += 2)
                Writes.swap(array, i - 1, i, delay ? 0.5 : 0, true, false);
        }
    },
    RANDOM_REVERSAL {
        public String getName() {
            return "Random Reversal";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            Random random = new Random();

            int start = random.nextInt(currentLen);
            int dest = random.nextInt(currentLen);
            Writes.reversal(array, start, dest, delay ? 1 : 0, true, false);
        }
    },
    NOISY {
        public String getName() {
            return "Noisy";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            Random random = new Random();

            int i, size = Math.max(4, (int) (Math.sqrt(currentLen) / 2));
            for (i = 0; i + size <= currentLen; i += random.nextInt(size - 1) + 1)
                shuffle(array, i, i + size, delay ? 0.5 : 0, Writes);
            shuffle(array, i, currentLen, delay ? 0.5 : 0, Writes);
        }
    },
    SHUFFLED_ODDS {
        public String getName() {
            return "Scrambled Odds";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            Random random = new Random();

            for (int i = 1; i < currentLen; i += 2) {
                int randomIndex = (((random.nextInt(currentLen - i) / 2)) * 2) + i;
                Writes.swap(array, i, randomIndex, 0, true, false);

                if (arrayVisualizer.shuffleEnabled())
                    Delays.sleep(2);
            }
        }
    },
    FINAL_MERGE {
        public String getName() {
            return "Final Merge Pass";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            int count = 2;

            int k = 0;
            int[] temp = Writes.createExternalArray(currentLen);

            for (int j = 0; j < count; j++)
                for (int i = j; i < currentLen; i += count)
                    Writes.write(temp, k++, array[i], delay ? 1 : 0, true, true);

            for (int i = 0; i < currentLen; i++)
                Writes.write(array, i, temp[i], delay ? 1 : 0, true, false);
            Writes.deleteExternalArray(temp);
        }
    },
    REAL_FINAL_MERGE {
        public String getName() {
            return "Shuffled Final Merge";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            this.shuffle(array, 0, currentLen, delay ? 0.5 : 0, Writes);
            Highlights.clearMark(2);
            this.sort(array, 0, currentLen / 2, delay ? 0.5 : 0, Writes);
            this.sort(array, currentLen / 2, currentLen, delay ? 0.5 : 0, Writes);
        }
    },
    SHUFFLED_HALF {
        public String getName() {
            return "Shuffled Half";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            this.shuffle(array, 0, currentLen, delay ? 2 / 3d : 0, Writes);
            Highlights.clearMark(2);
            this.sort(array, 0, currentLen / 2, delay ? 2 / 3d : 0, Writes);
        }
    },
    PARTITIONED {
        public String getName() {
            return "Partitioned";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            this.sort(array, 0, currentLen, delay ? 0.5 : 0, Writes);
            Highlights.clearMark(2);
            this.shuffle(array, 0, currentLen / 2, delay ? 0.5 : 0, Writes);
            this.shuffle(array, currentLen / 2, currentLen, delay ? 0.5 : 0, Writes);
        }
    },
    QUICK_PARTITIONED {
        public String getName() {
            return "Quick Partitioned";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            this.sort(array, 0, currentLen, delay ? 0.5 : 0, Writes);
            Highlights.clearMark(2);
            for (int i = 1; i < currentLen - 1; i *= 2) {
                this.shuffle(array, i, i * 2, delay ? 0.5 : 0, Writes);
            }

        }
    },
    STRANGE_PASS {
        public String getName() {
            return "First Strange Pass";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            Reads Reads = arrayVisualizer.getReads();

            this.shuffle(array, 0, currentLen, delay ? 0.05 : 0, Writes);
            int offset = 1;
            double mult = 1.0;
            int bound = 1;
            while (offset != currentLen) {
                mult = 1;
                bound = 1;
                while (offset + mult <= currentLen) {
                    if (Reads.compareIndices(array, (int) (offset + mult / 2) - 1, (int) (offset + mult) - 1,
                            0, true) > 0) {
                        Writes.swap(array, (int) (offset + mult / 2) - 1, (int) (offset + mult) - 1, delay ? 0.005 : 0,
                                true, false);
                        if (mult == 1 / 2) {
                            bound *= 2;
                            mult = bound;
                        } else {
                            mult /= 2;
                        }
                    } else {
                        bound *= 2;
                        mult = bound;
                    }
                }
                offset++;
            }
        }
    },
    SAWTOOTH {
        public String getName() {
            return "Sawtooth";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            int count = 4;

            int k = 0;
            int[] temp = Writes.createExternalArray(currentLen);

            for (int j = 0; j < count; j++)
                for (int i = j; i < currentLen; i += count)
                    Writes.write(temp, k++, array[i], delay ? 1 : 0, true, true);

            for (int i = 0; i < currentLen; i++)
                Writes.write(array, i, temp[i], delay ? 1 : 0, true, false);
            Writes.deleteExternalArray(temp);
        }
    },
    ORGAN {
        public String getName() {
            return "Pipe Organ";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            int[] temp = Writes.createExternalArray(currentLen);

            for (int i = 0, j = 0; i < currentLen; i += 2) {
                Writes.write(temp, j++, array[i], delay ? 1 : 0, true, true);
            }
            for (int i = 1, j = currentLen; i < currentLen; i += 2) {
                Writes.write(temp, --j, array[i], delay ? 1 : 0, true, true);
            }
            for (int i = 0; i < currentLen; i++) {
                Writes.write(array, i, temp[i], delay ? 1 : 0, true, false);
            }
            Writes.deleteExternalArray(temp);
        }
    },
    FINAL_BITONIC {
        public String getName() {
            return "Final Bitonic Pass";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            int[] temp = Writes.createExternalArray(currentLen);

            Writes.reversal(array, 0, currentLen - 1, delay ? 1 : 0, true, false);
            Highlights.clearMark(2);
            for (int i = 0, j = 0; i < currentLen; i += 2) {
                Writes.write(temp, j++, array[i], delay ? 1 : 0, true, true);
            }
            for (int i = 1, j = currentLen; i < currentLen; i += 2) {
                Writes.write(temp, --j, array[i], delay ? 1 : 0, true, true);
            }
            for (int i = 0; i < currentLen; i++) {
                Writes.write(array, i, temp[i], delay ? 1 : 0, true, false);
            }
            Writes.deleteExternalArray(temp);
        }
    },
    TRIANGLE_WAVE {
        public String getName() {
            return "Triangle Wave";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            int[] temp = Writes.createExternalArray(currentLen);

            for (int i = 0, j = 0; i < currentLen; i += 2) {
                Writes.write(temp, j++, array[i], delay ? 1 : 0, true, true);
            }
            for (int i = 1, j = currentLen; i < currentLen; i += 2) {
                Writes.write(temp, --j, array[i], delay ? 1 : 0, true, true);
            }
            for (int i = 0; i < currentLen; i++) {
                Writes.write(array, i, temp[i], delay ? 1 : 0, true, false);
            }
            Writes.deleteExternalArray(temp);
            int a = 0, m = (currentLen + 1) / 4;

            if (currentLen % 2 == 0)
                while (m < currentLen)
                    Writes.swap(array, a++, m++, delay ? 1 : 0, true, false);
            else {
                Highlights.clearMark(2);
                int tempv = array[a];
                while (m < currentLen) {
                    Writes.write(array, a++, array[m], delay ? 0.5 : 0, true, false);
                    Writes.write(array, m++, array[a], delay ? 0.5 : 0, true, false);
                }
                Writes.write(array, a, tempv, delay ? 0.5 : 0, true, false);
            }
        }
    },
    INTERLACED {
        public String getName() {
            return "Interlaced";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int[] referenceArray = Writes.createExternalArray(currentLen);
            for (int i = 0; i < currentLen; i++) {
                Writes.write(referenceArray, i, array[i], delay ? 1 : 0, true, true);
            }

            int leftIndex = 1;
            int rightIndex = currentLen - 1;

            for (int i = 1; i < currentLen; i++) {
                if (i % 2 == 0) {
                    Writes.write(array, i, referenceArray[leftIndex++], delay ? 1 : 0, true, false);
                } else {
                    Writes.write(array, i, referenceArray[rightIndex--], delay ? 1 : 0, true, false);
                }
            }
            Writes.deleteExternalArray(referenceArray);
        }
    },
    DOUBLE_LAYERED {
        public String getName() {
            return "Double Layered";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();

            for (int i = 0; i < currentLen / 2; i += 2) {
                Writes.swap(array, i, currentLen - i - 1, 0, true, false);
                if (arrayVisualizer.shuffleEnabled())
                    Delays.sleep(1);
            }
        }
    },
    FINAL_RADIX {
        public String getName() {
            return "Final Radix";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            currentLen -= currentLen % 2;
            int mid = currentLen / 2;
            int[] temp = Writes.createExternalArray(mid);

            for (int i = 0; i < mid; i++)
                Writes.write(temp, i, array[i], delay ? 1 : 0, true, true);

            for (int i = mid, j = 0; i < currentLen; i++, j += 2) {
                Writes.write(array, j, array[i], delay ? 1 : 0, true, false);
                Writes.write(array, j + 1, temp[i - mid], delay ? 1 : 0, true, false);
            }
            Writes.deleteExternalArray(temp);
        }
    },
    REAL_FINAL_RADIX {
        public String getName() {
            return "Real Final Radix";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int mask = 0;
            for (int i = 0; i < currentLen; i++)
                while (mask < array[i])
                    mask = (mask << 1) + 1;
            mask >>= 1;

            int[] counts = Writes.createExternalArray(mask + 2);
            int[] tmp = Writes.createExternalArray(currentLen);

            Writes.arraycopy(array, 0, tmp, 0, currentLen, delay ? 1 : 0, true, true);

            for (int i = 0; i < currentLen; i++)
                Writes.write(counts, (array[i] & mask) + 1, counts[(array[i] & mask) + 1] + 1, delay ? 1 : 0, true,
                        true);

            for (int i = 1; i < counts.length; i++)
                Writes.write(counts, i, counts[i] + counts[i - 1], delay ? 1 : 0, true,
                        true);

            for (int i = 0; i < currentLen; i++)
                Writes.write(array, counts[tmp[i] & mask]++, tmp[i], delay ? 1 : 0, true, false);
            Writes.deleteExternalArray(counts);
            Writes.deleteExternalArray(tmp);
        }
    },
    REC_RADIX {
        public String getName() {
            return "Recursive Final Radix";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            weaveRec(array, 0, currentLen, 1, delay ? 0.5 : 0, Writes, 0);
        }

        public void weaveRec(int[] array, int pos, int length, int gap, double delay, Writes Writes, int depth) {
            if (length < 2)
                return;

            int mod2 = length % 2;
            length -= mod2;
            int mid = length / 2;
            int[] temp = Writes.createExternalArray(mid);

            for (int i = pos, j = 0; i < pos + gap * mid; i += gap, j++)
                Writes.write(temp, j, array[i], delay, true, true);

            for (int i = pos + gap * mid, j = pos, k = 0; i < pos + gap * length; i += gap, j += 2 * gap, k++) {
                Writes.write(array, j, array[i], delay, true, false);
                Writes.write(array, j + gap, temp[k], delay, true, false);
            }
            Writes.deleteExternalArray(temp);
            Writes.recordDepth(depth++);
            Writes.recursion(2);
            weaveRec(array, pos, mid + mod2, 2 * gap, delay / 2, Writes, depth);
            weaveRec(array, pos + gap, mid, 2 * gap, delay / 2, Writes, depth);
        }
    },
    HALF_ROTATION {
        public String getName() {
            return "Half Rotation";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int a = 0, m = (currentLen + 1) / 2;

            if (currentLen % 2 == 0)
                while (m < currentLen)
                    Writes.swap(array, a++, m++, delay ? 1 : 0, true, false);
            else {
                Highlights.clearMark(2);
                int temp = array[a];
                while (m < currentLen) {
                    Writes.write(array, a++, array[m], delay ? 0.5 : 0, true, false);
                    Writes.write(array, m++, array[a], delay ? 0.5 : 0, true, false);
                }
                Writes.write(array, a, temp, delay ? 0.5 : 0, true, false);
            }
        }
    },
    QUARTER_ROTATION {
        public String getName() {
            return "Quarter Rotation";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int a = 0, m = (currentLen + 1) / 4;

            if (currentLen % 2 == 0)
                while (m < currentLen)
                    Writes.swap(array, a++, m++, delay ? 1 : 0, true, false);
            else {
                Highlights.clearMark(2);
                int temp = array[a];
                while (m < currentLen) {
                    Writes.write(array, a++, array[m], delay ? 0.5 : 0, true, false);
                    Writes.write(array, m++, array[a], delay ? 0.5 : 0, true, false);
                }
                Writes.write(array, a, temp, delay ? 0.5 : 0, true, false);
            }
        }
    },
    PARTIAL_REVERSE {
        public String getName() {
            return "Half Reversed";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            Writes.reversal(array, 0, currentLen - 1, delay ? 1 : 0, true, false);
            Writes.reversal(array, currentLen / 4, (3 * currentLen + 3) / 4 - 1, delay ? 1 : 0, true, false);
        }
    },
    BST_TRAVERSAL {
        public String getName() {
            return "BST Traversal";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            int[] temp = Writes.copyOfArray(array, currentLen);

            // credit to sam walko/anon

            class Subarray {
                private int start;
                private int end;

                Subarray(int start, int end) {
                    this.start = start;
                    this.end = end;
                }
            }

            Queue<Subarray> q = new LinkedList<Subarray>();
            q.add(new Subarray(0, currentLen));
            Writes.changeAuxWrites(1);
            Writes.changeAllocAmount(1);
            int i = 0;

            while (!q.isEmpty()) {
                Subarray sub = q.poll();
                Writes.changeAuxWrites(1);
                Writes.changeAllocAmount(-1);
                if (sub.start != sub.end) {
                    int mid = (sub.start + sub.end) / 2;
                    Writes.write(array, i, temp[mid], 0, true, false);
                    if (arrayVisualizer.shuffleEnabled())
                        Delays.sleep(1);
                    i++;
                    q.add(new Subarray(sub.start, mid));
                    Writes.changeAuxWrites(1);
                    Writes.changeAllocAmount(1);
                    q.add(new Subarray(mid + 1, sub.end));
                    Writes.changeAuxWrites(1);
                    Writes.changeAllocAmount(1);
                }
            }
            Writes.deleteExternalArray(temp);
        }
    },
    INV_BST {
        public String getName() {
            return "Inverted BST";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            int[] temp = Writes.createExternalArray(currentLen);

            // credit to sam walko/anon

            class Subarray {
                private int start;
                private int end;

                Subarray(int start, int end) {
                    this.start = start;
                    this.end = end;
                }
            }

            Queue<Subarray> q = new LinkedList<Subarray>();
            q.add(new Subarray(0, currentLen));
            Writes.changeAuxWrites(1);
            Writes.changeAllocAmount(1);
            int i = 0;

            while (!q.isEmpty()) {
                Subarray sub = q.poll();
                Writes.changeAuxWrites(1);
                Writes.changeAllocAmount(-1);
                if (sub.start != sub.end) {
                    int mid = (sub.start + sub.end) / 2;
                    Highlights.markArray(1, mid);
                    Writes.write(temp, i, mid, delay ? 1 : 0, true, true);
                    if (delay)
                        Delays.sleep(0.5);
                    i++;
                    q.add(new Subarray(sub.start, mid));
                    Writes.changeAuxWrites(1);
                    Writes.changeAllocAmount(1);
                    q.add(new Subarray(mid + 1, sub.end));
                    Writes.changeAuxWrites(1);
                    Writes.changeAllocAmount(1);
                }
            }
            int[] temp2 = Writes.copyOfArray(array, currentLen);
            for (i = 0; i < currentLen; i++)
                Writes.write(array, temp[i], temp2[i], delay ? 0.5 : 0, true, false);
            Writes.deleteExternalArray(temp);
            Writes.deleteExternalArray(temp2);
        }
    },
    LOG_SLOPES {
        public String getName() {
            return "Logarithmic Slopes";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int[] temp = Writes.createExternalArray(currentLen);
            for (int i = 0; i < currentLen; i++)
                Writes.write(temp, i, array[i], delay ? 1 : 0, true, true);

            Writes.write(array, 0, 0, delay ? 1 : 0, true, false);
            for (int i = 1; i < currentLen; i++) {
                int log = (int) (Math.log(i) / Math.log(2));
                int power = (int) Math.pow(2, log);
                int value = temp[2 * (i - power) + 1];
                Writes.write(array, i, value, delay ? 1 : 0, true, false);
            }
            Writes.deleteExternalArray(temp);
        }
    },
    HEAPIFIED {
        public String getName() {
            return "Max Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            MaxHeapSort heapSort = new MaxHeapSort(arrayVisualizer);
            heapSort.makeHeap(array, 0, currentLen, delay ? 1 : 0);
        }
    },
    MIN_HEAPIFIED {
        public String getName() {
            return "Min Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            MinHeapSort heapSort = new MinHeapSort(arrayVisualizer);
            heapSort.heapify(array, 0, currentLen, delay ? 1 : 0, false);
        }
    },
    FLIPPED_MIN_HEAPIFIED {
        public String getName() {
            return "Flipped Min Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();

            FlippedMinHeapSort heapSort = new FlippedMinHeapSort(arrayVisualizer);
            for (int i = currentLen / 2; i >= 1; i--) {
                heapSort.siftDown(array, currentLen, i, currentLen);
            }
        }
    },
    MIN_MAX_HEAPIFIED {
        public String getName() {
            return "Min-Max Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();

            MinMaxHeapSort heapSort = new MinMaxHeapSort(arrayVisualizer);
            for (int i = currentLen / 2; i >= 1; i--) {
                heapSort.heapify(array, 0, currentLen);
            }
        }
    },
    WEAK_HEAPIFIED {
        public String getName() {
            return "Weak Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();

            WeakHeapSort heapSort = new WeakHeapSort(arrayVisualizer);
            int i, j, Gparent;

            int bitsLength = (currentLen + 7) / 8;
            int[] bits = Writes.createExternalArray(bitsLength);

            for (i = 0; i < currentLen / 8; ++i) {
                Writes.write(bits, i, 0, 0, false, true);
            }

            for (i = currentLen - 1; i > 0; --i) {
                j = i;

                while ((j & 1) == WeakHeapSort.getBitwiseFlag(bits, j >> 1))
                    j >>= 1;
                Gparent = j >> 1;

                heapSort.weakHeapMerge(array, bits, Gparent, i);
            }
            Writes.deleteExternalArray(bits);
        }
    },
    TERNARY_HEAPIFIED {
        public String getName() {
            return "Ternary Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();

            TernaryHeapSort heapSort = new TernaryHeapSort(arrayVisualizer);
            for (int i = currentLen / 2; i >= 1; i--) {
                heapSort.buildMaxTernaryHeap(array, currentLen);
            }
        }
    },
    BINOMIAL_HEAPIFIED {
        public String getName() {
            return "Binomial Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            Reads Reads = arrayVisualizer.getReads();
            int maxNode, focus, index, depth;
            for (index = 2; index <= currentLen; index += 2) {
                maxNode = index;
                do {
                    focus = maxNode;
                    for (depth = 1; (focus & depth) == 0; depth *= 2) {
                        if (Reads.compareValues(array[focus - depth - 1], array[maxNode - 1]) > 0)
                            maxNode = (focus - depth);
                    }
                    if (focus != maxNode) {
                        Writes.swap(array, focus - 1, maxNode - 1, 0.5, true, false);
                    }
                } while (focus != maxNode);
            }
        }
    },
    BOTTOM_UP_HEAPIFIED {
        public String getName() {
            return "Bottom-Up Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int length = arrayVisualizer.getCurrentLength();

            BottomUpHeapSort heapSort = new BottomUpHeapSort(arrayVisualizer);
            for (int i = (length - 1) / 2; i >= 0; i--)
                heapSort.siftDown(array, i, length);
        }
    },
    FIBONACCI_HEAPIFIED {
        public String getName() {
            return "Fibonacci Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int length = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            if (delay)
                Delays.setSleepRatio(Delays.getSleepRatio() * 10);
            else
                Delays.changeSkipped(true);
            FibonacciHeapSort heapSort = new FibonacciHeapSort(arrayVisualizer);
            heapSort.fibHeapify(array, 0, length);
            if (delay)
                Delays.setSleepRatio(Delays.getSleepRatio() / 10);
            else
                Delays.changeSkipped(false);
        }
    },
    GEAPIFIED {
        public String getName() {
            return "Geapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int length = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            GeapSort heapSort = new GeapSort(arrayVisualizer);
            heapSort.makeGeap(array, 0, length, delay ? 0.5 : 0);
        }
    },
    PURE_FIBONACCI_HEAPIFIED {
        public String getName() {
            return "Pure Fibonacci Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int length = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            if (delay)
                Delays.setSleepRatio(Delays.getSleepRatio() * 1000);
            else
                Delays.changeSkipped(true);
            PureFibHeapSort heapSort = new PureFibHeapSort(arrayVisualizer);
            heapSort.fibHeapify(array, 0, length);
        }
    },
    NARNIA {
        public String getName() {
            return "Narnia Heapified";
        }

        private int[] array;
        private Reads Reads;

        class Node {
            public int index;
            private boolean tournament;
            private Node childLeft = null, childRight = null, winner = null;

            public Node(int index) {
                this.index = index;
                this.tournament = false;
            }

            public Node(boolean tournament) {
                this.index = -1;
                this.tournament = tournament;
            }

            public Node loser() {
                if (childLeft == null || childRight == null)
                    return null;
                return winner == childLeft ? childRight : childLeft;
            }

            public void build() {
                if (!tournament)
                    return;
                if (childLeft.index < 0) {
                    if (childRight.index < 0) {
                        this.index = -1;
                        tournament = false;
                        childLeft = childRight = winner = null;
                        return;
                    }
                    winner = childRight;
                    index = childRight.index;
                } else if (childRight.index < 0) {
                    winner = childLeft;
                    index = childLeft.index;
                } else if (indices(childLeft.index, childRight.index) == 1) {
                    winner = childRight;
                    index = childRight.index;
                } else {
                    winner = childLeft;
                    index = childLeft.index;
                }
            }

            private int indices(int a, int b) {
                return Reads.compareIndices(array, b, a, 0.1, true);
            }
        }

        public Node build(Node left, Node right) {
            Node t = new Node(true);
            t.childLeft = left;
            t.childRight = right;
            t.build();
            return t;
        }

        private Node traverse(int start, int end) {
            if (end - start == 0)
                return new Node(start);
            if (end - start == 1) {
                Node a = new Node(start),
                        b = new Node(end);

                return build(a, b);
            }
            int mid = start + (end - start) / 2;

            return build(traverse(start, mid), traverse(mid + 1, end));
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            this.array = array;
            this.Reads = arrayVisualizer.getReads();
            Node winner = traverse(0, currentLen - 1);
            Queue<Node> nodes = new LinkedList<Node>();
            nodes.add(winner);
            boolean[] struck = new boolean[currentLen];
            int[] heap = writes.createExternalArray(currentLen);
            int i = 0;
            while (!nodes.isEmpty()) {
                Node t = nodes.poll();
                if (struck[t.index]) {
                    if (t.winner != null)
                        nodes.add(t.winner);
                    if (t.loser() != null)
                        nodes.add(t.loser());
                    continue;
                }
                writes.write(heap, i++, array[t.index], 1, true, true);
                struck[t.index] = true;
                if (t.winner != null)
                    nodes.add(t.winner);
                if (t.loser() != null)
                    nodes.add(t.loser());
            }
            writes.arraycopy(heap, 0, array, 0, currentLen, 1, true, false);
            writes.deleteExternalArray(heap);
        }
    },
    SMOOTH {
        public String getName() {
            return "Smoothified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();

            SmoothSort smoothSort = new SmoothSort(arrayVisualizer);
            smoothSort.smoothHeapify(array, currentLen);
        }
    },
    BINOMIAL_SMOOTH {
        public String getName() {
            return "Binomial Smoothified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();

            BinomialSmoothSort heapSort = new BinomialSmoothSort(arrayVisualizer);
            int Node;
            for (Node = 1; Node < currentLen; Node++)
                heapSort.thrift(array, Node, Node % 2 == 1, (Node + (1 << heapSort.height(Node)) >= currentLen), 0);
        }
    },
    POPLAR {
        public String getName() {
            return "Poplarified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();

            PoplarHeapSort poplarHeapSort = new PoplarHeapSort(arrayVisualizer);
            poplarHeapSort.poplarHeapify(array, 0, currentLen);
        }
    },
    LAZY_HEAPIFIED {
        public String getName() {
            return "Lazy Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int length = arrayVisualizer.getCurrentLength();

            LazyHeapSort heapSort = new LazyHeapSort(arrayVisualizer);
            int s = (int) Math.sqrt(length - 1) + 1;

            for (int i = 0; i < length; i += s)
                heapSort.maxToFront(array, i, Math.min(i + s, length));
        }
    },
    OPTI_LAZY_HEAPIFIED {
        public String getName() {
            return "Optimized Lazy Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int length = arrayVisualizer.getCurrentLength();
            Reads Reads = arrayVisualizer.getReads();
            int s = (int) Math.sqrt(length - 1) + 1;

            int f = (length - 1) % s + 1;

            for (int j = f; j < length; j += s) {
                int min = findMin(array, j, j + 1, j + s, 1, Reads);

                if (j != min)
                    Writes.swap(array, j, min, 1, true, false);
            }
        }

        private int findMin(int[] array, int p, int a, int b, int s, Reads Reads) {
            int min = p;

            for (int i = a; i < b; i += s)
                if (Reads.compareIndices(array, i, min, 0.1, true) < 0)
                    min = i;

            return min;
        }
    },
    TRI_HEAP {
        public String getName() {
            return "Triangular Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            if (delay)
                Delays.setSleepRatio(Delays.getSleepRatio() * 10);
            else
                Delays.changeSkipped(true);

            TriangularHeapSort triangularHeapSort = new TriangularHeapSort(arrayVisualizer);
            triangularHeapSort.triangularHeapify(array, currentLen);

            if (delay)
                Delays.setSleepRatio(Delays.getSleepRatio() / 10);
            else
                Delays.changeSkipped(false);
        }
    },
    PSEUDO_HEAPIFIED {
        public String getName() {
            return "Pseudo Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int length = arrayVisualizer.getCurrentLength();
            Reads Reads = arrayVisualizer.getReads();
            for (int i = length - 2; i >= 0; i--) {
                sift(array, i, length, Reads, Writes);
            }
        }

        private void sift(int[] array, int start, int end, Reads Reads, Writes Writes) {
            sift_down(array, start, end - start + 1, 1, Reads, Writes);
        }

        private void sift_down(int[] array, int start, int length, int root, Reads Reads, Writes Writes) {
            int j = root;

            while (2 * j < length) {
                int k = 2 * j;
                if (k < length && Reads.compareValues(array[start + k - 1], array[start + k]) == 1) {
                    k++;
                }
                if (Reads.compareIndices(array, start + j - 1, start + k - 1, 0.05, true) == 1) {
                    Writes.swap(array, start + j - 1, start + k - 1, 0.05, true, false);
                    j = k;
                    continue;
                }
                break;
            }
        }
    },
    CIRCLE {
        public String getName() {
            return "First Circle Pass";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            Reads Reads = arrayVisualizer.getReads();

            shuffle(array, 0, currentLen, delay ? 0.5 : 0, Writes);

            int n = 1;
            for (; n < currentLen; n *= 2)
                ;

            circleSortRoutine(array, 0, n - 1, currentLen, delay ? 0.5 : 0, Reads, Writes, 0);
        }

        public void circleSortRoutine(int[] array, int lo, int hi, int end, double sleep, Reads Reads, Writes Writes,
                int depth) {
            if (lo == hi)
                return;

            int high = hi;
            int low = lo;
            int mid = (hi - lo) / 2;

            while (lo < hi) {
                if (hi < end && Reads.compareIndices(array, lo, hi, sleep / 2, true) > 0)
                    Writes.swap(array, lo, hi, sleep, true, false);

                lo++;
                hi--;
            }
            Writes.recordDepth(depth++);
            Writes.recursion(1);
            circleSortRoutine(array, low, low + mid, end, sleep / 2, Reads, Writes, depth);
            if (low + mid + 1 < end) {
                Writes.recordDepth(depth++);
                Writes.recursion(1);
                circleSortRoutine(array, low + mid + 1, high, end, sleep / 2, Reads, Writes, depth);
            }
        }
    },
    PAIRWISE {
        public String getName() {
            return "Final Pairwise Pass";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            Reads Reads = arrayVisualizer.getReads();

            shuffle(array, 0, currentLen, delay ? 0.5 : 0, Writes);

            // create pairs
            for (int i = 1; i < currentLen; i += 2)
                if (Reads.compareIndices(array, i - 1, i, delay ? 0.5 : 0, true) > 0)
                    Writes.swap(array, i - 1, i, delay ? 0.5 : 0, true, false);

            Highlights.clearMark(2);

            int[] temp = Writes.createExternalArray(currentLen);

            // sort the smaller and larger of the pairs separately with pigeonhole sort
            for (int m = 0; m < 2; m++) {
                for (int k = m; k < currentLen; k += 2)
                    Writes.write(temp, array[k], temp[array[k]] + 1, delay ? 1 : 0, true, true);

                int i = 0, j = m;
                while (true) {
                    while (i < currentLen && temp[i] == 0)
                        i++;
                    if (i >= currentLen)
                        break;

                    Writes.write(array, j, i, delay ? 0.5 : 0, true, false);

                    j += 2;
                    Writes.write(temp, i, temp[i] - 1, delay ? 1 : 0, true, true);
                }
            }
            Writes.deleteExternalArray(temp);
        }
    },
    REC_REV {
        public String getName() {
            return "Recursive Reversal";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            reversalRec(array, 0, currentLen, delay ? 1 : 0, Writes, 0);
        }

        public void reversalRec(int[] array, int a, int b, double sleep, Writes Writes, int depth) {
            if (b - a < 2)
                return;

            Writes.reversal(array, a, b - 1, sleep, true, false);

            int m = (a + b) / 2;
            Writes.recordDepth(depth++);
            Writes.recursion(2);
            this.reversalRec(array, a, m, sleep / 2, Writes, depth);
            this.reversalRec(array, m, b, sleep / 2, Writes, depth);
        }
    },
    GRAY_CODE {
        public String getName() {
            return "Gray Code Fractal";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            reversalRec(array, 0, currentLen, false, delay ? 1 : 0, Writes, 0);
        }

        public void reversalRec(int[] array, int a, int b, boolean bw, double sleep, Writes Writes, int depth) {
            if (b - a < 3)
                return;

            int m = (a + b) / 2;

            if (bw)
                Writes.reversal(array, a, m - 1, sleep, true, false);
            else
                Writes.reversal(array, m, b - 1, sleep, true, false);
            Writes.recordDepth(depth++);
            Writes.recursion(2);
            this.reversalRec(array, a, m, false, sleep / 2, Writes, depth);
            this.reversalRec(array, m, b, true, sleep / 2, Writes, depth);
        }
    },
    SIERPINSKI {
        public String getName() {
            return "Sierpinski Triangle";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            boolean delay = arrayVisualizer.shuffleEnabled();
            int currentLen = arrayVisualizer.getCurrentLength();
            int[] triangle = Writes.createExternalArray(currentLen);
            triangleRec(triangle, 0, currentLen, delay ? 1 : 0, Writes, 0);

            int[] temp = Writes.copyOfArray(array, currentLen);
            for (int i = 0; i < currentLen; i++)
                Writes.write(array, i, temp[triangle[i]], delay ? 1 : 0, true, false);
            Writes.deleteExternalArray(temp);
            Writes.deleteExternalArray(triangle);
        }

        public void triangleRec(int[] array, int a, int b, double sleep, Writes Writes, int depth) {
            if (b - a < 2)
                return;
            if (b - a == 2) {
                Writes.write(array, a + 1, array[a + 1] + 1, sleep, true, true);
                return;
            }

            int h = (b - a) / 3, t1 = (a + a + b) / 3, t2 = (a + b + b + 2) / 3;
            for (int i = a; i < t1; i++)
                Writes.write(array, i, array[i] + h, sleep, true, true);

            for (int i = t1; i < t2; i++)
                Writes.write(array, i, array[i] + 2 * h, sleep, true, true);
            Writes.recordDepth(depth++);
            Writes.recursion(3);
            triangleRec(array, a, t1, sleep, Writes, depth);
            triangleRec(array, t1, t2, sleep, Writes, depth);
            triangleRec(array, t2, b, sleep, Writes, depth);
        }
    },
    TRIANGULAR {
        public String getName() {
            return "Triangular";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            Reads Reads = arrayVisualizer.getReads();
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            int[] triangle = Writes.createExternalArray(currentLen);

            int j = 0, k = 2;
            int max = 0;

            for (int i = 1; i < currentLen; i++, j++) {
                if (i == k) {
                    j = 0;
                    k *= 2;
                }
                Writes.write(triangle, i, triangle[j] + 1, delay ? 1 : 0, true, true);
                if (Reads.compareValues(triangle[i], max) > 0)
                    max = triangle[i];
            }
            int[] cnt = Writes.createExternalArray(max + 1);

            for (int i = 0; i < currentLen; i++)
                Writes.write(cnt, triangle[i], cnt[triangle[i]] + 1, delay ? 1 : 0, true, true);

            for (int i = 1; i < cnt.length; i++)
                Writes.write(cnt, i, cnt[i] + cnt[i - 1], delay ? 1 : 0, true, true);

            for (int i = currentLen - 1; i >= 0; i--)
                Writes.write(triangle, i, --cnt[triangle[i]], delay ? 1 : 0, true, true);

            int[] temp = Writes.copyOfArray(array, currentLen);
            for (int i = 0; i < currentLen; i++)
                Writes.write(array, i, temp[triangle[i]], delay ? 1 : 0, true, false);
            Writes.deleteExternalArray(temp);
            Writes.deleteExternalArray(cnt);
            Writes.deleteExternalArray(triangle);
        }
    },
    ANTI_CIRCLE {
        public String getName() {
            return "Backwards Circle Pass";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            Reads Reads = arrayVisualizer.getReads();

            int n = 1;
            for (; n < currentLen; n *= 2)
                ;

            circleSortRoutine(array, 0, n - 1, currentLen, delay ? 0.25 : 0, Reads, Writes, false, 0);
        }

        public void circleSortRoutine(int[] array, int lo, int hi, int end, double sleep, Reads Reads, Writes Writes,
                boolean dir, int depth) {
            if (lo == hi)
                return;

            int high = hi;
            int low = lo;
            int mid = (hi - lo) / 2;

            while (lo < hi) {
                if (dir) {
                    if (hi < end && Reads.compareIndices(array, lo, hi, sleep / 2, true) > 0)
                        Writes.swap(array, lo, hi, sleep, true, false);

                    lo++;
                    hi--;
                } else {
                    if (hi < end && Reads.compareIndices(array, lo, hi, sleep / 2, true) < 0)
                        Writes.swap(array, lo, hi, sleep, true, false);

                    lo++;
                    hi--;
                }
            }
            Writes.recordDepth(depth++);
            Writes.recursion(1);
            circleSortRoutine(array, low, low + mid, end, sleep, Reads, Writes, dir, depth);
            if (low + mid + 1 < end) {
                Writes.recordDepth(depth++);
                Writes.recursion(1);
                circleSortRoutine(array, low + mid + 1, high, end, sleep, Reads, Writes, !dir, depth);
            }
        }
    },
    MODULO {
        public String getName() {
            return "Modulo";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            for (int i = 1; i < currentLen; i++) {
                if ((i * 2) % currentLen != i)
                    Writes.swap(array, i, (i * 2) % currentLen, delay ? 1 : 0, true, false);
            }
        }
    },
    QSORT_BAD {
        public String getName() {
            return "Quicksort Adversary";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            for (int j = currentLen - currentLen % 2 - 2, i = j - 1; i >= 0; i -= 2, j--)
                Writes.swap(array, i, j, delay ? 1 : 0, true, false);
        }
    },
    PDQ_BAD {
        Reads Reads;
        Writes Writes;
        Highlights Highlights;
        boolean delay;
        double sleep;

        int[] temp;
        boolean hasCandidate;
        int gas, frozen, candidate;

        final class PDQPair {
            private int pivotPosition;
            private boolean alreadyPartitioned;

            private PDQPair(int pivotPos, boolean presorted) {
                this.pivotPosition = pivotPos;
                this.alreadyPartitioned = presorted;
            }

            public int getPivotPosition() {
                return this.pivotPosition;
            }

            public boolean getPresortBool() {
                return this.alreadyPartitioned;
            }
        }

        public String getName() {
            return "PDQ Adversary";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            delay = arrayVisualizer.shuffleEnabled();
            sleep = delay ? 1 : 0;
            this.Reads = arrayVisualizer.getReads();
            this.Writes = Writes;
            this.Highlights = Highlights;

            int[] copy = Writes.createExternalArray(currentLen);

            hasCandidate = false;
            frozen = 1;
            temp = Writes.createExternalArray(currentLen);
            gas = currentLen;
            for (int i = 0; i < currentLen; i++) {
                Writes.write(copy, i, array[i], sleep, true, false);
                Writes.write(array, i, i, sleep, true, false);
                Writes.write(temp, i, gas, sleep, true, true);
            }

            pdqLoop(array, 0, currentLen, false, PDQSorting.pdqLog(currentLen), 0);

            for (int i = 0; i < currentLen; i++) {
                Writes.write(array, i, copy[temp[i] - 1], sleep, true, false);
            }
            Writes.deleteExternalArray(temp);
            Writes.deleteExternalArray(copy);
        }

        protected int compare(int ap, int bp) {
            Reads.addComparison();
            int a, b;
            if (!hasCandidate) {
                candidate = 0;
                hasCandidate = true;
            }

            a = ap;
            b = bp;

            if (temp[a] == gas && temp[b] == gas)
                if (a == candidate)
                    Writes.write(temp, a, frozen++, sleep, true, true);
                else
                    Writes.write(temp, b, frozen++, sleep, true, true);

            if (temp[a] == gas) {
                candidate = a;
                return 1;
            }

            if (temp[b] == gas) {
                candidate = b;
                return -1;
            }

            if (temp[a] < temp[b])
                return -1;
            if (temp[a] > temp[b])
                return 1;
            return 0;
        }

        protected void pdqLoop(int[] array, int begin, int end, boolean branchless, int badAllowed, int depth) {
            boolean leftmost = true;

            while (true) {
                int size = end - begin;

                if (size < 24) {
                    if (leftmost)
                        this.pdqInsertSort(array, begin, end);
                    else
                        this.pdqUnguardInsertSort(array, begin, end);
                    return;
                }

                int halfSize = size / 2;
                if (size > 128) {
                    this.pdqSortThree(array, begin, begin + halfSize, end - 1);
                    this.pdqSortThree(array, begin + 1, begin + (halfSize - 1), end - 2);
                    this.pdqSortThree(array, begin + 2, begin + (halfSize + 1), end - 3);
                    this.pdqSortThree(array, begin + (halfSize - 1), begin + halfSize, begin + (halfSize + 1));
                    Writes.swap(array, begin, begin + halfSize, sleep, true, false);
                    Highlights.clearMark(2);
                } else
                    this.pdqSortThree(array, begin + halfSize, begin, end - 1);

                if (!leftmost && !(compare(array[begin - 1], array[begin]) < 0)) {
                    begin = this.pdqPartLeft(array, begin, end) + 1;
                    continue;
                }

                PDQPair partResult = this.pdqPartRight(array, begin, end);

                int pivotPos = partResult.getPivotPosition();
                boolean alreadyParted = partResult.getPresortBool();

                int leftSize = pivotPos - begin;
                int rightSize = end - (pivotPos + 1);
                boolean highUnbalance = leftSize < size / 8 || rightSize < size / 8;

                if (highUnbalance) {
                    if (--badAllowed == 0) {
                        int length = end - begin;
                        for (int i = length / 2; i >= 1; i--) {
                            siftDown(array, i, length, begin, sleep, true);
                        }
                        return;
                    }

                    if (leftSize >= 24) {
                        Writes.swap(array, begin, begin + leftSize / 4, sleep, true, false);
                        Writes.swap(array, pivotPos - 1, pivotPos - leftSize / 4, sleep, true, false);

                        if (leftSize > 128) {
                            Writes.swap(array, begin + 1, begin + (leftSize / 4 + 1), sleep, true, false);
                            Writes.swap(array, begin + 2, begin + (leftSize / 4 + 2), sleep, true, false);
                            Writes.swap(array, pivotPos - 2, pivotPos - (leftSize / 4 + 1), sleep, true, false);
                            Writes.swap(array, pivotPos - 3, pivotPos - (leftSize / 4 + 2), sleep, true, false);
                        }
                    }

                    if (rightSize >= 24) {
                        Writes.swap(array, pivotPos + 1, pivotPos + (1 + rightSize / 4), sleep, true, false);
                        Writes.swap(array, end - 1, end - rightSize / 4, sleep, true, false);

                        if (rightSize > 128) {
                            Writes.swap(array, pivotPos + 2, pivotPos + (2 + rightSize / 4), sleep, true, false);
                            Writes.swap(array, pivotPos + 3, pivotPos + (3 + rightSize / 4), sleep, true, false);
                            Writes.swap(array, end - 2, end - (1 + rightSize / 4), sleep, true, false);
                            Writes.swap(array, end - 3, end - (2 + rightSize / 4), sleep, true, false);
                        }
                    }
                    Highlights.clearMark(2);
                } else {
                    if (alreadyParted && pdqPartialInsertSort(array, begin, pivotPos)
                            && pdqPartialInsertSort(array, pivotPos + 1, end))
                        return;
                }
                Writes.recordDepth(depth++);
                Writes.recursion(1);
                this.pdqLoop(array, begin, pivotPos, branchless, badAllowed, depth);
                begin = pivotPos + 1;
                leftmost = false;
            }
        }

        private void siftDown(int[] array, int root, int dist, int start, double sleep, boolean isMax) {
            int compareVal = 0;

            if (isMax)
                compareVal = -1;
            else
                compareVal = 1;

            while (root <= dist / 2) {
                int leaf = 2 * root;
                if (leaf < dist && compare(array[start + leaf - 1], array[start + leaf]) == compareVal) {
                    leaf++;
                }
                Highlights.markArray(1, start + root - 1);
                Highlights.markArray(2, start + leaf - 1);
                if (compare(array[start + root - 1], array[start + leaf - 1]) == compareVal) {
                    Writes.swap(array, start + root - 1, start + leaf - 1, sleep, true, false);
                    root = leaf;
                } else
                    break;
            }
        }

        private PDQPair pdqPartRight(int[] array, int begin, int end) {
            int pivot = array[begin];
            int first = begin;
            int last = end;

            while (compare(array[++first], pivot) < 0) {
                Highlights.markArray(1, first);
            }

            if (first - 1 == begin)
                while (first < last && !(compare(array[--last], pivot) < 0)) {
                    Highlights.markArray(2, last);
                }
            else
                while (!(compare(array[--last], pivot) < 0)) {
                    Highlights.markArray(2, last);
                }

            boolean alreadyParted = first >= last;

            while (first < last) {
                Writes.swap(array, first, last, sleep, true, false);
                while (compare(array[++first], pivot) < 0) {
                    Highlights.markArray(1, first);
                }
                while (!(compare(array[--last], pivot) < 0)) {
                    Highlights.markArray(2, last);
                }
            }
            Highlights.clearMark(2);

            int pivotPos = first - 1;
            Writes.write(array, begin, array[pivotPos], delay ? 1 : 0, true, false);
            Writes.write(array, pivotPos, pivot, delay ? 1 : 0, true, false);

            return new PDQPair(pivotPos, alreadyParted);
        }

        private boolean pdqPartialInsertSort(int[] array, int begin, int end) {
            if (begin == end)
                return true;

            double sleep = delay ? 1 / 3d : 0;

            int limit = 0;
            for (int cur = begin + 1; cur != end; ++cur) {
                if (limit > 8)
                    return false;

                int sift = cur;
                int siftMinusOne = cur - 1;

                if (compare(array[sift], array[siftMinusOne]) < 0) {
                    int tmp = array[sift];

                    do {
                        Writes.write(array, sift--, array[siftMinusOne], sleep, true, false);
                    } while (sift != begin && compare(tmp, array[--siftMinusOne]) < 0);

                    Writes.write(array, sift, tmp, sleep, true, false);
                    limit += cur - sift;
                }
            }
            return true;
        }

        private int pdqPartLeft(int[] array, int begin, int end) {
            int pivot = array[begin];
            int first = begin;
            int last = end;

            while (compare(pivot, array[--last]) < 0) {
                Highlights.markArray(2, last);
            }

            if (last + 1 == end)
                while (first < last && !(compare(pivot, array[++first]) < 0)) {
                    Highlights.markArray(1, first);
                }
            else
                while (!(compare(pivot, array[++first]) < 0)) {
                    Highlights.markArray(1, first);
                }

            while (first < last) {
                Writes.swap(array, first, last, sleep, true, false);
                while (compare(pivot, array[--last]) < 0) {
                    Highlights.markArray(2, last);
                }
                while (!(compare(pivot, array[++first]) < 0)) {
                    Highlights.markArray(1, first);
                }
            }
            Highlights.clearMark(2);

            int pivotPos = last;
            Writes.write(array, begin, array[pivotPos], sleep, true, false);
            Writes.write(array, pivotPos, pivot, sleep, true, false);

            return pivotPos;
        }

        private void pdqSortThree(int[] array, int a, int b, int c) {
            this.pdqSortTwo(array, a, b);
            this.pdqSortTwo(array, b, c);
            this.pdqSortTwo(array, a, b);
        }

        private void pdqSortTwo(int[] array, int a, int b) {
            if (compare(array[b], array[a]) < 0) {
                Writes.swap(array, a, b, sleep, true, false);
            }
            Highlights.clearMark(2);
        }

        private void pdqInsertSort(int[] array, int begin, int end) {
            if (begin == end)
                return;

            double sleep = delay ? 1 / 3d : 0;

            for (int cur = begin + 1; cur != end; ++cur) {
                int sift = cur;
                int siftMinusOne = cur - 1;

                if (compare(array[sift], array[siftMinusOne]) < 0) {
                    int tmp = array[sift];
                    do {
                        Writes.write(array, sift--, array[siftMinusOne], sleep, true, false);
                    } while (sift != begin && compare(tmp, array[--siftMinusOne]) < 0);

                    Writes.write(array, sift, tmp, sleep, true, false);
                }
            }
        }

        private void pdqUnguardInsertSort(int[] array, int begin, int end) {
            if (begin == end)
                return;

            double sleep = 1 / 3d;

            for (int cur = begin + 1; cur != end; ++cur) {
                int sift = cur;
                int siftMinusOne = cur - 1;

                if (compare(array[sift], array[siftMinusOne]) < 0) {
                    int tmp = array[sift];

                    do {
                        Writes.write(array, sift--, array[siftMinusOne], sleep, true, false);
                    } while (compare(tmp, array[--siftMinusOne]) < 0);

                    Writes.write(array, sift, tmp, sleep, true, false);
                }
            }
        }
    },
    GRAIL_BAD {
        public String getName() {
            return "Grailsort Adversary";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            if (currentLen <= 16)
                Writes.reversal(array, 0, currentLen - 1, delay ? 1 : 0, true, false);
            else {
                int blockLen = 1;
                while (blockLen * blockLen < currentLen)
                    blockLen *= 2;

                int numKeys = (currentLen - 1) / blockLen + 1;
                int keys = blockLen + numKeys;
                shuffle(array, 0, currentLen, delay ? 0.25 : 0, Writes);
                sort(array, 0, keys, delay ? 0.25 : 0, Writes);
                Writes.reversal(array, 0, keys - 1, delay ? 0.25 : 0, true, false);
                Highlights.clearMark(2);
                sort(array, keys, currentLen, delay ? 0.25 : 0, Writes);
                Highlights.clearMark(2);

                push(array, keys, currentLen, blockLen, delay ? 1 : 0, Writes, 0);
            }
        }

        public void rotate(int[] array, int a, int m, int b, double sleep, Writes Writes) {
            IndexedRotations.holyGriesMills(array, a, m, b, sleep, true, false);
        }

        public void push(int[] array, int a, int b, int bLen, double sleep, Writes Writes, int depth) {
            int len = b - a,
                    b1 = b - len % bLen, len1 = b1 - a;
            if (len1 <= 2 * bLen)
                return;

            int m = bLen;
            while (2 * m < len)
                m *= 2;
            m += a;

            if (b1 - m < bLen) {
                Writes.recordDepth(depth++);
                Writes.recursion(1);
                push(array, a, m, bLen, sleep, Writes, depth);
            } else {
                m = a + b1 - m;
                rotate(array, m - (bLen - 2), b1 - (bLen - 1), b1, sleep, Writes);
                Writes.multiSwap(array, a, m, sleep / 2, true, false);
                rotate(array, a, m, b1, sleep, Writes);
                m = a + b1 - m;
                Writes.recordDepth(depth++);
                Writes.recursion(2);
                push(array, a, m, bLen, sleep / 2, Writes, depth);
                push(array, m, b, bLen, sleep / 2, Writes, depth);
            }
        }
    },
    SHUF_MERGE_BAD {
        public String getName() {
            return "Shuffle Merge Adversary";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int n = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int[] tmp = Writes.createExternalArray(n);
            int d = 2, end = 1 << (int) (Math.log(n - 1) / Math.log(2) + 1);

            while (d <= end) {
                int i = 0, dec = 0;
                double sleep = 1d / d;

                while (i < n) {
                    int j = i;
                    dec += n;
                    while (dec >= d) {
                        dec -= d;
                        j++;
                    }
                    int k = j;
                    dec += n;
                    while (dec >= d) {
                        dec -= d;
                        k++;
                    }
                    shuffleMergeBad(array, tmp, i, j, k, delay ? sleep : 0, Writes);
                    i = k;
                }
                d *= 2;
            }
            Writes.deleteExternalArray(tmp);
        }

        public void shuffleMergeBad(int[] array, int[] tmp, int a, int m, int b, double sleep, Writes Writes) {
            if ((b - a) % 2 == 1) {
                if (m - a > b - m)
                    a++;
                else
                    b--;
            }
            shuffleBad(array, tmp, a, b, sleep, Writes);
        }

        // length is always even
        public void shuffleBad(int[] array, int[] tmp, int a, int b, double sleep, Writes Writes) {
            if (b - a < 2)
                return;

            int m = (a + b) / 2;
            int s = (b - a - 1) / 4 + 1;

            a = m - s;
            b = m + s;
            int j = a;

            for (int i = a + 1; i < b; i += 2)
                Writes.write(tmp, j++, array[i], sleep, true, true);
            for (int i = a; i < b; i += 2)
                Writes.write(tmp, j++, array[i], sleep, true, true);

            Writes.arraycopy(tmp, a, array, a, b - a, sleep, true, false);
        }
    },
    BIT_REVERSE {
        @Override
        public String getName() {
            return "Bit Reversal";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            int len = 1 << (int) (Math.log(arrayVisualizer.getCurrentLength()) / Math.log(2));
            boolean delay = arrayVisualizer.shuffleEnabled();
            boolean pow2 = len == currentLen;

            int[] temp = Writes.copyOfArray(array, currentLen);
            for (int i = 0; i < len; i++)
                Writes.write(array, i, i, delay ? 1 : 0, true, false);

            int m = 0;
            int d1 = len >> 1, d2 = d1 + (d1 >> 1);

            for (int i = 1; i < len - 1; i++) {
                int j = d1;

                for (int k = i, n = d2; (k & 1) == 0; j -= n, k >>= 1, n >>= 1)
                    ;
                m += j;
                if (m > i)
                    Writes.swap(array, i, m, delay ? 1 : 0, true, false);
            }
            Highlights.clearMark(2);

            if (!pow2) {
                for (int i = len; i < currentLen; i++)
                    Writes.write(array, i, array[i - len], 0.5, true, false);

                int[] cnt = Writes.createExternalArray(len);

                for (int i = 0; i < currentLen; i++)
                    Writes.write(cnt, array[i], cnt[array[i]] + 1, delay ? 1 : 0, true,
                            true);

                for (int i = 1; i < cnt.length; i++)
                    Writes.write(cnt, i, cnt[i] + cnt[i - 1], delay ? 1 : 0, true,
                            true);

                for (int i = currentLen - 1; i >= 0; i--)
                    Writes.write(array, i, --cnt[array[i]], 0.5, true, false);
                Writes.deleteExternalArray(cnt);
            }
            int[] bits = Writes.copyOfArray(array, currentLen);

            for (int i = 0; i < currentLen; i++)
                Writes.write(array, i, temp[bits[i]], delay ? 1 : 0, true, false);
            Writes.deleteExternalArray(temp);
            Writes.deleteExternalArray(bits);
        }
    },
    BLOCK_RANDOMLY {
        @Override
        public String getName() {
            return "Randomly w/ Blocks";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            int blockSize = pow2lte((int) Math.sqrt(currentLen));
            currentLen -= currentLen % blockSize;
            boolean delay = arrayVisualizer.shuffleEnabled();
            double sleep = delay ? 1 : 0;

            Random random = new Random();
            for (int i = 0; i < currentLen; i += blockSize) {
                int randomIndex = random.nextInt((currentLen - i) / blockSize) * blockSize + i;
                blockSwap(array, i, randomIndex, blockSize, Writes, sleep);
            }
        }

        private void blockSwap(int[] array, int a, int b, int len, Writes Writes, double sleep) {
            for (int i = 0; i < len; i++) {
                Writes.swap(array, a + i, b + i, sleep, true, false);
            }
        }

        private int pow2lte(int value) {
            int val;
            for (val = 1; val <= value; val <<= 1)
                ;
            return val >> 1;
        }
    },
    CRAZY_BLOCK_RANDOMLY {
        @Override
        public String getName() {
            return "Randomly w/ Crazy Blocks";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            int blockSize = pow2lte((int) Math.sqrt(currentLen));
            currentLen -= currentLen % blockSize;
            boolean delay = arrayVisualizer.shuffleEnabled();
            double sleep = delay ? 1 : 0;

            Random random = new Random();
            for (int i = 0; i < currentLen; i += blockSize) {
                int randomIndex = random.nextInt((currentLen - i) / blockSize) * blockSize + i;
                blockSwap(array, i, randomIndex, blockSize, writes, sleep);
            }

            for (int i = 0; i < currentLen; i += blockSize) {
                if (random.nextBoolean()) {
                    writes.reversal(array, i, i + blockSize - 1, sleep * 4, true, false);
                }
            }

        }

        private void blockSwap(int[] array, int a, int b, int len, Writes Writes, double sleep) {
            for (int i = 0; i < len; i++) {
                if (a != b)
                    Writes.swap(array, a + i, b + i, sleep, true, false);
            }
        }

        private int pow2lte(int value) {
            int val;
            for (val = 1; val <= value; val <<= 1)
                ;
            return val >> 1;
        }
    },
    BLOCK_REVERSE {
        @Override
        public String getName() {
            return "Block Reverse";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            int blockSize = pow2lte((int) Math.sqrt(currentLen));
            currentLen -= currentLen % blockSize;
            boolean delay = arrayVisualizer.shuffleEnabled();
            double sleep = delay ? 1 : 0;

            int i = 0, j = currentLen - blockSize;
            while (i < j) {
                blockSwap(array, i, j, blockSize, Writes, sleep);
                i += blockSize;
                j -= blockSize;
            }
        }

        private void blockSwap(int[] array, int a, int b, int len, Writes Writes, double sleep) {
            for (int i = 0; i < len; i++) {
                Writes.swap(array, a + i, b + i, sleep, true, false);
            }
        }

        private int pow2lte(int value) {
            int val;
            for (val = 1; val <= value; val <<= 1)
                ;
            return val >> 1;
        }
    },
    PRIME {
        @Override
        public String getName() {
            return "Prime Numbers";
        }

        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            boolean delay = ArrayVisualizer.shuffleEnabled();

            for (int i = 0; i < currentLen; i++) {
                if (!isPrime(array[i]))
                    Writes.write(array, i, 0, delay ? 1 : 0, true, false);
                else {
                    Highlights.markArray(1, i);
                    Delays.sleep(delay ? 1 : 0);
                }
            }
        }

        protected boolean isPrime(int n) {
            if (n < 2)
                return false;
            for (int i = 2; i < n; i++)
                if (n % i == 0)
                    return false;
            return true;
        }
    };

    public void sort(int[] array, int start, int end, double sleep, Writes Writes) {
        int min = array[start], max = min;
        for (int i = start + 1; i < end; i++) {
            if (array[i] < min)
                min = array[i];
            else if (array[i] > max)
                max = array[i];
        }

        int size = max - min + 1;
        int[] holes = new int[size];

        for (int i = start; i < end; i++)
            Writes.write(holes, array[i] - min, holes[array[i] - min] + 1, 0, false, true);

        for (int i = 0, j = start; i < size; i++) {
            while (holes[i] > 0) {
                Writes.write(holes, i, holes[i] - 1, 0, false, true);
                Writes.write(array, j, i + min, sleep, true, false);
                j++;
            }
        }
    }

    public void shuffle(int[] array, int start, int end, double sleep, Writes Writes) {
        Random random = new Random();
        for (int i = start; i < end; i++) {
            int randomIndex = random.nextInt(end - i) + i;
            Writes.swap(array, i, randomIndex, sleep, true, false);
        }
    }

    public abstract String getName();

    public abstract void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays,
            Highlights Highlights, Writes Writes);
}
