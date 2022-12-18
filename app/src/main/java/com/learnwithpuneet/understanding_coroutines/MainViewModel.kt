package com.learnwithpuneet.understanding_coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class MainViewModel: ViewModel() {

    // Job in Coroutines
    // Helps in managing the lifecycle of coroutine

    private val job =  Job()

    /* Context of Scope?
    -> We can use Dispatchers, and explicit Job instance for context
    -> Plus operator can be used to merge multiple context
     */
    private val scopeWithJob by lazy { CoroutineScope(job + Dispatchers.IO) }

    suspend fun callApi(){
        // launch
        scopeWithJob.launch {
            fetchData()
            // set data to live data here and observe in activity
        }

        // Async - async creates a coroutine and returns its future result as an implementation of Deferred
        // -> parallel execution
        val commerceStudentsCount = scopeWithJob.async(Dispatchers.IO){
            commerceStudentsCount()
        }
        val medicalStudentsCount = scopeWithJob.async(Dispatchers.IO){
            medicalStudentsCount()
        }
        var totalStudents = commerceStudentsCount.await() + medicalStudentsCount.await()
        // set totalStudents value to liveData and observe in UI
    }

    fun userAnotherScope(){
        /* viewModelScope - viewModelScope is a CoroutineScope tied to ViewModel lifecycle
        -> automatically handle cancellation when the ViewModel’s onClear is called
         */
        viewModelScope.launch {
            val commerceStudentsCount = async(Dispatchers.IO){
                commerceStudentsCount()
            }
            val medicalStudentsCount = async(Dispatchers.IO){
                medicalStudentsCount()
            }
            var totalStudents = commerceStudentsCount.await() + medicalStudentsCount.await()
        }
    }

    private fun fetchData(){
        // retrofit func. to get data from Api
    }

    private fun commerceStudentsCount():Int{
        // retrofit func. to get data from Api
        return 4
    }

    private fun medicalStudentsCount():Int{
        // retrofit func. to get data from Api
        return 5
    }

    override fun onCleared() {
        super.onCleared()
        // cancel job here
        scopeWithJob.cancel()
    }
}