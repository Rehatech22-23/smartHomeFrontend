package de.rehatech2223.lgg_frontend.ui.main

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import de.rehatech2223.datamodel.RoutineDTO
import de.rehatech2223.lgg_frontend.R
import de.rehatech2223.lgg_frontend.util.TileImageUtil

class RoutineTile(context: Context, attrs: AttributeSet? = null, routineArgument: RoutineDTO? = null): LinearLayout(context, attrs) {

    var routineDTO: RoutineDTO

    init {
        LayoutInflater.from(context).inflate(R.layout.routine_tile, this, true)
        orientation = VERTICAL

        routineDTO = routineArgument ?: RoutineDTO.Builder("Error Routine", -1, ArrayList()).build()

        initRoutineTile()
    }

    fun initRoutineTile(){
        findViewById<TextView>(R.id.name).text = routineDTO.routineName
        findViewById<ImageView>(R.id.image)
            .setImageResource(TileImageUtil.getRoutineImageId(routineDTO, context))
    }
}