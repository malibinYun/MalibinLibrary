package com.malibin.library.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author Malibin
 *
 * Created on 2021 08 26
 * Updated on 2021 08 26
 *
 * @sample
 * private val value: SomeClass by lifeCycled { SomeClass() }
 *
 * 안드로이드 LifeCycle과 생명주기를 함께하는 프로퍼티 입니다.
 * 처음 값을 참조하면, 초기에 정의 해두었던 initializer를 통해 객체를 생성합니다.
 * LifeCycleOwner가 destroy되면 값도 함께 null이 됩니다.
 *
 * ReadOnlyProperty이기 때문에 var로 변수를 할당할 수 없습니다.
 *
 * @throws IllegalStateException
 * LifeCycleOwner의 상태가 Destoryed일 때 값에 접근하면 exception이 발생합니다.
 *
 * 화면 전환 등이 일어날 때,
 * 값이 null이 되었다가 다시 initializer로 부터 새 객체를 할당합니다.
 *
 */

fun <T: Any> LifecycleOwner.lifeCycled(initializer: () -> T): ReadOnlyProperty<LifecycleOwner, T>{
    return LifeCycledValue(initializer)
}

private class LifeCycledValue<T : Any>(
    private val initializer: () -> T
) : ReadOnlyProperty<LifecycleOwner, T>, LifecycleObserver {

    @Volatile
    private var value: T? = null

    override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): T {
        val currentValue = value
        if (currentValue != null) {
            return currentValue
        }
        return when (thisRef.lifecycle.currentState) {
            Lifecycle.State.DESTROYED -> error("value is already released cause lifecycle owner is destroyed")
            else -> synchronized(this) {
                thisRef.lifecycle.removeObserver(this)
                thisRef.lifecycle.addObserver(this)
                initializer().also { this.value = it }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        value = null
    }
}
