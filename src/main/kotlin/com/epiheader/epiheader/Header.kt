package com.epiheader.epiheader

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.time.LocalDateTime


class Header: AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project: Project? = event.project
        val file: VirtualFile? = event.getData(PlatformDataKeys.VIRTUAL_FILE)
        val document: Document = event.getData(PlatformDataKeys.EDITOR)!!.document
        try {
            val year = LocalDateTime.now().year.toString()
            val type = file!!.extension
            var filename = file.name
            if (file.name.contentEquals("."))
                filename = file.name.substring(0, file.name.lastIndexOf('.'))
            val defineFilename = file.name.uppercase().replace('.', '_') + "_"
            val className = filename.replaceFirstChar { it.uppercase() }
            var header = ""
            if (type == "c" || type == "cpp" || type == "cc" || type == "h" || type == "hpp" || type == "hh") {
                header = "/*\n** EPITECH PROJECT, " + year + "\n** " + project!!.name + "\n** File description:\n** " + filename + "\n*/\n"
            }
            if (type == "hs") {
                header = "{-\n-- EPITECH PROJECT, " + year + "\n-- " + project!!.name + "\n-- File description:\n-- " + filename + "\n-}\n"
            }
            if (filename == "Makefile") {
                header = "##\n## EPITECH PROJECT, " + year + "\n## " + project!!.name + "\n## File description:\n## " + filename + "\n##\n"
            }
            if (type == "h" || type == "hh") {
                header += "#ifndef $defineFilename\n\t#define $defineFilename\n#endif /*$defineFilename*/"
            }
            if (type == "hpp") {
                header += "#ifndef $defineFilename\n\t#define $defineFilename\n"
                header += "\nclass $className {\n\tpublic:\n\t\t$className();\n\t\t~$className();\n\n\tprotected:\n\tprivate:\n};\n\n"
                header += "#endif /*$defineFilename*/"
            }
            val text = document.text
            header += text
            val r = Runnable {
                document.setReadOnly(false)
                document.setText(header)
            }
            WriteCommandAction.runWriteCommandAction(project, r)
        } catch (error: Exception) {
            System.err.println(error)
        }
    }
}
