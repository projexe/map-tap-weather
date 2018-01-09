package com.projexe.maptapweather.model.repository;

/**
 * Abstract class that extends RoomDatabase. Lists the entities contained in the database,
 * and the DAOs which access them. Generates a database instance.
 * @author Simon Hutton
 * @version 1.0
 */
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {WeatherData.class}, version = 1, exportSchema = false)

public abstract class WeatherPersistenceDatabase extends RoomDatabase {

    private static WeatherPersistenceDatabase INSTANCE;

    public abstract WeatherDao weatherDao();


    /**
     * Simple injector class returning a singleton instance of WeatherPerstenceDatabase to inject into caller
     * (could be incorporated into a Dependency Injection Framework implementation in future iterations)
     * @param context
     * @return
     */
    public static WeatherPersistenceDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context, WeatherPersistenceDatabase.class, "weatherdatabase")
                            .allowMainThreadQueries()
                            // recreate the database if necessary
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}