/**
 * Filename: YesnoquestApplication.kt
 * Author: Andrias Zelele
 * Date: 2026-03-03
 *
 * Description:
 * Main entry point for the YesNoQuest Spring Boot application.
 *
 * This class bootstraps the entire Spring context, enabling:
 * - Component scanning
 * - Auto-configuration
 * - Embedded server startup (Tomcat by default)
 * - Dependency injection wiring
 *
 * Responsibilities:
 * - Launch Spring Boot application
 * - Initialize configuration classes
 * - Start embedded web server
 *
 * Design Notes:
 * - @SpringBootApplication enables:
 *     @Configuration
 *     @EnableAutoConfiguration
 *     @ComponentScan
 * - runApplication() starts the application context
 */

package com.dre.yesnoquest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Primary Spring Boot configuration class.
 *
 * This annotation triggers component scanning starting
 * from this package downward.
 */
@SpringBootApplication
class YesnoquestApplication

/**
 * Main function that launches the Spring Boot application.
 *
 * @param args Command-line arguments
 */
fun main(args: Array<String>) {
	runApplication<YesnoquestApplication>(*args)
}