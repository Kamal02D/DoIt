package org.example.projectrr


object Constants{
        object DataBase {
            const val NAME : String = "Todo"
            const val STORAGE_FILE_NAME = "todo_db"
            object Tables {
                object Task{
                    const val NAME : String = "task"
                    object Columns {
                        const val TEXT : String = "text"
                        const val IS_DONE : String = "isDone"
                    }
                }
            }
        }
}
