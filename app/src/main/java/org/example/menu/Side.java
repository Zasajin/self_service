package org.example.menu;

/**
 * The Side class extends the Single class and represents a side item with a
 * name and price.
 */
public class Side extends Single {
  /**
   * Side class constructor.
   * added id to avoid error
   *
   * @param id      int id of the side dish
   * @param name    string name of the side dish
   * @param price   price value of the side dish
   * @param type    what type of dish it is
   * @param imgPath the path to the items image
   */
  public Side(int id, String name, float price, Type type, String imgPath) {
    super(id, name, price, type, imgPath);
  }
}
