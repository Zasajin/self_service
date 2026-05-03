package org.example.menu;

/**
 * The Dessert class extends the Single class and represents a dessert item with
 * a name and price.
 */
public class Dessert extends Single {

  /**
   * Dessert constructor.
   *
   * @param id      id int dessert
   * @param name    name string dessert
   * @param price   price dessert
   * @param type    type of single
   * @param imgPath path it items image
   */
  public Dessert(int id, String name, float price, Type type, String imgPath) {
    super(id, name, price, type, imgPath);
  }
}
