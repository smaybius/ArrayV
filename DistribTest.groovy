import io.github.arrayv.prompts.SortPrompt
import io.github.arrayv.groovyapi.GroovyLocals
//Warning: With this current code, this script will run as soon as ArrayV is loaded if it's in the scripts/ folder.
runGroup(256) { //made to test non-deterministic shuffles that might throw exceptions
        for (int i in 0..256) {
            run OptimizedPigeonholeSort go 128.numbers, 2048.speed
        }
    }