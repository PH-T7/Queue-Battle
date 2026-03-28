package kata

import java.util.PriorityQueue
import kotlin.math.ceil

class Soldier(
    val id: Int,
    val bulletspeed: Int
)

class Army(
    val id: Int,
    var soldiers: ArrayDeque<Soldier>
) {
    var target: Army? = null

    // A MÁGICA: Em vez de balas voando, nós anotamos o ROUND exato em que ele vai tomar o tiro.
    // O PriorityQueue sempre deixa o menor número (o tiro mais próximo) na frente da fila!
    val incomingBullets = PriorityQueue<Long>()
}

object PyrrhicVictor {
    fun queueBattle(dist: Int, vararg armies: IntArray): Pair<Int, IntArray> {

        var exercitosVivos = armies.mapIndexed { indexExercito, speedsArray ->
            val filaDeSoldados = ArrayDeque(
                speedsArray.mapIndexed { indexSoldado, velocidade ->
                    Soldier(id = indexSoldado, bulletspeed = velocidade)
                }
            )
            Army(id = indexExercito, soldiers = filaDeSoldados)
        }.toMutableList()

        // Conecta os alvos iniciais
        for (i in exercitosVivos.indices) {
            val proximoIndex = (i + 1) % exercitosVivos.size
            exercitosVivos[i].target = exercitosVivos[proximoIndex]
        }

        var currentRound = 0L // O Relógio Global do jogo

        while (exercitosVivos.size > 1) {
            currentRound++
            var eliminacao = false

            // 1. O turno de 1 segundo: Quem morre e quem atira
            for (exercito in exercitosVivos) {
                var tomouTiro = false

                // Verifica se tem alguma bala agendada para cair na cabeça dele NESTE round
                while (exercito.incomingBullets.isNotEmpty() && exercito.incomingBullets.peek() <= currentRound) {
                    exercito.incomingBullets.poll() // A bala bateu e foi consumida
                    tomouTiro = true
                }

                if (tomouTiro) {
                    // Regra 5: O cara da frente absorve todas as balas do segundo e cai ANTES de atirar
                    exercito.soldiers.removeFirst()

                    if (exercito.soldiers.isEmpty()) {
                        eliminacao = true // O exército inteiro foi dizimado
                    }
                } else {
                    // Se não tomou tiro, ele atira!
                    if (exercito.soldiers.isNotEmpty()) {
                        val atirador = exercito.soldiers.first()

                        // A Fórmula do Salto Temporal: Em que round essa bala bate?
                        val travelRounds = ceil(dist.toDouble() / atirador.bulletspeed.toDouble()).toLong()
                        val impactRound = currentRound + travelRounds

                        // Anotamos na "agenda de morte" do alvo
                        exercito.target!!.incomingBullets.add(impactRound)

                        // Atirou, vai para o final da fila
                        exercito.soldiers.removeFirst()
                        exercito.soldiers.addLast(atirador)
                    }
                }
            }

            // 2. Faxina e Reposicionamento (Regras 8A e 8B)
            if (eliminacao) {
                exercitosVivos.removeIf { it.soldiers.isEmpty() }

                if (exercitosVivos.size > 1) {
                    // Regra 8A: Se alguém morreu, todas as balas no ar perdem o alvo e somem
                    for (exercito in exercitosVivos) {
                        exercito.incomingBullets.clear()
                    }

                    // Fecham o círculo de novo
                    for (i in exercitosVivos.indices) {
                        val proximoIndex = (i + 1) % exercitosVivos.size
                        exercitosVivos[i].target = exercitosVivos[proximoIndex]
                    }
                }
            }
        }

        // 3. O Fim da Guerra
        return if (exercitosVivos.size == 1) {
            Pair(exercitosVivos[0].id, exercitosVivos[0].soldiers.map { it.id }.toIntArray())
        } else {
            Pair(-1, intArrayOf())
        }
    }
}
