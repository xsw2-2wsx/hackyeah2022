package com.xsw22wsx.hackyeah.instructions.io.storage

import com.xsw22wsx.hackyeah.instructions.Instruction
import com.xsw22wsx.hackyeah.instructions.InstructionRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class InMemoryInstructionRepository : InstructionRepository {

    private val instructions: MutableCollection<Instruction> = LinkedList()

    private val instructionIds: Iterator<Int> = generateSequence(0) { it + 1 }.iterator()

    @Synchronized fun generateId(): Int = instructionIds.next()


    override suspend fun saveInstruction(newInstruction: Instruction) {
        newInstruction.id = generateId()
        instructions.add(newInstruction)
    }

    override suspend fun findInstructions(limit: Int, offset: Int): List<Instruction> = instructions
        .drop(offset)
        .take(limit)

    override suspend fun findAllInstructions(): List<Instruction> = LinkedList(instructions)

    override suspend fun deleteInstruction(instructionId: Int) {
        instructions
            .removeIf { it.id == instructionId }
    }

    override suspend fun updateInstruction(instruction: Instruction) {
        deleteInstruction(instruction.id)
        instructions.add(instruction)
    }

    override suspend fun findInstruction(id: Int): Instruction? = instructions.firstOrNull { it.id == id }

}