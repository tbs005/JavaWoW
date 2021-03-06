package com.github.javawow.data.output;

import java.io.ByteArrayOutputStream;

import com.github.javawow.tools.HexTool;

/**
 * @author Jon
 *
 */
public class LittleEndianWriterStream extends GenericLittleEndianWriter {
	private ByteArrayOutputStream baos;

	public LittleEndianWriterStream() {
		this(32);
	}

	public LittleEndianWriterStream(int size) {
		this.baos = new ByteArrayOutputStream(size);
		setByteOutputStream(new ByteArrayOutputByteStream(baos));
	}

	public final byte[] toByteArray() {
		return baos.toByteArray();
	}

	public final int size() {
		return baos.size();
	}

	@Override
	public final String toString() {
		return HexTool.toString(baos.toByteArray());
	}
}