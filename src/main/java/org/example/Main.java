package org.example;
import org.example.entities.ServicoModel.Denuncia;
import org.example.entities.ServicoModel.Feedback;
import org.example.entities.UsuarioModel.Denunciante;
import org.example.infrastructure.CorsFilter;
import org.example.repositories.DenunciantesRepository;
import org.example.repositories.Starter;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;


import java.io.IOException;
import java.net.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in org.example package
        final ResourceConfig rc = new ResourceConfig().packages("org.example");
        rc.register(CorsFilter.class);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */


    public static void main(String[] args) throws IOException {

//
//        double lat = -23.4784604;
//        double lon = -46.7061884;
//        var address = OpenStreetMapUtils.getInstance().getEndereco(lat, lon);
//        System.out.println(address);
//        var city = OpenStreetMapUtils.getInstance().getCidade(lat, lon);
//        var state = OpenStreetMapUtils.getInstance().getEstado(lat, lon);
//        var bairro = OpenStreetMapUtils.getInstance().getBairro(lat, lon);
//        var cep = OpenStreetMapUtils.getInstance().getCep(lat, lon);
//
//
//        System.out.println(state);
//        System.out.println(city);
//        System.out.println(bairro);
//        System.out.println(cep);
//        System.out.println("Address: " + address);
        new Starter().initialize();
        var feedback = new Feedback("recebido", "estamos analisando seus dados", LocalDate.now());
        var denunciaf = new Denuncia("lixo despejado", LocalDate.now(), "-23.4784604,-46.7061884",
                "descarte indevido de lixo", null, null,
                null, feedback);

        var denuncia = new Denuncia("muito petroleo visto na area", LocalDate.now(),"-23.4784604,-46.7061884",
                "derramamento de residuos", "empresa folks", "um ano",
                "urgencia");

        var denunciante = new Denunciante("julia", "5556797@fiap.com.br","11985632147", new ArrayList<>(List.of(denuncia, denunciaf)));

        var denuncianteRepo = new DenunciantesRepository();
//
        denuncianteRepo.create(denunciante);
        denuncianteRepo.readAllTeste();
//        System.out.println(denuncianteRepo.getIdDenunciante(denunciante));

//        denuncianteRepo.getIdDenuncia(denunciante);








        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with endpoints available at "
                + "%s%nHit Ctrl-C to stop it...", BASE_URI));
        System.in.read();
        server.stop();
    }
}

