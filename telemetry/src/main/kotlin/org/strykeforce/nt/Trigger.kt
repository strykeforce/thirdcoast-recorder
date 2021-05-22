package org.strykeforce.nt

import edu.wpi.first.networktables.NetworkTableInstance
import kotlinx.coroutines.delay
import mu.KotlinLogging
import java.util.concurrent.CountDownLatch

private val logger = KotlinLogging.logger {}
private const val verbose = true

class Trigger(val server: String = "10.27.67.2") {

    init {
        val connectedSignal = CountDownLatch(1)
        var connectionListener = 0
        NetworkTableInstance.getDefault().apply {
            addLogger({ println("NetworkTables: ${it.message}") }, if (verbose) 20 else 30, 100)
            startClient(server)
            connectionListener = addConnectionListener({ connectedSignal.countDown() }, true)
        }
        connectedSignal.await()
        NetworkTableInstance.getDefault().removeConnectionListener(connectionListener)
    }

    suspend fun trigger() {
        val entry = NetworkTableInstance.getDefault().getEntry("Trigger")
        entry.setBoolean(false)
        delay(250)
        entry.setBoolean(true)
        logger.info {"triggered trigger"}
    }

}