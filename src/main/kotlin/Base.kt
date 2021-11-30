
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.File
import kotlin.system.measureNanoTime

private fun getInputFile(): File {
    var name = Throwable().stackTrace.first { it.className.contains("day") }.className.split(".")[0]
    val file = File("src/main/kotlin/$name/input")
    if (file.readText().isBlank()) {
        name = name.replace("_2", "_1")
        return File("src/main/kotlin/$name/input")
    }
    return file
}

private fun getInputs(): List<String> {
    return getInputFile().readText().trim().split("\n\n\n")
}

data class Run(val input: String, val output: String, val time: Double)

fun printInput(input: String) {
    if (input.contains("\n")) {
        if (input.lines().size >= 10) {
            println("In:\n")
            println(input.lines().take(2).joinToString("\n"))
            println("[> not showing ${input.lines().size - 4} lines <]")
            println(input.lines().takeLast(2).joinToString("\n"))
        } else {
            println("In:\n$input")
        }
    } else {
        println("In: $input")
    }
}

fun solve(additionalTiming: Boolean = false, solve: (List<String>) -> Any?) {
    val inputs = getInputs()
    val runs = mutableListOf<Run>()
    var finalAnswer = ""
    inputs.forEach {
        println("==========")
        printInput(it)
        var answer = ""
        val ns = measureNanoTime {
            answer = solve(it.lines()).toString()
        }
        val ms = ns / 1000000.0
        val time = "${String.format("%.3f", ms)}ms"
        println("Out: $answer [$time]")
        runs += Run(it, answer, ms)
        finalAnswer = answer
    }
    Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(finalAnswer), null)
    Thread.sleep(200) // Wait so the system has chance to notice the clipboard change

    if (additionalTiming) {
        val timeSensitiveRuns = runs.filter { it.time < 250 }
        if (timeSensitiveRuns.isNotEmpty()) {
            println("Rerunning for timing")
            timeSensitiveRuns.forEach { run ->
                val times = mutableListOf<Double>()
                val count = if (run.time < 100) 1000 else 100
                repeat(count) {
                    val ns = measureNanoTime { solve(run.input.lines()) }
                    val ms = ns / 1000000.0
                    times += ms
                }
                println("In: ${run.input.lines().first().take(50)} --> Out: ${run.output} [${String.format("%.3f", times.minOrNull())}ms]")
            }
        }
    }
}
