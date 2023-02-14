package com.example.mvvm.presentation.main

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.example.mvvm.R
import com.example.mvvm.core.Constants
import com.example.mvvm.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var rocketAnimation: ValueAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //inflate crée les objets des contrôles (Button, ImageView. TextView, ...)
        // ATTENTION il faut changer le paramètre de l'appel de setContentView
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //À chaque fois qu'un nouveau state est reçu
        viewModel.mainUiState.onEach {

            when(it) {
                MainUiState.Empty -> Unit
                is MainUiState.Error -> {
                    Snackbar.make(binding.root, R.string.msg_recharge, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.recharge) {
                            viewModel.recharge()
                        }.show()
                }
                MainUiState.Loading -> {

                    binding.btnStart.isEnabled =false
                    rocketAnimation.start()

                }
                is MainUiState.Succes -> {
                    with(binding) {
                        txvPiloteName.text = it.pilot.name
                        txvLevel.text = getString(R.string.txtLevel, it.pilot.level)
                        txvHealth.text = it.pilot.life.toString()
                        txvCube.text = it.pilot.cube.toString()
                        txvEnergy.text = it.pilot.energie.toString()
                        txvShield.text = it.pilot.shield.toString()
                        binding.btnStart.isEnabled = true
                    }

                }
            }
        }.launchIn(lifecycleScope)

        binding.btnStart.setOnClickListener {
            val revolution = binding.sldRevolution.value.toInt()
            val isTraining = binding.swtTraining.isChecked
            createAnimation()
            viewModel.fly(revolution, isTraining)

        }

/*        val planetclick = findViewById<ImageView>(R.id.imgPlanet)
        // set on-click listener
        planetclick.setOnClickListener {
            // your code to perform when the user clicks on the ImageView
            viewModel.recharge()
        }*/



    }

    fun imgPlanetRecharge(view: View) {
        viewModel.recharge()
    }

    private fun createAnimation(){
        val layoutParams = binding.imvRocket.layoutParams as ConstraintLayout.LayoutParams
        val repeatCount = binding.sldRevolution.value.toInt()
        val startAngle = layoutParams.circleAngle
        val endAngle = startAngle - 360

        rocketAnimation = ValueAnimator.ofFloat(startAngle, endAngle)
        rocketAnimation.repeatCount = repeatCount - 1
        rocketAnimation.duration = Constants.REVOLUTION_DURATION
        rocketAnimation.interpolator = LinearInterpolator()

        rocketAnimation.addUpdateListener {
            val animatedValue = it.animatedValue as Float
            layoutParams.circleAngle = animatedValue
            binding.imvRocket.layoutParams = layoutParams
            binding.imvRocket.rotation = animatedValue -90
        }

    }
}