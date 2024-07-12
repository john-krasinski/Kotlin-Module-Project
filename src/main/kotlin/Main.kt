import java.util.Scanner


fun main(args: Array<String>) {

    val input = Scanner(System.`in`)
    println("Добро пожаловать в Заметки!")
    var state:State = State.ArchiveSelectView(State.Exit("До свидания!"))
    while (! (state is State.Exit)) {
        state.printMenu()
        state = state.handleInput(input)
    }
    println((state as State.Exit).message)
}