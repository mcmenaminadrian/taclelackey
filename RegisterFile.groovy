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
		registers[registerMap[par1]] = (registers[registerMap[par2]] + 
			new BigInteger(par3, 10)).and(0xFFFFFFFFFFFFFFFF)
	}
	
	def add = {par1, par2, par3 ->
		registers[registerMap[par1]] = (registers[registerMap[par2]] +
			registers[registerMap[par3]]).and(0xFFFFFFFFFFFFFFFF)
	}
	
	def addiw = {par1, par2, par3 ->
		int tempA = registers[registerMap[par2]].and(0xFFFFFFFF)
		int tempB = par3.toInteger()
		int sum = tempA + tempB
		BigInteger bigSum = sum
		registers[registerMap[par1]] = bigSum
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
	
	def slliw = {par1, par2, par3 ->
		int tempA = registers[registerMap[par2]].and(0xFFFFFFFF)
		int tempB = par3.toInteger()
		int shifted = tempA << tempB
		BigInteger bigShifted = shifted
		registers[registerMap[par1]] = bigShifted
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
	
	def subw = {par1, par2, par3 ->
		int tempA = registers[registerMap[par2]].and(0xFFFFFFFF)
		int tempB = registers[registerMap[par3]].and(0xFFFFFFFF)
		int result = tempA - tempB
		BigInteger bigResult = result
		registers[registerMap[par1]] = bigResult
	}
	
	
	def or = {par1, par2, par3 ->
		registers[registerMap[par1]] =
			registers[registerMap[par2]].or(registers[registerMap[par3]])
	}
	
	def mulw = {par1, par2, par3 ->
		int tempA = (registers[registerMap[par2]]).and(0xFFFFFFFF)
		int tempB = (registers[registerMap[par3]]).and(0xFFFFFFFF)
		int multi = tempA * tempB
		BigInteger bigMulti = multi
		registers[registerMap[par1]] = bigMulti
	}
	
	def divw = {par1, par2, par3 ->
		int tempA = registers[registerMap[par2]].and(0xFFFFFFFF)
		int tempB = (registers[registerMap[par3]].abs()).and(0xFFFFFFFF)
		int div = tempA / tempB
		BigInteger bigDiv = div
		registers[registerMap[par1]] = div
	}
	
	def addw = {par1, par2, par3 ->
		int tempA = (registers[registerMap[par2]]).and(0xFFFFFFFF)
		int tempB = (registers[registerMap[par3]]).and(0xFFFFFFFF)
		int addUp = tempA + tempB
		BigInteger bigAddUp = addUp
		registers[registerMap[par1]] = bigAddUp
	}
	
	def srliw = {par1, par2, par3 ->
		println "SRLIW $par1 $par2 $par3"
		int tempA = (registers[registerMap[par2]]).and(0xFFFFFFFF)
		int tempB = par3.toInteger() 
		int shiftedLeft = tempA >> tempB
		BigInteger bigShiftedLeft = shiftedLeft
		registers[registerMap[par1]] = bigShiftedLeft
	}
	
	def sraiw = {par1, par2, par3 ->
		println "SRAIW $par1 $par2 $par3"
		int tempA = registers[registerMap[par2]].and(0xFFFFFFFF)
		int signA = Integer.signum(tempA)
		int tempB = par3.toInteger() % 0x400
		int initShift = tempA >> tempB
		if (Integer.signum(initShift) != signA) {
			initShift *= -1
		}
		BigInteger arithShift = new BigInteger(initShift)
		registers[registerMap[par1]] = arithShift
	}
	
	def stateUpdates = ["auipc":auipc, "addi":addi, "csrw": csr_rw, "li":li,
		"lui": lui, "csrs": csr_or, "csrr":csr_rw, "andi": andi,
		"fmv.s.x": csr_rw, "add": add, "slli": slli, "mv":mv,
		"srli":srli, "sub":sub, "or":or, "addiw":addiw,
		"mulw": mulw, "subw": subw, "slliw":slliw, "divw":divw,
		"addw": addw, "srliw": srliw, "sraiw": sraiw]

	def sd = {par1, par2, par3, xml ->
	//	def hexPar1 = (registers[registerMap[par1]]).toString(16)
	//	def hexPar3 = (registers[registerMap[par3]]).toString(16)
	//	println "STORE $par1:$hexPar1 $par2 $par3:$hexPar3"
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
	
	def sb = {par1, par2, par3, xml ->
		BigInteger valToStore = registers[registerMap[par1]]
		def baseAddress = registers[registerMap[par3]]
		def writeAddress = baseAddress + par2.toInteger()
		memory[writeAddress] = valToStore & 0xFF
		def hexWriteAddress = "0x" + writeAddress.toString(16)
		xml.store(address:hexWriteAddress, size:1)
	}
	
	def storeUpdates = ["sd":sd, "sw":sw, "sb":sb]

	def ld = {par1, par2, par3, xml ->
		def baseAddress = registers[registerMap[par3]]
		def readAddress = baseAddress + par2.toInteger()
		BigInteger sum = 0
		(0 .. 7).each { offset ->
			sum += 
				(memory[readAddress + offset]).shiftLeft(offset * 8)
		}
		registers[registerMap[par1]] = sum
		def hexReadAddress = "0x" + readAddress.toString(16)
	//	def hexPar1 = (registers[registerMap[par1]]).toString(16)
	//	def hexPar3 = (registers[registerMap[par3]]).toString(16)
	//	println "LOAD $par1:$hexPar1 $par2 $par3:$hexPar3"
		xml.load(address:hexReadAddress, size:8)
	}
	
	def lw = {par1, par2, par3, xml ->

		def hexPar1 = (registers[registerMap[par1]]).toString(16)
		def hexPar3 = (registers[registerMap[par3]]).toString(16)
		println "LOAD $par1:$hexPar1 $par2 $par3:$hexPar3"
		def baseAddress = registers[registerMap[par3]]
		def readAddress = baseAddress + par2.toInteger()
		BigInteger sum = 0
		(0 .. 3).each { offset ->
			try {
				sum +=
					(memory[readAddress + offset]).shiftLeft(offset * 8)
			}
			catch (NullPointerException e) {
				println "$readAddress $offset $par1 $par2 $par3"
			}
			
		}
		registers[registerMap[par1]] = sum
		def hexReadAddress = "0x" + readAddress.toString(16)
		xml.load(address:hexReadAddress, size:4)
	}
	
	def loadUpdates = ["ld":ld, "lw":lw]
		
	def jal = {ig1, ig2 ->
		registers[registerMap["ra"]] = pc + 4
	}

	def jumpUpdates = ["jal":jal]
	
	RegisterFile(def startPCVal)
	{
		pc = startPCVal
		def regVal = 0
		registerMap["zero"] = regVal++	//hard-wired zero 		x0
		registerMap["ra"] = regVal++ 	//return address		x1
		registerMap["sp"] = regVal++	//stack pointer			x2
		registerMap["gp"] = regVal++	//global pointer		x3
		registerMap["tp"] = regVal++	//thread pointer		x4
		registerMap["t0"] = regVal++	//temporaries			x5
		registerMap["t1"] = regVal++	//						x6
		registerMap["t2"] = regVal++	//						x7
		registerMap["s0"] = regVal		//saved register		x8
		registerMap["fp"] = regVal++	//also frame pointer	x8
		registerMap["s1"] = regVal++	//						x9
		registerMap["a0"] = regVal++	//arguments				x10				
		registerMap["a1"] = regVal++	//and returns			x11
		registerMap["a2"] = regVal++	//just arguments		x12
		registerMap["a3"] = regVal++	//						x13
		registerMap["a4"] = regVal++	//						x14
		registerMap["a5"] = regVal++	//						x15
		registerMap["a6"] = regVal++	//						x16
		registerMap["a7"] = regVal++	//						x17
		registerMap["s2"] = regVal++	//						x18
		registerMap["s3"] = regVal++	//						x19
		registerMap["s4"] = regVal++	//						x20
		registerMap["s5"] = regVal++	//						x21
		registerMap["s6"] = regVal++	//						x22
		registerMap["s7"] = regVal++	//						x23
		registerMap["s8"] = regVal++	//						x24
		registerMap["s9"] = regVal++	//						x25
		registerMap["s10"] = regVal++	//						x26
		registerMap["s11"] = regVal++	//						x27
		registerMap["t3"] = regVal++	//						x28
		registerMap["t4"] = regVal++	//						x29
		registerMap["t5"] = regVal++	//						x30
		registerMap["t6"] = regVal++	//						x31
		
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
	
	public unmangleLoad(def lineIn, def xml)
	{
		lineIn.find(
			/(.+):\s*(\w+)\s+\((\w+)\)\s*(\S*)\s*(\w*),\s*(-?\w*)\((\w*)\)/,
			{match, op1, op2, op3, op4, op5, op6, op7 ->
			//	println "store with $op4 and parameters $op5, $op6, $op7"
				(loadUpdates.find {it.key == op4}.value).call(op5, op6,
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
