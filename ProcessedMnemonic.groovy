package taclelackey

class ProcessedMnemonic {
	List<String> modify = []
	List<String> store = ["sd", "sw", "sb"]
	List<String> load = ["ld", "lw", "lbu", "lwu", "lb"]
	List<String> registers = ["auipc", "addi", "li", "csrw", "lui", "csrs",
		"csrr", "andi", "fmv.s.x", "slli", "add", "mv", "srli", "sub", "or",
		"addiw", "mulw", "subw", "slliw", "divw", "addw", "srliw", "sraiw",
		"mul", "srai", "sext.w", "sraw", "snez", "not", "sllw", "and", "seqz",
		"sltiu", "xori", "div", "xor", "rem", "remu", "divu"]
	List<String> noAction = ["j", "jr", "beqz", "bgeu", "bltz", "bnez", "bltu",
		"ret", "nop", "bge", "blt", "bgez", "beq", "bne"]
	List<String> jumps =["jal"]
	
	def iType
	
	ProcessedMnemonic(def mnemonic)
	{
		if (noAction.find {it == mnemonic}) {
			iType = "instruction"
			return
		}
		if (registers.find {it == mnemonic}) {
			iType = "registers"
			return
		}
		if (modify.find {it == mnemonic }) {
			iType = "modify"
			return
		}
		if (store.find{ it == mnemonic}) {
			iType = "store"
			return
		}
		if (load.find{ it == mnemonic}) {
			iType = "load"
			return
		}
		if (jumps.find{it == mnemonic}) {
			iType = "jump"
			return
		}
		if (mnemonic == "ecall") {
			iType = "done"
			return
		}
		println "$mnemonic not found"
		exit()
	}

}
