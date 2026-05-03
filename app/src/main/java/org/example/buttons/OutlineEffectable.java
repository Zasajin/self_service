package org.example.buttons;

import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;

/**
 * Interface for components that can have an outline effect applied or removed.
 */
public interface OutlineEffectable {
  /**
   * Apply or remove an effect (like outline) to the component.
   *
   * @param effect the effect to apply, or null to remove.
   */

  // Implementing classes must provide a method to set the effect on themselves
  void setOutlineEffect(Effect effect);

  /**
   * Enables or disables a white outline effect on the component.
   */
  default void enableOutline(boolean enabled) {
    if (enabled) {
      DropShadow outline = new DropShadow();
      outline.setColor(Color.WHITE);
      outline.setRadius(5);
      outline.setSpread(0.95);
      setOutlineEffect(outline);
    } else {
      setOutlineEffect(null);
    }
  }
}