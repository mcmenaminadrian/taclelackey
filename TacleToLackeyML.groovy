package taclelackey

import java.util.regex.Pattern


def tacleCli = new CliBuilder
	(usage: 'TacleToLackeyML [options]' )
tacleCli.i(longOpt: 'input', args: 1, 'input file - default is stdin')
tacleCli.o(longOpt: 'output', args: 1, 'output file - default is to stdout')
tacleCli.u(longOpt: 'usage', 'show usage')

def tacleParse = tacleCli.parse(args)
def writer
def reader
if (tacleParse.u) {
	tacleCli.usage()
} else {
	def inputFile = System.in
	def outputFile = System.out
	if (tacleParse.i) {
		inputFile = new File(tacleParse.i)
		reader = new BufferedReader(new FileReader(inputFile))
	} else {
		reader = new BufferedReader(new InputStreamReader(inputFile))
	}
	if (tacleParse.o) {
		outputFile = new File(tacleCli.o)
		writer = new BufferedWriter(new FileWriter(outputFile))
	}
	else {
		writer = new BufferedWriter(new OutputStreamWriter(outputFile))
	}
	
	//output header
	writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
	writer.write("<!DOCTYPE lackeyml [\n")
	def elStr = new String("<!ELEMENT lackeyml (application,")
	elStr += "(instruction|store|load|modify)*)>\n"
	writer.write(elStr)
	writer.write("<!ATTLIST lackeyml version CDATA #FIXED \"0.1\">")
	def attStr = new String("<!ATTLIST lackeyml xmlns CDATA #FIXED")
	attStr += " \"http://cartesianproduct.wordpress.com\">"
	writer.write(attStr)
	writer.write("<!ELEMENT application EMPTY>\n")
	writer.write("<!ATTLIST application command CDATA #REQUIRED>\n")
	writer.write("<!ELEMENT instruction EMPTY>\n")
	writer.write("<!ATTLIST instruction address CDATA #REQUIRED>\n")
	writer.write("<!ATTLIST instruction size CDATA #REQUIRED>\n")
	writer.write("<!ELEMENT modify EMPTY>\n")
	writer.write("<!ATTLIST modify address CDATA #REQUIRED>\n")
	writer.write("<!ATTLIST modify size CDATA #REQUIRED>\n")
	writer.write("<!ELEMENT store EMPTY>\n")
	writer.write("<!ATTLIST store address CDATA #REQUIRED>\n")
	writer.write("<!ATTLIST store size CDATA #REQUIRED>\n")
	writer.write("<!ELEMENT load EMPTY>\n")
	writer.write("<!ATTLIST load address CDATA #REQUIRED>\n")
	writer.write("<!ATTLIST load size CDATA #REQUIRED>\n")
	writer.write("]>\n")
	writer.write(
		"<lackeyml xmlns=\"http://cartesianproduct.wordpress.com\">\n")
	writer.flush()

	def matchCount = 0
	def xml = new groovy.xml.MarkupBuilder(writer)
	def registerState = new RegisterFile()
	def process = true
	reader.eachLine	{ lineIn, count ->
		lineIn.eachMatch( /(.+):\s*(\w+)\s+\((\w+)\)\s*(\S*)/, { matchup ->
				if (process == false) {
					return
				}
				//update PC and write out instruction xml
				registerState.pcUpdate(new BigInteger(
					matchup[2].stripIndent(2), 16))
				xml.instruction(address:matchup[2], size:4)
				//determine instruction type and need for further processing
				def instructionType = new ProcessedMnemonic(matchup[4])
				switch (instructionType.iType) {
					case "instruction": //no processing required
						break
					case "registers": //state update required
						registerState.unmangleStateUpdate(lineIn)
						break
					case "store":
						registerState.unmangleStore(lineIn, xml)
						break
					case "load":
						registerState.unmangleLoad(lineIn, xml)
						break
					case "jump":
						registerState.unmangleJump(lineIn)
						break
					case "fpRegisters":
						registerState.unmangleFPStateUpdate(lineIn)
						break
					case "done":
						process = false
						break
				}
			})
	}
	writer.write("\n</lackeyml>")
	writer.flush()
}
			
