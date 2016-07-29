package org.pfry.route;

import org.apache.camel.builder.RouteBuilder;

public class CassandraRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
        from("cql:localhost/camel?cql=SELECT * FROM emp")
       .log("body is ${body}");

	}

}
