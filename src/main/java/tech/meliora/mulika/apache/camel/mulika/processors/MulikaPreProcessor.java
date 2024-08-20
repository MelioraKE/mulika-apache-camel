package tech.meliora.mulika.apache.camel.mulika.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class MulikaPreProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.setProperty("mulika.requestTime", System.currentTimeMillis());
    }
}
