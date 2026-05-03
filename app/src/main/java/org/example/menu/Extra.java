package org.example.menu;

/**
 * The class Extra extends the class Single and
 * has a constructor that takes a name and price as parameters.
 */
public class Extra extends Single {

  /**
   * Extra constructor.
   * id added to avoid error
   *
   * @param id      int id of extra dish
   * @param name    string name of extra
   * @param price   price of the item
   * @param type    what kind of single it is
   * @param imgPath path to items image
   */
  public Extra(int id, String name, float price, Type type, String imgPath) {
    super(id, name, price, type, imgPath);
  }
}