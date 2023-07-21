package com.example.proyecto015

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {
    private lateinit var et1: EditText
    private lateinit var et2: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        et1 = findViewById(R.id.editText1)
        et2 = findViewById(R.id.editText2)

        // Llamar a checkStoragePermissions() en el método onCreate()
        checkStoragePermissions()
    }

    private val permissionRequestCode = 1001

    // Verificar permisos de almacenamiento en tiempo de ejecución
    private fun checkStoragePermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                permissionRequestCode
            )
        }
    }


    fun grabar(v: View) {
        val nomarchivo = et1.text.toString()
        val contenido = et2.text.toString()

        val externalFilesDir = getExternalFilesDir(null)
        if (externalFilesDir != null) {
            val file = File(externalFilesDir, nomarchivo)
            try {
                val osw = OutputStreamWriter(FileOutputStream(file))
                osw.write(contenido)
                osw.flush()
                osw.close()

                Toast.makeText(this, "Datos guardados en la tarjeta SD", Toast.LENGTH_SHORT).show()

                et1.text.clear()
                et2.text.clear()
            } catch (ioe: IOException) {
                Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Almacenamiento externo no disponible o no listo para escribir", Toast.LENGTH_SHORT).show()
        }
    }

    fun recuperar(v: View) {
        val nomarchivo = et1.text.toString()
        val tarjeta = Environment.getExternalStorageDirectory()
        val file = File(tarjeta.absolutePath, nomarchivo)
        try {
            val fIn = FileInputStream(file)
            val archivo = InputStreamReader(fIn)
            val br = BufferedReader(archivo)
            var linea: String? = br.readLine()
            var todo = ""
            while (linea != null) {
                todo += "$linea\n"
                linea = br.readLine()
            }
            br.close()
            archivo.close()
            et2.setText(todo)
        } catch (e: IOException) {
            Toast.makeText(this, "Archivo no encontrado", Toast.LENGTH_SHORT).show()
        }
    }
}
