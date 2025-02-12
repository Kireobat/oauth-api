package eu.kireobat.oauthapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OauthApiApplication

fun main(args: Array<String>) {
	runApplication<OauthApiApplication>(*args)
}
