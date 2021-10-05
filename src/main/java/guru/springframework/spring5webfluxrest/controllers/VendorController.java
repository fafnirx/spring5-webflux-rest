package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repository.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(VendorController.BASE_URL)
public class VendorController {
    public final static String BASE_URL="/api/v1/vendors";
    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping
    Flux<Vendor> listAll() {
        return vendorRepository.findAll();
    }

    @GetMapping("/{id}")
    Mono<Vendor> getById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    Mono<Void> create(@RequestBody Publisher<Vendor> vendorPublisher) {
        return vendorRepository.saveAll(vendorPublisher).then();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    Mono<Vendor> update(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    Mono<Vendor> patch(@PathVariable String id, @RequestBody Vendor vendor) {
        Vendor storedVendor = vendorRepository.findById(id).block();
        boolean updated=false;
        if (vendor.getFirstName() != null) {
            storedVendor.setFirstName(vendor.getFirstName());
            updated=true;
        }
        if (vendor.getLastName() != null) {
            storedVendor.setLastName(vendor.getLastName());
            updated=true;
        }
        if (updated) {
            return vendorRepository.save(storedVendor);
        }
        return Mono.just(storedVendor);
    }



}
