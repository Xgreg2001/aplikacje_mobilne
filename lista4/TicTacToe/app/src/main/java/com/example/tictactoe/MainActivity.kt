package com.example.tictactoe

import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.graphics.drawable.GradientDrawable.Orientation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.tictactoe.ui.theme.TicTacToeTheme
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: TicTacToeViewModel by viewModels()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {
                    TicTacToeTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(color = MaterialTheme.colorScheme.background) {
                            TicTacToeScreen(viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TicTacToeScreen(viewModel: TicTacToeViewModel) {
    val boardSize = viewModel.boardSize.value
    val gameBoard = viewModel.gameBoard.value
    val winner = viewModel.winner.value
    val gameOver = viewModel.gameOver.value

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Wybierz wielkość planszy: ${boardSize}x${boardSize}", modifier = Modifier.padding(8.dp), textAlign = TextAlign.Center)
        Slider(
            value = boardSize.toFloat(),
            onValueChange = { newSize -> viewModel.setBoardSize(newSize.toInt()) },
            valueRange = 3f..20f,
            steps = 17,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )
        for (i in 0 until boardSize) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (j in 0 until boardSize) {
                    Button(
                        onClick = { viewModel.makeMove(i, j) },
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp),
                        enabled = gameBoard[i * boardSize + j] == null && !gameOver,
                        // set red for "X" and blue for "O"
                        colors = ButtonDefaults.buttonColors(
                            disabledContainerColor = when (gameBoard[i * boardSize + j]) {
                                "X" -> Color.Red
                                "O" -> Color.Blue
                                else -> MaterialTheme.colorScheme.primary
                            }
                        )
                    ){}
                }
            }
        }

        if (gameOver) {
            Text("Gra zakończona. ${when (winner) {
                "X" -> "Wygrywa: Czerwony"
                "O" -> "Wygrywa: Niebieski"
                else -> "Remis"
            }
            }", modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally))
        }

        Button(onClick = { viewModel.resetGame() }, modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally)) {
            Text("Reset Game")
        }
    }
}



