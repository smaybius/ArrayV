import io.github.arrayv.prompts.SortPrompt
import io.github.arrayv.groovyapi.GroovyLocals
//Warning: With this current code, this script will run as soon as ArrayV is loaded if it's in the scripts/ folder.
runGroup(3) {
        setCategory("MSBMerge = false, onlyIncreasingRuns = false")
        run PowerSort go 2048.numbers, 0.buckets
        setCategory("MSBMerge = true, onlyIncreasingRuns = true")
        run PowerSort go 2048.numbers, 1.buckets
        setCategory("MSBMerge = true, onlyIncreasingRuns = false")
        run PowerSort go 2048.numbers, 2.buckets
    }