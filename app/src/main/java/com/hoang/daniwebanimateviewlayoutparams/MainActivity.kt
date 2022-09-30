package com.hoang.daniwebanimateviewlayoutparams

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Property
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import androidx.core.view.updateLayoutParams
import kotlinx.coroutines.NonCancellable.start

private const val TAG = "MAIN_ACTIVITY"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

/*        findViewById<ImageView>(R.id.imageView).setOnClickListener { image ->
            Log.d(TAG, "Starting Animation, Width: ${image.width}, Height: ${image.height}")

            ValueAnimator.ofInt(image.height, image.height * 2).apply {
                // Adds listener when value updates
                addUpdateListener { animation ->
                    Log.d(TAG, "Animated Value: ${animation.animatedValue as Int}")

                    // Updates LayoutParams here
                    image.updateLayoutParams<ViewGroup.LayoutParams> {
                        height = animation.animatedValue as Int
                        width = animation.animatedValue as Int
                    }
                }

                duration = 2000

                //Must call start to start the animation
                start()
            }
        }*/

        findViewById<ImageView>(R.id.imageView).setOnClickListener { image ->
            image.updateLayoutParams {
                height = image.height
                width = image.width
            }

            Log.d(TAG, "Starting height expected: ${image.layoutParams.height * 2}")
            Log.d(TAG, "Starting width expected: ${image.layoutParams.width * 2}")

            ObjectAnimator.ofObject(
                image,
                Property.of(View::class.java, LayoutParams::class.java, "layoutParams"),
                { fraction, next, finalTarget ->
                    // next will change as the animation progress
                    // finalTarget will stay the same throughout the entire animation
                    next.apply {
                        height = ((fraction + 1) * finalTarget.height - height).toInt()
                        width = ((fraction + 1) * finalTarget.width - width).toInt()
                    }
                },
                // Copy constructor. This is final target
                LayoutParams(image.layoutParams).apply {
                    height *= 2
                    width *= 2
                }
            ).apply {
                doOnEnd {
                    Log.d(TAG, "Final height: ${image.height}")
                    Log.d(TAG, "Final width: ${image.width}")
                    // If you want to, you can set the exact LayoutParams value
                    // here to make up for loss precision when converting Float to Int
                }
                duration = 2000
                start()
            }
        }

    }
}