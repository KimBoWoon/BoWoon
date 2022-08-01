package com.data.lol.repository

import com.data.lol.service.RiotApiService
import com.domain.lol.repository.RiotApiRepository

class RiotApiRepositoryImpl(
    private val riotApiService: RiotApiService
) : RiotApiRepository {

}