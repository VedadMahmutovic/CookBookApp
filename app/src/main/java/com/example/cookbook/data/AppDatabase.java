package com.example.cookbook.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.cookbook.model.InstructionStep;
import com.example.cookbook.model.Recipe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Recipe.class, InstructionStep.class}, version = 17, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = "AppDatabase";
    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract RecipeDao recipeDao();
    public abstract HistoryDao historyDao();
    public abstract InstructionStepDao instructionStepDao();

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
        }
    };
    private static final Migration MIGRATION_10_11 = new Migration(10, 11) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Recipe ADD COLUMN category TEXT");
        }
    };
    static final Migration MIGRATION_14_15 = new Migration(14, 15) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS `Recipe_new` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`name_en` TEXT, " +
                    "`name_bs` TEXT, " +
                    "`ingredients_en` TEXT, " +
                    "`ingredients_bs` TEXT, " +
                    "`calories` INTEGER NOT NULL, " +
                    "`image_url` TEXT, " +
                    "`rating` REAL NOT NULL, " +
                    "`is_favorite` INTEGER NOT NULL, " +
                    "`last_viewed` INTEGER NOT NULL, " +
                    "`category_en` TEXT, " +
                    "`category_bs` TEXT, " +
                    "`image1` TEXT, " +
                    "`image2` TEXT, " +
                    "`image3` TEXT, " +
                    "`created_by_user` INTEGER NOT NULL" +
                    ")");

            db.execSQL("INSERT INTO Recipe_new " +
                    "(id, name_en, name_bs, ingredients_en, ingredients_bs, calories, image_url, rating, is_favorite, last_viewed, category_en, category_bs, image1, image2, image3, created_by_user) " +
                    "SELECT id, name, name, ingredients, ingredients, calories, image_url, rating, is_favorite, last_viewed, category, category, image1, image2, image3, created_by_user FROM Recipe");

            db.execSQL("DROP TABLE Recipe");

            db.execSQL("ALTER TABLE Recipe_new RENAME TO Recipe");
        }
    };
    static final Migration MIGRATION_15_16 = new Migration(15, 16) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Recipe ADD COLUMN internal_name TEXT");
        }
    };





    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "recipe_database")
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_10_11, MIGRATION_14_15, MIGRATION_15_16)
                            .fallbackToDestructiveMigration() // Only for development
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Log.d(TAG, "Database created");
                                    databaseWriteExecutor.execute(() -> {
                                        try {
                                            new DatabaseInitializer(context.getApplicationContext()).insertInitialData();
                                        } catch (Exception e) {
                                            Log.e(TAG, "Error initializing database", e);
                                        }
                                    });
                                }

                                @Override
                                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                    super.onOpen(db);
                                    Log.d(TAG, "Database opened");
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static AppDatabase getInstance(Context context) {
        return getDatabase(context);
    }
}