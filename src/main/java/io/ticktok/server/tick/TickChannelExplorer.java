package io.ticktok.server.tick;

import io.ticktok.server.clock.Clock;

public interface TickChannelExplorer {
    boolean isExists(Clock clock);

    TickChannel create(Clock clock);

    void disable(Clock clock);

    void enable(Clock clock);
}
