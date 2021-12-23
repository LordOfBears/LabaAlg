import kotlin.math.*

fun main() {
    val expr = "sin 1 + 2 ^ 3"
    val pattern = "-?\\d*\\.?\\d+|\\(|\\)|\\+|-|\\*|/|\\^|(sin)".toRegex()
    val list = pattern.findAll(expr).map { it.value }.toList()
    println(list)
    println(infixToPostfix(list))
    println(calculator(infixToPostfix(list)))
}

fun infixToPostfix(arr: List<String>): List<String> {
    val priority = mapOf(
        "+" to 1, "-" to 1,
        "*" to 2, "/" to 2,
        "^" to 3,
        "sin" to 4,
        "(" to 5
    )
    val stack = mutableListOf<String>()
    val postfix = mutableListOf<String>()

    for (str in arr) {
        if (str.toDoubleOrNull() != null) {
            postfix.add(str)
        } else {
            when (str) {
                "(" -> stack.add(str)
                ")" -> {
                    while (stack.last() != "(") postfix.add(stack.removeLast())
                    stack.removeLast()
                }
                else -> {
                    while ((stack.isNotEmpty()) && (stack.last() != "(")
                        && (priority.getValue(str) <= priority.getValue(stack.last()))) postfix.add(stack.removeLast())
                    stack.add(str)
                }
            }
        }
    }
    while (stack.isNotEmpty()) postfix.add(stack.removeLast())
    return postfix.toList()
}

fun calculator(arrPostfix: List<String>): Double {
    val stack = mutableListOf<Double>()
    for (str in arrPostfix) {
        if (str.toDoubleOrNull() != null) stack.add(str.toDouble())
        when(str) {
            "+" -> {
                val a = stack.removeLast()
                val b = stack.removeLast()
                stack.add(a + b)
            }
            "-" -> {
                val a = stack.removeLast()
                val b = stack.removeLast()
                stack.add(b - a)
            }
            "*" -> {
                val a = stack.removeLast()
                val b = stack.removeLast()
                stack.add(a * b)
            }
            "/" -> {
                val a = stack.removeLast()
                val b = stack.removeLast()
                stack.add(b / a)
            }
            "^" -> {
                val a = stack.removeLast()
                val b = stack.removeLast()
                stack.add(b.pow(a))
            }
            "sin" -> {
                val a = stack.removeLast()
                stack.add(sin(a))
            }
        }
    }
    return stack[0]
}