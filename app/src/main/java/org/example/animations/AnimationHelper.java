package org.example.animations;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Utility class providing animation helper methods for JavaFX Nodes.
 */
public class AnimationHelper {

  // Fade In
  /**
   * Fades in the given JavaFX node by animating its opacity from 0 to 1.
   *
   * @param node            the JavaFX Node to animate
   * @param durationSeconds the duration of the fade in seconds
   * @param delaySeconds    the delay before the animation starts in seconds
   */
  public static void fadeIn(Node node, double durationSeconds, double delaySeconds) {
    node.setOpacity(0.0);
    FadeTransition fade = new FadeTransition(Duration.seconds(durationSeconds), node);
    fade.setFromValue(0.0);
    fade.setToValue(1.0);
    fade.setDelay(Duration.seconds(delaySeconds));
    fade.play();
  }

  // Slide in from top
  /**
   * Slides in the given JavaFX node from the top by animating its Y translation.
   *
   * @param node            the JavaFX Node to animate
   * @param distance        the distance to slide from (in pixels)
   * @param durationSeconds the duration of the slide in seconds
   * @param delaySeconds    the delay before the animation starts in seconds
   */
  public static void slideInFromTop(
      Node node,
      double distance,
      double durationSeconds,
      double delaySeconds) {
    node.setTranslateY(-distance);
    TranslateTransition slide = new TranslateTransition(Duration.seconds(durationSeconds), node);
    slide.setToY(0);
    slide.setDelay(Duration.seconds(delaySeconds));
    slide.setInterpolator(Interpolator.EASE_OUT);
    slide.play();
  }

  // Pop-in animation (scale)
  /**
   * Animates the given JavaFX node with a pop-in effect by scaling it from 0 to
   * 1.
   *
   * @param node            the JavaFX Node to animate
   * @param durationSeconds the duration of the pop-in animation in seconds
   * @param delaySeconds    the delay before the animation starts in seconds
   */
  public static void popIn(Node node, double durationSeconds, double delaySeconds) {
    node.setScaleX(0.0);
    node.setScaleY(0.0);
    ScaleTransition scale = new ScaleTransition(Duration.seconds(durationSeconds), node);
    scale.setToX(1.0);
    scale.setToY(1.0);
    scale.setDelay(Duration.seconds(delaySeconds));
    scale.setInterpolator(Interpolator.EASE_OUT);
    scale.play();
  }

  // Shake animation
  /**
   * Applies a shake animation to the given JavaFX node by translating it
   * horizontally.
   *
   * @param node           the JavaFX Node to animate
   * @param durationMillis the duration of the shake animation in milliseconds
   */
  public static void shake(Node node, double durationMillis) {
    TranslateTransition shake = new TranslateTransition(Duration.millis(durationMillis), node);
    shake.setFromX(-10);
    shake.setToX(10);
    shake.setCycleCount(6);
    shake.setAutoReverse(true);
    shake.play();
  }

  // Combine fade + slide
  public static void fadeAndSlideIn(
      Node node,
      double slideDistance,
      double durationSeconds,
      double delaySeconds) {
    fadeIn(node, durationSeconds, delaySeconds);
    slideInFromTop(node, slideDistance, durationSeconds, delaySeconds);
  }

  /**
   * Fades and scales in the given JavaFX node by animating its opacity and scale.
   *
   * @param node        the JavaFX Node to animate
   * @param durationSec the duration of the animation in seconds
   * @param delaySec    the delay before the animation starts in seconds
   * @param fromScale   the initial scale value
   * @param toScale     the final scale value
   * @param interp      the interpolator to use for the animation
   */
  public static void fadeAndScaleIn(
      Node node,
      double durationSec,
      double delaySec,
      double fromScale,
      double toScale,
      Interpolator interp) {
    node.setOpacity(0);
    node.setScaleX(fromScale);
    node.setScaleY(fromScale);

    FadeTransition fade = new FadeTransition(Duration.seconds(durationSec), node);
    fade.setFromValue(0);
    fade.setToValue(1);
    fade.setInterpolator(interp);
    fade.setDelay(Duration.seconds(delaySec));

    ScaleTransition scale = new ScaleTransition(Duration.seconds(durationSec), node);
    scale.setFromX(fromScale);
    scale.setFromY(fromScale);
    scale.setToX(toScale);
    scale.setToY(toScale);
    scale.setInterpolator(interp);
    scale.setDelay(Duration.seconds(delaySec));

    ParallelTransition animation = new ParallelTransition(fade, scale);
    animation.play();
  }

  /**
   * Slides in the given JavaFX node from above while fading it in.
   *
   * @param node        the JavaFX Node to animate
   * @param translateY  the distance to translate from (in pixels)
   * @param durationSec the duration of the animation in seconds
   * @param delaySec    the delay before the animation starts in seconds
   * @param interp      the interpolator to use for the animation
   */
  public static void slideAndFadeIn(
      Node node,
      double translateY,
      double durationSec,
      double delaySec,
      Interpolator interp) {
    node.setOpacity(0);
    node.setTranslateY(-translateY);

    FadeTransition fade = new FadeTransition(Duration.seconds(durationSec), node);
    fade.setFromValue(0);
    fade.setToValue(1);
    fade.setInterpolator(interp);
    fade.setDelay(Duration.seconds(delaySec));

    TranslateTransition slide = new TranslateTransition(Duration.seconds(durationSec), node);
    slide.setFromY(-translateY);
    slide.setToY(0);
    slide.setInterpolator(interp);
    slide.setDelay(Duration.seconds(delaySec));

    ParallelTransition animation = new ParallelTransition(fade, slide);
    animation.play();
  }

  /**
   * Applies a pulse animation to the given JavaFX node by scaling it up and then
   * back to normal.
   *
   * @param node     the JavaFX Node to animate
   * @param scale    the scale factor to pulse to
   * @param duration the duration of the pulse animation in seconds
   * @param delay    the delay before the animation starts in seconds
   */
  public static void pulse(Node node, double scale, double duration, double delay) {
    ScaleTransition pulse = new ScaleTransition(Duration.seconds(duration), node);
    pulse.setFromX(1.0);
    pulse.setFromY(1.0);
    pulse.setToX(scale);
    pulse.setToY(scale);
    pulse.setAutoReverse(true);
    pulse.setCycleCount(2); // pulse once: grow then shrink
    pulse.setDelay(Duration.seconds(delay));
    pulse.setInterpolator(Interpolator.EASE_BOTH);
    pulse.play();
  }
}
