package com.project3.storemanagement.controller;

import com.project3.storemanagement.dto.ProductDto;
import com.project3.storemanagement.dto.ProductResponseDto;
import com.project3.storemanagement.dto.ProductVariantDto;
import com.project3.storemanagement.dto.VariantDto;
import com.project3.storemanagement.entities.Product;
import com.project3.storemanagement.entities.Variant;
import com.project3.storemanagement.service.ProductService;
import com.project3.storemanagement.service.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private VariantService variantService;

    @GetMapping("")
    public Iterable<ProductResponseDto> listAllProducts() { return productService.listAllProducts(); }

    @GetMapping("/{id}/variants")
    public List<Variant> listAllVariantsByProductId(@PathVariable long id){
        return variantService.listAllVariantsByProductId(id);
    }

    @GetMapping("/{id}")
    ProductResponseDto getProductById(@PathVariable(name = "id") long id) { return productService.getProductById(id); }

    @PostMapping
    Product saveProduct(@RequestBody @Valid ProductVariantDto newProduct) {
        return productService.saveProduct(newProduct);
    }

    @PostMapping("/{id}/create-variant")
    Variant saveVariant(@PathVariable(name = "id") long id, @RequestBody @Valid VariantDto variantDto) {
        return productService.saveVariant(id, variantDto);
    }

    @PutMapping("/{id}")
    Product updateProduct(@PathVariable(name = "id") long id, @RequestBody @Valid ProductDto updateProduct) {
        return productService.updateProduct(id, updateProduct);
    }

    @DeleteMapping("/{id}")
    Product deleteProduct(@PathVariable(name = "id") long id) {
        return productService.deleteProduct(id);
    }

}
