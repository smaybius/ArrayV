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

        @Override
        public String getName() {
            return "Random";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            shuffle(array, 0, currentLen, delay ? 1 : 0, writes);
        }
    },
    NCRANDOM {
        @Override
        public String getName() {
            return "Non-Cyclic Random";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            Random r = new Random();
            boolean[] cycle = new boolean[currentLen];
            for (int i = 0; i < currentLen - 1; i++) {
                if (cycle[i])
                    continue;
                boolean cease = true;
                for (int j = i + 1; j < currentLen; j++) {
                    cease = cease && cycle[j];
                }
                if (cease)
                    break;
                int z;
                do {
                    z = r.nextInt(currentLen - i - 1) + i + 1;
                    highlights.markArray(1, z);
                    delays.sleep(delay ? 0.5 : 0.00025);
                } while (cycle[z]);
                Writes.swap(array, i, z, delay ? 1 : 0, true, false);
                cycle[z] = true;
            }
        }
    },
    REVERSE {
        @Override
        public String getName() {
            return "Reverse";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            writes.reversal(array, 0, currentLen - 1, delay ? 1 : 0, true, false);
        }
    },
    ALMOST {
        @Override
        public String getName() {
            return "Slight Shuffle";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();

            for (int i = 0; i < Math.max(currentLen / 20, 1); i++) {
                writes.swap(array, random.nextInt(currentLen), random.nextInt(currentLen), 0, true, false);

                if (arrayVisualizer.shuffleEnabled())
                    delays.sleep(10);
            }
        }
    },
    ALREADY {
        @Override
        public String getName() {
            return "No Shuffle";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            for (int i = 0; i < currentLen; i++) {
                highlights.markArray(1, i);
                if (arrayVisualizer.shuffleEnabled())
                    delays.sleep(1);
            }
        }
    },
    SORTED {
        @Override
        public String getName() {
            return "Sorted";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            this.sort(array, 0, currentLen, delay ? 1 : 0, writes);
        }
    },
    NAIVE {
        @Override
        public String getName() {
            return "Naive Random";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            for (int i = 0; i < currentLen; i++)
                writes.swap(array, i, random.nextInt(currentLen), delay ? 1 : 0, true, false);
        }
    },
    SHUFFLED_TAIL {
        @Override
        public String getName() {
            return "Scrambled Tail";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int[] aux = writes.createExternalArray(currentLen);
            int i = 0;
            int j = 0;
            int k = 0;
            while (i < currentLen) {
                highlights.markArray(2, i);
                if (random.nextDouble() < 1 / 7d)
                    writes.write(aux, k++, array[i++], delay ? 1 : 0, true, true);
                else
                    writes.write(array, j++, array[i++], delay ? 1 : 0, true, false);
            }
            writes.arraycopy(aux, 0, array, j, k, delay ? 1 : 0, true, false);
            shuffle(array, j, currentLen, delay ? 2 : 0, writes);
            writes.deleteExternalArray(aux);
        }
    },
    SHUFFLED_HEAD {
        @Override
        public String getName() {
            return "Scrambled Head";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int[] aux = writes.createExternalArray(currentLen);
            int i = currentLen - 1;
            int j = currentLen - 1;
            int k = 0;
            while (i >= 0) {
                highlights.markArray(2, i);
                if (random.nextDouble() < 1 / 7d)
                    writes.write(aux, k++, array[i--], delay ? 1 : 0, true, true);
                else
                    writes.write(array, j--, array[i--], delay ? 1 : 0, true, false);
            }
            writes.arraycopy(aux, 0, array, 0, k, delay ? 1 : 0, true, false);
            shuffle(array, 0, j, delay ? 2 : 0, writes);
            writes.deleteExternalArray(aux);
        }
    },
    SHUFFLED_BOTHSIDES {
        @Override
        public String getName() {
            return "Scrambled Both Sides";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int[] aux = writes.createExternalArray(currentLen);
            // head
            int i = currentLen - 1;
            int j = currentLen - 1;
            int k = 0;
            while (i >= 0) {
                highlights.markArray(2, i);
                if (random.nextDouble() < 1 / 7d)
                    writes.write(aux, k++, array[i--], delay ? 1 : 0, true, true);
                else
                    writes.write(array, j--, array[i--], delay ? 1 : 0, true, false);
            }
            writes.arraycopy(aux, 0, array, 0, k, delay ? 1 : 0, true, false);
            shuffle(array, 0, j, delay ? 2 : 0, writes);
            // tail
            i = 0;
            j = 0;
            k = 0;
            while (i < currentLen) {
                highlights.markArray(2, i);
                if (random.nextDouble() < 1 / 7d)
                    writes.write(aux, k++, array[i++], delay ? 1 : 0, true, true);
                else
                    writes.write(array, j++, array[i++], delay ? 1 : 0, true, false);
            }
            writes.arraycopy(aux, 0, array, j, k, delay ? 1 : 0, true, false);
            shuffle(array, j, currentLen, delay ? 2 : 0, writes);
            writes.deleteExternalArray(aux);
        }
    },
    MOVED_ELEMENT {
        @Override
        public String getName() {
            return "Shifted Element";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int start = random.nextInt(currentLen);
            int dest = random.nextInt(currentLen);
            if (dest < start) {
                IndexedRotations.adaptable(array, dest, start, start + 1, delay ? 1 : 0, true, false);
            } else {
                IndexedRotations.adaptable(array, start, start + 1, dest, delay ? 1 : 0, true, false);
            }
        }
    },
    MOVED_BLOCK {
        @Override
        public String getName() {
            return "Shifted Block";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            int blockSize = pow2lte((int) Math.sqrt(currentLen));
            currentLen -= currentLen % blockSize;
            boolean delay = arrayVisualizer.shuffleEnabled();
            Random random = new Random();

            int[] backtrack = new int[currentLen];
            Writes.arraycopy(array, 0, backtrack, 0, currentLen, 0, false, true);
            boolean anyswaps = false;
            while (!anyswaps) {
                int start = blockSize * random.nextInt((currentLen / blockSize));
                int dest = blockSize * random.nextInt(((currentLen / blockSize) + 1));
                if (dest < start) {
                    IndexedRotations.adaptable(array, dest, start, start + blockSize, delay ? 1 : 0, true, false);
                } else {
                    IndexedRotations.adaptable(array, start, start + blockSize, dest, delay ? 1 : 0, true, false);
                }
                for (int i = 0; i < currentLen; i++) {
                    if (array[i] != backtrack[i]) {
                        anyswaps = true;
                        break;
                    }
                }
            }
        }

        private int pow2lte(int value) {
            int val;
            for (val = 1; val <= value; val <<= 1)
                ;
            return val >> 1;
        }
    },
    RANDOM_SWAP {
        @Override
        public String getName() {
            return "Random Swap";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int start = random.nextInt(currentLen);
            int dest = random.nextInt(currentLen);
            writes.swap(array, start, dest, delay ? 1 : 0, true, false);
        }
    },
    RANDOM_PULL {
        @Override
        public String getName() {
            return "Random Pull";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int start = random.nextInt(currentLen);
            int dest = random.nextInt(currentLen);
            writes.multiSwap(array, start, dest, delay ? 1 : 0, true, false);
        }
    },
    RANDOM_BLOCKSWAP {
        @Override
        public String getName() {
            return "Random Blockswap";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            this.writes = writes;
            int len = random.nextInt(currentLen);
            int start = random.nextInt(currentLen - len);
            int dest = random.nextInt(currentLen - len);
            boolean dir = random.nextBoolean();

            if (start + len >= currentLen) { // add / 2 to currentLen on the int declarers for start dest and len
                dir = false;
            }
            if (dest + len >= currentLen) {
                dir = false;
            }

            if (start + len <= 0) {
                dir = true;
            }
            if (dest + len <= 0) {
                dir = true;
            }

            if (dir)
                blockSwap(array, start, dest, len, delay ? 1 : 0);
            else
                blockSwapBackwards(array, start, dest, len, delay ? 1 : 0);
        }

        private void blockSwapBackwards(int[] array, int a, int b, int len, double pause) {
            for (int i = 0; i < len; i++) {
                writes.swap(array, a + len - i - 1, b + len - i - 1, pause, true, false);
            }
        }

        private void blockSwap(int[] array, int a, int b, int len, double pause) {
            for (int i = 0; i < len; i++) {
                writes.swap(array, a + i, b + i, pause, true, false);
            }
        }
    },
    RANDOM_ROTATION {
        @Override
        public String getName() {
            return "Random Rotation";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            int a = random.nextInt(currentLen);
            int b = random.nextInt(currentLen);
            int start = Math.min(a, b);
            int end = Math.max(a, b) + 1;
            int mid = random.nextInt(start, end);
            IndexedRotations.adaptable(array, start, mid, end, delay ? 1 : 0, true, false);
        }
    },
    FIRST_LAST {
        @Override
        public String getName() {
            return "First Item Last";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int item = array[0];
            for (int i = 0; i < currentLen - 1; i++) {
                Writes.write(array, i, array[i + 1], delay ? 1 : 0, true, false);
            }
            Writes.write(array, currentLen - 1, item, delay ? 1 : 0, true, false);
        }
    },
    LAST_FIRST {
        @Override
        public String getName() {
            return "Last Item First";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int item = array[currentLen - 1];
            for (int i = currentLen - 1; i > 0; i--) {
                Writes.write(array, i, array[i - 1], delay ? 1 : 0, true, false);
            }
            Writes.write(array, 0, item, delay ? 1 : 0, true, false);
        }
    },
    SWAPPED_ENDS {
        @Override
        public String getName() {
            return "Swapped Ends";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            writes.swap(array, 0, currentLen - 1, delay ? 1 : 0, true, false);
        }
    },
    SWAPPED_PAIRS {
        @Override
        public String getName() {
            return "Swapped Pairs";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            for (int i = 1; i < currentLen; i += 2)
                writes.swap(array, i - 1, i, delay ? 0.5 : 0, true, false);
        }
    },
    RANDOM_REVERSAL {
        @Override
        public String getName() {
            return "Random Reversal";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int start = random.nextInt(currentLen);
            int dest = random.nextInt(currentLen);
            writes.reversal(array, start, dest, delay ? 1 : 0, true, false);
        }
    },
    NOISY {
        @Override
        public String getName() {
            return "Noisy";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int i;
            int size = Math.max(4, (int) (Math.sqrt(currentLen) / 2));
            for (i = 0; i + size <= currentLen; i += random.nextInt(size - 1) + 1)
                shuffle(array, i, i + size, delay ? 0.5 : 0, writes);
            shuffle(array, i, currentLen, delay ? 0.5 : 0, writes);
        }
    },
    SHUFFLED_ODDS {
        @Override
        public String getName() {
            return "Scrambled Odds";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();

            for (int i = 1; i < currentLen; i += 2) {
                int randomIndex = ((random.nextInt(currentLen - i) / 2) * 2) + i;
                writes.swap(array, i, randomIndex, 0, true, false);

                if (arrayVisualizer.shuffleEnabled())
                    delays.sleep(2);
            }
        }
    },
    FINAL_MERGE {
        @Override
        public String getName() {
            return "Final Merge Pass";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            int count = 2;

            int k = 0;
            int[] temp = writes.createExternalArray(currentLen);

            for (int j = 0; j < count; j++)
                for (int i = j; i < currentLen; i += count) {
                    highlights.markArray(2, array[i]);
                    writes.write(temp, k++, array[i], delay ? 1 : 0, true, true);
                }
            highlights.clearMark(2);
            for (int i = 0; i < currentLen; i++)
                writes.write(array, i, temp[i], delay ? 1 : 0, true, false);
            writes.deleteExternalArray(temp);
        }
    },
    REAL_FINAL_MERGE {
        @Override
        public String getName() {
            return "Shuffled Final Merge";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            this.shuffle(array, 0, currentLen, delay ? 0.5 : 0, writes);
            highlights.clearMark(2);
            this.sort(array, 0, currentLen / 2, delay ? 0.5 : 0, writes);
            this.sort(array, currentLen / 2, currentLen, delay ? 0.5 : 0, writes);
        }
    },
    SHUFFLED_SECOND_HALF {
        @Override
        public String getName() {
            return "Scrambled Second Half";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int[] aux = writes.createExternalArray(currentLen);
            int i = 0;
            int j = 0;
            int k = 0;
            while (i < currentLen) {
                if (random.nextDouble() < 1 / 2d)
                    writes.write(aux, k++, array[i++], delay ? 0.5 : 0, true, true);
                else
                    writes.write(array, j++, array[i++], delay ? 0.5 : 0, true, false);
            }
            writes.arraycopy(aux, 0, array, j, k, delay ? 0.5 : 0, true, false);
            shuffle(array, j, currentLen, delay ? 1 : 0, writes);
            writes.deleteExternalArray(aux);
        }
    },
    SHUFFLED_FIRST_HALF {
        @Override
        public String getName() {
            return "Scrambled First Half";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int[] aux = writes.createExternalArray(currentLen);
            int i = currentLen - 1;
            int j = currentLen - 1;
            int k = 0;
            while (i >= 0) {
                if (random.nextDouble() < 1 / 2d)
                    writes.write(aux, k++, array[i--], delay ? 0.5 : 0, true, true);
                else
                    writes.write(array, j--, array[i--], delay ? 0.5 : 0, true, false);
            }
            writes.arraycopy(aux, 0, array, 0, k, delay ? 0.5 : 0, true, false);
            shuffle(array, 0, j, delay ? 1 : 0, writes);
            writes.deleteExternalArray(aux);
        }
    },
    PARTITIONED {
        @Override
        public String getName() {
            return "Partitioned";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            this.sort(array, 0, currentLen, delay ? 0.5 : 0, writes);
            highlights.clearMark(2);
            this.shuffle(array, 0, currentLen / 2, delay ? 0.5 : 0, writes);
            this.shuffle(array, currentLen / 2, currentLen, delay ? 0.5 : 0, writes);
        }
    },
    QUICK_PARTITIONED {
        @Override
        public String getName() {
            return "Quick Partitioned";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            this.sort(array, 0, currentLen, delay ? 0.5 : 0, writes);
            highlights.clearMark(2);
            for (int i = 1; i < currentLen - 1; i *= 2) {
                this.shuffle(array, i, i * 2, delay ? 0.5 : 0, writes);
            }

        }
    },
    INCREASING_REVERSALS {
        @Override
        public String getName() {
            return "Increasingly Reversed";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            this.sort(array, 0, currentLen, delay ? 0.5 : 0, writes);
            highlights.clearMark(2);
            for (int i = 1; i < currentLen - 1; i *= 2) {
                writes.reversal(array, i, i * 2, delay ? 0.5 : 0, true, false);
            }

        }
    },
    INCREASING_SWAPS { // TODO: Is this accurate?
        @Override
        public String getName() {
            return "Increasingly Swapped";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            this.sort(array, 0, currentLen, delay ? 0.5 : 0, writes);
            highlights.clearMark(2);
            for (int i = 1; i < currentLen - 1; i *= 2) {
                writes.swap(array, i, i * 2, delay ? 0.5 : 0, true, false);
            }

        }
    },
    STRANGE_PASS {
        @Override
        public String getName() {
            return "First Strange Pass";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            this.reads = arrayVisualizer.getReads();

            this.shuffle(array, 0, currentLen, delay ? 0.05 : 0, writes);
            int offset = 1;
            double mult = 1;
            int bound = 1;
            while (offset != currentLen) {
                mult = 1;
                bound = 1;
                while (offset + mult <= currentLen) {
                    if (reads.compareIndices(array, (int) (offset + mult / 2) - 1, (int) (offset + mult) - 1,
                            0, true) > 0) {
                        writes.swap(array, (int) (offset + mult / 2) - 1, (int) (offset + mult) - 1, delay ? 0.005 : 0,
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
        @Override
        public String getName() {
            return "Sawtooth";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            int count = 4;

            int k = 0;
            int[] temp = writes.createExternalArray(currentLen);

            for (int j = 0; j < count; j++)
                for (int i = j; i < currentLen; i += count) {
                    highlights.markArray(2, array[i]);
                    writes.write(temp, k++, array[i], delay ? 1 : 0, true, true);
                }
            highlights.clearMark(2);
            for (int i = 0; i < currentLen; i++)
                writes.write(array, i, temp[i], delay ? 1 : 0, true, false);
            writes.deleteExternalArray(temp);
        }
    },
    ORGAN {
        @Override
        public String getName() {
            return "Pipe Organ";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            int[] temp = writes.createExternalArray(currentLen);

            for (int i = 0, j = 0; i < currentLen; i += 2) {
                highlights.markArray(2, array[i]);
                writes.write(temp, j++, array[i], delay ? 1 : 0, true, true);
            }
            for (int i = 1, j = currentLen; i < currentLen; i += 2) {
                highlights.markArray(2, array[i]);
                writes.write(temp, --j, array[i], delay ? 1 : 0, true, true);
            }
            highlights.clearMark(2);
            for (int i = 0; i < currentLen; i++) {
                writes.write(array, i, temp[i], delay ? 1 : 0, true, false);
            }
        }
    },
    FINAL_BITONIC {
        @Override
        public String getName() {
            return "Final Bitonic Pass";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            int[] temp = writes.createExternalArray(currentLen);

            writes.reversal(array, 0, currentLen - 1, delay ? 1 : 0, true, false);
            highlights.clearMark(2);
            for (int i = 0, j = 0; i < currentLen; i += 2) {
                highlights.markArray(2, array[i]);
                writes.write(temp, j++, array[i], delay ? 1 : 0, true, true);
            }
            for (int i = 1, j = currentLen; i < currentLen; i += 2) {
                highlights.markArray(2, array[i]);
                writes.write(temp, --j, array[i], delay ? 1 : 0, true, true);
            }
            highlights.clearMark(2);
            for (int i = 0; i < currentLen; i++) {
                writes.write(array, i, temp[i], delay ? 1 : 0, true, false);
            }
        }
    },
    PENULTIMATE_BITONIC {
        @Override
        public String getName() {
            return "Penultimate Bitonic Pass";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            this.reads = arrayVisualizer.getReads();
            boolean delay = arrayVisualizer.shuffleEnabled();
            int count = 2;

            int k = 0;
            int[] temp = writes.createExternalArray(currentLen);

            for (int j = 0; j < count; j++)
                for (int i = j; i < currentLen; i += count) {
                    highlights.markArray(2, i);
                    writes.write(temp, k++, array[i], delay ? 1 : 0, true, true);
                }
            highlights.clearMark(2);
            for (int i = 0; i < currentLen; i++)
                writes.write(array, i, temp[i], delay ? 1 : 0, true, false);
            writes.deleteExternalArray(temp);
            for (int i = 0, j = currentLen - 1; i < currentLen / 2; i++, j--) {
                highlights.markArray(1, i);
                highlights.markArray(2, j);
                delays.sleep(delay ? 0.01 : 0);
                if (reads.compareIndices(array, i, j, 0.1, true) > 0)
                    writes.swap(array, i, j, delay ? 1 : 0, true, false);
            }
        }
    },
    TRIANGLE_WAVE {
        @Override
        public String getName() {
            return "Triangle Wave";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            int[] temp = writes.createExternalArray(currentLen);

            for (int i = 0, j = 0; i < currentLen; i += 2) {
                highlights.markArray(2, array[i]);
                writes.write(temp, j++, array[i], delay ? 1 : 0, true, true);
            }
            for (int i = 1, j = currentLen; i < currentLen; i += 2) {
                highlights.markArray(2, array[i]);
                writes.write(temp, --j, array[i], delay ? 1 : 0, true, true);
            }
            highlights.clearMark(2);
            for (int i = 0; i < currentLen; i++) {
                writes.write(array, i, temp[i], delay ? 1 : 0, true, false);
            }
            int a = 0;
            int m = (currentLen + 1) / 4;

            if (currentLen % 2 == 0)
                while (m < currentLen)
                    writes.swap(array, a++, m++, delay ? 1 : 0, true, false);
            else {
                highlights.clearMark(2);
                int tempv = array[a];
                while (m < currentLen) {
                    writes.write(array, a++, array[m], delay ? 0.5 : 0, true, false);
                    writes.write(array, m++, array[a], delay ? 0.5 : 0, true, false);
                }
                writes.write(array, a, tempv, delay ? 0.5 : 0, true, false);
            }
        }
    },
    REVERSE_FINAL_MERGE {
        @Override
        public String getName() {
            return "Reverse Final Merge Pass";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            int count = 2;

            int k = 0;
            int[] temp = writes.createExternalArray(currentLen);

            for (int j = 0; j < count; j++)
                for (int i = j; i < currentLen; i += count) {
                    highlights.markArray(2, i);
                    writes.write(temp, k++, array[i], delay ? 1 : 0, true, true);
                }
            highlights.clearMark(2);

            for (int i = 0; i < currentLen; i++)
                writes.write(array, i, temp[i], delay ? 1 : 0, true, false);
            writes.deleteExternalArray(temp);
            writes.reversal(array, 0, currentLen, delay ? 1 : 0, true, false);
        }
    },
    REVERSE_SAWTOOTH {
        @Override
        public String getName() {
            return "Reverse Sawtooth";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            int count = 4;

            int k = 0;
            int[] temp = writes.createExternalArray(currentLen);

            for (int j = 0; j < count; j++)
                for (int i = j; i < currentLen; i += count) {
                    highlights.markArray(2, array[i]);
                    writes.write(temp, k++, array[i], delay ? 1 : 0, true, true);
                }
            highlights.clearMark(2);

            for (int i = 0; i < currentLen; i++)
                writes.write(array, i, temp[i], delay ? 1 : 0, true, false);
            writes.deleteExternalArray(temp);
            writes.reversal(array, 0, currentLen, delay ? 1 : 0, true, false);
        }
    },
    INTERLACED {
        @Override
        public String getName() {
            return "Interlaced";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int[] referenceArray = writes.createExternalArray(currentLen);
            for (int i = 0; i < currentLen; i++) {
                writes.write(referenceArray, i, array[i], delay ? 1 : 0, true, true);
            }

            int leftIndex = 1;
            int rightIndex = currentLen - 1;

            for (int i = 1; i < currentLen; i++) {
                if (i % 2 == 0) {
                    highlights.markArray(2, referenceArray[leftIndex]);
                    writes.write(array, i, referenceArray[leftIndex++], delay ? 1 : 0, true, false);
                } else {
                    highlights.markArray(2, referenceArray[rightIndex]);
                    writes.write(array, i, referenceArray[rightIndex--], delay ? 1 : 0, true, false);
                }
            }
            highlights.clearMark(2);
            writes.deleteExternalArray(referenceArray);
        }
    },
    DOUBLE_LAYERED {
        @Override
        public String getName() {
            return "Double Layered";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();

            for (int i = 0; i < currentLen / 2; i += 2) {
                writes.swap(array, i, currentLen - i - 1, 0, true, false);
                if (arrayVisualizer.shuffleEnabled())
                    delays.sleep(1);
            }
        }
    },
    DIAMOND {
        @Override
        public String getName() {
            return "Diamond Input";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            for (int i = 0; i < currentLen / 2; i += 2) {
                writes.swap(array, i, currentLen - i - 1, 0, true, false);
                if (arrayVisualizer.shuffleEnabled())
                    delays.sleep(delay ? 1 : 0);
            }
            int a = 0;
            int m = (currentLen + 1) / 2;

            if (currentLen % 2 == 0)
                while (m < currentLen)
                    writes.swap(array, a++, m++, delay ? 1 : 0, true, false);
            else {
                highlights.clearMark(2);
                int temp = array[a];
                while (m < currentLen) {
                    writes.write(array, a++, array[m], delay ? 0.5 : 0, true, false);
                    writes.write(array, m++, array[a], delay ? 0.5 : 0, true, false);
                }
                writes.write(array, a, temp, delay ? 0.5 : 0, true, false);
            }
        }
    },
    FINAL_RADIX {
        @Override
        public String getName() {
            return "Final Radix";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            currentLen -= currentLen % 2;
            int mid = currentLen / 2;
            int[] temp = writes.createExternalArray(mid);

            for (int i = 0; i < mid; i++)
                writes.write(temp, i, array[i], delay ? 1 : 0, true, true);

            for (int i = mid, j = 0; i < currentLen; i++, j += 2) {
                highlights.markArray(2, array[i]);
                writes.write(array, j, array[i], delay ? 1 : 0, true, false);
                highlights.markArray(2, temp[i - mid]);
                writes.write(array, j + 1, temp[i - mid], delay ? 1 : 0, true, false);
            }
            highlights.clearMark(2);
            writes.deleteExternalArray(temp);
        }
    },
    REAL_FINAL_RADIX {
        @Override
        public String getName() {
            return "Real Final Radix";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int mask = 0;
            for (int i = 0; i < currentLen; i++)
                while (mask < array[i])
                    mask = (mask << 1) + 1;
            mask >>= 1;

            int[] counts = writes.createExternalArray(mask + 2);
            int[] tmp = writes.createExternalArray(currentLen);

            writes.arraycopy(array, 0, tmp, 0, currentLen, delay ? 1 : 0, true, true);

            for (int i = 0; i < currentLen; i++)
                writes.write(counts, (array[i] & mask) + 1, counts[(array[i] & mask) + 1] + 1, delay ? 1 : 0, true,
                        true);

            for (int i = 1; i < counts.length; i++) {
                highlights.markArray(2, counts[i] + counts[i - 1]);
                writes.write(counts, i, counts[i] + counts[i - 1], delay ? 1 : 0, true,
                        true);
            }

            for (int i = 0; i < currentLen; i++) {
                highlights.markArray(2, tmp[i]);
                writes.write(array, counts[tmp[i] & mask]++, tmp[i], delay ? 1 : 0, true, false);
            }
            highlights.clearMark(2);
            writes.deleteExternalArray(counts);
            writes.deleteExternalArray(tmp);
        }
    },
    REC_RADIX {
        @Override
        public String getName() {
            return "Recursive Final Radix";
        }

        int[] temp;
        Highlights highlights;
        Writes writes;

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            temp = writes.createExternalArray(currentLen / 2);
            this.highlights = highlights;
            this.writes = writes;
            weaveRec(array, 0, currentLen, 1, delay ? 0.5 : 0, 0);
            highlights.clearMark(2);
            writes.deleteExternalArray(temp);
        }

        public void weaveRec(int[] array, int pos, int length, int gap, double delay, int depth) {
            if (length < 2)
                return;
            int mod2 = length % 2;
            length -= mod2;
            int mid = length / 2;

            for (int i = pos, j = 0; i < pos + gap * mid; i += gap, j++) {
                highlights.markArray(2, array[i]);
                writes.write(temp, j, array[i], delay, true, true);
            }
            highlights.clearMark(2);
            for (int i = pos + gap * mid, j = pos, k = 0; i < pos + gap * length; i += gap, j += 2 * gap, k++) {
                highlights.markArray(2, array[i]);
                writes.write(array, j, array[i], delay, true, false);
                highlights.markArray(2, temp[k]);
                writes.write(array, j + gap, temp[k], delay, true, false);
            }

            writes.recordDepth(depth);
            writes.recursion();
            weaveRec(array, pos, mid + mod2, 2 * gap, delay / 2, depth + 1);
            writes.recursion();
            weaveRec(array, pos + gap, mid, 2 * gap, delay / 2, depth + 1);
        }
    },
    HALF_ROTATION {
        @Override
        public String getName() {
            return "Half Rotation";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int a = 0;
            int m = (currentLen + 1) / 2;

            if (currentLen % 2 == 0)
                while (m < currentLen)
                    writes.swap(array, a++, m++, delay ? 1 : 0, true, false);
            else {
                highlights.clearMark(2);
                int temp = array[a];
                while (m < currentLen) {
                    writes.write(array, a++, array[m], delay ? 0.5 : 0, true, false);
                    writes.write(array, m++, array[a], delay ? 0.5 : 0, true, false);
                }
                writes.write(array, a, temp, delay ? 0.5 : 0, true, false);
            }
        }
    },
    QUARTER_ROTATION {
        @Override
        public String getName() {
            return "Quarter Rotation";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int a = 0;
            int m = (currentLen + 1) / 4;

            if (currentLen % 2 == 0)
                while (m < currentLen)
                    writes.swap(array, a++, m++, delay ? 1 : 0, true, false);
            else {
                highlights.clearMark(2);
                int temp = array[a];
                while (m < currentLen) {
                    writes.write(array, a++, array[m], delay ? 0.5 : 0, true, false);
                    writes.write(array, m++, array[a], delay ? 0.5 : 0, true, false);
                }
                writes.write(array, a, temp, delay ? 0.5 : 0, true, false);
            }
        }
    },
    PARTIAL_REVERSE {
        @Override
        public String getName() {
            return "Half Reversed";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            writes.reversal(array, 0, currentLen - 1, delay ? 1 : 0, true, false);
            writes.reversal(array, currentLen / 4, (3 * currentLen + 3) / 4 - 1, delay ? 1 : 0, true, false);
        }
    },
    BST_TRAVERSAL {
        @Override
        public String getName() {
            return "Binary Search Tree";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            int[] temp = writes.copyOfArray(array, currentLen);

            // credit to sam walko/anon

            class Subarray {
                private int start;
                private int end;

                Subarray(int start, int end) {
                    this.start = start;
                    this.end = end;
                }
            }

            Queue<Subarray> q = new LinkedList<>();
            q.add(new Subarray(0, currentLen));
            writes.changeAllocAmount(1);
            writes.changeAuxWrites(1);
            int i = 0;

            while (!q.isEmpty()) {
                Subarray sub = q.poll();
                writes.changeAllocAmount(-1);
                writes.changeAuxWrites(1);
                if (sub.start != sub.end) {
                    int mid = (sub.start + sub.end) / 2;
                    writes.write(array, i, temp[mid], 0, true, false);
                    i++;
                    q.add(new Subarray(sub.start, mid));
                    highlights.markArray(1, sub.start);
                    highlights.markArray(2, mid);
                    if (delay)
                        delays.sleep(0.5);
                    q.add(new Subarray(mid + 1, sub.end));
                    highlights.markArray(1, mid + 1);
                    highlights.markArray(2, sub.end);
                    if (delay)
                        delays.sleep(0.5);
                    writes.changeAllocAmount(2);
                    writes.changeAuxWrites(2);
                }
            }
            highlights.clearAllMarks();
            writes.deleteExternalArray(temp);
        }
    },
    INV_BST {
        @Override
        public String getName() {
            return "Inverted BST";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            int[] temp = writes.createExternalArray(currentLen);

            // credit to sam walko/anon

            class Subarray {
                private int start;
                private int end;

                Subarray(int start, int end) {
                    this.start = start;
                    this.end = end;
                }
            }

            Queue<Subarray> q = new LinkedList<>();
            q.add(new Subarray(0, currentLen));
            highlights.markArray(1, 0);
            highlights.markArray(2, currentLen);
            delays.sleep(1);
            writes.changeAuxWrites(1);
            writes.changeAllocAmount(1);
            int i = 0;

            while (!q.isEmpty()) {
                Subarray sub = q.poll();
                writes.changeAuxWrites(1);
                writes.changeAllocAmount(-1);
                if (sub.start != sub.end) {
                    int mid = (sub.start + sub.end) / 2;
                    writes.write(temp, i, mid, 0, true, true);
                    i++;
                    q.add(new Subarray(sub.start, mid));
                    highlights.markArray(1, sub.start);
                    highlights.markArray(2, mid);
                    if (delay)
                        delays.sleep(0.5);
                    writes.changeAuxWrites(1);
                    writes.changeAllocAmount(1);
                    q.add(new Subarray(mid + 1, sub.end));
                    highlights.markArray(1, mid + 1);
                    highlights.markArray(2, sub.end);
                    if (delay)
                        delays.sleep(0.5);
                    writes.changeAuxWrites(1);
                    writes.changeAllocAmount(1);
                }
            }
            highlights.clearAllMarks();
            int[] temp2 = writes.copyOfArray(array, currentLen);
            for (i = 0; i < currentLen; i++) {
                highlights.markArray(2, temp2[i]);
                writes.write(array, temp[i], temp2[i], delay ? 0.5 : 0, true, false);
            }
            writes.deleteExternalArray(temp);
            writes.deleteExternalArray(temp2);
        }
    },
    LOG_SLOPES {
        @Override
        public String getName() {
            return "Logarithmic Slopes";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int[] temp = writes.createExternalArray(currentLen);
            for (int i = 0; i < currentLen; i++)
                writes.write(temp, i, array[i], delay ? 1 : 0, true, true);

            writes.write(array, 0, 0, delay ? 1 : 0, true, false);
            for (int i = 1; i < currentLen; i++) {
                int log = (int) (Math.log(i) / Math.log(2));
                int power = (int) Math.pow(2, log);
                int value = temp[2 * (i - power) + 1];
                writes.write(array, i, value, delay ? 1 : 0, true, false);
            }
            writes.deleteExternalArray(temp);
        }
    },
    HEAPIFIED {
        @Override
        public String getName() {
            return "Max Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            MaxHeapSort heapSort = new MaxHeapSort(arrayVisualizer);
            heapSort.makeHeap(array, 0, currentLen, delay ? 1 : 0);
        }
    },
    MIN_HEAPIFIED {
        @Override
        public String getName() {
            return "Min Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            MinHeapSort heapSort = new MinHeapSort(arrayVisualizer);
            heapSort.heapify(array, 0, currentLen, delay ? 1 : 0, false);
        }
    },
    FLIPPED_MIN_HEAPIFIED {
        @Override
        public String getName() {
            return "Flipped Min Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();

            FlippedMinHeapSort heapSort = new FlippedMinHeapSort(arrayVisualizer);
            for (int i = currentLen / 2; i >= 1; i--) {
                heapSort.siftDown(array, currentLen, i, currentLen);
            }
        }
    },
    MIN_MAX_HEAPIFIED {
        @Override
        public String getName() {
            return "Min-Max Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();

            MinMaxHeapSort heapSort = new MinMaxHeapSort(arrayVisualizer);
            for (int i = currentLen / 2; i >= 1; i--) {
                heapSort.heapify(array, 0, currentLen);
            }
        }
    },
    WEAK_HEAPIFIED {
        @Override
        public String getName() {
            return "Weak Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();

            WeakHeapSort heapSort = new WeakHeapSort(arrayVisualizer);
            int i;
            int j;
            int gParent;

            int bitsLength = (currentLen + 7) / 8;
            int[] bits = writes.createExternalArray(bitsLength);

            for (i = 0; i < currentLen / 8; ++i) {
                writes.write(bits, i, 0, 0.25, true, true);
            }

            for (i = currentLen - 1; i > 0; --i) {
                j = i;

                while ((j & 1) == WeakHeapSort.getBitwiseFlag(bits, j >> 1))
                    j >>= 1;
                gParent = j >> 1;

                heapSort.weakHeapMerge(array, bits, gParent, i);
            }
            writes.deleteExternalArray(bits);
        }
    },
    TERNARY_HEAPIFIED {
        @Override
        public String getName() {
            return "Ternary Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();

            TernaryHeapSort heapSort = new TernaryHeapSort(arrayVisualizer);
            heapSort.buildMaxTernaryHeap(array, currentLen);
        }
    },
    BIN_SIFT_HEAPIFIED {
        @Override
        public String getName() {
            return "Binary Sift Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();

            BinarySiftHeapSort heapSort = new BinarySiftHeapSort(arrayVisualizer);
            heapSort.buildHeap(array, currentLen);
        }
    },
    DOUBLE_HEAPIFIED {
        @Override
        public String getName() {
            return "Double Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();

            DoubleHeapSort heapSort = new DoubleHeapSort(arrayVisualizer);
            heapSort.buildHeap(array, currentLen);
        }
    },
    LOG_NARY_HEAP {
        @Override
        public String getName() {
            return "Log n-ary Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();

            LogNaryHeapSort heapSort = new LogNaryHeapSort(arrayVisualizer);
            heapSort.buildHeap(array, 0, currentLen);
        }
    },
    VELV_HEAP {
        @Override
        public String getName() {
            return "Velvet Heapified";
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

            AdaptiveVelvetSort velvet = new AdaptiveVelvetSort(arrayVisualizer);
            velvet.heap(array, 0, currentLen, 0);

            if (delay)
                Delays.setSleepRatio(Delays.getSleepRatio() / 10);
            else
                Delays.changeSkipped(false);
        }
    },
    BINOMIAL_HEAPIFIED {
        @Override
        public String getName() {
            return "Binomial Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            this.reads = arrayVisualizer.getReads();
            int maxNode;
            int focus;
            int index;
            int depth;
            for (index = 2; index <= currentLen; index += 2) {
                maxNode = index;
                do {
                    focus = maxNode;
                    for (depth = 1; (focus & depth) == 0; depth *= 2) {
                        if (reads.compareIndices(array, focus - depth - 1, maxNode - 1, 0.5, true) > 0)
                            maxNode = (focus - depth);
                    }
                    if (focus != maxNode) {
                        writes.swap(array, focus - 1, maxNode - 1, 0.5, true, false);
                    }
                } while (focus != maxNode);
            }
        }
    },
    BOTTOM_UP_HEAPIFIED {
        @Override
        public String getName() {
            return "Bottom-Up Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int length = arrayVisualizer.getCurrentLength();

            BottomUpHeapSort heapSort = new BottomUpHeapSort(arrayVisualizer);
            for (int i = (length - 1) / 2; i >= 0; i--)
                heapSort.siftDown(array, i, length);
        }
    },
    FIBONACCI_HEAPIFIED {
        @Override
        public String getName() {
            return "Fibonacci Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int length = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            if (delay)
                delays.setSleepRatio(delays.getSleepRatio() * 10);
            else
                delays.changeSkipped(true);
            FibonacciHeapSort heapSort = new FibonacciHeapSort(arrayVisualizer);
            heapSort.fibHeapify(array, 0, length);
            if (delay)
                delays.setSleepRatio(delays.getSleepRatio() / 10);
            else
                delays.changeSkipped(false);
        }
    },
    GEAPIFIED {
        @Override
        public String getName() {
            return "Geapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int length = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            GeapSort heapSort = new GeapSort(arrayVisualizer);
            heapSort.makeGeap(array, 0, length, delay ? 0.1 : 0);
        }
    },
    PURE_FIBONACCI_HEAPIFIED {
        @Override
        public String getName() {
            return "Pure Fibonacci Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int length = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            if (delay)
                delays.setSleepRatio(delays.getSleepRatio() * 1000);
            else
                delays.changeSkipped(true);
            PureFibHeapSort heapSort = new PureFibHeapSort(arrayVisualizer);
            heapSort.fibHeapify(array, 0, length);
        }
    },
    NARNIA {
        @Override
        public String getName() {
            return "Narnia Heapified";
        }

        private int[] array;

        class Node {
            private int index;
            private boolean tournament;
            private Node childLeft = null;
            private Node childRight = null;
            private Node winner = null;

            Node(int index) {
                this.index = index;
                this.tournament = false;
            }

            Node(boolean tournament) {
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
                } else if (indices(childLeft.index, childRight.index) == 1) {
                    winner = childRight;
                    index = childRight.index;
                } else {
                    winner = childLeft;
                    index = childLeft.index;
                }
            }

            private int indices(int a, int b) {
                return reads.compareIndices(array, b, a, 0.1, true);
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
                Node a = new Node(start);
                Node b = new Node(end);

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
            this.reads = arrayVisualizer.getReads();
            Node winner = traverse(0, currentLen - 1);
            Queue<Node> nodes = new LinkedList<>();
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
                writes.write(heap, i++, array[t.index], delay ? 1 : 0, true, false);
                struck[t.index] = true;
                if (t.winner != null)
                    nodes.add(t.winner);
                if (t.loser() != null)
                    nodes.add(t.loser());
            }
            writes.arraycopy(heap, 0, array, 0, currentLen, delay ? 1 : 0, true, false);
            writes.deleteExternalArray(heap);
        }
    },
    SMOOTH {
        @Override
        public String getName() {
            return "Smoothified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();

            SmoothSort smoothSort = new SmoothSort(arrayVisualizer);
            smoothSort.smoothHeapify(array, currentLen);
        }
    },
    BINOMIAL_SMOOTH {
        @Override
        public String getName() {
            return "Binomial Smoothified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();

            BinomialSmoothSort heapSort = new BinomialSmoothSort(arrayVisualizer);
            for (int node = 1; node < currentLen; node++)
                heapSort.thrift(array, node, node % 2 == 1, (node + (1 << heapSort.height(node)) >= currentLen));
        }
    },
    POPLAR {
        @Override
        public String getName() {
            return "Poplarified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();

            PoplarHeapSort poplarHeapSort = new PoplarHeapSort(arrayVisualizer);
            poplarHeapSort.poplarHeapify(array, 0, currentLen);
        }
    },
    LAZY_HEAPIFIED {
        @Override
        public String getName() {
            return "Lazy Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int length = arrayVisualizer.getCurrentLength();

            LazyHeapSort heapSort = new LazyHeapSort(arrayVisualizer);
            int s = (int) Math.sqrt(length - 1d) + 1;

            for (int i = 0; i < length; i += s)
                heapSort.maxToFront(array, i, Math.min(i + s, length));
        }
    },
    OPTI_LAZY_HEAPIFIED {
        @Override
        public String getName() {
            return "Opti. Lazy Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int length = arrayVisualizer.getCurrentLength();
            this.reads = arrayVisualizer.getReads();
            int s = (int) Math.sqrt(length - 1d) + 1;

            int f = (length - 1) % s + 1;

            for (int j = f; j < length; j += s) {
                int min = findMin(array, j, j + 1, j + s, 1);

                if (j != min)
                    writes.swap(array, j, min, 1, true, false);
            }
        }

        private int findMin(int[] array, int p, int a, int b, int s) {
            int min = p;

            for (int i = a; i < b; i += s)
                if (reads.compareIndices(array, i, min, 0.1, true) < 0)
                    min = i;

            return min;
        }
    },
    TRI_HEAP {
        @Override
        public String getName() {
            return "Triangular Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            if (delay)
                delays.setSleepRatio(delays.getSleepRatio() * 10);
            else
                delays.changeSkipped(true);

            TriangularHeapSort triangularHeapSort = new TriangularHeapSort(arrayVisualizer);
            triangularHeapSort.triangularHeapify(array, currentLen);

            if (delay)
                delays.setSleepRatio(delays.getSleepRatio() / 10);
            else
                delays.changeSkipped(false);
        }
    },
    PSEUDO_HEAPIFIED {
        @Override
        public String getName() {
            return "Pseudo Heapified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int length = arrayVisualizer.getCurrentLength();
            this.reads = arrayVisualizer.getReads();
            this.writes = writes;
            for (int i = length - 2; i >= 0; i--) {
                sift(array, i, length);
            }
        }

        private void sift(int[] array, int start, int end) {
            siftDown(array, start, end - start + 1, 1);
        }

        private void siftDown(int[] array, int start, int length, int root) {
            int j = root;

            while (2 * j < length) {
                int k = 2 * j;
                if (k < length && reads.compareIndices(array, start + k - 1, start + k, 0.5, true) == 1) {
                    k++;
                }
                if (reads.compareIndices(array, start + j - 1, start + k - 1, 0.05, true) == 1) {
                    writes.swap(array, start + j - 1, start + k - 1, 0.05, true, false);
                    j = k;
                    continue;
                }
                break;
            }
        }
    },
    SCROLL_HEAPIFIED {
        @Override
        public String getName() {
            return "Scroll Heap Hunterified";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int length = arrayVisualizer.getCurrentLength();
            this.reads = arrayVisualizer.getReads();
            MinHeapSort heapSort = new MinHeapSort(arrayVisualizer);
            int j = 0;
            int k = length - 1;
            while (reads.compareIndices(array, j, k, 0, true) <= 0 && k > j)
                k--;
            if (k != j) {
                for (int i = k; i >= j; i--) {
                    highlights.markArray(3, i + 1);
                    highlights.markArray(4, j);
                    heapSort.heapify(array, i, k + 1, 0, false);
                }
            }
            highlights.clearAllMarks();
        }
    },
    CIRCLE {
        @Override
        public String getName() {
            return "First Circle Pass";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            this.reads = arrayVisualizer.getReads();
            this.writes = writes;
            this.highlights = highlights;

            shuffle(array, 0, currentLen, delay ? 0.5 : 0, writes);

            int n = 1;
            for (; n < currentLen; n *= 2)
                ;

            circleSortRoutine(array, 0, n - 1, currentLen, delay ? 0.5 : 0, 0);
        }

        public void circleSortRoutine(int[] array, int lo, int hi, int end, double sleep,
                int depth) {
            if (lo == hi)
                return;

            int high = hi;
            int low = lo;
            int mid = (hi - lo) / 2;

            while (lo < hi) {
                if (hi < end && reads.compareIndices(array, lo, hi, sleep / 2, true) > 0)
                    writes.swap(array, lo, hi, sleep, true, false);

                lo++;
                hi--;
            }
            writes.recordDepth(depth);
            writes.recursion();
            circleSortRoutine(array, low, low + mid, end, sleep / 2, depth + 1);
            if (low + mid + 1 < end) {
                writes.recordDepth(depth);
                writes.recursion();
                circleSortRoutine(array, low + mid + 1, high, end, sleep / 2, depth + 1);
            }
        }
    },
    PAIRWISE {
        @Override
        public String getName() {
            return "Final Pairwise Pass";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            this.reads = arrayVisualizer.getReads();

            shuffle(array, 0, currentLen, delay ? 0.5 : 0, writes);

            // create pairs
            for (int i = 1; i < currentLen; i += 2)
                if (reads.compareIndices(array, i - 1, i, delay ? 0.5 : 0, true) > 0)
                    writes.swap(array, i - 1, i, delay ? 0.5 : 0, true, false);

            highlights.clearMark(2);

            int[] temp = writes.createExternalArray(currentLen);

            // sort the smaller and larger of the pairs separately with pigeonhole sort
            for (int m = 0; m < 2; m++) {
                for (int k = m; k < currentLen; k += 2)
                    writes.write(temp, array[k], temp[array[k]] + 1, delay ? 0.2 : 0, true, true);

                int i = 0;
                int j = m;
                while (true) {
                    while (i < currentLen && temp[i] == 0)
                        i++;
                    if (i >= currentLen)
                        break;

                    writes.write(array, j, i, delay ? 0.5 : 0, true, false);

                    j += 2;
                    writes.write(temp, i, temp[i] - 1, delay ? 0.2 : 0, true, true);
                }
            }
            writes.deleteExternalArray(temp);
        }
    },
    PAIRWISED {
        @Override
        public String getName() {
            return "Pairwised";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            this.reads = arrayVisualizer.getReads();
            int length = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int a = 1;
            int b = 0;
            int c = 0;
            shuffle(array, 0, length, delay ? 0.5 : 0, writes);
            while (a < length) {
                b = a;
                c = 0;
                while (b < length) {
                    if (reads.compareIndices(array, b - a, b, delay ? 0.05 : 0, true) == 1) {
                        writes.swap(array, b - a, b, delay ? 0.05 : 0, true, false);
                    }
                    c = (c + 1) % a;
                    b++;
                    if (c == 0) {
                        b += a;
                    }
                }
                a *= 2;
            }
        }
    },
    HALVER {
        @Override
        public String getName() {
            return "Circle Halver Pass";
        }

        private boolean delay;

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            this.reads = arrayVisualizer.getReads();
            int end = arrayVisualizer.getCurrentLength();
            delay = arrayVisualizer.shuffleEnabled();
            int n = end;
            shuffle(array, 0, end, delay ? 0.5 : 0, writes);
            for (int j = 1; j < n / 2; j *= 2)
                for (int i = 0; i < end; i += 2 * j)
                    this.fwdComp(array, i, i + j, j, reads, writes);
        }

        private void fwdComp(int[] array, int a, int m, int l, Reads reads, Writes writes) {
            for (int i = 0; i < l; i++)
                this.compSwap(array, a + i, m + i, reads, writes);
        }

        private boolean compSwap(int[] array, int a, int b, Reads reads, Writes writes) {
            if (reads.compareIndices(array, a, b, delay ? 0.1 : 0, true) > 0) {

                writes.swap(array, a, b, 0, true, false);
                return true;
            }
            return false;
        }
    },
    REC_REV {
        @Override
        public String getName() {
            return "Recursive Reversal";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            reversalRec(array, 0, currentLen, delay ? 1 : 0, writes, 0);
        }

        public void reversalRec(int[] array, int a, int b, double sleep, Writes writes, int depth) {
            if (b - a < 2)
                return;

            writes.reversal(array, a, b - 1, sleep, true, false);

            int m = (a + b) / 2;
            writes.recordDepth(depth);
            writes.recursion();
            this.reversalRec(array, a, m, sleep / 2, writes, depth + 1);
            writes.recursion();
            this.reversalRec(array, m, b, sleep / 2, writes, depth + 1);
        }
    },
    GRAY_CODE {
        @Override
        public String getName() {
            return "Gray Code Fractal";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            reversalRec(array, 0, currentLen, false, delay ? 1 : 0, writes, 0);
        }

        public void reversalRec(int[] array, int a, int b, boolean bw, double sleep, Writes writes, int depth) {
            if (b - a < 3)
                return;

            int m = (a + b) / 2;

            if (bw)
                writes.reversal(array, a, m - 1, sleep, true, false);
            else
                writes.reversal(array, m, b - 1, sleep, true, false);
            writes.recordDepth(depth);
            writes.recursion();
            this.reversalRec(array, a, m, false, sleep / 2, writes, depth + 1);
            writes.recursion();
            this.reversalRec(array, m, b, true, sleep / 2, writes, depth + 1);
        }
    },
    SIERPINSKI {
        @Override
        public String getName() {
            return "Sierpinski Triangle";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            boolean delay = arrayVisualizer.shuffleEnabled();
            int currentLen = arrayVisualizer.getCurrentLength();
            int[] triangle = writes.createExternalArray(currentLen);
            triangleRec(triangle, 0, currentLen, delay ? 1 : 0, writes, 0);

            int[] temp = writes.copyOfArray(array, currentLen);
            for (int i = 0; i < currentLen; i++) {
                highlights.markArray(2, triangle[i]);
                writes.write(array, i, temp[triangle[i]], delay ? 0.5 : 0, true, false);
            }
            writes.deleteExternalArray(temp);
            writes.deleteExternalArray(triangle);
        }

        public void triangleRec(int[] array, int a, int b, double sleep, Writes writes, int depth) {
            if (b - a < 2)
                return;
            if (b - a == 2) {
                writes.write(array, a + 1, array[a + 1] + 1, sleep, true, true);
                return;
            }

            int h = (b - a) / 3;
            int t1 = (a + a + b) / 3;
            int t2 = (a + b + b + 2) / 3;
            for (int i = a; i < t1; i++)
                writes.write(array, i, array[i] + h, sleep, true, true);

            for (int i = t1; i < t2; i++)
                writes.write(array, i, array[i] + 2 * h, sleep, true, true);
            writes.recordDepth(depth);
            writes.recursion();
            triangleRec(array, a, t1, sleep, writes, depth + 1);
            writes.recursion();
            triangleRec(array, t1, t2, sleep, writes, depth + 1);
            writes.recursion();
            triangleRec(array, t2, b, sleep, writes, depth + 1);
        }
    },
    TRIANGULAR {
        @Override
        public String getName() {
            return "Triangular";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            this.reads = arrayVisualizer.getReads();
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            int[] triangle = writes.createExternalArray(currentLen);

            int j = 0;
            int k = 2;
            int max = 0;

            for (int i = 1; i < currentLen; i++, j++) {
                if (i == k) {
                    j = 0;
                    k *= 2;
                }
                writes.write(triangle, i, triangle[j] + 1, delay ? 1 : 0, true, true);
                if (reads.compareValues(triangle[i], max) > 0) {
                    highlights.markArray(2, i);
                    delays.sleep(delay ? 1 : 0);
                    max = triangle[i];
                }
            }
            int[] cnt = writes.createExternalArray(max + 1);

            for (int i = 0; i < currentLen; i++)
                writes.write(cnt, triangle[i], cnt[triangle[i]] + 1, delay ? 1 : 0, true, true);

            for (int i = 1; i < cnt.length; i++)
                writes.write(cnt, i, cnt[i] + cnt[i - 1], delay ? 1 : 0, true, true);

            for (int i = currentLen - 1; i >= 0; i--) {
                highlights.markArray(2, triangle[i]);
                writes.write(triangle, i, --cnt[triangle[i]], delay ? 1 : 0, true, true);
            }

            int[] temp = writes.copyOfArray(array, currentLen);
            for (int i = 0; i < currentLen; i++) {
                highlights.markArray(2, triangle[i]);
                writes.write(array, i, temp[triangle[i]], delay ? 1 : 0, true, false);
            }
            writes.deleteExternalArray(temp);
            writes.deleteExternalArray(cnt);
            writes.deleteExternalArray(triangle);
            highlights.clearMark(2);
        }
    },
    ANTI_CIRCLE {
        @Override
        public String getName() {
            return "Backwards Circle Pass";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            this.reads = arrayVisualizer.getReads();
            this.writes = writes;

            int n = 1;
            for (; n < currentLen; n *= 2)
                ;

            circleSortRoutine(array, 0, n - 1, currentLen, delay ? 0.25 : 0, false, 0);
        }

        public void circleSortRoutine(int[] array, int lo, int hi, int end, double sleep, boolean dir, int depth) {
            if (lo == hi)
                return;

            int high = hi;
            int low = lo;
            int mid = (hi - lo) / 2;

            while (lo < hi) {
                if (dir) {
                    if (hi < end && reads.compareIndices(array, lo, hi, sleep / 2, true) > 0)
                        writes.swap(array, lo, hi, sleep, true, false);

                    lo++;
                    hi--;
                } else {
                    if (hi < end && reads.compareIndices(array, lo, hi, sleep / 2, true) < 0)
                        writes.swap(array, lo, hi, sleep, true, false);

                    lo++;
                    hi--;
                }
            }
            writes.recordDepth(depth);
            writes.recursion();
            circleSortRoutine(array, low, low + mid, end, sleep, dir, depth + 1);
            if (low + mid + 1 < end) {
                writes.recordDepth(depth);
                writes.recursion();
                circleSortRoutine(array, low + mid + 1, high, end, sleep, !dir, depth + 1);
            }
        }
    },
    MODULO {
        @Override
        public String getName() {
            return "Modulo";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            for (int i = 1; i < currentLen; i++) {
                if ((i * 2) % currentLen != i)
                    writes.swap(array, i, (i * 2) % currentLen, delay ? 1 : 0, true, false);
            }
        }
    },
    QSORT_BAD {
        @Override
        public String getName() {
            return "Quicksort Adversary";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            for (int j = currentLen - currentLen % 2 - 2, i = j - 1; i >= 0; i -= 2, j--)
                writes.swap(array, i, j, delay ? 1 : 0, true, false);
        }
    },
    PDQ_BAD {
        boolean delay;
        double sleep;

        int[] temp;
        boolean hasCandidate;
        int gas;
        int frozen;
        int candidate;

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

        @Override
        public String getName() {
            return "PDQ Adversary";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            delay = arrayVisualizer.shuffleEnabled();
            sleep = delay ? 1 : 0;
            this.reads = arrayVisualizer.getReads();
            this.writes = writes;
            this.highlights = highlights;
            this.delays = delays;

            int[] copy = writes.createExternalArray(currentLen);

            hasCandidate = false;
            frozen = 1;
            temp = writes.createExternalArray(currentLen);
            gas = currentLen;
            for (int i = 0; i < currentLen; i++) {
                writes.write(copy, i, array[i], sleep / 4, true, false);
                writes.write(array, i, i, sleep / 4, true, false);
                writes.write(temp, i, gas, sleep / 4, true, true);
            }

            pdqLoop(array, 0, currentLen, false, PDQSorting.pdqLog(currentLen), 0);

            for (int i = 0; i < currentLen; i++) {
                writes.write(array, i, copy[temp[i] - 1], sleep, true, false);
            }
            writes.deleteExternalArray(temp);
            writes.deleteExternalArray(copy);
        }

        protected int compare(int ap, int bp) {
            reads.addComparison();
            int a;
            int b;
            if (!hasCandidate) {
                candidate = 0;
                hasCandidate = true;
            }

            a = ap;
            b = bp;
            highlights.markArray(2, a);
            highlights.markArray(3, b);
            delays.sleep(sleep / 2);
            if (temp[a] == gas && temp[b] == gas) {
                if (a == candidate) {
                    writes.write(temp, a, frozen++, sleep, true, true);
                } else {
                    writes.write(temp, b, frozen++, sleep, true, true);
                }
            }
            highlights.clearMark(3);
            highlights.markArray(2, a);
            delays.sleep(sleep / 2);
            if (temp[a] == gas) {
                candidate = a;
                highlights.clearAllMarks();
                return 1;
            }
            highlights.markArray(2, b);
            delays.sleep(sleep / 2);
            if (temp[b] == gas) {
                candidate = b;
                highlights.clearAllMarks();
                return -1;
            }
            highlights.markArray(2, a);
            highlights.markArray(3, b);
            delays.sleep(sleep / 2);
            highlights.clearAllMarks();
            if (temp[a] < temp[b])
                return -1;
            if (temp[a] > temp[b])
                return 1;
            return 0;
        }

        protected int compareIndices(int[] array, int a, int b) {
            highlights.clearAllMarks();
            highlights.markArray(1, a);
            highlights.markArray(2, b);
            delays.sleep(sleep);
            highlights.clearAllMarks();
            return compare(array[a], array[b]);
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
                    writes.swap(array, begin, begin + halfSize, sleep, true, false);
                    highlights.clearMark(2);
                } else
                    this.pdqSortThree(array, begin + halfSize, begin, end - 1);

                if (!leftmost && (compareIndices(array, begin - 1, begin) >= 0)) {
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
                        writes.swap(array, begin, begin + leftSize / 4, sleep, true, false);
                        writes.swap(array, pivotPos - 1, pivotPos - leftSize / 4, sleep, true, false);

                        if (leftSize > 128) {
                            writes.swap(array, begin + 1, begin + (leftSize / 4 + 1), sleep, true, false);
                            writes.swap(array, begin + 2, begin + (leftSize / 4 + 2), sleep, true, false);
                            writes.swap(array, pivotPos - 2, pivotPos - (leftSize / 4 + 1), sleep, true, false);
                            writes.swap(array, pivotPos - 3, pivotPos - (leftSize / 4 + 2), sleep, true, false);
                        }
                    }

                    if (rightSize >= 24) {
                        writes.swap(array, pivotPos + 1, pivotPos + (1 + rightSize / 4), sleep, true, false);
                        writes.swap(array, end - 1, end - rightSize / 4, sleep, true, false);

                        if (rightSize > 128) {
                            writes.swap(array, pivotPos + 2, pivotPos + (2 + rightSize / 4), sleep, true, false);
                            writes.swap(array, pivotPos + 3, pivotPos + (3 + rightSize / 4), sleep, true, false);
                            writes.swap(array, end - 2, end - (1 + rightSize / 4), sleep, true, false);
                            writes.swap(array, end - 3, end - (2 + rightSize / 4), sleep, true, false);
                        }
                    }
                    highlights.clearMark(2);
                } else {
                    if (alreadyParted && pdqPartialInsertSort(array, begin, pivotPos)
                            && pdqPartialInsertSort(array, pivotPos + 1, end))
                        return;
                }
                writes.recordDepth(depth);
                writes.recursion();
                this.pdqLoop(array, begin, pivotPos, branchless, badAllowed, depth + 1);
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
                if (leaf < dist && compareIndices(array, start + leaf - 1, start + leaf) == compareVal) {
                    leaf++;
                }
                if (compareIndices(array, start + root - 1, start + leaf - 1) == compareVal) {
                    writes.swap(array, start + root - 1, start + leaf - 1, sleep, true, false);
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
                highlights.markArray(1, first);
            }

            if (first - 1 == begin)
                while (first < last && (compare(array[--last], pivot) >= 0)) {
                    highlights.markArray(2, last);
                }
            else
                while ((compare(array[--last], pivot) >= 0)) {
                    highlights.markArray(2, last);
                }

            boolean alreadyParted = first >= last;

            while (first < last) {
                writes.swap(array, first, last, sleep, true, false);
                while (compare(array[++first], pivot) < 0) {
                    highlights.markArray(1, first);
                }
                while ((compare(array[--last], pivot) >= 0)) {
                    highlights.markArray(2, last);
                }
            }
            highlights.clearMark(2);

            int pivotPos = first - 1;
            writes.write(array, begin, array[pivotPos], delay ? 1 : 0, true, false);
            writes.write(array, pivotPos, pivot, delay ? 1 : 0, true, false);

            return new PDQPair(pivotPos, alreadyParted);
        }

        private boolean pdqPartialInsertSort(int[] array, int begin, int end) {
            if (begin == end)
                return true;

            sleep = delay ? 1 / 3d : 0;

            int limit = 0;
            for (int cur = begin + 1; cur != end; ++cur) {
                if (limit > 8)
                    return false;

                int sift = cur;
                int siftMinusOne = cur - 1;

                if (compareIndices(array, sift, siftMinusOne) < 0) {
                    int tmp = array[sift];

                    do {
                        writes.write(array, sift--, array[siftMinusOne], sleep, true, false);
                    } while (sift != begin && compare(tmp, array[--siftMinusOne]) < 0);

                    writes.write(array, sift, tmp, sleep, true, false);
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
                highlights.markArray(2, last);
            }

            if (last + 1 == end)
                while (first < last && (compare(pivot, array[++first]) >= 0)) {
                    highlights.markArray(1, first);
                }
            else
                while ((compare(pivot, array[++first]) >= 0)) {
                    highlights.markArray(1, first);
                }

            while (first < last) {
                writes.swap(array, first, last, sleep, true, false);
                while (compare(pivot, array[--last]) < 0) {
                    highlights.markArray(2, last);
                }
                while ((compare(pivot, array[++first]) >= 0)) {
                    highlights.markArray(1, first);
                }
            }
            highlights.clearMark(2);

            int pivotPos = last;
            writes.write(array, begin, array[pivotPos], sleep, true, false);
            writes.write(array, pivotPos, pivot, sleep, true, false);

            return pivotPos;
        }

        private void pdqSortThree(int[] array, int a, int b, int c) {
            this.pdqSortTwo(array, a, b);
            this.pdqSortTwo(array, b, c);
            this.pdqSortTwo(array, a, b);
        }

        private void pdqSortTwo(int[] array, int a, int b) {
            if (compareIndices(array, b, a) < 0) {
                writes.swap(array, a, b, sleep, true, false);
            }
            highlights.clearMark(2);
        }

        private void pdqInsertSort(int[] array, int begin, int end) {
            if (begin == end)
                return;

            sleep = delay ? 1 / 3d : 0;

            for (int cur = begin + 1; cur != end; ++cur) {
                int sift = cur;
                int siftMinusOne = cur - 1;

                if (compareIndices(array, sift, siftMinusOne) < 0) {
                    int tmp = array[sift];
                    do {
                        writes.write(array, sift--, array[siftMinusOne], sleep, true, false);
                    } while (sift != begin && compare(tmp, array[--siftMinusOne]) < 0);

                    writes.write(array, sift, tmp, sleep, true, false);
                }
            }
        }

        private void pdqUnguardInsertSort(int[] array, int begin, int end) {
            if (begin == end)
                return;

            sleep = 1 / 3d;

            for (int cur = begin + 1; cur != end; ++cur) {
                int sift = cur;
                int siftMinusOne = cur - 1;

                if (compareIndices(array, sift, siftMinusOne) < 0) {
                    int tmp = array[sift];

                    do {
                        writes.write(array, sift--, array[siftMinusOne], sleep, true, false);
                    } while (compare(tmp, array[--siftMinusOne]) < 0);

                    writes.write(array, sift, tmp, sleep, true, false);
                }
            }
        }
    },
    GRAIL_BAD {
        @Override
        public String getName() {
            return "Grailsort Adversary";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            if (currentLen <= 16)
                writes.reversal(array, 0, currentLen - 1, delay ? 1 : 0, true, false);
            else {
                int blockLen = 1;
                while (blockLen * blockLen < currentLen)
                    blockLen *= 2;

                int numKeys = (currentLen - 1) / blockLen + 1;
                int keys = blockLen + numKeys;
                shuffle(array, 0, currentLen, delay ? 0.25 : 0, writes);
                sort(array, 0, keys, delay ? 0.25 : 0, writes);
                writes.reversal(array, 0, keys - 1, delay ? 0.25 : 0, true, false);
                highlights.clearMark(2);
                sort(array, keys, currentLen, delay ? 0.25 : 0, writes);
                highlights.clearMark(2);

                push(array, keys, currentLen, blockLen, delay ? 1 : 0, writes, 0);
            }
        }

        public void rotate(int[] array, int a, int m, int b, double sleep) {
            IndexedRotations.adaptable(array, a, m, b, sleep / 2, true, false);
        }

        public void push(int[] array, int a, int b, int bLen, double sleep, Writes writes, int depth) {
            int len = b - a;
            int b1 = b - len % bLen;
            int len1 = b1 - a;
            if (len1 <= 2 * bLen)
                return;

            int m = bLen;
            while (2 * m < len)
                m *= 2;
            m += a;

            if (b1 - m < bLen) {
                writes.recordDepth(depth);
                writes.recursion();
                push(array, a, m, bLen, sleep, writes, depth + 1);
            } else {
                m = a + b1 - m;
                rotate(array, m - (bLen - 2), b1 - (bLen - 1), b1, sleep);
                writes.multiSwap(array, a, m, sleep / 2, true, false);
                rotate(array, a, m, b1, sleep);
                m = a + b1 - m;
                writes.recordDepth(depth);
                writes.recursion();
                push(array, a, m, bLen, sleep / 2, writes, depth + 1);
                writes.recursion();
                push(array, m, b, bLen, sleep / 2, writes, depth + 1);
            }
        }
    },
    SHUF_MERGE_BAD {
        @Override
        public String getName() {
            return "Shuffle Merge Adversary";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int n = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            int[] tmp = writes.createExternalArray(n);
            int d = 2;
            int end = 1 << (int) (Math.log(n - 1d) / Math.log(2) + 1);

            while (d <= end) {
                int i = 0;
                int dec = 0;
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
                    shuffleMergeBad(array, tmp, i, j, k, delay ? sleep : 0, writes);
                    i = k;
                }
                d *= 2;
            }
            writes.deleteExternalArray(tmp);
        }

        public void shuffleMergeBad(int[] array, int[] tmp, int a, int m, int b, double sleep, Writes writes) {
            if ((b - a) % 2 == 1) {
                if (m - a > b - m)
                    a++;
                else
                    b--;
            }
            shuffleBad(array, tmp, a, b, sleep, writes);
        }

        // length is always even
        public void shuffleBad(int[] array, int[] tmp, int a, int b, double sleep, Writes writes) {
            if (b - a < 2)
                return;

            int m = (a + b) / 2;
            int s = (b - a - 1) / 4 + 1;

            a = m - s;
            b = m + s;
            int j = a;

            for (int i = a + 1; i < b; i += 2)
                writes.write(tmp, j++, array[i], sleep, true, true);
            for (int i = a; i < b; i += 2)
                writes.write(tmp, j++, array[i], sleep, true, true);

            writes.arraycopy(tmp, a, array, a, b - a, sleep, true, false);
        }
    },
    BIT_REVERSE {
        @Override
        public String getName() {
            return "Bit Reversal";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            int len = 1 << (int) (Math.log(arrayVisualizer.getCurrentLength()) / Math.log(2));
            boolean delay = arrayVisualizer.shuffleEnabled();
            boolean pow2 = len == currentLen;

            int[] temp = writes.copyOfArray(array, currentLen);
            for (int i = 0; i < len; i++)
                writes.write(array, i, i, delay ? 1 : 0, true, false);

            int m = 0;
            int d1 = len >> 1;
            int d2 = d1 + (d1 >> 1);

            for (int i = 1; i < len - 1; i++) {
                int j = d1;

                for (int k = i, n = d2; (k & 1) == 0; j -= n, k >>= 1, n >>= 1)
                    ;
                m += j;
                if (m > i)
                    writes.swap(array, i, m, delay ? 1 : 0, true, false);
            }
            highlights.clearMark(2);

            if (!pow2) {
                for (int i = len; i < currentLen; i++)
                    writes.write(array, i, array[i - len], 0.5, true, false);

                int[] cnt = writes.createExternalArray(len);

                for (int i = 0; i < currentLen; i++)
                    writes.write(cnt, array[i], cnt[array[i]] + 1, delay ? 1 : 0, true,
                            true);

                for (int i = 1; i < cnt.length; i++)
                    writes.write(cnt, i, cnt[i] + cnt[i - 1], delay ? 1 : 0, true,
                            true);

                for (int i = currentLen - 1; i >= 0; i--)
                    writes.write(array, i, --cnt[array[i]], 0.5, true, false);
                writes.deleteExternalArray(cnt);
            }
            int[] bits = writes.copyOfArray(array, currentLen);

            for (int i = 0; i < currentLen; i++)
                writes.write(array, i, temp[bits[i]], delay ? 1 : 0, true, false);
            writes.deleteExternalArray(temp);
            writes.deleteExternalArray(bits);
        }
    },
    BLOCK_RANDOMLY {
        @Override
        public String getName() {
            return "Random w/ Blocks";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            int blockSize = pow2lte((int) Math.sqrt(currentLen));
            currentLen -= currentLen % blockSize;
            boolean delay = arrayVisualizer.shuffleEnabled();
            double sleep = delay ? 1 : 0;

            for (int i = 0; i < currentLen; i += blockSize) {
                int randomIndex = random.nextInt((currentLen - i) / blockSize) * blockSize + i;
                blockSwap(array, i, randomIndex, blockSize, writes, sleep);
            }
        }

        private void blockSwap(int[] array, int a, int b, int len, Writes writes, double sleep) {
            for (int i = 0; i < len; i++) {
                writes.swap(array, a + i, b + i, sleep, true, false);
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
            return "Random w/ Crazy Blocks";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            int blockSize = pow2lte((int) Math.sqrt(currentLen));
            currentLen -= currentLen % blockSize;
            boolean delay = arrayVisualizer.shuffleEnabled();
            double sleep = delay ? 1 : 0;

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

        private void blockSwap(int[] array, int a, int b, int len, Writes writes, double sleep) {
            for (int i = 0; i < len; i++) {
                if (a != b)
                    writes.swap(array, a + i, b + i, sleep, true, false);
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
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            int blockSize = pow2lte((int) Math.sqrt(currentLen));
            currentLen -= currentLen % blockSize;
            boolean delay = arrayVisualizer.shuffleEnabled();
            double sleep = delay ? 1 : 0;

            int i = 0;
            int j = currentLen - blockSize;
            while (i < j) {
                blockSwap(array, i, j, blockSize, writes, sleep);
                i += blockSize;
                j -= blockSize;
            }
        }

        private void blockSwap(int[] array, int a, int b, int len, Writes writes, double sleep) {
            for (int i = 0; i < len; i++) {
                writes.swap(array, a + i, b + i, sleep, true, false);
            }
        }

        private int pow2lte(int value) {
            int val;
            for (val = 1; val <= value; val <<= 1)
                ;
            return val >> 1;
        }
    },
    UNIQUE {
        @Override
        public String getName() {
            return "Unique Pattern";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            Reads reads = arrayVisualizer.getReads();

            sort(array, 0, currentLen, delay ? 0.5 : 0, Writes);

            int[] tmp = Writes.createExternalArray(currentLen);
            int[] cnt = Writes.createExternalArray(currentLen);
            int[] pos = Writes.createExternalArray(currentLen);

            System.arraycopy(array, 0, tmp, 0, currentLen);

            int cIdx = 0, cPtr = 0, max = 0;

            for (int i = 0; i < currentLen; i++) {
                if (reads.compareIndices(array, i, cIdx, 0.5, true) > 0) {
                    cIdx = i;
                    cPtr = 0;
                }
                Writes.write(cnt, i, cPtr, 0.5, true, true);
                Writes.write(pos, cPtr, pos[cPtr] + 1, 0.5, true, true);
                cPtr++;
                max = Math.max(max, cPtr);
            }
            for (int i = 1; i < max; i++)
                Writes.write(pos, i, pos[i] + pos[i - 1], 1, true, true);

            for (int i = currentLen - 1; i >= 0; i--)
                Writes.write(array, --pos[cnt[i]], tmp[i], delay ? 0.5 : 0, true, false);
            Writes.deleteExternalArray(tmp);
            Writes.deleteExternalArray(cnt);
            Writes.deleteExternalArray(pos);
        }
    },
    INVERT {
        @Override
        public String getName() {
            return "Linear Invert";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            Reads Reads = arrayVisualizer.getReads();

            int[] tmp = Writes.createExternalArray(currentLen);
            tableinvert(array, tmp, currentLen, Reads, Writes, Highlights);
            Highlights.clearAllMarks();
            Writes.arraycopy(tmp, 0, array, 0, currentLen, delay ? 1 : 0, true, false);
            Writes.deleteExternalArray(tmp);
        }

        protected void siftDown(int[] array, int[] keys, int r, int len, int a, int t, Reads Reads, Writes Writes,
                Highlights Highlights) {
            int j = r;
            while (2 * j + 1 < len) {
                j = 2 * j + 1;
                if (j + 1 < len) {
                    int cmp = Reads.compareIndices(array, a + keys[j + 1], a + keys[j], 0.01, true);
                    if (cmp > 0 || (cmp == 0 && Reads.compareOriginalValues(keys[j + 1], keys[j]) > 0))
                        j++;
                }
            }
            for (int cmp = Reads.compareIndices(array, a + t, a + keys[j], 0.01, true); cmp > 0
                    || (cmp == 0 && Reads.compareOriginalValues(t, keys[j]) > 0); j = (j - 1) / 2, cmp = Reads
                            .compareIndices(array, a + t, a + keys[j], 0.01, true))
                ;
            for (int t2; j > r; j = (j - 1) / 2) {
                t2 = keys[j];
                Writes.write(keys, j, t, 0.01, true, true);
                t = t2;
            }
            Writes.write(keys, r, t, 0.01, true, true);
        }

        protected void tableSort(int[] array, int[] keys, int a, int b, Reads Reads, Writes Writes,
                Highlights Highlights) {
            int len = b - a;
            for (int i = (len - 1) / 2; i >= 0; i--)
                this.siftDown(array, keys, i, len, a, keys[i], Reads, Writes, Highlights);
            for (int i = len - 1; i > 0; i--) {
                int t = keys[i];
                Writes.write(keys, i, keys[0], 0.1, true, true);
                this.siftDown(array, keys, 0, i, a, t, Reads, Writes, Highlights);
            }
            Highlights.clearAllMarks();
        }

        protected void tableinvert(int[] array, int[] table, int currentLength, Reads Reads, Writes Writes,
                Highlights Highlights) {
            for (int i = 0; i < currentLength; i++)
                Writes.write(table, i, i, 1, true, true);
            tableSort(array, table, 0, currentLength, Reads, Writes, Highlights);
        }
    },
    PRIME {
        @Override
        public String getName() {
            return "Prime Numbers";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();

            for (int i = 0; i < currentLen; i++) {
                if (!isPrime(array[i]))
                    writes.write(array, i, 0, delay ? 1 : 0, true, false);
                else {
                    highlights.markArray(1, i);
                    delays.sleep(delay ? 1 : 0);
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
    },
    PRIMES_REVERSED {
        @Override
        public String getName() {
            return "Primes Reversed";
        }

        // sieve of Eratosthenes
        void sieve(boolean[] f, int n) {
            for (int i = 2; i <= n; i++)
                f[i] = true;
            for (int i = 2; i * i <= n; i++)
                if (f[i])
                    for (int j = i * i; j <= n; j += i)
                        f[j] = false;
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays Delays, Highlights Highlights,
                Writes Writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            boolean[] f = new boolean[currentLen];
            sieve(f, currentLen - 1);
            int[] indices = Writes.createExternalArray(currentLen);
            int cnt = 0;
            for (int i1 = 0; i1 < currentLen; i1++)
                if (f[i1]) {
                    Writes.write(indices, cnt, i1, 1, true, true);
                    cnt++;
                }
            for (int i = 0; i < cnt / 2; i++) {
                Writes.swap(array, indices[i], indices[cnt - 1 - i], delay ? 1 : 0, true, false);
            }
            Writes.deleteExternalArray(indices);
        }
    },
    ONLY_RUNS {
        @Override
        public String getName() {
            return "Random Runs";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            Random random = new Random();
            shuffle(array, 0, currentLen, delay ? 1 : 0, writes);
            int r = random.nextInt((int) Math.sqrt(currentLen));
            int i = 0;
            for (; i + r < currentLen; i += r) {
                sort(array, i, i + r, delay ? 1 : 0, writes);
                r = random.nextInt((int) Math.sqrt(currentLen));
            }
            sort(array, i, currentLen - 1, delay ? 1 : 0, writes);
        }
    },
    RUNS {
        @Override
        public String getName() {
            return "Random Runs + Scrambles";
        }

        @Override
        public void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays, Highlights highlights,
                Writes writes) {
            int currentLen = arrayVisualizer.getCurrentLength();
            boolean delay = arrayVisualizer.shuffleEnabled();
            Random random = new Random();
            shuffle(array, 0, currentLen, delay ? 1 : 0, writes);
            int r = random.nextInt((int) Math.sqrt(currentLen));
            for (int i = 0; i + r < currentLen; i += r) {
                if (random.nextInt(3) == 0)
                    sort(array, i, i + r, delay ? 1 : 0, writes);
                r = random.nextInt((int) Math.sqrt(currentLen));
            }
        }
    };

    public void sort(int[] array, int start, int end, double sleep, Writes writes) { // Pigeonhole Sort
        int min = array[start];
        int max = min;
        for (int i = start + 1; i < end; i++) {
            if (array[i] < min)
                min = array[i];
            else if (array[i] > max)
                max = array[i];
        }

        int size = max - min + 1;
        int[] holes = new int[size];

        for (int i = start; i < end; i++)
            writes.write(holes, array[i] - min, holes[array[i] - min] + 1, 0, false, true);

        for (int i = 0, j = start; i < size; i++) {
            while (holes[i] > 0) {
                writes.write(holes, i, holes[i] - 1, 0, false, true);
                writes.write(array, j, i + min, sleep, true, false);
                j++;
            }
        }
    }

    public void shuffle(int[] array, int start, int end, double sleep, Writes writes) {
        for (int i = start; i < end; i++) {
            int randomIndex = random.nextInt(end - i) + i;
            writes.swap(array, i, randomIndex, sleep, true, false);
        }
    }

    Random random = new Random();

    public abstract String getName();

    Reads reads;
    Highlights highlights;
    Writes writes;
    Delays delays;

    public abstract void shuffleArray(int[] array, ArrayVisualizer arrayVisualizer, Delays delays,
            Highlights highlights, Writes writes);
}
