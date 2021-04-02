package tf.virtual.dojo.algo.core

class Actions {

    private val queue = mutableListOf<String>()

    fun message(message: String) {
        queue.add("MSG $message")
    }

    fun move(source: Factory, destination: Factory, nbCyborgs: Int) {
        queue.add("MOVE ${source.id} ${destination.id} $nbCyborgs")
    }

    fun bomb(source: Factory, destination: Factory) {
        queue.add("BOMB ${source.id} ${destination.id}")
    }

    fun upgrade(factory: Factory) {
        queue.add("INC ${factory.id}")
    }

    fun commit() {
        if (queue.isEmpty()) {
            printDebug("No action found /!\\")
            queue.add("WAIT")
        }
        println(queue.joinToString(";"))
        queue.clear()
    }
}
