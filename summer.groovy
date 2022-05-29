import io.github.arrayv.prompts.SortPrompt
import io.github.arrayv.utils.Shuffles
import io.github.arrayv.utils.ShuffleGraph
import io.github.arrayv.utils.Distributions
def shuffles = arrayv.arrayManager.shuffles
def distributions = arrayv.arrayManager.distributions
//Warning: With this current code, this script will run as soon as ArrayV is loaded if it's in the scripts/ folder.
registerEventHandler(EventType.ARRAYV_FULLY_LOADED) {
    runGroup(shuffles.size() + distributions.size()) { // The number of inputs will be shown in the UI as the length of the shuffles plus the length of the distributions.
        for (shuffle in shuffles) { // Iterate through shuffles and distributions instead of manually listing each of them.
            if (shuffle != Shuffles.ALREADY) { // Exclude the "no shuffle" shuffle, for being redundant with "sorted".
                category = shuffle.name
            }
            // Reverse the input first if the shuffle doesn't affect sorted inputs.
            if (shuffle == Shuffles.MIN_HEAPIFIED || shuffle == Shuffles.BINOMIAL_HEAPIFIED || shuffle == Shuffles.SMOOTH || shuffle == Shuffles.BINOMIAL_SMOOTH || shuffle == Shuffles.POPLAR || shuffle == Shuffles.OPTI_LAZY_HEAPIFIED || shuffle == Shuffles.PSEUDO_HEAPIFIED) {
                arrayv.arrayManager.setShuffleSingle(Shuffles.REVERSE).addSingle(shuffle)
            } else {
                arrayv.arrayManager.setShuffleSingle(shuffle)
            }
            run OptimizedPigeonholeSort go 2048.numbers, 2048.speed
        }
    }
}