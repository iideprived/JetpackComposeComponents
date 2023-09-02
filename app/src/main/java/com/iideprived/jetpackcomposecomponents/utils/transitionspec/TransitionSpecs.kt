package com.iideprived.jetpackcomposecomponents.utils.transitionspec

import androidx.compose.animation.*

class TransitionSpecs {
    companion object {
        @OptIn(ExperimentalAnimationApi::class)
        fun <S> fadingSlideInAndOut(upwards: Boolean) :  AnimatedContentTransitionScope<S>.() -> ContentTransform = {
            if (upwards){
                fadeIn().plus( slideInVertically { it }).with(fadeOut().plus(slideOutVertically { -it }))
            } else {
                fadeIn().plus( slideInVertically { -it }).with(fadeOut().plus(slideOutVertically { it }))
            }
        }

        @OptIn(ExperimentalAnimationApi::class)
        fun <S> fadingSlideHorizontal(leftwards: Boolean) : AnimatedContentTransitionScope<S>.() -> ContentTransform = {
            if (leftwards){
                fadeIn() + slideInHorizontally { it } with fadeOut() + slideOutHorizontally { -it }
            } else {
                fadeIn() + slideInHorizontally { -it } with fadeOut() + slideOutHorizontally { it }
            }
        }
    }
}