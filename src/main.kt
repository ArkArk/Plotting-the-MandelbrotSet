import processing.core.PApplet
import processing.core.PVector
import processing.event.MouseEvent
import processing.opengl.PShader

class MainApp: PApplet() {
    private lateinit var sd: PShader
    private var zoomPower: Float = 0f
    private var preMousePressed: Boolean = false
    private var mouseDiff: PVector = PVector(0f, 0f)

    override fun settings() {
        size(600, 400, P3D)
    }

    override fun setup() {
        background(0f)
        sd = loadShader("MandelbrotSet.frag")
        sd.set("resolution", width.toFloat(), height.toFloat())
    }

    override fun draw() {
        updateMouseDiff()
        updateUniformVariables()

        shader(sd)

        beginShape(QUADS)
        PVector(width.toFloat(), height.toFloat()).let {
            vertex(0f, it.y, 0f)
            vertex(it.x, it.y, 0f)
            vertex(it.x, 0f, 0f)
            vertex(0f, 0f, 0f)
        }
        endShape()

        preMousePressed = mousePressed
    }

    fun updateMouseDiff() {
        if (mousePressed && preMousePressed) {
            PVector((mouseX-pmouseX)/width.toFloat()*2f, (mouseY-pmouseY)/height.toFloat()*2f).let {
                mouseDiff = mouseDiff.add(it.mult(exp(zoomPower)))
            }
        }
    }

    fun updateUniformVariables() {
        sd.set("zoom", exp(zoomPower))
        sd.set("diff", mouseDiff.x, -mouseDiff.y)
    }

    override fun mouseWheel(e: MouseEvent) {
        zoomPower += e.count.toFloat() / 10f
    }
}

fun main(args: Array<String>) = PApplet.main(MainApp().javaClass.name)