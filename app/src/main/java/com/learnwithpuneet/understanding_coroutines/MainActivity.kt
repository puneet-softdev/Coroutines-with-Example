package com.learnwithpuneet.understanding_coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.learnwithpuneet.understanding_coroutines.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    lateinit var activityMainBinding: ActivityMainBinding
    var firstCount = 0;
    var secondCount = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // -- Use Coroutines to switch to background thread --

        /* 1. Scope - provide scope to our coroutine
        -> CoroutineScope - All coroutines going to start in this scope will be run on this context i.e Activity/Fragment
        -> GlobalScope - To launch top level coroutines
         */


        /* 2. Dispatchers - Dispatchers describes the kind of thread where the coroutine should be run
        -> Main - Launch coroutine in Main Thread/ UI Thread
        -> IO - Launch coroutine in a Background Thread
        -> Default - For CPU Intensive tasks
         */


        /* 3. Coroutine Builder - used to launch coroutines
        -> launch:- Launches a new coroutine without blocking the current thread.
        Returns an instance of Job which can be used as a reference to coroutine.
        We use this builder that doesn’t have any result as return value
        Concept - “fire and forget”
        -> async:- If we want to get result as return value we should use Async builder
        It also allows use to launch coroutine in Parallel.
        We need to invoke await() to get the value
         */


        // Wrong - this function should be called from background thread
        // callPlayerApi()

        CoroutineScope(Dispatchers.IO).launch {
            callPlayerApi()
        }

        // Why Coroutines?

        activityMainBinding.tvFirstTask.text = "0"
        activityMainBinding.tvSecondTask.text = "0"
        activityMainBinding.tvThirdTask.text = "0"

        activityMainBinding.btnFirstTask.setOnClickListener {
            activityMainBinding.tvFirstTask.text =
                firstCount++.toString()
        }

        activityMainBinding.btnSecondTask.setOnClickListener {
            activityMainBinding.tvSecondTask.text =
                secondCount++.toString()
        }

        // Without Coroutine - Lag

        /* activityMainBinding.btnUpload.setOnClickListener {
            uploadFiles()
        }
        */

        // With Coroutine - No Lag
        activityMainBinding.btnUpload.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                uploadFiles()
            }
        }
    }

    private fun uploadFiles() {
        for (i in 1..400000) {
            Log.d("TAG", "Uploading Files:- $i")
        }
    }

    // Just to simulate api call
    // For this example - let's assume that this function should be called from Background Thread and not from Main Thread
    // used "suspend" keyword here

    /*
    Suspend? - Whenever a coroutine is suspended, the current stack frame of the function is copied and saved in the memory
    When the function resumes after completing its task, the stack frame is copied back from where it was saved and starts running again
    With Suspend modifier, we are actually limiting the use of the function only for coroutines, we label it as a function with heavy long running task
     */
    private suspend fun callPlayerApi() {
        Log.d("TAG", "Receiving Api Response")
        // switch to Main / UI Thread to Display Response or to Communicate with UI
        // we use withContext to switch between Dispatchers
        withContext(Dispatchers.Main) {
            // do some work on MainThread
            // for example - update TextView / Show response in RecyclerView etc.
        }
    }
}