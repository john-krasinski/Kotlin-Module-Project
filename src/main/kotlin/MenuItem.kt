open class MenuItem(val name: String) {
    override fun toString(): String {
        return name
    }
}

class Archive(archiveName:String): MenuItem(archiveName) {
    public var notes: MutableList<Note> = mutableListOf()
}
class Note(noteName:String, var text: String): MenuItem(noteName)