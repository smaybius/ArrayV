import io.github.arrayv.prompts.SortPrompt

SortPrompt.setSortThreadForCategory('Quick Sorts', 21) {
    run LLQuickSort go 2048.numbers, ((arrayv.arrayManager.containsShuffle(Shuffles.RANDOM) ? 1.5 : 5).speed)
    run LRQuickSort go 2048.numbers
    run LRQuickSortParallel go 2048.numbers
    run DualPivotQuickSort go 2048.numbers
    run StableQuickSort go 2048.numbers, ((arrayv.arrayManager.containsShuffle(Shuffles.RANDOM) ? 1 : 6.5).speed)
    run StableQuickSortMiddlePivot go 2048.numbers
    run StableQuickSortParallel go 2048.numbers
    run ForcedStableQuickSort go 2048.numbers
    run IndexQuickSort go 1024.numbers
    run TableSort go 1024.numbers, 0.75.speed
    run CubeRootQuickSort go 1024.numbers
    run ImprovedCubeRootQuickSort go 2048.numbers
    run IterativeQuickSort go 2048.numbers
    run StacklessQuickSort go 2048.numbers
    run TernaryLLQuickSort go 2048.numbers
    run TernaryLRQuickSort go 2048.numbers
    run LazyStableQuickSort go 512.numbers
    run LinkedIterativeQuickSort go 2048.numbers
    run MeanQuickSort go 2048.numbers
    run ooPQuicksort go 2048.numbers
    run MagneticaQuickSort go 2048.numbers
}
