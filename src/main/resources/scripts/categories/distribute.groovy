import io.github.arrayv.prompts.SortPrompt

SortPrompt.setSortThreadForCategory('Distribution Sorts', 18) {
    run CountingSort go 2048.numbers, 1.5.speed
    run OptimizedPigeonholeSort go 64.numbers, 0.025.speed
    run OptimizedPigeonholeSort go 2048.numbers, 1.5.speed
    run GravitySort go 64.numbers, 0.025.speed
    run GravitySort go 1024.numbers
    run ClassicGravitySort go 64.numbers, 0.005.speed
    run ClassicGravitySort go 1024.numbers
    run SimplisticGravitySort go 64.numbers, 0.025.speed
    run SimplisticGravitySort go 1024.numbers
    run StaticSort go 64.numbers, 0.025.speed
    run StaticSort go 2048.numbers
    run IndexSort go 64.numbers, 0.005.speed
    run IndexSort go 2048.numbers
    run OptimizedIndexSort go 64.numbers, 0.005.speed
    run OptimizedIndexSort go 2048.numbers
    run FeatureSort go 64.numbers, 2.buckets, 0.25.speed
    run FeatureSort go 2048.numbers, 4.buckets, 1.25.speed
    run FeatureSort go 4096.numbers, 64.buckets, 2.5.speed
    run StacklessAmericanFlagSort go 64.numbers, 2.buckets, 0.025.speed
    run StacklessAmericanFlagSort go 2048.numbers, 128.buckets, 0.75.speed
    run StacklessAmericanFlagSort go 4096.numbers, 512.buckets
    run LSDRadixSort go 64.numbers, 4.buckets, 0.025.speed
    run LSDRadixSort go 2048.numbers, 4.buckets, 1.5.speed
    run LSDRadixSort go 4096.numbers, 16.buckets, 2.5.speed

    def oldSofterSounds = arrayv.sounds.softerSounds
    arrayv.sounds.softerSounds = true
    run InPlaceLSDRadixSort go 64.numbers, 10.buckets, 0.0001.speed
    run InPlaceLSDRadixSort go 2048.numbers, 10.buckets
    arrayv.sounds.softerSounds = oldSofterSounds

    run MSDRadixSort go 64.numbers, 8.buckets, 0.025.speed
    run MSDRadixSort go 2048.numbers, 4.buckets, 1.25.speed
    run MSDRadixSort go 4096.numbers, 512.buckets, 2.5.speed
    run InPlaceMSDRadixSort go 64.numbers, 8.buckets, 0.025.speed
    run InPlaceMSDRadixSort go 2048.numbers, 4.buckets, 1.25.speed
    run LMSDRadixSort go 64.numbers, 4.buckets, 0.025.speed
    run LMSDRadixSort go 2048.numbers, 4.buckets, 1.25.speed
    run LMSDRadixSort go 4096.numbers, 16.buckets, 2.5.speed
    run DivisorSort go 64.numbers, 2.buckets, 0.025.speed
    run DivisorSort go 2048.numbers, 4.buckets, 1.25.speed
    run DivisorSort go 4096.numbers, 256.buckets, 2.5.speed
    run RotateLSDRadixSort go 64.numbers, 8.buckets, 0.025.speed
    run RotateLSDRadixSort go 1024.numbers, 4.buckets
    run RotateLSDRadixSort go 2048.numbers, 128.buckets, 2.5.speed
    run RotateMSDRadixSort go 64.numbers, 8.buckets, 0.025.speed
    run RotateMSDRadixSort go 1024.numbers, 4.buckets
    run RotateMSDRadixSort go 2048.numbers, 128.buckets, 2.5.speed
    run FlashSort go 64.numbers, 0.025.speed
    run FlashSort go 2048.numbers
    run BinaryQuickSortIterative go 64.numbers, 0.025.speed
    run BinaryQuickSortIterative go 2048.numbers
    run StacklessBinaryQuickSort go 2048.numbers, 0.025.speed
    run StacklessBinaryQuickSort go 2048.numbers, 2.speed
    run QuickBinaryRadixSort go 64.numbers, 0.025.speed
    run QuickBinaryRadixSort go 2048.numbers
    run ShatterSort go 2048.numbers, 128.buckets
    run SimpleShatterSort go 2048.numbers, 128.buckets
    run TimeSort go 512.numbers, 10.buckets, 0.05.speed
}
