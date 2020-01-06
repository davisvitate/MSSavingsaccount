package com.microservice.counth.CountH.services;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.microservice.counth.CountH.model.ClientPerson;
import com.microservice.counth.CountH.model.CountH;
import com.microservice.counth.CountH.model.Firmante;
import com.microservice.counth.CountH.model.Movement;
import com.microservice.counth.CountH.model.Titular;
import com.microservice.counth.CountH.repository.ClientPersonRepository;
import com.microservice.counth.CountH.repository.CountHRepository;
import com.microservice.counth.CountH.repository.FirmanteRepository;
import com.microservice.counth.CountH.repository.MoveRepository;
import com.microservice.counth.CountH.repository.TitularRepository;





import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CountHServiceImp implements CountHServices {
	
	@Autowired
	@Qualifier("Movement")
	WebClient client;
	
	@Autowired
	@Qualifier("Client")
	WebClient client1;
	
	@Autowired
	private CountHRepository countrepositry;
	
	@Autowired
	private ClientPersonRepository clientrerepository;
	
	
	@Autowired 
	private TitularRepository titularrepo;
	
	@Autowired
	private FirmanteRepository firmarepo;
	
	@Autowired
	private MoveRepository moverepo;

	@Override
	public Flux<CountH> findAll() {
		
		return countrepositry.findAll();
	}

	@Override
	public Mono<CountH> findById(String id) {
		
		return countrepositry.findById(id);
	}

	@Override
	public Mono<CountH> save(CountH counth) {
		
		return countrepositry.save(counth);
	}

	@Override
	public Mono<Void> delete(CountH counth) {
		
		return countrepositry.delete(counth);
	}


	@Override
	public Flux<ClientPerson> findAllClientPerson() {
		
		return clientrerepository.findAll();
	}

	@Override
	public Mono<ClientPerson> findClientPersonById(String id) {
	
		return clientrerepository.findByIDA(id);
	}

	@Override
	public Mono<ClientPerson> saveClientPerson(ClientPerson clientperson) {
		
		return clientrerepository.save(clientperson);
	}
	
	@Override
	public Mono<Void> deleteClientPerson(String idClient) {
		// TODO Auto-generated method stub
		return clientrerepository.deleteById(idClient);
	}
	
	@Override
	public Flux<Titular> findAllTitular() {
		// TODO Auto-generated method stub
		return titularrepo.findAll();
	}

	@Override
	public Mono<Titular> findByIdTitular(String id) {
		// TODO Auto-generated method stub
		return titularrepo.findById(id);
	}

	@Override
	public Mono<Titular> saveTitular(Titular titular) {
		// TODO Auto-generated method stub
		return titularrepo.save(titular);
	}

	@Override
	public Mono<Void> deleteTitular(Titular titular) {
		// TODO Auto-generated method stub
		return titularrepo.delete(titular);
	}
//	@Override
//	public Mono<Void> deleteTitularbyId(String titular) {
//		// TODO Auto-generated method stub
//		return titularrepo.deleteById(titular);
//	}

	@Override
	public Flux<Firmante> findAllFirmante() {
		// TODO Auto-generated method stub
		return firmarepo.findAll();
	}

	@Override
	public Mono<Firmante> findByIdFirmante(String id) {
		// TODO Auto-generated method stub
		return firmarepo.findById(id);
	}

	@Override
	public Mono<Firmante> saveFirmante(Firmante firmante) {
		// TODO Auto-generated method stub
		return firmarepo.save(firmante);
	}

	@Override
	public Mono<Void> deleteFirmante(Firmante firmante) {
		// TODO Auto-generated method stub
		return firmarepo.delete(firmante);
	}

	@Override
	public Flux<Movement> findAllMove() {
		// TODO Auto-generated method stub
		return moverepo.findAll();
	}

	@Override
	public Mono<Movement> findByIdMove(String id) {
		// TODO Auto-generated method stub
		return moverepo.findById(id);
	}

	@Override
	public Mono<Movement> saveMove(Movement move) {
		// TODO Auto-generated method stub
		return moverepo.save(move);
	}

	@Override
	public Mono<Void> deleteMove(Movement move) {
		// TODO Auto-generated method stub
		return moverepo.delete(move);
	}

	@Override
	public Flux<ClientPerson> findClientPersonByName(String Name) {
		// TODO Auto-generated method stub
		return clientrerepository.findByNamee(Name);
	}

	@Override
	public Flux<ClientPerson> findClientPersonByDni(String dni) {
		// TODO Auto-generated method stub
		return clientrerepository.findByDni(dni);
	}

	@Override
	public Flux<ClientPerson> findClientPersonByLastname(String lastname) {
		// TODO Auto-generated method stub
		return clientrerepository.findByLastnamee(lastname);
	}

	@SuppressWarnings("deprecation")
	public Mono<Movement> saveMSMovement(Movement movement) {
		return client.post()
				.accept(APPLICATION_JSON_UTF8)
				.contentType(APPLICATION_JSON_UTF8)
				//.body(fromObject(producto))
				.syncBody(movement)
				.retrieve()
				.bodyToMono(Movement.class);
	}
	
	@SuppressWarnings("deprecation")
	public Mono<ClientPerson> saveMSClient(ClientPerson client) {
		return client1.post()
				.accept(APPLICATION_JSON_UTF8)
				.contentType(APPLICATION_JSON_UTF8)
				//.body(fromObject(producto))
				.syncBody(client)
				.retrieve()
				.bodyToMono(ClientPerson.class);
	}

	@Override
	public Flux<CountH> findByDniClient(String dni) {
		// TODO Auto-generated method stub
		return countrepositry.findByDniClient(dni);
	}
	
	public Mono<Map<String, Object>> getMoney(String dni) {
		// TODO Auto-generated method stub
		Map<String, Object> respuesta = new HashMap<String, Object>();

		return countrepositry.findByDniMono(dni).map(c -> {
			respuesta.put("money", c.getMonto());
			return respuesta;
		});
		// return null;
	}

	@Override
	public Mono<CountH> findByDniMono(String dni) {
		// TODO Auto-generated method stub
		return countrepositry.findByDniMono(dni);
	}
	

}
