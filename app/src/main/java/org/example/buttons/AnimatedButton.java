package org.example.buttons;

import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;

/**
 * A custom JavaFX Button with animated press and release scaling effects.
 */
public class AnimatedButton extends Button {

  public AnimatedButton() {
    super();
    initAnimation();
  }

  public AnimatedButton(String text) {
    super(text);
    initAnimation();
  }

  private void initAnimation() {
    setOnMousePressed(e -> animateButtonPress(this, 0.95));
    setOnMouseReleased(e -> animateButtonPress(this, 1.0));
  }

  private void animateButtonPress(Button button, double scale) {
    ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
    st.setToX(scale);
    st.setToY(scale);
    st.play();
  }
}
