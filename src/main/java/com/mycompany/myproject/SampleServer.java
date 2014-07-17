package com.mycompany.myproject;

import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;

/**
 * @author ishimura shinichi
 */
public class SampleServer extends Verticle {
	@Override
	public void start() {
		HttpServer httpServer = vertx.createHttpServer();
		Logger logger = container.logger();

		httpServer.requestHandler(request -> {
			switch (request.path()) {
				case "/ping":
					logger.info("ping????????");
					vertx.eventBus().send("ping-address", "dummy", (Message<String> handler) -> {
						request.response().end(handler.body());
					});
			}
		});
		httpServer.listen(8080);
		logger.info("listen");
		container.deployVerticle("com.mycompany.myproject.PingVerticle");
		logger.info("deploy verticle");
	}
}
