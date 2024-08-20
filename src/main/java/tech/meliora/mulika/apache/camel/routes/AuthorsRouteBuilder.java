package tech.meliora.mulika.apache.camel.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.meliora.mulika.apache.camel.mulika.processors.MulikaExceptionProcessor;
import tech.meliora.mulika.apache.camel.mulika.processors.MulikaPostProcessor;
import tech.meliora.mulika.apache.camel.mulika.processors.MulikaPreProcessor;

@Component
public class AuthorsRouteBuilder extends RouteBuilder {


    @Autowired
    MulikaPreProcessor mulikaPreProcessor;

    @Autowired
    MulikaPostProcessor mulikaPostProcessor;

    @Autowired
    MulikaExceptionProcessor mulikaExceptionProcessor;

    @Override
    public void configure() throws Exception {

        onException(Exception.class)
                .process(mulikaExceptionProcessor)
                .handled(true);

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.auto);

        rest()
                .path("/authors")
                .post()
                .outType(String.class)
                .to("log:mylogger?showAll=true")
                .to("direct:call-rest-all");

        from("direct:call-rest-all")
                .to("log:DEBUG?showBody=true&showHeaders=true")
                .routeId("all-service")
                .removeHeader("CamelHttp*")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .process(mulikaPreProcessor)
                .to("http://51.15.211.168/api/authors.php?bridgeEndpoint=true&throwExceptionOnFailure=true")
                .to("log:DEBUG?showBody=true&showHeaders=true")
                .unmarshal().json()
                .process(mulikaPostProcessor);

    }
}
