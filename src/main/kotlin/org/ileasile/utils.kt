package org.ileasile

import org.jetbrains.kotlin.jupyter.api.Notebook

fun printAllCellsContents(nb: Notebook<*>) {
    nb.cells.forEach { (number, cell) ->
        println("Cell #$number:")
        println(cell.code)
        println()
    }
}