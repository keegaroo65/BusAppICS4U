package com.example.busappics4u.data

import kotlinx.coroutines.flow.Flow

interface TripsRepository {
    fun getAllTripsStream(): Flow<List<Trip>>

    fun getTripStream(id: Int): Flow<Trip?>

    suspend fun insertTrip(trip: Trip)

    suspend fun deleteTrip(trip: Trip)

    suspend fun updateTrip(trip: Trip)
}