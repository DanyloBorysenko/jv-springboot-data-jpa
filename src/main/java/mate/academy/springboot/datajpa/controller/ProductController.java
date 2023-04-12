package mate.academy.springboot.datajpa.controller;

import mate.academy.springboot.datajpa.dto.product.ProductRequestDto;
import mate.academy.springboot.datajpa.dto.product.ProductResponseDto;
import mate.academy.springboot.datajpa.maper.DtoMapper;
import mate.academy.springboot.datajpa.model.Product;
import mate.academy.springboot.datajpa.service.CategoryService;
import mate.academy.springboot.datajpa.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final DtoMapper<Product, ProductRequestDto, ProductResponseDto> mapper;

    public ProductController(ProductService productService, CategoryService categoryService, DtoMapper<Product, ProductRequestDto, ProductResponseDto> mapper) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ProductResponseDto add(@RequestBody ProductRequestDto productRequestDto) {
        return mapper.toDto(productService.create(mapper.toModel(productRequestDto)));
    }

    @GetMapping("/{id}")
    ProductResponseDto getById(@PathVariable Long id) {
        return mapper.toDto(productService.getById(id));
    }

    @PutMapping("/{id}")
    ProductResponseDto update(@PathVariable Long id,
                              @RequestBody ProductRequestDto productRequestDto) {
        Product product = mapper.toModel(productRequestDto);
        product.setId(id);
        return mapper.toDto(productService.update(product));
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    @GetMapping("/by-price")
    List<ProductResponseDto> findAllByPriceBetween(@RequestParam BigDecimal price_from,
                                                   @RequestParam BigDecimal price_to) {
        List<Product> products = productService.findAllByPriceBetween(price_from, price_to);
        return products.stream().map(mapper::toDto).toList();
    }

    @GetMapping("/by-category/{ids}")
    List<ProductResponseDto> findAllByCategoryIds(@PathVariable List<Long> ids) {
        List<Product> products = productService.findAllByCategoryIds(ids);
        return products.stream().map(mapper::toDto).toList();
    }
}
