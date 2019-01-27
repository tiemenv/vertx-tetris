package webapi;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import org.pmw.tinylog.Logger;
import data.H2Connection;
import data.TetrisRepository;
import messagehandlers.PlayerConsumer;

/**
 * Main body of the application. Initialise all other components here.
 *
 * @author JVD
 */

public class WebServer extends AbstractVerticle {
    // JVD: start() function wordt automatisch aangeroepen bij het deployen van een verticle
    @Override
    public void start() {
        // JVD: start webservers
        this.initWebserver();

        // JVD: start database
        this.initDB();

        // JVD: Start event bus
        this.initEventBus();
    }

    private void initWebserver() {
        Logger.info("Deploying Verticle Tetris Webserver");
        final HttpServer server = vertx.createHttpServer();
        final Router router = Router.router(vertx);

        // JVD: afhandelen als mensen naar de root surfen
        router.route("/").handler(routingContext -> {
            // JVD: je hebt een response object nodig om "iets" terug te sturen naar de client die connecteert
            final HttpServerResponse response = routingContext.response();
            response.setChunked(true);
            response.write("Hands off the root");
            response.end();
        });

        //JVD: Handle static files -> zorgt ervoor dat je kan surfen naar localhost:eenpoort/ en dat
        // [RULE]: Statuc url MOET "/static/*" zijn
        router.route("/static/*").handler(StaticHandler.create());

        // JVD: socket server opzetten tetris.events.anything waarvan minstens 1 char
        // [RULE]: Socket url MOET tetris-xx/socket/ zijn (xx is groepsnummer met leading zero) zijn
        router.route("/tetris-15/socket/*").handler(new TetrisSockJSHandler(vertx).create());

        //JVD: effectief opstarten van de server
        // [RULE]: poort MOET 80xx (xx is groepsnummer met leading zero) zijn
        server.requestHandler(router::accept).listen(8015);
    }

    private void initDB() {
        new H2Connection().startDBServer();
        TetrisRepository.populateDB();
    }

    private void initEventBus() {

        final EventBus eb = vertx.eventBus();
        Logger.info("[EVENTBUS] Listening to messages");
        //JVD: Dit stuk is illustratief, optioneel om jullie op weg te helpen.
        // Uiteraard kan je dit uit elkaar trekken in verschillende klasses
        final PlayerConsumer playerConsumer = new PlayerConsumer();


        //JVD: kritieke systemen in een klasse abstrageren
        eb.consumer("brickingbad/events/player", playerConsumer::handler);

        //JVD: Niet kritieke zaken met een lambda functie aghandelen

        eb.consumer("brickingbad/events/info", message -> {
            eb.publish("brickingbad/events/gameinfo", "The game is running");
            message.reply("OK");
            Logger.info("[EVENTBUS] (Info) message received in info ", message.body());

        });

    }


}
