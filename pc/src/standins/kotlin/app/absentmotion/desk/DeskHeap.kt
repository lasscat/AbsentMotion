package app.absentmotion.desk

import app.absentmotion.editor.packedStoreBudget
import com.sun.management.HotSpotDiagnosticMXBean
import java.lang.management.ManagementFactory

object DeskHeap {
    private const val WORKING_BYTES = 512L * 1024 * 1024

    fun softLimit(cacheMb: Int, heapMax: Long): Long = (packedStoreBudget(cacheMb, heapMax) + WORKING_BYTES).coerceAtMost(heapMax)

    fun follow(cacheMb: Int) {
        val limit = softLimit(cacheMb, Runtime.getRuntime().maxMemory())
        runCatching { ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean::class.java).setVMOption("SoftMaxHeapSize", limit.toString()) }
    }
}
