package com.epiheader.epiheader

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.time.LocalDateTime


class Hello: AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project: Project? = event.project
        val file: VirtualFile? = event.getData(PlatformDataKeys.VIRTUAL_FILE)
        val document: Document = event.getData(PlatformDataKeys.EDITOR)!!.document
        try {
            val year = LocalDateTime.now().year.toString()
            val type = file!!.extension
            val filename = file.name
            var header = ""
            if (type == "c") {
                header = "/*\n** EPITECH PROJECT, " + year + "\n** " + project!!.name + "\n** File description:\n** " + filename + "\n*/\n"
            }
            if (filename == "Makefile") {
                header = "##\n## EPITECH PROJECT, " + year + "\n## " + project!!.name + "\n## File description:\n## " + filename + "\n##\n"
            }
            val text = document.text
            header += text
            val r = Runnable {
                document.setReadOnly(false)
                document.setText(header)
            }
            WriteCommandAction.runWriteCommandAction(project, r)
        } catch (_: Exception) {
        }
    }
}
