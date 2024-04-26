package se.bjurr.wiremockpact.wiremockpactextension.support;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

import java.util.concurrent.locks.ReentrantLock;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/** https://stackoverflow.com/questions/43282798/in-junit-5-how-to-run-code-before-all-tests */
public abstract class BaseSetupJunitExtension
    implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

  /** Gate keeper to prevent multiple Threads within the same routine */
  private static final ReentrantLock LOCK = new ReentrantLock();

  @Override
  public void beforeAll(final ExtensionContext context) throws Exception {
    LOCK.lock();
    try {
      // We need to use a unique key here, across all usages of this particular extension.
      final String uniqueKey = this.getClass().getName();
      final Object value = context.getRoot().getStore(GLOBAL).get(uniqueKey);
      if (value == null) {
        // First test container invocation.
        context.getRoot().getStore(GLOBAL).put(uniqueKey, this);
        this.setup();
      }
    } finally {
      // free the access
      LOCK.unlock();
    }
  }

  /**
   * Callback that is invoked <em>exactly once</em> before the start of <em>all</em> test
   * containers.
   */
  public abstract void setup();

  /**
   * Callback that is invoked <em>exactly once</em> after the end of <em>all</em> test containers.
   * Inherited from {@code CloseableResource}
   */
  @Override
  public abstract void close() throws Throwable;
}
