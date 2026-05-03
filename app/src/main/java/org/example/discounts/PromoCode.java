package org.example.discounts;

import java.time.LocalDate;

/**
 * This is the class for the promocode that the user enters
 * at checkout.
 */
public class PromoCode {

  private String code;
  private float discountAmount;
  private boolean percentage;
  private LocalDate expiryDate;

  /**
   * This is the promocode constructor.
   *
   * @param code           the entered promocode
   * @param discountAmount how much discount in money
   * @param percentage     how much discount in percentage
   * @param expiryDate     expiration date of the promocode
   */
  public PromoCode(String code, float discountAmount, boolean percentage, LocalDate expiryDate) {
    this.code = code;
    this.discountAmount = discountAmount;
    this.percentage = percentage;
    this.expiryDate = expiryDate;

  }

  public String getCode() {
    return code;
  }

  public float getDiscountAmount() {
    return discountAmount;
  }

  public boolean isPercentage() {
    return percentage;
  }

  public boolean isValid() {
    return LocalDate.now().isBefore(expiryDate);
  }

  // public PromoCodeManager findPromoCode(String inputCode)
}
