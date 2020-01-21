package d.core.verticle;

import d.scene.SceneVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class MainVerticle extends AbstractVerticle {

	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		System.out.println("start");
		
		
		vertx.deployVerticle(new SceneVerticle(), asyncResult ->{
			if(asyncResult.succeeded()) {
				startPromise.complete();
			}
			else {
				startPromise.fail(asyncResult.cause());
			}
		});
		
	}
}
