import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Day21_1 {
    public static void main(String[] args) throws IOException {
        List<String> inputs = Files.readAllLines(new File("./in/day21.in").toPath());

        List<Food> foods = new ArrayList<>();
        for (String input : inputs) {
            String[] ingredientAllergenSplit = input.split(" \\(contains ");
            Set<String> ingredients = new HashSet<>();
            for (String ingredient : ingredientAllergenSplit[0].split(" ")) {
                ingredients.add(ingredient);
            }
            Set<String> allergens = new HashSet<>();
            for (String allergen : ingredientAllergenSplit[1].substring(0, ingredientAllergenSplit[1].length() - 1).split(", ")) {
                allergens.add(allergen);
            }
            foods.add(new Food(ingredients, allergens));
        }

        Map<String, List<Food>> allergenFoodMap = new HashMap<>();
        for (Food food : foods) {
            for (String allergen : food.allergens) {
                if (!allergenFoodMap.containsKey(allergen)) {
                    allergenFoodMap.put(allergen, new ArrayList<>());
                }
                allergenFoodMap.get(allergen).add(food);
            }
        }

        Set<String> alreadyMappedIngredients = new HashSet<>();
        Map<String, String> allergenIngredientMap = new HashMap<>();
        while (allergenIngredientMap.size() != allergenFoodMap.size()) {
            for (Map.Entry<String, List<Food>> entry : allergenFoodMap.entrySet()) {
                Map<String, Integer> ingredientCount = new HashMap<>();
                for (Food food : entry.getValue()) {
                    for (String ingredient : food.ingredients) {
                        if (!alreadyMappedIngredients.contains(ingredient)) {
                            if (!ingredientCount.containsKey(ingredient)) {
                                ingredientCount.put(ingredient, 1);
                            } else {
                                ingredientCount.put(ingredient, ingredientCount.get(ingredient) + 1);
                            }
                        }
                    }
                }
                int onlyOne = 0;
                for (int count : ingredientCount.values()) {
                    if (count == entry.getValue().size()) {
                        onlyOne++;
                    }
                }
                if (onlyOne != 1) {
                    continue;
                }
                for (Map.Entry<String, Integer> count : ingredientCount.entrySet()) {
                    if (count.getValue() == entry.getValue().size()) {
                        allergenIngredientMap.put(entry.getKey(), count.getKey());
                        alreadyMappedIngredients.add(count.getKey());
                        break;
                    }
                }
            }
        }

        int count = 0;
        for (Food food : foods) {
            for (String ingredient : food.ingredients) {
                if (!alreadyMappedIngredients.contains(ingredient)) {
                    count++;
                }
            }
        }

        System.out.println(count);
    }

    private static class Food {
        public final Set<String> ingredients;
        public final Set<String> allergens;

        public Food(Set<String> ingredients, Set<String> allergens) {
            this.ingredients = ingredients;
            this.allergens = allergens;
        }
    }
}
