package app.absentmotion.desk

import app.absentmotion.editor.PreviewCache
import java.awt.Desktop

object DeskAway {
    private const val DROP_AFTER_MS = 60_000
    private val timer = javax.swing.Timer(DROP_AFTER_MS) { if (PreviewCache.automatic) PreviewCache.clear() }.apply { isRepeats = false }
    private var minimised = false
    private var hidden = false

    fun watch(window: java.awt.Frame) {
        window.addWindowListener(object : java.awt.event.WindowAdapter() {
            override fun windowIconified(e: java.awt.event.WindowEvent) { minimised = true; update() }
            override fun windowDeiconified(e: java.awt.event.WindowEvent) { minimised = false; update() }
        })
        runCatching {
            Desktop.getDesktop().takeIf { Desktop.isDesktopSupported() && it.isSupported(Desktop.Action.APP_EVENT_HIDDEN) }?.addAppEventListener(object : java.awt.desktop.AppHiddenListener {
                override fun appHidden(e: java.awt.desktop.AppHiddenEvent) { hidden = true; update() }
                override fun appUnhidden(e: java.awt.desktop.AppHiddenEvent) { hidden = false; update() }
            })
        }
    }

    private fun update() = if (minimised || hidden) timer.restart() else timer.stop()
}
