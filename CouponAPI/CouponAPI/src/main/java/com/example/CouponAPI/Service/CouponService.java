package com.example.CouponAPI.Service;

import com.example.CouponAPI.Model.Coupon;
import com.example.CouponAPI.dto.CouponDTO;
import com.example.CouponAPI.Model.CouponType;
import com.example.CouponAPI.Repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    // Save a new Coupon
    public Coupon saveCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    // Get all Coupons
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    // Get Coupon by ID
    public Coupon getCouponById(Long id) {
        Optional<Coupon> couponOpt = couponRepository.findById(id);
        return couponOpt.orElse(null);
    }

    // Update Coupon
    public Coupon updateCoupon(Long id, Coupon couponDetails) {
        Optional<Coupon> existingCouponOpt = couponRepository.findById(id);
        if (existingCouponOpt.isPresent()) {
            Coupon existingCoupon = existingCouponOpt.get();
            // Update coupon details
            existingCoupon.setName(couponDetails.getName());
            existingCoupon.setType(couponDetails.getType());
            existingCoupon.setActive(couponDetails.isActive());
            existingCoupon.setExpiryDate(couponDetails.getExpiryDate());
            existingCoupon.setStartDate(couponDetails.getStartDate());
            existingCoupon.setMaxUsesPerUser(couponDetails.getMaxUsesPerUser());
            existingCoupon.setMaxTotalUses(couponDetails.getMaxTotalUses());
            existingCoupon.setUsedCount(couponDetails.getUsedCount());
            existingCoupon.setDetails(couponDetails.getDetails());
            existingCoupon.setCartThreshold(couponDetails.getCartThreshold());
            existingCoupon.setDiscountPercentage(couponDetails.getDiscountPercentage());
            return couponRepository.save(existingCoupon);
        }
        return null;
    }

    // Delete Coupon
    public void deleteCoupon(Long id) {
        couponRepository.deleteById(id);
    }

    // Apply Coupon
    public String applyCoupon(Long id, CouponDTO couponDTO) {
        // Fetch coupon from the database
        Coupon coupon = getCouponById(id);
        if (coupon == null || !coupon.isActive()) {
            return "Coupon is not active or doesn't exist.";
        }

        // Cart-wise Coupon Logic
        if (coupon.getType() == CouponType.CART_WISE) {
            if (couponDTO.getCartTotal() > coupon.getCartThreshold()) {
                double discount = couponDTO.getCartTotal() * (coupon.getDiscountPercentage() / 100);
                return "Coupon applied: Discount of " + discount + " applied to the cart.";
            }
            return "Cart total does not meet the required threshold for the coupon.";
        }

        // Product-wise Coupon Logic
        else if (coupon.getType() == CouponType.PRODUCT_WISE) {
            if (couponDTO.getProductList().contains(couponDTO.getProduct())) {
                double discount = couponDTO.getProductPrice() * (coupon.getDiscountPercentage() / 100);
                return "Coupon applied: " + coupon.getDiscountPercentage() + "% off on product " + couponDTO.getProduct();
            }
            return "Coupon is not applicable to this product.";
        }

        // BxGy Coupon Logic (Buy X Get Y)
        else if (coupon.getType() == CouponType.BXGY) {
            int buyCount = couponDTO.getBuyList().size();
            int getCount = couponDTO.getGetList().size();
            int maxRepetitions = couponDTO.getMaxRepetitions();

            // Calculate how many free items the user gets
            int freeItems = Math.min(buyCount / 2, getCount);  // Example for b2g1 logic
            if (freeItems > 0) {
                return "Coupon applied: You get " + freeItems + " free items.";
            } else {
                return "Not enough products for Buy X Get Y to apply.";
            }
        }

        return "Invalid coupon or coupon type not supported.";
    }
}
