package tech.meliora.mulika.apache.camel.mulika.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.meliora.mulika.apache.camel.mulika.MulikaConnector;

@Component
public class MulikaPostProcessor implements Processor {

    @Autowired
    MulikaConnector mulikaConnector;
    
    @Override
    public void process(Exchange exchange) throws Exception {

        long start = exchange.getProperty("mulika.requestTime", Long.class);

        long end =  System.currentTimeMillis();
        long processingTime = end - start;

        boolean successful = true;
        String serviceName = (String) exchange.getMessage().getHeader("CamelHttpUri");

        exchange.setProperty("mulika.processingTime", processingTime);
        exchange.setProperty("mulika.endTime", end);
        exchange.setProperty("mulika.successful", successful);
        exchange.setProperty("mulika.serviceName", serviceName);

        mulikaConnector.report(serviceName, successful, (int)processingTime, 0);

    }
}
