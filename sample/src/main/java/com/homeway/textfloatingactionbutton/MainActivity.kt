package com.homeway.textfloatingactionbutton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.homeway.textfab.ButtonType
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

//        textFab.buttonType = ButtonType.Icon
//        textFab.backgroundTintList = resources.getColorStateList(R.color.tint_color_primary)
//        textFab.icon = R.drawable.ic_free_breakfast_black_24dp
//        textFab.iconSizeRational = 1.0f

        textFab.buttonType = ButtonType.Text
        textFab.backgroundTintList = resources.getColorStateList(R.color.tint_color_primary)
        textFab.text = "+24"
        textFab.textAreaSizeRational = 1.0f
    }
}
