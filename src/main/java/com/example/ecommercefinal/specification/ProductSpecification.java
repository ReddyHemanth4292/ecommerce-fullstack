package com.example.ecommercefinal.specification;

import com.example.ecommercefinal.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    private ProductSpecification() {
    }

    public static Specification<Product> hasBrand(String brand) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("brand"), brand);

    }
    public static Specification<Product> nameContains(String keyword) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + keyword.toLowerCase() + "%"
                );

    }

    public static Specification<Product> priceGreaterThanOrEqualTo(Double minPrice) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("price"),
                        minPrice
                );

    }

    public static Specification<Product> priceLessThanOrEqualTo(Double maxPrice) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("price"),
                        maxPrice
                );

    }
}
