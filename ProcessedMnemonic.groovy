package taclelackey

class ProcessedMnemonic {
	List<String> modify = []
	List<String> store = ["sd", "sw"]
	List<String> load = []
	List<String> registers = ["auipc", "addi", "li", "csrw", "lui", "csrs",
		"csrr", "andi", "fmv.s.x", "slli", "add", "mv", "srli", "sub"]
	List<String> noAction = ["j", "jr", "beqz", "bgeu", "bltz", "jal"]
	
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
		println "$mnemonic not found"
		exit()
	}

}
