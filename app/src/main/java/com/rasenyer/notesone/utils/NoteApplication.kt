package com.rasenyer.notesone.utils

import android.app.Application
import com.rasenyer.notesone.db.NoteDatabase

class NoteApplication : Application() {
    val noteDatabase: NoteDatabase by lazy { NoteDatabase.getDatabase(this) }
}