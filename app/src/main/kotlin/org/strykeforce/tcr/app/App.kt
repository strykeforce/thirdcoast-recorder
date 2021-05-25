package org.strykeforce.tcr.app

import com.github.ajalt.clikt.completion.completionOption
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.check
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.long
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.strykeforce.nt.createTrigger
import org.strykeforce.tcr.Session
import org.strykeforce.tcr.asJson
import org.strykeforce.tcr.readSubscriptionFromFile
import org.strykeforce.tcr.writeToFile
import java.io.File

private const val SUBSCRIPTION_DEFAULT = "subscription.json"

class Tcr : CliktCommand() {
    init {
        completionOption()
    }

    val robotAddress by option(
        "-r",
        "--robot-address",
        help = "Address of robot providing telemetry",
        metavar = "ADDRESS"
    ).default("10.27.67.2")

    override fun run() {
        currentContext.obj = Session(address = robotAddress)
    }
}

class Inventory : CliktCommand("Print inventory to stdout.") {
    val session by requireObject<Session>()
    val format by option("-f", "--format").choice("csv", "json").default("json")
    override fun run() = runBlocking {
        val inventory = session.inventory()
        val output = when (format) {
            "csv" -> inventory.asCsv()
            "json" -> inventory.asJson()
            else -> ""
        }
        echo(output)
    }
}

class Subscription : CliktCommand("Print subscription template to stdout.") {
    val session by requireObject<Session>()
    val output by option("-o", "--output").file()

    override fun run() = runBlocking {
        val inventory = session.inventory()
        val subscription = inventory.toSubscription()
        if (output != null) {
            subscription.writeToFile(output!!)
            return@runBlocking
        }
        echo(subscription.asJson())
    }
}

class Trigger : CliktCommand(
    help = """
    Trigger command and capture for DURATION milliseconds.
    
    Subscription will be read from "$SUBSCRIPTION_DEFAULT" by default. Use the
    "subscription" command to create a subscription JSON file for editing.
    
    Telemetry output is written to "tcr-<TIMESTAMP>.csv".
"""
) {
    val session by requireObject<Session>()
    val duration: Long by argument().long()

    val subscription by option("-f", "--subscription-from", help = "Specify subscription JSON file.", metavar = "JSON")
        .file()
        .default(File(SUBSCRIPTION_DEFAULT))
        .check(lazyMessage = { "\"${it.name}\" does not exist." }) { it.exists() }

    override fun run() = runBlocking {
        val trigger = createTrigger()
        val subscription = readSubscriptionFromFile(subscription)
        println(subscription)
        launch {
            println("launch")
            session.start(subscription)
        }

        delay(2000)
        trigger.trigger()
        println("trigger")
        delay(duration)
        println("shutdown")
        session.shutdown()

    }
}

fun main(args: Array<String>) = Tcr().subcommands(Inventory(), Subscription(), Trigger()).main(args)

////    val inventory = session.inventory()
////    val subscription = inventory.toSubscription()
////    subscription.writeToFile("subscription.json")
