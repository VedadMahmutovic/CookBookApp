package com.example.cookbook.data;

import android.content.Context;
import android.util.Log;

import com.example.cookbook.model.InstructionStep;
import com.example.cookbook.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class DatabaseInitializer {

    private static final String TAG = "DatabaseInitializer";

    private final RecipeDao recipeDao;
    private final InstructionStepDao instructionStepDao;
    private final Context context;

    public DatabaseInitializer(Context context) {
        this.context = context.getApplicationContext();
        AppDatabase db = AppDatabase.getInstance(this.context);
        this.recipeDao = db.recipeDao();
        this.instructionStepDao = db.instructionStepDao();
    }

    public void insertInitialData() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                AppDatabase.getInstance(context).runInTransaction(() -> {
                    int existingCount = recipeDao.getRecipeCount();
                    if (existingCount > 0) {
                        Log.d(TAG, "Database already contains " + existingCount + " recipes – skipping");
                        return;
                    }

                    Log.d(TAG, "Starting data insertion...");
                    List<Recipe> initialRecipes = createInitialRecipes();

                    for (Recipe recipe : initialRecipes) {
                        long recipeId = recipeDao.insertRecipeReturnId(recipe);
                        if (recipeId == -1) {
                            Log.e(TAG, "Failed to insert recipe: " + recipe.nameEn);
                            continue;
                        }
                        List<InstructionStep> steps = generateStepsForRecipe((int) recipeId, recipe.internalName);
                        instructionStepDao.insertAll(steps);
                    }

                    Log.d(TAG, "Initial data insertion completed.");
                });
            } catch (Exception e) {
                Log.e(TAG, "Error inserting initial data", e);
            }
        });
    }




    private List<Recipe> createInitialRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        recipes.add(new Recipe(
                "Chocolate Cake",
                "cokoladna torta",
                "Flour, sugar, cocoa powder, baking powder, baking soda, salt, milk, vegetable oil, eggs, vanilla extract, boiling water",
                "Brasno, secer, kakao prah, prasak za pecivo, soda bikarbona, so, mlijeko, biljno ulje, jaja, ekstrakt vanilije, kipuca voda",
                400,
                "chocolate_cake.jpg",
                4.9f,
                false,
                0,
                "Cakes & Cupcakes",
                "Torte i mafini",
                "chocolate_cake"
        ));

        recipes.add(new Recipe(
                "Vanilla Sponge Cake",
                "Vanilin biskvit",
                "Unsalted butter, caster sugar, eggs, self-raising flour, vanilla extract, milk",
                "Maslac bez soli, sitni secer, jaja, brasno s praskom za pecivo, ekstrakt vanilije, mlijeko",
                275,
                "vanilla_sponge.jpg",
                4.8f,
                false,
                0,
                "Cakes & Cupcakes",
                "Torte i mafini",
                "vanilla_sponge_cake"
        ));

        recipes.add(new Recipe(
                "Red Velvet Cake",
                "Crvena barsunasta torta",
                "All-purpose flour, granulated sugar, baking soda, salt, cocoa powder, vegetable oil, buttermilk, eggs, red food coloring, vanilla extract, white vinegar, cream cheese, unsalted butter, powdered sugar",
                "Brasno, secer, soda bikarbona, so, kakao prah, biljno ulje, mlacenica, jaja, crvena boja za hranu, ekstrakt vanilije, bijeli ocat, krem sir, maslac bez soli, secer u prahu",
                450,
                "red_velvet.jpg",
                4.9f,
                false,
                0,
                "Cakes & Cupcakes",
                "Torte i mafini",
                "red_velvet_cake"
        ));
        recipes.add(new Recipe(
                "Carrot Cake",
                "Torta od mrkve",
                "Flour, baking soda, salt, ground cinnamon, vegetable oil, buttermilk, granulated sugar, eggs, vanilla extract, grated carrots, chopped pecans, cream cheese, unsalted butter, powdered sugar",
                "Brasno, soda bikarbona, so, mljeveni cimet, biljno ulje, mlacenica, secer, jaja, ekstrakt vanilije, rendana mrkva, sjeckani pekani orasi, krem sir, neslani maslac, secer u prahu",
                400,
                "carrot_cake.jpg",
                4.9f,
                false,
                0,
                "Cakes & Cupcakes", "Torte i mafini",
                "carrot_cake"
        ));

        recipes.add(new Recipe(
                "Cheesecake",
                "cizkejk",
                "Graham cracker crumbs, granulated sugar, unsalted butter, cream cheese, sour cream, vanilla extract, eggs",
                "Mrvice graham keksa, secer, neslani maslac, krem sir, kiselo vrhnje, ekstrakt vanilije, jaja",
                500,
                "cheesecake.jpg",
                4.8f,
                false,
                0,
                "Cakes & Cupcakes", "Torte i mafini",
                "cheesecake"
        ));

        recipes.add(new Recipe(
                "Lemon Drizzle Cake",
                "Limun kolac",
                "Unsalted butter, caster sugar, eggs, self-raising flour, lemon zest, lemon juice",
                "Neslani maslac, sitni secer, jaja, brasno sa praskom za pecivo, narendana kora limuna, sok od limuna",
                400,
                "lemon_drizzle.jpg",
                4.8f,
                false,
                0,
                "Cakes & Cupcakes", "Torte i mafini",
                "lemon_drizzle_cake"
        ));

        recipes.add(new Recipe(
                "Black Forest Cake",
                "svarcvald torta",
                "Flour, cocoa powder, baking powder, baking soda, salt, buttermilk, vegetable oil, eggs, vanilla extract, cherries, whipping cream, powdered sugar, chocolate shavings",
                "Brasno, kakao prah, prasak za pecivo, soda bikarbona, so, mlacenica, biljno ulje, jaja, ekstrakt vanilije, visnje, slatka pavlaka, secer u prahu, cokoladne strugotine",
                550,
                "black_forest.jpg",
                4.9f,
                false,
                0,
                "Cakes & Cupcakes", "Torte i mafini",
                "black_forest_cake"
        ));

        recipes.add(new Recipe(
                "Tres Leches Cake",
                "Trileca",
                "Eggs, granulated sugar, vanilla sugar, flour, baking powder, whole milk, sweetened condensed milk, heavy whipping cream, caramel sauce",
                "Jaja, secer, vanilin secer, brasno, prasak za pecivo, punomasno mlijeko, zasladeno kondenzirano mlijeko, slatka pavlaka, karamel preliv",
                450, "tres_leches.jpg", 4.8f, false, 0,
                "Cakes & Cupcakes", "Torte i mafini", "tres_leches_cake"
        ));

        recipes.add(new Recipe(
                "Angel Food Cake",
                "Andeoski kolac",
                "Cake flour, granulated sugar, egg whites, vanilla extract, cream of tartar, salt",
                "Brasno za torte, secer, bjelanca, ekstrakt vanilije, vinska kiselina, so",
                180, "angel_food.jpg", 4.9f, false, 0,
                "Cakes & Cupcakes", "Torte i mafini", "angel_food_cake"
        ));

        recipes.add(new Recipe(
                "Hummingbird Cake",
                "Torta kolibric",
                "Flour, granulated sugar, baking soda, salt, cinnamon, eggs, vegetable oil, vanilla extract, crushed pineapple, bananas, pecans, cream cheese, butter, powdered sugar",
                "Brasno, secer, soda bikarbona, so, cimet, jaja, biljno ulje, ekstrakt vanilije, zgnjeceni ananas, banane, pekani orasi, krem sir, maslac, secer u prahu",
                550, "hummingbird_cake.jpg", 4.9f, false, 0,
                "Cakes & Cupcakes", "Torte i mafini","hummingbird_cake"
        ));

        recipes.add(new Recipe(
                "Marble Cake",
                "Mramorni kolac",
                "Flour, white sugar, milk, eggs, butter, baking powder, vanilla extract, salt, cocoa powder",
                "Brasno, bijeli secer, mlijeko, jaja, maslac, prasak za pecivo, ekstrakt vanilije, so, kakao prah",
                280, "marble_cake.jpg", 4.5f, false, 0,
                "Cakes & Cupcakes", "Torte i mafini","marble_cake"
        ));

        recipes.add(new Recipe(
                "Vanilla Cupcakes",
                "Vanilin mafini",
                "Flour, granulated sugar, baking powder, salt, buttermilk, water, vegetable oil, egg, vanilla extract",
                "Brasno, secer, prasak za pecivo, so, mlacenica, voda, biljno ulje, jaje, ekstrakt vanilije",
                460, "cupcake.jpg", 4.8f, false, 0,
                "Cakes & Cupcakes", "Torte i mafini","vanilla_cupcakes"
        ));


        recipes.add(new Recipe(
                "Chocolate Chip Cookies",
                "Keksi s komadicima cokolade",
                "Flour, baking soda, salt, unsalted butter, granulated sugar, brown sugar, vanilla extract, eggs, chocolate chips",
                "Brasno, soda bikarbona, so, neslani maslac, secer, smedi secer, ekstrakt vanilije, jaja, komadici cokolade",
                200, "cookies.jpg", 4.8f, false, 0,
                "Cookies", "Keksi","chocolate_chip_cookies"
        ));

        recipes.add(new Recipe(
                "Peanut Butter Cookies",
                "Keksi s kikiriki puterom",
                "Peanut butter, butter, sugar, brown sugar, eggs, milk, vanilla extract, flour, baking powder, salt",
                "Kikiriki puter, maslac, secer, smedi secer, jaja, mlijeko, ekstrakt vanilije, brasno, prasak za pecivo, so",
                190, "peanut_butter_cookie.jpg", 4.7f, false, 0,
                "Cookies", "Keksi","peanut_butter_cookies"
        ));

        recipes.add(new Recipe(
                "Sugar Cookies",
                "secerni keksi",
                "Flour, baking soda, baking powder, butter, sugar, eggs, vanilla extract",
                "Brasno, soda bikarbona, prasak za pecivo, maslac, secer, jaja, ekstrakt vanilije",
                150, "sugar_cookie.jpg", 4.6f, false, 0,
                "Cookies", "Keksi","sugar_cookies"
        ));

        recipes.add(new Recipe(
                "Snickerdoodles",
                "Snickerdoodle keksi",
                "Sugar, butter, eggs, flour, cream of tartar, baking soda, salt, cinnamon",
                "secer, maslac, jaja, brasno, vinska kiselina, soda bikarbona, so, cimet",
                160, "snickerdoodle.jpg", 4.8f, false, 0,
                "Cookies", "Keksi","snickerdoodles"
        ));

        recipes.add(new Recipe(
                "Oatmeal Raisin Cookies",
                "Zobeni keksi s grozdicama",
                "Butter, sugar, brown sugar, eggs, vanilla extract, flour, baking soda, cinnamon, salt, oats, raisins",
                "Maslac, secer, smedi secer, jaja, ekstrakt vanilije, brasno, soda bikarbona, cimet, so, zobene pahuljice, grozdice",
                180, "oatmeal_raisin.jpg", 4.7f, false, 0,
                "Cookies", "Keksi","oatmeal_raisin_cookies"
        ));

        recipes.add(new Recipe(
                "Double Chocolate Cookies",
                "Dupli cokoladni keksi",
                "Butter, white sugar, brown sugar, eggs, vanilla, flour, cocoa powder, baking soda, salt, chocolate chips",
                "Maslac, bijeli secer, smedi secer, jaja, vanilija, brasno, kakao prah, soda bikarbona, so, komadici cokolade",
                194, "double_chocolate.jpg", 4.7f, false, 0,
                "Cookies", "Keksi","double_chocolate_cookies"
        ));

        recipes.add(new Recipe(
                "Macarons",
                "Makroni",
                "Egg whites, white sugar, confectioners' sugar, ground almonds",
                "Bjelanca, bijeli secer, secer u prahu, mljeveni bademi",
                96, "macarons.jpg", 4.6f, false, 0,
                "Cookies", "Keksi","macarons"
        ));

        recipes.add(new Recipe(
                "Biscotti",
                "Biskoti",
                "White sugar, vegetable oil, eggs, anise extract, flour, baking powder",
                "Bijeli secer, biljno ulje, jaja, ekstrakt anisa, brasno, prasak za pecivo",
                110, "biscotti.jpg", 4.5f, false, 0,
                "Cookies", "Keksi","biscotti"
        ));

        recipes.add(new Recipe(
                "Shortbread Cookies",
                "Prhki keksi",
                "Butter, powdered sugar, vanilla extract, flour",
                "Maslac, secer u prahu, ekstrakt vanilije, brasno",
                150, "shortbread.jpg", 4.8f, false, 0,
                "Cookies", "Keksi","shortbread_cookies"
        ));



        recipes.add(new Recipe(
                "Fudgy Brownies",
                "Socni brauniji",
                "Unsalted butter, granulated sugar, eggs, vanilla extract, cocoa powder, flour, salt, baking powder",
                "Neslani maslac, kristal secer, jaja, ekstrakt vanilije, kakao prah, brasno, so, prasak za pecivo",
                257, "brownies.jpg", 4.7f, false, 0,
                "Brownies & Bars", "Brauniji i plocice","fudgy_brownies"
        ));

        recipes.add(new Recipe(
                "Blondies",
                "Blondiji",
                "Unsalted butter, brown sugar, eggs, vanilla extract, flour, baking powder, baking soda, salt",
                "Neslani maslac, smedi secer, jaja, ekstrakt vanilije, brasno, prasak za pecivo, soda bikarbona, so",
                200, "blondies.jpg", 4.8f, false, 0,
                "Brownies & Bars", "Brauniji i plocice","blondies"
        ));

        recipes.add(new Recipe(
                "Lemon Bars",
                "Limun plocice",
                "Unsalted butter, granulated sugar, flour, eggs, lemon juice",
                "Neslani maslac, kristal secer, brasno, jaja, limunov sok",
                220, "lemon_bars.jpg", 4.9f, false, 0,
                "Brownies & Bars", "Brauniji i plocice","lemon_bars"
        ));

        recipes.add(new Recipe(
                "Magic Cookie Bars",
                "carobne plocice",
                "Graham crackers, unsalted butter, condensed milk, chocolate chips, flaked coconut",
                "Graham keks, neslani maslac, kondenzirano mlijeko, komadici cokolade, kokosove mrvice",
                250, "magic_cookie_bar.jpg", 4.8f, false, 0,
                "Brownies & Bars", "Brauniji i plocice","magic_cookie_bars"
        ));

        recipes.add(new Recipe(
                "S’mores Bars",
                "S’mores plocice",
                "Graham crackers, unsalted butter, brown sugar, marshmallows, chocolate chips",
                "Graham keks, neslani maslac, smedi secer, marshmallow, komadici cokolade",
                200, "smores_bar.jpg", 4.6f, false, 0,
                "Brownies & Bars", "Brauniji i plocice","smores_bars"
        ));

        recipes.add(new Recipe(
                "Peanut Butter Bars",
                "Plocice s kikiriki puterom",
                "Peanut butter, unsalted butter, powdered sugar, graham crackers, chocolate chips",
                "Kikiriki puter, neslani maslac, secer u prahu, graham keks, komadici cokolade",
                300, "peanut_butter.jpg", 4.9f, false, 0,
                "Brownies & Bars", "Brauniji i plocice","peanut_butter_bars"
        ));

        recipes.add(new Recipe(
                "Cheesecake Brownies",
                "Brauniji sa sirom",
                "Unsalted butter, granulated sugar, eggs, vanilla extract, cocoa powder, flour, salt, baking powder",
                "Neslani maslac, kristal secer, jaja, ekstrakt vanilije, kakao prah, brasno, so, prasak za pecivo",
                300, "cheesecake_brownie.jpg", 4.8f, false, 0,
                "Brownies & Bars", "Brauniji i plocice","cheesecake_brownies"
        ));

        recipes.add(new Recipe(
                "Pumpkin Bars",
                "Bundeva plocice",
                "Eggs, granulated sugar, vegetable oil, pumpkin puree, flour, baking powder, cinnamon, salt, baking soda",
                "Jaja, kristal secer, biljno ulje, pire od bundeve, brasno, prasak za pecivo, cimet, so, soda bikarbona",
                250, "pumpkin_bar.jpg", 4.9f, false, 0,
                "Brownies & Bars", "Brauniji i plocice","pumpkin_bars"
        ));

        recipes.add(new Recipe(
                "Date Squares",
                "Plocice s hurmama",
                "Chopped dates, water, granulated sugar, vanilla extract, flour, oats, brown sugar, baking soda, salt, unsalted butter",
                "Isjeckane hurme, voda, kristal secer, ekstrakt vanilije, brasno, zobene pahuljice, smedi secer, soda bikarbona, so, neslani maslac",
                220, "date_square.png", 4.7f, false, 0,
                "Brownies & Bars", "Brauniji i plocice","date_squares"
        ));

        recipes.add(new Recipe(
                "Krispie Rice Treats",
                "Hrskave rizine plocice",
                "Unsalted butter, marshmallows, crispy rice cereal",
                "Neslani maslac, marshmallow, hrskave rizine pahuljice",
                65, "krispie_rice_treats.jpg", 4.8f, false, 0,
                "Brownies & Bars", "Brauniji i plocice","krispie_rice_treats"
        ));


        recipes.add(new Recipe(
                "Apple Pie",
                "Pita od jabuka",
                "package pastry, sugar, cinnamon, apples, butter",
                "lisnato tijesto, secer, cimet, jabuke, maslac",
                350, "apple_pie.jpg", 4.5f, false, 0,
                "Pies & Tarts", "Pite i Tartovi","apple_pie"
        ));

        recipes.add(new Recipe(
                "Pecan Pie",
                "Pita sa pekan orasima",
                "pie crust, corn syrup, sugar, eggs, unsalted butter, vanilla extract, pecans",
                "kora za pitu, kukuruzni sirup, secer, jaja, neslani maslac, ekstrakt vanilije, pekan orasi",
                500, "pecan_pie.jpg", 4.8f, false, 0,
                "Pies & Tarts", "Pite i Tartovi","pecan_pie"
        ));

        recipes.add(new Recipe(
                "Pumpkin Pie",
                "Pita od bundeve",
                "pie crust, pumpkin puree, condensed milk, eggs, cinnamon, ginger, nutmeg, salt",
                "kora za pitu, pire od bundeve, kondenzirano mlijeko, jaja, cimet, dumbir, muskatni orascic, so",
                300, "pumpkin_pie.jpg", 4.7f, false, 0,
                "Pies & Tarts", "Pite i Tartovi","pumpkin_pie"
        ));

        recipes.add(new Recipe(
                "Key Lime Pie",
                "Pita od limete",
                "graham cracker, condensed milk, sour cream, key lime juice, lime zest",
                "graham keks, kondenzirano mlijeko, kiselo vrhnje, sok limete, korica limete",
                350, "key_lime.jpg", 4.8f, false, 0,
                "Pies & Tarts", "Pite i Tartovi","key_lime_pie"
        ));

        recipes.add(new Recipe(
                "Cherry Pie",
                "Pita od visanja",
                "pie crust, tart cherries, sugar, cornstarch, lemon juice, almond extract, butter, eggs",
                "kora za pitu, visnje, secer, gustin, limunov sok, ekstrakt badema, maslac, jaja",
                400, "cherry_pie.jpg", 4.6f, false, 0,
                "Pies & Tarts", "Pite i Tartovi","cherry_pie"
        ));

        recipes.add(new Recipe(
                "Lemon Meringue Pie",
                "Pita sa limunom i meringom",
                "pie crust, sugar, flour, cornstarch, salt, water, lemon juice, lemon zest, butter, eggs",
                "kora za pitu, secer, brasno, gustin, so, voda, limunov sok, korica limuna, maslac, jaja",
                320, "lemon_meringue.jpg", 4.8f, false, 0,
                "Pies & Tarts", "Pite i Tartovi","lemon_meringue_pie"
        ));

        recipes.add(new Recipe(
                "Chocolate Cream Pie",
                "Pita sa cokoladom",
                "pie crust, sugar, flour, milk, chocolate, eggs, butter, vanilla extract",
                "kora za pitu, secer, brasno, mlijeko, cokolada, jaja, maslac, ekstrakt vanilije",
                350, "chocolate_cream.jpg", 4.7f, false, 0,
                "Pies & Tarts", "Pite i Tartovi","chocolate_cream_pie"
        ));

        recipes.add(new Recipe(
                "Banana Cream Pie",
                "Pita sa bananama",
                "pie crust, whole milk, sugar, flour, salt, eggs, unsalted butter, vanilla extract, bananas, whipped cream",
                "kora za pitu, punomasno mlijeko, secer, brasno, so, jaja, neslani maslac, ekstrakt vanilije, banane, slag",
                350, "banana_cream.jpg", 4.7f, false, 0,
                "Pies & Tarts", "Pite i Tartovi","banana_cream_pie"
        ));

        recipes.add(new Recipe(
                "Fruit Tart",
                "Vocni tart",
                "flour, sugar, salt, unsalted butter, whole milk, cornstarch, eggs, vanilla extract, fresh fruits",
                "brasno, secer, so, neslani maslac, punomasno mlijeko, gustin, jaja, ekstrakt vanilije, svjeze voce",
                300, "fruit_tart.jpg", 4.8f, false, 0,
                "Pies & Tarts", "Pite i Tartovi","fruit_tart"
        ));


        recipes.add(new Recipe(
                "Cinnamon Rolls",
                "Cimet rolnice",
                "milk, yeast, sugar, unsalted butter, eggs, flour, salt, brown sugar, cinnamon, cream cheese, powdered sugar, vanilla extract",
                "mlijeko, kvasac, secer, neslani maslac, jaja, brasno, so, smedi secer, cimet, krem sir, secer u prahu, ekstrakt vanilije",
                350, "cinnamon_rolls.jpg", 4.9f, false, 0,
                "Pastries & Breads", "Peciva i Hljebovi","cinnamon_rolls"
        ));

        recipes.add(new Recipe(
                "Danish Pastries",
                "Danska peciva",
                "milk, yeast, flour, sugar, salt, eggs, unsalted butter, cream cheese, powdered sugar, vanilla extract",
                "mlijeko, kvasac, brasno, secer, so, jaja, neslani maslac, krem sir, secer u prahu, ekstrakt vanilije",
                300, "danish_pastries.jpg", 4.8f, false, 0,
                "Pastries & Breads", "Peciva i Hljebovi","danish_pastries"
        ));

        recipes.add(new Recipe(
                "Croissants",
                "Kroasani",
                "flour, sugar, yeast, salt, milk, unsalted butter, water, eggs",
                "brasno, secer, kvasac, so, mlijeko, neslani maslac, voda, jaja",
                250, "croissant.jpg", 4.9f, false, 0,
                "Pastries & Breads", "Peciva i Hljebovi","croissants"
        ));

        recipes.add(new Recipe(
                "Sweet Scones",
                "Slatki skonsi",
                "flour, baking powder, salt, sugar, unsalted butter, milk, chocolate chips",
                "brasno, prasak za pecivo, so, secer, neslani maslac, mlijeko, cokoladne kapljice",
                200, "sweet_scone.jpg", 4.7f, false, 0,
                "Pastries & Breads", "Peciva i Hljebovi","sweet_scones"
        ));

        recipes.add(new Recipe(
                "Banana Bread",
                "Hljeb od banane",
                "flour, baking soda, salt, brown sugar, butter, eggs, bananas",
                "brasno, soda bikarbona, so, smedi secer, maslac, jaja, banane",
                210, "banana_bread.jpg", 4.8f, false, 0,
                "Pastries & Breads", "Peciva i Hljebovi","banana_bread"
        ));

        recipes.add(new Recipe(
                "Zucchini Bread",
                "Hljeb od tikvica",
                "eggs, sugar, vegetable oil, zucchini, vanilla extract, flour, baking soda, baking powder, salt, cinnamon, walnuts",
                "jaja, secer, biljno ulje, tikvica, ekstrakt vanilije, brasno, soda bikarbona, prasak za pecivo, so, cimet, orasi",
                190, "zucchini_bread.jpg", 4.7f, false, 0,
                "Pastries & Breads", "Peciva i Hljebovi","zucchini_bread"
        ));

        recipes.add(new Recipe(
                "Monkey Bread",
                "Majmunski hljeb",
                "biscuit dough, sugar, cinnamon, butter, brown sugar",
                "tijesto za biskvit, secer, cimet, maslac, smedi secer",
                300, "monkey_bread.jpg", 4.9f, false, 0,
                "Pastries & Breads", "Peciva i Hljebovi","monkey_bread"
        ));

        recipes.add(new Recipe(
                "Babka",
                "Babka",
                "flour, sugar, yeast, milk, eggs, vanilla extract, unsalted butter, chocolate chips, cocoa powder",
                "brasno, secer, kvasac, mlijeko, jaja, ekstrakt vanilije, neslani maslac, cokoladne kapljice, kakao prah",
                350, "babka.jpg", 4.8f, false, 0,
                "Pastries & Breads", "Peciva i Hljebovi","babka"
        ));

        recipes.add(new Recipe(
                "Apple Turnovers",
                "Pita dzepovi s jabukom",
                "puff pastry, apples, sugar, cinnamon, flour, lemon juice, eggs",
                "lisnato tijesto, jabuke, secer, cimet, brasno, limunov sok, jaja",
                250, "apple_turnovers.jpg", 4.7f, false, 0,
                "Pastries & Breads", "Peciva i Hljebovi","apple_turnovers"
        ));

        recipes.add(new Recipe(
                "Puff Pastry Twists",
                "Uvijeno lisnato pecivo",
                "puff pastry, melted butter, sugar, cinnamon",
                "lisnato tijesto, otopljeni maslac, secer, cimet",
                150, "puff_pastry.jpg", 4.6f, false, 0,
                "Pastries & Breads", "Peciva i Hljebovi","puff_pastry_twists"
        ));


        recipes.add(new Recipe(
                "No-Bake Cheesecake",
                "cizkejk bez pecenja",
                "graham crackers, brown sugar, unsalted butter, cream cheese, powdered sugar, vanilla extract, heavy whipping cream",
                "graham keks, smedi secer, neslani maslac, krem sir, secer u prahu, ekstrakt vanilije, slatka pavlaka",
                350, "no_bake_cheesecake.jpg", 4.9f, false, 0,
                "No-Bake Desserts", "Deserti bez pecenja","no_bake_cheesecake"
        ));

        recipes.add(new Recipe(
                "Chocolate Mousse",
                "cokoladni mus",
                "eggs, dark chocolate, unsalted butter, heavy cream, caster sugar",
                "jaja, tamna cokolada, neslani maslac, slatka pavlaka, sitni secer",
                300, "chocolate_mousse.jpg", 4.8f, false, 0,
                "No-Bake Desserts", "Deserti bez pecenja","chocolate_mousse"
        ));

        recipes.add(new Recipe(
                "Tiramisu",
                "Tiramisu",
                "ladyfingers, coffee, mascarpone cheese, sugar, vanilla extract, heavy cream, cocoa powder",
                "piskote, kafa, mascarpone sir, secer, ekstrakt vanilije, slatka pavlaka, kakao prah",
                400, "tiramisu.jpg", 4.9f, false, 0,
                "No-Bake Desserts", "Deserti bez pecenja","tiramisu"
        ));

        recipes.add(new Recipe(
                "Oreo Truffles",
                "Oreo kuglice",
                "oreos, cream cheese, chocolate",
                "oreo keksi, krem sir, cokolada",
                110, "oreo_truffle.jpg", 4.8f, false, 0,
                "No-Bake Desserts", "Deserti bez pecenja","oreo_truffles"
        ));

        recipes.add(new Recipe(
                "No-Bake Peanut Butter Pie",
                "Pita od kikiriki putera bez pecenja",
                "graham cracker pie crust, creamy peanut butter, cream cheese, powdered sugar",
                "kora od graham keksa, kremasti kikiriki puter, krem sir, secer u prahu",
                450, "no_bake_peanut_butter.jpg", 4.9f, false, 0,
                "No-Bake Desserts", "Deserti bez pecenja","no_bake_peanut_butter_pie"
        ));

        recipes.add(new Recipe(
                "Panna Cotta",
                "Pana kota",
                "skim milk, gelatin, heavy cream, sugar, vanilla extract",
                "obrano mlijeko, zelatin, slatka pavlaka, secer, ekstrakt vanilije",
                300, "panna_cotta.jpg", 4.8f, false, 0,
                "No-Bake Desserts", "Deserti bez pecenja","panna_cotta"
        ));

        recipes.add(new Recipe(
                "Chia Pudding",
                "cija puding",
                "almond milk, chia seeds, maple syrup",
                "bademovo mlijeko, cija sjemenke, javorov sirup",
                200, "chia_pudding.jpg", 4.7f, false, 0,
                "No-Bake Desserts", "Deserti bez pecenja","chia_pudding"
        ));

        recipes.add(new Recipe(
                "Icebox Cake",
                "Kolac iz frizidera",
                "heavy whipping cream, sugar, vanilla extract, chocolate wafer cookies",
                "slatka pavlaka, secer, ekstrakt vanilije, cokoladni keks",
                350, "icebox_cake.jpg", 4.8f, false, 0,
                "No-Bake Desserts", "Deserti bez pecenja","icebox_cake"
        ));

        recipes.add(new Recipe(
                "Energy Bites",
                "Energetske kuglice",
                "oats, shredded coconut, creamy peanut butter, flaxseed, chocolate chips, honey, chia seeds, vanilla extract",
                "zobene pahuljice, rendani kokos, kremasti kikiriki puter, lanene sjemenke, cokoladne kapljice, med, cija sjemenke, ekstrakt vanilije",
                100, "energy_bite.jpg", 4.9f, false, 0,
                "No-Bake Desserts", "Deserti bez pecenja","energy_bites"
        ));


        recipes.add(new Recipe(
                "Ice Cream",
                "Sladoled",
                "heavy whipping cream, condensed milk, vanilla extract",
                "slatka pavlaka, kondenzirano mlijeko, ekstrakt vanilije",
                200, "ice_cream.jpg", 4.8f, false, 0,
                "Frozen Treats", "Zamrznuti deserti","ice_cream"
        ));

        recipes.add(new Recipe(
                "Sorbet",
                "Sorbe",
                "fresh fruit puree, water, sugar, lemon juice",
                "pire od svjezeg voca, voda, secer, sok od limuna",
                150, "sorbet.jpg", 4.7f, false, 0,
                "Frozen Treats", "Zamrznuti deserti","sorbet"
        ));

        recipes.add(new Recipe(
                "Popsicles",
                "Ledenice",
                "fresh fruit, water, sugar",
                "svjeze voce, voda, secer",
                100, "popsicle.jpg", 4.6f, false, 0,
                "Frozen Treats", "Zamrznuti deserti","popsicles"
        ));

        recipes.add(new Recipe(
                "Frozen Yogurt",
                "Zamrznuti jogurt",
                "blueberries, strawberries, raspberries, vanilla yogurt, honey",
                "borovnice, jagode, maline, vanilin jogurt, med",
                140, "frozen_yogurt.jpg", 4.7f, false, 0,
                "Frozen Treats", "Zamrznuti deserti","frozen_yogurt"
        ));

        recipes.add(new Recipe(
                "Ice Cream Sandwiches",
                "Sendvic od sladoleda",
                "chocolate cookies, vanilla ice cream, sprinkles",
                "cokoladni keksi, vanilin sladoled, sarene mrvice",
                460, "ice_cream_sandwich.jpg", 4.8f, false, 0,
                "Frozen Treats", "Zamrznuti deserti","ice_cream_sandwiches"
        ));

        recipes.add(new Recipe(
                "Granita",
                "Granita",
                "fruit juice, water, sugar",
                "vocni sok, voda, secer",
                100, "granita.jpg", 4.6f, false, 0,
                "Frozen Treats", "Zamrznuti deserti","granita"
        ));

        recipes.add(new Recipe(
                "Ice Cream Cake",
                "Torta od sladoleda",
                "oreos, unsalted butter, ice cream, hot fudge, heavy whipping cream, powdered sugar, vanilla extract",
                "oreo keksi, neslani maslac, sladoled, topli cokoladni preliv, slatka pavlaka, secer u prahu, ekstrakt vanilije",
                930, "ice_cream_cake.jpg", 4.8f, false, 0,
                "Frozen Treats", "Zamrznuti deserti","ice_cream_cake"
        ));

        recipes.add(new Recipe(
                "Milkshake",
                "Milksejk",
                "ice cream, whole milk, vanilla extract",
                "sladoled, punomasno mlijeko, ekstrakt vanilije",
                380, "milkshake.jpg", 4.7f, false, 0,
                "Frozen Treats", "Zamrznuti deserti","milkshake"
        ));


        recipes.add(new Recipe(
                "Fudge",
                "Fadz",
                "chocolate chips, condensed milk, vanilla extract",
                "cokoladne mrvice, kondenzirano mlijeko, ekstrakt vanilije",
                140, "fudge.jpg", 4.7f, false, 0,
                "Candy & Confections", "Bombone i slatkisi","fudge"
        ));

        recipes.add(new Recipe(
                "Caramel",
                "Karamel",
                "sugar, unsalted butter, heavy cream",
                "secer, neslani maslac, slatka pavlaka",
                100, "caramel.jpg", 4.8f, false, 0,
                "Candy & Confections", "Bombone i slatkisi","caramel"
        ));

        recipes.add(new Recipe(
                "Toffee",
                "Tofi",
                "unsalted butter, sugar, salt, chocolate chips, almonds",
                "neslani maslac, secer, so, cokoladne mrvice, bademi",
                250, "toffee.jpg", 4.6f, false, 0,
                "Candy & Confections", "Bombone i slatkisi","toffee"
        ));

        recipes.add(new Recipe(
                "Chocolate Bark",
                "cokoladni listici",
                "chocolate, nuts, seeds, dried fruit",
                "cokolada, orasasti plodovi, sjemenke, suho voce",
                150, "chocolate_bark.jpg", 4.8f, false, 0,
                "Candy & Confections", "Bombone i slatkisi","chocolate_bark"
        ));

        recipes.add(new Recipe(
                "Truffles",
                "Trufle",
                "chocolate, heavy cream, unsalted butter, vanilla extract, cocoa powder",
                "cokolada, slatka pavlaka, neslani maslac, ekstrakt vanilije, kakao prah",
                100, "truffles.jpg", 4.9f, false, 0,
                "Candy & Confections", "Bombone i slatkisi","truffles"
        ));

        recipes.add(new Recipe(
                "Peanut Brittle",
                "Kikiriki krokant",
                "sugar, corn syrup, water, salt, peanuts, unsalted butter, baking soda",
                "secer, kukuruzni sirup, voda, so, kikiriki, neslani maslac, soda bikarbona",
                150, "peanut_brittle.jpg", 4.7f, false, 0,
                "Candy & Confections", "Bombone i slatkisi","peanut_brittle"
        ));

        recipes.add(new Recipe(
                "Marshmallows",
                "Marmelade",
                "gelatin, water, sugar, corn syrup, salt, vanilla extract, powdered sugar",
                "zelatin, voda, secer, kukuruzni sirup, so, ekstrakt vanilije, secer u prahu",
                80, "marshamllow.jpg", 4.8f, false, 0,
                "Candy & Confections", "Bombone i slatkisi","marshmallows"
        ));

        recipes.add(new Recipe(
                "Chocolate-Covered Pretzels",
                "cokoladni pereci",
                "mini pretzels, chocolate, vegetable oil",
                "mini pereci, cokolada, biljno ulje",
                50, "choco_pretzel.jpg", 4.6f, false, 0,
                "Candy & Confections", "Bombone i slatkisi","chocolate_covered_pretzels"
        ));

        recipes.add(new Recipe(
                "Candied Nuts",
                "Kandirani orasasti plodovi",
                "nuts, sugar, water, cinnamon, vanilla extract, salt",
                "orasasti plodovi, secer, voda, cimet, ekstrakt vanilije, so",
                210, "candied_nuts.jpg", 4.8f, false, 0,
                "Candy & Confections", "Bombone i slatkisi","candied_nuts"
        ));

        recipes.add(new Recipe(
                "Rock Candy",
                "Kristal bombona",
                "sugar, water, flavoring extract",
                "secer, voda, ekstrakt arome",
                60, "rock_candy.jpg", 4.6f, false, 0,
                "Candy & Confections", "Bombone i slatkisi","rock_candy"
        ));


        recipes.add(new Recipe(
                "Mochi",
                "Mochi",
                "rice flour, sugar, water, starch",
                "rizino brasno, secer, voda, skrob",
                100, "mochi.jpg", 4.7f, false, 0,
                "International Sweets", "Medunarodni slatkisi","mochi"
        ));

        recipes.add(new Recipe(
                "Gulab Jamun",
                "Gulab dzamun",
                "milk powder, flour, baking powder, eggs, heavy cream, ghee, water, sugar, cardamom pods",
                "mlijeko u prahu, brasno, prasak za pecivo, jaja, slatka pavlaka, ghee, voda, secer, mahune kardamoma",
                150, "gulab_jamun.jpg", 4.8f, false, 0,
                "International Sweets", "Medunarodni slatkisi","gulab_jamun"
        ));

        recipes.add(new Recipe(
                "Churros",
                "curosi",
                "water, unsalted butter, sugar, salt, flour, eggs, vanilla extract, vegetable oil, cinnamon",
                "voda, neslani maslac, secer, so, brasno, jaja, ekstrakt vanilije, biljno ulje, cimet",
                120, "churro.jpg", 4.9f, false, 0,
                "International Sweets", "Medunarodni slatkisi","churros"
        ));

        recipes.add(new Recipe(
                "Cannoli",
                "Kanoli",
                "flour, sugar, cinnamon, shortening, marsala wine, water, white vinegar, eggs, oil, ricotta cheese, powdered sugar, vanilla extract, chocolate chips",
                "brasno, secer, cimet, mast, marsala vino, voda, bijeli ocat, jaja, ulje, rikota sir, secer u prahu, ekstrakt vanilije, cokoladne mrvice",
                200, "cannoli.jpg", 4.8f, false, 0,
                "International Sweets", "Medunarodni slatkisi","cannoli"
        ));

        recipes.add(new Recipe(
                "Baklava",
                "Baklava",
                "nuts, phyllo dough, butter, sugar, cinnamon, cloves, water, honey, lemon juice",
                "orasasti plodovi, jufka, maslac, secer, cimet, karanfilic, voda, med, limunov sok",
                290, "baklava.jpg", 4.9f, false, 0,
                "International Sweets", "Medunarodni slatkisi","baklava"
        ));

        recipes.add(new Recipe(
                "Alfajores",
                "Alfahores",
                "flour, cornstarch, baking powder, salt, unsalted butter, powdered sugar, eggs, vanilla extract, lemon zest, dulce de leche, desiccated coconut",
                "brasno, kukuruzni skrob, prasak za pecivo, so, neslani maslac, secer u prahu, jaja, ekstrakt vanilije, limunova korica, dulce de leche, kokosovo brasno",
                240, "alfajores.jpg", 4.8f, false, 0,
                "International Sweets", "Medunarodni slatkisi","alfajores"
        ));

        recipes.add(new Recipe(
                "Brigadeiros",
                "Brigadeiros",
                "condensed milk, cocoa powder, unsalted butter, sprinkles",
                "kondenzirano mlijeko, kakao prah, neslani maslac, mrvice",
                110, "brigadeiros.jpg", 4.7f, false, 0,
                "International Sweets", "Medunarodni slatkisi","brigadeiros"
        ));

        recipes.add(new Recipe(
                "Tteok",
                "Tteok (korejski kolac od rize)",
                "sweet rice flour, sugar, water, vegetable oil, salt, seeds, dried fruit",
                "slatko rizino brasno, secer, voda, biljno ulje, so, sjemenke, suho voce",
                120, "tteok.jpg", 4.6f, false, 0,
                "International Sweets", "Medunarodni slatkisi","tteok"
        ));

        recipes.add(new Recipe(
                "Knafeh",
                "Knafe",
                "phyllo dough, unsalted butter, mozzarella cheese, heavy cream, sugar, water, lemon juice, rose water, pistachios",
                "jufka, neslani maslac, mocarela sir, slatka pavlaka, secer, voda, limunov sok, ruzina vodica, pistacije",
                350, "knafeh.jpg", 4.7f, false, 0,
                "International Sweets", "Medunarodni slatkisi","knafeh"
        ));

        recipes.add(new Recipe(
                "Flan",
                "Flan",
                "sugar, cream cheese, eggs, condensed milk, evaporated milk, vanilla extract",
                "secer, krem sir, jaja, kondenzirano mlijeko, evaporirano mlijeko, ekstrakt vanilije",
                380, "flan.jpg", 4.8f, false, 0,
                "International Sweets", "Medunarodni slatkisi","flan"
        ));


        recipes.add(new Recipe(
                "Fruit Salad",
                "Vocna salata",
                "cantaloupe, honeydew, pineapple, raspberries, blueberries, fruits, fresh mint, lime zest, lime juice, honey",
                "kantalupa, medena dinja, ananas, maline, borovnice, voce, svjeza menta, korica limete, sok limete, med",
                130, "fruit_salad.jpg", 4.6f, false, 0,
                "Fruit-Based Desserts", "Vocni deserti","fruit_salad"
        ));

        recipes.add(new Recipe(
                "Baked Apples",
                "Pecene jabuke",
                "apples, brown sugar, cinnamon, walnuts, butter, water",
                "jabuke, smedi secer, cimet, orasi, maslac, voda",
                220, "baked_apples.jpg", 4.7f, false, 0,
                "Fruit-Based Desserts", "Vocni deserti","baked_apples"
        ));

        recipes.add(new Recipe(
                "Poached Pears",
                "Kruske u sirupu",
                "pears, water, sugar, honey, cinnamon, vanilla extract",
                "kruske, voda, secer, med, cimet, ekstrakt vanilije",
                180, "poached_pears.jpg", 4.5f, false, 0,
                "Fruit-Based Desserts", "Vocni deserti","poached_pears"
        ));

        recipes.add(new Recipe(
                "Grilled Peaches",
                "Grilane breskve",
                "peaches, vegetable oil, ice cream, honey",
                "breskve, biljno ulje, sladoled, med",
                230, "grilled_peaches.jpg", 4.8f, false, 0,
                "Fruit-Based Desserts", "Vocni deserti","grilled_peaches"
        ));

        recipes.add(new Recipe(
                "Berry Parfait",
                "Parfe od bobicastog voca",
                "fresh berries, yogurt, granola",
                "svjeze bobicasto voce, jogurt, granola",
                250, "berry_parfait.jpg", 4.7f, false, 0,
                "Fruit-Based Desserts", "Vocni deserti","berry_parfait"
        ));

        return recipes;
    }


    private List<InstructionStep> generateStepsForRecipe(int recipeId, String recipeInternalName) {
        List<InstructionStep> steps = new ArrayList<>();

        switch (recipeInternalName) {
            case "chocolate_cake":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-2 cups all-purpose flour\n-2 cups granulated sugar\n-¾ cup unsweetened cocoa powder\n-2 tsp baking powder\n-1½ tsp baking soda\n-1 tsp salt\n-1 cup milk\n-½ cup vegetable oil\n-2 eggs\n-2 tsp vanilla extract\n-1 cup boiling water", "Sastojci:\n-2 solje brasna\n-2 solje secera\n-¾ solje kakao praha\n-2 kasicice praska za pecivo\n-1½ kasicica sode bikarbone\n-1 kasicica soli\n-1 solja mlijeka\n-½ solje ulja\n-2 jaja\n-2 kasicice ekstrakta vanilije\n-1 solja kljucale vode", "apple_pie_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C (350°F). Grease and flour two 9-inch pans.", "Zagrij pecnicu na 175°C. Podmazi i pobrasni dva kalupa od 23 cm.", "apple_pie_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Mix flour, sugar, cocoa, baking powder, baking soda, and salt.", "Pomijesaj brasno, secer, kakao, prasak za pecivo, sodu bikarbonu i so.", "apple_pie_step3_crust.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Add milk, oil, eggs, and vanilla; mix well.", "Dodaj mlijeko, ulje, jaja i vaniliju; dobro izmijesaj.", "apple_pie_step4_mix_apples.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Stir in boiling water carefully (batter will be thin).", "Pazljivo umijesaj kljucalu vodu (smjesa ce biti rijetka).", "apple_pie_step5_fill.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Pour batter into pans.", "Izlij smjesu u kalupe.", "apple_pie_step6_cover.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Bake 30–35 minutes or until toothpick comes out clean.", "Peci 30–35 minuta ili dok cackalica ne izade cista.", "apple_pie_step7_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Cool for 10 minutes; remove and cool completely.", "Ohladi 10 minuta, izvadi i ostavi da se potpuno ohladi.", "apple_pie_step7_bake.jpg"));
                break;
            case "vanilla_sponge_cake":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-225g butter\n-225g sugar\n-4 eggs\n-225g self-raising flour\n-2 tsp vanilla\n-2 tbsp milk", "Sastojci:\n-225g maslaca\n-225g secera\n-4 jaja\n-225g samorastuceg brasna\n-2 kasicice vanilije\n-2 kasike mlijeka", "cheesecake_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C. Grease and line two 20cm pans.", "Zagrij pecnicu na 175°C. Podmazi i oblozi dva kalupa od 20cm.", "cheesecake_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Cream butter and sugar until fluffy.", "Umuti maslac i secer dok ne postanu pjenasti.", "cheesecake_step3_crust.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Add eggs one by one with a spoon of flour.", "Dodaj jaja jedno po jedno uz kasiku brasna.", "cheesecake_step4_mix.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Fold in flour, then vanilla and milk. Mix smooth.", "Umijesaj ostatak brasna, pa vaniliju i mlijeko. Umuti glatko.", "cheesecake_step5_combine.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Divide into tins and level tops.", "Podijeli smjesu u kalupe i izravnaj vrhove.", "cheesecake_step6_pour.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Bake 20–25 minutes or until golden.", "Peci 20–25 minuta dok ne porumene.", "cheesecake_step7_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Cool 5 minutes in tins, then turn out.", "Ohladi 5 minuta u kalupima pa izvadi.", "cheesecake_step8_chill.jpg"));
                break;
            case "red_velvet_cake":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-2½ cups flour\n-1½ cups sugar\n-1 tsp baking soda\n-1 tsp salt\n-1 tsp cocoa\n-1½ cups oil\n-1 cup buttermilk\n-2 eggs\n-2 tbsp red color\n-1 tsp vanilla\n-1 tsp vinegar\nFrosting:\n-8 oz cream cheese\n-½ cup butter\n-4 cups powdered sugar\n-1 tsp vanilla", "Sastojci:\n-2½ solje brasna\n-1½ solje secera\n-1 kasicica sode bikarbone\n-1 kasicica soli\n-1 kasicica kakaa\n-1½ solje ulja\n-1 solja mlacenice\n-2 jaja\n-2 kasike crvene boje\n-1 kasicica vanilije\n-1 kasicica sirceta\nPremaz:\n-200g krem sira\n-120g maslaca\n-4 solje secera u prahu\n-1 kasicica vanilije", "chocolate_cake_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C. Grease two 9-inch pans.", "Zagrij pecnicu na 175°C. Podmazi dva kalupa od 23 cm.", "chocolate_cake_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Sift flour, sugar, baking soda, salt, cocoa.", "Prosij brasno, secer, sodu bikarbonu, so i kakao.", "chocolate_cake_step3_mix_dry.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Mix oil, buttermilk, eggs, coloring, vanilla, vinegar.", "Pomijesaj ulje, mlacenicu, jaja, boju, vaniliju i sirce.", "chocolate_cake_step4_add_wet.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Combine wet and dry ingredients until smooth.", "Spoji suhe i mokre sastojke dok ne dobijes glatku smjesu.", "chocolate_cake_step5_add_coffee.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Divide into pans.", "Podijeli smjesu u kalupe.", "chocolate_cake_step6_pour.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Bake 30–35 min or until toothpick comes out clean.", "Peci 30–35 minuta dok cackalica ne izade cista.", "chocolate_cake_step7_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Cool 10 minutes in pans, then remove.", "Ohladi 10 minuta u kalupima pa izvadi.", "chocolate_cake_step7_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "For frosting, beat cheese and butter, add sugar & vanilla.", "Za premaz umuti sir i maslac, dodaj secer i vaniliju.", "chocolate_cake_step7_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 10, "Frost the cooled cakes.", "Premazi ohladene kore.", "chocolate_cake_step7_bake.jpg"));
                break;
            case "carrot_cake":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-2 cups all-purpose flour\n-2 tsp baking soda\n-½ tsp salt\n-1½ tsp cinnamon\n-¾ cup vegetable oil\n-¾ cup buttermilk\n-2 cups sugar\n-3 eggs\n-2 tsp vanilla\n-2 cups grated carrots\n-1 cup chopped pecans\nFrosting:\n-8 oz cream cheese, ¼ cup butter, 2 cups powdered sugar, 1 tsp vanilla", "Sastojci:\n-2 solje brasna\n-2 kasicice sode bikarbone\n-½ kasicice soli\n-1½ kasicice cimeta\n-¾ solje biljnog ulja\n-¾ solje mlacenice\n-2 solje secera\n-3 jaja\n-2 kasicice vanilije\n-2 solje rendane mrkve\n-1 solja sjeckanih oraha\nPremaz:\n-200g krem sira, 60g maslaca, 2 solje secera u prahu, 1 kasicica vanilije", "cookies_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C (350°F). Grease and flour two 9-inch round cake pans.", "Zagrij pecnicu na 175°C. Podmazi i pobrasni dva kalupa od 23 cm.", "cookies_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Whisk flour, baking soda, salt, cinnamon.", "Umuti brasno, sodu bikarbonu, so i cimet.", "cookies_step3_cream_butter.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Mix oil, buttermilk, sugar, eggs, and vanilla.", "Pomijesaj ulje, mlacenicu, secer, jaja i vaniliju.", "cookies_step4_add_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Gradually add dry to wet ingredients, stir until just combined.", "Postepeno dodaj suhe sastojke u mokre, mijesaj dok se sjedine.", "cookies_step5_baking_soda.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Fold in carrots and pecans.", "Umijesaj rendanu mrkvu i orahe.", "cookies_step6_add_chips.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Divide batter into prepared pans.", "Podijeli smjesu u pripremljene kalupe.", "cookies_step7_drop_spoonfuls.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Bake for 30–35 minutes or until toothpick comes out clean.", "Peci 30–35 minuta ili dok cackalica ne izade cista.", "cookies_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Cool in pans for 10 minutes, then remove to cool completely.", "Ohladi u kalupima 10 minuta pa izvadi da se potpuno ohladi.", "cookies_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 10, "Beat cream cheese and butter, add sugar and vanilla until fluffy.", "Umuti krem sir i maslac, dodaj secer i vaniliju dok ne postane pjenasto.", "cookies_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 11, "Frost the cooled cakes.", "Premazi ohladene kore.", "cookies_step8_bake.jpg"));
                break;
            case "cheesecake":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-1½ cups graham cracker crumbs\n-¼ cup sugar\n-½ cup butter, melted\n-900g cream cheese\n-1½ cups sugar\n-¾ cup sour cream\n-1 tsp vanilla\n-4 eggs", "Sastojci:\n-1½ solje mrvica keksa\n-¼ solje secera\n-½ solje rastopljenog maslaca\n-900g krem sira\n-1½ solje secera\n-¾ solje kisele pavlake\n-1 kasicica vanilije\n-4 jaja", "cinnamon_rolls_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 163°C (325°F).", "Zagrij pecnicu na 163°C.", "cinnamon_rolls_step2_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Mix crumbs, sugar, butter; press into 9-inch pan.", "Pomijesaj mrvice, secer i maslac; utisni u kalup od 23 cm.", "cinnamon_rolls_step3_mix_dough.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Beat cream cheese, then add sugar and mix.", "Umuti krem sir pa dodaj secer i izmijesaj.", "cinnamon_rolls_step4_knead_rise.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Add sour cream and vanilla, mix well.", "Dodaj pavlaku i vaniliju, dobro izmijesaj.", "cinnamon_rolls_step5_spread.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Add eggs one at a time, mixing gently.", "Dodaj jaja jedno po jedno, lagano mijesajuci.", "cinnamon_rolls_step6_roll_cut.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Pour filling over crust.", "Izlij fil preko podloge.", "cinnamon_rolls_step7_rise.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Bake 55–70 min until center is nearly set.", "Peci 55–70 minuta dok se sredina skoro ne stisne.", "cinnamon_rolls_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Turn off oven, leave cheesecake 1 hour with door ajar.", "Iskljuci rernu i ostavi kolac jos 1h sa otvorenim vratima.", "cinnamon_rolls_step9_icing.jpg"));
                steps.add(new InstructionStep(recipeId, 10, "Refrigerate at least 4 hours or overnight.", "Ohladi u frizideru najmanje 4 sata ili preko noci.", "cinnamon_rolls_step9_icing.jpg"));
                break;
            case "lemon_drizzle_cake":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-225g butter\n-225g sugar\n-4 eggs\n-225g flour\n-zest of 1 lemon\nDrizzle:\n-juice of 1½ lemons, 85g sugar", "Sastojci:\n-225g maslaca\n-225g secera\n-4 jaja\n-225g brasna\n-kora 1 limuna\nPreliv:\n-sok od 1½ limuna, 85g secera", "brownies_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C. Grease and line loaf tin.", "Zagrij pecnicu na 175°C. Podmazi i oblozi kalup za hljeb.", "brownies_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Cream butter and sugar until pale.", "Umuti maslac i secer dok ne postanu svijetli.", "brownies_step3_melt_butter.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Add eggs one at a time, mix after each.", "Dodaj jaja jedno po jedno, mijesaj nakon svakog.", "brownies_step4_mix.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Fold in flour and lemon zest.", "Umijesaj brasno i limunovu koru.", "brownies_step5_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Pour into tin and level top.", "Izlij u kalup i izravnaj vrh.", "brownies_step5_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Bake 45–50 min or until skewer comes out clean.", "Peci 45–50 minuta dok cackalica ne izade cista.", "brownies_step5_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Mix lemon juice and sugar for drizzle.", "Pomijesaj limunov sok i secer za preliv.", "brownies_step5_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Prick cake and pour drizzle while warm.", "Izbockaj kolac i prelij dok je jos topao.", "brownies_step5_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 10, "Cool in tin before serving.", "Ohladi u kalupu prije posluzivanja.", "brownies_step5_bake.jpg"));
                break;
            case "black_forest_cake":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-1¾ cups flour\n-¾ cup cocoa powder\n-2 tsp baking powder\n-½ tsp baking soda\n-½ tsp salt\n-1 cup buttermilk\n-½ cup oil\n-2 eggs\n-2 tsp vanilla\n-1 cup boiling water\nFilling/Topping:\n-½ kg visanja u kirsu, 2 solje slatke pavlake, ¼ solje secera u prahu, cokoladne mrvice", "Sastojci:\n-1¾ solje brasna\n-¾ solje kakaa\n-2 kasicice praska za pecivo\n-½ kasicice sode bikarbone\n-½ kasicice soli\n-1 solja mlacenice\n-½ solje ulja\n-2 jaja\n-2 kasicice vanilije\n-1 solja kipuce vode\nPunjenje:\n-½ kg visanja u kirsu, 2 solje slatke pavlake, ¼ solje secera u prahu, cokoladne mrvice", "macarons_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C. Grease and line two 23cm pans.", "Zagrij pecnicu na 175°C. Podmazi i oblozi dva kalupa od 23 cm.", "macarons_step2_line_sheets.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Sift flour, cocoa, baking powder, baking soda, salt.", "Prosij brasno, kakao, prasak za pecivo, sodu i so.", "macarons_step3_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Whisk buttermilk, oil, eggs, vanilla.", "Umuti mlacenicu, ulje, jaja i vaniliju.", "macarons_step4_beat_whites.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Combine wet and dry, stir in boiling water.", "Pomijesaj mokre i suhe sastojke, dodaj kipucu vodu.", "macarons_step5_add_sugar.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Pour into pans, bake 30–35 min.", "Raspodijeli u kalupe, peci 30–35 min.", "macarons_step6_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Cool completely.", "Ohladi potpuno.", "macarons_step7_pipe.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Whip cream with sugar until stiff.", "Umuti pavlaku sa secerom dok ne postane cvrsta.", "macarons_step8_rest.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Layer cake, top with visnje i slag.", "Stavi koru, dodaj visnje i slag.", "macarons_step9_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 10, "Add second layer, dekorisi cokoladom.", "Dodaj drugu koru, dekorisi cokoladnim mrvicama.", "macarons_step10_fill.jpg"));
                break;
            case "tres_leches_cake":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-6 eggs, separated\n-½ cup sugar\n-1 tbsp vanilla sugar\n-1½ cups flour\n-1 tsp baking powder\nMilk mix:\n-3 cups milk, 1 can condensed milk, 1 pint cream\nTopping: caramel", "Sastojci:\n-6 jaja (odvojeno bjelanca i zumanca)\n-½ solje secera\n-1 kasika vanilin secera\n-1½ solje brasna\n-1 kasicica praska za pecivo\nMlijeko:\n-3 solje mlijeka, 1 konzerva kondenzovanog, 500ml pavlake\nPreliv: karamel", "red_velvet_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C. Grease 9x13” dish.", "Zagrij pecnicu na 175°C. Podmazi pleh 23x33cm.", "red_velvet_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Beat whites to stiff peaks. In another bowl, beat yolks + sugar.", "Umuti bjelanca u cvrst snijeg. U drugoj posudi, zumanca sa secerom.", "red_velvet_step3_cream.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Fold yolks into whites.", "Lagano sjedini zumanca s bjelancima.", "red_velvet_step4_color.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Sift in flour and baking powder. Fold gently.", "Dodaj brasno i prasak, lagano promijesaj.", "red_velvet_step5_add_flour_buttermilk.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Pour batter in dish, bake 25–30 min.", "Izlij u pleh, peci 25–30 minuta.", "red_velvet_step6_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Cool cake slightly. Mix all milks.", "Ohladi kolac, pomijesaj mlijeka.", "red_velvet_step7_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Poke holes in cake, pour milk.", "Izbockaj kolac, prelij mlijeko.", "red_velvet_step8_frost.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Refrigerate for few hours or overnight.", "Hladiti nekoliko sati ili preko noci.", "red_velvet_step8_frost.jpg"));
                steps.add(new InstructionStep(recipeId, 10, "Drizzle caramel before serving.", "Pre serviranja prelij karamelom.", "red_velvet_step8_frost.jpg"));
                break;
            case "angel_food_cake":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-1 cup cake flour\n-1½ cups sugar (divided)\n-12 egg whites\n-1½ tsp vanilla\n-1½ tsp cream of tartar\n-½ tsp salt", "Sastojci:\n-1 solja specijalnog brasna\n-1½ solje secera (podijeljeno)\n-12 bjelanaca\n-1½ kasicica vanilije\n-1½ kasicica vinske kiseline\n-½ kasicice soli", "tiramisu_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C.", "Zagrij pecnicu na 175°C.", "tiramisu_step2_cook_yolks.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Sift cake flour and ¾ cup sugar.", "Prosij brasno i ¾ solje secera.", "tiramisu_step3_beat_cream.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Beat whites until foamy. Add cream of tartar and salt. Beat to soft peaks.", "Umuti bjelanca u pjenu, dodaj vinsku kiselinu i so, mutiti do mekih vrhova.", "tiramisu_step4_mascarpone.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Gradually add sugar. Beat to stiff peaks.", "Postepeno dodaj preostali secer. Mutiti dok se ne formiraju cvrsti vrhovi.", "tiramisu_step5_dip_ladyfingers.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Fold in vanilla.", "Dodaj vaniliju i lagano promijesaj.", "tiramisu_step6_layer.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Fold in flour mixture gently.", "Lagano umijesaj brasno.", "tiramisu_step7_chill.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Pour into ungreased tube pan.", "Izlij u neopran kalup s rupom u sredini.", "tiramisu_step7_chill.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Bake 40–45 min until top is golden and springs back.", "Peci 40–45 minuta dok povrsina ne postane zlatna i elasticna.", "tiramisu_step7_chill.jpg"));
                steps.add(new InstructionStep(recipeId, 10, "Invert and cool completely before removing.", "Prevrni i ohladi prije vadenja iz kalupa.", "tiramisu_step7_chill.jpg"));
                break;
            case "hummingbird_cake":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-3 cups flour\n-2 cups sugar\n-1 tsp baking soda\n-1 tsp salt\n-1 tsp cinnamon\n-3 eggs\n-1½ cups oil\n-1½ tsp vanilla\n-8 oz crushed pineapple\n-2 cups bananas\n-1 cup pecans\nFrosting: 8 oz cream cheese, ½ cup butter, 4 cups powdered sugar, 1 tsp vanilla",
                        "Sastojci:\n-3 solje brasna\n-2 solje secera\n-1 kasicica sode bikarbone\n-1 kasicica soli\n-1 kasicica cimeta\n-3 jaja\n-1½ solja ulja\n-1½ kasicica vanilije\n-8 oz ananasa\n-2 solje banana\n-1 solja oraha\nPremaz: 225g krem sira, 115g maslaca, 400g secera u prahu, 1 kasicica vanilije",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C. Grease 3 round pans.", "Zagrij pecnicu na 175°C. Podmazi 3 kalupa.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Whisk flour, sugar, baking soda, salt, and cinnamon.", "Pomijesaj brasno, secer, sodu, so i cimet.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Add eggs, oil, and vanilla. Mix well.", "Dodaj jaja, ulje i vaniliju. Dobro izmijesaj.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Stir in pineapple, bananas, and pecans.", "Dodaj ananas, banane i orahe. Umijesaj.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Divide batter into pans.", "Raspodijeli smjesu u kalupe.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Bake 25–30 min until toothpick comes out clean.", "Peci 25–30 min dok cackalica ne izade cista.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Cool 10 minutes, then remove from pans.", "Ohladi 10 min, zatim izvadi iz kalupa.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Beat frosting ingredients until fluffy.", "Izmiksaj krem sir, maslac, secer i vaniliju.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 10, "Assemble and frost layers.", "Slozi kore i premazi glazurom.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "marble_cake":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-2 cups flour\n-1 cup sugar\n-1 cup milk\n-2 eggs\n-½ cup butter\n-2 tsp baking powder\n-1 tsp vanilla\n-½ tsp salt\n-2 tbsp cocoa",
                        "Sastojci:\n-2 solje brasna\n-1 solja secera\n-1 solja mlijeka\n-2 jaja\n-115g maslaca\n-2 kasicice praska za pecivo\n-1 kasicica vanilije\n-½ kasicice soli\n-2 kasike kakaa",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C. Grease and flour a round pan.", "Zagrij pecnicu na 175°C. Podmazi i pobrasni kalup.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Cream butter and sugar until fluffy.", "Umuti maslac i secer dok ne postanu pjenasti.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Add eggs one at a time, beating well.", "Dodaj jaja jedno po jedno i dobro umutiti.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Stir in vanilla extract.", "Umijesaj ekstrakt vanilije.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Combine flour, baking powder, and salt.", "Pomijesaj brasno, prasak za pecivo i so.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Add dry mix alternately with milk.", "Dodaj naizmjenicno suhu smjesu i mlijeko.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Divide batter. Add cocoa to one part.", "Podijeli smjesu, pa dodaj kakao u jedan dio.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Layer both batters in pan and swirl.", "Ulij obje smjese i promijesaj viljuskom.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 10, "Bake 30–35 min or until done.", "Peci 30–35 minuta dok ne bude peceno.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 11, "Cool 10 min, then remove.", "Ohladi 10 minuta, zatim izvadi iz kalupa.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "vanilla_cupcakes":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-1¼ cups flour\n-1 cup sugar\n-1½ tsp baking powder\n-½ tsp salt\n-½ cup buttermilk\n-½ cup water\n-¼ cup oil\n-1 egg\n-1 tbsp vanilla",
                        "Sastojci:\n-1¼ solja brasna\n-1 solja secera\n-1½ kasicica praska za pecivo\n-½ kasicica soli\n-½ solja mlacenice\n-½ solja vode\n-¼ solja ulja\n-1 jaje\n-1 kasika vanilije",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C. Line 12 muffin cups.", "Zagrij pecnicu na 175°C. Pripremi 12 kalupa za mafine.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Whisk flour, sugar, baking powder, salt.", "Pomijesaj brasno, secer, prasak i so.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Add buttermilk, water, oil, egg, and vanilla.", "Dodaj mlacenicu, vodu, ulje, jaje i vaniliju.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Mix all until smooth.", "Sve izmijesaj dok ne postane glatko.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Fill liners ¾ full with batter.", "Napuni korpice do ¾ visine.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Bake 18–20 min or until toothpick comes out clean.", "Peci 18–20 minuta dok cackalica ne bude cista.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Cool 5 min in pan, then transfer to rack.", "Ohladi 5 minuta, zatim prebaci na resetku.", "vanilla_sponge_step8_bake.jpg"));
                break;


            //COOKIES
            case "chocolate_chip_cookies":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-2¼ cups flour\n-1 tsp baking soda\n-1 tsp salt\n-1 cup butter\n-¾ cup white sugar\n-¾ cup brown sugar\n-1 tsp vanilla\n-2 eggs\n-2 cups chocolate chips",
                        "Sastojci:\n-2¼ solje brasna\n-1 kasicica sode\n-1 kasicica soli\n-1 solja maslaca\n-¾ solje secera\n-¾ solje smedeg secera\n-1 kasicica vanilije\n-2 jaja\n-2 solje cokoladnih komadica",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 190°C.", "Zagrij pecnicu na 190°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Mix flour, baking soda, and salt.", "Pomijesaj brasno, sodu i so.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Cream butter, sugars, and vanilla until fluffy.", "Izmiksaj maslac, secere i vaniliju dok ne postane pjenasto.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Add eggs one at a time, beating well.", "Dodaj jaja jedno po jedno, dobro muteci.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Gradually beat in flour mixture.", "Postepeno dodaj smjesu brasna.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Stir in chocolate chips.", "Umijesaj cokoladne komadice.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Drop dough by spoon onto baking sheet.", "Stavljaj po kasiku tijesta na lim.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Bake 9–11 minutes until golden.", "Peci 9–11 minuta dok ne porumene.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 10, "Cool 2 min on sheet, then transfer.", "Ohladi 2 min pa prebaci na resetku.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "peanut_butter_cookies":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-1 cup peanut butter\n-½ cup butter\n-½ cup white sugar\n-½ cup brown sugar\n-1 egg\n-3 tbsp milk\n-1 tsp vanilla\n-1¼ cups flour\n-¾ tsp baking powder\n-¼ tsp salt",
                        "Sastojci:\n-1 solja putera od kikirikija\n-½ solje maslaca\n-½ solje secera\n-½ solje smedeg secera\n-1 jaje\n-3 kasike mlijeka\n-1 kasicica vanilije\n-1¼ solje brasna\n-¾ kasicice praska za pecivo\n-¼ kasicice soli",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 190°C.", "Zagrij pecnicu na 190°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Cream peanut butter, butter, and sugars.", "Umuti puter od kikirikija, maslac i secere.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Beat in egg, milk, and vanilla.", "Dodaj jaje, mlijeko i vaniliju, pa izmiksaj.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Mix in dry ingredients.", "Dodaj suhe sastojke i sjedini.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Roll into balls and place on baking sheet.", "Oblikuj kuglice i stavi na lim.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Flatten with a fork in crisscross pattern.", "Pritisni viljuskom u obliku mreze.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Bake 10 minutes until lightly browned.", "Peci 10 minuta dok ne porumene.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Cool 2 min, then transfer.", "Ohladi 2 min pa prebaci na resetku.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "sugar_cookies":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-2¾ cups flour\n-1 tsp baking soda\n-½ tsp baking powder\n-1 cup butter\n-1½ cups sugar\n-1 egg\n-1 tsp vanilla",
                        "Sastojci:\n-2¾ solje brasna\n-1 kasicica sode\n-½ kasicice praska za pecivo\n-1 solja maslaca\n-1½ solje secera\n-1 jaje\n-1 kasicica vanilije",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 190°C.", "Zagrij pecnicu na 190°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Mix flour, baking soda, and powder.", "Pomijesaj brasno, sodu i prasak za pecivo.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Cream butter and sugar.", "Umuti maslac i secer.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Beat in egg and vanilla.", "Dodaj jaje i vaniliju, pa izmiksaj.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Blend in dry ingredients.", "Umijesaj suhe sastojke.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Roll into balls and place on sheet.", "Oblikuj kuglice i stavi na lim.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Flatten with glass, sprinkle sugar.", "Pritisni casom i pospi secerom po zelji.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Bake 8–10 minutes until golden.", "Peci 8–10 minuta dok ne porumene.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 10, "Let cool completely.", "Ohladi u potpunosti.", "vanilla_sponge_step5_fold.jpg"));
                break;
            case "snickerdoodles":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-1½ cups sugar\n-1 cup butter\n-2 eggs\n-2¾ cups flour\n-2 tsp cream of tartar\n-1 tsp baking soda\n-¼ tsp salt\nCoating:\n-2 tbsp sugar, 2 tsp cinnamon",
                        "Sastojci:\n-1½ solje secera\n-1 solja maslaca\n-2 jaja\n-2¾ solje brasna\n-2 kasicice kiselog praska\n-1 kasicica sode\n-¼ kasicice soli\nPremaz:\n-2 kasike secera, 2 kasicice cimeta",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 200°C.", "Zagrij pecnicu na 200°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Cream butter and 1½ cups sugar.", "Umuti maslac i 1½ solje secera.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Add eggs one at a time.", "Dodaj jaja jedno po jedno.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Mix in flour, cream of tartar, baking soda, and salt.", "Dodaj brasno, kiseli prasak, sodu i so.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Mix sugar and cinnamon in a small bowl.", "Pomijesaj secer i cimet u zdjeli.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Roll dough into balls and coat with sugar mix.", "Oblikuj kuglice i uvaljaj u smjesu secera.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Place 5 cm apart on baking sheet.", "Poredaj kolacice s razmakom od 5 cm.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Bake 8–10 minutes until set.", "Peci 8–10 minuta dok ne ocvrsnu.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 10, "Cool cookies immediately after baking.", "Odmah ih izvadi i ostavi da se hlade.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "oatmeal_raisin_cookies":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-¾ cup butter\n-¾ cup sugar\n-¾ cup brown sugar\n-2 eggs\n-1 tsp vanilla\n-1¼ cups flour\n-1 tsp baking soda\n-¾ tsp cinnamon\n-½ tsp salt\n-2¾ cups oats\n-1 cup raisins",
                        "Sastojci:\n-¾ solje maslaca\n-¾ solje secera\n-¾ solje smedeg secera\n-2 jaja\n-1 kasicica vanilije\n-1¼ solje brasna\n-1 kasicica sode\n-¾ kasicice cimeta\n-½ kasicice soli\n-2¾ solje ovsenih pahuljica\n-1 solja grozdica",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 190°C.", "Zagrij pecnicu na 190°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Cream butter, white and brown sugar.", "Umuti maslac, bijeli i smedi secer.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Add eggs one at a time, stir in vanilla.", "Dodaj jaja jedno po jedno, zatim vaniliju.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Combine flour, soda, cinnamon, and salt; mix in.", "Pomijesaj brasno, sodu, cimet i so, pa dodaj.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Stir in oats and raisins.", "Umijesaj zobene pahuljice i grozdice.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Drop by spoonfuls on cookie sheet.", "Stavljaj po kasiku tijesta na lim.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Bake 8–10 minutes until golden.", "Peci 8–10 minuta dok ne porumene.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Let cookies cool.", "Ohladi kolacice.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "double_chocolate_cookies":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-1 cup butter\n-1 cup sugar\n-1 cup brown sugar\n-2 eggs\n-2 tsp vanilla\n-2 cups flour\n-¾ cup cocoa powder\n-1 tsp baking soda\n-½ tsp salt\n-2 cups chocolate chips",
                        "Sastojci:\n-1 solja maslaca\n-1 solja secera\n-1 solja smedeg secera\n-2 jaja\n-2 kasicice vanilije\n-2 solje brasna\n-¾ solje kakaa\n-1 kasicica sode\n-½ kasicice soli\n-2 solje cokoladnih komadica",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C.", "Zagrij pecnicu na 175°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Cream butter, white and brown sugar.", "Umuti maslac, bijeli i smedi secer.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Add eggs one by one, then vanilla.", "Dodaj jaja jedno po jedno, zatim vaniliju.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Mix in flour, cocoa, soda, and salt.", "Dodaj brasno, kakao, sodu i so.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Stir in chocolate chips.", "Umijesaj cokoladne komadice.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Drop dough on baking sheet by tablespoon.", "Stavljaj tijesto po kasiku na lim.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Bake 8–10 min until edges are set.", "Peci 8–10 minuta dok se ivice ne stegnu.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Cool 5 min on tray, then transfer.", "Ohladi 5 minuta pa prebaci na resetku.", "vanilla_sponge_step6_heat_milk.jpg"));
                break;
            case "macarons":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-3 egg whites\n-¼ cup sugar\n-1⅔ cups powdered sugar\n-1 cup ground almonds",
                        "Sastojci:\n-3 bjelanca\n-¼ solje secera\n-1⅔ solje secera u prahu\n-1 solja mljevenih badema",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 150°C.", "Zagrij pecnicu na 150°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Beat whites until foamy; add sugar gradually to stiff peaks.", "Mutite bjelanca do pjene, dodajte secer postepeno dok se ne formiraju cvrsti vrhovi.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Sift sugar and almonds; fold into whites until lava-like.", "Prosij secer u prahu i bademe; lagano umijesaj u bjelanca dok ne dobijete teksturu lave.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Pipe onto parchment in small circles.", "Iscijedi male krugove na papir za pecenje.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Tap tray to release air bubbles.", "Lagano udari pleh da izadu mjehurici zraka.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Rest 30–60 min until skin forms.", "Pusti da odstoji 30–60 minuta dok se ne stvori korica.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Bake 15–20 minutes.", "Peci 15–20 minuta.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Cool fully before filling.", "Ohladi u potpunosti prije filovanja.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "biscotti":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-1 cup sugar\n-½ cup oil\n-3 eggs\n-1 tbsp anise extract\n-3¼ cups flour\n-1 tbsp baking powder",
                        "Sastojci:\n-1 solja secera\n-½ solje ulja\n-3 jaja\n-1 kasika anis ekstrakta\n-3¼ solje brasna\n-1 kasika praska za pecivo",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 190°C.", "Zagrij pecnicu na 190°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Mix sugar, oil, eggs, and anise.", "Pomijesaj secer, ulje, jaja i anis.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Stir in flour and baking powder to form dough.", "Dodaj brasno i prasak za pecivo dok ne dobijes tijesto.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Shape dough into 2 logs (30×5cm).", "Oblikuj tijesto u 2 valjka (30x5cm).", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Place on parchment-lined baking sheet.", "Stavi ih na papir za pecenje.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Bake 25–30 minutes until golden.", "Peci 25–30 minuta dok ne porumene.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Cool 10 minutes; slice diagonally.", "Pusti da se hladi 10 minuta, zatim isijeci dijagonalno.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Bake slices cut-side down for 10–15 min.", "Peci kriske polozene na bok jos 10–15 minuta.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 10, "Cool completely.", "Ohladi u potpunosti.", "vanilla_sponge_step7_add_milk.jpg"));
                break;
            case "shortbread_cookies":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-1 cup butter\n-¾ cup powdered sugar\n-½ tsp vanilla\n-2 cups flour",
                        "Sastojci:\n-1 solja maslaca\n-¾ solje secera u prahu\n-½ kasicice vanilije\n-2 solje brasna",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C.", "Zagrij pecnicu na 175°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Cream butter and sugar until smooth.", "Umuti maslac i secer dok ne bude glatko.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Stir in vanilla.", "Dodaj vaniliju i promijesaj.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Add flour until dough forms.", "Dodaj brasno dok ne dobijes tijesto.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Roll into 1-inch balls, place on tray.", "Oblikuj kuglice i poredaj na pleh.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Flatten with glass bottom.", "Pritisni svaku casom da se spljosti.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Bake 12–15 minutes until golden edges.", "Peci 12–15 minuta dok ivice ne porumene.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Cool 5 minutes, then transfer.", "Ohladi 5 minuta, pa prebaci na resetku.", "vanilla_sponge_step8_bake.jpg"));
                break;

            //BROWNIES & BARS
            case "fudgy_brownies":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-½ cup butter\n-1 cup sugar\n-2 eggs\n-1 tsp vanilla\n-⅓ cup cocoa\n-½ cup flour\n-¼ tsp salt\n-¼ tsp baking powder",
                        "Sastojci:\n-115g maslaca\n-200g secera\n-2 jaja\n-1 kasicica vanilije\n-40g kakaa\n-65g brasna\n-¼ kasicice soli\n-¼ kasicice praska za pecivo",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C.", "Zagrij pecnicu na 175°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Melt butter in saucepan.", "Otopi maslac u serpi.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Stir in sugar, eggs, vanilla.", "Dodaj secer, jaja i vaniliju, pa promijesaj.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Add cocoa, flour, salt, baking powder.", "Dodaj kakao, brasno, so i prasak za pecivo.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Spread batter in greased 8-inch pan.", "Sipaj smjesu u podmazan kalup 20x20cm.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Bake for 25–30 minutes.", "Peci 25–30 minuta.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Cool and cut into squares.", "Ohladi i isijeci na kocke.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "blondies":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-1 cup melted butter\n-2 cups brown sugar\n-2 eggs\n-2 tsp vanilla\n-2 cups flour\n-1 tsp baking powder\n-¼ tsp baking soda\n-½ tsp salt\n-1 cup chocolate chips or nuts (optional)",
                        "Sastojci:\n-226g otopljenog maslaca\n-400g smedeg secera\n-2 jaja\n-2 kasicice vanilije\n-250g brasna\n-1 kasicica praska za pecivo\n-¼ kasicice sode\n-½ kasicice soli\n-170g cokoladnih kapljica ili oraha (opcionalno)",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C.", "Zagrij pecnicu na 175°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Grease a 9x13-inch pan.", "Podmazi kalup 23x33cm.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Mix melted butter and sugar.", "Pomijesaj otopljeni maslac i secer.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Beat in eggs and vanilla.", "Dodaj jaja i vaniliju, umuti.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Whisk flour, baking powder, soda, salt.", "Izmijesaj brasno, prasak, sodu i so.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Add dry to wet ingredients.", "Dodaj suhe sastojke mokrim i promijesaj.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Fold in chips or nuts.", "Umijesaj cokoladu ili orahe.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Spread into pan.", "Sipaj smjesu u kalup.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 10, "Bake 25–30 minutes.", "Peci 25–30 minuta.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 11, "Cool and cut into squares.", "Ohladi i isijeci na kocke.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "lemon_bars":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\nCrust:\n-1 cup butter\n-½ cup sugar\n-2 cups flour\nFilling:\n-4 eggs\n-1½ cups sugar\n-¼ cup flour\n-2/3 cup lemon juice",
                        "Sastojci:\nKora:\n-226g maslaca\n-100g secera\n-250g brasna\nNadjev:\n-4 jaja\n-300g secera\n-30g brasna\n-160ml limunovog soka",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C.", "Zagrij pecnicu na 175°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Cream butter and sugar for crust.", "Umuti maslac i secer za koru.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Add flour; mix until combined.", "Dodaj brasno i pomijesaj.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Press into greased 9x13 pan.", "Utisni tijesto u kalup 23x33cm.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Bake crust 15–20 min.", "Peci koru 15–20 minuta.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Whisk eggs, sugar, flour, juice.", "Izmijesaj jaja, secer, brasno i limun.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Pour filling over crust.", "Prelij nadjev preko kore.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Bake 20–25 minutes.", "Peci jos 20–25 minuta.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 10, "Cool and dust with sugar.", "Ohladi i pospi secerom u prahu.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "magic_cookie_bars":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-1½ cups graham cracker crumbs\n-½ cup melted butter\n-1 can sweetened condensed milk\n-2 cups chocolate chips\n-1⅓ cups coconut\n-1 cup chopped nuts (optional)",
                        "Sastojci:\n-150g mrvica od keksa\n-115g otopljenog maslaca\n-1 konzerva kondenziranog mlijeka\n-340g cokoladnih kapljica\n-100g kokosa\n-120g sjeckanih oraha (opcionalno)",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C.", "Zagrij pecnicu na 175°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Mix crumbs and butter; press into 9x13 pan.", "Pomijesaj mrvice i maslac; utisni u kalup 23x33cm.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Pour milk evenly over crust.", "Prelij kondenzirano mlijeko preko kore.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Layer chips, coconut, and nuts.", "Pospi cokoladu, kokos i orahe.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Press down firmly with fork.", "cvrsto pritisni viljuskom.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Bake 25–30 min until golden.", "Peci 25–30 min dok ne porumeni.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Cool before cutting into bars.", "Ohladi prije sjecenja na stanglice.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "smores_bars":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-9 graham crackers\n-½ cup melted butter\n-½ cup brown sugar\n-2 cups mini marshmallows\n-1½ cups chocolate chips",
                        "Sastojci:\n-9 keksa od graham brasna\n-115g otopljenog maslaca\n-100g smedeg secera\n-100g mini marshmallows\n-270g cokoladnih kapljica",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C.", "Zagrij pecnicu na 175°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Crush graham crackers.", "Izmrvi kekse.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Mix crumbs, butter, sugar; press in pan.", "Pomijesaj mrvice, maslac i secer; utisni u kalup.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Bake crust 10 minutes.", "Peci koru 10 minuta.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Add chocolate and bake 2–3 mins.", "Dodaj cokoladu i peci jos 2–3 minute.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Add marshmallows over chocolate.", "Stavi marshmallow preko cokolade.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Broil 1–2 mins until golden.", "Zapeci 1–2 minuta dok ne porumene.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Cool completely and cut.", "Ohladi i isijeci.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "peanut_butter_bars":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-1 cup peanut butter\n-½ cup melted butter\n-2 cups powdered sugar\n-2 cups graham crumbs\n-1½ cups chocolate chips",
                        "Sastojci:\n-240g kikiriki putera\n-115g otopljenog maslaca\n-240g secera u prahu\n-200g mrvica od keksa\n-270g cokoladnih kapljica",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Mix peanut butter, butter, sugar, and crumbs.", "Pomijesaj kikiriki puter, maslac, secer i mrvice.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Press into greased 9x13 pan.", "Utisni smjesu u podmazan kalup 23x33cm.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Melt chocolate and spread on top.", "Otopi cokoladu i prelij preko sloja.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Chill at least 1 hour.", "Ohladi najmanje 1 sat.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Cut into bars and serve.", "Isijeci na stanglice i posluzi.", "vanilla_sponge_step6_heat_milk.jpg"));
                break;
            case "cheesecake_brownies":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\nBrownie Layer:\n-½ cup butter\n-1 cup sugar\n-2 eggs\n-1 tsp vanilla\n-⅓ cup cocoa\n-½ cup flour\n-¼ tsp salt\n-¼ tsp baking powder\nCheesecake Layer:\n-225g cream cheese\n-¼ cup sugar\n-1 egg\n-½ tsp vanilla",
                        "Sastojci:\nBrownie sloj:\n-115g maslaca\n-200g secera\n-2 jaja\n-1 kasicica vanilije\n-40g kakaa\n-65g brasna\n-¼ kasicice soli\n-¼ kasicice praska za pecivo\nCheesecake sloj:\n-225g krem sira\n-50g secera\n-1 jaje\n-½ kasicice vanilije",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C.", "Zagrij pecnicu na 175°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Grease a 9x9-inch pan.", "Podmazi kalup 23x23cm.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "For brownie layer: melt butter, mix with sugar, eggs, vanilla, cocoa, flour, salt, and baking powder. Spread in pan.", "Za brownie sloj: otopi maslac i umuti sa secerom, jajima, vanilijom, kakaom, brasnom, soli i praskom. Rasporedi u kalup.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "For cheesecake: beat cream cheese, add sugar, egg, and vanilla.", "Za cheesecake: umuti krem sir, dodaj secer, jaje i vaniliju.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Drop spoonfuls on brownie batter, swirl with knife.", "Dodaj smjesu kasikom i viljuskom stvori mramorni uzorak.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Bake 35–40 min or until center has moist crumbs.", "Peci 35–40 min dok cackalica ne izade s malo vlaznih mrvica.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Cool completely before slicing.", "Ohladi u potpunosti prije rezanja.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "pumpkin_bars":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\nBars:\n-4 eggs\n-1⅔ cups sugar\n-1 cup oil\n-1 can pumpkin puree\n-2 cups flour\n-2 tsp baking powder\n-2 tsp cinnamon\n-1 tsp salt\n-1 tsp baking soda\nFrosting:\n-225g cream cheese\n-115g butter\n-2 cups powdered sugar\n-1 tsp vanilla",
                        "Sastojci:\nZa biskvit:\n-4 jaja\n-330g secera\n-240ml ulja\n-1 konzerva pirea od bundeve\n-250g brasna\n-2 kasicice praska za pecivo\n-2 kasicice cimeta\n-1 kasicica soli\n-1 kasicica sode bikarbone\nZa kremu:\n-225g krem sira\n-115g maslaca\n-240g secera u prahu\n-1 kasicica vanilije",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C.", "Zagrij pecnicu na 175°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Beat eggs, sugar, oil, and pumpkin until fluffy.", "Umuti jaja, secer, ulje i bundevu dok ne postane pjenasto.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "In another bowl, whisk flour, baking powder, cinnamon, salt, baking soda.", "U drugoj zdjeli pomijesaj brasno, prasak za pecivo, cimet, so i sodu.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Gradually mix dry into pumpkin mixture.", "Postepeno dodaj suhe sastojke u smjesu s bundevom.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Spread in ungreased 9x13 pan.", "Izlij u nepodmazan kalup 23x33cm.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Bake 25–30 min or until toothpick comes out clean.", "Peci 25–30 min dok cackalica ne izade cista.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Cool completely.", "Potpuno ohladi.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "For frosting: Beat cream cheese and butter, add sugar and vanilla.", "Za kremu: Umuti sir i maslac, dodaj secer i vaniliju.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 10, "Spread frosting over bars.", "Premazi ohladene kocke kremom.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 11, "Cut into squares and serve.", "Isijeci na kocke i posluzi.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "date_squares":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\nFilling:\n-2 cups chopped dates\n-1 cup water\n-⅓ cup sugar\n-1 tsp vanilla\nCrust & Topping:\n-1½ cups flour\n-1½ cups oats\n-1 cup brown sugar\n-1 tsp baking soda\n-¼ tsp salt\n-1 cup melted butter",
                        "Sastojci:\nPunjenje:\n-300g nasjeckanih hurmi\n-240ml vode\n-67g secera\n-1 kasicica vanilije\nKora i preliv:\n-190g brasna\n-135g zobenih pahuljica\n-200g smedeg secera\n-1 kasicica sode bikarbone\n-¼ kasicice soli\n-226g otopljenog maslaca",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C.", "Zagrij pecnicu na 175°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Cook dates, water, and sugar until thickened. Stir in vanilla.", "Kuhaj hurme, vodu i secer dok se ne zgusne. Umijesaj vaniliju.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Mix flour, oats, brown sugar, baking soda, salt. Add butter until crumbly.", "Pomijesaj brasno, zob, secer, sodu i so. Dodaj maslac dok ne postane mrvicasto.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Press half into greased 9x13 pan.", "Pritisni polovinu smjese u podmazan kalup 23x33cm.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Spread date filling over base.", "Premazi smjesu hurmi preko kore.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Top with remaining oat mix, press gently.", "Pospi ostatkom smjese i lagano pritisni.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Bake 25–30 min until golden.", "Peci 25–30 min dok ne porumeni.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Cool fully before cutting.", "Ohladi potpuno prije rezanja.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "krispie_rice_treats":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-3 tbsp butter\n-10 oz marshmallows (or 4 cups mini)\n-6 cups crispy rice cereal",
                        "Sastojci:\n-3 kasike maslaca\n-283g marshmallow bombona (ili 4 solje mini)\n-150g rizinih pahuljica",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Melt butter over low heat.", "Otopi maslac na laganoj vatri.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Add marshmallows, stir until melted. Remove from heat.", "Dodaj marshmallow i mijesaj dok se ne otope. Skloni s vatre.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Stir in crispy rice cereal until coated.", "Dodaj pahuljice i mijesaj dok se ne obloze.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Press into greased 23x33cm pan with spatula or wax paper.", "Utisni smjesu u podmazan kalup pomocu spatule ili papira.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Cool completely and cut into squares.", "Ohladi u potpunosti i isijeci na kocke.", "vanilla_sponge_step6_heat_milk.jpg"));
                break;

            //PIES & TARTS
            case "apple_pie":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-1 package pastry (9\" double crust)\n-¾ cup sugar\n-1 tsp cinnamon\n-6 cups apple slices\n-1 tbsp butter",
                        "Sastojci:\n-1 pakovanje tijesta za pitu (za 23cm s dvostrukom korom)\n-¾ solje secera\n-1 kasicica cimeta\n-6 soljica narezanih jabuka\n-1 kasika maslaca",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 220°C (425°F).", "Zagrij pecnicu na 220°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Place one crust in pie plate.", "Stavi jednu koru u kalup za pitu.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Mix sugar and cinnamon, toss with apples.", "Pomijesaj secer i cimet, pa umijesaj u jabuke.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Fill crust with apple mix, dot with butter.", "Napuniti koru jabukama i staviti komadice maslaca.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Cover with top crust, seal, cut slits.", "Prekrij drugom korom, zatvori rubove, napravi rezove.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Bake 40–50 min until golden.", "Peci 40–50 minuta dok ne porumeni.", "vanilla_sponge_step7_add_milk.jpg"));
                break;
            case "pecan_pie":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-1 unbaked pie crust\n-1 cup corn syrup\n-1 cup sugar\n-3 eggs\n-2 tbsp butter, melted\n-1 tsp vanilla\n-1½ cups pecans",
                        "Sastojci:\n-1 sirova kora za pitu\n-1 solja kukuruznog sirupa\n-1 solja secera\n-3 jaja\n-2 kasike otopljenog maslaca\n-1 kasicica vanilije\n-1½ solja polovina oraha",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C.", "Zagrij pecnicu na 175°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Whisk together syrup, sugar, eggs, butter, vanilla.", "Umuti sirup, secer, jaja, maslac i vaniliju.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Place crust in pie plate.", "Postavi koru u kalup.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Add pecans evenly.", "Rasporedi orahe ravnomjerno.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Pour filling over pecans.", "Prelij smjesu preko oraha.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Bake 60–70 min until set.", "Peci 60–70 minuta dok se ne stegne.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Cool completely before serving.", "Ohladi u potpunosti prije posluzivanja.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "pumpkin_pie":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-1 unbaked pie crust\n-1 can pumpkin puree\n-1 can condensed milk\n-2 eggs\n-1 tsp cinnamon\n-½ tsp ginger\n-½ tsp nutmeg\n-½ tsp salt",
                        "Sastojci:\n-1 sirova kora za pitu\n-1 konzerva pirea od bundeve\n-1 konzerva kondenzovanog mlijeka\n-2 jaja\n-1 kasicica cimeta\n-½ kasicice dumbira\n-½ kasicice muskatnog orascica\n-½ kasicice soli",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 220°C.", "Zagrij pecnicu na 220°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Whisk all filling ingredients until smooth.", "Umuti sve sastojke za fil dok ne postane glatko.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Pour into pie crust.", "Sipaj u koru za pitu.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Bake 15 min, reduce to 175°C, then bake 35–40 min.", "Peci 15 min, smanji na 175°C i peci jos 35–40 min.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Cool 2 hours before serving.", "Ohladi 2 sata prije serviranja.", "vanilla_sponge_step6_heat_milk.jpg"));
                break;
            case "key_lime_pie":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-1 graham crust\n-3 cups condensed milk\n-½ cup sour cream\n-¾ cup key lime juice\n-1 tbsp lime zest",
                        "Sastojci:\n-1 kora od keksa\n-3 solje kondenzovanog mlijeka\n-½ solje kisele pavlake\n-¾ solje soka od limete\n-1 kasika naribane kore limete",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C.", "Zagrij pecnicu na 175°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Whisk filling ingredients until smooth.", "Umuti sastojke za fil dok ne postanu glatki.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Pour into crust.", "Sipaj u pripremljenu koru.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Bake 5–8 min until small bubbles form. Don't brown.", "Peci 5–8 min dok se ne pojave mjehurici. Ne smije porumeniti.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Chill before serving.", "Ohladi prije serviranja.", "vanilla_sponge_step6_heat_milk.jpg"));
                break;
            case "cherry_pie":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-2 pie crusts\n-4 cups tart cherries\n-1 to 1½ cups sugar\n-4 tbsp cornstarch\n-1 tbsp lemon juice\n-1 tsp almond extract\n-2 tbsp butter\n-1 egg (egg wash)",
                        "Sastojci:\n-2 kore za pitu\n-4 solje visanja\n-1 do 1½ solja secera\n-4 kasike gustina\n-1 kasika limunovog soka\n-1 kasicica ekstrakta badema\n-2 kasike maslaca\n-1 jaje (za premaz)",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2,
                        "Preheat oven to 190°C (375°F).",
                        "Zagrij pecnicu na 190°C.",
                        "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3,
                        "Cook cherries, sugar, cornstarch, lemon juice, and extract until thick.",
                        "Kuhaj visnje, secer, gustin, limunov sok i ekstrakt dok se ne zgusnu.",
                        "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4,
                        "Place bottom crust, fill with cherry mix, and dot with butter.",
                        "Stavi donju koru, napuni smjesom od visanja i dodaj maslac.",
                        "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5,
                        "Cover with top crust, seal, and cut slits.",
                        "Pokrivaj drugom korom, zatvori ivice i napravi proreze.",
                        "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6,
                        "Brush top with beaten egg.",
                        "Premazi gornju koru umucenim jajetom.",
                        "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7,
                        "Bake for 45–50 minutes until golden.",
                        "Peci 45–50 minuta dok ne porumeni.",
                        "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8,
                        "Cool before serving.",
                        "Ohladi prije serviranja.",
                        "vanilla_sponge_step8_bake.jpg"));
                break;
            case "lemon_meringue_pie":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-1 baked crust\nFilling:\n-1 cup sugar\n-2 tbsp flour\n-3 tbsp cornstarch\n-¼ tsp salt\n-1½ cups water\n-2 lemons, juiced/zested\n-2 tbsp butter\n-4 egg yolks\nMeringue:\n-4 egg whites\n-6 tbsp sugar",
                        "Sastojci:\n-1 pecena kora\nNadjev:\n-1 solja secera\n-2 kasike brasna\n-3 kasike gustina\n-¼ kasicice soli\n-1½ solje vode\n-2 limuna (sok i korica)\n-2 kasike maslaca\n-4 zumanca\nMeringa:\n-4 bjelanca\n-6 kasika secera",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2,
                        "Preheat oven to 175°C (350°F).",
                        "Zagrij pecnicu na 175°C.",
                        "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3,
                        "Cook filling until thick, then pour into crust.",
                        "Skuhaj nadjev dok se ne zgusne, zatim sipaj u koru.",
                        "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4,
                        "Temper yolks, return to pot, cook till thick again.",
                        "Postepeno dodaj zumanca, kuhaj dok se ne zgusne.",
                        "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5,
                        "Whip whites with sugar until stiff; spread over pie.",
                        "Umuti bjelanca sa secerom u cvrst snijeg; rasporedi preko pite.",
                        "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6,
                        "Bake 10 minutes until golden.",
                        "Peci 10 minuta dok meringa ne porumeni.",
                        "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7,
                        "Cool before serving.",
                        "Ohladi prije serviranja.",
                        "vanilla_sponge_step7_add_milk.jpg"));
                break;
            case "chocolate_cream_pie":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-1 baked crust\nFilling:\n-¾ cup sugar\n-⅓ cup flour\n-2 cups milk\n-2 oz chocolate\n-3 egg yolks\n-2 tbsp butter\n-1 tsp vanilla\nTopping:\n-1 cup whipped topping",
                        "Sastojci:\n-1 pecena kora\nNadjev:\n-¾ solje secera\n-⅓ solje brasna\n-2 solje mlijeka\n-2 cokolade (28g)\n-3 zumanca\n-2 kasike maslaca\n-1 kasicica vanilije\nPreliv:\n-1 solja umucene slatke pavlake",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2,
                        "Cook sugar, flour, milk, and chocolate until thick.",
                        "Skuhaj secer, brasno, mlijeko i cokoladu dok se ne zgusne.",
                        "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3,
                        "Add yolks and return to heat. Stir in butter and vanilla.",
                        "Dodaj zumanca, zatim umijesaj maslac i vaniliju.",
                        "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4,
                        "Pour into crust and chill 4 hours.",
                        "Sipaj u koru i hladi 4 sata.",
                        "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5,
                        "Top with whipped topping before serving.",
                        "Dodaj slag prije posluzivanja.",
                        "vanilla_sponge_step5_fold.jpg"));
                break;
            case "banana_cream_pie":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-1 (9-inch) baked pie crust\n-3 cups whole milk\n-¾ cup granulated sugar\n-⅓ cup all-purpose flour\n-¼ teaspoon salt\n-3 egg yolks, slightly beaten\n-2 tablespoons unsalted butter\n-1 teaspoon vanilla extract\n-3 ripe bananas\n-1 cup whipped cream (for topping",
                        "Sastojci:\n-1 (23 cm) pecena kora za pitu\n-720 ml punomasnog mlijeka\n-150 g secera\n-40 g brasna\n-¼ kasicice soli\n-3 zumanjka, lagano umucena\n-2 kasike neslanog maslaca\n-1 kasicica ekstrakta vanilije\n-3 zrele banane\n-1 solja slaga (za dekoraciju)",
                        "vanilla_sponge_step1_ingredients.jpg"));

                steps.add(new InstructionStep(recipeId, 2,
                        "In a saucepan, combine sugar, flour, and salt. Gradually whisk in milk. Cook over medium heat, stirring constantly until the mixture thickens and comes to a boil. Boil for 1 minute.",
                        "U serpi pomijesaj secer, brasno i so. Postepeno dodaj mlijeko uz mucenje. Kuhaj na srednjoj vatri uz stalno mijesanje dok se smjesa ne zgusne i prokljuca. Kuhaj jos 1 minutu.",
                        "vanilla_sponge_step2_preheat.jpg"));

                steps.add(new InstructionStep(recipeId, 3,
                        "Gradually stir about 1 cup of the hot mixture into the beaten egg yolks. Return the egg mixture to the saucepan and cook for 2 more minutes, stirring constantly. Remove from heat and stir in butter and vanilla extract.",
                        "Dodaj oko 1 solju vruce smjese u zumanjke i promijesaj. Vrati u serpu i kuhaj jos 2 minute uz mijesanje. Skini s vatre i dodaj maslac i vaniliju.",
                        "vanilla_sponge_step3_beat_eggs.jpg"));

                steps.add(new InstructionStep(recipeId, 4,
                        "Slice the bananas and arrange them in the baked pie crust. Pour the custard over the bananas.",
                        "Isijeci banane i poredaj ih u pecenu koru za pitu. Prelij krem preko banana.",
                        "vanilla_sponge_step4_sift.jpg"));

                steps.add(new InstructionStep(recipeId, 5,
                        "Cover and refrigerate the pie for at least 4 hours or until set.",
                        "Pokrij i stavi u frizider na najmanje 4 sata ili dok se ne stegne.",
                        "vanilla_sponge_step5_fold.jpg"));

                steps.add(new InstructionStep(recipeId, 6,
                        "Top with whipped cream before serving.",
                        "Dekorisi slagom prije posluzivanja.",
                        "vanilla_sponge_step6_heat_milk.jpg"));
                break;
            case "fruit_tart":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\nFor the crust:\n-1½ cups all-purpose flour\n-½ cup powdered sugar\n-¼ teaspoon salt\n-9 tablespoons unsalted butter, cold\nFor the pastry cream:\n-2 cups whole milk\n-½ cup granulated sugar\n-¼ cup cornstarch\n-4 large egg yolks\n-2 tablespoons butter\n-1 tsp vanilla\nFor the topping:\n-Fresh fruits (strawberries, kiwi, blueberries, raspberries)\n-¼ cup apricot jam",
                        "Sastojci:\nZa koru:\n-190 g brasna\n-60 g secera u prahu\n-¼ kasicice soli\n-125 g hladnog maslaca\nZa krem:\n-480 ml punomasnog mlijeka\n-100 g secera\n-30 g gustina\n-4 zumanjka\n-2 kasike maslaca\n-1 kasicica vanilije\nZa dekoraciju:\n-Svjeze voce (jagode, kivi, borovnice, maline)\n-¼ solje kajsijine marmelade",
                        "vanilla_sponge_step1_ingredients.jpg"));

                steps.add(new InstructionStep(recipeId, 2,
                        "Prepare the crust:\nIn a food processor, combine flour, powdered sugar, and salt. Add butter and pulse. Press into a tart pan. Chill 30 mins. Preheat oven to 190°C. Bake for 15–20 mins. Cool completely.",
                        "Priprema kore:\nU blenderu sjedini brasno, secer u prahu i so. Dodaj maslac i pulsiraj. Utisni u kalup za tart. Hladiti 30 minuta. Peci na 190°C 15–20 min. Ohladi u potpunosti.",
                        "vanilla_sponge_step2_preheat.jpg"));

                steps.add(new InstructionStep(recipeId, 3,
                        "Prepare the pastry cream:\nHeat milk. Whisk sugar, cornstarch, and egg yolks. Temper with hot milk. Cook until thick. Add butter and vanilla. Chill covered.",
                        "Krem:\nZagrij mlijeko. Umuti secer, gustin i zumanjke. Dodaj vruce mlijeko uz mucenje. Kuhaj dok se ne zgusne. Dodaj maslac i vaniliju. Ohladi pokriveno.",
                        "vanilla_sponge_step3_beat_eggs.jpg"));

                steps.add(new InstructionStep(recipeId, 4,
                        "Assemble the tart:\nSpread cream in crust. Decorate with fruit. Warm apricot jam and brush over fruit.",
                        "Sastavi tart:\nRasporedi krem u koru. Dekorisi vocem. Zagrij marmeladu i premazi voce.",
                        "vanilla_sponge_step4_sift.jpg"));
                break;

            //PASTRIES & BREADS
            case "cinnamon_rolls":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-1 cup (240ml) warm milk\n-2¼ teaspoons (7g) active dry yeast\n-½ cup (100g) granulated sugar\n-⅓ cup (75g) unsalted butter, melted\n-2 large eggs\n-4½ cups (565g) all-purpose flour\n-1 teaspoon salt\nFilling:\n-1 cup (200g) brown sugar\n-2½ tablespoons ground cinnamon\n-⅓ cup (75g) unsalted butter, softened\nCream Cheese Frosting:\n-4 ounces (115g) cream cheese, softened\n-¼ cup (60g) unsalted butter, softened\n-1½ cups (180g) powdered sugar\n-½ teaspoon vanilla extract",
                        "Sastojci:\n-1 solja (240ml) toplog mlijeka\n-2¼ kasicice (7g) suhog kvasca\n-½ solje (100g) secera\n-⅓ solje (75g) otopljenog neslanog maslaca\n-2 velika jaja\n-4½ solje (565g) univerzalnog brasna\n-1 kasicica soli\nFil:\n-1 solja (200g) smedeg secera\n-2½ kasike mljevenog cimeta\n-⅓ solje (75g) omeksanog neslanog maslaca\nGlazura od krem sira:\n-115g krem sira (omeksan)\n-¼ solje (60g) omeksanog neslanog maslaca\n-1½ solje (180g) secera u prahu\n-½ kasicice ekstrakta vanilije",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a large bowl, combine warm milk and yeast. Let sit for 5 minutes until foamy.", "U velikoj zdjeli pomijesaj toplo mlijeko i kvasac. Ostaviti 5 minuta dok se ne zapjeni.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Add sugar, melted butter, eggs, flour, and salt. Mix until a dough forms.", "Dodaj secer, otopljeni maslac, jaja, brasno i so. Mijesaj dok se ne formira tijesto.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Knead the dough on a floured surface for about 5–7 minutes until smooth and elastic.", "Mijesi tijesto na pobrasnjenoj povrsini 5–7 minuta dok ne postane glatko i elasticno.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Place the dough in a greased bowl, cover, and let rise in a warm place for 1 hour or until doubled in size.", "Stavi tijesto u podmazanu zdjelu, pokrij i ostavi na toplom 1 sat dok se ne udvostruci.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Roll out the dough into a 16x12-inch rectangle. Spread softened butter over the dough.", "Razvuci tijesto u pravougaonik 40x30cm. Premazi ga omeksanim maslacem.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Mix brown sugar and cinnamon; sprinkle evenly over the buttered dough.", "Pomijesaj smedi secer i cimet pa ravnomjerno pospi preko tijesta.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Roll up the dough tightly from the long side; cut into 12 equal slices.", "Uvij tijesto sa duze strane u rolnu i isijeci na 12 jednakih dijelova.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Place rolls in a greased 9x13-inch baking dish. Cover and let rise for 30 minutes.", "Stavi rolnice u podmazani pleh 23x33cm. Pokrij i ostavi 30 minuta da narastu.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 10, "Preheat oven to 175°C (350°F). Bake rolls for 25–30 minutes until golden brown.", "Zagrij rernu na 175°C. Peci rolnice 25–30 minuta dok ne porumene.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 11, "For frosting, beat cream cheese and butter until smooth. Add powdered sugar and vanilla; mix until creamy.", "Za glazuru umutiti krem sir i maslac dok ne postanu glatki. Dodaj secer u prahu i vaniliju, pa mijesaj dok ne postane kremasto.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 12, "Spread frosting over warm rolls before serving.", "Premazi glazurom tople rolnice prije posluzivanja.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "danish_pastries":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\nDough:\n-1 cup (240ml) cold milk\n-2¼ teaspoons (7g) active dry yeast\n-3¼ cups (410g) all-purpose flour\n-⅓ cup (65g) granulated sugar\n-½ teaspoon salt\n-1 large egg\n-1 cup (226g) unsalted butter, cold and grated\nFilling:\n-Cream cheese, fruit preserves, or custard\nGlaze:\n-1 cup (120g) powdered sugar\n-2 tablespoons milk\n-½ teaspoon vanilla extract",
                        "Sastojci:\nTijesto:\n-1 solja (240ml) hladnog mlijeka\n-2¼ kasicice (7g) suhog kvasca\n-3¼ solje (410g) univerzalnog brasna\n-⅓ solje (65g) secera\n-½ kasicice soli\n-1 jaje\n-1 solja (226g) hladnog neslanog maslaca, izribanog\nFil:\n-Krem sir, vocni dzem ili krem\nGlazura:\n-1 solja (120g) secera u prahu\n-2 kasike mlijeka\n-½ kasicice ekstrakta vanilije",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a bowl, combine cold milk and yeast; let sit for 5 minutes.", "U zdjeli pomijesaj hladno mlijeko i kvasac; ostavi 5 minuta.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "In a large bowl, mix flour, sugar, and salt. Add the yeast mixture, egg, and grated butter. Mix until a dough forms.", "U velikoj zdjeli pomijesaj brasno, secer i so. Dodaj smjesu s kvascem, jaje i ribani maslac. Mijesaj dok se ne formira tijesto.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Knead the dough gently, then cover and refrigerate for at least 2 hours or overnight.", "Lagano mijesi tijesto, zatim pokrij i ostavi u frizider najmanje 2 sata ili preko noci.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Roll out the dough on a floured surface; fold into thirds like a letter. Repeat rolling and folding twice more.", "Razvuci tijesto na pobrasnjenoj povrsini; presavij ga na trecine kao pismo. Ponovi jos dva puta.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Roll out the dough and cut into desired shapes. Add fillings as desired.", "Razvuci tijesto i isijeci u zeljene oblike. Dodaj nadjeve po zelji.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Place pastries on a baking sheet, cover, and let rise for 30 minutes.", "Poredaj peciva na pleh, pokrij i ostavi da rastu 30 minuta.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Preheat oven to 200°C (400°F). Bake pastries for 15–20 minutes until golden brown.", "Zagrij rernu na 200°C. Peci peciva 15–20 minuta dok ne porumene.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Mix glaze ingredients and drizzle over cooled pastries.", "Pomijesaj sastojke za glazuru i prelij ohladena peciva.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "croissants":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\nDough:\n-4 cups (500g) all-purpose flour\n-½ cup (100g) granulated sugar\n-2¼ teaspoons (7g) active dry yeast\n-1½ teaspoons salt\n-1 cup (240ml) warm milk\n-2 tablespoons (30g) unsalted butter, melted\nButter Layer:\n-1½ cups (340g) unsalted butter, cold\nEgg Wash:\n-1 egg, beaten with 1 tablespoon water",
                        "Sastojci:\nTijesto:\n-4 solje (500g) univerzalnog brasna\n-½ solje (100g) secera\n-2¼ kasicice (7g) suhog kvasca\n-1½ kasicice soli\n-1 solja (240ml) toplog mlijeka\n-2 kasike (30g) otopljenog neslanog maslaca\nMaslac sloj:\n-1½ solje (340g) hladnog neslanog maslaca\nPremaz:\n-1 jaje, umuceno sa 1 kasikom vode",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a bowl, combine flour, sugar, yeast, and salt. Add warm milk and melted butter; mix until a dough forms.", "U zdjeli pomijesaj brasno, secer, kvasac i so. Dodaj toplo mlijeko i otopljeni maslac; mijesaj dok se ne formira tijesto.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Knead the dough until smooth; cover and refrigerate for 1 hour.", "Mijesi tijesto dok ne postane glatko; pokrij i ostavi u frizider 1 sat.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Roll out the dough into a rectangle. Place cold butter between parchment paper and pound into a thin sheet. Place butter on half of the dough and fold over, sealing edges.", "Razvuci tijesto u pravougaonik. Stavi hladan maslac izmedu papira za pecenje i istanji. Postavi ga na polovinu tijesta, preklopi i zatvori ivice.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Roll out the dough and fold into thirds. Repeat rolling and folding three times, chilling for 30 minutes between each fold.", "Razvuci tijesto i presavij na trecine. Ponavljaj postupak jos tri puta, hladeci 30 minuta izmedu svakog presavijanja.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Roll out the dough into a large rectangle; cut into triangles. Roll each triangle from the base to the tip to form croissants.", "Razvuci tijesto u veliki pravougaonik; isijeci na trouglove. Svaki trougao uvij od baze ka vrhu da dobijes kroasan.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Place croissants on a baking sheet, cover, and let rise until doubled in size, about 1–2 hours.", "Poredaj kroasane na pleh, pokrij i ostavi da narastu 1–2 sata dok ne udvostruce volumen.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Preheat oven to 200°C (400°F). Brush croissants with egg wash.", "Zagrij rernu na 200°C. Premazi kroasane umucenim jajetom.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Bake for 15–20 minutes until golden brown.", "Peci 15–20 minuta dok ne porumene.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "sweet_scones":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-2½ cups (315g) all-purpose flour\n-1 tablespoon baking powder\n-½ teaspoon salt\n-¼ cup (50g) granulated sugar\n-8 tablespoons (115g) cold unsalted butter, cut into pieces\n-⅔ cup (160ml) milk\n-Optional add-ins: dried fruits, chocolate chips, or nuts",
                        "Sastojci:\n-2½ solje (315g) univerzalnog brasna\n-1 kasika praska za pecivo\n-½ kasicice soli\n-¼ solje (50g) secera\n-8 kasika (115g) hladnog neslanog maslaca, isjecenog na komadice\n-⅔ solje (160ml) mlijeka\n-Opcioni dodaci: suho voce, komadici cokolade ili orasasti plodovi",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 220°C (425°F).", "Zagrij rernu na 220°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "In a large bowl, whisk together flour, baking powder, salt, and sugar.", "U velikoj zdjeli umuti brasno, prasak za pecivo, so i secer.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Cut in cold butter until the mixture resembles coarse crumbs.", "Dodaj hladan maslac i mijesaj dok smjesa ne podsjeca na grube mrvice.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Stir in milk just until combined; do not overmix.", "Dodaj mlijeko i lagano promijesaj samo dok se sastojci ne sjedine; nemoj previse mijesati.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Fold in any optional add-ins.", "Umijesaj dodatke po zelji.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Turn dough onto a floured surface; pat into a 1-inch thick circle.", "Istresi tijesto na pobrasnjenu povrsinu i oblikuj u krug debljine oko 2,5 cm.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Cut into wedges and place on a baking sheet.", "Isijeci na trouglove i poredaj na pleh.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Bake for 12–15 minutes until golden brown.", "Peci 12–15 minuta dok ne dobiju zlatno-smedu boju.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "banana_bread":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-2 cups (250g) all-purpose flour\n-1 teaspoon baking soda\n-¼ teaspoon salt\n-¾ cup (150g) brown sugar\n-½ cup (115g) butter, softened\n-2 large eggs, beaten\n-2⅓ cups (550ml) mashed overripe bananas",
                        "Sastojci:\n-2 solje (250g) univerzalnog brasna\n-1 kasicica sode bikarbone\n-¼ kasicice soli\n-¾ solje (150g) smedeg secera\n-½ solje (115g) omeksalog maslaca\n-2 velika jaja, umucena\n-2⅓ solje (550ml) izgnjecenih prezrelih banana",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C (350°F).", "Zagrij rernu na 175°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "In a large bowl, combine flour, baking soda, and salt.", "U velikoj zdjeli pomijesaj brasno, sodu bikarbonu i so.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "In a separate bowl, cream together brown sugar and butter.", "U drugoj zdjeli umutite smedi secer i maslac dok ne postanu kremasti.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Stir in eggs and mashed bananas until well blended.", "Dodaj jaja i izgnjecene banane, pa dobro izmijesaj.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Stir banana mixture into flour mixture; stir just to moisten.", "Dodaj smjesu banana u brasno i mijesaj samo dok se sastojci ne navlaze.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Pour batter into a lightly greased 9x5 inch loaf pan.", "Sipaj smjesu u lagano podmazan kalup 23x13 cm.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Bake for 60 to 65 minutes, until a toothpick inserted into center of the loaf comes out clean.", "Peci 60 do 65 minuta, dok cackalica umetnuta u sredinu ne izade cista.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Let bread cool in pan for 10 minutes, then turn out onto a wire rack.", "Pusti da se hljeb hladi 10 minuta u kalupu, zatim ga prebaci na resetku da se potpuno ohladi.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "zucchini_bread":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-3 eggs\n-2 cups (400g) granulated sugar\n-1 cup (240ml) vegetable oil\n-2 cups (260g) grated zucchini\n-2 teaspoons vanilla extract\n-3 cups (375g) all-purpose flour\n-1 teaspoon baking soda\n-¼ teaspoon baking powder\n-1 teaspoon salt\n-3 teaspoons ground cinnamon\n-1 cup (120g) chopped walnuts (optional)",
                        "Sastojci:\n-3 jaja\n-2 solje (400g) secera\n-1 solja (240ml) biljnog ulja\n-2 solje (260g) naribane tikvice\n-2 kasicice ekstrakta vanilije\n-3 solje (375g) univerzalnog brasna\n-1 kasicica sode bikarbone\n-¼ kasicice praska za pecivo\n-1 kasicica soli\n-3 kasicice cimeta\n-1 solja (120g) sjeckanih oraha (opcionalno)",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C (350°F).", "Zagrij rernu na 175°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "In a large bowl, beat eggs. Add sugar, oil, zucchini, and vanilla; mix well.", "U velikoj zdjeli umuti jaja. Dodaj secer, ulje, tikvice i vaniliju; dobro promijesaj.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "In a separate bowl, combine flour, baking soda, baking powder, salt, and cinnamon.", "U drugoj zdjeli pomijesaj brasno, sodu bikarbonu, prasak za pecivo, so i cimet.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Gradually add dry ingredients to the zucchini mixture; stir until just combined.", "Postepeno dodaji suhe sastojke smjesi sa tikvicama; mijesaj samo dok se ne sjedine.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Fold in chopped walnuts if using.", "Umijesaj sjeckane orahe ako ih koristis.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Pour batter into two greased 8x4 inch loaf pans.", "Sipaj smjesu u dva podmazana kalupa 20x10 cm.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Bake for 60 to 70 minutes, or until a toothpick inserted into the center comes out clean.", "Peci 60 do 70 minuta, ili dok cackalica umetnuta u sredinu ne izade cista.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Cool in pans for 10 minutes before removing to cool completely.", "Pusti da se hljebovi hlade 10 minuta u kalupu prije nego ih izvadis da se potpuno ohlade.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "monkey_bread":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-3 cans (7.5 oz each) refrigerated biscuit dough\n-1 cup (200g) granulated sugar\n-2 teaspoons ground cinnamon\n-½ cup (115g) butter\n-½ cup (110g) packed brown sugar",
                        "Sastojci:\n-3 konzerve (po 7.5 oz) rashladenog tijesta za biskvite\n-1 solja (200g) secera\n-2 kasicice cimeta\n-½ solje (115g) maslaca\n-½ solje (110g) smedeg secera",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C (350°F). Grease a 10-inch Bundt pan.", "Zagrij rernu na 175°C. Podmazi Bundt kalup od 25 cm.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Cut each biscuit into quarters.", "Isijeci svaki biskvit na cetiri dijela.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "In a large plastic bag, combine granulated sugar and cinnamon. Add biscuit pieces and shake to coat.", "U velikoj plasticnoj vrecici pomijesaj secer i cimet. Dodaj komadice biskvita i protresi da se obloze.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Arrange coated biscuit pieces in the prepared pan.", "Poredaj oblozene komade tijesta u pripremljen kalup.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "In a small saucepan, melt butter and brown sugar over medium heat. Bring to a boil and cook for 1 minute.", "U manjoj serpici otopi maslac i smedi secer na srednjoj temperaturi. Pusti da provri i kuhaj 1 minut.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Pour the butter mixture over the biscuit pieces.", "Prelij smjesu od maslaca preko komada biskvita.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Bake for 35 minutes, or until golden brown.", "Peci 35 minuta, ili dok ne dobije zlatno-smedu boju.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Let cool in pan for 10 minutes, then invert onto a serving plate.", "Ohladi 10 minuta u kalupu, zatim prevrni na tanjir za serviranje.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "babka":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n" +
                                "For the dough:\n" +
                                "-4 cups (500g) all-purpose flour\n" +
                                "-½ cup (100g) granulated sugar\n" +
                                "-2¼ teaspoons (7g) active dry yeast\n" +
                                "-¾ cup (180ml) warm milk\n" +
                                "-2 large eggs\n" +
                                "-1 teaspoon vanilla extract\n" +
                                "-½ cup (115g) unsalted butter, softened\n" +
                                "For the filling:\n" +
                                "-1 cup (200g) semisweet chocolate chips\n" +
                                "-½ cup (115g) unsalted butter\n" +
                                "-½ cup (100g) granulated sugar\n" +
                                "-¼ cup (25g) unsweetened cocoa powder",
                        "Sastojci:\n" +
                                "Za tijesto:\n" +
                                "-500g glatkog brasna\n" +
                                "-100g secera\n" +
                                "-7g suhog kvasca\n" +
                                "-180ml toplog mlijeka\n" +
                                "-2 velika jaja\n" +
                                "-1 kasicica ekstrakta vanilije\n" +
                                "-115g omeksalog putera\n" +
                                "Za fil:\n" +
                                "-200g cokoladnih kapljica (poluslatkih)\n" +
                                "-115g putera\n" +
                                "-100g secera\n" +
                                "-25g nezasladenog kakao praha",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a large bowl, combine flour, sugar, and yeast.", "U velikoj posudi pomijesaj brasno, secer i kvasac.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Add warm milk, eggs, and vanilla extract; mix until combined.", "Dodaj toplo mlijeko, jaja i ekstrakt vanilije; mijesaj dok se ne sjedini.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Add softened butter and knead the dough until smooth and elastic.", "Dodaj omeksali puter i mijesi tijesto dok ne postane glatko i elasticno.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Cover and let rise in a warm place until doubled in size, about 1.5 hours.", "Pokrij i ostavi da naraste na toplom mjestu dok se ne udvostruci, oko 1.5 sat.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "While the dough is rising, prepare the filling by melting chocolate chips and butter together. Stir in sugar and cocoa powder until smooth; let cool.", "Dok tijesto raste, pripremi fil tako sto ces otopiti cokoladne kapljice i puter zajedno. Umijesaj secer i kakao dok smjesa ne postane glatka; ostavi da se ohladi.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Punch down the risen dough and divide it in half. Roll each half into a rectangle.", "Izduvaj nadoslo tijesto i podijeli ga na pola. Svaku polovinu razvuci u pravougaonik.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Spread the chocolate filling over each rectangle, leaving a small border.", "Premazi fil od cokolade preko svakog pravougaonika, ostavljajuci mali rub.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Roll up each rectangle tightly into a log, then twist the logs and place into greased loaf pans.", "Urolaj svaki pravougaonik cvrsto u rolat, zatim ih uvij i stavi u podmazane kalupe za hljeb.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 10, "Cover and let rise for another hour.", "Pokrij i ostavi da naraste jos jedan sat.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 11, "Preheat oven to 175°C (350°F). Bake babkas for 30–35 minutes, or until golden brown.", "Zagrij rernu na 175°C. Peci babke 30–35 minuta, ili dok ne porumene.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 12, "Let cool before slicing.", "Ostavi da se ohladi prije rezanja.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "apple_turnovers":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n" +
                                "-2 sheets puff pastry, thawed\n" +
                                "-2 cups (240g) peeled and diced apples\n" +
                                "-¼ cup (50g) granulated sugar\n" +
                                "-1 teaspoon ground cinnamon\n" +
                                "-1 tablespoon all-purpose flour\n" +
                                "-1 tablespoon lemon juice\n" +
                                "-1 egg, beaten (for egg wash)",
                        "Sastojci:\n" +
                                "-2 lista lisnatog tijesta, odmrznuta\n" +
                                "-240g oguljenih i nasjeckanih jabuka\n" +
                                "-50g secera\n" +
                                "-1 kasicica mljevenog cimeta\n" +
                                "-1 kasika brasna\n" +
                                "-1 kasika limunovog soka\n" +
                                "-1 umuceno jaje (za premazivanje)",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 200°C (400°F). Line a baking sheet with parchment paper.", "Zagrij rernu na 200°C. Oblozi pleh papirom za pecenje.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "In a bowl, combine diced apples, sugar, cinnamon, flour, and lemon juice.", "U posudi pomijesaj sjeckane jabuke, secer, cimet, brasno i limunov sok.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Cut each puff pastry sheet into four squares.", "Isijeci svaki list lisnatog tijesta na cetiri kvadrata.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Place a spoonful of apple mixture in the center of each square.", "Stavi kasiku smjese od jabuka u sredinu svakog kvadrata.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Fold each square into a triangle and press edges to seal.", "Preklopi svaki kvadrat u trougao i pritisni ivice da se zatvore.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Place turnovers on the prepared baking sheet and brush with beaten egg.", "Poredaj jastucice na pripremljeni pleh i premazi umucenim jajetom.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Bake for 20–25 minutes, or until golden brown.", "Peci 20–25 minuta, ili dok ne porumene.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Allow to cool slightly before serving.", "Pusti da se malo ohlade prije serviranja.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "puff_pastry_twists":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n" +
                                "-1 sheet of puff pastry (store-bought or homemade)\n" +
                                "-2 tablespoons melted butter\n" +
                                "-¼ cup granulated sugar\n" +
                                "-1 teaspoon ground cinnamon\n" +
                                "-Optional: ¼ cup finely chopped nuts or mini chocolate chips",
                        "Sastojci:\n" +
                                "-1 list lisnatog tijesta (kupovno ili domace)\n" +
                                "-2 kasike otopljenog putera\n" +
                                "-1/4 solje secera\n" +
                                "-1 kasicica mljevenog cimeta\n" +
                                "-Po zelji: 1/4 solje sitno sjeckanih orasastih plodova ili mini cokoladnih kapljica",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat your oven to 200°C (400°F).", "Zagrij rernu na 200°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "On a lightly floured surface, roll out the puff pastry sheet into a rectangle.", "Na blago pobrasnjenoj povrsini razvuci lisnato tijesto u pravougaonik.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Brush the entire surface with melted butter.", "Premazi cijelu povrsinu otopljenim puterom.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "In a small bowl, mix together the sugar and cinnamon. Sprinkle this mixture evenly over the buttered pastry.", "U maloj zdjeli pomijesaj secer i cimet. Ravnomjerno pospi preko puterom premazanog tijesta.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "If using, sprinkle chopped nuts or chocolate chips over the cinnamon-sugar layer.", "Ako koristis, pospi sjeckane orasaste plodove ili cokoladne kapljice preko sloja sa secerom i cimetom.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Using a sharp knife or pizza cutter, cut the pastry into strips about 1 inch wide.", "Ostrim nozem ili rezacem za pizzu isijeci tijesto na trake sirine oko 2.5 cm.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Take each strip and twist it several times, then place it onto a baking sheet lined with parchment paper.", "Uzmi svaku traku i uvrni je nekoliko puta, zatim je stavi na pleh oblozen papirom za pecenje.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Bake for 12–15 minutes, or until the twists are golden brown and puffed.", "Peci 12–15 minuta, ili dok uvrnuti stapici ne porumene i narastu.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 10, "Remove from the oven and let cool slightly before serving.", "Izvadi iz rerne i pusti da se malo ohlade prije serviranja.", "vanilla_sponge_step8_bake.jpg"));
                break;


            //NO BAKE DESSERTS
            case "no_bake_cheesecake":
                steps.add(new InstructionStep(recipeId, 1,
                        "Ingredients:\n-1½ cups graham cracker crumbs\n-¼ cup brown sugar\n-½ cup unsalted butter, melted\n-16 oz (450g) cream cheese, softened\n-1 cup powdered sugar\n-1 teaspoon vanilla extract\n-1 cup heavy whipping cream",
                        "Sastojci:\n-1½ solja mrvica od graham keksa\n-¼ solje smedeg secera\n-½ solje otopljenog neslanog maslaca\n-450g krem sira, omeksan\n-1 solja secera u prahu\n-1 kasicica ekstrakta vanilije\n-1 solja slatke pavlake",
                        "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Combine graham cracker crumbs, brown sugar, and melted butter. Press into a 9-inch springform pan. Freeze while preparing the filling.", "Pomijesaj mrvice keksa, smedi secer i otopljeni maslac. Utisni u kalup s odvojivim obodom (23 cm). Zamrzni dok pripremas fil.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "In a bowl, beat cream cheese until smooth. Add powdered sugar and vanilla; mix well.", "U posudi izmuti krem sir dok ne postane gladak. Dodaj secer u prahu i vaniliju; dobro promijesaj.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "In a separate bowl, whip the heavy cream until stiff peaks form. Gently fold into the cream cheese mixture.", "U drugoj posudi umuti slatku pavlaku dok ne dobijes cvrste vrhove. Pazljivo umijesaj u smjesu sa krem sirom.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Pour the filling over the crust and smooth the top. Refrigerate for at least 6 hours or overnight.", "Izlij fil preko kore i poravnaj povrsinu. Ohladi u frizideru najmanje 6 sati ili preko noci.", "vanilla_sponge_step5_fold.jpg"));
                break;
            case "chocolate_mousse":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-3 large eggs\n-125g dark chocolate (70% cocoa)\n-10g unsalted butter\n-½ cup heavy cream\n-3 tablespoons caster sugar", "Sastojci:\n-3 velika jaja\n-125g tamne cokolade (70% kakaa)\n-10g neslanog maslaca\n-½ solje slatke pavlake\n-3 kasike sitnog secera", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Separate the eggs. Beat the yolks with sugar until pale.", "Odvoji bjelanca od zumanaca. Umuti zumanca sa secerom dok ne posvijetle.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Melt chocolate and butter together; let cool slightly.", "Otopi cokoladu i maslac zajedno; ostavi da se malo ohladi.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Fold the chocolate mixture into the egg yolk mixture.", "Umijesaj cokoladnu smjesu u smjesu od zumanaca.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Whip the cream to soft peaks and fold into the chocolate mixture.", "Umuti pavlaku do mekanih vrhova pa je umijesaj u cokoladnu smjesu.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "In a separate bowl, beat egg whites until stiff peaks form; gently fold into the mousse.", "U drugoj posudi umuti bjelanca dok ne postanu cvrsta; pazljivo ih umijesaj u mousse.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Divide into serving dishes and chill for at least 2 hours.", "Podijeli u posudice za serviranje i hladi najmanje 2 sata.", "vanilla_sponge_step7_add_milk.jpg"));
                break;
            case "tiramisu":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-6 oz ladyfinger cookies\n-1 cup strong brewed coffee, cooled\n-16 oz mascarpone cheese\n-½ cup granulated sugar\n-1 teaspoon vanilla extract\n-1½ cups heavy cream\n-Unsweetened cocoa powder for dusting", "Sastojci:\n-170g piskota\n-1 solja jake crne kafe, ohladene\n-450g mascarpone sira\n-½ solje kristal secera\n-1 kasicica ekstrakta vanilije\n-1½ solja slatke pavlake\n-Nezasladeni kakao za posipanje", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Dip each ladyfinger into the coffee and arrange in a single layer in a 9x9-inch dish.", "Umocite svaku piskotu u kafu i slozite ih u jednom sloju u posudu 23x23 cm.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "In a bowl, beat mascarpone, sugar, and vanilla until smooth.", "U posudi umutite mascarpone, secer i vaniliju dok ne dobijete glatku smjesu.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "In another bowl, whip the cream to stiff peaks; fold into the mascarpone mixture.", "U drugoj posudi umutite slatku pavlaku dok ne postane cvrsta, zatim je umijesajte u mascarpone smjesu.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Spread half of the cream mixture over the ladyfingers. Repeat layers.", "Rasporedite polovinu smjese preko piskota. Ponovite slojeve.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Cover and refrigerate for at least 6 hours. Dust with cocoa powder before serving.", "Pokrij i ohladi najmanje 6 sati. Prije serviranja pospi kakaom.", "vanilla_sponge_step6_heat_milk.jpg"));
                break;
            case "oreo_truffles":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-36 Oreo cookies\n-8 oz cream cheese, softened\n-16 oz semisweet or white chocolate, melted", "Sastojci:\n-36 Oreo keksa\n-225g krem sira, omeksanog\n-450g poluslatke ili bijele cokolade, otopljene", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Crush Oreos into fine crumbs. Mix with cream cheese until well combined.", "Samelji Oreo kekse u sitne mrvice. Pomijesaj s krem sirom dok se potpuno ne sjedini.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Roll into 1-inch balls and place on a baking sheet. Freeze for 15 minutes.", "Oblikuj kuglice velicine 2.5 cm i stavi ih na pleh. Zamrzni 15 minuta.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Dip each ball into melted chocolate, allowing excess to drip off.", "Umoci svaku kuglicu u otopljenu cokoladu, pusti visak da iscuri.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Place back on the baking sheet and refrigerate until set.", "Vrati na pleh i stavi u frizider dok se ne stegne.", "vanilla_sponge_step5_fold.jpg"));
                break;
            case "no_bake_peanut_butter_pie":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-1 (9-inch) graham cracker pie crust\n-1 cup creamy peanut butter\n-8 oz cream cheese, softened\n-1 cup powdered sugar\n-8 oz whipped topping (e.g., Cool Whip), thawed", "Sastojci:\n-1 kora od graham keksa (23 cm)\n-1 solja kremastog putera od kikirikija\n-225g krem sira, omeksan\n-1 solja secera u prahu\n-225g umucenog preljeva (npr. Cool Whip), odmrznut", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a bowl, beat peanut butter and cream cheese until smooth.", "U posudi izmuti puter od kikirikija i krem sir dok ne postanu glatki.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Add powdered sugar and mix well.", "Dodaj secer u prahu i dobro promijesaj.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Fold in whipped topping until combined.", "Umijesaj umuceni preljev dok se ne sjedini.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Pour into the crust and smooth the top. Refrigerate for at least 4 hours.", "Izlij smjesu u koru i poravnaj vrh. Ohladi u frizideru najmanje 4 sata.", "vanilla_sponge_step5_fold.jpg"));
                break;
            case "panna_cotta":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-⅓ cup skim milk\n-1 envelope (0.25 oz) unflavored gelatin\n-2½ cups heavy cream\n-½ cup granulated sugar\n-1½ teaspoons vanilla extract", "Sastojci:\n-⅓ solje obranog mlijeka\n-1 kesica (7g) zelatine bez ukusa\n-2½ solje slatke pavlake\n-½ solje kristal secera\n-1½ kasicica ekstrakta vanilije", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a small bowl, sprinkle gelatin over milk; let stand for 5 minutes.", "U manjoj posudi pospi zelatinu preko mlijeka; ostavi da stoji 5 minuta.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "In a saucepan, combine cream and sugar; bring to a simmer over medium heat.", "U serpi pomijesaj pavlaku i secer; zagrijavaj dok ne prokljuca na srednjoj temperaturi.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Remove from heat and stir in gelatin mixture until dissolved.", "Skini sa vatre i umijesaj smjesu sa zelatinom dok se ne otopi.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Stir in vanilla extract. Pour into serving glasses and refrigerate for at least 4 hours.", "Dodaj ekstrakt vanilije. Sipaj u case za serviranje i ohladi najmanje 4 sata.", "vanilla_sponge_step5_fold.jpg"));
                break;
            case "chia_pudding":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-1 cup almond milk\n-¼ cup chia seeds\n-1 tablespoon maple syrup (optional)", "Sastojci:\n-1 solja bademovog mlijeka\n-¼ solje chia sjemenki\n-1 kasika javorovog sirupa (po zelji)", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a bowl, whisk together almond milk, chia seeds, and maple syrup.", "U posudi umutite bademovo mlijeko, chia sjemenke i javorov sirup.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Let sit for 5 minutes; whisk again to prevent clumping.", "Ostavi da stoji 5 minuta, zatim ponovo promijesaj da se sprijece grudvice.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Cover and refrigerate for at least 2 hours or overnight.", "Pokrij i stavi u frizider najmanje 2 sata ili preko noci.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Stir before serving; top with fruits or nuts if desired.", "Promijesaj prije serviranja; po zelji dodaj voce ili orasaste plodove.", "vanilla_sponge_step5_fold.jpg"));
                break;
            case "icebox_cake":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-3 cups heavy whipping cream\n-½ cup granulated sugar\n-1 teaspoon vanilla extract\n-1 (9-ounce) package chocolate wafer cookies", "Sastojci:\n-3 solje slatke pavlake za slag\n-½ solje kristal secera\n-1 kasicica ekstrakta vanilije\n-1 pakovanje (255g) cokoladnih keksica", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a bowl, whip cream with sugar and vanilla until stiff peaks form.", "U posudi umutite pavlaku sa secerom i vanilijom dok ne dobijete cvrste vrhove.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Spread a thin layer of whipped cream on the bottom of a 9x13-inch dish.", "Namazi tanak sloj slaga na dno posude velicine 23x33 cm.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Layer cookies over the cream, then spread another layer of cream. Repeat layers, ending with cream on top.", "Poredaj kekse preko slaga, zatim nanesi novi sloj slaga. Ponavljaj slojeve, zavrsavajuci slagom na vrhu.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Cover and refrigerate for at least 6 hours or overnight.", "Pokrij i stavi u frizider najmanje 6 sati ili preko noci.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Garnish with crushed cookies or cocoa powder before serving.", "Ukrasite zdrobljenim keksima ili kakaom prije serviranja.", "vanilla_sponge_step6_heat_milk.jpg"));
                break;
            case "energy_bites":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-1 cup old-fashioned oats\n-⅔ cup toasted shredded coconut\n-½ cup creamy peanut butter\n-½ cup ground flaxseed\n-½ cup semisweet chocolate chips\n-⅓ cup honey\n-1 tablespoon chia seeds (optional)\n-1 teaspoon vanilla extract", "Sastojci:\n-1 solja ovsenih pahuljica\n-⅔ solje tostiranog kokosovog brasna\n-½ solje kremastog putera od kikirikija\n-½ solje mljevenog lanenog sjemena\n-½ solje poluslatkih cokoladnih kapljica\n-⅓ solje meda\n-1 kasika chia sjemenki (po zelji)\n-1 kasicica ekstrakta vanilije", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a large bowl, combine all ingredients until well mixed.", "U velikoj posudi pomijesaj sve sastojke dok se dobro ne sjedine.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Roll into 1-inch balls and place on a baking sheet.", "Oblikuj kuglice velicine 2.5 cm i stavi ih na pleh.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Refrigerate for at least 30 minutes before serving.", "Ohladi u frizideru najmanje 30 minuta prije serviranja.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Store in an airtight container in the refrigerator for up to a week.", "cuvaj u hermeticki zatvorenoj posudi u frizideru do sedam dana.", "vanilla_sponge_step5_fold.jpg"));
                break;

            //FROZEN TREATS

            case "ice_cream":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-2 cups heavy whipping cream\n-1 (14 oz) can sweetened condensed milk\n-2 teaspoons vanilla extract", "Sastojci:\n-2 solje slatke pavlake za slag\n-1 konzerva (397g) zasladenog kondenzovanog mlijeka\n-2 kasicice ekstrakta vanilije", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a large bowl, whip the heavy cream until stiff peaks form.", "U velikoj posudi umutite pavlaku dok ne dobijete cvrste vrhove.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "In another bowl, combine sweetened condensed milk and vanilla extract.", "U drugoj posudi pomijesaj zasladeno kondenzovano mlijeko i ekstrakt vanilije.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Gently fold the whipped cream into the condensed milk mixture until well combined.", "Pazljivo umijesaj umucenu pavlaku u smjesu s kondenzovanim mlijekom dok se ne sjedini.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Pour the mixture into a loaf pan or airtight container.", "Izlij smjesu u kalup za hljeb ili hermeticki zatvorenu posudu.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Cover and freeze for at least 6 hours or until firm.", "Pokrij i zamrzni najmanje 6 sati ili dok se ne stegne.", "vanilla_sponge_step2_preheat.jpg"));
                break;
            case "sorbet":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-2 cups fresh fruit puree (e.g., strawberries, mangoes)\n-1 cup water\n-¾ cup granulated sugar\n-1 tablespoon lemon juice", "Sastojci:\n-2 solje pirea od svjezeg voca (npr. jagode, mango)\n-1 solja vode\n-¾ solje kristal secera\n-1 kasika limunovog soka", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a saucepan, combine water and sugar. Heat until sugar dissolves to make a simple syrup. Let it cool.", "U serpici pomijesaj vodu i secer. Zagrij dok se secer ne otopi da dobijes jednostavni sirup. Ohladi.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Blend the fresh fruit to make a puree.", "Izmiksaj svjeze voce u pire.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Mix the fruit puree, simple syrup, and lemon juice together.", "Pomijesaj vocni pire, jednostavni sirup i limunov sok.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Pour the mixture into a shallow dish and freeze.", "Izlij smjesu u plitku posudu i zamrzni.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Every 30 minutes, stir the mixture with a fork to break up ice crystals, until fully frozen.", "Svakih 30 minuta promijesaj smjesu viljuskom kako bi razbio kristale leda, dok se potpuno ne zamrzne.", "vanilla_sponge_step6_heat_milk.jpg"));
                break;
            case "popsicles":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-1 cup fresh fruit (e.g., strawberries, kiwi, pineapple)\n-½ cup water\n-2 tablespoons sugar", "Sastojci:\n-1 solja svjezeg voca (npr. jagode, kivi, ananas)\n-½ solje vode\n-2 kasike secera", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Blend the fruit, water, and sugar until smooth.", "Izmiksaj voce, vodu i secer dok ne dobijes glatku smjesu.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Pour the mixture into popsicle molds.", "Sipaj smjesu u kalupe za sladoled na stapicu.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Insert sticks and freeze for at least 4 hours or until solid.", "Ubaci stapice i zamrzni najmanje 4 sata ili dok se potpuno ne stvrdne.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "To release, run warm water over the outside of the molds for a few seconds.", "Da ih izvadis, pusti mlaku vodu preko spoljasnjosti kalupa nekoliko sekundi.", "vanilla_sponge_step5_fold.jpg"));
                break;
            case "frozen_yogurt":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-1 cup frozen blueberries\n-1 cup frozen strawberries\n-1 cup frozen raspberries\n-1 cup vanilla yogurt\n-3 tablespoons honey", "Sastojci:\n-1 solja zamrznutih borovnica\n-1 solja zamrznutih jagoda\n-1 solja zamrznutih malina\n-1 solja vanilija jogurta\n-3 kasike meda", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Combine all ingredients in a food processor and blend until smooth.", "Stavi sve sastojke u blender ili procesor hrane i miksaj dok ne dobijes glatku smjesu.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Transfer to a container and freeze for at least 2 hours.", "Premjesti smjesu u posudu i zamrzni najmanje 2 sata.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Scoop and serve.", "Serviraj koristeci kasiku za sladoled.", "vanilla_sponge_step4_sift.jpg"));
                break;
            case "ice_cream_sandwiches":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-6 chocolate cookies\n-1 pint vanilla ice cream\n-1 cup rainbow sprinkles", "Sastojci:\n-6 cokoladnih keksica\n-1 pakovanje (473ml) vanilija sladoleda\n-1 solja sarenih mrvica", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Slice the ice cream into 3 equal discs.", "Isijeci sladoled na 3 jednake ploske.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Place each ice cream slice between two cookies to form sandwiches.", "Stavi svaku plosku sladoleda izmedu dva keksa da napravis sendvice.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Roll the edges in sprinkles to coat.", "Uvaljaj ivice sendvica u sarene mrvice.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Freeze for 30 minutes before serving.", "Zamrzni 30 minuta prije serviranja.", "vanilla_sponge_step5_fold.jpg"));
                break;
            case "granita":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-2 cups fruit juice or coffee\n-½ cup water\n-½ cup sugar", "Sastojci:\n-2 solje vocnog soka ili kafe\n-½ solje vode\n-½ solje secera", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a saucepan, combine water and sugar. Heat until sugar dissolves to make a simple syrup. Let it cool.", "U serpi pomijesaj vodu i secer. Zagrij dok se secer ne otopi da dobijes jednostavni sirup. Ohladi.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Mix the simple syrup with fruit juice or coffee.", "Pomijesaj jednostavni sirup sa vocnim sokom ili kafom.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Pour the mixture into a shallow metal pan and freeze.", "Izlij smjesu u plitku metalnu posudu i zamrzni.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Every 30 minutes, scrape the mixture with a fork to create a granular texture, until fully frozen.", "Svakih 30 minuta struzite smjesu viljuskom kako biste dobili zrnastu teksturu, dok se potpuno ne zamrzne.", "vanilla_sponge_step5_fold.jpg"));
                break;
            case "ice_cream_cake":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-10 Oreo cookies (110g)\n-2 tablespoons (28g) unsalted butter, melted\n-1.5 quarts (1.42L) chocolate ice cream, softened\n-1 cup (8 ounces/227g) homemade hot fudge (or store-bought), divided\n-1.5 quarts (1.42L) strawberry ice cream, softened\n-1 pint (473ml) heavy whipping cream\n-3 tablespoons powdered sugar\n-2 teaspoons vanilla extract", "Sastojci:\n-10 Oreo keksica (110g)\n-2 kasike (28g) otopljenog neslanog maslaca\n-1.42L cokoladnog sladoleda, omeksanog\n-1 solja (227g) domaceg toplog cokoladnog preljeva (ili kupovni), podijeljeno\n-1.42L jagoda sladoleda, omeksanog\n-473ml slatke pavlake\n-3 kasike secera u prahu\n-2 kasicice ekstrakta vanilije", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Line a 9×3-inch springform pan with plastic wrap, leaving overhang for easy removal.", "Oblozi kalup s odvojivim obodom (23x8 cm) plasticnom folijom, ostavljajuci rubove da vire radi lakseg vadenja.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Crush Oreo cookies and mix with melted butter. Press the mixture into the bottom of the prepared pan to form the crust.", "Izmrvi Oreo keksice i pomijesaj s otopljenim maslacem. Utisni smjesu na dno pripremljenog kalupa za koru.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Spread softened chocolate ice cream over the crust and smooth the top. Freeze for 30 minutes.", "Premazi omeksani cokoladni sladoled preko kore i poravnaj vrh. Zamrzni 30 minuta.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Pour half of the hot fudge over the chocolate ice cream layer and spread evenly. Freeze for another 30 minutes.", "Prelij polovinu cokoladnog preljeva preko cokoladnog sladoleda i ravnomjerno rasporedi. Zamrzni jos 30 minuta.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Spread softened strawberry ice cream over the fudge layer and smooth the top. Freeze for 30 minutes.", "Premazi omeksani jagoda sladoled preko sloja cokolade i poravnaj vrh. Zamrzni 30 minuta.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Pour the remaining hot fudge over the strawberry layer and spread evenly. Freeze for at least 12 hours or overnight.", "Prelij ostatak cokoladnog preljeva preko sloja od jagoda i ravnomjerno rasporedi. Zamrzni najmanje 12 sati ili preko noci.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "In a bowl, whip the heavy cream with powdered sugar and vanilla extract until stiff peaks form.", "U posudi umutite pavlaku sa secerom u prahu i vanilijom dok ne dobijete cvrste vrhove.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Remove the cake from the pan using the plastic wrap overhang and place it on a serving plate. Frost the top and sides with whipped cream.", "Izvadi tortu iz kalupa pomocu folije i stavi na tanjir za serviranje. Premazi vrh i stranice umucenom pavlakom.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 10, "Decorate with additional crushed Oreos or sprinkles if desired.", "Ukrasite dodatno zdrobljenim Oreo keksima ili sarenim mrvicama po zelji.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 11, "Freeze for 1 hour before serving.", "Zamrzni jos 1 sat prije serviranja.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "milkshake":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-2 cups vanilla ice cream\n-1 cup whole milk\n-1 teaspoon vanilla extract\n-Optional toppings: whipped cream, sprinkles, cherry", "Sastojci:\n-2 solje vanilija sladoleda\n-1 solja punomasnog mlijeka\n-1 kasicica ekstrakta vanilije\n-Dodaci po zelji: slag, sarene mrvice, tresnja", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a blender, combine vanilla ice cream, whole milk, and vanilla extract.", "U blenderu pomijesaj vanilija sladoled, punomasno mlijeko i ekstrakt vanilije.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Blend on low speed until smooth and creamy.", "Miksaj na niskoj brzini dok smjesa ne postane glatka i kremasta.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Pour the milkshake into a tall glass.", "Sipaj milkshake u visoku casu.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Top with whipped cream, sprinkles, and a cherry if desired.", "Po vrhu dodaj slag, sarene mrvice i tresnju po zelji.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Serve immediately with a straw.", "Serviraj odmah uz slamku.", "vanilla_sponge_step6_heat_milk.jpg"));
                break;

            //CANDY AND CONFECTIONS
            case "fudge":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-2 cups semisweet chocolate chips\n-1 (14 oz) can sweetened condensed milk\n-1 teaspoon vanilla extract", "Sastojci:\n-2 solje poluslatkih cokoladnih kapljica\n-1 konzerva (397g) zasladenog kondenzovanog mlijeka\n-1 kasicica ekstrakta vanilije", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a saucepan over low heat, combine chocolate chips and sweetened condensed milk.", "U serpi na laganoj vatri pomijesaj cokoladne kapljice i zasladeno kondenzovano mlijeko.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Stir continuously until the chocolate melts and the mixture is smooth.", "Stalno mijesaj dok se cokolada ne otopi i smjesa ne postane glatka.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Remove from heat and stir in vanilla extract.", "Skini s vatre i dodaj ekstrakt vanilije.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Pour the mixture into a greased 8x8-inch pan.", "Izlij smjesu u podmazan kalup 20x20 cm.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Let it set at room temperature for at least 4 hours before cutting into squares.", "Ostavi da se stegne na sobnoj temperaturi najmanje 4 sata prije sjecenja na kocke.", "vanilla_sponge_step6_heat_milk.jpg"));
                break;
            case "caramel":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-1 cup granulated sugar\n-6 tablespoons unsalted butter, cut into pieces\n-½ cup heavy cream\n-1 teaspoon salt (optional)", "Sastojci:\n-1 solja kristal secera\n-6 kasika neslanog maslaca, isjecenog\n-½ solje slatke pavlake\n-1 kasicica soli (po zelji)", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a medium saucepan over medium heat, melt the sugar, stirring constantly until it turns a deep amber color.", "U srednje velikoj serpi na srednjoj vatri otopi secer uz stalno mijesanje dok ne poprimi tamnu jantarnu boju.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Carefully add the butter, stirring until melted.", "Pazljivo dodaj maslac i mijesaj dok se ne otopi.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Slowly pour in the heavy cream while stirring. Be cautious as the mixture will bubble vigorously.", "Polako sipaj slatku pavlaku uz mijesanje. Pazi jer ce smjesa snazno kljucati.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Allow the mixture to boil for 1 minute, then remove from heat.", "Pusti da smjesa kuha 1 minut, zatim skini s vatre.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Stir in salt if desired. Let cool before using.", "Dodaj so po zelji. Ohladi prije upotrebe.", "vanilla_sponge_step6_heat_milk.jpg"));
                break;
            case "toffee":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-2 cups unsalted butter\n-2 cups white sugar\n-¼ teaspoon salt\n-2 cups semisweet chocolate chips\n-1 cup finely chopped almonds", "Sastojci:\n-2 solje neslanog maslaca\n-2 solje bijelog secera\n-¼ kasicice soli\n-2 solje poluslatkih cokoladnih kapljica\n-1 solja sitno sjeckanih badema", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a heavy saucepan over medium heat, combine butter, sugar, and salt.", "U teskoj serpi na srednjoj vatri pomijesaj maslac, secer i so.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Stir until the mixture reaches 150°C (300°F) (hard crack stage).", "Mijesaj dok smjesa ne dostigne 150°C (stadij tvrdog pucanja).", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Pour the mixture onto a greased baking sheet and spread evenly.", "Izlij smjesu na podmazan pleh i ravnomjerno rasporedi.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Sprinkle chocolate chips over the hot toffee and let sit for a few minutes until melted.", "Pospi cokoladne kapljice po vrucem toffeeju i ostavi nekoliko minuta da se otope.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Spread the melted chocolate evenly and sprinkle chopped almonds on top.", "Ravnomjerno rasporedi otopljenu cokoladu i pospi sjeckanim bademima.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Allow to cool completely before breaking into pieces.", "Ostavi da se potpuno ohladi prije nego sto polomis na komade.", "vanilla_sponge_step7_add_milk.jpg"));
                break;
            case "chocolate_bark":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-12 ounces quality chocolate (bittersweet or semisweet)\n-¾ cup raw nuts or seeds (e.g., almonds, pecans)\n-¼ cup dried cranberries or other dried fruit\n-½ teaspoon flaky sea salt (optional)", "Sastojci:\n-340g kvalitetne cokolade (gorka ili poluslatka)\n-¾ solje sirovih orasastih plodova ili sjemenki (npr. bademi, pekan orah)\n-¼ solje suhih brusnica ili drugog suhog voca\n-½ kasicice krupne morske soli (po zelji)", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Melt the chocolate in a double boiler or microwave until smooth.", "Otopi cokoladu na pari ili u mikrotalasnoj dok ne postane glatka.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Spread the melted chocolate onto a parchment-lined baking sheet.", "Razmazi otopljenu cokoladu po plehu oblozenom papirom za pecenje.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Sprinkle nuts, dried fruit, and sea salt over the chocolate.", "Pospi orasaste plodove, suho voce i morsku so po cokoladi.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Let it set at room temperature or refrigerate until firm.", "Ostavi na sobnoj temperaturi ili u frizideru dok se ne stegne.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Break into pieces and store in an airtight container.", "Polomi na komade i cuvaj u hermeticki zatvorenoj posudi.", "vanilla_sponge_step6_heat_milk.jpg"));
                break;
            case "truffles":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-8 ounces semi-sweet or bittersweet chocolate, finely chopped\n-⅔ cup heavy cream\n-1 tablespoon unsalted butter (optional)\n-½ teaspoon vanilla extract (optional)\n-Cocoa powder, sprinkles, or chopped nuts for coating", "Sastojci:\n-225g poluslatke ili gorke cokolade, sitno nasjeckane\n-⅔ solje slatke pavlake\n-1 kasika neslanog maslaca (po zelji)\n-½ kasicice ekstrakta vanilije (po zelji)\n-Kakao, sarene mrvice ili sjeckani orasi za oblaganje", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Heat the heavy cream in a saucepan until it begins to simmer.", "Zagrij slatku pavlaku u serpi dok ne pocne da vrije.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Pour the hot cream over the chopped chocolate and let sit for 5 minutes.", "Prelij vrucu pavlaku preko nasjeckane cokolade i ostavi 5 minuta.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Stir until smooth, then add butter and vanilla if using.", "Mijesaj dok smjesa ne postane glatka, zatim dodaj maslac i vaniliju ako koristis.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Refrigerate the mixture for at least 2 hours until firm.", "Stavi smjesu u frizider najmanje 2 sata dok se ne stegne.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Scoop and roll into balls, then coat with desired toppings.", "Vadi kasikom i oblikuj kuglice, zatim ih uvaljaj u zeljeni dodatak.", "vanilla_sponge_step6_heat_milk.jpg"));
                break;
            case "peanut_brittle":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-1 cup white sugar\n-½ cup light corn syrup\n-¼ cup water\n-¼ teaspoon salt\n-1 cup peanuts\n-2 tablespoons unsalted butter\n-1 teaspoon baking soda", "Sastojci:\n-1 solja bijelog secera\n-½ solje svijetlog kukuruznog sirupa\n-¼ solje vode\n-¼ kasicice soli\n-1 solja kikirikija\n-2 kasike neslanog maslaca\n-1 kasicica sode bikarbone", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a saucepan, combine sugar, corn syrup, water, and salt. Cook over medium heat until the mixture reaches 150°C (300°F).", "U serpi pomijesaj secer, kukuruzni sirup, vodu i so. Kuhaj na srednjoj temperaturi dok smjesa ne dostigne 150°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Stir in peanuts and butter, then remove from heat.", "Dodaj kikiriki i maslac, promijesaj, pa skloni s vatre.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Quickly stir in baking soda, causing the mixture to foam.", "Brzo dodaj sodu bikarbonu i promijesaj – smjesa ce zapjeniti.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Pour onto a greased baking sheet and spread evenly.", "Izlij na podmazan pleh i ravnomjerno rasporedi.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Allow to cool completely before breaking into pieces.", "Ostavi da se potpuno ohladi prije nego sto polomis na komade.", "vanilla_sponge_step6_heat_milk.jpg"));
                break;
            case "marshmallows":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-2 envelopes (0.25 oz each) unflavored gelatin\n-1 cup cold water, divided\n-1½ cups granulated sugar\n-1 cup light corn syrup\n-¼ teaspoon salt\n-2 teaspoons vanilla extract\n-Confectioners’ sugar for dusting", "Sastojci:\n-2 kesice (po 7g) zelatine bez ukusa\n-1 solja hladne vode, podijeljena\n-1½ solje kristal secera\n-1 solja svijetlog kukuruznog sirupa\n-¼ kasicice soli\n-2 kasicice ekstrakta vanilije\n-secer u prahu za posipanje", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a mixing bowl, sprinkle gelatin over ½ cup cold water and let it bloom.", "U posudi pospi zelatinu preko ½ solje hladne vode i ostavi da nabubri.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "In a saucepan, combine remaining water, sugar, corn syrup, and salt. Cook over medium heat until the mixture reaches 115°C (240°F).", "U serpi pomijesaj preostalu vodu, secer, sirup i so. Kuhaj na srednjoj vatri dok ne dostigne 115°C.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Slowly pour the hot syrup into the gelatin while mixing on low speed.", "Polako sipaj vruci sirup u zelatinu uz mijesanje na niskoj brzini.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Increase speed to high and beat until the mixture is thick and fluffy, about 10-15 minutes.", "Povecaj brzinu na visoku i miksaj dok smjesa ne postane gusta i pjenasta, oko 10–15 minuta.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Add vanilla extract and mix until combined.", "Dodaj vaniliju i kratko promijesaj.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Pour into a greased and powdered sugar-dusted 9x13-inch pan.", "Sipaj u podmazan kalup velicine 23x33 cm posut secerom u prahu.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Let set for at least 4 hours, then cut into squares and dust with more confectioners’ sugar.", "Ostavi najmanje 4 sata da se stegne, zatim isijeci na kocke i ponovo pospi secerom u prahu.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "chocolate_covered_pretzels":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-4 cups mini pretzels\n-8 ounces semi-sweet or white chocolate, chopped\n-½ teaspoon vegetable oil\n-Sprinkles or crushed nuts for decoration (optional)", "Sastojci:\n-4 solje mini pereca\n-225g poluslatke ili bijele cokolade, nasjeckane\n-½ kasicice biljnog ulja\n-sarene mrvice ili sjeckani orasi za dekoraciju (po zelji)", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Melt chocolate with vegetable oil in a microwave-safe bowl, stirring every 30 seconds until smooth.", "Otopi cokoladu s uljem u mikrotalasnoj, mijesajuci svakih 30 sekundi dok ne postane glatka.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Dip pretzels into the melted chocolate, allowing excess to drip off.", "Umoci perece u otopljenu cokoladu i pusti visak da iscuri.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Place on a parchment-lined baking sheet and decorate with sprinkles or nuts if desired.", "Stavi na pleh oblozen papirom za pecenje i ukrasi mrvicama ili orasima.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Let set at room temperature or refrigerate until firm.", "Ostavi na sobnoj temperaturi ili u frizideru dok se ne stegne.", "vanilla_sponge_step5_fold.jpg"));
                break;
            case "candied_nuts":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-3 cups mixed nuts (e.g., cashews, pecans, almonds, peanuts)\n-1 cup granulated sugar\n-¼ cup water\n-1 tablespoon cinnamon\n-1 teaspoon vanilla extract\n-1 teaspoon salt", "Sastojci:\n-3 solje mijesanih orasastih plodova (npr. indijski orah, pekan, bademi, kikiriki)\n-1 solja secera\n-¼ solje vode\n-1 kasika cimeta\n-1 kasicica ekstrakta vanilije\n-1 kasicica soli", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Heat a large frying pan or deep pot over medium heat.", "Zagrij veliku tavu ili dublju serpu na srednjoj vatri.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Add sugar, water, cinnamon, salt, and vanilla extract; stir until fully combined.", "Dodaj secer, vodu, cimet, so i vaniliju; mijesaj dok se ne sjedini.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Once the sugar has melted, add the nuts and mix until fully coated.", "Kada se secer otopi, dodaj orasaste plodove i mijesaj dok se ne obloze.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Continue stirring frequently until the sugar begins to crystallize and coats the nuts.", "Nastavi cesto mijesati dok se secer ne kristalizuje i ne zalijepi za orahe.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Remove from heat and let sit for 1–2 minutes. Stir to break up clusters.", "Skini s vatre i ostavi 1–2 minute. Promijesaj da razdvojis grupice.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Transfer nuts to a parchment-lined surface to cool completely.", "Prebaci orasaste plodove na papir za pecenje da se potpuno ohlade.", "vanilla_sponge_step7_add_milk.jpg"));
                break;
            case "rock_candy":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-6 cups granulated sugar\n-2 cups water\n-2–3 drops food coloring (optional)\n-½ to 1 teaspoon flavoring extract (e.g., vanilla, peppermint)", "Sastojci:\n-6 solja kristal secera\n-2 solje vode\n-2–3 kapi prehrambene boje (po zelji)\n-½ do 1 kasicica arome (npr. vanilija, menta)", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a saucepan, bring water to a boil. Gradually add sugar, stirring until fully dissolved.", "U serpi zagrij vodu do kljucanja. Postepeno dodaj secer uz mijesanje dok se potpuno ne otopi.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Remove from heat; add food coloring and flavoring extract.", "Skini sa vatre; dodaj prehrambenu boju i aromu.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Allow the solution to cool slightly, then pour into clean glass jars.", "Ostavi da se smjesa malo ohladi, zatim sipaj u ciste staklene tegle.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Insert a sugar-coated stick or string into each jar, ensuring it doesn't touch the sides or bottom.", "U svaku teglu ubaci stapic ili konac umocen u secer, pazeci da ne dodiruje dno ili strane.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Cover jars loosely with paper towels and let sit undisturbed for 5–7 days to allow crystals to form.", "Tegle lagano pokrij papirnim ubrusima i ostavi da miruju 5–7 dana da se kristali formiraju.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Once crystals have formed, remove the sticks and let them dry before enjoying.", "Kada se kristali formiraju, izvadi stapice i ostavi da se osuse prije konzumacije.", "vanilla_sponge_step7_add_milk.jpg"));
                break;

            //INTERNATIONAL SWEETS
            case "mochi":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-1 cup glutinous rice flour\n-1/4 cup sugar\n-3/4 cup water\n-Cornstarch or potato starch for dusting", "Sastojci:\n-1 solja ljepljivog rizinog brasna\n-¼ solje secera\n-¾ solje vode\n-Kukuruzni ili krompirov skrob za posipanje", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a microwave-safe bowl, mix glutinous rice flour and sugar.", "U zdjeli pogodnoj za mikrotalasnu pomijesaj rizino brasno i secer.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Add water and stir until smooth.", "Dodaj vodu i mijesaj dok ne dobijes glatku smjesu.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Cover the bowl with plastic wrap and microwave for 1 minute.", "Pokrij zdjelu plasticnom folijom i stavi u mikrotalasnu na 1 minut.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Stir the mixture with a wet spatula.", "Promijesaj smjesu mokrom spatulom.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Repeat microwaving and stirring two more times, for a total of 3 minutes.", "Ponovi zagrijavanje i mijesanje jos dva puta, ukupno 3 minute.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Dust a surface with cornstarch and transfer the mochi onto it.", "Pospi radnu povrsinu skrobom i prebaci mochi na nju.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Let it cool slightly, then cut into pieces and shape as desired.", "Pusti da se malo ohladi, zatim isijeci i oblikuj po zelji.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "gulab_jamun":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-1 cup milk powder\n-3 tbsp all-purpose flour\n-1½ tsp baking powder\n-1 egg (or 3-4 tbsp milk as a substitute)\n-3 tbsp heavy cream\n-2 tbsp melted ghee\n-1¼ cups water\n-1 cup sugar\n-1–2 cardamom pods\n-Optional: saffron and rose water", "Sastojci:\n-1 solja mlijeka u prahu\n-3 kasike brasna\n-1½ kasicica praska za pecivo\n-1 jaje (ili 3–4 kasike mlijeka kao zamjena)\n-3 kasike slatke pavlake\n-2 kasike otopljenog gheeja\n-1¼ solje vode\n-1 solja secera\n-1–2 mahune kardamoma\n-Po zelji: safran i ruzina vodica", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a bowl, mix milk powder, flour, and baking powder.", "U zdjeli pomijesaj mlijeko u prahu, brasno i prasak za pecivo.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Add egg, heavy cream, and melted ghee; mix to form a soft dough.", "Dodaj jaje, pavlaku i otopljeni ghee; umijesi mekano tijesto.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Divide the dough into small balls.", "Podijeli tijesto na male kuglice.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "In a saucepan, combine water, sugar, and cardamom; bring to a boil to make syrup.", "U serpi pomijesaj vodu, secer i kardamom; zakuhaj da dobijes sirup.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Fry the dough balls in oil until golden brown.", "Przi kuglice u ulju dok ne dobiju zlatno-smedu boju.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Soak the fried balls in warm syrup for at least 30 minutes before serving.", "Potopi przene kuglice u topao sirup i ostavi najmanje 30 minuta prije serviranja.", "vanilla_sponge_step7_add_milk.jpg"));
                break;
            case "churros":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-1 cup water\n-6 tbsp unsalted butter\n-1 tbsp granulated sugar\n-½ tsp salt\n-1 cup all-purpose flour\n-3 large eggs\n-1 tsp vanilla extract\n-Vegetable oil for frying\n-¾ cup sugar mixed with 2 tsp cinnamon for coating", "Sastojci:\n-1 solja vode\n-6 kasika neslanog maslaca\n-1 kasika secera\n-½ kasicice soli\n-1 solja brasna\n-3 velika jaja\n-1 kasicica ekstrakta vanilije\n-Biljno ulje za przenje\n-¾ solje secera pomijesanog sa 2 kasicice cimeta za posipanje", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a saucepan, combine water, butter, sugar, and salt; bring to a boil.", "U serpi pomijesaj vodu, maslac, secer i so; dovedi do kljucanja.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Add flour all at once; stir vigorously until the mixture forms a ball.", "Dodaj svo brasno odjednom; snazno mijesaj dok ne dobijes kuglu od tijesta.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Remove from heat; let it cool slightly.", "Skini s vatre i pusti da se malo ohladi.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Beat in eggs one at a time, then add vanilla extract.", "Dodaj jaja jedno po jedno i umuti, zatim dodaj ekstrakt vanilije.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Transfer dough to a piping bag fitted with a star tip.", "Premjesti tijesto u slasticarsku vrecicu s nastavkom u obliku zvjezdice.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Heat oil in a deep fryer or large pot to 190°C (375°F).", "Zagrij ulje u fritezi ili dubokoj serpi na 190°C.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Pipe strips of dough into the hot oil; fry until golden brown.", "Istisni trake tijesta u vruce ulje; przi dok ne porumene.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Drain on paper towels, then roll in cinnamon sugar.", "Ocijedi na papirnom ubrusu, zatim uvaljaj u secer s cimetom.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "cannoli":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\nFor the shells:\n-3 cups all-purpose flour\n-¼ cup white sugar\n-¼ tsp ground cinnamon\n-3 tbsp shortening\n-½ cup sweet Marsala wine\n-2 tbsp water\n-1 tbsp distilled white vinegar\n-1 large egg\n-1 egg yolk\n-1 egg white\n-Oil for frying\nFor the filling:\n-32 oz ricotta cheese, drained\n-¾ cup confectioners' sugar\n-1 tsp vanilla extract\n-½ cup mini chocolate chips", "Sastojci:\nZa korice:\n-3 solje brasna\n-¼ solje bijelog secera\n-¼ kasicice cimeta\n-3 kasike masti ili skracenog maslaca\n-½ solje slatkog Marsala vina\n-2 kasike vode\n-1 kasika bijelog sirceta\n-1 veliko jaje\n-1 zumance\n-1 bjelance\n-Ulje za przenje\nZa fil:\n-900g ricotta sira, ocijedenog\n-¾ solje secera u prahu\n-1 kasicica ekstrakta vanilije\n-½ solje mini cokoladnih kapljica", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a bowl, combine flour, sugar, and cinnamon.", "U zdjeli pomijesaj brasno, secer i cimet.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Cut in shortening until the mixture resembles coarse crumbs.", "Dodaj mast i utrljaj dok smjesa ne podsjeca na krupne mrvice.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Add Marsala wine, water, vinegar, egg, and egg yolk; mix to form a dough.", "Dodaj Marsala vino, vodu, sirce, jaje i zumance; zamijesi tijesto.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Knead the dough until smooth; cover and let rest for 1 hour.", "Mijesi tijesto dok ne postane glatko; pokrij i ostavi da odmori 1 sat.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Roll out the dough thinly; cut into circles.", "Razvuci tijesto tanko i izrezi krugove.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Wrap circles around cannoli forms; seal edges with egg white.", "Uvij krugove oko kalupa za cannoli i zatvori rubove bjelancetom.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Fry in hot oil until golden brown; drain and let cool.", "Przi u vrucem ulju dok ne postanu zlatni; ocijedi i ohladi.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "In a bowl, mix ricotta, confectioners' sugar, vanilla, and chocolate chips.", "U zdjeli pomijesaj ricottu, secer u prahu, vaniliju i cokoladne kapljice.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 10, "Fill cooled shells with the ricotta mixture just before serving.", "Napuni ohladene korice filom neposredno prije serviranja.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "baklava":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-1 pound chopped nuts (almonds, walnuts, or pistachios)\n-1 pound phyllo dough, thawed\n-1 cup butter, melted\n-1/3 cup sugar\n-1 tsp ground cinnamon\n-1/3 tsp ground cloves\nFor the syrup:\n-1 cup water\n-1 cup sugar\n-1/2 cup honey\n-2 tbsp lemon juice\n-1 cinnamon stick", "Sastojci:\n-450g sjeckanih orasastih plodova (bademi, orasi ili pistaci)\n-450g jufke (phyllo tijesto), odmrznute\n-1 solja otopljenog maslaca\n-1/3 solje secera\n-1 kasicica cimeta\n-1/3 kasicice mljevenih karanfilica\nZa sirup:\n-1 solja vode\n-1 solja secera\n-½ solje meda\n-2 kasike limunovog soka\n-1 stapic cimeta", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C (350°F).", "Zagrij rernu na 175°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "In a bowl, mix chopped nuts, sugar, cinnamon, and cloves.", "U zdjeli pomijesaj orahe, secer, cimet i karanfilic.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Brush a 9x13-inch baking dish with melted butter.", "Premazi pleh velicine 23x33 cm otopljenim maslacem.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Place one sheet of phyllo dough in the dish; brush with butter.", "Stavi jedan list jufke u pleh i premazi maslacem.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Repeat layering and buttering for 8 sheets.", "Ponavljaj slaganje i premazivanje s ukupno 8 listova.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Sprinkle a thin layer of the nut mixture over the phyllo.", "Pospi tanki sloj smjese s orasima preko jufke.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Continue layering phyllo sheets and nuts, ending with 8 layers of phyllo on top.", "Nastavi slaganje jufke i oraha, zavrsavajuci s 8 slojeva jufke na vrhu.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Cut into diamond or square shapes.", "Isijeci na rombove ili kvadrate.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 10, "Bake for 50 minutes or until golden and crisp.", "Peci 50 minuta ili dok ne porumeni i postane hrskava.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 11, "In a saucepan, combine water, sugar, honey, lemon juice, and cinnamon stick; bring to a boil, then simmer for 10 minutes.", "U serpi pomijesaj vodu, secer, med, limunov sok i stapic cimeta; zakuhaj, zatim krckaj 10 minuta.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 12, "Remove cinnamon stick and pour hot syrup over the baked baklava.", "Ukloni stapic cimeta i prelij vruc sirup preko pecene baklave.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 13, "Let cool completely before serving.", "Ohladi potpuno prije serviranja.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "alfajores":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-1 cup (125g) all-purpose flour\n-1 cup (125g) cornstarch\n-½ tsp baking powder\n-¼ tsp salt\n-½ cup (115g) unsalted butter, softened\n-⅓ cup (40g) powdered sugar\n-2 egg yolks\n-1 tsp vanilla extract\n-Zest of 1 lemon (optional)\n-1 cup (300g) dulce de leche\n-½ cup (50g) desiccated coconut (for rolling)", "Sastojci:\n-1 solja (125g) brasna\n-1 solja (125g) gustina\n-½ kasicice praska za pecivo\n-¼ kasicice soli\n-½ solje (115g) neslanog maslaca, omeksanog\n-⅓ solje (40g) secera u prahu\n-2 zumanca\n-1 kasicica ekstrakta vanilije\n-Korica 1 limuna (po zelji)\n-1 solja (300g) dulce de leche\n-½ solje (50g) kokosa za uvaljati", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a bowl, whisk together flour, cornstarch, baking powder, and salt.", "U zdjeli pomijesaj brasno, gustin, prasak za pecivo i so.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "In another bowl, beat butter and powdered sugar until light and fluffy.", "U drugoj zdjeli umuti maslac i secer u prahu dok ne postanu pjenasti.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Add egg yolks, vanilla extract, and lemon zest (if using) to the butter mixture; mix until combined.", "Dodaj zumanca, vaniliju i koricu limuna (ako koristis) u smjesu s maslacem; promijesaj.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Gradually add the dry ingredients to the wet ingredients, mixing until a soft dough forms.", "Postepeno dodaj suhe sastojke u mokre dok ne dobijes mekano tijesto.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Divide the dough in half, shape into disks, wrap in plastic wrap, and refrigerate for at least 1 hour.", "Podijeli tijesto na pola, oblikuj diskove, umotaj u foliju i ostavi u frizideru najmanje 1 sat.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Preheat oven to 175°C (350°F). Line baking sheets with parchment paper.", "Zagrij rernu na 175°C. Oblozi pleh papirom za pecenje.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Roll out the dough to ¼-inch thickness and cut into 2-inch circles.", "Razvuci tijesto na 0.5 cm debljine i izrezi krugove od 5 cm.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Place cookies on prepared baking sheets and bake for 10–12 minutes, or until edges are lightly golden.", "Poredaj kolacice na pleh i peci 10–12 minuta dok rubovi blago ne porumene.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 10, "Allow cookies to cool completely.", "Pusti da se kolaci potpuno ohlade.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 11, "Spread dulce de leche on the flat side of half the cookies and top with the remaining cookies to form sandwiches.", "Namazi dulce de leche na donju stranu pola kolacica i prekrij drugom polovinom.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 12, "Roll the edges in desiccated coconut.", "Uvaljaj ivice u kokos.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "brigadeiros":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-1 (14-ounce) can sweetened condensed milk\n-3 tbsp unsweetened cocoa powder\n-1 tbsp unsalted butter\n-Chocolate sprinkles (for rolling)", "Sastojci:\n-1 konzerva (397g) zasladenog kondenzovanog mlijeka\n-3 kasike kakaa u prahu bez secera\n-1 kasika neslanog maslaca\n-cokoladne mrvice (za valjanje)", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a medium saucepan over medium heat, combine sweetened condensed milk, cocoa powder, and butter.", "U srednjoj serpi na srednjoj vatri pomijesaj kondenzovano mlijeko, kakao i maslac.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Cook, stirring constantly, until the mixture thickens and starts to pull away from the sides of the pan (about 10–15 minutes).", "Kuhaj uz stalno mijesanje dok se smjesa ne zgusne i pocne odvajati od stranica serpe (oko 10–15 minuta).", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Remove from heat and let cool slightly.", "Skini s vatre i ostavi da se malo ohladi.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Once cool enough to handle, grease your hands with butter and roll the mixture into small balls (about 1 inch in diameter).", "Kada se dovoljno ohladi, namasti ruke i oblikuj male kuglice (otprilike 2.5 cm).", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Roll each ball in chocolate sprinkles to coat.", "Uvaljaj svaku kuglicu u cokoladne mrvice.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Place brigadeiros in paper cups and refrigerate until firm.", "Stavi brigadeiro kuglice u papirne korpice i ohladi u frizideru dok se ne stegnu.", "vanilla_sponge_step7_add_milk.jpg"));
                break;
            case "tteok":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-2 cups (300g) sweet rice flour (also called glutinous rice flour)\n-⅓ cup (65g) sugar\n-1⅓ cups (320ml) water\n-1 tbsp vegetable oil (for brushing)\n-Pinch of salt\n-Optional toppings: crushed roasted sesame seeds, chopped dried fruits, red bean paste, or shredded coconut", "Sastojci:\n-2 solje (300g) ljepljivog rizinog brasna\n-⅓ solje (65g) secera\n-1⅓ solje (320ml) vode\n-1 kasika biljnog ulja (za premazivanje)\n-Prstohvat soli\n-Po zelji: przeni susam, suho voce, pasta od crvenog graha ili kokos", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a large microwave-safe bowl, combine sweet rice flour, sugar, salt, and water; mix until smooth and no lumps remain.", "U velikoj zdjeli pogodnoj za mikrotalasnu pomijesaj rizino brasno, secer, so i vodu; mijesaj dok ne postane glatko bez grudvica.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Cover the bowl loosely with plastic wrap or a microwave-safe plate.", "Lagano prekrij zdjelu plasticnom folijom ili tanjirom pogodnim za mikrotalasnu.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Microwave on high for 2 minutes. Stir the mixture with a wet spoon or spatula.", "Zagrij u mikrovalnoj 2 minute. Promijesaj smjesu vlaznom kasikom ili spatulom.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Microwave again for another 1–2 minutes until the mixture becomes more translucent and sticky.", "Ponovo zagrij jos 1–2 minute dok smjesa ne postane providnija i ljepljiva.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Using a wet spatula or gloved hands, knead the dough briefly in the bowl to ensure it’s uniformly smooth.", "Pomocu vlazne spatule ili rukavica kratko izmijesi tijesto u zdjeli da postane jednolicno glatko.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Transfer the rice cake onto a surface lightly brushed with vegetable oil or lined with parchment.", "Prebaci tijesto na podlogu premazanu uljem ili oblozenu papirom za pecenje.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Flatten and shape into a round or rectangular slab, about 1-inch thick. Let it cool for 5–10 minutes.", "Poravnaj i oblikuj u pravougaonik ili krug, debljine oko 2.5 cm. Pusti da se hladi 5–10 minuta.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Cut into bite-sized pieces using a plastic knife or oiled kitchen scissors.", "Isijeci na komadice pomocu plasticnog noza ili makaza premazanih uljem.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 10, "Roll or top with your choice of sesame seeds, coconut, or sweet fillings like red bean paste.", "Uvaljaj ili pospi sa susamom, kokosom ili zasladenim filom poput paste od crvenog graha.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 11, "Serve warm or at room temperature. Store leftovers in an airtight container and re-steam or microwave before serving.", "Serviraj toplo ili na sobnoj temperaturi. Ostatke cuvaj u hermeticki zatvorenoj posudi i podgrij prije serviranja.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "knafeh":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-1 package (16 ounces) shredded phyllo dough (kataifi), thawed\n-1 cup (225g) unsalted butter, melted\n-2 cups (500g) shredded mozzarella cheese\n-1 cup (250ml) heavy cream\n-1 cup (200g) granulated sugar\n-½ cup (120ml) water\n-1 teaspoon lemon juice\n-1 tablespoon rose water or orange blossom water\n-Crushed pistachios (for garnish)", "Sastojci:\n-1 pakovanje (450g) usitnjene jufke (kataifi), odmrznuto\n-1 solja (225g) otopljenog neslanog maslaca\n-2 solje (500g) rendanog mozzarella sira\n-1 solja (250ml) slatke pavlake\n-1 solja (200g) secera\n-½ solje (120ml) vode\n-1 kasicica limunovog soka\n-1 kasika ruzine vode ili vode narandzinog cvijeta\n-Drobljeni pistaci (za ukras)", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C (350°F).", "Zagrij rernu na 175°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "In a saucepan, combine sugar, water, and lemon juice. Bring to a boil, then simmer for 10 minutes. Remove from heat and stir in rose water or orange blossom water. Let syrup cool.", "U serpi pomijesaj secer, vodu i limunov sok. Zakuhaj, zatim krckaj 10 minuta. Skini s vatre, dodaj ruzinu ili narandzinu vodu i ohladi sirup.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "In a bowl, mix shredded phyllo dough with melted butter until well coated.", "U zdjeli pomijesaj jufku s otopljenim maslacem dok se dobro ne oblozi.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Grease a baking dish and spread half of the buttered phyllo dough evenly in the bottom.", "Podmazi pleh i rasporedi polovinu jufke ravnomjerno na dno.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "In another bowl, combine mozzarella cheese and heavy cream. Spread this mixture over the phyllo layer.", "U drugoj zdjeli pomijesaj mozzarellu i pavlaku, pa rasporedi preko sloja jufke.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Cover the cheese layer with the remaining phyllo dough, pressing down gently.", "Pokrij preostalom jufkom i lagano pritisni.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Bake for 30–40 minutes, or until the top is golden brown.", "Peci 30–40 minuta, dok vrh ne postane zlatno-smed.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "Remove from oven and immediately pour the cooled syrup over the hot knafeh.", "Izvadi iz rerne i odmah prelij ohladenim sirupom.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 10, "Let it soak for a few minutes, then garnish with crushed pistachios. Serve warm.", "Ostavi nekoliko minuta da upije, zatim ukrasi drobljenim pistacima. Serviraj toplo.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "flan":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-¾ cup (150g) granulated sugar (for caramel)\n-1 (8-ounce) package cream cheese, softened\n-5 large eggs\n-1 (14-ounce) can sweetened condensed milk\n-1 (12-ounce) can evaporated milk\n-1 teaspoon vanilla extract", "Sastojci:\n-¾ solje (150g) secera (za karamel)\n-1 pakovanje (225g) krem sira, omeksanog\n-5 velikih jaja\n-1 konzerva (397g) kondenzovanog mlijeka\n-1 konzerva (354ml) evaporiranog mlijeka\n-1 kasicica ekstrakta vanilije", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 175°C (350°F).", "Zagrij rernu na 175°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "In a saucepan over medium heat, melt granulated sugar until it turns golden brown, stirring constantly. Quickly pour the caramel into a round baking dish, tilting to coat the bottom evenly.", "U serpi na srednjoj vatri otopi secer uz stalno mijesanje dok ne postane zlatno-smed. Brzo izlij karamel u okrugli kalup i nagnij da oblozi dno.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "In a blender, combine cream cheese, eggs, sweetened condensed milk, evaporated milk, and vanilla extract. Blend until smooth.", "U blenderu sjedini krem sir, jaja, kondenzovano mlijeko, evaporisano mlijeko i vaniliju. Blendaj dok ne postane glatko.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Pour the mixture over the caramel in the baking dish.", "Izlij smjesu preko karamele u kalupu.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Place the baking dish in a larger pan filled with about 1 inch of hot water (bain-marie).", "Stavi kalup u veci pleh napunjen s oko 2.5 cm vruce vode (vodena kupka).", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Bake for 50–60 minutes, or until the center is set.", "Peci 50–60 minuta, dok se sredina ne stegne.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Remove from oven and let cool. Refrigerate for at least 4 hours or overnight.", "Izvadi iz rerne i ohladi. Onda stavi u frizider najmanje 4 sata ili preko noci.", "vanilla_sponge_step8_bake.jpg"));
                steps.add(new InstructionStep(recipeId, 9, "To serve, run a knife around the edges of the flan, invert onto a serving plate, and let the caramel sauce flow over the top.", "Za serviranje, prodi nozem uz ivice, izvrni flan na tanjir i pusti da karamel prede preko vrha.", "vanilla_sponge_step8_bake.jpg"));
                break;

            //FRUIT BASED DESSERTS
            case "fruit_salad":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-4 cups cantaloupe, cubed\n-4 cups honeydew, cubed\n-4 cups pineapple, cubed\n-1 cup raspberries\n-1 cup blueberries\nDressing:\n-3 tablespoons chopped fresh mint\n-Zest of 1 lime\n-5 tablespoons lime juice (about 4 limes)\n-2 tablespoons honey", "Sastojci:\n-4 solje dinje, na kockice\n-4 solje medene dinje, na kockice\n-4 solje ananasa, na kockice\n-1 solja malina\n-1 solja borovnica\nPreliv:\n-3 kasike svjeze nasjeckane mente\n-Korica 1 limete\n-5 kasika soka od limete (oko 4 limete)\n-2 kasike meda", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a large bowl, combine cantaloupe, honeydew, pineapple, raspberries, and blueberries.", "U velikoj zdjeli pomijesaj dinju, medenu dinju, ananas, maline i borovnice.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "In a small bowl, whisk together chopped mint, lime zest, lime juice, and honey to make the dressing.", "U manjoj zdjeli umutiti mentu, koricu limete, sok od limete i med za preliv.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Pour the dressing over the fruit and gently toss to combine.", "Prelij preliv preko voca i lagano promijesaj da se sjedini.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Serve immediately or refrigerate until ready to serve.", "Serviraj odmah ili rashladi do posluzivanja.", "vanilla_sponge_step5_fold.jpg"));
                break;
            case "baked_apples":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-4 large apples (such as Fuji or Gala)\n-¼ cup brown sugar\n-1 teaspoon ground cinnamon\n-¼ cup chopped walnuts or pecans\n-4 teaspoons butter\n-½ cup hot water", "Sastojci:\n-4 velike jabuke (npr. Fuji ili Gala)\n-¼ solje smedeg secera\n-1 kasicica cimeta\n-¼ solje sjeckanih oraha ili pekana\n-4 kasicice maslaca\n-½ solje vruce vode", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat oven to 190°C (375°F).", "Zagrij rernu na 190°C.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Core the apples, leaving the bottom intact to create a well.", "Izvadi sredinu jabuka, pazeci da dno ostane netaknuto kako bi se dobila udubljenja.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "In a small bowl, mix together brown sugar, cinnamon, and chopped nuts.", "U maloj zdjeli pomijesaj smedi secer, cimet i orahe.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Stuff each apple with the sugar-nut mixture and place a teaspoon of butter on top of each.", "Napuni svaku jabuku smjesom i stavi kasicicu maslaca na vrh.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Place the apples in a baking dish and pour hot water into the bottom of the dish.", "Stavi jabuke u pleh i dodaj vrucu vodu na dno posude.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Bake for 30–45 minutes, until the apples are tender but not mushy.", "Peci 30–45 minuta, dok jabuke ne omeksaju ali da ostanu cvrste.", "vanilla_sponge_step7_add_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 8, "Serve warm, optionally with a scoop of vanilla ice cream.", "Serviraj toplo, po zelji uz kuglu sladoleda od vanilije.", "vanilla_sponge_step8_bake.jpg"));
                break;
            case "poached_pears":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-4 ripe but firm pears, peeled and halved\n-2 cups water\n-½ cup sugar\n-2 tablespoons honey\n-1 cinnamon stick\n-1 vanilla bean, split (or 1 teaspoon vanilla extract)", "Sastojci:\n-4 zrele ali cvrste kruske, oguljene i prepolovljene\n-2 solje vode\n-½ solje secera\n-2 kasike meda\n-1 stapic cimeta\n-1 mahuna vanilije, prerezana (ili 1 kasicica ekstrakta vanilije)", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a saucepan, combine water, sugar, honey, cinnamon stick, and vanilla. Bring to a simmer over medium heat, stirring until sugar dissolves.", "U serpi pomijesaj vodu, secer, med, cimet i vaniliju. Kuhaj na srednjoj vatri dok se secer ne otopi.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Add the pear halves to the saucepan, ensuring they are submerged in the liquid.", "Dodaj polovice krusaka u serpu tako da su potpuno potopljene.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Simmer gently for 25 minutes, or until the pears are tender when pierced with a knife.", "Kuhaj na laganoj vatri 25 minuta, dok kruske ne omeksaju kada se probodu nozem.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Remove the pears and let them cool. Continue simmering the poaching liquid until it reduces to a syrupy consistency.", "Izvadi kruske i ostavi da se ohlade. Nastavi kuhati tecnost dok se ne zgusne u sirup.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Serve the pears drizzled with the reduced syrup.", "Serviraj kruske prelivenim sirupom.", "vanilla_sponge_step6_heat_milk.jpg"));
                break;
            case "grilled_peaches":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-4 ripe peaches, halved and pitted\n-Vegetable oil, for brushing\n-8 scoops vanilla ice cream\n-Honey, for drizzling (optional)", "Sastojci:\n-4 zrele breskve, prepolovljene i bez kospica\n-Biljno ulje za premazivanje\n-8 kugli sladoleda od vanilije\n-Med za prelijevanje (po zelji)", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "Preheat grill to medium-high heat.", "Zagrij rostilj na srednje jaku temperaturu.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Brush the cut sides of the peach halves with vegetable oil.", "Premazi prerezane strane breskvi uljem.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Place peaches cut-side down on the grill and cook for 3–4 minutes, until grill marks appear and peaches are slightly softened.", "Stavi breskve prerezanom stranom na rostilj i peci 3–4 minute dok se ne pojave tragovi i breskve ne omeksaju.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Remove from grill and place each peach half in a serving dish.", "Skini s rostilja i stavi svaku polovicu u posudu za serviranje.", "vanilla_sponge_step5_fold.jpg"));
                steps.add(new InstructionStep(recipeId, 6, "Top each peach half with a scoop of vanilla ice cream.", "Na svaku polovicu stavi kuglu sladoleda od vanilije.", "vanilla_sponge_step6_heat_milk.jpg"));
                steps.add(new InstructionStep(recipeId, 7, "Drizzle with honey, if desired.", "Prelij medom ako zelis.", "vanilla_sponge_step7_add_milk.jpg"));
                break;
            case "berry_parfait":
                steps.add(new InstructionStep(recipeId, 1, "Ingredients:\n-2 cups mixed fresh berries (such as strawberries, blueberries, raspberries)\n-2 cups plain Greek yogurt\n-½ cup granola", "Sastojci:\n-2 solje mijesanog svjezeg bobicastog voca (npr. jagode, borovnice, maline)\n-2 solje grckog jogurta\n-½ solje granole", "vanilla_sponge_step1_ingredients.jpg"));
                steps.add(new InstructionStep(recipeId, 2, "In a glass or bowl, layer ¼ cup of Greek yogurt.", "U casi ili zdjeli stavi sloj od ¼ solje grckog jogurta.", "vanilla_sponge_step2_preheat.jpg"));
                steps.add(new InstructionStep(recipeId, 3, "Add a layer of mixed berries over the yogurt.", "Dodaj sloj mijesanog voca preko jogurta.", "vanilla_sponge_step3_beat_eggs.jpg"));
                steps.add(new InstructionStep(recipeId, 4, "Sprinkle a layer of granola over the berries.", "Preko voca dodaj sloj granole.", "vanilla_sponge_step4_sift.jpg"));
                steps.add(new InstructionStep(recipeId, 5, "Repeat the layers until the glass is filled, ending with a layer of berries on top. Serve immediately.", "Ponavljaj slojeve dok casa ne bude puna, zavrsavajuci sa slojem voca na vrhu. Serviraj odmah.", "vanilla_sponge_step5_fold.jpg"));
                break;
        }

        return steps;
    }
}
