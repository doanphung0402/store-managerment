package com.project3.storemanagement.service;

import com.project3.storemanagement.entities.Product;
import com.project3.storemanagement.entities.Variant;
import com.project3.storemanagement.dto.ProductDto;
import com.project3.storemanagement.dto.ProductResponseDto;
import com.project3.storemanagement.dto.ProductVariantDto;
import com.project3.storemanagement.dto.VariantDto;

public interface ProductService {
    Iterable<ProductResponseDto> listAllProducts();

    ProductResponseDto getProductById(Long id);

    Product saveProduct(ProductVariantDto productVariantDto);

    Product updateProduct(long id, ProductDto productDto);

    Product deleteProduct(Long id);

    Variant saveVariant(long id, VariantDto variantDto);
}
