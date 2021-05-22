package org.strykeforce.tcr.app

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.strykeforce.nt.Trigger
import org.strykeforce.tcr.Session
import org.strykeforce.tcr.readSubscriptionFromFile

fun main() = runBlocking {
    val session = Session()
    val trigger = Trigger()
    
//    val inventory = session.inventory()
//    val subscription = inventory.toSubscription()
//    subscription.writeToFile("subscription.json")


    val subscription = readSubscriptionFromFile("subscription.json")
    launch {
        session.start(subscription)
    }

    trigger.trigger()
    delay(5000)
    session.shutdown()
}
