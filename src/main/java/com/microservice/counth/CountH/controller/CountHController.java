package com.microservice.counth.CountH.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.net.URI;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.microservice.counth.CountH.model.ClientPerson;
import com.microservice.counth.CountH.model.CountH;
import com.microservice.counth.CountH.services.CountHServiceImp;
import com.microservice.counth.CountH.services.CountHServices;

import ch.qos.logback.core.util.ContentTypeUtil;
import lombok.Value;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/counth")
public class CountHController {
	private static Logger log = LoggerFactory.getLogger(CountHController.class);

	@Autowired
	private CountHServices service;

	@Autowired
	private CountHServiceImp serviceclient;

	@GetMapping
	public Mono<ResponseEntity<Flux<CountH>>> lista() {
		return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(service.findAll()));
	}

	// see the list of savings accounts for id
	@GetMapping("/{id}")
	public Mono<ResponseEntity<CountH>> ver(@PathVariable String id) {
		return service.findById(id).map(p -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(p))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

//	@PostMapping
//	public Mono<CountH> create(@RequestBody CountH monoCounth){
//		String dni =monoCounth.getClientperson().getDni();
//		Mono<CountH> mono = null;
//		if( service.findClientPersonByDni(dni)!= null) {
//			 mono= service.save(monoCounth);			
//		}
//		return mono;
//			 
//									
//	}
	// creation of a savings account
	@PostMapping
	public Mono<CountH> create(@RequestBody CountH monoCounth) {
		ClientPerson client = new ClientPerson();
		client = monoCounth.getClientperson();
		client.setType("personal client");
		serviceclient.saveMSClient(client).subscribe();
		service.saveClientPerson(client).subscribe();
		return service.save(monoCounth);

	}

//	@PostMapping
//	public Mono<ResponseEntity<Map<String, Object>>> crear(@RequestBody Mono<CountH> monoProducto){
//		
//		Map<String, Object> respuesta = new HashMap<String, Object>();
//		
//		return monoProducto.flatMap(counth->{
//			return service.save(counth).map(c->{
//				respuesta.put("saving account", c);
//				respuesta.put("mensaje", "cuenta de ahoro creada con exito");
//				return ResponseEntity
//						.created(URI.create("/api/counth/".concat(c.getId())))
//						.contentType(MediaType.APPLICATION_JSON_UTF8)
//						.body(respuesta);											
//			});
//		}).onErrorResume(t -> {
//			return Mono.just(t).cast(WebExchangeBindException.class)
//					.flatMap(e -> Mono.just(e.getFieldErrors()))
//					.flatMapMany(Flux::fromIterable)
//					.map(fieldError -> "El campo "+fieldError.getField() + " " + fieldError.getDefaultMessage())
//					.collectList()
//					.flatMap(list -> {
//						respuesta.put("errors", list);
//						respuesta.put("status", HttpStatus.BAD_REQUEST.value());
//						return Mono.just(ResponseEntity.badRequest().body(respuesta));
//					});						
//		});
//	}

//	public Mono<> create(@RequestBody Mono<CountH> monoProducto){
//		

	// edit the savings account
	@PutMapping("/{id}")
	public Mono<ResponseEntity<CountH>> upadate(@RequestBody CountH counth, @PathVariable String id) {
		return service.findById(id).flatMap(c -> {
			c.setNum(counth.getNum());
			c.setMonto(counth.getMonto());
			c.setClientperson(counth.getClientperson());
			return service.save(c);
		}).map(c -> ResponseEntity.created(URI.create("/counth/".concat(c.getId())))
				.contentType(MediaType.APPLICATION_JSON_UTF8).body(c))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	// delete the savings account
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
		return service.findById(id).flatMap(p -> {
			return service.delete(p).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
		}).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}
	
	@GetMapping("/dni/{dni}")
	public Flux<CountH> getClientDni(@PathVariable String dni) {
		
		return service.findByDniClient(dni);

	}
	
	@GetMapping("/getmoney/{dni}")
	public Mono<Map<String, Object>>  getMoney(@PathVariable String dni) {
		return service.getMoney(dni);
	}

}
