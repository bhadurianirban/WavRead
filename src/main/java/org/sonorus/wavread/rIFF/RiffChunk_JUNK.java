package org.sonorus.wavread.rIFF;

import java.nio.ByteBuffer;

/**
 * Whatcha gonna do with all that JUNK, all that JUNK inside your chunk?<br>
 * The JUNK chunk is typically non-data i.e. all-zeroes, used for block alignment in some files, like with 2048 blocksize in CDs.<br>
 * In case it is not empty, its contents can be read. Who knows what fun little goodies could be in there...
 * @author chuck
 *
 */

public class RiffChunk_JUNK extends RiffChunk
	{
	byte [] junkData;
	
	@Override
	public void fromData(ByteBuffer fileBuffer)
		{
		int length = (int)RiffChunk.readUnsignedInt(fileBuffer);
		junkData = new byte[length];
		fileBuffer.get(junkData);
		}

	@Override
	public void _toData(ByteBuffer buffer)
		{
		buffer.putInt(junkData.length);
		buffer.put(junkData);
		}

	@Override
	public int _sizeEstimateInBytes()
		{
		return 4+junkData.length;
		}

	/**
	 * JUNK data should be non-data but it doesn't have to be.
	 * @return the junkData
	 */
	public byte[] getJunkData()
		{
		return junkData;
		}

	/**
	 * JUNK data should be non-data but it doesn't have to be.
	 * @param junkData the junkData to set
	 */
	public void setJunkData(byte[] junkData)
		{
		this.junkData = junkData;
		}

	}//end RiffChunk_JUNK
