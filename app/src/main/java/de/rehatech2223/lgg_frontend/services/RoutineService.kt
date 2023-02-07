package de.rehatech2223.lgg_frontend.services

import de.rehatech2223.datamodel.Routine

class RoutineService {

    fun getRoutineList(): ArrayList<Long>{
        return ArrayList()
    }

    fun getRoutineInfo(routineId: Long): Routine?{
        return null
    }

    fun triggerRoutine(routineId: Long){

    }

    /*
     *  Returns a Routine on 201 Created, when Routine info had to be changed from the predefined routine
     *  can return null!
     */
    fun createRoutine(routine: Routine): Routine?{
        return null
    }

    fun deleteRoutine(routineId: Long): Boolean{
        return false
    }
}