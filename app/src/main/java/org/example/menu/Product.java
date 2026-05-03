package org.example.menu;

/**
 * This is the product class used as a parent for single and meal.
 */
public abstract class Product {
  private String name;
  private String description;
  private double price;
  private int id;
  private Type type;
  private int isActive;
  private String imagePath;
  private int preparationTime;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public int getActivity() {
    return isActive;
  }

  public void setActivity(int isActive) {
    this.isActive = isActive;
  }

  public int getPreparationTime() {
    return preparationTime;
  }

  public void setPreparationTime(int preparationTime) {
    this.preparationTime = preparationTime;
  }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

}
