package d.core;

import io.vertx.core.Launcher;
import io.vertx.core.VertxOptions;

public class MainLauncher extends Launcher {

	@Override
	public void beforeStartingVertx(VertxOptions options) {
		options.setWorkerPoolSize(1);
		super.beforeStartingVertx(options);
	}
}
