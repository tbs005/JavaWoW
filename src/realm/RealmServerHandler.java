package realm;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tools.BitTools;
import tools.HexTool;
import data.input.GenericSeekableLittleEndianAccessor;
import data.input.SeekableByteArrayStream;
import data.input.SeekableLittleEndianAccessor;
import data.output.LittleEndianWriterStream;

final class RealmServerHandler extends IoHandlerAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(RealmServerHandler.class);

	@Override
	public final void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		LOGGER.error(cause.getLocalizedMessage(), cause);
	}

	@Override
	public final void inputClosed(IoSession session) throws Exception {
		super.inputClosed(session);
	}

	@Override
	public final void messageReceived(IoSession session, Object msg) throws Exception {
		if (!(msg instanceof byte[])) { // wtf
			LOGGER.info("Received unknown object: {}", msg.getClass().getName());
			return;
		}
		SeekableLittleEndianAccessor slea = new GenericSeekableLittleEndianAccessor(new SeekableByteArrayStream((byte[]) msg));
		switch (slea.readByte()) {
			default: {
				LOGGER.info("Unhandled Packet: {}", slea.toString());
				break;
			}
		}
		//LOGGER.info("Received: {}", HexTool.toString((byte[]) msg));
	}

	@Override
	public final void messageSent(IoSession session, Object msg) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public final void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public final void sessionCreated(IoSession session) throws Exception {
		super.sessionClosed(session);
	}

	@Override
	public final void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public final void sessionOpened(IoSession session) throws Exception {
		// FIXME Realm connection is not working (probably this packet is wrong)
		LOGGER.info("IoSession opened with {}.", session.getRemoteAddress());
		// Send Authentication Challenge Packet:
		LittleEndianWriterStream lews = new LittleEndianWriterStream();
		lews.writeShort(0x01EC); // header
		lews.writeInt(1);
		Random r = new Random(1337);
		lews.writeInt(r.nextInt()); // _authSeed
		byte[] seed1 = new byte[16];
		byte[] seed2 = new byte[16];
		r.nextBytes(seed1);
		r.nextBytes(seed2);
		lews.write(seed1);
		lews.write(seed2);
		System.out.println(HexTool.toString(lews.toByteArray()));
		session.write(lews.toByteArray());
		/*
		ec01 0100 0000 [header] [1 - int]
		6507 a331 [_authSeed]
		2f1c 55d3 d88b f890 [seed1]
		9413 bdfd b09e b4cf
		7f65 c574 a355 3d9e [seed2]
		d72f 42f0 2eef cca6
		*/
		/*
		EC01 0100 0000
		8A28 F1A8
		B893 8A2C 1F35 72B0
		4C48 D5DF 5228 1EE2
		11AD 73F7 7F97 04E6
		7929 FFCF D796 B02C
		*/
	}
}