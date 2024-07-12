import java.util.Scanner

open class Navigation {

    open fun printMenu(header: String, createMessage: String? = null, items: List<MenuItem>? = null) {

        println(header)

        if (createMessage != null) {
            println("0. $createMessage")
        }

        var numCmd = 1
        if (items != null) {
            items.forEachIndexed { index, menuItem -> println("${index+1}. $menuItem") }
            numCmd += items.size
        }

        println("${numCmd}. Выход")
    }

    open fun handleInput(
            input: Scanner,
            onCreate: () -> State,
            onSelect: (Int) -> State,
            onExit: () -> State,
            onError: () -> State,
            maxNum: Int = 0
    ): State {

        val num = input.nextLine().toIntOrNull() ?: -1
        return when (num) {
            0 -> onCreate()
            maxNum + 1 -> onExit()
            in (1..maxNum) -> onSelect(num)
            else -> {
                println("Неверный ввод. Введите число, сответствующее нужному пункту меню.")
                onError()
            }
        }
    }

    fun askString(input: Scanner, message: String): String {
        while (true) {
            println(message)
            val name = input.nextLine()
            if (name.isEmpty()) {
                println("Длина строки должна быть больше 0")
            } else {
                return name
            }
        }
    }
}



