package org.example.buttons;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import org.example.kiosk.Dictionary;
import org.example.kiosk.LanguageSetting;

/**
 * LangBtn is a button that represents a language selection option.
 * It updates its image automatically based on the current global language
 * setting.
 */
public class LangBtn extends RoundButton {

  private Dictionary dictionary;
  private String englishImagePath;
  private String swedishImagePath;

  private static final List<WeakReference<LangBtn>> instances = new ArrayList<>();

  /**
   * Constructs a LangBtn and initializes its image paths and dictionary.
   */
  public LangBtn() {
    super("", 140); // Initialize RoundButton with empty text and size
    this.dictionary = new Dictionary();

    englishImagePath = "/eng.png";
    swedishImagePath = "/swe.png";

    imagePaths.clear(); // Disable cycling logic
    imagePaths.add(englishImagePath);
    imagePaths.add(swedishImagePath);

    instances.add(new WeakReference<>(this));
    updateImage(); // Set image based on current language

  }

  /**
   * Updates the image of this button based on the global language setting.
   */
  public void updateImage() {
    String currLang = LanguageSetting.getInstance().getSelectedLanguage();

    if ("en".equals(currLang)) {
      setImage(swedishImagePath, 140);
    } else {
      setImage(englishImagePath, 140);
    }
  }

  /**
   * Updates all LangBtn instances to reflect the current language setting.
   */
  public static void updateAllLanguageButtons() {
    Iterator<WeakReference<LangBtn>> iterator = instances.iterator();

    while (iterator.hasNext()) {
      LangBtn button = iterator.next().get();
      if (button == null) {
        iterator.remove();
      } else {
        button.updateImage();
      }
    }
  }

  /**
   * Toggles language and updates text of components.
   */
  public void updateLanguage(List<Labeled> labels, List<TextInputControl> inputs) {
    dictionary.toggleLanguage();

    for (Labeled label : labels) {
      label.setText(dictionary.translate(label.getText()));
    }

    for (TextInputControl input : inputs) {
      input.setPromptText(dictionary.translate(input.getPromptText()));
    }

    LangBtn.updateAllLanguageButtons();
  }

  /**
   * Overload for just Labeled components.
   */
  public void updateLanguage(List<Labeled> components) {
    dictionary.toggleLanguage();

    for (Labeled component : components) {
      component.setText(dictionary.translate(component.getText()));
    }

    LangBtn.updateAllLanguageButtons();
  }
}