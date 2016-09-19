package taclelackey

class RegisterFile {

	def pc
	def registers = []
	def registerMap = [:]
	def memory = [:]
	def maxRegVal
	
	def auipc = {par1, par2, ig1 ->
		def addValue = new BigInteger(par2.stripIndent(2), 16)
		registers[registerMap[par1]] = pc + addValue.shiftLeft(12)
	}
	
	def addi = {par1, par2, par3 ->
		registers[registerMap[par1]] = registers[registerMap[par2]] + 
			new BigInteger(par3, 10)
	}
	
	def add = {par1, par2, par3 ->
		registers[registerMap[par1]] = registers[registerMap[par2]] +
			registers[registerMap[par3]]
	}
	
	def csr_rw = {par1, par2, ig1 ->
		registers[registerMap[par1]] = registers[registerMap[par2]]
	}
	
	def li = {par1, par2, ig1 ->
		registers[registerMap[par1]] = new BigInteger(par2, 10)
	}
	
	def lui = {par1, par2, ig1 ->
		def addValue = new BigInteger(par2.stripIndent(2), 16)
		registers[registerMap[par1]] = addValue.shiftLeft(12)
	}
	
	def csr_or = {par1, par2, ig1 ->
		registers[registerMap[par1]] |= registers[registerMap[par2]]
	}
	
	def andi = {par1, par2, par3 ->
		def addValue = par3.toInteger()
		registers[registerMap[par1]] = registers[registerMap[par2]] & addValue
	}
	
	def slli = {par1, par2, par3 ->
		registers[registerMap[par1]] = 
			BigInteger.valueOf((registers[registerMap[par2]]))
				.shiftLeft(par3.toInteger())
		registers[registerMap[par1]] &= 0xFFFFFFFFFFFFFFFF
	}
	
	def srli = {par1, par2, par3 ->
		registers[registerMap[par1]] =
		(registers[registerMap[par2]]).shiftRight(par3.toInteger())
	}
	
	def mv = {par1, par2, ig1 ->
		registers[registerMap[par1]] = registers[registerMap[par2]]
	}
	
	def sub = {par1, par2, par3 ->
		def result = registers[registerMap[par2]] -
			registers[registerMap[par3]]
		registers[registerMap[par1]] = result & 0xFFFFFFFFFFFFFFFF
	}	
	
	def stateUpdates = ["auipc":auipc, "addi":addi, "csrw": csr_rw, "li":li,
		"lui": lui, "csrs": csr_or, "csrr":csr_rw, "andi": andi,
		"fmv.s.x": csr_rw, "add": add, "slli": slli, "mv":mv,
		"srli":srli, "sub":sub]

	def sd = {par1, par2, par3, xml ->
		BigInteger valToStore = registers[registerMap[par1]]
		def baseAddress = registers[registerMap[par3]]
		def writeAddress = baseAddress + par2.toInteger()
		(0..7).each {offset ->
			memory[writeAddress + offset] = 
				(valToStore.shiftRight(8 * offset)) & 0xFF
		}
		def hexWriteAddress = "0x" + writeAddress.toString(16)
		xml.store(address:hexWriteAddress, size:8)
		
	}
	
	def sw = {par1, par2, par3, xml ->
		BigInteger valToStore = registers[registerMap[par1]]
		def baseAddress = registers[registerMap[par3]]
		def writeAddress = baseAddress + par2.toInteger()
		(0 .. 3).each { offset ->
			memory[writeAddress + offset] =
				(valToStore.shiftRight(8 * offset)) & 0xFF
		}
		def hexWriteAddress = "0x" + writeAddress.toString(16)
		xml.store(address:hexWriteAddress, size:4)
	}
	
	def storeUpdates = ["sd":sd, "sw":sw]
	
	def jal = {par1, par2 ->
		def multiplier = 1
		if (par1 == "-") {
			multiplier = -1
		}
		registers[registerMap["ra"]] = pc +
			new BigInteger(par2, 16) * multiplier
	}

	
	def jumpUpdates = ["jal":jal]
	
	RegisterFile(def startPCVal)
	{
		pc = startPCVal
		def regVal = 0
		registerMap["zero"] = regVal++	//hard-wired zero
		registerMap["ra"] = regVal++ 	//return address
		registerMap["sp"] = regVal++	//stack pointer
		registerMap["gp"] = regVal++	//global pointer
		registerMap["tp"] = regVal++	//thread pointer
		registerMap["t0"] = regVal++	//temporaries
		registerMap["t1"] = regVal++
		registerMap["t2"] = regVal++
		registerMap["s0"] = regVal		//saved register
		registerMap["fp"] = regVal++	//also frame pointer
		registerMap["s1"] = regVal++
		registerMap["a0"] = regVal++	//arguments
		registerMap["a1"] = regVal++	//and returns
		registerMap["a2"] = regVal++	//just arguments
		registerMap["a3"] = regVal++
		registerMap["a4"] = regVal++
		registerMap["a5"] = regVal++
		registerMap["a6"] = regVal++
		registerMap["a7"] = regVal++
		registerMap["s2"] = regVal++
		registerMap["s3"] = regVal++
		registerMap["s4"] = regVal++
		registerMap["s5"] = regVal++
		registerMap["s6"] = regVal++
		registerMap["s7"] = regVal++
		registerMap["s8"] = regVal++
		registerMap["s9"] = regVal++
		registerMap["s10"] = regVal++
		registerMap["s11"] = regVal++
		registerMap["t3"] = regVal++
		registerMap["t4"] = regVal++
		registerMap["t5"] = regVal++
		registerMap["t6"] = regVal++
		
		//floating point - we don't care so much
		registerMap["ft0"] = regVal++
		registerMap["ft1"] = regVal++
		registerMap["ft2"] = regVal++
		registerMap["ft3"] = regVal++
		registerMap["ft4"] = regVal++
		registerMap["ft5"] = regVal++
		registerMap["ft6"] = regVal++
		registerMap["ft7"] = regVal++
		registerMap["fs0"] = regVal++
		registerMap["fs1"] = regVal++
		registerMap["fa0"] = regVal++
		registerMap["fa1"] = regVal++
		registerMap["fa2"] = regVal++
		registerMap["fa3"] = regVal++
		registerMap["fa4"] = regVal++
		registerMap["fa5"] = regVal++
		registerMap["fa6"] = regVal++
		registerMap["fa7"] = regVal++
		registerMap["fs2"] = regVal++
		registerMap["fs3"] = regVal++
		registerMap["fs4"] = regVal++
		registerMap["fs5"] = regVal++
		registerMap["fs6"] = regVal++
		registerMap["fs7"] = regVal++
		registerMap["fs8"] = regVal++
		registerMap["fs9"] = regVal++
		registerMap["fs10"] = regVal++
		registerMap["fs11"] = regVal++
		registerMap["ft8"] = regVal++
		registerMap["ft9"] = regVal++
		registerMap["ft10"] = regVal++
		registerMap["ft11"] = regVal++
		
		//csrs
		registerMap["misa"] = regVal++
		registerMap["mvendorid"] = regVal++
		registerMap["marchid"] = regVal++
		registerMap["mimpid"] = regVal++
		registerMap["mhartid"] = regVal++
		registerMap["mstatus"] = regVal++
		registerMap["medeleg"] = regVal++
		registerMap["mideleg"] = regVal++
		registerMap["mie"] = regVal++
		registerMap["mtvec"] = regVal++
		registerMap["mscratch"] = regVal++
		registerMap["mepc"] = regVal++
		registerMap["mcause"] = regVal++
		registerMap["mbadaddr"] = regVal
		registerMap["mip"] = regVal++
		registerMap["mtime"] = regVal++
		registerMap["mtimecmp"] = regVal++
		
		registerMap["fcsr"] = regVal++
	
		maxRegVal = regVal
		
		registerMap.each {
			registers << 0 
		}		
	}
	
	public pcUpdate(def newPCVal = -1)
	{
		if (newPCVal == - 1) {
			pc += 4
		} else {
			pc = newPCVal
		}
	}
	
	public unmangleStateUpdate(def lineIn)
	{
		lineIn.find (
			/(.+):\s*(\w+)\s+\((\w+)\)\s*(\S*)\s*(\w*),\s*([-\w]*),?\s?(\S*)?/,
			{match, op1, op2, op3, op4, op5, op6, op7 ->
				//println "state update with $op4 and paramters $op5, $op6, $op7"
				(stateUpdates.find {it.key == op4}.value).call(op5, op6, op7)
			}
		)
	}
	
	public unmangleStore(def lineIn, def xml)
	{
		lineIn.find(
			/(.+):\s*(\w+)\s+\((\w+)\)\s*(\S*)\s*(\w*),\s*(-?\w*)\((\w*)\)/,
			{match, op1, op2, op3, op4, op5, op6, op7 ->
			//	println "store with $op4 and parameters $op5, $op6, $op7"
				(storeUpdates.find {it.key == op4}.value).call(op5, op6,
					op7, xml)
			}
		)
	}
	
	public unmangleJump(def lineIn)
	{
		lineIn.find(
			/(.+):\s*(\w+)\s+\((\w+)\)\s*(\S*)\s*\w*\s*([-+])\s*[0x]*(\S*)/,
			{match, op1, op2, op3, op4, op5, op6 ->
				(jumpUpdates.find{it.key == op4}.value).call(op5, op6)
			}
		)
	}
	

}
