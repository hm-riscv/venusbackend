package venusbackend.riscv.insts.dsl.impls.base.b32

import venusbackend.riscv.InstructionField
import venusbackend.riscv.MachineCode
import venusbackend.riscv.insts.dsl.impls.InstructionImplementation
import venusbackend.riscv.insts.dsl.impls.setBitslice
import venusbackend.riscv.insts.dsl.impls.signExtend
import venusbackend.simulator.Simulator

class STypeImplementation32(private val store: (Simulator, Int, Int) -> Unit) : InstructionImplementation {
    override operator fun invoke(mcode: MachineCode, sim: Simulator) {
        val rs1 = mcode[InstructionField.RS1].toInt()
        val rs2 = mcode[InstructionField.RS2].toInt()
        val imm = constructStoreImmediate(mcode)
        val vrs1 = sim.getReg(rs1).toInt()
        val vrs2 = sim.getReg(rs2).toInt()
        store(sim, vrs1 + imm, vrs2)
        sim.incrementPC(mcode.length)
    }
}

fun constructStoreImmediate(mcode: MachineCode): Int {
    val imm_11_5 = mcode[InstructionField.IMM_11_5].toInt()
    val imm_4_0 = mcode[InstructionField.IMM_4_0].toInt()
    var imm = 0
    imm = setBitslice(imm, imm_11_5, 5, 12)
    imm = setBitslice(imm, imm_4_0, 0, 5)
    return signExtend(imm, 12)
}
