package org.example.menu;

import java.util.List;

/**
 * The interface declares several methods that define the behavior of a menu
 * system.
 *
 */

public interface Imenu {
  /**
   * To get the main dishes.
   *
   * @return main dishes as single
   */
  List<Single> getMains();

  /**
   * To get all side dishes.
   *
   * @return side dishes as single
   */
  List<Single> getSides();

  /**
   * To get all side drinks.
   *
   * @return drinks as single
   */
  List<Single> getDrinks();

  /**
   * To get all side desserts.
   *
   * @return desserts as single
   */
  List<Single> getDesserts();

  /**
   * To get all side extra dishes.
   *
   * @return extra dishes as single
   */
  List<Single> getExtras();

  /**
   * To get all currently searched for products.
   *
   * @return searched products as objects
   */
  List<Object> searchProducts(String name);
}