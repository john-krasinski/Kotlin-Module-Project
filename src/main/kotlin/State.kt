import java.util.Scanner

sealed interface State {
    fun handleInput(input: Scanner): State
    fun printMenu()
    data class ArchiveSelectView(val parentState: State) : Navigation(), State {

//        init {
//            super.currentState = this
//        }
        private var archives: MutableList<Archive> = mutableListOf()

        private fun createArchive(input: Scanner) : State {
            val name = askString(input, "Введите имя архива:")
            archives.add(Archive(name))
            return this
        }

        override fun printMenu() {
            super.printMenu("Список архивов:", "Создать архив", archives as List<MenuItem>)
        }

        override fun handleInput(input: Scanner): State {
            val onCreate = { createArchive(input) }
            val onSelect = { num: Int -> State.NotesView(archives[num-1], this) }
            val onExit = { parentState }
            val onError = { this }

           return super.handleInput(input, onCreate, onSelect, onExit, onError, archives.size)
        }

    }

    data class NotesView(val archive: Archive, val parentState: State) : Navigation(), State {

        private fun createNote(input: Scanner) : State {
            val name = askString(input, "Введите имя заметки")
            var text: String? = null
            while (true) {
                text = askText(input, "Введите текст", "*КОНЕЦ")
                if (text == null) {
                    println("Извините, добавить пустую заметку нельзя")
                } else {
                    break
                }
            }

            if (text != null) {
                archive.notes.add(Note(name,text))
            }

            return this
        }

        override fun printMenu() {
            super.printMenu(
                    "Открыт архив $archive\nСозданные заметки:",
                    "Создать заметку",
                    archive.notes)
        }

        override fun handleInput(input: Scanner): State {
            val onCreate = { createNote(input) }
            val onSelect = { num: Int -> State.ReadingNoteView(archive.notes[num-1], this) }
            val onExit = { parentState }
            val onError = { this }

            return super.handleInput(input, onCreate, onSelect, onExit, onError, archive.notes.size)
        }

        private fun askText(input: Scanner, message: String, stopMarker: String?): String? {
            if (stopMarker == null) {
                return askString(input, message)
            }
            println(message + ", для завершения введите '${stopMarker}'")
            var text = ""
            var line = input.nextLine()
            while (!line.endsWith(stopMarker)) {
                text += line + '\n'
                line = input.nextLine()
            }

            text += line.subSequence(0, line.length - stopMarker.length)
            if (text.length == 0) {
                return null
            }
            text += "\n"
            return text
        }
    }

    data class ReadingNoteView(val note: Note, val parentState: State) : Navigation(), State {

        override fun printMenu() {
            super.printMenu("Заметка ${note.name}:\n\n" + note.text,null, null)
        }

        override fun handleInput(input: Scanner): State {
            //  support only returning back with cmd "0"
            return super.handleInput(input, { parentState }, { this }, { this }, { this } , 0)
        }
    }

    data class Exit(val message: String): State {
        override fun handleInput(input: Scanner): State { return this }
        override fun printMenu() {}
    }

}