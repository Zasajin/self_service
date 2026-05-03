package org.example.menu;

/**
 * The Drink class extends the Single class and represents a drink with a name
 * and price.
 */
public class Drink extends Single {

  /**
   * Drink constructor.
   * id added to avoid error
   *
   * @param id      int id number drink
   * @param name    string name drink
   * @param price   price drink
   * @param type    what type of single it is
   * @param imgPath path to items image
   */
  public Drink(int id, String name, float price, Type type, String imgPath) {
    super(id, name, price, type, imgPath);
  }
}