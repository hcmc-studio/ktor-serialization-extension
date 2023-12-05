package studio.hcmc.ktor.serialization

import io.ktor.http.*
import io.ktor.util.date.*
import kotlinx.serialization.json.Json

fun main() {
    val gmtDate = GMTDate(System.currentTimeMillis())
    val encodedGMTDate = Json.encodeToString(GMTDateSerializer, gmtDate)
    println(encodedGMTDate)
    val decodedGMTDate = Json.decodeFromString(GMTDateSerializer, encodedGMTDate)
    println(decodedGMTDate)

    val cookie = Cookie("name", "value", extensions = mapOf("a" to "1", "b" to null))
    val encodedCookie = Json.encodeToString(CookieSerializer, cookie)
    println(encodedCookie)
    val decodedCookie = Json.decodeFromString(CookieSerializer, encodedCookie)
    println(decodedCookie)

    val url = URLBuilder(URLProtocol.HTTP, "localhost", 8080, password = "world").build()
    val encodedUrl = Json.encodeToString(UrlSerializer, url)
    println(encodedUrl)
    val decodedUrl = Json.decodeFromString(UrlSerializer, encodedUrl)
    println(decodedUrl)
}