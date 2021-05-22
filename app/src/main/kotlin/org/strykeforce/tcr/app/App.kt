package org.strykeforce.tcr.app

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.strykeforce.nt.Trigger
import org.strykeforce.tcr.Item
import org.strykeforce.tcr.Session
import org.strykeforce.tcr.SubscriptionRequest

fun main() = runBlocking {
    val session = Session()
    val trigger = Trigger()
//    val inventory = session.inventory()

//    session.subscribe2(
//        SubscriptionRequest(
//            listOf(
////                Item(0, "SELECTED_SENSOR_VELOCITY"),
//                Item(0, "OUTPUT_VOLTAGE"),
////                Item(0, "OUTPUT_PERCENTAGE"),
//            )
//        )
//    )

//    println(inventory)

    launch {
        session.start(
            SubscriptionRequest(
                listOf(
                    Item(0, "SELECTED_SENSOR_VELOCITY"),
                    Item(0, "OUTPUT_VOLTAGE"),
//                    Item(0, "OUTPUT_PERCENTAGE"),
                )
            )
        )
    }

    trigger.trigger()
    delay(5000)
    session.shutdown()
}
