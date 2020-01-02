package com.microservice.counth.CountH.controller;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.microservice.counth.CountH.model.CountH;
import com.microservice.counth.CountH.model.Movement;
import com.microservice.counth.CountH.services.CountHServiceImp;
import com.microservice.counth.CountH.services.CountHServices;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/counth")
public class MovementController {
	
	@Autowired
	private CountHServices service;
	
	@Autowired
	private CountHServiceImp serviceclient;
	
	@GetMapping("/movement")
	public Mono<ResponseEntity<Flux<Movement>>> lista(){
		return Mono.just(
				ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(service.findAllMove())
				);
	}
	
	
	//withdrawal is done
	@PutMapping("/retire/{id}")
	public Mono<ResponseEntity<CountH>> upadateretire(@RequestBody CountH counth, @PathVariable String id){
		
		Movement mov= new Movement();
		
		return service.findById(id).flatMap(c -> {
			double montoantes=c.getMonto();
			if(montoantes>=counth.getMonto()) {
			c.setMonto(montoantes-counth.getMonto());
			mov.setNum_count(counth.getNum());
			mov.setDescription("Retire");
			mov.setSaldo(counth.getMonto());
			mov.setDate(new Date());
			mov.setClient(counth.getClientperson());
			service.saveMove(mov).subscribe();// registre of the movement
			
			serviceclient.saveMSMovement(mov).subscribe();// registre of the movement on the microservice
			}
			return service.save(c);
		}).map(c->ResponseEntity.created(URI.create("/counth/retire/".concat(c.getId())))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(c))
		.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	//deposit is made
	@PutMapping("/deposite/{id}")
	public Mono<ResponseEntity<CountH>> upadeposit(@RequestBody CountH counth, @PathVariable String id){
		Movement mov= new Movement();
		return service.findById(id).flatMap(c -> {
			double montoantes= c.getMonto();
			c.setMonto(montoantes + counth.getMonto());
			c.setClientperson(counth.getClientperson());
			mov.setNum_count(counth.getNum());
			mov.setDescription("Deposite");
			mov.setSaldo(counth.getMonto());
			mov.setDate(new Date());
			mov.setClient(counth.getClientperson());
			service.saveMove(mov).subscribe();// deposite of the mevement
			return service.save(c);
		}).map(c->ResponseEntity.created(URI.create("/counth/deposite/".concat(c.getId())))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(c))
		.defaultIfEmpty(ResponseEntity.notFound().build());
	}

}
