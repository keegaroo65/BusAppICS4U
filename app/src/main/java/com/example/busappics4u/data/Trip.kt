package com.example.busappics4u.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "start_timestamp")
    val startTime: Long,

    @ColumnInfo(name = "end_timestamp")
    val endTime: Long,

    @ColumnInfo(name = "route_id")
    val routeId: Int,

    @ColumnInfo(name = "route_headsign")
    val routeHeadsign: String,

    @ColumnInfo(name = "bus_id")
    val busId: String,

    @ColumnInfo(name = "start_stop")
    val startStop: Int,

    @ColumnInfo(name = "end_stop")
    val endStop: Int,
)
