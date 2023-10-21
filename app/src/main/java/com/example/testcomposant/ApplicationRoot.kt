package com.example.testcomposant
import android.app.Application
import android.content.Context

/**
 * Classe permettant de récupérer le contexte de l'application
 * depuis n'importe où dans le code
 *
 * Pour cela il suffit d'appeler ApplicationRoot.getContext()
 *
 * Elle est initialisée dans le fichier AndroidManifest.xml
 * application android:name=".ApplicationRoot"
 * Android va automatiquement appeler la méthode onCreate() pour nous
 * afin d'initialiser la variable INSTANCE
 */
class ApplicationRoot: Application() {

    companion object {
        private lateinit var INSTANCE: Application

        fun getContext(): Context = INSTANCE.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}
