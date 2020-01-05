package com.microservice.counth.CountH.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.microservice.counth.CountH.model.CountH;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CountHRepository extends ReactiveMongoRepository<CountH, String> {
	
	@Query("{ 'num': ?0 }")
    Flux<CountH> findByNumCount(final String num);
	
	@Query("{ 'clientperson.dni': ?0 }")
    Flux<CountH> findByDniClient(final String dni);
	
	@Query("{ 'clientperson.dni': ?0 }")
    Mono<CountH> findByDniMono(final String dni);

}
