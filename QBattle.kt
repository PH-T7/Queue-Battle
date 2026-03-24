package kata

object PyrrhicVictor {
    fun queueBattle(dist:int,vararg armies:IntArray): Pair<Int,IntArray> {
        Class Soldier(
            val id: Int,
            val life: Int,
            val weapons: String,
            val bulletspeed:Int
        )

        val filaDeSoldados = exércitoA.mapIndexed { index, speed -> 
        Soldier(id = index, bulletSpeed = speed) 
        }
        

    }
}