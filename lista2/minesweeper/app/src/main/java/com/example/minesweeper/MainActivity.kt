package com.example.minesweeper

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.children
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {

    private val fields = mutableListOf<Field>()
    var correctlyFlagged = 0
    var revealed = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val table = findViewById<TableLayout>(R.id.tableLayout)

        initializeGame(table)
    }

    private fun initializeGame(table: TableLayout) {
        val screenWidth = resources.displayMetrics.widthPixels
        val usableWidth = screenWidth - 2 * table.paddingLeft

        val toggle = findViewById<SwitchMaterial>(R.id.toggle)

        val bombCount = 10

        val bombIds = mutableListOf<Int>()
        while (bombIds.size < bombCount) {
            val id = Random.nextInt(0, 81)
            if (!bombIds.contains(id)) {
                bombIds.add(id)
            }
        }

        for (i in 0..8) {
            val row = TableRow(this)
            for (j in 0..8) {
                val id = i * 9 + j
                val field = Field(this, i, j, bombIds.contains(id), usableWidth / 9)
                row.addView(field)
                fields.add(field)
            }
            table.addView(row)
        }

        countBombs()

        for (field in fields) {
            field.setOnClickListener {
                field.handleFieldClick(toggle)
            }
        }
    }

    private fun countBombs() {
        for (field in fields) {
            if (!field.isBomb) {
                var bombCount = 0
                for (i in -1..1) {
                    for (j in -1..1) {
                        if (i == 0 && j == 0) {
                            continue
                        }

                        val row = field.row + i
                        val col = field.col + j

                        if (row < 0 || row > 8 || col < 0 || col > 8) {
                            continue
                        }

                        val id = row * 9 + col
                        if (fields[id].isBomb) {
                            bombCount++
                        }
                    }
                }

                field.bombCount = bombCount
            }
        }
    }

    private fun notifyNeighbours(id: Int) {
        val row = id / 9
        val col = id % 9

        for (i in -1..1) {
            for (j in -1..1) {
                if (i == 0 && j == 0) {
                    continue
                }

                val r = row + i
                val c = col + j

                if (r < 0 || r > 8 || c < 0 || c > 8) {
                    continue
                }

                val fieldId = r * 9 + c
                val field = fields[fieldId]
                if (field.state == Field.State.UNKNOWN) {
                    if (field.revealState()) {
                        notifyNeighbours(fieldId)
                    }
                }
            }
        }
    }

    private fun finishGame(lost: Boolean) {
        if (lost) {
            Toast.makeText(this, "You lost!", Toast.LENGTH_SHORT).show()
            for (field in fields) {
                if (field.isBomb) {
                    field.setImageResource(R.drawable.bomb)
                }
            }
        } else {
            Toast.makeText(this, "You won!", Toast.LENGTH_SHORT).show()
        }

        for (field in fields) {
            field.setOnClickListener(null)
        }

        val resetButton = findViewById<Button>(R.id.resetButton)
        resetButton.visibility = Button.VISIBLE
        resetButton.setOnClickListener {
            resetGame()
        }
    }

    private fun resetGame() {
        val table = findViewById<TableLayout>(R.id.tableLayout)
        table.removeAllViews()
        correctlyFlagged = 0
        revealed = 0
        fields.clear()
        initializeGame(table)
        val resetButton = findViewById<Button>(R.id.resetButton)
        resetButton.visibility = Button.INVISIBLE
    }


    class Field(private val c: Context, val row: Int, val col: Int, val isBomb: Boolean, val size: Int): AppCompatImageView(c) {

        var state = State.UNKNOWN
        var bombCount = 0

        init {
            setImageResource(R.drawable.unkown)
            this.id = row * 9 + col
            this.adjustViewBounds = true
            this.maxHeight = size
            this.maxWidth = size
        }

        public fun handleFieldClick(toggle: SwitchMaterial) {
            val activity = c as MainActivity

            if (toggle.isChecked) {
                when (state) {
                    State.UNKNOWN -> {
                        state = State.FLAGGED
                        setImageResource(R.drawable.flag)

                        if (isBomb) {
                            activity.correctlyFlagged++
                            if (activity.correctlyFlagged == 10 && activity.revealed == 71) {
                                activity.finishGame(false)
                            }
                        }
                    }
                    State.FLAGGED -> {
                        state = State.UNKNOWN
                        setImageResource(R.drawable.unkown)

                        if (isBomb) {
                            activity.correctlyFlagged--
                        }
                    }
                    State.NUMBER -> {
                        // do nothing
                    }
                    State.EMPTY -> {
                        // do nothing
                    }
                }
            } else {
                when (state) {
                    State.UNKNOWN -> {
                        if (isBomb) {
                            activity.finishGame(true)
                        } else {
                            if(revealState()){
                                activity.notifyNeighbours(this.id)
                            }
                            if (activity.revealed == 71 && activity.correctlyFlagged == 10) {
                                activity.finishGame(false)
                            }
                        }
                    }
                    State.FLAGGED -> {
                        // do nothing
                    }
                    State.NUMBER -> {
                        // do nothing
                    }
                    State.EMPTY -> {
                        // do nothing
                    }
                }
            }


        }

        fun revealState(): Boolean {
            val activity = c as MainActivity
            activity.revealed++
            return if (bombCount == 0) {
                state = State.EMPTY
                setImageResource(R.drawable.empty)
                true
            } else {
                state = State.NUMBER
                setImageResource(resources.getIdentifier("n$bombCount", "drawable", c.packageName))
                false
            }
        }

        enum class State {
            UNKNOWN,
            FLAGGED,
            NUMBER,
            EMPTY
        }
    }



}