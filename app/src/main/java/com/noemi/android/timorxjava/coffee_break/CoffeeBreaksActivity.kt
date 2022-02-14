package com.noemi.android.timorxjava.coffee_break

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.textChanges
import com.noemi.android.timorxjava.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.SerialDisposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_coffee_breaks.*
import java.util.*
import java.util.concurrent.TimeUnit

class CoffeeBreaksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coffee_breaks)

//        serialExample()
//        compositeExample()
//         withLatest()
//         customObservables()

        showDialog()
    }

    private fun serialExample() {
        val serial = SerialDisposable()

        val emitter = Observable.interval(1, TimeUnit.SECONDS)
            .map { it.toString() }
            .observeOn(AndroidSchedulers.mainThread())

        tvFirst.clicks().subscribe {
            serial.set(emitter.subscribe { tvFirst.text = it })
        }

        tvSecond.clicks().subscribe {
            serial.set(emitter.subscribe { tvSecond.text = it })
        }

        tvThird.clicks().subscribe {
            serial.set(emitter.subscribe { tvThird.text = it })
        }
    }

    private fun compositeExample() {
        val composite = CompositeDisposable()

        val emitter = Observable.interval(1, TimeUnit.SECONDS)
            .map { it.toString() }
            .observeOn(AndroidSchedulers.mainThread())

        tvFirst.clicks().subscribe {
            composite.add(emitter.subscribe { tvFirst.text = it })
        }

        tvSecond.clicks().subscribe {
            composite.add(emitter.subscribe { tvSecond.text = it })
        }

        tvThird.clicks().subscribe {
            composite.add(emitter.subscribe { tvThird.text = it })
        }

        btRelease.clicks().subscribe {
            dispose(composite)
        }
    }

    private fun dispose(composite: CompositeDisposable) {
        composite.clear()
        tvFirst.text = getString(R.string.txt_first)
        tvSecond.text = getString(R.string.txt_second)
        tvThird.text = getString(R.string.txt_third)
    }

    private fun withLatest() {
        val mySource1 = Observable.just("a", "b", "c", "d", "e")
        val mySource2 = Observable.just("x", "z")
        val mySource3 = Observable.just("y")

        mySource2.withLatestFrom(mySource1, mySource3,
            { s2: String?, s1: String?, s3: String? ->
                String.format(
                    "%s - %s - %s",
                    s1,
                    s2,
                    s3
                )
            })
            .subscribe { s: String? -> Log.d("TAG", "Latest with: $s") }
    }

    private fun customObservables() {
        val emitter = io.reactivex.Observable.interval(5, TimeUnit.SECONDS)
            .map { it.toString() }
            .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())

        tvFirst.clicks().subscribe {
            emitter.subscribe {
                Toast.makeText(this, "The counter $it", Toast.LENGTH_LONG).show()
            }
        }

        val firstName = BehaviorSubject.createDefault("Noemi")
        val lastName = BehaviorSubject.createDefault("Varazslo")

        tvSecond.clicks().subscribe {

            io.reactivex.Observable.combineLatest(
                firstName.observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread()),
                lastName.observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread()),
                { first, last -> String.format("%s %s", first, last) })
                .doOnNext {
                    Log.d("TAG", "Name: $it")
                }
                .subscribe {
                    Toast.makeText(this, "Your name is $it", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun showDialog() {
        val titleObservable: Observable<String> = etTitle.textChanges().map { it.toString() }
        val messageObservable: Observable<String> = etMessage.textChanges().map { it.toString() }

        val dialogInfo: Observable<Pair<String, String>> =
            Observable.combineLatest(titleObservable, messageObservable, { title, message ->
                Pair(title, message)
            })

        val infoToShow: Observable<Pair<String, String>> =
            btShowDialog.clicks().map { }
                .withLatestFrom(dialogInfo, { _, info ->
                    Pair(info.first, info.second)
                })

        infoToShow.observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                AlertDialog.Builder(this).setTitle(it.first).setMessage(it.second).create().show()
                etTitle.setText("")
                etMessage.setText("")
            }
    }

    companion object {
        const val TAG = "CoffeeActivity"
    }
}