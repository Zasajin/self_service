package org.example.menu;

/**
 * The MainDish class extends the Single class and
 * represents a main dish item with a name and price.
 */
public class MainDish extends Single {

  /**
   * The main dish constructor.
   *
   * @param id      dish id
   * @param name    string name dish
   * @param price   price dish
   * @param type    dish single type; what kind of dish
   * @param imgPath path to the items image
   */
  public MainDish(int id, String name, float price, Type type, String imgPath) {
    super(id, name, price, type, imgPath);
  }
}