import androidx.compose.desktop.DesktopTheme
import androidx.compose.desktop.Window
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.*


import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp


enum class Colors {
    DodgerBlue {
        override fun getRGB() = Color(16, 152, 247)
    },
    VioleRed {
        override fun getRGB() = Color(247, 85, 144)
    },
    Champagne {
        override fun getRGB() = Color(238, 227, 171)
    },
    Bone {
        override fun getRGB() = Color(217, 207, 193)
    },
    Honeydew {
        override fun getRGB() = Color(223, 248, 235)
    },
    InternationalOrangeEngineering {
        override fun getRGB() = Color(186, 45, 11)
    },
    OceanGreen {
        override fun getRGB() = Color(115, 186, 155)
    },
    ChinaPink {
        override fun getRGB() = Color(229, 99, 153)
    },
    TerraCotta {
        override fun getRGB() = Color(237, 106, 90)
    };
    abstract fun getRGB(): Color
}


var warningText = mutableStateOf("!!!")
var color =  mutableStateOf(Color(0.5F,0.5F,0.5F,1F))

var colorMap = mutableMapOf<String, Float>(
    "R" to 0.5f,
    "G" to 0.5f,
    "B" to 0.5f,
    "C" to 0f,
    "M" to 0f,
    "Y" to 0f,
    "K" to 0f,
    "H" to 0f,
    "L" to 0f,
    "S" to 0f)

fun onColorChanged(newCM: MutableMap<String, Float>) {
    colorMap = newCM
}

fun CMYKtoRGB(c: Float, m: Float, y: Float, k: Float) : Array<Float> {
    var r = roundColor((1-c)*(1-k))
    var g = roundColor((1-m)*(1-k))
    var b = roundColor((1-y)*(1-k))
    colorMap["R"] = r
    colorMap["G"] = g
    colorMap["B"] = b
    return arrayOf(r, g, b)
}

fun RGBtoCMYK(r: Float, g: Float, b: Float) : Array<Float> {
    val k =  minOf(1-r,1-g,1-b)
    if(k == 1f) {
        colorMap["C"] = 0f
        colorMap["M"] = 0f
        colorMap["Y"] = 0f
        colorMap["K"] = 1f
        return arrayOf(0f, 0f, 0f, 1f)
    }
    var c = roundColor((1-r-k)/(1-k))
    var m = roundColor((1-g-k)/(1-k))
    var y = roundColor((1-b-k)/(1-k))
    colorMap["C"] = c
    colorMap["M"] = m
    colorMap["Y"] = y
    colorMap["K"] = k
    return arrayOf(c, m, y, k)
}

fun RGBtoHLS(r: Float, g: Float, b: Float) : Array<Float> {
    val (max, min) = listOf(maxOf(r, g, b), minOf(r, g, b))
    val (delta, sum) =listOf(max - min, max + min)

    var l = sum / 2
    var h: Float
    var s: Float

    if (delta == 0f) {
        h = 0f
        s = h
    }
    else {
        s = delta / if (sum < 1) sum else 2 - sum
        if (r == max) h = (g - b) / delta / 6
        else if (g == max) h = (2 + (b - r) / delta) / 6
        else h = (4 + (r - g) / delta) / 6
        if (h < 0) h += 1
    }
    h = roundColor(h)
    l = roundColor(l)
    s = roundColor(s)
    colorMap["H"] = h
    colorMap["L"] = l
    colorMap["S"] = s
    return arrayOf(h, l, s)
}

fun HLStoRGB(h: Float, l: Float, s: Float) : Array<Float> {
    var (r, g, b) = listOf(0f, 0f, 0f)

    if (s == 0f) {
        r = l
        g = l
        b = l
    }
    else {
        var q = if (l < 0.5) l * (1 + s) else l + s - l * s
        var p = 2 * l - q;
        r = rgbHue(p, q, (h + 1f/3))
        g = rgbHue(p, q, h);
        b = rgbHue(p, q, h - 1f/3)
    }
    r = roundColor(r)
    g = roundColor(g)
    b = roundColor(b)
    colorMap["R"] = r
    colorMap["G"] = g
    colorMap["B"] = b
    return arrayOf(r, g, b)
}

fun rgbHue(p: Float, q: Float, tt: Float): Float {
    var t = tt;
    if(t < 0) {
        t += 1
    }
    if(t > 1) {
        t -= 1
    }
    if(t < 1f/6) return p + (q - p) * 6 * t
    else if(t < 1f/2) return q
    else if(t < 2f/3) return p + (q - p) * (2f/3 - t) * 6
    else return p;
}

@Composable
fun ColorSlider(col: MutableMap<String, Float>,
                state: MutableState<Float>,
                Str: String,
                onColorChange: (String) -> Unit,
                isCMYK: Boolean,
                mul: Float) {
    Box(modifier = Modifier.width(300.dp).padding(2.dp)) {
        Row {
            Column {
                var sliderPosition = remember { mutableStateOf(col[Str]) }
                Text(text = Str)
                TextField(
                    value = if(isCMYK) (colorMap[Str]!!*mul).toString() else (col[Str]!!*mul).toString(),
                    { col[Str] = when(it.toFloatOrNull()) {
                        null -> 0.0f
                        else ->  if(it.toFloat() / mul > 1f) 1f else it.toFloat() / mul
                    }
                        sliderPosition.value = when(it.toFloatOrNull()) {
                            null -> 0.0f
                            else -> if(it.toFloat() / mul > 1f) 1f else it.toFloat() / mul
                        }
                        state.value = when(it.toFloatOrNull()) {
                            null -> 0.0f
                            else -> if(it.toFloat()  / mul > 1f) 1f else it.toFloat() / mul
                        }
                        colorMap[Str] = state.value},

                    )
                Slider(
                    value = if(isCMYK) colorMap[Str]!! else col[Str]!!,
                    onValueChange = { col[Str] = it
                        sliderPosition.value = it
                        state.value = it
                        colorMap[Str] = state.value},
                )
            }
        }
    }

}

@Composable
fun ColorModelRGB(
    col: MutableMap<String, Float>, /* state */
    color: MutableState<Color>,
    onColorChange: (String) -> Unit /* event */) {
    Column {
        var r = remember { mutableStateOf(colorMap["R"]!!) }
        var g = remember { mutableStateOf(colorMap["G"]!!) }
        var b = remember { mutableStateOf(colorMap["B"]!!) }
        Column (Modifier.padding(6.dp)){
            ColorSlider(col, r,"R", onColorChange, false, 255f)
            ColorSlider(col, g, "G", onColorChange, false, 255f)
            ColorSlider(col, b, "B", onColorChange, false, 255f)
            r.value
            g.value
            b.value
            /*colorMap["R"] = r.value
            colorMap["G"] = g.value
            colorMap["B"] = b.value*/
            //var cmyk = RGBtoCMYK(col["R"]!!,col["G"]!!,col["B"]!!)
            //var hls = RGBtoHLS(col["R"]!!,col["G"]!!,col["B"]!!)
            color.value = Color(col["R"]!!, col["G"]!!, col["B"]!!, 1f)
            //Text("${r.value}, ${g.value}, ${b.value}")
        }
    }
}

@Composable
fun ColorModelCMYK(
    col: MutableMap<String, Float>,
    color: MutableState<Color>,
    onColorChange: (String) -> Unit /* event */
) {
    Column {
        var c = remember {  mutableStateOf(colorMap["C"]!!) }
        var m = remember {  mutableStateOf(colorMap["M"]!!) }
        var y = remember {  mutableStateOf(colorMap["Y"]!!) }
        var k = remember {  mutableStateOf(colorMap["K"]!!)}
        Column (Modifier.padding(6.dp)){
            ColorSlider(col, c, "C", onColorChange, true, 1f)
            ColorSlider(col, m, "M", onColorChange, true, 1f)
            ColorSlider(col, y, "Y", onColorChange, true, 1f)
            ColorSlider(col, k, "K", onColorChange, true, 1f)
            c.value
            m.value
            y.value
            k.value
            /*colorMap["C"] = col["C"]!!
            colorMap["M"] = col["M"]!!
            colorMap["Y"] = col["Y"]!!
            colorMap["K"] = col["K"]!!*/
            var rgb = CMYKtoRGB(col["C"]!!, col["M"]!!, col["Y"]!!, col["K"]!!)
            //var hls = RGBtoHLS(rgb[0], rgb[1], rgb[2])
            color.value = Color(rgb[0], rgb[1], rgb[2], 1f)
            //Text("${c.value}, ${m.value}, ${y.value}, ${k.value}")
        }
    }
}

@Composable
fun ColorModelHLS(
    col: MutableMap<String, Float>,
    color: MutableState<Color>,
    onColorChange: (String) -> Unit /* event */
) {
    Column {
        Column (Modifier.padding(6.dp)){
            var h = remember { mutableStateOf(colorMap["H"]!!) }
            var l = remember { mutableStateOf(colorMap["L"]!!) }
            var s = remember { mutableStateOf(colorMap["S"]!!) }
            ColorSlider(col, h, "H", onColorChange, false, 360f)
            ColorSlider(col, l, "L", onColorChange, false, 1f)
            ColorSlider(col, s, "S", onColorChange, false, 1f)
            h.value
            l.value
            s.value
            /*colorMap["H"] = col["H"]!!
            colorMap["L"] = col["L"]!!
            colorMap["S"] = col["S"]!!*/
            var rgb = HLStoRGB(col["H"]!!, col["L"]!! ,col["S"]!!)
            //var cmyk = RGBtoCMYK(rgb[0], rgb[1], rgb[2])
            color.value = Color(rgb[0], rgb[1], rgb[2], 1f)
            //Text("${h.value}, ${l.value}, ${s.value}")
        }
    }
}

@Composable
fun palitra(col: Color) {
    @OptIn(ExperimentalFoundationApi::class)
    Box(
        modifier = Modifier
            .background(col)
            .width(20.dp)
            .height(20.dp)
            .clickable {
                colorMap["R"] = roundColor(col.red)
                colorMap["G"] = roundColor(col.green)
                colorMap["B"] = roundColor(col.blue)
                color.value = Color(colorMap["R"]!!, colorMap["G"]!!, colorMap["B"]!!,1f)}
    )
}

fun roundColor(col: Float): Float {
    if(col > 1f) {
        warningText.value = "Warning! We have to round the color"
        return 1.0f
    }
    if(col < 0f) {
        warningText.value = "Warning! We have to round the color"
        return 0.0f
    }
    else {
        warningText.value = ""
        return col
    }
}

@Composable
fun ColorComponent() {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth())  {
        var col: MutableMap<String, Float> = remember {colorMap}

        var colorr =  remember {mutableStateOf(Color(0.5F,0.5F,0.5F,1F))}
        Row(Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
        ) {
            RGBtoCMYK(colorMap["R"]!!, colorMap["G"]!!, colorMap["B"]!!)
            RGBtoHLS(colorMap["R"]!!, colorMap["G"]!!, colorMap["B"]!!)
            ColorModelRGB(col = col, color, onColorChange = { onColorChanged(col) })
            ColorModelCMYK(col = col, color, onColorChange = { onColorChanged(col) })
            ColorModelHLS(col = col, color, onColorChange = { onColorChanged(col) })
        }
        Surface (modifier = Modifier.height(100.dp).width(900.dp), color = color.value){
            //Text("${color.value.red}, ${color.value.green}, ${color.value.blue}")
        }
        Column {
            Row {
                palitra(Color.Red)
                palitra(Color.Blue)
                palitra(Color.Green)
                palitra(Color.Cyan)
                palitra(Color.Black)
                palitra(Color.DarkGray)
                palitra(Color.Gray)
                palitra(Color.LightGray)
                palitra(Color.White)
                palitra(Color.Magenta)
            }
            Row {
                palitra(Color.Yellow)
                palitra(Colors.DodgerBlue.getRGB())
                palitra(Colors.Bone.getRGB())
                palitra(Colors.Champagne.getRGB())
                palitra(Colors.ChinaPink.getRGB())
                palitra(Colors.Honeydew.getRGB())
                palitra(Colors.InternationalOrangeEngineering.getRGB())
                palitra(Colors.TerraCotta.getRGB())
                palitra(Colors.OceanGreen.getRGB())
                palitra(Colors.VioleRed.getRGB())
            }
            Text(text = warningText.value, textAlign = TextAlign.Center)
        }
    }
}

fun main() = Window(title = "Compose for Desktop", size = IntSize(1000, 800)) {
    MaterialTheme {
        DesktopTheme() {
            ColorComponent()
        }
    }

}
