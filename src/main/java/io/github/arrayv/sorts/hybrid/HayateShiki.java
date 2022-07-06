package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

// Copyright(c) 2018 Emura Daisuke
// MIT License

public final class HayateShiki extends Sort {
    public HayateShiki(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Hayate-Shiki");
        this.setRunAllSortsName("Hayate-Shiki");
        this.setRunSortName("Hayate-Shiki");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // itr_t = int
    // dif_t = int

    private static int Bit(int v) {
        return (1 << v);
    }

    private static int cbIns = 5;
    private static int cnIns = Bit(cbIns);
    private int[] maExternal;

    enum oRow {
        oAsc,
        oDsc,
        oNum,
    };

    private class Part {
        int[] a = Writes.createExternalArray(3);
        int[] n = Writes.createExternalArray(3);
        oRow o;

        private void Delete() {
            Writes.deleteExternalArrays(a, n);
        }
    }

    private class Unit {
        int a;
        int n;
    }

    private static class Dive {
        Part mUnit;
        int miJoin;
    }

    int Msb(int v) {
        return (int) (Math.log(v) / Math.log(2));
    }

    int MsbAlignment(int v) {
        return Msb(v + v - 1);
    }

    int LowerLimit(int v, int limit) {
        return (v > limit) ? v : limit;
    }

    int Copy(int iDst, int iSrc, int nSrc) {
        while (nSrc-- > 0) {
            iDst++;
            iDst = iSrc;
            iSrc++;

        }
        return iDst;
    }

    int Copy(int iDst, int iSrc, int nSrc, int v) {
        iDst = v;
        return Copy(++iDst, ++iSrc, --nSrc);
    }

    int Join(int iJoin, Unit rUnit, Part rPart) {
        int nDsc = rPart.n[1];
        int nAsc = rPart.n[0];

        rUnit.a = iJoin;
        rUnit.n = nDsc + nAsc;

        iJoin = Copy(iJoin, rPart.a[1], nDsc);
        iJoin = Copy(iJoin, rPart.a[0], nAsc);
        return iJoin;
    }

    int findRun(int[] array, int a, int b) {
        int i = a + 1;
        if (i == b)
            return i;
        if (Reads.compareIndices(array, i - 1, i++, 1, true) == 1) {
            while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) == 1)
                i++;
            Writes.reversal(array, a, i - 1, 1, true, false);
        } else
            while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) <= 0)
                i++;
        Highlights.clearMark(2);
        return i;
    }

    private int MakePart(int[] arr, Part rPart, int riSrc, int eSrc, int raDsc) {
        int iSrc = riSrc;
        int aAsc = iSrc;
        int eAsc = eSrc;
        int nSrc = eSrc - riSrc;
        if (nSrc > 1) {
            eAsc = findRun(arr, iSrc, nSrc);
        }

        int aDsc = raDsc;
        int eDsc = aDsc;

        int iOdd = eAsc;
        int nOdd = eSrc - iOdd;
        if (nOdd > 0) {
            if (Reads.compareValues(arr[iOdd], aAsc) > 0) {
                Writes.write(arr, --aDsc, arr[iOdd++], 1, true, false);

                while (--nOdd > 0) {
                    if (Reads.compareValues(arr[iOdd], eAsc - 1) > 0) {
                        if (Reads.compareValues(arr[iOdd], aDsc) > 0) {
                            Writes.write(arr, --aDsc, arr[iOdd++], 1, true, false);
                        } else {
                            break;
                        }
                    } else {
                        Writes.write(arr, eAsc++, arr[iOdd++], 1, true, false);
                    }
                }
            }
        }
        int nDsc = eDsc - aDsc;
        Writes.write(rPart.a, 0, aAsc, 1, false, true); // Part::oAsc
        Writes.write(rPart.n, 0, eAsc - aAsc, 1, false, true); // Part::oAsc
        Writes.write(rPart.a, 1, aDsc, 1, false, true); // Part::oDsc
        Writes.write(rPart.n, 1, nDsc, 1, false, true); // Part::oDsc
        rPart.o = (nDsc > 0) ? oRow.oDsc : oRow.oAsc;

        riSrc = iOdd;
        raDsc = aDsc;
        return nOdd;
    }

    private void Turn(int[] arr, int iDst, int iSrc, int nSrc) {
        iDst += nSrc;
        iSrc += nSrc;
        while (nSrc-- > 0)
            Writes.write(arr, --iDst, arr[--iSrc], 1, true, false);
    }

    private void Turn(int[] arr, Part rPart) {
        int nDsc = rPart.n[1];
        int nAsc = rPart.n[0];
        int aDsc = arr[0];
        int aAsc = aDsc + nDsc;

        if (nDsc > 0) {
            Turn(arr, aAsc, rPart.a[0], nAsc);
            Turn(arr, aDsc, rPart.a[1], nDsc);
        }
    }

    @Override
    public void runSort(int[] maOriginal, int mnOriginal, int bucketCount) {
        maExternal = Writes.createExternalArray(mnOriginal);
        int nDive = LowerLimit((MsbAlignment(mnOriginal) - cbIns), 1);
        Dive[] aDive = new Dive[nDive + 1];
        for (int oDive = 0; oDive < nDive; ++oDive) {
            aDive[oDive].miJoin = (oDive & Bit(0)) > 0 ? maExternal[0] : maOriginal[0];
        }
        int nJoin = 0;

        int iJoin = maExternal[0];

        int iSrc = maOriginal[0];
        int eSrc = mnOriginal;
        while (iSrc != eSrc) {
            Unit vUnit = new Unit();

            Part vPart0 = new Part();
            Part vPart1 = new Part();
            int aDsc = maExternal.length;
            if (MakePart(maOriginal, vPart0, iSrc, eSrc, aDsc) > 0) {
                MakePart(maOriginal, vPart1, iSrc, eSrc, aDsc);

            }
        }
    }
}
