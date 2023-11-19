package com.foodapp;

import com.foodapp.controller.PaymentController;
import com.foodapp.framework.controller.ControllerProcessor;
import com.foodapp.framework.webserver.WebServer;
import com.foodapp.registry.RegistryClient;
import com.foodapp.service.*;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

public class Application {
    private static final Logger logger = Logger.getLogger(Application.class.getName());
    private static final String serviceName = "PAYMENT_SERVICE";
    private static Application instance;

    private WebServer webServer;
    private ControllerProcessor controllerProcessor;

    private String serviceRegistryURL = "http://localhost:8081";
    private RegistryClient registryClient;
    private InetSocketAddress address;

    private String mongoDBURL = "mongodb+srv://kiyer:E6eTIOtcUV6RovQY@cluster0.mabavh3.mongodb.net/?retryWrites=true&w=majority";
    private Datastore mongoDatastore = Morphia.createDatastore(MongoClients.create(mongoDBURL), "paymentservice");

    private PaymentController paymentController;
    private PaymentService paymentService;
    private PaymentDataAdapter paymentDataAdapter;

    public static void initApp(String[] args) throws SQLException, IOException {
        if (Objects.isNull(instance)) {
            instance = new Application(args);
        }
    }

    public static Application getInstance() {
        return instance;
    }

    private Application(String[] args) throws IOException, SQLException {
        Map<String, String> argsKeyVal = parseArgs(args);
        Integer port = Optional.ofNullable(argsKeyVal.get("port")).map(Integer::parseInt).orElse(null);
        webServer = new WebServer(port);
        address = webServer.getAddress();

        registryClient = new RegistryClient(serviceRegistryURL, serviceName, address.toString());

        paymentDataAdapter = new PaymentDataAdapter(mongoDatastore);
        paymentService = new PaymentService(paymentDataAdapter);

        controllerProcessor = new ControllerProcessor(webServer);
        paymentController = new PaymentController(paymentService);
        controllerProcessor.process(paymentController);
    }

    public void init() {
        webServer.start();
        logger.info("Started application on port: " + webServer.getPort());
    }

    private static Map<String, String> parseArgs(String[] args) {
        if (args.length % 2 != 0)
            throw new RuntimeException("Unable to pass args. All args should be key value pairs");
        Map<String, String> argKeyVal = new HashMap<>();
        for (int i = 0; i < args.length; i+=2) {
            String key = args[i];
            if (!key.startsWith("--"))
                throw new RuntimeException("Unable to recognize argument: " + key + "\nArgument key should start with --\nEg: --port");
            key = key.replace("--", "");
            String val = args[i+1];
            argKeyVal.put(key, val);
        }
        return argKeyVal;
    }
}
