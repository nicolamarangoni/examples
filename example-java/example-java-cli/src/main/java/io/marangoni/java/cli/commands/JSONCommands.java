package io.marangoni.java.cli.commands;

import org.slf4j.LoggerFactory;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

@Component
public class JSONCommands implements CommandMarker {

	static final org.slf4j.Logger logger = LoggerFactory.getLogger(JSONCommands.class);

	private boolean simpleCommandExecuted = false;

	@CliAvailabilityIndicator({ "hw simple", "json-extract", "json-generator", "spark-jdbc" })
	public boolean isSimpleAvailable() {
		// always available
		return true;
	}

	@CliAvailabilityIndicator({ "hw complex", "hw enum" })
	public boolean isComplexAvailable() {
		if (simpleCommandExecuted) {
			return true;
		} else {
			return false;
		}
	}

	@CliCommand(value = "hw simple", help = "Print a simple hello world message")
	public String simple(@CliOption(key = { "message" }, mandatory = true, help = "The hello world message") final String message,
			@CliOption(key = { "location" }, mandatory = false, help = "Where you are saying hello", specifiedDefaultValue = "At work") final String location) {
		simpleCommandExecuted = true;
		return "Message = [" + message + "] Location = [" + location + "]";
	}

	@CliCommand(value = "hw complex", help = "Print a complex hello world message (run 'hw simple' once first)")
	public String hello(
			@CliOption(key = { "message" }, mandatory = true, help = "The hello world message") final String message,
			@CliOption(key = { "name1" }, mandatory = true, help = "Say hello to the first name") final String name1,
			@CliOption(key = { "name2" }, mandatory = true, help = "Say hello to a second name") final String name2,
			@CliOption(key = { "time" }, mandatory = false, specifiedDefaultValue = "now", help = "When you are saying hello") final String time,
			@CliOption(key = { "location" }, mandatory = false, help = "Where you are saying hello") final String location
	) {
		return "Hello " + name1 + " and " + name2 + ". Your special message is " + message + ". time=[" + time + "] location=[" + location + "]";
	}

	@CliCommand(value = "hw enum", help = "Print a simple hello world message from an enumerated value (run 'hw simple' once first)")
	public String eenum(@CliOption(key = { "message" }, mandatory = true, help = "The hello world message") final MessageType message) {
		return "Hello.  Your special enumerated message is " + message;
	}

	@CliCommand(value = "json-extract")
	public String jsonExtract(@CliOption(key = { "property-file" }, mandatory = false, help = "The property file") final String propertyFile) {
		io.marangoni.scala.cli.CLI.loadProperties(propertyFile);
		io.marangoni.scala.cli.CLI.jsonExtract();
		return "JSON elements successfully extracted";
	}

	@CliCommand(value = "json-random")
	public String jsonRandom(@CliOption(key = { "property-file" }, mandatory = false, help = "The property file") final String propertyFile) {
		io.marangoni.scala.cli.CLI.loadProperties(propertyFile);
		io.marangoni.scala.cli.CLI.jsonRandomGenerate();
		return "JSON elements successfully generated";
	}

	@CliCommand(value = "hbase-connect")
	public String hbaseConnect(@CliOption(key = { "property-file" }, mandatory = false, help = "The property file") final String propertyFile) {
		io.marangoni.scala.cli.CLI.loadProperties(propertyFile);
		io.marangoni.scala.cli.CLI.hBaseConnect();
		return "Successfully connected to HBase";
	}

	@CliCommand(value = "spark-jdbc-load-table")
	public String sparkJDBC(@CliOption(key = { "property-file" }, mandatory = false, help = "The property file") final String propertyFile) {
		io.marangoni.scala.cli.CLI.loadProperties(propertyFile);
		io.marangoni.scala.cli.CLI.loadTable();
		return "JDBC table successfully loaded";
	}

	enum MessageType {
		Type1("type1"),
		Type2("type2"),
		Type3("type3");

		private String type;

		private MessageType(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}
	}
}
