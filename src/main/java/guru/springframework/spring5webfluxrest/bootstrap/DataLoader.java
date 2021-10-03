package guru.springframework.spring5webfluxrest.bootstrap;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repository.CategoryRepository;
import guru.springframework.spring5webfluxrest.repository.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public DataLoader(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count().block() == 0L) {
            categoryRepository.save(Category.builder()
                    .description("Category1")
                    .build()).block();
            categoryRepository.save(Category.builder()
                    .description("Category2")
                    .build()).block();
            categoryRepository.save(Category.builder()
                    .description("Category3")
                    .build()).block();
        }
        if (vendorRepository.count().block() == 0L) {
            vendorRepository.save(Vendor.builder()
                    .firstName("Joe")
                    .lastName("Doe")
                    .build()).block();
            vendorRepository.save(Vendor.builder()
                    .firstName("Jane")
                    .lastName("Doe")
                    .build()).block();
            vendorRepository.save(Vendor.builder()
                    .firstName("Lucky")
                    .lastName("Luke")
                    .build()).block();
        }
    }
}
