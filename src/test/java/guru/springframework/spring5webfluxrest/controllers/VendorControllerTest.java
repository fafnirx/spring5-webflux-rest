package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repository.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendorControllerTest {
    WebTestClient webTestClient;

    @Mock
    VendorRepository vendorRepository;

    @InjectMocks
    VendorController vendorController;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void listAll() {
        given(vendorRepository.findAll()).willReturn(
                Flux.just(Vendor.builder().build(), Vendor.builder().build()));

        webTestClient.get()
                .uri(VendorController.BASE_URL)
                .exchange()
                .expectBodyList(Vendor.class).hasSize(2);
    }

    @Test
    void getById() {
        given(vendorRepository.findById(anyString())).willReturn(Mono.just(Vendor.builder().build()));

        webTestClient.get().uri(VendorController.BASE_URL + "/{id}", "123456")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Vendor.class);
    }

    @Test
    void create() {
        given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));
        Mono<Vendor> savedVendorMono = Mono.just(Vendor.builder().build());

        webTestClient.post()
                .uri(VendorController.BASE_URL)
                .body(savedVendorMono, Vendor.class)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void update() {
        given(vendorRepository.save(any())).willReturn(Mono.just(Vendor.builder().build()));
        Mono<Vendor> savedVendorMono = Mono.just(Vendor.builder().build());

        webTestClient.put()
                .uri(VendorController.BASE_URL + "/{id}", "12345")
                .body(savedVendorMono, Vendor.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void patchWithUpdate() {
        given(vendorRepository.findById(anyString())).willReturn(Mono.just(Vendor.builder().build()));
        given(vendorRepository.save(any())).willReturn(Mono.just(Vendor.builder().build()));
        Mono<Vendor> savedVendorMono = Mono.just(Vendor.builder().firstName("Rob").build());

        webTestClient.patch()
                .uri(VendorController.BASE_URL + "/{id}", "12345")
                .body(savedVendorMono, Vendor.class)
                .exchange()
                .expectStatus().isOk();

        verify(vendorRepository, times(1)).save(any(Vendor.class));
    }

    @Test
    void patchWithOutUpdate() {
        given(vendorRepository.findById(anyString())).willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> savedVendorMono = Mono.just(Vendor.builder().build());

        webTestClient.patch()
                .uri(VendorController.BASE_URL + "/{id}", "12345")
                .body(savedVendorMono, Vendor.class)
                .exchange()
                .expectStatus().isOk();
        verify(vendorRepository, never()).save(any(Vendor.class));
    }
}