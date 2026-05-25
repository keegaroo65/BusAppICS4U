package ca.kee65.busappics4u.data

import android.content.Context

interface AppContainer {
    val tripsRepository: TripsRepository
}

class AppDataContainer(private val context: Context): AppContainer {
    override val tripsRepository: TripsRepository by lazy {
        OfflineTripsRepository(BusDatabase.getDatabase(context).tripDao())
    }
}