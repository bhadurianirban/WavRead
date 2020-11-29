package org.sonorus.wavread.rIFF;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;


public class WavUtil {

    public static final void toByteArray(int value, int numBytes, byte[] dest, int off) {
        for (int i = 0; i < numBytes; i++) {
            dest[i + off] = (byte) (value >>> ((i + 2) * 8) & 0xff);
        }
    }

    public static final void flipEndian(byte[] original, byte[] output) {
        for (int i = 0; i < original.length; i++) {
            output[(original.length - 1) - i] = original[i];
        }
    }

    public static float[][] WAVToFloats(File input) throws IOException {
        RiffChunk riff = RiffRoot.readRIFF(input).get(0);
        return ((RiffChunk_WAVE) riff.getChildChunk(RiffChunk_WAVE.class)).getAudioAsFloats();
    }

    public static void floatsToWAV(float[][] buffer, File destFile, float sampleRate) throws IOException {
        RiffRoot root = new RiffRoot();
        RiffChunk_RIFF riffChunk = new RiffChunk_RIFF();
        RiffChunk_WAVE waveChunk = new RiffChunk_WAVE();
        RiffChunk_data dataChunk = new RiffChunk_data();
        RiffChunk_fmt_ fmtChunk = new RiffChunk_fmt_();
        fmtChunk.setAudioChannelCount(buffer[0].length);
        fmtChunk.setAudioFormatCode(1);// 1 for PCM
        fmtChunk.setBitsPerSample(16);
        fmtChunk.setBlockAlign(2 * buffer[0].length);
        fmtChunk.setByteRate((long) sampleRate * 2 * fmtChunk.getAudioChannelCount());
        fmtChunk.setSampleRate((long) sampleRate);
        fmtChunk.setSize(16);// 16 for PCM

        waveChunk.addChildChunk(dataChunk);
        waveChunk.addChildChunk(fmtChunk);
        riffChunk.addChildChunk(waveChunk);
        waveChunk.setAudioFromFloats(buffer);
        root.addChildChunk(riffChunk);
        FileOutputStream os = new FileOutputStream(destFile);
        FileChannel fc = os.getChannel();
        ByteBuffer fileBuffer = ByteBuffer.allocateDirect(root.sizeEstimateInBytes());//Limited to 3.5GB files
        fileBuffer.order(ByteOrder.LITTLE_ENDIAN);
        fileBuffer.rewind();
        try {
            root.toData(fileBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        fileBuffer.rewind();
        fc.write(fileBuffer);
        fc.close();
        os.flush();
        os.close();

    }

}
