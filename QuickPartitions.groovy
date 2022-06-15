import io.github.arrayv.prompts.SortPrompt
import io.github.arrayv.groovyapi.GroovyLocals
//Warning: With this current code, this script will run as soon as ArrayV is loaded if it's in the scripts/ folder.
runGroup(4) {
        setCategory("Lomuto")
        run QuickSortPartitions go 2048.numbers, 0.buckets
        setCategory("Hoare")
        run QuickSortPartitions go 2048.numbers, 1.buckets
        setCategory("Modified Lomuto")
        run QuickSortPartitions go 2048.numbers, 2.buckets
        setCategory("Modified Hoare")
        run QuickSortPartitions go 2048.numbers, 3.buckets
    }