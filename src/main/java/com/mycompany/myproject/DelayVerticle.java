package com.mycompany.myproject;

import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;

import java.util.concurrent.TimeUnit;

/**
 * @author SHINICHI Ishimura
 */
public class DelayVerticle extends Verticle {
    @Override
    public void start() {
        Logger logger = container.logger();
        vertx.eventBus().registerHandler("delay", (Message message) -> {
            logger.info("waiting....");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            message.reply(true);
        });
        logger.info("DelayVerticle started");
    }
}
