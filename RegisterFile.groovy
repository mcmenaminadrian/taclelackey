package taclelackey

class RegisterFile {

	BigInteger pc
	def registers = []
	def registerMap = [:]
	def memory = [:]
	def maxRegVal
	
	def auipc = {par1, par2, ig1 ->
		int addValue = Integer.parseInt(par2.stripIndent(2), 16)
		long lAddValue = (addValue ^ 0x80000) - 0x80000
		BigInteger result = pc + (lAddValue << 12) 
		registers[registerMap[par1]] = result
	}
	
	def addi = {par1, par2, par3 ->
		long tempA = par3.toLong()
		long tempB = registers[registerMap[par2]]
		long result = tempA + tempB
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}
	
	def add = {par1, par2, par3 ->
		long tempA = registers[registerMap[par3]]
		long tempB = registers[registerMap[par2]]
		long result = tempA + tempB
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}
	
	def addiw = {par1, par2, par3 ->
		int tempA = registers[registerMap[par2]]
		int tempB = par3.toLong()
		int result = tempA + tempB
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}
	
	def csr_rw = {par1, par2, ig1 ->
		registers[registerMap[par1]] = registers[registerMap[par2]]
	}
	
	def li = {par1, par2, ig1 ->
		BigInteger result = par2.toLong()
		registers[registerMap[par1]] = result
	}
	
	def lui = {par1, par2, ig1 ->
		int addValue = Integer.parseInt(par2.stripIndent(2), 16)
		long lAddValue = (addValue ^ 0x80000) - 0x80000
		BigInteger bResult = lAddValue << 12
		registers[registerMap[par1]] = bResult
		
	}
	
	def csr_or = {par1, par2, ig1 ->
		registers[registerMap[par1]] |= registers[registerMap[par2]]
	}
	
	def andi = {par1, par2, par3 ->
		BigInteger addValue = par3.toLong()
		registers[registerMap[par1]] =
			registers[registerMap[par2]].and(addValue)
		}
	
	def slli = {par1, par2, par3 ->
		long tempB = par3.toLong()
		long tempA = registers[registerMap[par2]]
		long result = tempA << tempB
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}
	
	def sllw = {par1, par2, par3 ->
		int tempA = registers[registerMap[par2]]
		int tempB = registers[registerMap[par3]] & 0x3F
		int result = tempA << tempB
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}
	
	def slliw = {par1, par2, par3 ->
		int tempA = registers[registerMap[par2]]
		int tempB = par3.toInteger()
		int result = tempA << tempB
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}
	
	def srli = {par1, par2, par3 ->
		long tempA = registers[registerMap[par2]]
		long tempB = par3.toLong()
		long result = tempA >>> tempB
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}
	
	def mv = {par1, par2, ig1 ->
		registers[registerMap[par1]] = registers[registerMap[par2]]
	}
	
	def sub = {par1, par2, par3 ->
		long tempA = registers[registerMap[par2]]
		long tempB = registers[registerMap[par3]]
		long result = tempA - tempB
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}	
	
	def subw = {par1, par2, par3 ->
		int tempA = registers[registerMap[par2]]
		int tempB = registers[registerMap[par3]]
		int result = tempA - tempB
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}
	
	def or = {par1, par2, par3 ->
		long tempA = registers[registerMap[par2]]
		long tempB = registers[registerMap[par3]]
		long result = tempA | tempB
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult 
	}
	
	def mulw = {par1, par2, par3 ->
		int tempA = registers[registerMap[par2]]
		int tempB = registers[registerMap[par3]]
		int result = tempA * tempB
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}
	
	def mul = {par1, par2, par3 ->
		long tempA = registers[registerMap[par2]]
		long tempB = registers[registerMap[par3]]
		long result = tempA * tempB
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}
	
	def divw = {par1, par2, par3 ->
		int tempA = registers[registerMap[par2]]
		int tempB = registers[registerMap[par3]]
		int result = tempA / tempB
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}
	
	def addw = {par1, par2, par3 ->
		int tempA = registers[registerMap[par2]]
		int tempB = registers[registerMap[par3]]
		int result = tempA + tempB
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}
	
	def srliw = {par1, par2, par3 ->
		int tempA = registers[registerMap[par2]]
		int tempB = par3.toInteger() 
		int result = tempA >>> tempB
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}
	
	def srlw = {par1, par2, par3 ->
		int tempA = registers[registerMap[par2]]
		int tempB = registers[registerMap[par3]]
		int result = tempA >>> tempB
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}
	
	def sraiw = {par1, par2, par3 ->
		int tempA = registers[registerMap[par2]]
		int tempB = par3.toInteger()
		int result = tempA >> tempB
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}
	
	def srai = {par1, par2, par3 ->
		long tempA = registers[registerMap[par2]]
		long tempB = par3.toLong()
		long result = tempA >> tempB
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}
	
	def sraw = {par1, par2, par3 ->
		int tempA = registers[registerMap[par2]]
		int tempB = registers[registerMap[par3]]
		int result = tempA >> tempB
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}
	
	def sextw = {par1, par2, ig1 ->
		int tempA = registers[registerMap[par2]]
		long result = tempA.longValue()
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}
	
	def snez = {par1, par2, ig1 ->
		if (registers[registerMap[par2]] != 0) {
			registers[registerMap[par1]] = new BigInteger(1)
		} else {
			registers[registerMap[par1]] = new BigInteger(0)
		}
	}
	
	def not = {par1, par2, ig1 ->
		long tempA = registers[registerMap[par2]]
		long result = tempA ^ 0xFFFFFFFFFFFFFFFF
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}
	
	def and = {par1, par2, par3 ->
		long tempA = registers[registerMap[par2]]
		long tempB = registers[registerMap[par3]]
		long result = tempA & tempB
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}
	
	def seqz = {par1, par2, ig1 ->
		if (registers[registerMap[par2]] == 0) {
			registers[registerMap[par1]] = new BigInteger(1)
		} else {
			registers[registerMap[par1]] = new BigInteger(0)
		}
	}
	
	def sltiu = {par1, par2, par3 ->
		if (registers[registerMap[par2]] < par3.toInteger()) {
			registers[registerMap[par1]] = new BigInteger(1)
		} else {
			registers[registerMap[par1]] = new BigInteger(0)
		}
	}
	
	def slti = {par1, par2, par3 ->
		if (registers[registerMap[par2]] < par3.toInteger()) {
			registers[registerMap[par1]] = new BigInteger(1)
		} else {
			registers[registerMap[par1]] = new BigInteger(0)
		}
	}
	
	def slt = {par1, par2, par3 ->
		if (registers[registerMap[par2]] < 
			registers[registerMap[par3]]) {
			registers[registerMap[par1]] = new BigInteger(1)
		} else {
			registers[registerMap[par1]] = new BigInteger(0)
		}
	}
	
	def xori = {par1, par2, par3 ->
		long tempA = registers[registerMap[par2]]
		int tempB = par3.toInteger()
		long tempC = (tempB ^ 0x800) - 0x800
		long result = tempA ^ tempC
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}
	
	def div = {par1, par2, par3 ->
		long tempA = registers[registerMap[par2]]
		long tempB = registers[registerMap[par3]]
		long result = tempA / tempB
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult 
	}
	
	def xor = {par1, par2, par3 ->
		long tempA = registers[registerMap[par2]]
		long tempB = registers[registerMap[par3]]
		long result = tempA ^ tempB
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}
			
	
	def rem = {par1, par2, par3 ->
		if (registers[registerMap[par3]] == 0) {
			registers[registerMap[par1]] = registers[registerMap[par2]]
		} else {
			long tempA = registers[registerMap[par2]]
			long tempB = registers[registerMap[par3]]
			long result = tempA % tempB
			BigInteger bResult = result
			registers[registerMap[par1]] = bResult
		}
	}
	
	def remu = {par1, par2, par3 ->
		if (registers[registerMap[par3]] == 0) {
			registers[registerMap[par1]] = registers[registerMap[par2]]
		} else {
			registers[registerMap[par1]] =
				registers[registerMap[par2]] % (
					registers[registerMap[par3]])
				if (registers[registerMap[par1]] < 0) {
					registers[registerMap[par1]] *= -1
				}
		}
	}
	
	def remw = {par1, par2, par3 ->
		int tempA = registers[registerMap[par3]]
		if (tempA == 0) {
			registers[registerMap[par1]] = 0
			return
		}
		int tempB = registers[registerMap[par2]]
		registers[registerMap[par1]] = tempB % tempA
	}
	
	def divu = { par1, par2, par3 ->
		long tempA = registers[registerMap[par2]]
		long tempB = registers[registerMap[par3]]
		if (tempB == 0) {
			return;
		}
		if (tempA < 0) {
			tempA *= -1
		}
		if (tempB < 0) {
			tempB *= -1
		}
		long result = tempA / tempB
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}
	
	def divuw = {par1, par2, par3 ->
		int tempA = registers[registerMap[par2]]
		int tempB = registers[registerMap[par3]]
		if (tempB == 0) {
			return;
		}
		if (tempA < 0) {
			tempA *= -1
		}
		if (tempB < 0) {
			tempB *= -1
		}
		int result = tempA / tempB
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}
	
	def ori = {par1, par2, par3 ->
		long tempA = registers[registerMap[par2]]
		int tempB = par3.toInteger()
		long tempC = (tempB ^ 0x800) - 0x800
		long result = tempA | tempC
		BigInteger bResult = result
		registers[registerMap[par1]] = bResult
	}

	
	def stateUpdates = ["auipc":auipc, "addi":addi, "csrw": csr_rw, "li":li,
		"lui": lui, "csrs": csr_or, "csrr":csr_rw, "andi": andi,
		"fmv.s.x": csr_rw, "add": add, "slli": slli, "mv":mv,
		"srli":srli, "sub":sub, "or":or, "addiw":addiw,
		"mulw": mulw, "subw": subw, "slliw":slliw, "divw":divw,
		"addw": addw, "srliw": srliw, "sraiw": sraiw, "mul": mul, "srai":srai,
		"sext.w": sextw, "sraw": sraw, "snez": snez, "not":not, "sllw": sllw,
		"and": and, "seqz": seqz, "sltiu": sltiu, "xori": xori, "div": div,
		"xor": xor, "rem": rem, "remu": remu, "divu": divu, "remw": remw,
		"srlw": srlw, "slt": slt, "slti": slti, "divuw": divuw, "ori": ori]
	
	
	def fsgnjs = { par1, par2, par3 ->
		def signOut = Math.signum(registers[registerMap[par3]])
		registers[registerMap[par1]] =
			Math.copySign(registers[registerMap[par2]], signOut)
	}
	
	def fsgnjns = { par1, par2, par3 ->
		def signOut = Math.signum(registers[registerMap[par3]]) * -1.0
		registers[registerMap[par1]] =
			Math.copySign(registers[registerMap[par2]], signOut)
	}
	
	def fcvtds = {par1, par2, ig1 ->
		double tempA = registers[registerMap[par2]]
		registers[registerMap[par1]] = tempA
	}
	
	def fcvtdw = {par1, par2, ig1 ->
		int tempA = registers[registerMap[par2]]
		double result = tempA
		registers[registerMap[par1]] = result
	}
	
	def fcvtsd = {par1, par2, ig1 ->
		float tempA = registers[registerMap[par2]]
		registers[registerMap[par1]] = tempA
	}
	
	def fcvtsw = {par1, par2, ig1 ->
		float tempA = registers[registerMap[par2]]
		registers[registerMap[par1]] = tempA
	}
	
	def fmuld = {par1, par2, par3 ->
		Double tempA = registers[registerMap[par3]]
		Double tempB = registers[registerMap[par2]]
		Double result = tempA * tempB
		registers[registerMap[par1]] = result
	}
	
	def fmuls = {par1, par2, par3 ->
		float tempA = registers[registerMap[par3]]
		float tempB = registers[registerMap[par2]]
		float result = tempA * tempB
		registers[registerMap[par1]] = result
	}
	
	def fdivd = {par1, par2, par3 ->
		Double tempA = registers[registerMap[par3]]
		Double tempB = registers[registerMap[par2]]
		Double result = tempA / tempB
		registers[registerMap[par1]] = result
	}
	
	def fsubs = {par1, par2, par3 ->
		float tempA = registers[registerMap[par2]]
		float tempB = registers[registerMap[par3]]
		float result = tempA - tempB
		registers[registerMap[par1]] = result
	}
	
	def fdivs = {par1, par2, par3 ->
		float tempA = registers[registerMap[par3]]
		float tempB = registers[registerMap[par2]]
		float result = tempA / tempB
		registers[registerMap[par1]] = result
	}
	
	def feqs = {par1, par2, ig1 ->
		float tempA = registers[registerMap[par2]]
		if (tempA == 0.0) {
			registers[registerMap[par1]] = 1
		} else {
			registers[registerMap[par1]] = 0
		}
	}
	
	def fadds = {par1, par2, par3 ->
		float tempA = registers[registerMap[par3]]
		float tempB = registers[registerMap[par2]]
		float result = tempA + tempB
		registers[registerMap[par1]] = result
	}
	
	def fcvwts = {par1, par2, ig1 ->
		float tempA = registers[registerMap[par2]]
		int result = tempA
		registers[registerMap[par1]] = result
	}
	
	def stateFPUpdates = ["fsgnj.s": fsgnjs, "fcvt.d.s": fcvtds,
		"fmul.d": fmuld, "fdiv.d": fdivd, "fcvt.s.d": fcvtsd,
		"fcvt.s.w": fcvtsw, "fsub.s": fsubs, "fdiv.s": fdivs,
		"feq.s": feqs, "fmul.s": fmuls, "fadd.s": fadds,
		"fcvt.w.s": fcvwts, "fsgnjn.s": fsgnjns, "fcvt.d.w": fcvtdw]

	//Everything that follows should be BIGENDIAN
	
	def sd = {par1, par2, par3, xml ->
		long valToStore = registers[registerMap[par1]]
		BigInteger baseAddress = registers[registerMap[par3]]
		BigInteger writeAddress = baseAddress + par2.toLong()
		def mRange = 7
		(0 .. mRange).each {offset ->
			byte partialResult =
				(valToStore >>> ((mRange - offset) * 8)) & 0xFF
			memory[writeAddress + offset] = partialResult
		}
		def hexWriteAddress = "0x" + writeAddress.toString(16)
		xml.store(address:hexWriteAddress, size:8)
		
	}
	
	def sw = {par1, par2, par3, xml ->
		long valToStore = registers[registerMap[par1]]
		BigInteger baseAddress = registers[registerMap[par3]]
		BigInteger writeAddress = baseAddress + par2.toInteger()
		def mRange = 3
		(0 .. mRange).each {offset ->
			byte partialResult =
				(valToStore >>> ((mRange - offset) * 8)) & 0xFF
			memory[writeAddress + offset] = partialResult
		}
		def hexWriteAddress = "0x" + writeAddress.toString(16)
		xml.store(address:hexWriteAddress, size:4)
	}
	
	def sb = {par1, par2, par3, xml ->
		long valToStore = registers[registerMap[par1]]
		BigInteger baseAddress = registers[registerMap[par3]]
		BigInteger writeAddress = baseAddress + par2.toInteger()
		byte storeThis = valToStore & 0xFF
		memory[writeAddress] = storeThis
		def hexWriteAddress = "0x" + writeAddress.toString(16)
		xml.store(address:hexWriteAddress, size:1)
	}
	
	def fsd = {par1, par2, par3, xml ->
		Double valToStore = registers[registerMap[par1]]
		BigInteger baseAddress = registers[registerMap[par3]]
		BigInteger writeAddress = baseAddress + par2.toInteger()
		def mRange = 7
		(0 .. mRange).each {offset ->
			memory[writeAddress + offset] =
				(Double.doubleToLongBits(valToStore) >>>
					((mRange - offset) * 8)) & 0xFF
		}
		def hexWriteAddress = "0x" + writeAddress.toString(16)
		xml.store(address:hexWriteAddress, size:8)
	}
	
	def fsw = {par1, par2, par3, xml ->
		float valToStore = registers[registerMap[par1]]
		BigInteger baseAddress = registers[registerMap[par3]]
		BigInteger writeAddress = baseAddress + par2.toInteger()
		def mRange = 3
		(0 .. mRange).each {offset ->
			memory[writeAddress + offset] =
				(Float.floatToIntBits(valToStore) >>>
					((mRange - offset) * 8)) & 0xFF
		}
		def hexWriteAddress = "0x" + writeAddress.toString(16)
		xml.store(address:hexWriteAddress, size:4)
	}
	
	def storeUpdates = ["sd":sd, "sw":sw, "sb":sb, "fsd": fsd, "fsw": fsw]

	def ld = {par1, par2, par3, xml ->
		BigInteger baseAddress = registers[registerMap[par3]]
		BigInteger readAddress = baseAddress + par2.toInteger()
		BitSet sum = new BitSet(64) 
		def mRange = 7
		(mRange .. 0).each { offset ->
			try {
				byte readByte = memory[readAddress + offset]
				if (readByte != 0) {
					for (int i = 0; i < 8; i++) {
						byte testByte = (readByte & 0xFF) >>> i
						if (testByte == 0)
							break;
						if (testByte & 0x01) {
							sum.set((mRange - offset) * 8 + i)
						}
					}
				}
			}
			catch (Exception e) {
				System.err.println "EXCEPTION!!!! ${readAddress} $par1 $par2 $par3"
				memory[readAddress + offset] = 0
			}
		}
		if (sum.isEmpty()) {
			registers[registerMap[par1]] = 0
		} else {
			registers[registerMap[par1]] = sum.toLongArray()[0]
		}
		def hexReadAddress = "0x" + readAddress.toString(16)
		xml.load(address:hexReadAddress, size:8)
	}
	
	def lw = {par1, par2, par3, xml ->
		BigInteger baseAddress = registers[registerMap[par3]]
		BigInteger readAddress = baseAddress + par2.toInteger()
		BitSet sum = new BitSet(64) 
		def mRange = 3
		(mRange .. 0).each { offset ->
			try {
				byte readByte = memory[readAddress + offset]
				if (readByte != 0) {
					for (int i = 0; i < 8; i++) {
						byte testByte = (readByte & 0xFF) >>> i
						if (testByte == 0)
							break;
						if (testByte & 0x01) {
							sum.set((mRange - offset) * 8 + i)
						}
					}
				}
			}
			catch (Exception e) {
				System.err.println "EXCEPTION!!!! ${readAddress} $par1 $par2 $par3"
				memory[readAddress + offset] = 0
			}
		}
		if (sum.isEmpty()) {
			registers[registerMap[par1]] = 0
		} else {
			int result = (sum.toLongArray()[0]) & 0xFFFFFFFF
			registers[registerMap[par1]] = result
		}
		def hexReadAddress = "0x" + readAddress.toString(16)
		xml.load(address:hexReadAddress, size:4)
	}
	
	def lbu = {par1, par2, par3, xml ->
		BigInteger baseAddress = registers[registerMap[par3]]
		BigInteger readAddress = baseAddress + par2.toInteger()
		BigInteger numb = 0
		try {
			byte readIn = memory[readAddress]
			numb |= (readIn & 0xFF)
		}
		catch (Exception e) {
			System.err.println "EXCEPTION!!! lbu fail at $readAddress"
			memory[readAddress] = 0
			numb = 0
		}
		registers[registerMap[par1]] = numb.and(0xFFFFFFFFFFFFFFFF)
		def hexReadAddress = "0x" + readAddress.toString(16)
		xml.load(address:hexReadAddress, size:1)
	}
	
	def lb = {par1, par2, par3, xml ->
		BigInteger baseAddress = registers[registerMap[par3]]
		BigInteger readAddress = baseAddress + par2.toInteger()
		BigInteger numb = 0
		try {
			byte readIn = memory[readAddress]
			registers[registerMap[par1]] = (readIn & 0xFFFFFFFF)
		}
		catch (Exception e) {
			System.err.println "EXCEPTION!!! lb fail at $readAddress"
			numb = 0
			memory[readAddress] = 0
		}
		def hexReadAddress = "0x" + readAddress.toString(16)
		xml.load(address:hexReadAddress, size:1)
	}
	
	def lwu = {par1, par2, par3, xml ->
		BigInteger baseAddress = registers[registerMap[par3]]
		BigInteger readAddress = baseAddress + par2.toInteger()
		BitSet sum = new BitSet(64) 
		def mRange = 3
		(mRange .. 0).each { offset ->
			try {
				byte readByte = memory[readAddress + offset]
				if (readByte != 0) {
					for (int i = 0; i < 8; i++) {
						byte testByte = (readByte & 0xFF) >>> i
						if (testByte == 0)
							break;
						if (testByte & 0x01) {
							sum.set((mRange - offset) * 8 + i)
						}
					}
				}
			}
			catch (Exception e) {
				System.err.println "EXCEPTION!!!! ${readAddress} $par1 $par2 $par3"
				memory[readAddress + offset] = 0
			}
		}
		if (sum.isEmpty()) {
			registers[registerMap[par1]] = 0
		} else {
			registers[registerMap[par1]] = sum.toLongArray()[0]
		}
		def hexReadAddress = "0x" + readAddress.toString(16)
		xml.load(address:hexReadAddress, size:4)
	}
	
	def flw = {par1, par2, par3, xml ->
		BigInteger baseAddress = registers[registerMap[par3]]
		BigInteger readAddress = baseAddress + par2.toInteger()
		BitSet sum = new BitSet(64)
		def mRange = 3
		(mRange .. 0).each { offset ->
			try {
				byte readByte = memory[readAddress + offset]
				if (readByte != 0) {
					for (int i = 0; i < 8; i++) {
						byte testByte = (readByte & 0xFF) >>> i
						if (testByte == 0)
							break;
						if (testByte & 0x01) {
							sum.set((mRange - offset) * 8 + i)
						}
					}
				}
			}
			catch (Exception e) {
				System.err.println "EXCEPTION!!!! ${readAddress} $par1 $par2 $par3"
				memory[readAddress + offset] = 0
			}
		}
		if (sum.isEmpty()) {
			registers[registerMap[par1]] = new Float(0)
		} else {
			int pResult = sum.toLongArray()[0]
			registers[registerMap[par1]] =
				Float.intBitsToFloat(pResult)
		}
		def hexReadAddress = "0x" + readAddress.toString(16)
		xml.load(address:hexReadAddress, size:4)
	}
	
	
	def fld = {par1, par2, par3, xml ->
		BigInteger baseAddress = registers[registerMap[par3]]
		BigInteger readAddress = baseAddress + par2.toInteger()
		BitSet sum = new BitSet(64)
		def mRange = 7
		(mRange .. 0).each { offset ->
			try {
				byte readByte = memory[readAddress + offset]
				if (readByte != 0) {
					for (int i = 0; i < 8; i++) {
						byte testByte = (readByte & 0xFF) >>> i
						if (testByte == 0)
							break;
						if (testByte & 0x01) {
							sum.set((mRange - offset) * 8 + i)
						}
					}
				}
			}
			catch (Exception e) {
				System.err.println "EXCEPTION!!!! ${readAddress} $par1 $par2 $par3"
				memory[readAddress] = 0
			}
		}
		if (sum.isEmpty()) {
			registers[registerMap[par1]] = new Double(0)
		} else {
			registers[registerMap[par1]] = 
				Double.longBitsToDouble(sum.toLongArray()[0])
		}
		def hexReadAddress = "0x" + readAddress.toString(16)
		xml.load(address:hexReadAddress, size:8)
	}
	
	def loadUpdates = ["ld":ld, "lw":lw, "lbu": lbu, "lwu":lwu, "lb": lb,
		"flw": flw, "fld": fld]
		
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
			registers << new BigInteger(0)
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
	
	public unmangleFPStateUpdate(def lineIn)
	{
		lineIn.find (
			/(.+):\s*(\w+)\s+\((\w+)\)\s*(\S*)\s*(\w*),\s*([-\w]*),?\s?(\S*)?/,
			{match, op1, op2, op3, op4, op5, op6, op7 ->
				//println "state update with $op4 and paramters $op5, $op6, $op7"
				(stateFPUpdates.find {it.key == op4}.value).call(op5, op6, op7)
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
