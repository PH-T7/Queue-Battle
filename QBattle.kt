package kata

class Soldier(
    val id: Int,
    val bulletspeed: Int
)

class Army(
    val id: Int,
    var soldiers: ArrayDeque<Soldier>, // A nossa Fila mutável de soldados
    var target: Army?                  // O alvo (pode ser nulo se ele vencer)
)

class Bullet(
    val target: Army,
    var timeToImpact: Double
)


object PyrrhicVictor {
    fun queueBattle(dist: Int, vararg armies: IntArray): Pair<Int, IntArray> {

        var exercitosVivos = armies.mapIndexed { indexExercito, speedsArray ->

            val filaDeSoldados = ArrayDeque(
                speedsArray.mapIndexed { indexSoldado, velocidade ->
                    Soldier(id = indexSoldado, bulletspeed = velocidade)
                }
            )

            Army(id = indexExercito, soldiers = filaDeSoldados, target = null)

        }.toMutableList()

        for (i in exercitosVivos.indices) {
            val proximoIndex = (i + 1) % exercitosVivos.size
            exercitosVivos[i].target = exercitosVivos[proximoIndex]
        }

        var balasNoAr = mutableListOf<Bullet>()


        while (exercitosVivos.size > 1) {

            val iteratorBalas = balasNoAr.iterator()

            while (iteratorBalas.hasNext()) {
                val bala = iteratorBalas.next()
                bala.timeToImpact -= 1.0

                if (bala.timeToImpact <= 0.0) {

                    val alvo = bala.target

                    if (alvo.soldiers.isNotEmpty()) {

                        alvo.soldiers.removeFirst()
                    }

                    iteratorBalas.remove()
                }

                for (exercito in exercitosVivos) {
                    if (exercito.soldiers.isNotEmpty()) {

                        val soldadoDaFrente = exercito.soldiers.first()

                        val tempoDeVoo = dist.toDouble() / soldadoDaFrente.bulletspeed.toDouble()

                        val novaBala = Bullet(target = exercito.target!!, timeToImpact = tempoDeVoo)

                        balasNoAr.add(novaBala)

                        val atirador = exercito.soldiers.removeFirst()
                        exercito.soldiers.addLast(atirador)
                    }
                }

                val exercitosVivosAntes = exercitosVivos.size

                exercitosVivos.removeIf { exercito -> exercito.soldiers.isEmpty() }

                if (exercitosVivos.size < exercitosVivosAntes) {

                    balasNoAr.clear()

                    for (i in exercitosVivos.indices) {
                        val proximoIndex = (i + 1) % exercitosVivos.size
                        exercitosVivos[i].target = exercitosVivos[proximoIndex]
                    }
                }


            }
        }

        if (exercitosVivos.size == 1) {
            return Pair(exercitosVivos[0].id, exercitosVivos[0].soldiers.map { it.id }.toIntArray())
        } else {
            return Pair(-1, intArrayOf())
        }
    }
}
