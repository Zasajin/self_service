package org.example.kiosk;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a Dictionary.
 */
public class Dictionary {
  private Map<String, String> englishToSwedish = new HashMap<>();
  private Map<String, String> swedishToEnglish = new HashMap<>();
  private boolean toEnglish;

  /**
   * Constructs a Dictionary object.
   */
  public Dictionary() {

    // Welcome screen vocabulary
    addTranslation("Welcome to", "Välkommen till");
    addTranslation("Eat Here", "Ät här");
    addTranslation("Takeaway", "Ta med");
    addTranslation("Driver found and connected", "Drivrutin hittad och ansluten");
    addTranslation("Terms of Service", "Användarvillkor");

    // Terms and conditions vocabulary
    addTranslation(
        "1. Acceptance of Terms\n"
            + "By using our services, you agree to these terms...\n\n"
            + "2. Service Description\n"
            + "We provide food ordering services...\n\n"
            + "3. User Responsibilities\n"
            + "You must provide accurate information...\n\n"
            + "4. Limitation of Liability\n"
            + "We are not responsible for...\n\n"
            + "Last Updated: ",
        "1. Godkännande av villkor\n"
            + "Genom att använda våra tjänster godkänner du dessa villkor...\n\n"
            + "2. Tjänstebeskrivning\n"
            + "Vi tillhandahåller matbeställningstjänster...\n\n"
            + "3. Användarens ansvar\n"
            + "Du måste lämna korrekt information...\n\n"
            + "4. Ansvarsbegränsning\n"
            + "Vi ansvarar inte för...\n\n"
            + "Senast uppdaterad: ");

    // Main menu vocabulary
    addTranslation("Burgers", "Burgare");
    addTranslation("Sides", "Tillbehör");
    addTranslation("Drinks", "Drycker");
    addTranslation("Desserts", "Desserter");
    addTranslation("Meals", "Kombomenyer");
    // addTranslation("Special\nOffers", "Special\nErbjudanden");
    addTranslation("Cancel", "Avbryt");
    addTranslation("Filter Items", "Filtrera artiklar");
    addTranslation("Filter", "Filter");
    addTranslation("Item", "Artikel");
    // addTranslation("Special", "Special");
    // addTranslation("Offers", "Erbjudanden");

    // Admin login vocabulary
    addTranslation("Admin Menu", "Adminmeny");
    addTranslation("Login", "Logga in");
    addTranslation("Back to Menu", "Gå tillbaks");
    addTranslation("Password", "Lösenord");
    addTranslation("Username", "Användarnamn");
    addTranslation("Invalid login details", "Ogiltig inloggning");

    // Admin Main Menu vocabulary
    addTranslation("Welcome, Admin!", "Välkommen, Admin!");
    addTranslation("Update Menu Items", "Uppdatera menyartiklar");
    addTranslation("Order History", "Beställningshistorik");
    addTranslation("Change Timer Setting", "Ändra Timerinställningar");
    addTranslation("See Sales Summary", "Försäljningsöversikt");
    addTranslation("Set Special Offers", "Sätt Specialerbjudanden");
    addTranslation("Design", "Designa");

    // Customization Screen Vocabulary
    addTranslation("Change Kiosk Name here: ", "Byt Kiosknamn här: ");
    addTranslation("Save name", "Spara namn");
    addTranslation("Save Color Scheme", "Spara Färgschema");
    addTranslation("Reset Color Scheme", "Återställ Färgschema");
    addTranslation("Set & Test Design", "Ange & Testa Design");
    addTranslation("Prime color", "Primärfärg");
    addTranslation("Secondary color", "Sekundärfärg");
    addTranslation("Background color", "Bakgrundsfärg");
    // Basic Colors
    addTranslation("Red", "Röd");
    addTranslation("Blue", "Blå");
    addTranslation("Green", "Grön");
    addTranslation("Yellow", "Gul");
    addTranslation("Black", "Svart");
    addTranslation("White", "Vit");
    addTranslation("Orange", "Orange");
    addTranslation("Purple", "Lila");
    addTranslation("Gray", "Grå");
    addTranslation("Brown", "Brun");

    // Update Menu Items vocabulary
    addTranslation("Add Product to Menu", "Lägg till produkten");
    addTranslation("Change Prices", "Ändra priser");
    addTranslation("Remove Product from Menu", "Ta bort produkten");
    addTranslation("Apply Global Discounts", "Tillämpa globala rabatter");
    addTranslation("Add Ingredients to DB", "Lägg till ingredienser i DB:n");

    // Add Product to Menu vocabulary
    addTranslation("Add a Product to the Menu", "Lägg till en Produkt i Menyn");
    addTranslation("Product Name:", "Produktnamn:");
    addTranslation("Product Description:", "Produktbeskrivning:");
    addTranslation("Product Price:", "Produktpris:");
    addTranslation("Is active?", "Är aktiv?");
    addTranslation("Is limited?", "Begränsad?");
    addTranslation("Product Category:", "Produktkategori:");
    addTranslation("Ingredient List", "Ingredienslista");
    addTranslation("Confirm", "Bekräfta");
    addTranslation("Select image", "Välj bild");
    addTranslation("No image selected", "Ingen bild vald");
    addTranslation("Success!", "Lyckat!");
    addTranslation("Product Added Successfully!", "Produkten har lagts till!");
    addTranslation("Creating producted failed, no ID obtained",
        "Misslyckades med att skapa produkt, inget ID erhölls");
    addTranslation("No ingredients selected!", "Inga ingredienser valda!");
    addTranslation("Info", "Information");
    addTranslation("All fields are required!", "Alla fält måste fyllas i!");
    addTranslation("Database error", "Databasfel");
    addTranslation("Failed to load categories", "Kunde inte ladda kategorier");

    // Add ingridents to the DB vocabulary
    addTranslation("Add Ingredients to the Database", "Lägg till ingredienser i databasen");
    addTranslation("Select Categories which will have this ingredient:",
        "Välj kategorier som ska ha denna ingrediens:");
    addTranslation("Ingredient Name:", "Ingrediensnamn:");
    addTranslation("Add", "Lägg till");
    addTranslation("Ingredient field cannot be empty", "Ingrediensfältet får inte vara tomt");
    addTranslation("This ingredient is already in the database",
        "Denna ingrediens finns redan i databasen");
    addTranslation("Ingredient has been successfully added!", "Ingrediensen har lagts till!");

    // Item Details vocabulary
    addTranslation("Add To Cart", "Lägg till");
    addTranslation("Back", "Tillbaks");

    // Items
    addTranslation("Chicken Burger", "Kycklingburgare");
    addTranslation("Beef Burger", "Nötköttsburgare");
    addTranslation("Beer Burger", "Ölburgare");
    addTranslation("Double Burger", "Dubbelburgare");
    addTranslation("Standard Burger", "Standardburgare");
    addTranslation("All American Burger", "All American-burgare");
    addTranslation("Extra Veggies Burger", "Extra grönsaksburgare");
    addTranslation("King Burger", "Kungaburgare");
    addTranslation("Onion Rings", "Lökringar");
    addTranslation("Fries", "Pommes frites");
    addTranslation("Chili Cheese Fries", "Chili cheese-pommes");
    addTranslation("Banana", "Banan");
    addTranslation("Ice Cream", "Glass");
    addTranslation("Standard Burger Meal", "Standardburgarmeny");
    addTranslation("Chicken Burger Meal", "Kycklingburgarmeny");
    addTranslation("All American Burger Meal", "All American-meny");
    addTranslation("Beer Burger Meal", "Ölburgarmeny");
    addTranslation("Double Burger Meal", "Dubbelburgarmeny");
    addTranslation("Extra Veggies Burger Meal", "Extra grönsaksburgarmeny");
    addTranslation("King Burger Meal", "Kungaburgarmeny");

    // Item Descriptions
    addTranslation(
        "Juicy chicken with lettuce tomato and mayo in a soft bun",
        "Saftig kyckling med sallad tomat och majonnäs i ett bröd");

    addTranslation(
        "Two beef patties with cheese pickles and house sauce",
        "Två nötköttburgare med ost pickles och hemlagad sås");

    addTranslation(
        "Beef with lettuce tomato onions and ketchup mayo mix",
        "Nötkött med sallad tomat lök och ketchup majonnäs");

    addTranslation(
        "Beef cheddar bacon and BBQ sauce just like at diners",
        "Nötkött cheddar bacon och BBQ sås precis som på diner");

    addTranslation(
        "Grilled veggies with lettuce tomato onion and vegan mix",
        "Grillade grönsaker med sallad tomat lök och vegansk sås");

    addTranslation(
        "Beef with beer flavor onions bacon and Swiss cheese",
        "Nötkött med ölsmak lök bacon och schweizisk ost");

    addTranslation(
        "Triple beef double cheese lettuce tomato and King sauce",
        "Trippel kött dubbel ost sallad tomat och King sås");

    addTranslation(
        "Premium beef with pepper jack avocado and chipotle mayo",
        "Premiumkött med pepper jack avokado och chipotlemajonnäs");

    addTranslation(
        "Crispy onion rings crunchy outside and soft inside",
        "Krispiga lökringar frasiga utanpå och mjuka inuti");

    addTranslation(
        "Classic fries salted and served hot and fresh",
        "Klassiska pommes saltade och serverade varma");

    addTranslation(
        "Fries with chili and cheese spicy and satisfying",
        "Pommes med chili och ost kryddiga och mättande");

    addTranslation(
        "Banana dessert with caramel and chopped nuts",
        "Banandessert med karamell och hackade nötter");

    addTranslation(
        "Fruity orange soda with sweet sparkling taste",
        "Fruktig apelsinläsk med söt och bubblig smak");

    addTranslation(
        "Lemon lime soda fresh bubbly and light",
        "Citron lime läsk frisk bubblig och lätt");

    addTranslation(
        "Coca Cola taste with no sugar full flavor remains",
        "Coca Cola smak utan socker full smak ändå");

    addTranslation(
        "Cola with bold taste always good with a meal",
        "Cola med kraftig smak alltid bra till maten");

    addTranslation(
        "Blend of 23 flavors rich and smooth experience",
        "Blandning av 23 smaker rik och len upplevelse");

    addTranslation(
        "Classic cola sweet and fizzy timeless refreshment",
        "Klassisk cola söt och bubblig tidlös dryck");

    addTranslation(
        "Cold creamy ice cream ask for todays flavors",
        "Kall krämig glass fråga om dagens smaker");

    addTranslation("This is a yummy chicken burger", "Det här är en god kycklingburgare");
    addTranslation("This is a yummy beef burger", "Det här är en god nötköttsburgare");
    addTranslation("This is a yummy beer burger", "Det här är en god ölburgare");
    addTranslation("This is a yummy chicken burger", "Det här är en god kycklingburgare");
    addTranslation("This is a yummy beef burger", "Det här är en god nötköttsburgare");
    addTranslation("This is a yummy beer burger", "Det här är en god ölburgare");
    addTranslation("This is a yummy double burger", "Det här är en god dubbelburgare");
    addTranslation("This is a yummy standard burger", "Det här är en god standardburgare");
    addTranslation("This is a yummy all american burger", "Det här är en god all American-burgare");
    addTranslation(
        "This is a yummy extra veggies burger", "Det här är en god extra grönsaksburgare");
    addTranslation("This is a yummy king burger", "Det här är en god kungaburgare");
    addTranslation("This is a yummy onion rings", "Det här är goda lökringar");
    addTranslation("This is a yummy fries", "Det här är goda pommes frites");
    addTranslation("This is a yummy banana", "Det här är en god banan");
    addTranslation("This is a yummy ice cream", "Det här är en god glass");
    addTranslation("This is a yummy fanta", "Det här är en god fanta");
    addTranslation("This is a yummy sprite", "Det här är en god sprite");
    addTranslation("This is a yummy coke zero", "Det här är en god coke zero");
    addTranslation("This is a yummy pepsi", "Det här är en god pepsi");
    addTranslation("This is a yummy dr pepper", "Det här är en god Dr pepper");
    addTranslation("This is a yummy standard burger meal", "Det här är en god standardburgarmeny");
    addTranslation("This is a yummy chicken burger meal", "Det här är en god kycklingburgarmeny");
    addTranslation(
        "This is a yummy all american burger meal", "Det här är en god all American-meny");
    addTranslation("This is a yummy beer burger meal", "Det här är en god ölburgarmeny");
    addTranslation("This is a yummy double burger meal", "Det här är en god dubbelburgarmeny");
    addTranslation(
        "This is a yummy extra veggies burger meal", "Det här är en god extra grönsaksburgarmeny");
    addTranslation("This is a yummy king burger meal", "Det här är en god kungaburgarmeny");

    // Ingredients
    addTranslation("Beef Patty", "Nötköttspuck");
    addTranslation("Bun", "Bröd");
    addTranslation("Swiss Cheese", "Schweizerost");
    addTranslation("Pickles", "Picklad gurka");
    addTranslation("Onion", "Lök");
    addTranslation("Mustard", "Senap");
    addTranslation("Cream", "Grädde");
    addTranslation("Chocolate Bits", "Chokladbitar");
    addTranslation("Rainbow Sprinkles", "Strössel");
    addTranslation("Whipped Cream", "Vispad grädde");
    addTranslation("Sugar", "Socker");
    addTranslation("Lemon", "Citron");
    addTranslation("Mint Leaves", "Myntablad");
    addTranslation("Ice", "Is");
    addTranslation("Chili Flakes", "Chiliflakes");
    addTranslation("Black Pepper", "Svartpeppar");
    addTranslation("Sea Salt", "Havssalt");
    addTranslation("Chicken Patty", "Kycklingpuck");
    addTranslation("Lettuce", "Sallad");
    addTranslation("Tomato", "Tomat");
    addTranslation("Cheddar Cheese", "Cheddarost");
    addTranslation("Mayonnaise", "Majonnäs");

    // Checkout screen vocabulary
    addTranslation("Checkout", "Kassan");
    addTranslation("Confirm Order", "Bekräfta ");
    addTranslation("Total", "Totalt");
    addTranslation("Enter Promo Code", "Ange Kampanjkod");
    addTranslation("Total", "Totalt");
    addTranslation("Page", "Sida");
    addTranslation("of", "av");

    // Sales statistics vocabulary
    addTranslation("Sales Statistics:", "Försäljningsstatistik:");
    addTranslation("Sold Products", "Sålda Produkter");
    addTranslation("Orders per Day", "Beställningar per dag");
    addTranslation("Orders by Hour", "Beställningar per timme");
    addTranslation("Revenue by Products", "Intäkter per produkt");
    addTranslation("Revenue (last 12 Month)", "Intäkter (sen. 12 månaderna)");

    addTranslation("Product Sales", "Produktsförsäljning");
    addTranslation("Sales Data", "Försäljningsdata");
    addTranslation("Product", "Produkt");
    addTranslation("Quantity Sold", "Antal sålda");
    addTranslation("Orders per Weekday", "Beställningar per veckodag");
    addTranslation("Orders", "Beställningar");
    addTranslation("Number of Orders", "Antal beställningar");
    addTranslation("Weekday", "Veckodag");
    addTranslation("Total Revenue of the last 12 Months", "Totalintäkter de senaste 12 månaderna");
    addTranslation("Revenue in SEK", "Intäkter i SEK");
    addTranslation("Month", "Månad");
    addTranslation("Revenue", "Intäkter");

    // Order history vocabulary
    addTranslation("Order History:", "Beställningshistorik:");
    addTranslation("Order ID", "Beställnings-ID");
    addTranslation("Kiosk ID", "Kiosk-ID");
    addTranslation("Products", "Produkter");
    addTranslation("Amount Total", "Totalt belopp");
    addTranslation("Order Date", "Beställningsdatum");
    addTranslation("pending", "väntande");
    addTranslation("PAID", "Betald");

    // Edit Product vocabulary
    addTranslation("Product Editor:", "Produktredigerare:");
    addTranslation("Search by name...", "Sök efter namn...");
    addTranslation("Search by max price...", "Sök efter maxpris...");
    addTranslation("Search Ingredients", "Sök ingredienser");
    addTranslation("Preparation Time", "Tillagningstid");

    // Timer Editor
    addTranslation("Timer Editor:", "Timerredigerare");
    addTranslation("Timer:", "Tidtagare:");
    addTranslation("Current inactivity timer: ", "Nuvarande inaktivitetstimer: ");
    addTranslation(" seconds", " sekunder");
    // addTranslation("New timer value (in seconds)", "Nytt timervärde (i
    // sekunder)");
    addTranslation("Update Timer", "Uppdatera Timer");
    addTranslation("Please enter a value >= 5 seconds.", "Vänligen ange ett värde >= 5 sekunder.");
    addTranslation("Timer updated successfully!", "Timern har uppdaterats!");
    addTranslation(
        "Invalid input! Please enter a number.", "Ogiltig inmatning! Vänligen ange ett nummer.");
    addTranslation("Popup Timer:", "Timer för Popup:");
    addTranslation("Are you still there?", "Är du fortfarande där?");
    addTranslation("Current", "Nuvarande");
    addTranslation("inactivity", "inaktivitet");
    addTranslation("timer", "timer");
    addTranslation("seconds", "sekunder");
    addTranslation("New", "Nytt");
    addTranslation("value", "värde");
    addTranslation("in", "i");

    // Delete/Edit vocabulary
    addTranslation("Edit Product Data", "Redigera produktdata");

    addTranslation("Numbers of Orders", "Antal beställningar");
    addTranslation("Hour of Day", "Timme på dagen");
    addTranslation("Volume of Orders", "Volym av beställningar");
    addTranslation("Product Revenue Share", "Produktintäktsandel");
    addTranslation("Search", "Sök");
    addTranslation("Product ID", "Produkt-ID");
    addTranslation("Product Name", "Produktnamn");
    addTranslation("Description", "Beskrivning");
    addTranslation("Product Category", "Produktkategori");
    addTranslation("Product Active", "Produkt aktiv");
    addTranslation("Product Price", "Produktpris");
    addTranslation("BURGERS", "BURGARE");
    addTranslation("SIDES", "SIDOR");
    addTranslation("DRINKS", "DRINKAR");
    addTranslation("DESSERTS", "DESSERTER");
    addTranslation("Product Deletion:", "Produktborttagning:");
    addTranslation("Yes", "Ja");
    addTranslation("No", "Nej");

    // For the deletion confirmation line
    addTranslation("Product '", "Produkt '");
    addTranslation(" should be deleted?", " ska tas bort?");
    addTranslation("Yes", "Ja");
    addTranslation("No", "Nej");

    // Meal Screen vocabulary
    addTranslation("Pick a Side for your Meal", "Välj ett tillbehör till din måltid");
    addTranslation("Pick a Drink for your Meal", "Välj en dryck till din måltid");

    // Keyboard Keys
    addTranslation("Close", "Stäng");
    addTranslation("Space", "Mellanslag");
    addTranslation("Shift", "Skift");

    // Checkout screen vocabulary
    addTranslation("Enter Promo Code:", "Ange kampanjkod:");
    addTranslation("Apply", "Använd");

  }

  // Adds translations to both the English to Swedish and Swedish to English maps
  private void addTranslation(String english, String swedish) {
    englishToSwedish.put(english, swedish);
    swedishToEnglish.put(swedish, english);
  }

  /**
   * Toggle between English and Swedish.
   */
  public void toggleLanguage() {
    toEnglish = !toEnglish;
  }

  /**
   * Translates a given word based on the current language setting.
   */
  public String translate(String word) {
    if (toEnglish) {
      return englishToSwedish.getOrDefault(word, word);
    } else {
      return swedishToEnglish.getOrDefault(word, word);
    }
  }

  /**
   * Attempts to translate a full sentence; if not found, translates word by
   * word.
   * Handles dynamic sentences like: "Current inactivity timer: 60 seconds"
   * or "New timer value (in seconds)"
   */
  public String smartTranslate(String sentence) {
    String fullTranslation = translate(sentence);
    if (!fullTranslation.equals(sentence)) {
      return fullTranslation;
    }

    String[] words = sentence.split("\\s+");
    StringBuilder result = new StringBuilder();

    for (String word : words) {
      // Detect word boundaries with punctuation (e.g., "Total:", "1.23", "kr.")
      String prefix = "";
      String suffix = "";

      // Extract leading punctuation (e.g., quotes, parentheses)
      while (!word.isEmpty() && !Character.isLetterOrDigit(word.charAt(0))) {
        prefix += word.charAt(0);
        word = word.substring(1);
      }

      // Extract trailing punctuation
      while (!word.isEmpty()
          && !Character.isLetterOrDigit(word.charAt(word.length() - 1))) {
        suffix = word.charAt(word.length() - 1) + suffix;
        word = word.substring(0, word.length() - 1);
      }

      // Now 'word' should be clean — like "Total", "1.23", or "kr"
      String translated = translate(word);
      result.append(prefix).append(translated).append(suffix).append(" ");
    }

    return result.toString().trim();
  }

}
