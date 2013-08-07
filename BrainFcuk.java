package com.ken.fun;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class BrainFcuk {
  
	private static final int maxBuffer = 30000;

	public static void main(String args[]) {
		BrainFcuk instance = new BrainFcuk();
		if(args.length < 1) {
			System.out.println("Usage:  BrainFcuk program.bf");
			System.exit(1);
		}
		File file = new File(args[0]);
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line ;
			StringBuilder sb = new StringBuilder();
			while((line = br.readLine()) != null) {
				if(!line.startsWith("#")) {
					sb.append(line);
				}
			}
			br.close();
			fr.close();
			System.out.println("About to start!");
			instance.run(sb.toString());
			System.out.println();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Finished!");
		
	}

	private Map<Integer, Integer> computeJumps(String program) {
		Stack<Integer> stack = new Stack<Integer>();
		HashMap<Integer, Integer> ret = new HashMap<Integer, Integer>();

		int pc = 0;
		while (pc != program.length()) {
			String opcode = program.substring(pc, pc+1);
			if (opcode.equals("[")) {
				stack.add(pc);
			} else if (opcode.equals("]")) {
				int target = stack.pop();
				ret.put(target, pc);
				ret.put(pc, target);
			}
			pc++;
		}

		return ret;
	}

	private void run(String program) {
		byte[] buffer = new byte[maxBuffer];
		Map<Integer, Integer> jumpMap = computeJumps(program);

		int ptr = 0;
		int pc = 0;

		while (pc != program.length()) {
			String opcode = program.substring(pc, pc+1);
			//System.out.print(opcode);
			switch (opcode) {
			case ">":
				ptr++;
				if(ptr > buffer.length) {
					buffer = handleOverflow(buffer);
				}
				break;
			case "<":
				ptr--;
				break;
			case "+":
				buffer[ptr]++;
				break;
			case "-":
				buffer[ptr]--;
				break;
			case ".":
				System.out.print((char)(buffer[ptr % 255]));
				break;
			case "[":
				if (buffer[ptr] == 0 ) {
					pc = jumpMap.get(pc);
				}
				break;
			case "]":
				if(buffer[ptr] != 0) {
					pc = jumpMap.get(pc);
				}
			default:
				break;
			}
			pc++;
		}
	}
	
	private static byte[] handleOverflow(byte[] data) {
		byte [] newData = new byte[data.length+maxBuffer];
		for(int i = 0 ; i < data.length ; i++) {
			newData[i] = data[i];
		}
		return newData;
	}
}
