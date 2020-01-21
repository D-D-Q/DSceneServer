package d.scene;

import java.util.HashMap;
import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;
import io.vertx.core.net.SocketAddress;

public class SceneVerticle extends AbstractVerticle {

	private DatagramSocket socket = null;
	private Map<SocketAddress, SocketAddress> players = null;

	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		
		System.out.println("SceneVerticle star");
		
		players = new HashMap<>();

		DatagramSocketOptions datagramSocketOptions = new DatagramSocketOptions();
		datagramSocketOptions.setReusePort(true);

		socket = vertx.createDatagramSocket(datagramSocketOptions);

		socket.listen(1234, "127.0.0.1", asyncResult -> {
			if (asyncResult.succeeded()) {
				socket.handler(packet -> {

					SocketAddress sender = packet.sender();
					Buffer buffer = packet.data();

					String msg = buffer.toString();
					System.out.println(msg);
					if ("enter".equals(msg)) {
						System.out.println("new player enter" + sender);
						players.put(sender, sender);
					}
					else if ("exit".equals(msg)) {
						System.out.println("player exit" + sender);
						players.remove(sender);
					}
					
					broadcast(buffer);

				});
			} else {
				System.out.println("Listen failed" + asyncResult.cause());
			}
		});
		

		vertx.setPeriodic(3*1000, handler ->{
			Buffer buffer = Buffer.buffer("test pos");
			broadcast(buffer);
		});
		
		startPromise.complete();
	}
	
	public void broadcast(Buffer buffer) {
		
		for (SocketAddress player : players.keySet()) {
			socket.send(buffer, player.port(), player.host(), asyncResult -> {
				System.out.println("Send succeeded? " + asyncResult.succeeded());
			});
		}
	}

}
