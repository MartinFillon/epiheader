package com.epiheader.epiheader

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile

class Style: AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project: Project? = event.project
        val file: VirtualFile? = event.getData(PlatformDataKeys.VIRTUAL_FILE)
        val document: Document = event.getData(PlatformDataKeys.EDITOR)!!.document
        val cTypes: Array<String> = arrayOf("int", "char", "void", "struct")
        try {
            val name = file!!.name
            val type = file.extension
            val filename = name.split('.')
            if (type == "c" || type == "h") {
                //Rule C-O4 => only snake case
                if (!isSnakeCase(filename.first())) {
                    Messages.showMessageDialog(
                        event.project, "File name is not in snake case",
                        "CodingStyleCheck", Messages.getErrorIcon()
                    )
                }
                val text = document.text
                //Rule A-3
                if (text.last() != '\n') {
                    Messages.showMessageDialog(
                        event.project, "File does not end with an empty line",
                        "CodingStyleCheck", Messages.getErrorIcon()
                    )
                }
                val lines = text.split('\n')
                var lineNb = 0
                for (line in lines) {
                    lineNb += 1
                    val words = line.split(' ', '(', ')')
                    val itr = words.iterator()
                    while (itr.hasNext()) {
                        val word = itr.next()
                        if (word == "printf") {
                            Messages.showMessageDialog(
                                event.project, "You have a forbidden function in line: $lineNb",
                                "CodingStyleCheck", Messages.getInformationIcon()
                            )
                        }
                        if (word.isType(cTypes)) {
                            if (word == "struct") {
                                val structName = itr.next()
                                val functionName = itr.next()
                                if (!isSnakeCase(functionName)) {
                                    Messages.showMessageDialog(
                                            event.project, "$functionName is not in snake case line: $lineNb",
                                            "CodingStyleCheck", Messages.getErrorIcon()
                                        )
                                }
                            }
                        }
                    }
                }
            }
        } catch (_: Exception) {
        }
    }
}

fun isSnakeCase(str: String): Boolean {
    for (c in str) {
        if (!c.isLetter() && !c.isDigit() && c != '_') {
            return false
        }
        if (c.isLetter()) {
            if (!c.isLowerCase()) {
                return false
            }
        }
    }
    return true
}

fun String.isType(types: Array<String>): Boolean {
    for (type in types) {
        if (this == type) {
            return true
        }
    }
    return false
}
