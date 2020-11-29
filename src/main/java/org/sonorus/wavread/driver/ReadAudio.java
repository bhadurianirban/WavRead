package org.sonorus.wavread.driver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import org.sonorus.wavread.rIFF.WavUtil;



public class ReadAudio {
	
	private File wavToRead;
	private String stripExtFname;
	private String outFname;
	private float wavData[][];

	

	public ReadAudio(File wavToRead) {
		this.wavToRead = wavToRead;
	}

	public void readAudioIntoText() throws IOException {
		this.wavData = readWavtoFloat(wavToRead);
		//String temp = "_text\\";
		Double[] superPosedData = superPoseChannels(wavData, 5);
		stripExtFname = stripExtension(wavToRead.getName());
		outFname = prepOutFilename(stripExtFname, ".csv");
		outFname = wavToRead.getAbsoluteFile().getParent()+File.separator+outFname;
		writeDoubleArrtoFile(outFname, null, superPosedData);
		//System.out.println("printing"+wavToRead.getAbsoluteFile().getParent());
	}
	

	public String getOutFname() {
		return outFname;
	}


	private static float[][] readWavtoFloat(File wavFileName) throws IOException {

		
			float wavArr[][] = WavUtil.WAVToFloats(wavFileName);

			return wavArr;
		

	}

	private static Double[] superPoseChannels(float[][] wavChannelData, int amplifyFactor) {
		Double superPosedWavForm[] = new Double[wavChannelData.length];
		for (int i = 0; i < wavChannelData.length; i++) {
			Double superPosedVal = 0.0;
			for (int channelCounter = 0; channelCounter < wavChannelData[i].length; channelCounter++) {

				superPosedVal = superPosedVal + wavChannelData[i][channelCounter];
			}
			superPosedWavForm[i] = superPosedVal * Math.pow(10, amplifyFactor);
		}
		return superPosedWavForm;
	}

	private static String stripExtension(String str) {
		if (str == null)
			return null;
		int pos = str.lastIndexOf(".");
		if (pos == -1)
			return str;
		return str.substring(0, pos);
	}

	private static String prepOutFilename(String fNamewithoutExt, String ext) {
		//System.out.println("outfile name " + fNamewithoutExt + ext);
		return fNamewithoutExt + ext;
	}

	private static void writeDoubleArrtoFile(String fname, String header, Double[] wavData) throws IOException {
		
			//System.out.println("Writing file to " + fname);
			File file = new File(fname);

			// if file does not exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(file.getAbsoluteFile()), "UTF8");

			BufferedWriter bw = new BufferedWriter(fw);

			if (header != null)
				bw.write(header + "\n");
			DecimalFormat df = new DecimalFormat("##########0.00000000");
			for (int i = 0; i < wavData.length; i++) {
				bw.write(df.format(wavData[i]) + "\n");
			}
			bw.close();
		

	}

}
