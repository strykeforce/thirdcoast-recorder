package org.strykeforce.nt

import edu.wpi.first.networktables.LogMessage
import edu.wpi.first.networktables.NetworkTableInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import java.util.concurrent.CountDownLatch

private val logger = KotlinLogging.logger {}
private const val entryName = "Trigger"
private const val triggerDelay = 100L

suspend fun createTrigger(): Trigger = withContext(Dispatchers.IO) {
    Trigger()
}

class Trigger internal constructor(val server: String = "10.27.67.2") {

    init {
        val connectedSignal = CountDownLatch(1)
        var connectionListener = 0
        NetworkTableInstance.getDefault().apply {
            addLogger(Logger(), LogMessage.kInfo, LogMessage.kCritical)
            startClient(server)
            connectionListener = addConnectionListener({ connectedSignal.countDown() }, true)
        }
        connectedSignal.await()
        NetworkTableInstance.getDefault().removeConnectionListener(connectionListener)
    }

    suspend fun trigger() {
        val entry = NetworkTableInstance.getDefault().getEntry(entryName)
        entry.setBoolean(false)
        delay(triggerDelay)
        entry.setBoolean(true)
        logger.info { "NetworkButton '$entryName' triggered" }
    }

}