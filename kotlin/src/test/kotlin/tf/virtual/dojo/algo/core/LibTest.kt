package tf.virtual.dojo.algo.core

import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

class LibTest {

    @Test
    fun testCanPrintDebug() {
        val os = ByteArrayOutputStream()
        PrintStream(os).use {
            val oldErr = System.err
            System.setErr(it)
            printDebug("foobar")
            System.setErr(oldErr)
        }
        assertEquals("foobar\n", os.toString())
    }
}
