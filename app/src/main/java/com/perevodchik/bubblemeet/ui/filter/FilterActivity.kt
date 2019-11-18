package com.perevodchik.bubblemeet.ui.filter

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.custom.Radio
import com.perevodchik.bubblemeet.data.model.Filter
import com.perevodchik.bubblemeet.util.UserInstance
import com.perevodchik.bubblemeet.util.dpToPx

class FilterActivity : AppCompatActivity() {
    private val filters: HashMap<String, String> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        setItems(findViewById(R.id.gender_group), resources.getStringArray(R.array.gender), "gender")
        setItems(findViewById(R.id.age_group), resources.getStringArray(R.array.age), "age")
        setItems(findViewById(R.id.location_group), resources.getStringArray(R.array.location), "location")
        setItems(findViewById(R.id.eye_group), resources.getStringArray(R.array.eye), "eye")
        setItems(findViewById(R.id.height_group), resources.getStringArray(R.array.height), "height")
        setItems(findViewById(R.id.smoking_group), resources.getStringArray(R.array.smoking), "smoking")
        setItems(findViewById(R.id.married_group), resources.getStringArray(R.array.married), "married")
        setItems(findViewById(R.id.children_group), resources.getStringArray(R.array.children), "children")
        setItems(findViewById(R.id.looking_group), resources.getStringArray(R.array.lookingForChoice), "looking")
        setItems(findViewById(R.id.cook_group), resources.getStringArray(R.array.loveToCook), "cook")
    }

    private fun setItems(rg: RadioGroup, list: Array<String>, key: String) {
        for(s in list) {
            var str = s
            if(key.equals("height", true)) str = str.replace("hght", "")
            val r = Radio(this, key, str)
            r.buttonDrawable = null
            r.gravity = Gravity.CENTER
            r.background = ContextCompat.getDrawable(this, R.drawable.radio_btn_stats)
            r.setPadding(dpToPx(25F), dpToPx(10F), dpToPx(25F), dpToPx(10F))

            r.setOnClickListener {
                filters[r.key] = r.value
            }

            rg.addView(r)
        }
    }

    fun useNavigate(view: View) {
//        val i = Intent()
        when(view.id) {
            R.id.btn_set_filter -> {
                /*var str = ""
                for(e in filters.entries) {
                    str = str.plus("${e.key}=${e.value},")
                }
                Log.d("filters ", "->!!! $str !!!<-")
                i.putExtra("filter", str)*/

                setResult(Activity.RESULT_OK)
                Log.d("filters", "$filters")
                Log.d("UserInstance.filters", "${UserInstance.filters}")
                if(filters.isEmpty())
                    UserInstance.filters.clear()
                else
                    UserInstance.filters.putAll(filters)
                Log.d("UserInstance.filters", "${UserInstance.filters}")
                finish()
            }
            R.id.btn_close_filter -> { finish() }
        }
    }
}
