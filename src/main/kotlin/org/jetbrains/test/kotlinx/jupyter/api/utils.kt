package org.jetbrains.test.kotlinx.jupyter.api

import org.jetbrains.kotlinx.jupyter.api.*
import org.jetbrains.kotlinx.jupyter.api.libraries.CodeExecution
import org.jetbrains.kotlinx.jupyter.api.libraries.LibraryDefinition
import org.jetbrains.kotlinx.jupyter.api.libraries.LibraryDefinitionImpl
import org.jetbrains.kotlinx.jupyter.api.libraries.LibraryDefinitionProducer

fun printAllCellsContents(nb: Notebook<*>) {
    nb.cells.forEach { (number, cell) ->
        println("Cell #$number:")
        println(cell.code)
        println()
    }
}

fun htmlResultId(text: String, id: Int?) = MimeTypedResult(mapOf("text/html" to text), id = id?.toString())

class RenderableColor(private val name: String, private val id: Int?): Renderable {
    override fun render(notebook: Notebook<*>): MimeTypedResult {
        return htmlResultId("""
            <span style="color:$name">$name</span>
        """.trimIndent(), id)
    }

}

class TestSession(private val notebook: Notebook<*>) {
    fun visualizeColor(name: String): RenderableColor {
        return RenderableColor(name, notebook.currentCell?.id)
    }
}

class ApiTestLibraryDefinitionProducer: LibraryDefinitionProducer {
    override fun getDefinitions(notebook: Notebook<*>?): List<LibraryDefinition> {
        return listOf(
            LibraryDefinitionImpl(
                imports = listOf("org.jetbrains.test.kotlinx.jupyter.api.*"),
                init = listOf(CodeExecution("val ses = TestSession(notebook)"))
            )
        )
    }
}
