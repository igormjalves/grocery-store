package com.grocery_store.payment.service;

import static com.grocery_store.payment.util.ApplicationUtils.convertPriceToPounds;
import com.grocery_store.payment.dto.Product;
import com.grocery_store.payment.dto.ProductRequest;
import com.grocery_store.payment.dto.Promotion;
import com.grocery_store.payment.exception.ResourceNotFoundException;
import com.grocery_store.payment.model.Cart;
import com.grocery_store.payment.model.CartItem;
import com.grocery_store.payment.model.CartStatus;
import com.grocery_store.payment.model.ProductInfo;
import com.grocery_store.payment.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;

    public CartService(CartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    @Transactional
    public Cart create(@Valid List<ProductRequest> productRequests) {
        Cart cart = new Cart();
        cart.setStatus(CartStatus.ACTIVE);
        List<Product> productList = productRequests
                .stream()
                .map(product -> productService.getProductById(product.getProductId()))
                .collect(Collectors.toUnmodifiableList());
        for (ProductRequest request : productRequests) {
            Product productMatch = productList
                    .stream()
                    .filter(product -> product.id().equals(request.getProductId()))
                    .findFirst().orElse(null);
            addOrRemoveCartProduct(cart, request, productMatch, true);
        }
        calculateCartTotals(cart, productList);
        return cartRepository.save(cart);
    }

    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    public Cart getCartById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + id));
    }

    @Transactional
    public Cart addProductsToCart(Long id, @Valid List<ProductRequest> productRequests, Boolean add) {
        Cart cart = getCartById(id);
        List<Product> productList = getProductList(productRequests, cart.getItems());
        productRequests.forEach(request -> {
            Product productMatch = productList
                    .stream()
                    .filter(product -> product.id().equals(request.getProductId()))
                    .findFirst().orElse(null);
            addOrRemoveCartProduct(cart, request, productMatch, add);
        });
        calculateCartTotals(cart, productList);
        return cartRepository.save(cart);
    }

    private List<Product> getProductList(List<ProductRequest> productRequests, List<CartItem> cartItems) {
        List<Product> productListInCart = cartItems
                .stream()
                .map(item -> productService.getProductById(item.getProductInfo().getProductId()))
                .collect(Collectors.toUnmodifiableList());
        List<Product> productRequestList = productRequests
                .stream()
                .map(product -> productService.getProductById(product.getProductId()))
                .collect(Collectors.toUnmodifiableList());
        return Stream.concat(productRequestList.stream(), productListInCart.stream())
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }

    private void addOrRemoveCartProduct(Cart cart, ProductRequest productRequest, Product product, Boolean add) {
        if(product == null) throw new ResourceNotFoundException("Product not found with id: " + productRequest.getProductId());

        if(cart.getItems() == null) cart.setItems(new ArrayList<>());
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProductInfo().getProductId().equals(productRequest.getProductId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(add ?
                    existingItem.getQuantity() + productRequest.getQuantity() :
                    existingItem.getQuantity() - productRequest.getQuantity());
            if(existingItem.getQuantity() <= 0) {
                cart.getItems().remove(existingItem);
            }
        } else {
            CartItem newItem = new CartItem();
            ProductInfo productInfo = new ProductInfo(product.id(), product.name());
            newItem.setProductInfo(productInfo);
            newItem.setQuantity(productRequest.getQuantity());
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }
    }

    private void calculateCartTotals(Cart cart, List<Product> productList) {
        double grossValue = 0.00D;
        double totalSavings = 0.00D;
        double netValue = 0.00D;

        for (CartItem item : cart.getItems()) {
            Optional<Product> productInList = productList
                    .stream()
                    .filter(product -> product.id().equals(item.getProductInfo().getProductId()))
                    .findFirst();
            double originalPrice = 0;
            if(productInList.isPresent()) {
                originalPrice = productInList.get().price() * item.getQuantity();
            } else {
                throw new ResourceNotFoundException("Product not found with id: " + item.getProductInfo().getProductId());
            }

            double netPrice = calculateNetPrice(item, productInList.get());
            grossValue += originalPrice;
            netValue += netPrice;
            totalSavings += (originalPrice - netPrice);
        }

        cart.setNetValue(convertPriceToPounds(netValue));
        cart.setGrosslValue(convertPriceToPounds(grossValue));
        cart.setTotalSavings(convertPriceToPounds(totalSavings));
    }

    private double calculateNetPrice(CartItem item, Product product) {

        double totalPrice = product.price() * item.getQuantity();

        if (product.promotions() != null) {
            for (Promotion promotion : product.promotions()) {
                totalPrice -= applyPromotion(promotion, item, product.price());
            }
        }
        return totalPrice;
    }

    private double applyPromotion(Promotion promotion, CartItem item, double price) {
        int quantity = item.getQuantity();

        switch (promotion.type()) {
            case "QTY_BASED_PRICE_OVERRIDE":
                if (quantity >= promotion.required_qty()) {
                    int numOfDiscounts = quantity / promotion.required_qty();
                    int remainingItems = quantity % promotion.required_qty();

                    return (price * quantity) - (promotion.price() * numOfDiscounts + remainingItems * price);
                }
            case "BUY_X_GET_Y_FREE":
                if (quantity >= promotion.required_qty()) {
                    int freeItems = (quantity / promotion.required_qty()) * promotion.free_qty();
                    return freeItems * price;
                }
            case "FLAT_PERCENT":
                return (price * quantity * promotion.amount()) / 100.0;
            default:
                return 0D;
        }
    }

    public Cart changeCartStatus(Long id, CartStatus cartStatus) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + id));
        cart.setStatus(cartStatus);
        return cartRepository.save(cart);
    }
}
