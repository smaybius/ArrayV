import io.github.arrayv.prompts.SortPrompt

SortPrompt.setSortThreadForCategory('Selection Sorts', 32) {
    run SelectionSort go 128.numbers, 0.01.speed
    run DoubleSelectionSort go 128.numbers, 0.01.speed
    run ReverseSelectionSort go 128.numbers, 0.01.speed
    run StableSelectionSort go 128.numbers, 0.5.speed
    run MultiSelectionSort go 128.numbers, 0.01.speed
    run ImprovedMultiSelectionSort go 128.numbers, 0.01.speed
    run RankSort go 128.numbers
    run CycleSort go 128.numbers, 0.01.speed
    run StableCycleSort go 128.numbers, 0.01.speed
    run BingoSort go 128.numbers, 0.1.speed
    run MaxHeapSort go 2048.numbers, 1.5.speed
    run BottomUpHeapSort go 2048.numbers, 1.5.speed
    run MinHeapSort go 2048.numbers, 1.5.speed
    run FlippedMinHeapSort go 2048.numbers, 1.5.speed
    run BaseNMaxHeapSort go 2048.numbers, 4.buckets, 1.5.speed
    run TriangularHeapSort go 2048.numbers, 1.5.speed
    run FibonacciHeapSort go 512.numbers, 1.5.speed
    run PureFibHeapSort go 512.numbers, 1.5.speed
    run WeakHeapSort go 2048.numbers
    run TernaryHeapSort go 2048.numbers
    run GeapSort go 2048.numbers
    run HeapHunterSort go 2048.numbers
    run ScrollHeapHunterSort go 2048.numbers
    run BinomialHeapSort go 2048.numbers, 1.5.speed
    run SmoothSort go 2048.numbers, 1.5.speed
    run BinomialSmoothSort go 2048.numbers, 1.5.speed
    run PoplarHeapSort go 2048.numbers
    run LazyHeapSort go 1024.numbers, 1.5.speed
    run OptimizedLazyHeapSort go 1024.numbers, 1.5.speed
    run HeavyHeapSort go 2048.numbers, 1.5.speed
    run TournamentSort go 2048.numbers, 1.5.speed
    run ClassicTournamentSort go 2048.numbers, 1.5.speed
}
