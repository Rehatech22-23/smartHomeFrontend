package de.rehatech2223.lgg_frontend.ui.main

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import de.rehatech2223.datamodel.RoutineDTO
import de.rehatech2223.lgg_frontend.R
import de.rehatech2223.lgg_frontend.ui.routineDetail.RoutineDetailActivity
import de.rehatech2223.lgg_frontend.util.TileImageUtil

/**
 * LinearLayout subclass that represents a Routine UI-Tile that is clickable and starts the
 * corresponding RoutineDetailActivity.
 *
 * @author Fynn Debus
 */
class RoutineTile(context: Context, attrs: AttributeSet? = null, routineArgument: RoutineDTO? = null): LinearLayout(context, attrs) {

    var routineDTO: RoutineDTO

    init {
        LayoutInflater.from(context).inflate(R.layout.routine_tile, this, true)
        orientation = VERTICAL

        routineDTO = routineArgument ?: RoutineDTO.Builder("Error Routine", -1, ArrayList(), -1L).build()

        initRoutineTile()
        initOnClickListeners()
    }

    private fun initOnClickListeners() {
        this.setOnClickListener {
            val routineDetailActivity = Intent(context, RoutineDetailActivity::class.java)
            routineDetailActivity.putExtra("routineId", routineDTO.routineId)
            context.startActivity(routineDetailActivity)
        }
    }

    private fun initRoutineTile(){
        val name: TextView = findViewById(R.id.name)
        val image: ImageView = findViewById(R.id.image)
        val routineDTOLabeling: List<String> = routineDTO.routineName.split(':')
        if (routineDTOLabeling.size < 2){
            name.text = "Error Name"
            image.setImageResource(R.drawable.error_100px)
        }else {
            name.text = routineDTOLabeling[1].ifEmpty { "Error Name" }
            image.setImageResource(TileImageUtil.getRoutineImageResource(routineDTOLabeling[0]))
        }
    }
}