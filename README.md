# CouponAPI

Readme…

Most of the code of the project is well commented by me for easy understanding. Apart from that below is the more detailed explanation about the coupon service that I had developed.

Existing Use Cases

1. Create a Coupon
Use Case: Allows admins or business owners to create new coupons with detailed specifications.
Fields: Name, type, active status, expiry date, start date, cart threshold, discount percentage, product-specific details (applicable products, buy/get arrays, etc.), and limits like maxUsesPerUser and maxTotalUses.
Endpoint: POST /coupons
Example: Create a cart-wise coupon offering 10% off for orders over ₹100.
Validation: Ensures fields like cartThreshold, discountPercentage, and product arrays are correctly provided based on the coupon type.

2. Get All Coupons
Use Case: Retrieve all coupons stored in the database.
Endpoint: GET /coupons
Purpose: Useful for the admin to view all active, expired, or inactive coupons.
Filter Suggestion: Can add filters for isActive, type, and expiryDate.

3. Get a Coupon by ID
Use Case: Retrieve details of a specific coupon by its unique ID.
Endpoint: GET /coupons/{id}
Purpose: View all details of a particular coupon for auditing or debugging.

4. Update a Coupon
Use Case: Modify details of an existing coupon.
Endpoint: PUT /coupons/{id}
Purpose: Update attributes such as the discount percentage, thresholds, product arrays, or expiry dates.
Validation: Prevent modifications to usedCount and handle cases where an already expired coupon cannot be reactivated.

5. Delete a Coupon
Use Case: Permanently remove a coupon from the database.
Endpoint: DELETE /coupons/{id}
Purpose: Remove old or irrelevant coupons no longer in use.

6. Apply a Coupon
Use Case: Validate and apply a coupon to a cart provided by the customer.
Endpoint: POST /coupons/apply-coupon/{id}
Logic:
Cart-wise Coupon: Apply a percentage discount if the cart total exceeds the cartThreshold.
Product-wise Coupon: Check if applicable products are in the cart and apply a discount to those products.
BxGy Coupons: Validate the "buy" and "get" arrays, and apply discounts as per the rules.
Validation:
Ensure coupon is active, not expired, and within usage limits.
Verify cart structure and thresholds before applying.

Suggested Use Cases

1. Filter Coupons
Purpose: Provide a way to retrieve coupons based on specific criteria.
Endpoint: GET /coupons?type={type}&isActive={true|false}&expiryDate={YYYY-MM-DD}
Example:
Get all active CART_WISE coupons.
Retrieve coupons expiring in the next 7 days.
Benefits: Helps admins quickly find relevant coupons.

2. Deactivate Coupons Automatically
Purpose: Handle coupon expirations and ensure inactive coupons are not accidentally used.
Implementation:
Add a scheduled job (using @Scheduled or Quartz) to check all coupons and deactivate expired ones.
Example:
A daily job runs at midnight to deactivate coupons whose expiryDate is in the past.

3. Coupon Usage Reports
Purpose: Track the performance and usage of coupons for analytics.
Endpoint: GET /coupons/usage-report
Details:
Include metrics like usedCount, maxUsesPerUser, maxTotalUses, and the most popular coupons.
Break down usage by coupon type.
Benefits: Helps businesses evaluate which coupons drive the most sales.

4. Bulk Creation of Coupons
Purpose: Allow admins to create multiple coupons at once.
Endpoint: POST /coupons/bulk
Input: A list of coupon objects in the request body.
Use Case: Useful for promotions requiring many similar coupons.

5. Generate Unique Coupon Codes
Purpose: Generate unique, user-specific codes for private offers.
Implementation:
Extend the Coupon entity to include a code field.
Add a new endpoint: POST /coupons/generate-code/{id}.
Automatically generate a unique alphanumeric code for the specified coupon.
Example: Create a personalized discount code like WELCOME10 for new users.

6. Pagination for Coupons
Purpose: Handle large datasets by providing paginated responses.
Endpoint: GET /coupons?page={page}&size={size}
Details:
Add sorting (e.g., GET /coupons?sort=expiryDate,asc).
Benefits: Enhances scalability for systems with many coupons.

7. Coupon Usage by Customer
Purpose: Retrieve how many times a specific customer has used a particular coupon.
Endpoint: GET /coupons/{id}/usage/{customerId}
Benefits: Helps monitor abuse or overuse of coupons.

8. Customer-Specific Coupons
Purpose: Target specific customers with personalized discounts.
Implementation:
Add a customerId field to the Coupon entity.
Restrict application of such coupons to the specified customers.

9. Duplicate Coupons
Purpose: Easily clone an existing coupon.
Endpoint: POST /coupons/{id}/duplicate
Details:
Create a new coupon with all the same attributes but with a new id and optional updates (like changing expiry dates).
Benefits: Saves time when creating similar promotions.

10. Dynamic Pricing Adjustments
Purpose: Automatically calculate and update price, discountedPrice, and finalPrice fields based on coupon application.
Details:
Integrate coupon logic with product pricing to ensure discounts are accurately reflected in the response.

__________________________________________________________________________________
Current Limitations and Suggestions to Overcome Them

1. Lack of Validation Logic for Coupon Application
Limitation:
Coupons can be applied to carts or products without proper validation (e.g., ensuring cart value meets cartThreshold, or product-specific rules are adhered to).
Current validations may not handle edge cases like overlapping coupons or invalid startDate and expiryDate.
Solution:
Add Validation Rules:
Validate cartThreshold for CART_WISE coupons.
Check product IDs in applicableProducts for PRODUCT_WISE coupons.
Verify startDate is before expiryDate.
Ensure usedCount doesn’t exceed maxUsesPerUser or maxTotalUses.
Test Edge Cases: Write unit tests for scenarios like:
Applying expired coupons.
Using coupons more than the allowed limit.

2. Manual Expiry Handling
Limitation:
Expired coupons remain active unless explicitly updated or handled by the admin.
Solution:
Automatic Expiry:
Use a scheduled task (@Scheduled or Quartz Scheduler) to deactivate expired coupons daily.
Query-Level Validation:
Modify the applyCoupon service to reject expired coupons dynamically during runtime.

3. Performance Issues with Large Datasets
Limitation:
As the number of coupons grows, retrieving and processing them might become slow, especially when querying all coupons (GET /coupons).
Solution:
Pagination:
Implement pagination for GET /coupons with parameters like page and size.
Example: GET /coupons?page=1&size=10.
Indexing:
Add database indexes on frequently queried fields like type, isActive, and expiryDate.

4. Static Coupon Types
Limitation:
The coupon logic is rigid and limited to predefined types (CART_WISE, PRODUCT_WISE, BXGY). Custom coupon types cannot be added dynamically.
Solution:
Dynamic Coupon Rules:
Use a rule engine like Drools or Spring Expression Language (SpEL) to define flexible coupon rules.
Example: Admins could create rules like “10% off on the first 3 orders above ₹500.”
Extensible CouponType Enum:
Store type as a string in the database instead of an enum. This allows adding new types without modifying the code.

5. Limited Reporting and Analytics
Limitation:
No detailed insights into coupon performance, such as:
Which coupons are used most frequently?
How much revenue is generated through coupons?
Which customers frequently use coupons?
Solution:
Add Usage Reports:
Track metrics like usedCount, usage per customer, and total discounts applied.
Store a separate transaction log for each coupon application.
Provide admin-friendly endpoints to retrieve reports (GET /coupons/reports).

6. No Security for Coupon Codes
Limitation:
Coupons could be exploited (e.g., sharing unique codes with unauthorized users or modifying requests in Postman).
Solution:
Secure Coupon Validation:
Add customer-specific coupon codes and validate against the logged-in user's ID.
Use JWT or API keys for authentication to protect endpoints like applyCoupon.
Rate Limiting:
Add rate-limiting to prevent abuse of the applyCoupon endpoint.

7. Handling Overlapping Coupons
Limitation:
There’s no clear logic to handle cases where multiple coupons are eligible for a single cart or product.
Solution:
Define Rules for Coupon Priority:
Allow only one coupon per order.
Define priority rules based on type, discountPercentage, or expiryDate.
Enable Stacking:
If stacking is allowed, ensure cumulative discounts are capped at a maximum percentage.

_________________________________________________________
Developer Comments: I have worked on this project and it has been a great and knowledge refreshing experience. I could possibly resolve every use-case that is remaining in the project but due to limited time and exponential use-cases I didn’t get into. Moreover thankyou for this opportunity and I’m looking forward to hearing back from you.

Best Regards
Yash Sharma



