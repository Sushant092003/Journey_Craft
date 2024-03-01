package com.gmail_bssushant2003.journeycraft.Models

import java.io.Serializable

data class Preferences(
    private val isLiftedTemple : Boolean,
    private var isLiftedBeach : Boolean,
    private var isLiftedHistorical : Boolean,
    private var isLiftedMountain : Boolean,
    private var isLiftedLake : Boolean,
    private var isLiftedMuseum : Boolean,
    private var isLiftedPark : Boolean,
    private var isLiftedWildlife : Boolean
) : Serializable
