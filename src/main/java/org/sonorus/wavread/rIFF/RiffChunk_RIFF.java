package org.sonorus.wavread.rIFF;

import java.nio.ByteBuffer;

public class RiffChunk_RIFF extends RiffChunk {

    @Override
    public void fromData(ByteBuffer fileBuffer) {
        parseRiff(fileBuffer);
    }

    @Override
    public void _toData(ByteBuffer buffer) {
        //Payload size (not including this)
        buffer.putInt(this.childrenSizeEstimateInBytes());
    }

    @Override
    public int _sizeEstimateInBytes() {
        return 4;
    }
}//end RiffChunk_RIFF
