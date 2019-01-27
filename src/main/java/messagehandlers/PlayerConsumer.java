package messagehandlers;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.pmw.tinylog.Logger;

/**
 * Sample event consumer.
 *
 * @author  JVD
 */
public class PlayerConsumer {


    public <T> void handler(Message<T> message) {
        final JsonObject json = (JsonObject) message.body();

        Logger.info("[EVENTBUS] (Player) received from JSON", json.getString("content"));
        //message.reply("Player message well received");
    }
}
