package org.strykeforce.tcr

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import java.io.File
import java.net.InetSocketAddress
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean


private val logger = KotlinLogging.logger {}

class Session(val address: String = "10.27.67.2", val port: Int = 5800) {

    val client = HttpClient(CIO) {
        install(JsonFeature)
//        engine {
//            proxy = ProxyBuilder.http("http://192.168.1.52:9090/")
//        }
    }

    val running = AtomicBoolean(false)

    val outputWriter =
        File("tcr-${Date().let { SimpleDateFormat("MMdd-HHmmss").format(it) }}.csv").bufferedWriter()

    suspend fun inventory(): Inventory {
        val endpoint = "http://$address:$port/v1/grapher/inventory"
        logger.debug { "GET inventory from $endpoint" }
        return client.get(endpoint)
    }

    suspend fun subscribe(subscriptionRequest: SubscriptionRequest): SubscriptionResponse {
        val endpoint = "http://$address:$port/v1/grapher/subscription"
        logger.debug { "POST subscription to $endpoint" }
        return client.post(endpoint) {
            contentType(ContentType.Application.Json)
            body = subscriptionRequest
        }
    }

    suspend fun start(subscriptionRequest: SubscriptionRequest) {
        if (!running.compareAndSet(false, true))
            throw IllegalStateException("session is already running")

        logger.info { subscriptionRequest }
        val response = subscribe(subscriptionRequest)
        logger.debug { response }

        writeCsvHeader(response)

        telemetryFlow()
            .onEach { it.adjustTimestamp(response.timestamp) }
            .collect { writeCsv(it.toCsv()) }
    }

    suspend fun shutdown() {
        if (!running.compareAndSet(true, false))
            throw IllegalStateException("session is not running")

        client.delete<Unit>("http://$address:$port/v1/grapher/subscription")
        closeOutputWriter()
        logger.info { "shutdown session" }
    }


    fun telemetryFlow(): Flow<Telemetry> = flow {
        val server = aSocket(ActorSelectorManager(Dispatchers.IO)).udp().bind(InetSocketAddress(5801))
        logger.info { "started UDP listener at ${server.localAddress}" }
        while (running.get()) {
            val datagram = server.receive()
            emit(Json.decodeFromString(datagram.packet.readText()))
        }
    }

    private suspend fun writeCsvHeader(subscriptionResponse: SubscriptionResponse) {
        val headers = subscriptionResponse.descriptions.map { it.toSlug() }
        writeCsv("timestamp,${headers.joinToString(separator = ",")}")
    }

    private suspend fun writeCsv(row: String) = withContext(Dispatchers.IO) {
        outputWriter.appendLine(row)
    }

    private suspend fun closeOutputWriter() = withContext(Dispatchers.IO) {
        try {
            outputWriter.close()
        } catch (e: Exception) {
            logger.error(e) { "closing CSV file" }
        }
    }
}

fun String.toSlug() = lowercase()
    .replace("\n", " ")
    .replace("[^a-z\\d\\s]".toRegex(), " ")
    .split(" ")
    .joinToString("_")
    .replace("-+".toRegex(), "_")