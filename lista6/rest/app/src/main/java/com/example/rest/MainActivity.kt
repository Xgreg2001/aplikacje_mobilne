package com.example.rest

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.View
import java.net.URLEncoder


class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    private lateinit var editText: EditText
    private lateinit var buttonSimplify: Button
    private lateinit var buttonFactor: Button
    private lateinit var buttonDerive: Button
    private lateinit var buttonIntegrate: Button
    private lateinit var buttonZeros: Button
    private lateinit var buttonTangent: Button
    private lateinit var buttonArea: Button
    private lateinit var buttonCosine: Button
    private lateinit var buttonSine: Button
    private lateinit var buttonTangentFunc: Button
    private lateinit var buttonArcCosine: Button
    private lateinit var buttonArcSine: Button
    private lateinit var buttonArcTangent: Button
    private lateinit var buttonAbs: Button
    private lateinit var buttonLog: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.editText)
        buttonSimplify = findViewById(R.id.button_simplify)
        buttonFactor = findViewById(R.id.button_factor)
        buttonDerive = findViewById(R.id.button_derive)
        buttonIntegrate = findViewById(R.id.button_integrate)
        buttonZeros = findViewById(R.id.button_zeroes)
        buttonTangent = findViewById(R.id.button_tangent)
        buttonArea = findViewById(R.id.button_area)
        buttonCosine = findViewById(R.id.button_cos)
        buttonSine = findViewById(R.id.button_sin)
        buttonTangentFunc = findViewById(R.id.button_tan)
        buttonArcCosine = findViewById(R.id.button_arccos)
        buttonArcSine = findViewById(R.id.button_arcsin)
        buttonArcTangent = findViewById(R.id.button_arctan)
        buttonAbs = findViewById(R.id.button_abs)
        buttonLog = findViewById(R.id.button_log)

        val buttonClickListener = View.OnClickListener { view ->
            val expression = editText.text.toString()
            val encodedExpression = URLEncoder.encode(expression, "UTF-8")
            val operation = when (view.id) {
                R.id.button_simplify -> "simplify"
                R.id.button_factor -> "factor"
                R.id.button_derive -> "derive"
                R.id.button_integrate -> "integrate"
                R.id.button_zeroes -> "zeroes"
                R.id.button_tangent -> "tangent"
                R.id.button_area -> "area"
                R.id.button_cos -> "cos"
                R.id.button_sin -> "sin"
                R.id.button_tan -> "tan"
                R.id.button_arccos -> "arccos"
                R.id.button_arcsin -> "arcsin"
                R.id.button_arctan -> "arctan"
                R.id.button_abs -> "abs"
                R.id.button_log -> "log"
                else -> return@OnClickListener
            }

            Log.d(TAG, "buttonClickListener: $operation $encodedExpression")

            RetrofitInstance.api.calculate(operation, encodedExpression).enqueue(object : Callback<MathResponse> {
                override fun onResponse(call: Call<MathResponse>, response: Response<MathResponse>) {
                    Log.d(TAG, "onResponse: ${response.body()}")

                    val result = response.body()?.result
                    Toast.makeText(this@MainActivity, "Wynik: $result", Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<MathResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Błąd: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }

        buttonSimplify.setOnClickListener(buttonClickListener)
        buttonFactor.setOnClickListener(buttonClickListener)
        buttonDerive.setOnClickListener(buttonClickListener)
        buttonIntegrate.setOnClickListener(buttonClickListener)
        buttonZeros.setOnClickListener(buttonClickListener)
        buttonTangent.setOnClickListener(buttonClickListener)
        buttonArea.setOnClickListener(buttonClickListener)
        buttonCosine.setOnClickListener(buttonClickListener)
        buttonSine.setOnClickListener(buttonClickListener)
        buttonTangentFunc.setOnClickListener(buttonClickListener)
        buttonArcCosine.setOnClickListener(buttonClickListener)
        buttonArcSine.setOnClickListener(buttonClickListener)
        buttonArcTangent.setOnClickListener(buttonClickListener)
        buttonAbs.setOnClickListener(buttonClickListener)
        buttonLog.setOnClickListener(buttonClickListener)
    }
}
