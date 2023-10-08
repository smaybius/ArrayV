import io.github.arrayv.prompts.SortPrompt
import io.github.arrayv.groovyapi.GroovyLocals
//Warning: With this current code, this script will run as soon as ArrayV is loaded if it's in the scripts/ folder.
runGroup(4) {
        setCategory("Size 38")
        run BestForNSort go 38.numbers, 38.buckets, 0.01.speed
        setCategory("Size 49")
        run BestForNSort go 49.numbers, 49.buckets, 0.01.speed
        setCategory("Size 57")
        run BestForNSort go 57.numbers, 57.buckets, 0.01.speed
        setCategory("Size 64")
        run BestForNSort go 64.numbers, 64.buckets, 0.01.speed
    }