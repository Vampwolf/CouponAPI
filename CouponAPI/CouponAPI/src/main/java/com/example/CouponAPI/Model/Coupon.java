package com.example.CouponAPI.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(indexes = {
        @Index(name = "idx_type", columnList = "type"),
        @Index(name = "idx_isActive", columnList = "isActive")
})
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Coupon name cannot be blank")
    private String name; // Name of the coupon (e.g., "Cart Discount", "Buy X Get Y")

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Coupon type cannot be null")
    private CouponType type; // Enum to define coupon types

    private boolean isActive; // Determines if the coupon is currently valid

    @FutureOrPresent(message = "Expiry date cannot be in the past")
    private LocalDate expiryDate; // Date when the coupon expires

    private LocalDateTime startDate; // Optional start date for the coupon

    @PositiveOrZero(message = "Max uses per user must be zero or positive")
    private Integer maxUsesPerUser; // Maximum number of times a single user can use this coupon

    @PositiveOrZero(message = "Max total uses must be zero or positive")
    private Integer maxTotalUses; // Maximum total number of times the coupon can be used across all users

    private Integer usedCount = 0; // Tracks how many times this coupon has been used

    @Lob
    private String details; // JSON structure for rules (e.g., discount thresholds, BxGy logic)

    // Specific fields for business logic
    @PositiveOrZero(message = "Cart threshold must be zero or positive")
    private Double cartThreshold; // Minimum cart value for cart-wise discounts

    @PositiveOrZero(message = "Discount percentage must be zero or positive")
    private Double discountPercentage; // Discount percentage for cart-wise and product-wise coupons

    @ElementCollection
    private List<String> applicableProducts; // List of products for product-wise coupons

    @ElementCollection
    private List<String> buyProducts; // List of products in the "buy" array for BxGy coupons

    @ElementCollection
    private List<String> getProducts; // List of products in the "get" array for BxGy coupons

    private Integer buyQuantity; // Number of products to buy in the BxGy rule

    private Integer getQuantity; // Number of products to get for free in the BxGy rule

    private Integer repetitionLimit; // Maximum times a BxGy coupon can be applied

    // Pricing fields
    @PositiveOrZero(message = "Price must be zero or positive")
    private Double price; // Original price before the discount

    @PositiveOrZero(message = "Discounted price must be zero or positive")
    private Double discountedPrice; // Discount amount

    @PositiveOrZero(message = "Final price must be zero or positive")
    private Double finalPrice; // Price after applying the discount

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CouponType getType() {
        return type;
    }

    public void setType(CouponType type) {
        this.type = type;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public Integer getMaxUsesPerUser() {
        return maxUsesPerUser;
    }

    public void setMaxUsesPerUser(Integer maxUsesPerUser) {
        this.maxUsesPerUser = maxUsesPerUser;
    }

    public Integer getMaxTotalUses() {
        return maxTotalUses;
    }

    public void setMaxTotalUses(Integer maxTotalUses) {
        this.maxTotalUses = maxTotalUses;
    }

    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Double getCartThreshold() {
        return cartThreshold;
    }

    public void setCartThreshold(Double cartThreshold) {
        this.cartThreshold = cartThreshold;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public List<String> getApplicableProducts() {
        return applicableProducts;
    }

    public void setApplicableProducts(List<String> applicableProducts) {
        this.applicableProducts = applicableProducts;
    }

    public List<String> getBuyProducts() {
        return buyProducts;
    }

    public void setBuyProducts(List<String> buyProducts) {
        this.buyProducts = buyProducts;
    }

    public List<String> getGetProducts() {
        return getProducts;
    }

    public void setGetProducts(List<String> getProducts) {
        this.getProducts = getProducts;
    }

    public Integer getBuyQuantity() {
        return buyQuantity;
    }

    public void setBuyQuantity(Integer buyQuantity) {
        this.buyQuantity = buyQuantity;
    }

    public Integer getGetQuantity() {
        return getQuantity;
    }

    public void setGetQuantity(Integer getQuantity) {
        this.getQuantity = getQuantity;
    }

    public Integer getRepetitionLimit() {
        return repetitionLimit;
    }

    public void setRepetitionLimit(Integer repetitionLimit) {
        this.repetitionLimit = repetitionLimit;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(Double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
    }
}
