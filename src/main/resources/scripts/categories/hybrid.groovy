import io.github.arrayv.prompts.SortPrompt
import io.github.arrayv.utils.Shuffles

SortPrompt.setSortThreadForCategory('Hybrid Sorts', 70) {
    /*run HybridCombSort go 1024.numbers
    run IntroCircleSortRecursive go 1024.numbers
    run IntroCircleSortIterative go 1024.numbers
    run PairwiseCircleSort go 1024.numbers
    run QuickSPSort go 512.numbers
    run BinaryMergeSort go 2048.numbers
    run MergeInsertionSort go 2048.numbers, 1.75.speed
    run BaseNMergeSort go 2048.numbers, 4.buckets
    run WeaveMergeSort go 2048.numbers, ((arrayv.arrayManager.containsShuffle(Shuffles.RANDOM) ? 1.65 : 6.5).speed)
    run ImprovedWeaveMergeSort go 2048.numbers
    run SampleSort go 2048.numbers
    run TimSort go 2048.numbers
    run StacklessTimSort go 2048.numbers
    run CocktailMergeSort go 2048.numbers
    run OptimizedPDMergeSort go 2048.numbers
    run BinaryPDMergeSort go 2048.numbers
    run ThreadedPDMergeSort go 2048.numbers
    run LazierSort go 1024.numbers
    run LazierestSort go 1024.numbers
    run LaziestSort go 1024.numbers
    run PDLaziestSort go 1024.numbers
    run WikiSort go 2048.numbers
    run MiniWikiSort go 2048.numbers
    run GrailSort go 2048.numbers
    run MiniGrailSort go 2048.numbers
    run AdaptiveGrailSort go 2048.numbers
    run UnstableGrailSort go 2048.numbers
    run CircularGrailSort go 2048.numbers
    run ChaliceSort go 2048.numbers
    run OptimizedLazyStableSort go 256.numbers, 0.2.speed
    run SqrtSort go 2048.numbers
    run KotaSort go 2048.numbers
    run EctaSort go 2048.numbers
    run KitaSort go 2048.numbers
    run MiniKitaSort go 2048.numbers
    run UnstableKitaSort go 2048.numbers
    run ParallelBlockMergeSort go 2048.numbers
    run ParallelGrailSort go 2048.numbers
    run FlanSort go 2048.numbers
    run RemiSort go 2048.numbers
    run BufferedMergeSort go 2048.numbers
    run OOPBufferedMergeSort go 2048.numbers, 2.speed
    run SynchronousEctaSort go 2048.numbers
    run SynchronousSqrtSort go 2048.numbers
    run ImprovedBlockSelectionSort go 2048.numbers
    run BufferedBlockSelectionMergeSort go 2048.numbers, 2.speed
    run MedianMergeSort go 2048.numbers
    run InPlaceStableCycleSort go 1024.numbers
    run BufferPartitionMergeSort go 2048.numbers*/
    run QuarterMergeSort go 2048.numbers
    run StableQuarterMergeSort go 2048.numbers
    run FifthMergeSort go 2048.numbers
    run YujisBufferedMergeSort2 go 2048.numbers
    run BubblescanQuickSort go 2048.numbers
    run IntroSort go 2048.numbers
    run OptimizedBottomUpMergeSort go 2048.numbers
    run OptimizedDualPivotQuickSort go 2048.numbers, 0.75.speed
    run FluxSort go 2048.numbers
    run OptimizedRotateMergeSort go 1024.numbers
    run OptimizedWeaveMergeSort go 1024.numbers, 0.4.speed
    run StupidQuickSort go 1024.numbers
    run LAQuickSort go 2048.numbers
    run MedianOfSixteenAdaptiveQuickSort go 2048.numbers
    run SimpleHybridQuickSort go 2048.numbers
    run StacklessHybridQuickSort go 2048.numbers, 0.75.speed
    run StacklessDualPivotQuickSort go 2048.numbers, 1.5.speed
    run PDQBranchedSort go 2048.numbers, 0.75.speed
    run PDQBranchlessSort go 2048.numbers, 0.75.speed
    run DropMergeSort go 2048.numbers, 0.75.speed
}
