import io.github.arrayv.prompts.SortPrompt
import io.github.arrayv.utils.Shuffles

SortPrompt.setSortThreadForCategory('Exchange Sorts', 27) {
    run MoreOptimizedBubbleSort go 512.numbers, 1.5.speed
    run ReverseBubbleSort go 512.numbers, 1.5.speed
    run CocktailShakerSort go 512.numbers, 1.25.speed
    run OptimizedCocktailShakerSort go 512.numbers, 1.25.speed
    run ChinottoSort go 512.numbers
    run DandelionSort go 512.numbers, 2.speed
    run ReverseGnomeSort go 512.numbers, 0.5.speed
    run OddEvenSort go 512.numbers
    run SwapMapSort go 512.numbers, 0.5.speed
    run CombSort go 1024.numbers, 130.buckets
    run SplitCenterSort go 512.numbers, 0.5.speed
    run OptimizedStoogeSort go 512.numbers
    run OptimizedStoogeSortStudio go 512.numbers
    run FunSort go 256.numbers, 2.speed
    run ThreeSmoothCombSortRecursive go 1024.numbers, 1.25.speed
    run ThreeSmoothCombSortParallel go 1024.numbers, 1.25.speed
    run ThreeSmoothCombSortIterative go 1024.numbers, 1.25.speed
    run ClassicThreeSmoothCombSort go 1024.numbers, 1.25.speed
    run CircleSortParallel go 1024.numbers
    run CircloidSort go 1024.numbers
    run CompleteGraphSort go 1024.numbers
    run WeirdInsertionSort go 512.numbers
    run PseudoHeapSort go 1024.numbers, 2.speed
    run WiggleSort go 512.numbers
    run SandpaperSort go 512.numbers
    run ReverseSandpaperSort go 512.numbers
    run OptimizedReverseSandpaperSort go 512.numbers
}
