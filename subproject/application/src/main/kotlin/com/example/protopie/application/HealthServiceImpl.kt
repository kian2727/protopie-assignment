package com.example.protopie.application

import com.example.protopie.domain.HealthRepository

class HealthServiceImpl(
    private val healthRepository: HealthRepository
):HealthService {

    override fun check(): Boolean {
        try {
            healthRepository.check()
        } catch (e:Exception){
            e.printStackTrace()
            return false
        }
        return true
    }
}