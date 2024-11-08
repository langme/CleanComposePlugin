package com.example

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction

class CleanComposeAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        val psiDirectory = PsiManager.getInstance(project).findDirectory(virtualFile) ?: return

        val featureName = Messages.showInputDialog(
            project,
            "Entrez le nom de la feature:",
            "Nouvelle Feature Clean Architecture",
            Messages.getQuestionIcon()
        ) ?: return

        createCleanArchitectureStructure(project, psiDirectory, featureName)
    }

    private fun createCleanArchitectureStructure(project: Project, parentDir: PsiDirectory, featureName: String) {
        try {
            // Utilisation de WriteCommandAction pour les opérations d'écriture
            WriteCommandAction.runWriteCommandAction(project) {
                val featureDir = parentDir.createSubdirectory(featureName)

                // Création de la structure presentation
                val presentationDir = featureDir.createSubdirectory("presentation")
                val presentationDirUI = presentationDir.createSubdirectory("ui")
                presentationDirUI.createSubdirectory("screens")
                presentationDirUI.createSubdirectory("composents")
                presentationDirUI.createSubdirectory("theme")
                presentationDir.createSubdirectory("viewmodels")
                presentationDir.createSubdirectory("states")
                presentationDir.createSubdirectory("events")
                presentationDir.createSubdirectory("mappers")
                presentationDir.createSubdirectory("navigation")

                // Création de la structure domain
                val domainDir = featureDir.createSubdirectory("domain")
                domainDir.createSubdirectory("usecases")
                domainDir.createSubdirectory("models")
                domainDir.createSubdirectory("repositories")
                domainDir.createSubdirectory("validators")

                // Création de la structure data
                val dataDir = featureDir.createSubdirectory("data")
                dataDir.createSubdirectory("repositories")
                val dataDirData = dataDir.createSubdirectory("datasources")
                dataDirData.createSubdirectory("local")
                dataDirData.createSubdirectory("remote")
                dataDir.createSubdirectory("mappers")
                val dataDirModel = dataDir.createSubdirectory("models")
                dataDirModel.createSubdirectory("local")
                dataDirModel.createSubdirectory("remote")
            }

            ApplicationManager.getApplication().invokeLater {
                Messages.showInfoMessage(
                    project,
                    "Structure Clean Architecture créée avec succès!",
                    "Succès"
                )
            }
        } catch (e: Exception) {
            ApplicationManager.getApplication().invokeLater {
                Messages.showErrorDialog(
                    project,
                    "Erreur lors de la création de la structure: ${e.message}",
                    "Erreur"
                )
            }
        }
    }

    override fun update(e: AnActionEvent) {
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
        e.presentation.isEnabledAndVisible = virtualFile?.isDirectory == true
    }
}