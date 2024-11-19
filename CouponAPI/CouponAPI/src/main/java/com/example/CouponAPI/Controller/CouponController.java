package com.example.CouponAPI.Controller;

import com.example.CouponAPI.Model.Coupon;
import com.example.CouponAPI.Model.CouponType;
import com.example.CouponAPI.Repository.CouponRepository;
import com.example.CouponAPI.Service.CouponService;
import com.example.CouponAPI.dto.CouponDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    @Autowired
    private CouponService couponService;

    // Create Coupon
    @PostMapping
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon) {
        // Ensure that the new fields (cartThreshold, discountPercentage, etc.) are correctly set
        return ResponseEntity.ok(couponService.saveCoupon(coupon));
    }

    // Get all Coupons
    @GetMapping
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    // Get Coupon by ID
    @GetMapping("/{id}")
    public ResponseEntity<Coupon> getCouponById(@PathVariable Long id) {
        Coupon coupon = couponService.getCouponById(id);
        return coupon != null ? ResponseEntity.ok(coupon) : ResponseEntity.notFound().build();
    }

    @Autowired
    private CouponRepository couponRepository;

    // Update Coupon
    @PutMapping("/{id}")
    public ResponseEntity<Coupon> updateCoupon(@PathVariable Long id, @RequestBody Coupon couponDetails) {
        Optional<Coupon> existingCouponOpt = couponRepository.findById(id);

        if (!existingCouponOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Coupon not found
        }

        Coupon existingCoupon = existingCouponOpt.get();

        // Updated fields (highlighted)
        existingCoupon.setName(couponDetails.getName()); // Updated field
        existingCoupon.setType(couponDetails.getType()); // Updated field
        existingCoupon.setActive(couponDetails.isActive()); // Updated field
        existingCoupon.setExpiryDate(couponDetails.getExpiryDate()); // Updated field
        existingCoupon.setStartDate(couponDetails.getStartDate()); // Updated field
        existingCoupon.setMaxUsesPerUser(couponDetails.getMaxUsesPerUser()); // Updated field
        existingCoupon.setMaxTotalUses(couponDetails.getMaxTotalUses()); // Updated field
        existingCoupon.setUsedCount(couponDetails.getUsedCount()); // Updated field
        existingCoupon.setDetails(couponDetails.getDetails()); // Updated field

        // Set new fields based on coupon type
        existingCoupon.setCartThreshold(couponDetails.getCartThreshold()); // Updated field
        existingCoupon.setDiscountPercentage(couponDetails.getDiscountPercentage()); // Updated field

        // Save updated coupon to the repository
        Coupon updatedCoupon = couponRepository.save(existingCoupon);

        return new ResponseEntity<>(updatedCoupon, HttpStatus.OK);
    }

    // Delete Coupon
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    // Apply Coupon
    @PostMapping("/apply-coupon/{id}")
    public ResponseEntity<String> applyCoupon(@PathVariable Long id, @RequestBody CouponDTO couponDTO) {
        String response = couponService.applyCoupon(id, couponDTO);

        // Modify logic here according to your business rules for the new coupon types
        if (response.equals("Valid coupon")) {
            // For cart-wise coupons
            Coupon coupon = couponService.getCouponById(id);
            if (coupon.getType() == CouponType.CART_WISE) {
                // Apply logic for cart-wide coupon (e.g., apply discount if cart total > threshold)
                if (couponDTO.getCartTotal() > coupon.getCartThreshold()) {
                    double discount = couponDTO.getCartTotal() * (coupon.getDiscountPercentage() / 100);
                    return ResponseEntity.ok("Coupon applied: Discount of " + discount + " applied to the cart.");
                }
                return ResponseEntity.badRequest().body("Coupon not applicable for this cart total.");
            }

            // For product-wise coupons
            else if (coupon.getType() == CouponType.PRODUCT_WISE) {
                // Apply product-specific logic
                if (couponDTO.getProductList().contains(couponDTO.getProduct())) {
                    double productPrice = couponDTO.getProductPrice();
                    double discount = productPrice * (coupon.getDiscountPercentage() / 100);
                    return ResponseEntity.ok("Coupon applied: " + coupon.getDiscountPercentage() + "% off on product " + couponDTO.getProduct());
                }
                return ResponseEntity.badRequest().body("Coupon not applicable to this product.");
            }

            // For BxGy coupons
            else if (coupon.getType() == CouponType.BXGY) {
                // Apply Buy X Get Y logic
                int buyCount = couponDTO.getBuyList().size();
                int getCount = couponDTO.getGetList().size();
                int maxRepetitions = couponDTO.getMaxRepetitions();

                int freeItems = Math.min(buyCount / 2, getCount); // Example for b2g1 logic
                if (freeItems > 0) {
                    return ResponseEntity.ok("Coupon applied: You get " + freeItems + " free items.");
                } else {
                    return ResponseEntity.badRequest().body("Not enough products for Buy X Get Y.");
                }
            }
        }

        return ResponseEntity.badRequest().body("Invalid coupon code or not applicable.");
    }
}

