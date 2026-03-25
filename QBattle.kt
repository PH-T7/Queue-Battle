package kata

object PyrrhicVictor {
    fun queueBattle(dist:int,vararg armies:IntArray): Pair<Int,IntArray> {
        Class Soldier(
            val id: Int,
            val bulletspeed:Int
        )

        class Army(
            val id: Int,
            var soldiers: ArrayDeque<Soldier>, // A nossa Fila mutável de soldados
            var target: Army?                  // O alvo (pode ser nulo se ele vencer)
        )

        
        

    }
}