package com.example.notificationsmap.data.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.notificationsmap.data.entities.ActiveTaskEntity
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MarkerDatabaseTest : TestCase() {
    private lateinit var db: MarkerDatabase
    private lateinit var dao: MarkerDao

    @Before
    fun createDatabase() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MarkerDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.markerDao()
    }

    @Test
    fun getAndInsertTask() = runBlocking {
        val task = ActiveTaskEntity(
            1,
            "New Task",
            "(55.275614,38.749295)",
            "Description of New Task",
            55.275614,
            38.749295
        )
        dao.insertTask(task)
        val tasks = dao.getAllTasks()
        assertEquals(1, tasks.size)
        assertEquals(true, tasks.contains(task))
    }

    @Test
    fun updateTask() = runBlocking {
        val task = ActiveTaskEntity(
            1,
            "New Task",
            "(55.275614,38.749295)",
            "Description of New Task",
            55.275614,
            38.749295
        )
        dao.insertTask(task)

        val updatedTask = ActiveTaskEntity(
            1,
            "Buy Bread",
            "(55.275614,38.749295)",
            "Description of New Task",
            55.275614,
            38.749295
        )
        dao.updateTask(updatedTask)

        val tasks = dao.getAllTasks()
        assertEquals(1, tasks.size)
        assertEquals("Buy Bread", tasks[0].name)
    }

    @Test
    fun deleteTask() = runBlocking {
        val task = ActiveTaskEntity(
            1,
            "New Task",
            "(55.275614,38.749295)",
            "Description of New Task",
            55.275614,
            38.749295
        )
        dao.insertTask(task)

        dao.deleteTask(task)
        val tasks = dao.getAllTasks()
        assertEquals(0, tasks.size)
    }

    @Test
    fun getTaskById() = runBlocking {
        val task = ActiveTaskEntity(
            1,
            "New Task",
            "(55.275614,38.749295)",
            "Description of New Task",
            55.275614,
            38.749295
        )
        dao.insertTask(task)

        val taskById = dao.getTaskById(1)
        assertEquals(task, taskById)
    }

    @After
    fun closeDb() {
        db.close()
    }


//    fun insertAndGetNight() {
//        val night = ActiveTaskEntity()
//        sleepDao.insert(night)
//        val tonight = sleepDao.getTonight()
//        assertEquals(tonight?.sleepQuality, -1)
//    }
}