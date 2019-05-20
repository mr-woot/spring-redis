//package com.paisabazaar.kafka_producer.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.logging.Filter;
//
///**
// * Contributed By: Tushar Mudgal
// * On: 13/5/19
// */
//@Configuration
//public class Filter {
//    //    @Bean
////    public CommonsRequestLoggingFilter logFilter() {
////        CommonsRequestLoggingFilter filter
////                = new CommonsRequestLoggingFilter();
//////        filter.setIncludeQueryString(true);
//////        filter.setIncludePayload(true);
//////        filter.setMaxPayloadLength(10000);
//////        filter.setIncludeHeaders(false);
////        filter.setAfterMessagePrefix("REQUEST DATA : ");
////        return filter;
////    }
//    @Bean(name = "TeeFilter")
//    public Filter teeFilter() {
//        return new ch.qos.logback.access.servlet.TeeFilter();
//    }
//}
