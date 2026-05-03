package org.example.users;

/**
 * Interface for user actions.
 */
public interface User {

  /**
   * Lets user browse the menu.
   */
  void browseMenu();

  /**
   * Lets user search for products by specific name.
   *
   * @param name String name of item.
   */
  void searchProduct(String name);

}
