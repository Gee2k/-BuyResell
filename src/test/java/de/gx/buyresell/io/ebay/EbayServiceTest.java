package de.gx.buyresell.io.ebay;

import de.gx.buyresell.db.service.DBService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class EbayServiceTest {

    @InjectMocks
    EbayService ebayService;// = new EbayService();

    @Mock
    EbayUrlScraper ebayUrlScraper;

    @Mock
    DBService dbService;

    CircuitBreaker circuitBreaker;

    @BeforeEach
    void setUp() {
        circuitBreaker = CircuitBreaker.ofDefaults("Test_CircuitBreaker");
        ebayService.setFinishedEbayUrl("test");
//        ebayService.setCircuitBreaker(circuitBreaker);
        ebayService.setUrlScraper(ebayUrlScraper);
    }

    @Test
    void scrapeSoldComputerAuctions() throws Exception{

        //1. prerequisites
        int totalServiceCalls = 1000;
        int neededCalls = totalServiceCalls/3;  //minimum calls circuitBreaker needs before interrupting

        //circuitBreaker config
        CircuitBreakerConfig cbc = CircuitBreakerConfig.custom()
                .minimumNumberOfCalls(neededCalls)
                .slidingWindowSize(neededCalls)
                .build();
        CircuitBreakerRegistry cbr = CircuitBreakerRegistry.of(cbc);
        CircuitBreaker cb = cbr.circuitBreaker("test_cb");
        ebayService.setCircuitBreaker(cb);  //override default/global config in service

        //make service return only exceptions / failures
        Mockito.when(ebayUrlScraper.scrapeElementsFromUrl(Mockito.any())).thenThrow(new RuntimeException());

        //additional measures
        int callsPutTrough = 0;
        int cutCalls = 0;
        String exceptionType = "";

        //2. call Service
        for (int i = 0; i < totalServiceCalls; i++) {
            try {
                ebayService.scrapeSoldComputerAuctions();
            }
            catch (CallNotPermittedException cnpe){
                cutCalls++;
                exceptionType = cnpe.getClass().toString();
            }
            catch(RuntimeException e){
                callsPutTrough++;
            }
        }

        //3. check
        //check that circuitBreaker cut calls to service after needed failing calls are invoked
        Mockito.verify(ebayUrlScraper, Mockito.times(neededCalls)).scrapeElementsFromUrl(Mockito.any());

        //call amount check
        Assert.assertEquals("Permitted calls mismatch", neededCalls, callsPutTrough);
        Assert.assertEquals("Revoked calls mismatch", totalServiceCalls-neededCalls, cutCalls);
        //after circuitBreaker interrupt, specific exception should be thrown
        Assert.assertEquals("CircuitBreaker Exception not thrown", CallNotPermittedException.class.toString(), exceptionType);
    }
}