package presentation.components

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun AboutBezierCurves(
    modifier: Modifier = Modifier,
    onClickClose: (Boolean) -> Unit
) {
    val scrollState = rememberScrollState()

    Box(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(end = 8.dp)
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    text = """
Кривые Безье — это параметрические кривые, которые активно используются для описания плавных линий в компьютерной графике: в описаниях шрифтов (например, TrueType), в языке масштабируемой векторной графики SVG, в редакторах изображений (Figma, Adobe Illustrator, Photoshop, GIMP, Inkscape), в 3D-графике, в каскадных таблицах стилей CSS для анимации и многом другом.

Кривые были предложены в 1960-х годах Пьером Безье (Renault) и Полем де Кастельжо (Citroën) независимо друг от друга, применяя их для проектирования кузовов автомобилей.

Кривые названы в честь Пьера Безье, предложившего прямые формулы их вычисления:

— линейная (1-го порядка):  
  B(t) = (1 − t) × P₀ + t × P₁

— квадратичная (2-го порядка):  
  B(t) = (1 − t)² × P₀ + 2t(1 − t) × P₁ + t² × P₂

— кубическая (3-го порядка):  
  B(t) = (1 − t)³ × P₀ + 3t(1 − t)² × P₁ + 3t²(1 − t) × P₂ + t³ × P₃

Полю де Кастельжо приписывают рекурсивный алгоритм вычисления кривых Безье, который наглядно показывает, что эти кривые — частный случай многочленов Бернштейна:
B(t) = ∑ⁿᵢ₌₀ C(n, i) × tⁱ × (1 − t)ⁿ⁻ⁱ × Pᵢ

Геометрический смысл алгоритма де Кастельжо:

1. Заданы опорные точки P₀, …, Pₙ. Соединяя их по порядку, получаем ломаную.

2. Каждую линию ломаной делим в соотношении t / (1 − t), соединяем новые точки. Получаем новую ломаную на один отрезок короче.

3. Повторяем процесс, пока не останется одна точка — это и будет B(t), точка на кривой при заданном параметре t.
                    """.trimIndent(),
                    textAlign = TextAlign.Justify
                )
            }

            VerticalScrollbar(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.Top),
                adapter = rememberScrollbarAdapter(scrollState)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "О кривых Безье",
                style = MaterialTheme.typography.h6
            )

            IconButton(
                imageVector = Icons.Default.Close,
                tooltipText = "Закрыть",
                onClick = { onClickClose(false) }
            )
        }
    }
}