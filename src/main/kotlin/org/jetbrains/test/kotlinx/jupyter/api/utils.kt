package org.jetbrains.test.kotlinx.jupyter.api

import org.jetbrains.kotlinx.jupyter.api.*
import org.jetbrains.kotlinx.jupyter.api.libraries.*
import kotlin.reflect.typeOf

fun printAllCellsContents(nb: Notebook) {
    nb.cellsList.forEach { cell ->
        println("Cell #${cell.id}:")
        println(cell.code)
        println()
    }
}

fun htmlResultId(text: String, id: Int?) = MimeTypedResult(mapOf("text/html" to text), id = id?.toString())

class RenderableColor(private val name: String, private val id: Int?): Renderable {
    override fun render(notebook: Notebook): MimeTypedResult {
        return htmlResultId("""
            <span style="color:$name">$name</span>
        """.trimIndent(), id)
    }



}

class TestSession(private val notebook: Notebook) {
    fun visualizeColor(name: String): RenderableColor {
        return RenderableColor(name, notebook.currentCell?.id)
    }
}
/*
@JupyterLibrary
class ApiTestLibraryDefinitionProducer: LibraryDefinitionProducer {
    override fun getDefinitions(notebook: Notebook?): List<LibraryDefinition> {
        return listOf(
            LibraryDefinitionImpl(
                imports = listOf("org.jetbrains.test.kotlinx.jupyter.api.*"),
                init = listOf(CodeExecution("val ses = TestSession(notebook)").toExecutionCallback())
            )
        )
    }
}

@JupyterLibrary
class ApiTestLibraryDefinitionProducer3: LibraryDefinitionProducer {
    override fun getDefinitions(notebook: Notebook): List<LibraryDefinition> {
        return listOf(
            libraryDefinition {
                it.imports = listOf("org.jetbrains.test.kotlinx.jupyter.api.*")
                it.init = listOf(CodeExecution("val ses = TestSession(notebook)").toExecutionCallback())
            }
        )
    }
}
*/

class Integration1: JupyterIntegration() {
    override fun Builder.onLoaded() {
        onLoaded { execute("val xxx = 1") }
    }

}

class Integration2: JupyterIntegration() {
    override fun Builder.onLoaded() {
        onLoaded { execute("val yyy = 2") }
    }

}

class Integration3(
    notebook: Notebook,
    private val options: Map<String, String>,
): JupyterIntegration() {
    override fun Builder.onLoaded() {
        onLoaded {
            declare(
                VariableDeclaration(
                    "integrationOptions",
                    options,
                    typeOf<Map<String, String>>()
                )
            )
        }
    }

}
