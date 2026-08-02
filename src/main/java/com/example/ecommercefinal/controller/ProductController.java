package com.example.ecommercefinal.controller;

import com.example.ecommercefinal.dto.PageResponse;
import com.example.ecommercefinal.dto.ProductResponse;
import com.example.ecommercefinal.entity.Product;
import com.example.ecommercefinal.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@Tag(
        name = "Product Management",
        description = "APIs for managing products"
)
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService){
        this.productService=productService;
    }


    private static final Logger logger= LoggerFactory.getLogger(ProductController.class);

    @Operation(
            summary = "Create a new product",
            description = "Creates a new product in the catalog"
    )

    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Product created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    })

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product){
        logger.info("Received request to create product");
        Product savedProduct=productService.createProduct(product);
        return new ResponseEntity<>(savedProduct,HttpStatus.CREATED);
    }
    @Operation(
            summary = "Get all products",
            description = "Returns all available products"
    )

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Products retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    })

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("")
    public ResponseEntity<PageResponse<Product>> getAllProducts(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size,@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction){
        return new ResponseEntity<>(productService.getAllProducts(page, size,sortBy,direction),HttpStatus.OK);
    }

    @Operation(
            summary = "Get product by ID",
            description = "Retrieve a single product using its ID"
    )

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Product found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found"
            )
    })

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@Parameter(description = "Unique product ID", example = "1") @PathVariable int id) throws Exception {
        return new ResponseEntity<>(productService.getProductById(id),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
        boolean is_deleted= productService.deleteProduct(id);
        if(is_deleted) return new ResponseEntity("Product deleted successfully",HttpStatus.OK);
        else return new ResponseEntity("Product not found",HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @Valid @RequestBody Product product){
        Product updatedProduct=productService.updateProduct(id,product);
        if(updatedProduct!=null){
            return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
        }
        else return new ResponseEntity<>(updatedProduct,HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Product>> getProductsByBrand(@PathVariable String brand){
        List<Product> products=productService.getProductsByBrand(brand);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }

    @GetMapping("/search/{brand}")
    public ResponseEntity<List<Product>> searchProducts(@PathVariable String keyword){
        List<Product> products=productService.searchProducts(keyword);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }

    @GetMapping("/price")
    public ResponseEntity<List<Product>> getProductsByPriceRange(@RequestParam Double minPrice, @RequestParam Double maxPrice){
        List<Product> products=productService.getProductsByPriceRange(minPrice,maxPrice);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<Product>> searchProducts(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue ="5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc")String direction) {
        PageResponse<Product> products =
                productService.searchProducts(brand, name, minPrice, maxPrice, page, size, sortBy, direction);

        return ResponseEntity.ok(products);
    }

}
