package com.tlnacl.reactiveapp.uniflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tlnacl.reactiveapp.DispatcherProvider
import com.tlnacl.reactiveapp.uniflow.data.UIState
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.reflect.KClass

abstract class DataFlowBaseViewModel (
        defaultState: UIState = UIState.Empty,
        defaultDispatcher: CoroutineDispatcher = DispatcherProvider.dispatchers.io()// TODO use
): ViewModel(), DataFlow {
    val dataPublisher: LiveDataPublisher = LiveDataPublisher(defaultState)
    private val dataStore: UIDataStore = UIDataStore(dataPublisher, defaultState)
    private val actionDispatcher: ActionDispatcher
        get() = ActionDispatcher(viewModelScope, dataStore, this)

    final override fun getCurrentState() = actionDispatcher.getCurrentState()
    final override fun <T : UIState> getCurrentStateOrNull(stateClass: KClass<T>): T? = actionDispatcher.getCurrentStateOrNull()
    final override fun action(onAction: ActionFunction<UIState>): ActionFlow = actionDispatcher.action(onAction)
    final override fun action(onAction: ActionFunction<UIState>, onError: ActionErrorFunction): ActionFlow = actionDispatcher.action(onAction, onError)
    final override fun <T : UIState> actionOn(stateClass: KClass<T>, onAction: ActionFunction<T>): ActionFlow = actionDispatcher.actionOn(stateClass, onAction)
    final override fun <T : UIState> actionOn(stateClass: KClass<T>, onAction: ActionFunction<T>, onError: ActionErrorFunction): ActionFlow = actionDispatcher.actionOn(stateClass, onAction, onError)

}