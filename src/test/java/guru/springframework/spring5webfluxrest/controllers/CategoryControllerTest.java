package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.repository.CategoryRepository;
import org.assertj.core.api.Assertions;
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

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    WebTestClient webTestClient;
    @Mock
    CategoryRepository categoryRepository;
    @InjectMocks
    CategoryController categoryController;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    void listAll() {
        given(categoryRepository.findAll())
                .willReturn(Flux.just(Category.builder().description("Cat1").build(),
                        Category.builder().description("Cat2").build()));
        webTestClient.get()
                .uri(CategoryController.BASE_URL)
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void getById() {
        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().description("Cat1").build()));

        webTestClient.get()
                .uri(CategoryController.BASE_URL + "/{id}","12345566")
                .exchange()
                .expectBody(Category.class)
                .consumeWith( response -> Assertions.assertThat(response.getResponseBody()).isNotNull() );
    }

    @Test
    void create() {
        given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().build()));
        Mono<Category> catToSaveMono = Mono.just(Category.builder().description("Some test").build());

        webTestClient.post()
                .uri(CategoryController.BASE_URL)
                .body(catToSaveMono, Category.class)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void update() {
        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToSaveMono = Mono.just(Category.builder().description("Some test").build());
        webTestClient.put()
                .uri(CategoryController.BASE_URL + "/{id}", "122344")
                .body(catToSaveMono, Category.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void patchWithChanges() {
        given(categoryRepository.findById(anyString())).willReturn(Mono.just(Category.builder().build()));
        given(categoryRepository.save(any(Category.class))).willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catForUpdate = Mono.just(Category.builder().description("Some description").build());

        webTestClient.patch()
                .uri(CategoryController.BASE_URL + "/{id}", "54321")
                .body(catForUpdate, Category.class)
                .exchange()
                .expectStatus().isOk();
        verify(categoryRepository,times(1)).save(any(Category.class));
    }
}