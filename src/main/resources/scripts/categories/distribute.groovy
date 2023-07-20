import io.github.arrayv.prompts.SortPrompt

SortPrompt.setSortThreadForCategory('Distribution Sorts', 26) {
    run CountingSort go 2048.numbers, 1.5.speed
    run OptimizedPigeonholeSort go 2048.numbers, 1.5.speed
    run MoreOptimizedPigeonholeSort go 2048.numbers, 1.5.speed
    run GravitySort go 1024.numbers
    run ClassicGravitySort go 1024.numbers
    run SimplisticGravitySort go 1024.numbers, 2.speed
    run StaticSort go 2048.numbers
    run OptimizedIndexSort go 2048.numbers
    run FeatureSort go 2048.numbers, 4.buckets, 1.25.speed
    run StacklessAmericanFlagSort go 2048.numbers, 128.buckets, 0.75.speed
    run LSDRadixSort go 2048.numbers, 4.buckets, 1.5.speed

    def oldSofterSounds = arrayv.sounds.softerSounds
    arrayv.sounds.softerSounds = true
    run InPlaceLSDRadixSort go 2048.numbers, 10.buckets
    arrayv.sounds.softerSounds = oldSofterSounds

    run MSDRadixSort go 2048.numbers, 4.buckets, 1.25.speed
    run InPlaceMSDRadixSort go 2048.numbers, 4.buckets, 1.25.speed
    run LMSDRadixSort go 2048.numbers, 4.buckets, 1.25.speed
    run DivisorSort go 2048.numbers, 4.buckets, 1.25.speed
    run RotateLSDRadixSort go 1024.numbers, 4.buckets
    run RotateMSDRadixSort go 1024.numbers, 4.buckets
    run ProxmapSort go 512.numbers
    run FlashSort go 2048.numbers
    run HashSort go 2048.numbers
    run BinaryQuickSortIterative go 2048.numbers
    run StacklessBinaryQuickSort go 2048.numbers, 2.speed
    run QuickBinaryRadixSort go 2048.numbers
    run ShatterSort go 2048.numbers, 128.buckets
    run SimpleShatterSort go 2048.numbers, 128.buckets
    run AsynchronousSort go 1024.numbers, 1.5.speed
    run TimeSort go 512.numbers, 10.buckets, 0.05.speed
}
