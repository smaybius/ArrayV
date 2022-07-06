import io.github.arrayv.prompts.SortPrompt

SortPrompt.setSortThreadForCategory('Merge Sorts', 25) {
    run MergeSort go 2048.numbers, 1.5.speed
    run MergeSortParallel go 2048.numbers, 1.5.speed
    run IterativeTopDownMergeSort go 2048.numbers, 1.5.speed
    run StacklessTopDownMergeSort go 2048.numbers, 1.5.speed
    run WeavedMergeSort go 2048.numbers, 1.5.speed
    run PDMergeSort go 2048.numbers
    run TwinSort go 2048.numbers
    run QuadSort go 2048.numbers
    run ImprovedInPlaceMergeSort go 2048.numbers, 1.5.speed
    run LazyStableSort go 256.numbers, 0.2.speed
    run BlockSwapMergeSort go 256.numbers, 0.1.speed
    run RotateMergeSort go 512.numbers, 0.2.speed
    run RotateMergeSortParallel go 512.numbers, 0.2.speed
    run RotateMergeSortEvenMoreParallel go 512.numbers, 0.2.speed
    run StacklessRotateMergeSort go 512.numbers, 0.2.speed
    run AndreySort go 2048.numbers
    run NewShuffleMergeSort go 1024.numbers
    run StrandSort go 2048.numbers
    run BufferedStoogeSort go 256.numbers, 0.2.speed
    run ImprovedBufferedStoogeSort2 go 256.numbers, 0.2.speed
    run IndexMergeSort go 1024.numbers
    run OptimizedPancakeSort go 1024.numbers
    run OutOfPlaceWeaveMergeSort go 2048.numbers
    run Split16Merge go 512.numbers
    run TransposeModuloWeaveMergeSort go 2048.numbers, 0.5.speed
}
