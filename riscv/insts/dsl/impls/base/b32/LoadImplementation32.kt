package venusbackend.riscv.insts.dsl.impls.base.b32

import venusbackend.riscv.InstructionField
import venusbackend.riscv.MachineCode
import venusbackend.riscv.insts.dsl.impls.InstructionImplementation
import venusbackend.riscv.insts.dsl.impls.signExtend
import venusbackend.simulator.Simulator

class LoadImplementation32(
    private val load: (Simulator, Int) -> Int,
    private val postLoad: (Int) -> Int
) : InstructionImplementation {
    override operator fun invoke(mcode: MachineCode, sim: Simulator) {
        val rs1 = mcode[InstructionField.RS1].toInt()
        val rd = mcode[InstructionField.RD].toInt()
        val vrs1 = sim.getReg(rs1).toInt()
        val imm = signExtend(mcode[InstructionField.IMM_11_0].toInt(), 12)
        sim.setReg(rd, postLoad(load(sim, vrs1 + imm)))
        sim.incrementPC(mcode.length)
    }
}
