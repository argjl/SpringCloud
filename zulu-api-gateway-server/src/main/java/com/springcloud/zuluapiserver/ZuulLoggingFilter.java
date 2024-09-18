package com.springcloud.zuluapiserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.result.view.RequestContext;
import org.springframework.web.server.ServerWebExchange;

import io.micrometer.core.instrument.binder.http.HttpServletRequestTagsProvider;
import io.micrometer.core.ipc.http.HttpSender.Request;
import reactor.core.publisher.Mono;

@Configuration
public class ZuulLoggingFilter implements GlobalFilter, Ordered {

	Logger logger=LoggerFactory.getLogger(ZuulLoggingFilter.class);
	
	@Override
	public int getOrder() {
		return -1;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange,
			org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
		System.out.println("Global Pre Filter executed");
		String request=exchange.getRequest().getURI().toString();
		logger.info("Incoming request URI: {}", request);
		return chain.filter(exchange).then(Mono.fromRunnable(() -> {
			System.out.println("Global Post Filter executed");
		}));
	}

}