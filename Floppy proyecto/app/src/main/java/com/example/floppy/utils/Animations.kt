package com.example.floppy.utils

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.example.floppy.R

class Animations {
    companion object {
        /**its function is appear an any view*/
        fun View.animAppear() {
            val animation = AnimationUtils.loadAnimation(this.context, R.anim.anim_aparecer)
            this.visibility = View.VISIBLE
            this.animation = animation
        }

        /**its function is vanish an any view*/
        fun View.animVanish() {
            val animation   = AnimationUtils.loadAnimation(this.context, R.anim.anim_desaparecer)
            this.visibility = View.GONE
            this.animation  = animation
        }

        fun View.animAppearAndVanish(){
            val animation   = AnimationUtils.loadAnimation(this.context,R.anim.anim_aparecer_desaparecer)
            this.visibility = View.VISIBLE
            this.animation  = animation
            this.visibility = View.INVISIBLE
        }

        /**Animation for cascading views to appear*/
        fun RelativeLayout.animationCascadeFade() = this.scheduleLayoutAnimation()

        /**Animation for cascading views to appear*/
        fun LinearLayout  .animationCascadeFade() = this.scheduleLayoutAnimation()

        fun View.animationTranslate(translate :Boolean){
            val moveY = if (translate)  this.height else 0
            this.animate().translationY(moveY.toFloat()).setDuration(300).setStartDelay(100).start()
        }
    }
}
