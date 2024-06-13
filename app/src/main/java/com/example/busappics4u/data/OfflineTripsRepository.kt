package com.example.busappics4u.data

import kotlinx.coroutines.flow.Flow

class OfflineTripsRepository(private val tripDao: TripDao) : TripsRepository {
    override fun getAllTripsStream(): Flow<List<Trip>> = tripDao.getAllTrips()

    override fun getAllTripsStream(limit: Int): Flow<List<Trip>> = tripDao.getAllTrips(limit)

    override fun getTripStream(id: Int): Flow<Trip?> = tripDao.getTrip(id)

    override suspend fun insertTrip(trip: Trip) = tripDao.insert(trip)

    override suspend fun deleteTrip(trip: Trip) = tripDao.delete(trip)

    override suspend fun updateTrip(trip: Trip) = tripDao.update(trip)
}
