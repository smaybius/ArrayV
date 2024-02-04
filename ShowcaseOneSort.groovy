import io.github.arrayv.prompts.SortPrompt
import io.github.arrayv.utils.Shuffles
import io.github.arrayv.utils.ShuffleGraph
import io.github.arrayv.utils.Distributions
import io.github.arrayv.utils.Delays
def shuffles = arrayv.arrayManager.shuffles
def distributions = arrayv.arrayManager.distributions
//Warning: With this current code, this script will run as soon as ArrayV is loaded if it's in the scripts/ folder.
runGroup(shuffles.size() + distributions.size()) { // The number of inputs will be shown in the UI as the length of the shuffles plus the length of the distributions.
    for (shuffle in shuffles) { // Iterate through shuffles and distributions instead of manually listing each of them.
        if (shuffle != Shuffles.ALREADY) { // Exclude the "no shuffle" shuffle, for being redundant with "sorted".
            category = shuffle.name
            // Reverse the input first if the shuffle doesn't affect sorted inputs.
            if (shuffle == Shuffles.MIN_HEAPIFIED || shuffle == Shuffles.BINOMIAL_HEAPIFIED || shuffle == Shuffles.SMOOTH || shuffle == Shuffles.BINOMIAL_SMOOTH || shuffle == Shuffles.POPLAR || shuffle == Shuffles.OPTI_LAZY_HEAPIFIED || shuffle == Shuffles.PSEUDO_HEAPIFIED || shuffle == Shuffles.VELV_HEAP || shuffle == Shuffles.SCROLL_HEAPIFIED) {
                arrayv.arrayManager.setShuffleSingle(Shuffles.REVERSE).addSingle(shuffle)
            } else {
                arrayv.arrayManager.setShuffleSingle(shuffle)
            }
            run CircleHalverSort go 2048.numbers
        } else {
            arrayv.setUniqueItems(64)
            category = "Few Uniques (64)"
            arrayv.arrayManager.setShuffleSingle(Shuffles.RANDOM)
            run CircleHalverSort go 2048.numbers
            category = "Few Uniques (32)"
            arrayv.setUniqueItems(32)
            arrayv.arrayManager.setShuffleSingle(Shuffles.RANDOM)
            run CircleHalverSort go 2048.numbers
            category = "Few Uniques (16)"
            arrayv.setUniqueItems(16)
            arrayv.arrayManager.setShuffleSingle(Shuffles.RANDOM)
            run CircleHalverSort go 2048.numbers
            arrayv.setUniqueItems(2048)
        }
        
    }
    for (distrib in distributions) {
        if (distrib != Distributions.LINEAR || distrib != Distributions.CUSTOM) { // Exclude the redundant "linear" and the unnecessary "custom"
            category = distrib.name
            // Reverse the input first if the shuffle doesn't affect sorted inputs.
            if (distrib == Distributions.SIMILAR || distrib == Distributions.SQUARE || distrib == Distributions.SQRT || distrib == Distributions.CUBIC || distrib == Distributions.QUINTIC || distrib == Distributions.CBRT || distrib == Distributions.QTRT || distrib == Distributions.CANTOR || distrib == Distributions.SIGMOID || distrib == Distributions.VERT_SIG || distrib == Distributions.EXP) {
                arrayv.arrayManager.setShuffleSingle(distrib).addSingle(Shuffles.RANDOM)
            } else {
                arrayv.arrayManager.setShuffleSingle(distrib)
            }
            run CircleHalverSort go 2048.numbers
        }
        if (distrib == Distributions.CUSTOM)
        return;
    }
}