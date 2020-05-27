package cn.droid.game.demo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.act_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.act_splash)

        portraitGame.setOnClickListener {
            startActivity(Intent(this,GameActivity::class.java))
        }

        landscapeGame.setOnClickListener {
            startActivity(Intent(this,LGameActivity::class.java))
        }

        otherFunctions.setOnClickListener {
            startActivity(Intent(this,OtherActivity::class.java))
        }

    }
}
