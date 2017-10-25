package io.marangoni.kotlin.simple.commands

import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

@Component
public class TestCommands: CommandMarker {
	
	@CliAvailabilityIndicator("hw simple", "json-extract", "json-generator", "spark-jdbc")
	fun isSimpleAvailable(): Boolean {
		// always available
		return true;
	}
	
	@CliCommand(value = "hw simple", help = "Print a simple hello world message")
	fun hello (
			@CliOption(key = arrayOf("message"), mandatory = true, help = "The hello world message") message: String,
			@CliOption(key = arrayOf("name1"), mandatory = true, help = "Say hello to the first name") name1: String,
			@CliOption(key = arrayOf("name2"), mandatory = true, help = "Say hello to a second name") name2: String,
			@CliOption(key = arrayOf("time"), mandatory = false, specifiedDefaultValue = "now", help = "When you are saying hello") time: String,
			@CliOption(key = arrayOf("location"), mandatory = false, help = "Where you are saying hello") location: String
	): String {
		return "Hello " + name1 + " and " + name2 + ". Your special message is " + message + ". time=[" + time + "] location=[" + location + "]";
	}
	
}