package org.strykeforce.nt

import edu.wpi.first.networktables.LogMessage
import mu.KotlinLogging
import java.util.function.Consumer

private val logger = KotlinLogging.logger("NetworkTables")

class Logger : Consumer<LogMessage> {
    override fun accept(logMessage: LogMessage) {
        when (logMessage.level) {
            in 0..LogMessage.kDebug -> logger.debug(logMessage.message)
            in LogMessage.kDebug..LogMessage.kInfo -> logger.info(logMessage.message)
            in LogMessage.kInfo..LogMessage.kWarning -> logger.warn(logMessage.message)
            in LogMessage.kWarning..LogMessage.kCritical -> logger.error(logMessage.message)
        }
    }
}