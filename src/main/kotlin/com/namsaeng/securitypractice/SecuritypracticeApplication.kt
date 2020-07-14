package com.namsaeng.securitypractice

import org.apache.catalina.Context
import org.apache.catalina.connector.Connector
import org.apache.tomcat.util.descriptor.web.SecurityCollection
import org.apache.tomcat.util.descriptor.web.SecurityConstraint
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.servlet.server.ServletWebServerFactory
import org.springframework.context.annotation.Bean


// jdk bin 폴더에서 keytool -genkey -alias batoners -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore batoners.p12 -validity 10000 입력
// 그리고 그 p12 파일을 resources 폴더에 넣자.
// application.properties 파일 고치고 이걸 고치면 이렇게 된다.
// not secure가 뜨는 건 custom certification 때문에 그렇다. 인증된 기관 것을 사용하면 없어진다.

@SpringBootApplication
class SecurityApplication {
	@Bean
	fun servletContainer(): ServletWebServerFactory {
		// Enable SSL Traffic
		val tomcat: TomcatServletWebServerFactory = object : TomcatServletWebServerFactory() {
			override fun postProcessContext(context: Context) {
				val securityConstraint = SecurityConstraint()
				securityConstraint.userConstraint = "CONFIDENTIAL"
				val collection = SecurityCollection()
				collection.addPattern("/*")
				securityConstraint.addCollection(collection)
				context.addConstraint(securityConstraint)
			}
		}

		// Add HTTP to HTTPS redirect
		tomcat.addAdditionalTomcatConnectors(httpToHttpsRedirectConnector())
		return tomcat
	}

	/*
    We need to redirect from HTTP to HTTPS. Without SSL, this application used
    port 8082. With SSL it will use port 8443. So, any request for 8082 needs to be
    redirected to HTTPS on 8443.
     */
	private fun httpToHttpsRedirectConnector(): Connector {
		val connector = Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL)
		connector.scheme = "http"
		connector.port = 8080
		connector.secure = false
		connector.redirectPort = 443
		return connector
	}
}

fun main(args: Array<String>) {
	runApplication<SecurityApplication>(*args)
}