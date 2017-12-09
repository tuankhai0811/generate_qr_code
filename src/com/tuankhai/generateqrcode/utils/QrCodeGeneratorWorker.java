package com.tuankhai.generateqrcode.utils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public final class QrCodeGeneratorWorker {
	
//	public static void main(String[] args) {
//		// Set up input stream and start loop
//		try (Scanner input = new Scanner(System.in, "US-ASCII")) {
//			input.useDelimiter("\r\n|\n|\r");
//			while (processCase(input));
//		}
//	}
	
	
	private static boolean processCase(Scanner input) {
		// Read data length or exit
		int length = input.nextInt();
		if (length == -1)
			return false;
		if (length > Short.MAX_VALUE)
			throw new RuntimeException();
		
		// Read data bytes
		boolean isAscii = true;
		byte[] data = new byte[length];
		for (int i = 0; i < data.length; i++) {
			int b = input.nextInt();
			if (b < 0 || b > 255)
				throw new RuntimeException();
			data[i] = (byte)b;
			isAscii &= b < 128;
		}
		
		// Read encoding parameters
		int errCorLvl  = input.nextInt();
		int minVersion = input.nextInt();
		int maxVersion = input.nextInt();
		int mask       = input.nextInt();
		int boostEcl   = input.nextInt();
		if (!(0 <= errCorLvl && errCorLvl <= 3) || !(-1 <= mask && mask <= 7) || (boostEcl >>> 1) != 0
				|| !(QrCode.MIN_VERSION <= minVersion && minVersion <= maxVersion && maxVersion <= QrCode.MAX_VERSION))
			throw new RuntimeException();
		
		// Make segments for encoding
		List<QrSegment> segs;
		if (isAscii)
			segs = QrSegment.makeSegments(new String(data, StandardCharsets.US_ASCII));
		else
			segs = Arrays.asList(QrSegment.makeBytes(data));
		
		
		try {  // Try to make QR Code symbol
			QrCode qr = QrCode.encodeSegments(segs, QrCode.Ecc.values()[errCorLvl], minVersion, maxVersion, mask, boostEcl != 0);
			// Print grid of modules
			System.out.println(qr.version);
			for (int y = 0; y < qr.size; y++) {
				for (int x = 0; x < qr.size; x++)
					System.out.println(qr.getModule(x, y) ? 1 : 0);
			}
			
		} catch (IllegalArgumentException e) {
			if (!e.getMessage().equals("Data too long"))
				throw e;
			System.out.println(-1);
		}
		System.out.flush();
		return true;
	}
	
}
