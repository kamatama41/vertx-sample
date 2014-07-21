package com.mycompany.myproject;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;

/**
 * @author SHINICHI Ishimura
 */
public class SampleServer extends Verticle {
	@Override
	public void start() {
		HttpServer httpServer = vertx.createHttpServer();
		Logger logger = container.logger();

		httpServer.requestHandler(request -> {
			switch (request.path()) {
				case "/ping":
					vertx.eventBus().send("ping-address", "dummy", (Message<String> handler) -> {
						request.response().end(handler.body());
					});
                    break;
                case "/delay":
                    final int timeoutMillis = 3500; // Return response
//                    final int timeoutMillis = 2500; // timeout
                    vertx.eventBus().sendWithTimeout("delay", "dummy", timeoutMillis, (AsyncResult<Message<Boolean>> handler) -> {
                        logger.info("succeeded=" + handler.succeeded() + ", failed=" + handler.failed());
                        final String message;
                        if(handler.failed()) {
                            message = "failed";
                        } else if(handler.succeeded()) {
                            message = handler.result().body().toString();
                        }   else {
                            message = "????";
                        }
                        request.response().end(message);
                    });
                    break;
                default:
                    logger.warn("Path(" + request.path() + ") is not found.");
			}
		});
		httpServer.listen(8080);
		logger.info("Listen 8080");
		container.deployVerticle("com.mycompany.myproject.PingVerticle");
		container.deployVerticle("com.mycompany.myproject.DelayVerticle");
		logger.info("Deploy verticles");
	}
}
